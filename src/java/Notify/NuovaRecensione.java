/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Notify;

import DataBase.DBManager;
import DataBase.Recensione;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author lucadiliello
 */
public class NuovaRecensione extends Notifica {

    private final Recensione recensione;
    
    /**
     * Crea un nuovo oggetto di tipo NuovaRecensione, che servirà per avvisare un Utente che un suo ristorante è stato recensito
     * @param manager collegamento al DBManager
     * @param id id su DB
     * @param data data di creazione della notifica
     * @param recensione recensione alla quale si riferisce
     */
    public NuovaRecensione(DBManager manager, int id, Date data, Recensione recensione) {
        super(manager, id, data);
        this.recensione = recensione;
    }

    @Override
    public boolean rifiuta() {
        return false;
    }

    @Override
    public boolean accetta() {
        return false;
    }

    @Override
    public boolean done() {
        return manager.done(this);
    }

    @Override
    public String toString() {
        return "L'utente " + recensione.getUtente().getNomeCognome() + " ha aggiunto una nuova recensione al tuo ristorante " + recensione.getRistorante().getName() + "\n"
                + "Titolo: " + recensione.getTitolo()
                + "Testo: " + recensione.getTesto();
    }

    @Override
    public String getFotoPath() {
        return recensione.getFotoPath();
    }
}
