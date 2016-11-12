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
 * @author Luca
 */
public class CommentoRecensione extends Notifica{
    
    private final Recensione rec;
    private final String commento;
    
    /**
     * Crea un nuovo oggetto di tipo CommentoRecensione, che servirà per rifiutare o confermare l'aggiunta di un commento da parte di un ristoratore ad una recensione lasciata da un Utente
     * @param manager collegamento al DBManager per accedere al DB
     * @param id id sul db
     * @param rec recensione alla quale si riferisce la notifica
     * @param commento commento che si intende aggiungere
     * @param data data di creazione della notifica
     */
    public CommentoRecensione(DBManager manager, int id, Recensione rec, String commento, Date data){
        super(manager,id,data);
        this.commento = commento;
        this.rec = rec;
    }
    
    @Override
    public String toString(){
        String res = "L'utente ristoratore " + rec.getRistorante().getUtente().getNomeCognome() + "\n" + 
                "voleva aggiungere il commento '" + commento + "' \n"+
                "alla recensione: " +
                rec.getTitolo() + "\n" + 
                rec.getTesto() + "\n" +
                "lasciata da " + rec.getUtente().getNomeCognome();
        return res;
    }
    
    
    @Override
    public boolean accetta(){
        rec.addComment(commento);
        return done();
    }
    
    @Override
    public boolean rifiuta(){
        return done();
    }
    
    @Override
    public boolean done(){
        PreparedStatement stm;
        try{
            stm = con.prepareStatement("delete from rispostarecensione where id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
            return true;
        } catch(SQLException ex){
            System.out.println("Fallita eliminazione notifica CommentoRecensione");
            return false;
        }
    }

    @Override
    public String getFotoPath() {
        return null;
    }

   
}
