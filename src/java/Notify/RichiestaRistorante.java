/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Notify;

import DataBase.DBManager;
import DataBase.Ristorante;
import DataBase.Utente;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author Luca
 */
public class RichiestaRistorante extends Notifica{

    private final Utente utente;
    private final Ristorante ristorante;
    
    /**
     * Crea un oggetto di tipo RichiestaRistorante, che serve per notificare e far decidere ad un amministratore se il ristorante va assegnato ad un utente
     * @param manager collegamento al DBManager per poter effetturare operazioni sul DB
     * @param id id sul DB
     * @param data data di creazione
     * @param utente utente che chiede il possesso di un ristorante
     * @param ristorante ristorante di cui l'utente chiede il possesso
     */
    public RichiestaRistorante(DBManager manager, int id, Date data, Utente utente, Ristorante ristorante) {
        super(manager,id,data);
        this.ristorante = ristorante;
        this.utente = utente;
    }

    @Override
    public boolean rifiuta() {
        return done();
    }

    @Override
    public boolean accetta() {
        try {
            manager.linkRistorante(ristorante, utente);
        } catch (SQLException ex) {
            return false;
        }
        return done();
    }

    @Override
    public boolean done(){
        return manager.done(this);
    }
    
    @Override
    public String toString(){
        String res = "L'utente " + utente.getNomeCognome() + " ha chiesto il possesso del ristorante " + ristorante.getName();
        return res;
    }

    @Override
    public String getFotoPath() {
        return null;
    }
    
}
