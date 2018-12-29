package Modle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class City implements Serializable {

    private String city_name;
    private String country;
    private String population;
    private String currency;
    private HashMap<String,String> location;
    private boolean update;


    //for city we update from parser
    public City(String city_name, String country, String population, String currency) {

        this.city_name = city_name;
        this.country = country;
        this.population = population;
        this.currency = currency;
        this.location = new HashMap<>();
        this.update=false;

    }



    //for cities we found in tag city
    public City(String city_name, String country, String population, String currency,String docNo) {
        this.city_name = city_name;
        this.country = country;
        this.population = population;
        this.currency = currency;
        this.location = new HashMap<>();
        addLocationToCity(docNo,"title");
        this.update=false;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }


    public void addLocationToCity(String DocNo,String loc){
        this.location.put(DocNo,loc);
    }



    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HashMap<String,String> getLocation() {
        return location;
    }



    public boolean docExistANdAdd(String docNo,String loc){

        if(location.containsKey(docNo)){
            location.put(docNo,location.get(docNo)+","+loc);
            return true;
        }
        else
            location.put(docNo, loc);
        return false;
    }


    public void setLocation(HashMap<String,String> location) {
        this.location = location;
    }


}