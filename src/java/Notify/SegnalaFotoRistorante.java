/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Notify;

import DataBase.DBManager;
import DataBase.Foto;
import DataBase.Ristorante;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Luca
 */
public class SegnalaFotoRistorante extends Notifica {

    private final Ristorante ristorante;
    private final Foto foto;

    /**
     * Crea un nuovo oggetto di tipo SegnalaFotoRistorante, che serve ad un utente ristoratore per segnalare una foto al suo ristorante non consona
     * @param manager collegamento al DBManager per operare sul DB
     * @param id id sul DB 
     * @param data data di creazione
     * @param ristorante ristorante dell'utente 
     * @param foto foto da segnalare
     */
    public SegnalaFotoRistorante(DBManager manager, int id, Date data, Ristorante ristorante, Foto foto) {
        super(manager, id, data);
        this.ristorante = ristorante;
        this.foto = foto;
    }

    @Override
    public boolean rifiuta() {
        return done();
    }

    @Override
    @SuppressWarnings("empty-statement")
    public boolean accetta() {
        ristorante.removeFoto(foto);
        return done();
    }

    @Override
    public boolean done() {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("delete from segnalafotoristorante where id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Fallita eliminazione notifica SegnalaFotoRistorante");
            return false;
        }
    }

    @Override
    public String toString() {
        System.out.println(ristorante);
        String res = ristorante.getUtente().getNomeCognome();
        res += ", proprietario del ristorante " + ristorante.getName() + ", ha chiesto la rimozione della seguente fotografia";
        System.out.println(res);
        return res;
    }

    @Override
    public String getFotoPath() {
        return foto.getFotopath();
    }

}
