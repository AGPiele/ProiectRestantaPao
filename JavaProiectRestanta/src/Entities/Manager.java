package Entities;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import Services.*;

public class Manager {
    private static Manager inst=null;
    private Manager(){}
    public static Manager getInst(){
        if(inst==null){
            inst=new Manager();
        }
        return inst;
    }

    public ArrayList<Destination> cities=new ArrayList<Destination>();
    public ArrayList<Route> routes=new ArrayList<Route>();
    public ArrayList<Trip> trips=new ArrayList<Trip>();
    public ArrayList<Traveller> travellers=new ArrayList<Traveller>();

    public void updatePopularity(Trip trip) throws IOException{
        if(trip.isUsed()==1){
            return;
        }

        int currPop=trip.getTo().getPopularity();
        double currPrice=trip.getTo().getAvgPricePerDay()*trip.getTo().getPopularity();

        int newPop=currPop+trip.getCntPers();
        double newAvg=(currPrice+trip.getPrice())/newPop;

        trip.getTo().setPopularity(newPop);
        trip.getTo().setAvgPricePerDay(newAvg);
        trip.setUsed(1);

        DestinationService destinationService=DestinationService.getInst();
        destinationService.editDestination(trip.getTo().getName(),
                trip.getTo().getName(),newPop,newAvg);
    }

    public void loadDestinations() throws IOException {
        Audit.printQuery("loadDestinations",Thread.currentThread().getName());

        cities.clear();
        String dbUrl="jdbc:mysql://localhost:3306/pao";
        String dbUser="root";
        String dbPass="root";

        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from destinations";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                Destination dest=new Destination(Integer.parseInt(rs.getString("popularity")),rs.getString("name"),Integer.parseInt(rs.getString("avgpriceperday")));
                this.cities.add(dest);
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void loadRoutes() throws  IOException{
        Audit.printQuery("loadRoutes",Thread.currentThread().getName());

        routes.clear();
        String dbUrl="jdbc:mysql://localhost:3306/pao";
        String dbUser="root";
        String dbPass="root";

        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from routes";
            ResultSet rs=statement.executeQuery(sqlQuery);

            int currIdx=0;
            while(rs.next()){
                String fDest=rs.getString("fdest");
                String sDest=rs.getString("sdest");
                double distance=Double.parseDouble(rs.getString("distance"));
                int time=Integer.parseInt(rs.getString("time"));
                double money=Double.parseDouble(rs.getString("money"));
                String rtype=rs.getString("rtype");

                int fIdx=this.getIndex(fDest);
                int sIdx=this.getIndex(sDest);

                Route route=null;

                if(rtype.equals("air")){
                    route=new AirRoute(fDest,sDest,distance,time,money);
                } else {
                    route=new GroundRoute(fDest,sDest,distance,time,money);
                }

                this.cities.get(fIdx).addRoute(route);
                this.cities.get(sIdx).addRoute(route);
                this.routes.add(route);
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void loadTrips() throws IOException{
        Audit.printQuery("loadTrips",Thread.currentThread().getName());

        Manager manager=Manager.getInst();

        String dbUrl="jdbc:mysql://localhost:3306/pao";
        String dbUser="root";
        String dbPass="root";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from trips";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                String fromName=rs.getString("fromname");
                String toName=rs.getString("toname");
                int cntPers=Integer.parseInt(rs.getString("cntpers"));
                double price=Double.parseDouble(rs.getString("price"));
                int used=Integer.parseInt(rs.getString("used"));

                int fromIdx=manager.getIndex(fromName);
                int toIdx=manager.getIndex(toName);

                Trip trip=new Trip(manager.cities.get(fromIdx),manager.cities.get(toIdx),cntPers,price,used);

                this.updatePopularity(trip);
                this.trips.add(trip);
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void loadTravellers() throws IOException{
        Audit.printQuery("loadTravellers",Thread.currentThread().getName());

        this.travellers.clear();
        String dbUrl="jdbc:mysql://localhost:3306/pao";
        String dbUser="root";
        String dbPass="root";

        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from travellers";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                String name=rs.getString("name");
                String uniqueCode=rs.getString("uniquecode");
                String city=rs.getString("city");
                String airfear=rs.getString("airfear");

                Traveller traveller=null;
                if(airfear.equals("yes")){
                    traveller=new AirFearTraveller(name,uniqueCode,city);
                } else {
                    traveller=new AllRoutesTraveller(name,uniqueCode,city);
                }
                this.travellers.add(traveller);
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void printRoutes(String cityName){
        int idx=getIndex(cityName);
        if(idx<0){
            System.out.println("City not found");
            return;
        }
        for(Route route:cities.get(idx).routes){
            String firstName=route.getfDest();
            String secondName=route.getsDest();

            System.out.println(firstName+"-"+secondName+": distance: "+route.getDistance()+" time "+route.getTime()+" money "+route.getMoneyCost());
        }
    }

    public void printRoutes(){
        for(Route route:this.routes){
            System.out.println(route);
        }
    }

    public void printDestinations(){
        for(Destination dest:cities){
            System.out.println(dest.toString());
        }
    }

    public int getIndex(String name){
        for(int i=0;i<this.cities.size();i++){
            if(this.cities.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public boolean canReach(String cityA, String cityB){
        LinkedList<String> que=new LinkedList<String>();
        Map<String,Boolean> visited=new HashMap<String,Boolean>();

        visited.put(cityB,false);
        visited.put(cityA,true);
        que.add(cityA);

        while(que.size()>0){
            String currentNode=que.getFirst();

            int currIdx=this.getIndex(currentNode);

            que.removeFirst();
            for(Route r:cities.get(currIdx).routes){
                String other="";
                if(r.getfDest().compareTo(this.cities.get(currIdx).getName())==0){
                    other=r.getsDest();
                } else {
                    other=r.getfDest();
                }

                if(visited.containsKey(other)==false || visited.get(other)==false){
                    visited.put(other,true);
                    que.add(other);
                }
            }
        }

        return visited.get(cityB);
    }

    public boolean canReachNoPlane(String cityA, String cityB){
        LinkedList<String> que=new LinkedList<String>();
        Map<String,Boolean> visited=new HashMap<String,Boolean>();

        visited.put(cityB,false);
        visited.put(cityA,true);
        que.add(cityA);

        while(que.size()>0){
            String currentNode=que.getFirst();

            int currIdx=this.getIndex(currentNode);

            que.removeFirst();
            for(Route r:cities.get(currIdx).routes){
                if (r instanceof AirRoute){
                    continue;
                }

                String other="";
                if(r.getfDest().compareTo(this.cities.get(currIdx).getName())==0){
                    other=r.getsDest();
                } else {
                    other=r.getfDest();
                }

                if(visited.containsKey(other)==false || visited.get(other)==false){
                    visited.put(other,true);
                    que.add(other);
                }
            }
        }

        return visited.get(cityB);
    }

}
