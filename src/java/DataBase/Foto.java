/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author Luca
 */
public class Foto implements Serializable {
    transient private final DBManager manager;
    /**
     * Creazione dell'oggetto foto
     * @param id id della foto su DB
     * @param fotopath path della foto riferito a contextpath del progetto
     * @param descr breve descrizione della foto
     * @param data data di pubblicazione
     * @param utente utente che ha pubblicato questa foto
     * @param ristorante ristorante sulla quale è pubblicata questa foto
     * @param manager per collegamente a database
     */
    public Foto(int id, String fotopath, String descr, Date data, Utente utente, Ristorante ristorante, DBManager manager) {
        this.manager = manager;
        this.id = id;
        this.fotopath = fotopath;
        this.descr = descr;
        this.data = data;
        this.ristorante = ristorante;
        this.utente = utente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFotopath() {
        return fotopath;
    }

    public void setFotopath(String fotopath) {
        this.fotopath = fotopath;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    private int id;
    private String fotopath;
    private String descr;
    private Date data;
    private final Utente utente;
    private final Ristorante ristorante;

    public Ristorante getRistorante() {
        return ristorante;
    }
    
    public boolean justSegnalato(){
        return manager.justSegnalato(this);
    }
    
    /**
     * Per ottenere l'utente proprietario di questa foto, cioè colui che l'ha caricata e pubblicata
     * @return l'utente proprietario della foto
     */
    public Utente getUtente(){
        return utente;
    }
    
}
