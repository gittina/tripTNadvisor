/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Notify;

import DataBase.DBManager;
import DataBase.Foto;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucadiliello
 */
public class NuovaFoto extends Notifica {

    private final Foto foto;

    /**
     * Crea un nuovo oggetto di tipo  NuovaFoto, per notificare ad un Ristoratore l'aggiunta di una foto ad un suo ristorante
     * @param manager collegamento al DBManager per eseguire operazioni sul db
     * @param id id della notifica su db
     * @param data data di creazione della notifica
     * @param foto Foto in allegato alla notifica
     */
    public NuovaFoto(DBManager manager, int id, Date data, Foto foto) {
        super(manager, id, data);
        this.foto = foto;
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
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("remove from nuovafoto where id = ?");
            stm.setInt(1, getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "L'utente " + foto.getUtente() + " ha aggiunto una nuova foto al tuo ristorante " + foto.getRistorante().getName();
    }
    
    @Override
    public String getFotoPath(){
        return foto.getFotopath();
    }

}
