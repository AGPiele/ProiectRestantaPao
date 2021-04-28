package Entities;

import java.util.Objects;

public class Traveller {
    String name;
    String uniqueCode;
    String city;

    public boolean canTravel(String toCity){return false;}

    public String getRecommendations(){return "No recommandations\n";}

    public Traveller(String name, String uniqueCode, String city) {
        this.name = name;
        this.uniqueCode = uniqueCode;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", uniqueCode='" + uniqueCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traveller traveller = (Traveller) o;
        return Objects.equals(uniqueCode, traveller.uniqueCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode);
    }
}
