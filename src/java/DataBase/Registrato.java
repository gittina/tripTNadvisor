/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.Notifica;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Luca
 */
public class Registrato extends Utente {

    private final boolean attivato;

    /**
     *
     * @param id id dell'utente su db
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param email indirizzo mail dell'utente
     * @param avpath path della foto del profilo dell'utente
     * @param attivato booleano per vedere se l'utente è attivato
     * @param manager collegamento al DBManager per eseguire query su DB
     */
    public Registrato(int id, String nome, String cognome, String email, String avpath, boolean attivato, DBManager manager) {
        super(id, nome, cognome, email, avpath, manager);
        this.attivato = attivato;
    }

    /**
     * Funzione per attivare un utente registrato non ancora verificato tramite mail
     * @return true se l'attivazione è andata a buon fine, false altrimentie
     */
    public boolean attiva() {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("update utente set attivato = ? where id = ?");
            stm.setBoolean(1, true);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /*
    * L'utente registrato non ha notifiche, probabile implementazione futura
    */
    @Override
    public ArrayList<Notifica> getNotifiche() {
        return new ArrayList<>();
    }

}
