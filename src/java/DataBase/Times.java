/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Time;

/**
 *
 * @author lucadiliello
 */
public class Times {
    Time apertura;
    Time chiusura;
    
    public Time getChiusura() {
        return chiusura;
    }

    public void setChiusura(Time chiusura) {
        this.chiusura = chiusura;
    }
    
    public Time getApertura() {
        return apertura;
    }

    public void setApertura(Time apertura) {
        this.apertura = apertura;
    }
}

