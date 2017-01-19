/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import static DataBase.DBManager.readJsonFromUrl;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Luca
 */
public class Luogo implements Serializable{
    private double lat;
    private double lng;
    private int street_number;
    private String street;
    private String city;
    private String area1;
    private String area2;
    private String state;

    public int getStreet_number() {
        return street_number;
    }

    public void setStreet_number(int street_number) {
        this.street_number = street_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea1() {
        return area1;
    }

    public void setArea1(String area1) {
        this.area1 = area1;
    }

    public String getArea2() {
        return area2;
    }

    public void setArea2(String area2) {
        this.area2 = area2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DBManager getManager() {
        return manager;
    }

    public void setManager(DBManager manager) {
        this.manager = manager;
    }
    private DBManager manager;

    /**
     * Crea un oggetto di tipo Luogo
     * @param address indirizzo del luogo
     * @param manager per utilizzo del database manager
     */
    public Luogo(String address, DBManager manager) {
        this.manager = manager;
        try {
            String req = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(' ', '+') + "&key=" + manager.googleKey;
            JSONObject json = readJsonFromUrl(req);
            if(json.getString("status").equals("OK")){
                JSONArray faddress = json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                street_number = Integer.parseInt(faddress.getJSONObject(0).getString("long_name"));
                street = faddress.getJSONObject(1).getString("long_name");
                city = faddress.getJSONObject(2).getString("long_name");
                area1 = faddress.getJSONObject(3).getString("long_name");
                area2 = faddress.getJSONObject(4).getString("long_name");
                state = faddress.getJSONObject(5).getString("long_name");
                JSONObject location = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                lat = location.getDouble("lat");
                lng = location.getDouble("lng");
                System.out.println(state + "," + area2 + "," + area1 + "," + city + "," + street + "," + street_number + " | (" + lat + "," + lng + ")");
            }    
        } catch (JSONException | IOException ex) {
            Logger.getLogger(Luogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Luogo(double lat, double lng, int street_number, String street, String city, String area1, String area2, String state, DBManager manager) {
        this.lat = lat;
        this.lng = lng;
        this.street_number = street_number;
        this.street = street;
        this.city = city;
        this.area1 = area1;
        this.area2 = area2;
        this.state = state;
        this.manager = manager;
    }

    public String getAddress(){
       return street + " " + street_number + ", " + city + ", " + area1 + ", " + area2 + ", " + state;
    }
    
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    
}
