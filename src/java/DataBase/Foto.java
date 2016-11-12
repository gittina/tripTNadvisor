/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class Foto {
    private final DBManager manager;
    private final Connection con;
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
        con = manager.con;
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
        PreparedStatement stm;
        ResultSet rs;
        try{
            stm = con.prepareStatement("select * from segnalafotoristorante where id_foto = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            boolean res = rs.next();
            rs.close();
            stm.close();
            return res;
        } catch (SQLException ex) {
            return true;
        }
    }
    
    /**
     * Per ottenere l'utente proprietario di questa foto, cioè colui che l'ha caricata e pubblicata
     * @return l'utente proprietario della foto
     */
    public Utente getUtente(){
        return utente;
    }
    
}
