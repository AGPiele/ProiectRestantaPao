package Services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import Entities.*;

public class TravellerService {
    private static TravellerService inst=null;
    private TravellerService(){}
    public static TravellerService getInst(){
        if(inst==null){
            inst=new TravellerService();
        }
        return inst;
    }

    public String showTravellers() throws IOException {
        Audit.printQuery("showTravellers",Thread.currentThread().getName());

        String result="";
        Manager manager=Manager.getInst();

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select * from travellers";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                String name=rs.getString("name");
                String uniqueCode=rs.getString("uniquecode");
                String city=rs.getString("city");
                String fear=rs.getString("airfear");

                Traveller traveller=null;
                if(fear=="yes"){
                    traveller=new AirFearTraveller(name,uniqueCode,city);
                } else {
                    traveller=new AllRoutesTraveller(name,uniqueCode,city);
                }

                result+=traveller.getName()+" "+traveller.getUniqueCode()+" "+traveller.getCity()+"\n";
            }
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public void addTraveller(Traveller traveller) throws IOException {
        Audit.printQuery("addTraveller",Thread.currentThread().getName());

        Manager manager=Manager.getInst();

        for(Traveller t: manager.travellers){
            if(t.getUniqueCode().equals(traveller.getUniqueCode())==true){
                System.out.println("Already added in database");
                return;
            }
        }

        manager.travellers.add(traveller);
        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";

        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String airfear="";
            if(traveller instanceof AirFearTraveller){
                airfear="yes";
            } else {
                airfear="no";
            }

            String sqlQuery="insert into travellers (name,uniquecode,city,airfear) values("
                    +"'"+traveller.getName()+"'"
                    +",'"+traveller.getUniqueCode()+"'"
                    +",'"+traveller.getCity()+"'"
                    +",'"+airfear+"'"
                    +")";
            System.out.println(sqlQuery);
            statement.execute(sqlQuery);
            statement.close();
            connection.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteTraveller(Traveller traveller) throws IOException{
        Audit.printQuery("deleteTraveller",Thread.currentThread().getName());

        Manager manager=Manager.getInst();

        for(int i=0;i<manager.travellers.size();i++){
            if(manager.travellers.get(i).getUniqueCode().equals(traveller.getUniqueCode())==true){
                manager.travellers.remove(i);
            }
        }

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";
        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="delete from travellers where uniquecode='"+traveller.getUniqueCode()+"'";
            statement.execute(sqlQuery);
        } catch (Exception exception){
            System.out.println(exception);
        }
    }

    public void editTraveller(String uniqueCode,String newName,String newCity) throws IOException{
        Audit.printQuery("editTraveller",Thread.currentThread().getName());

        Manager manager=Manager.getInst();
        for(int i=0;i<manager.travellers.size();i++){
            if(manager.travellers.get(i).getUniqueCode().equals(uniqueCode)==true){
                manager.travellers.get(i).setCity(newCity);
                manager.travellers.get(i).setName(newName);
            }
        }

        String dbUrl="jdbc:mysql://localhost:3306/DbPAO";
        String dbUser="root";
        String dbPass="Paorestanta2021";
        try{
            Connection connection=DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="update travellers set name="+"'"+newName+"'"
                    +",city="+"'"+newCity+"'"
                    +" where uniquecode="+"'"+uniqueCode+"'";
            statement.execute(sqlQuery);
        } catch (Exception exception){
            System.out.println(exception);
        }
    }
}
