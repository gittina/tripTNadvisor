/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Time;

/**
 *
 * @author Luca
 */

//giorni della settimana da 1 a 7
public class Orario {
    private final int giorno;
    private final int id;
    private final Ristorante ristorante;

    public int getId() {
        return id;
    }
    /**
     * Costruttore di Orario
     * @param id id dell'orario su db
     * @param ristorante
     * @param giorno intero da 1 a 7 corrispondente al giorno della settimana: 1 = Lunedì, 2 = Martedì, ... , 7 = Domenica
     * @param apertura Time di apertura
     * @param chiusura Time di chiusura 
     */
    public Orario(int id, Ristorante ristorante, int giorno, Time apertura, Time chiusura) {
        this.apertura = apertura;
        this.chiusura = chiusura;
        this.giorno = giorno;
        this.id = id;
        this.ristorante = ristorante;
    }

    /**
     * Ritorna il giorno sotto forma di stringa: Lunedì, Martedì, ... , Domenica
     * @return
     */
    public String getGiornoString() {
        switch(giorno){
            case 0: return "Lunedi";
            case 1: return "Martedi";
            case 2: return "Mercoledi";
            case 3: return "Giovedi";
            case 4: return "Venerdi";
            case 5: return "Sabato";
            case 6: return "Domenica";
        }
        return null;
    }
    
    @Override
    public String toString(){
        return getGiornoString() + ": " + getApertura() + " - " + getChiusura() + "\n";
    }
    
    public int getGiorno(){
        return giorno;
    }

    public Time getApertura() {
        return apertura;
    }

    public void setApertura(Time apertura) {
        this.apertura = apertura;
    }

    public Time getChiusura() {
        return chiusura;
    }

    public void setChiusura(Time chiusura) {
        this.chiusura = chiusura;
    }
    
    private Time apertura;
    private Time chiusura;
}
