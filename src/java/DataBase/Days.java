/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Luca
 */
//giorni della settimana da 1 a 7
public class Days implements Serializable{

    transient private final DBManager manager;
    private final int giorno;
    private final int id;
    private final Ristorante ristorante;

    public int getId() {
        return id;
    }

    /**
     * Costruttore di Orario
     *
     * @param id id dell'orario su db
     * @param ristorante
     * @param giorno intero da 1 a 7 corrispondente al giorno della settimana: 1
     * = Lunedì, 2 = Martedì, ... , 7 = Domenica
     * @param manager per eseguire operazioni sul database
     */
    public Days(int id, Ristorante ristorante, int giorno, DBManager manager) {
        this.giorno = giorno;
        this.id = id;
        this.ristorante = ristorante;
        this.manager = manager;
    }

    /**
     * Ritorna il giorno sotto forma di stringa: Lunedì, Martedì, ... , Domenica
     *
     * @return
     */
    public String getGiornoString() {
        switch (giorno) {
            case 0:
                return "Lunedi";
            case 1:
                return "Martedi";
            case 2:
                return "Mercoledi";
            case 3:
                return "Giovedi";
            case 4:
                return "Venerdi";
            case 5:
                return "Sabato";
            case 6:
                return "Domenica";
        }
        return null;
    }

    public int getGiorno() {
        return giorno;
    }

    public ArrayList<Times> getTimes() {
        ArrayList<Times> res = manager.getTimes(this);
        Comparator c = (Comparator<Times>) new Comparator<Times>() {
            @Override
            public int compare(Times o1, Times o2) {
                return o1.getApertura().after(o2.getApertura()) ? 1 : -1;
            }
        };
        res.sort(c);
        return res;
    }

    public boolean addTimes(Time apertura, Time chiusura) {
        return manager.addTimes(this, apertura, chiusura);
    }
}
