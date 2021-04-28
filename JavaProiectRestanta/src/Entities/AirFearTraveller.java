package Entities;

import java.util.LinkedList;
import Services.*;

public class AirFearTraveller extends Traveller{
    public AirFearTraveller(String name, String uniqueCode,String city) {
        super(name, uniqueCode, city);
    }

    @Override
    public boolean canTravel(String toCity) {
        Manager manager=Manager.getInst();
        return manager.canReachNoPlane(this.city,toCity);
    }

    @Override
    public String getRecommendations(){
        LinkedList<Destination> recomm=new LinkedList<Destination>();
        Manager manager=Manager.getInst();
        for(Destination dest: manager.cities){
            if(this.canTravel(dest.getName())==true){
                recomm.add(dest);
            }
        }

        recomm.sort((o1, o2) -> -(Integer.compare(o1.getPopularity(),o2.getPopularity())));

        String res="";
        for(Destination dest:recomm){
            res+=dest+"\n";
        }
        if(res.length()<2){
            res="No recommendations\n";
        }

        return res;
    }
}
