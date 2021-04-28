package Services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Entities.*;

public class DestinationService {
    private static DestinationService inst=null;
    private DestinationService(){}
    public static DestinationService getInst(){
        if(inst==null){
            inst=new DestinationService();
        }
        return inst;
    }

    public String showDestinations()throws IOException {
        Audit.printQuery("showDestinations",Thread.currentThread().getName());

        String result="";

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from destinations";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                Destination dest=new Destination(Integer.parseInt(rs.getString("popularity")),rs.getString("name"),Integer.parseInt(rs.getString("avgpriceperday")));
                result+=dest+"\n";
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public void addDestination(Destination dest)throws IOException{
        Audit.printQuery("addDestination",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        for(Destination d: manager.cities){
            if(d.getName().equals(dest.getName())==true){
                System.out.println("Destination already added");
                return;
            }
        }

        manager.cities.add(dest);

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="insert into destinations (popularity,name,avgpriceperday) values("+dest.getPopularity()+",'"+dest.getName()+"',"+dest.getAvgPricePerDay()+")";
            System.out.println(sqlQuery);
            statement.execute(sqlQuery);
            statement.close();
            connection.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteDestination(Destination dest) throws IOException{
        Audit.printQuery("deleteDestination",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        RouteService routeService=RouteService.getInst();

        String currName=dest.getName();
        int idx=manager.getIndex(currName);

        manager.cities.remove(idx);
        for(Route r: manager.routes){
            if(r.getfDest().equals(dest.getName()) || r.getsDest().equals(dest.getName())){
                routeService.deleteRoute(r);
            }
        }

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";
        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="delete from destinations where name='"+currName+"'";
            statement.execute(sqlQuery);
        } catch (Exception exception){
            System.out.println(exception);
        }
    }

    public void editDestination(String name,String newName,int newPop,double newPrice)throws IOException{
        Audit.printQuery("editDestination",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        int idx=manager.getIndex(name);

        manager.cities.get(idx).setName(newName);
        manager.cities.get(idx).setPopularity(newPop);
        manager.cities.get(idx).setAvgPricePerDay(newPrice);

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";
        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="update destinations set name="+"'"+newName+"'"
                    +",popularity="+newPop
                    +",avgpriceperday="+newPrice
                    +" where name="+"'"+name+"'";
            statement.execute(sqlQuery);
        } catch (Exception exception){
            System.out.println(exception);
        }
    }
}
