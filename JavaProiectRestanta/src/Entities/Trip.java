package Entities;

import Entities.Destination;

public class Trip {
    private Destination from;
    private Destination to;
    private int cntPers;
    private double price;
    private int used;

    public int isUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Destination getFrom() {
        return from;
    }

    public void setFrom(Destination from) {
        this.from = from;
    }

    public Destination getTo() {
        return to;
    }

    public void setTo(Destination to) {
        this.to = to;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCntPers() {
        return cntPers;
    }

    public void setCntPers(int cntPers) {
        this.cntPers = cntPers;
    }

    public Trip(Destination from, Destination to, int cntPers, double price, int used) {
        this.from = from;
        this.to = to;
        this.cntPers = cntPers;
        this.price = price;
        this.used = used;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "from=" + from.getName() +
                ", to=" + to.getName() +
                ", cntPers=" + cntPers +
                ", price=" + price +
                '}';
    }
}
