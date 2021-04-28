package Entities;

import java.util.ArrayList;
import Services.*;

public class Destination {
    protected int popularity;
    private String name;
    private double avgPricePerDay;
    public ArrayList<Route> routes;
    private int index;

    public Destination(String name) {
        this.name = name;
        this.popularity=0;
        this.avgPricePerDay=0;
        this.routes=new ArrayList<Route>();
    }

    public Destination(int popularity, String name) {
        this.popularity = popularity;
        this.name = name;
        this.avgPricePerDay=0;
        this.routes=new ArrayList<Route>();
    }

    public Destination(int popularity, String name, int avgPricePerDay) {
        this.popularity = popularity;
        this.name = name;
        this.avgPricePerDay = avgPricePerDay;
        routes=new ArrayList<Route>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public double getAvgPricePerDay() {
        return avgPricePerDay;
    }

    public void setAvgPricePerDay(double avgPricePerDay) {
        this.avgPricePerDay = avgPricePerDay;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoute(Route r){
        routes.add(r);
    }

    @Override
    public String toString() {
        return name+" popularity: "+popularity+" avgPricePerDay: "+avgPricePerDay;
    }
}
