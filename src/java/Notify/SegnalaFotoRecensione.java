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
public class SegnalaFotoRecensione extends Notifica{
     
    private final Recensione recensione;
    
    /**
     * Crea un nuovo oggetto di tipo SegnalaFotoRecensione, che serve ad un utente ristoratore di segnalare una foto di una recensione non consona
     * @param manager collegamento al DBManager per operare sul DB
     * @param id id sul DB
     * @param data data di creazione
     * @param recensione recensione nella quale è contenuta la foto da segnalare
     */
    public SegnalaFotoRecensione(DBManager manager, int id, Date data, Recensione recensione) {
        super(manager,id,data);
        this.recensione = recensione;
    }

    @Override
    public boolean rifiuta() {
        return done();
    }

    @Override
    public boolean accetta() {
        recensione.removeFoto();
        return done();
    }

    @Override
    public boolean done() {
        return manager.done(this);
    }

    @Override
    public String toString() {
        return "L'utente ristoratore " + recensione.getRistorante().getUtente().getNomeCognome() + 
                " proprietario del ristorante " + recensione.getRistorante().getName() + 
                " ha chiesto la rimozione della seguente foto dalla recensione " + 
                recensione.getTitolo() + "\n" + recensione.getTesto();
    }

    @Override
    public String getFotoPath(){
        return recensione.getFotoPath();
    }
    
}
