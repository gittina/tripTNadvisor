/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.Notifica;
import Notify.NuovaFoto;
import Notify.NuovaRecensione;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Luca
 */
public class Ristoratore extends Utente {

    public Ristoratore(int id, String nome, String cognome, String email, String avpath, boolean attivato, DBManager manager) {
        super(id, nome, cognome, email, avpath, manager);
    }
    
    @Override
    public ArrayList<Notifica> getNotifiche() {
        ArrayList<Notifica> res = new ArrayList<>();
        res.addAll(getNuovaFotoNotifiche());
        res.addAll(getNuovaRecensioneNotifiche());
        Comparator<Notifica> c = (Notifica o1, Notifica o2) -> {
            if(o1.getData().after(o2.getData())) return 1;
            else if(o1.getData().before(o2.getData())) return -1;
            else return 0;
        };
        res.sort(c);
        return res;
    }
    
    /*
    * Riceve tutte le notifiche di avvenuta aggiunta foto ad uno dei ristorante dell'utente
    */
    ArrayList<NuovaFoto> getNuovaFotoNotifiche() {
        return manager.getNuovaFotoNotifiche(this);
    }

    /*
    * Riceve tutte le notifiche di avvenuta aggiunta recensione ad uno dei ristorante dell'utente
    */
    ArrayList<NuovaRecensione> getNuovaRecensioneNotifiche() {
        return manager.getNuovaRecensioneNotifiche(this);
    }

    /**
     * Per ottenere la lista dei ristoranti posseduti da un utente
     * @return Un ArrayList dei ristoranti dell'utente
     * @throws SQLException
     */
    public ArrayList<Ristorante> getRistoranti() throws SQLException {
        return manager.getRistoranti(this);
    }
}
