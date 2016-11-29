/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Luca
 */
//giorni della settimana da 1 a 7
public class Days {

    private final DBManager manager;
    private final int giorno;
    private final int id;
    private final Ristorante ristorante;
    private final ArrayList<Times> tempi;

    public int getId() {
        return id;
    }

    /**
     * Costruttore di Orario
     *
     * @param id id dell'orario su db
     * @param ristorante
     * @param giorno intero da 1 a 7 corrispondente al giorno della settimana: 1
     * = Lunedì, 2 = Martedì, ... , 7 = Domenica
     * @param manager per eseguire operazioni sul database
     */
    public Days(int id, Ristorante ristorante, int giorno, DBManager manager) {
        this.giorno = giorno;
        this.id = id;
        this.ristorante = ristorante;
        this.manager = manager;
        tempi = new ArrayList<>();
        update();
    }

    /**
     * Ritorna il giorno sotto forma di stringa: Lunedì, Martedì, ... , Domenica
     *
     * @return
     */
    public String getGiornoString() {
        switch (giorno) {
            case 0:
                return "Lunedi";
            case 1:
                return "Martedi";
            case 2:
                return "Mercoledi";
            case 3:
                return "Giovedi";
            case 4:
                return "Venerdi";
            case 5:
                return "Sabato";
            case 6:
                return "Domenica";
        }
        return null;
    }

    public int getGiorno() {
        return giorno;
    }

    private void update() {
        PreparedStatement stm;
        ResultSet rs;

        try {
            stm = manager.con.prepareStatement("SELECT * from Times WHERE id_days = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();

            while (rs.next()) {
                tempi.add(new Times(rs.getInt("id"), rs.getTime("apertura"), rs.getTime("chiusura")));
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione tempi");
        }
    }

    public ArrayList<Times> getTimes() {
        Comparator c = (Comparator<Times>) new Comparator<Times>() {
            @Override
            public int compare(Times o1, Times o2) {
                return o1.getApertura().after(o2.getApertura()) ? 1 : -1;
            }
        };
        tempi.sort(c);
        return tempi;
    }

    public void addTimes(Time apertura, Time chiusura) {
        PreparedStatement stm;
        try {
            stm = manager.con.prepareStatement("insert into times (id_days,apertura,chiusura) values (?,?,?)");
            stm.setInt(1, this.id);
            stm.setTime(2, apertura);
            stm.setTime(3, chiusura);
            stm.executeUpdate();
        } catch (SQLException ex){
            System.out.println("Failed to add times to days");
        }
    }
}
