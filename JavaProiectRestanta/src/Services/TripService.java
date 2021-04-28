package Services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Entities.*;

public class TripService {
    private static TripService inst=null;
    private TripService(){}
    public static TripService getInst(){
        if(inst==null){
            inst=new TripService();
        }
        return inst;
    }

    public String showTrips() throws IOException {
        Audit.printQuery("showTrips",Thread.currentThread().getName());

        String result="";
        Manager manager=Manager.getInst();

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

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

                result+=trip+"\n";
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public void addTrip(Trip trip) throws IOException {
        Audit.printQuery("addTrip",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        manager.trips.add(trip);
        manager.updatePopularity(trip);

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="paorestanta";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="insert into trips (fromname,toname,cntpers,price,used) values("
                        +"'"+trip.getFrom().getName()+"'"
                        +",'"+trip.getTo().getName()+"'"
                        +","+trip.getCntPers()
                        +","+trip.getPrice()
                        +","+1+")";
            System.out.println(sqlQuery);
            statement.execute(sqlQuery);
            statement.close();
            connection.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
