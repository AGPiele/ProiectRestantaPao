import Entities.*;
import Services.*;

import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException {
        Manager manager=Manager.getInst();
        manager.loadDestinations();
        manager.loadRoutes();
        manager.loadTrips();
        manager.loadTravellers();

       DestinationService destinationService=DestinationService.getInst();
        RouteService routeService=RouteService.getInst();
        TripService tripService=TripService.getInst();
        TravellerService travellerService=TravellerService.getInst();

        Destination destDragasani=new Destination(10,"Dragasani",100);
        destinationService.addDestination(destDragasani);
        Destination destvValcea=new Destination(12,"Valcea",200);
        destinationService.addDestination(destvValcea);

        Destination destParis=new Destination(15,"Paris",350);
        destinationService.addDestination(destParis);

        Destination destMallorca=new Destination(20,"Mallorca",400);
        destinationService.addDestination(destMallorca);
        Route route=new AirRoute("Paris","Dragasani",2500,500,400);
        routeService.addRoute(route);
        Route route1=new Route("Dragasani", "Mallorca", 3000, 600,700);
        routeService.addRoute(route1);
        System.out.println(manager.canReach("Paris","Dragasani"));
        System.out.println(manager.canReach("Dragasani","Paris"));



        System.out.println(routeService.showRoutes());

        routeService.editRoute(route,10,1000000,20);
        System.out.println(routeService.showRoutes());


        routeService.deleteRoute(route);
        System.out.println(routeService.showRoutes());
        Destination destBucuresti = new Destination(10,"Bucuresti", 300);
        Destination destSingapore = new Destination(10,"Singapore", 300);
        destinationService.addDestination(destBucuresti);
        destinationService.addDestination(destSingapore);
        manager.loadTrips();
        Destination cityA=manager.cities.get(manager.getIndex("Bucuresti"));
        Destination cityB=manager.cities.get(manager.getIndex("Singapore"));

        Trip trip=new Trip(cityA,cityB,100,200.0,0);
        tripService.addTrip(trip);
        System.out.println(tripService.showTrips());

        Traveller tr1=new AllRoutesTraveller("Piele","198121724123456","Dragasani");
        Traveller tr2=new AllRoutesTraveller("Buinoiu","1234567891234","Bucuresti");

        travellerService.addTraveller(tr1);
        System.out.println(manager.travellers);
        travellerService.addTraveller(tr2);
        System.out.println(manager.travellers);

        Traveller tr3=new AirFearTraveller("Dragos","1234567891334","Botosani");
        System.out.println(manager.travellers);
        travellerService.deleteTraveller(tr3);
        System.out.println(manager.travellers);
        travellerService.editTraveller("1234567891334","Dragos","Botosani");
        System.out.println(manager.travellers.get(0).getCity());

        System.out.println(tr1 instanceof AirFearTraveller);
        System.out.println(tr1.canTravel("Mallorca"));
        System.out.println(tr1.getRecommendations());

        System.out.println(tr1.canTravel("Mallorca"));
        System.out.println(tr2.canTravel("Mallorca"));

    }
}
