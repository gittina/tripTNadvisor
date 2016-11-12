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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        ArrayList<CommentoRecensione> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from rispostarecensione");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new CommentoRecensione(manager, rs.getInt("id"), manager.getRecensione(rs.getInt("id_rec")), rs.getString("commento"), rs.getDate("data")));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }

    /*
    * Per ricevere tutte le notifiche di avvenuta richiesta di possesso ristorante da parte di un utente
    */
    ArrayList<RichiestaRistorante> getReclamaRistoranteNotifiche() {
        ArrayList<RichiestaRistorante> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from richiestaristorante");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new RichiestaRistorante(manager, rs.getInt("id"), rs.getDate("data"), manager.getUtente(rs.getInt("id_utente")), manager.getRistorante(rs.getInt("id_rist"))));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }
    
    /*
    * Per ricevere tutte le notifiche di avvenuta segnalazione foto di un ristorante da parte del proprietario
    */
    ArrayList<SegnalaFotoRistorante> getSegnalaFotoRistoranteNotifiche() {
        ArrayList<SegnalaFotoRistorante> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from segnalafotoristorante");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { 
                Foto f = manager.getFoto(rs.getInt("id_foto"));//DBManager manager, int id, Date data, Ristorante ristorante, Foto foto
                res.add(new SegnalaFotoRistorante(manager, rs.getInt("id"), rs.getDate("data"), manager.getRistorante(f.getRistorante().getId()), f));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }
    
    /*
    * Per ricevere tutte le notifiche di avvenuta segnalazione della foto di una recensione da parte del proprietario del ristorante a cui riferisce la recensione
    */
    ArrayList<SegnalaFotoRecensione> getSegnalaFotoRecensioneNotifiche() {
        ArrayList<SegnalaFotoRecensione> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from segnalafotorecensione");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { 
                res.add(new SegnalaFotoRecensione(manager, rs.getInt("id"), rs.getDate("data"), manager.getRecensione(rs.getInt("id_rec"))));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }

}
