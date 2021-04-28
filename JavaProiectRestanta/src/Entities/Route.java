package Entities;

import java.util.Objects;

public class Route {
    private String fDest;
    private String sDest;
    private double distance;
    private int time;
    private double moneyCost;

    public Route(String fDest, String sDest, double distance, int time, double moneyCost) {
        this.fDest = fDest;
        this.sDest = sDest;
        this.distance = distance;
        this.time = time;
        this.moneyCost = moneyCost;
    }

    void printData(){
        System.out.println("Route from city "+this.fDest+" to "+this.sDest+" of length "+this.distance);
    }

    public String getfDest() {
        return fDest;
    }

    public String getsDest() {
        return sDest;
    }

    public double getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public double getMoneyCost() {
        return moneyCost;
    }

    public void setfDest(String fDest) {
        this.fDest = fDest;
    }

    public void setsDest(String sDest) {
        this.sDest = sDest;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setMoneyCost(double moneyCost) {
        this.moneyCost = moneyCost;
    }

    @Override
    public String toString() {
        return "" +
                "fDest=" + fDest +
                ", sDest=" + sDest +
                ", distance=" + distance +
                ", time=" + time +
                ", moneyCost=" + moneyCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Double.compare(route.distance, distance) == 0 &&
                time == route.time &&
                Double.compare(route.moneyCost, moneyCost) == 0 &&
                fDest.equals(route.fDest) &&
                sDest.equals(route.sDest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fDest, sDest, distance, time, moneyCost);
    }
}
