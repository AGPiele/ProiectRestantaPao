package Services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Entities.*;

public class RouteService {
    private static RouteService inst=null;
    private RouteService(){}
    public static RouteService getInst(){
        if(inst==null){
            inst=new RouteService();
        }
        return inst;
    }

    public String showRoutes()throws IOException {
        Audit.printQuery("showRoutes",Thread.currentThread().getName());

        String result="";
        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from routes";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                Route route=new Route(rs.getString("fDest"),rs.getString("sDest"),
                        Double.parseDouble(rs.getString("distance")),
                        Integer.parseInt(rs.getString("time")),
                        Double.parseDouble(rs.getString("money")));
                result+=route+"\n";
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public void addRoute(Route route) throws IOException{
        Audit.printQuery("addRoute",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        manager.routes.add(route);

        int idxA=manager.getIndex(route.getfDest());
        int idxB=manager.getIndex(route.getsDest());

        if(idxA<0 || idxB<0){
            System.out.println("Cities do not exist");
            return;
        }

        manager.cities.get(idxA).addRoute(route);
        manager.cities.get(idxB).addRoute(route);

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String rtype="";
            if(route instanceof AirRoute){
                rtype="air";
            } else {
                rtype="ground";
            }
            String sqlQuery="insert into routes (fDest,sDest,distance,time,money,rtype) values("+"'"+route.getfDest()
                    +"'"+","+"'"+route.getsDest()+"',"+route.getDistance()
                    +","+route.getTime()
                    +","+route.getMoneyCost()
                    +","+"'"+rtype+"'"
                    +")";
            System.out.println(sqlQuery);
            statement.execute(sqlQuery);
            statement.close();
            connection.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteRoute(Route route) throws IOException{
        Audit.printQuery("deleteRoute",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        manager.routes.remove(route);
        for(Destination dest: manager.cities){
            for(int i=0;i<dest.routes.size();i++){
                if(dest.routes.get(i).equals(route)){
                    dest.routes.remove(i);
                }
            }
        }

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="delete from routes where "
                    +"fDest="+"'"+route.getfDest()+"'"+" AND "
                    +"sDest="+"'"+route.getsDest()+"'"+" AND "
                    +"distance="+ route.getDistance()+" AND "
                    +"time="+route.getTime()+" AND "
                    +"money="+route.getMoneyCost();

            statement.execute(sqlQuery);
            statement.close();
            connection.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void editRoute(Route route,double newDistance,int newTime,double newMoney) throws IOException{
        Audit.printQuery("editRoute",Thread.currentThread().getName());

        Manager manager=Manager.getInst();

        for(Route r: manager.routes){
            if(r.equals(route)){
                r.setDistance(newDistance);
                r.setTime(newTime);
                r.setMoneyCost(newMoney);
            }
        }

        for(Destination dest: manager.cities){
            for(Route r:dest.routes){
                if(r.equals(route)){
                    r.setDistance(newDistance);
                    r.setTime(newTime);
                    r.setMoneyCost(newMoney);
                }
            }
        }

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";
        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="update routes set distance="+newDistance
                    +",time="+newTime
                    +",money="+newMoney
                    +" where fDest="+"'"+route.getfDest()+"'"
                    +" AND sDest="+"'"+route.getsDest()+"'"
                    +" AND distance="+route.getDistance()
                    +" AND time="+route.getTime()
                    +" AND money="+route.getMoneyCost()
                    ;
            System.out.println(sqlQuery);
            statement.execute(sqlQuery);
        } catch (Exception exception){
            System.out.println(exception);
        }
    }
}
