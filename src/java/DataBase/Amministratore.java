/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.CommentoRecensione;
import Notify.Notifica;
import Notify.RichiestaRistorante;
import Notify.SegnalaFotoRecensione;
import Notify.SegnalaFotoRistorante;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Luca
 */
public class Amministratore extends Utente {  
    
    public Amministratore(int id, String nome, String cognome, String email, String avpath, DBManager manager) {
        super(id, nome, cognome, email, avpath, manager);

    }

    @Override
    public ArrayList<Notifica> getNotifiche() {
        ArrayList<Notifica> res = new ArrayList<>();
        res.addAll(getCommentoRecensioneNotifiche());
        res.addAll(getReclamaRistoranteNotifiche());
        res.addAll(getSegnalaFotoRistoranteNotifiche());
        res.addAll(getSegnalaFotoRecensioneNotifiche());
        Comparator<Notifica> c = (Notifica o1, Notifica o2) -> {
            if(o1.getData().after(o2.getData())) return 1;
            else if(o1.getData().before(o2.getData())) return -1;
            else return 0;
        };
        res.sort(c);
        return res;
    }

    /*
    * Per ricevere tutte le notifiche di avvenuto commento a recensione
    */
    ArrayList<CommentoRecensione> getCommentoRecensioneNotifiche() {
        return manager.getCommentoRecensioneNotifiche();
    }
    

    /*
    * Per ricevere tutte le notifiche di avvenuta richiesta di possesso ristorante da parte di un utente
    */
    ArrayList<RichiestaRistorante> getReclamaRistoranteNotifiche() {
        return manager.getReclamaRistoranteNotifiche();
    }
    
    /*
    * Per ricevere tutte le notifiche di avvenuta segnalazione foto di un ristorante da parte del proprietario
    */
    ArrayList<SegnalaFotoRistorante> getSegnalaFotoRistoranteNotifiche() {
        return manager.getSegnalaFotoRistoranteNotifiche();
    }
    
    /*
    * Per ricevere tutte le notifiche di avvenuta segnalazione della foto di una recensione da parte del proprietario del ristorante a cui riferisce la recensione
    */
    ArrayList<SegnalaFotoRecensione> getSegnalaFotoRecensioneNotifiche() {
        return manager.getSegnalaFotoRecensioneNotifiche();
    }
}
