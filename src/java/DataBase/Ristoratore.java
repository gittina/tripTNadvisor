/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.Notifica;
import Notify.NuovaFoto;
import Notify.NuovaRecensione;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        ArrayList<NuovaFoto> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from nuovafoto where id_dest = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { //DBManager manager, int id, Date data, Foto foto
                res.add(new NuovaFoto(manager, rs.getInt("id"), rs.getDate("data"), manager.getFoto(rs.getInt("id_foto"))));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }

    /*
    * Riceve tutte le notifiche di avvenuta aggiunta recensione ad uno dei ristorante dell'utente
    */
    ArrayList<NuovaRecensione> getNuovaRecensioneNotifiche() {
        ArrayList<NuovaRecensione> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from nuovafoto where id_dest = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { //DBManager manager, int id, Date data, Foto foto
                res.add(new NuovaRecensione(manager, rs.getInt("id"), rs.getDate("data"), manager.getRecensione(rs.getInt("id_rec"))));
                System.out.println("Added notifica");
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }

    /**
     * Per ottenere la lista dei ristoranti posseduti da un utente
     * @return Un ArrayList dei ristoranti dell'utente
     * @throws SQLException
     */
    public ArrayList<Ristorante> getRistoranti() throws SQLException {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from Ristorante where id_utente = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linksito"), rs.getString("fascia"), rs.getString("cucina"), manager, manager.getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
            }
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }
}
