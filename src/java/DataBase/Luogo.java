/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.Serializable;

/**
 *
 * @author Luca
 */
public class Luogo implements Serializable{
    private double lat;
    private double lng;
    private String address;

    /**
     * Crea un oggetto di tipo Luogo
     * @param lat latitudine espressa come double
     * @param lng longitudine espressa come double
     * @param address indirizzo del luogo
     */
    public Luogo(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAddressForGoogle(){
        return this.address.replace(" ", "+");
    }
    
}
