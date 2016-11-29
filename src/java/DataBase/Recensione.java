/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.*;
import java.time.LocalDate;

/**
 *
 * @author Luca
 */
public class Recensione {

    private int id;
    private String titolo;
    private String testo;
    private Date data;
    private String commento;
    private final DBManager manager;
    private final Connection con;
    private final Ristorante ristorante;
    private final Utente utente;

    /**
     * Per ottenere l'utente che ha scritto questa recensione
     * @return l'utente che ha scritto la recensione
     */
    public Utente getUtente() {
        return utente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
    
    protected String fotoPath;

    /**
     * Costruttore di una Recensione
     * @param id id della recensione sul DB
     * @param ristorante ristorante a cui appartiene la recensione
     * @param utente utente che ha scritto la recensione
     * @param titolo titolo della recensione
     * @param testo testo o corpo della recensione
     * @param data data in cui è stata pubblicata la recensione
     * @param commento commento scritto dal proprietario del ristorante a questa recensione
     * @param fotoPath path della foto di questa recensione
     * @param manager collegamento al DBManager per utilizzare il DB
     */
    public Recensione(int id, Ristorante ristorante, Utente utente, String titolo, String testo, Date data, String commento, String fotoPath, DBManager manager) {
        this.id = id;
        this.titolo = titolo;
        this.testo = testo;
        this.data = data;
        this.commento = commento;
        this.fotoPath = fotoPath;
        this.manager = manager;
        this.utente = utente;
        this.ristorante = ristorante;
        con = manager.con;
    }

    /**
     * Aggiunge un commento alla recensione, scritto dal proprietario del ristorante a cui è riferica
     * @param commento il commento da aggiungere
     * @return true se il commento è stato inserito/aggiuornato con successo, false altrimenti
     */
    public boolean addComment(String commento) {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("UPDATE RECENSIONE SET commento = ? WHERE id = ?");
            stm.setString(1, commento);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
            this.commento = commento;
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Per ottenere il ristorante su cui è stata scritta questa recensione
     * @return il ristorante a cui è riferita questa recensione
     */
    public Ristorante getRistorante(){
        return ristorante;
    }

    public boolean justSegnalato(){
        PreparedStatement stm;
        ResultSet rs;
        try{
            stm = con.prepareStatement("select * from segnalafotorecensione where id_rec = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            boolean res = rs.next();
            rs.close();
            stm.close();
            return res;
        } catch (SQLException ex) {
            return true;
        }
    }
    
    /**
     * Aggiunge/Aggiorna la foto di questa recensione
     * @param pathFoto il path della foto da aggiungere/aggiornare
     * @return true se la foto è stata aggiunta/aggiornata con successo, false altrimenti
     * @throws SQLException
     */
    public boolean addFoto(String pathFoto) throws SQLException {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("UPDATE RECENSIONE SET fotoPath = ? WHERE id = ?");
            stm.setString(1, pathFoto);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita aggiunta foto a ristorante su DB");
            return false;
        }
        return true;
    }
    
    /**
     * Elimina la foto di questa recensione
     * @return true se la foto è stata eliminata con successo, false altrimenti
     */
    public boolean removeFoto(){
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("UPDATE RECENSIONE SET fotoPath = ? WHERE id = ?");
            stm.setString(1, null);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
            fotoPath = null;
        } catch (SQLException ex) {
            System.out.println("Fallita aggiunta foto a ristorante su DB");
            return false;
        }
        return true;
    }

    /**
     * Per ottenere la valutazione di questa recensione come media delle valutazioni lasciate dagli altri utenti
     * @return float tra 0 e 5 che indica la media delle valutazioni lasciate a questa recensione
     */
    public float getMediaVoti() {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select rating from votorec where id_rec = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            int somma = 0;
            int count = 0;
            while (rs.next()) {
                somma += rs.getInt("rating");
                count++;
            }
            float res = count != 0 ? ((float) somma) / count : -1;
            stm.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione voti recensione");
            return -1;
        }
    }

    /**
     * Per controllare se la foto è già stata commentata
     * @return true se è già stata commentata, false altrimenti
     */
    public boolean esisteCommento() {

        return commento != null;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCommento() {
        return commento;
    }

    /**
     * Per aggiungere un voto alla recensione
     * @param utente utente che vota la recensione
     * @param voto intero da 0 a 5 
     * @return
     */
    public boolean addVoto(Utente utente, int voto) {
        PreparedStatement stm;
        try {
            Date current = Date.valueOf(LocalDate.now());
            stm = con.prepareStatement("insert into votorec (id_utente,id_rec,rating,data) values (?,?,?,?)");
            stm.setInt(1, utente.getId());
            stm.setInt(2, id);
            stm.setInt(3, voto);
            stm.setDate(4, current);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
}
