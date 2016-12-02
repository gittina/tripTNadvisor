/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author Luca
 */
public class Recensione implements Serializable{

    transient private final DBManager manager;
    private int id;
    private String titolo;
    private String testo;
    private Date data;
    private String commento;
    private final Ristorante ristorante;
    private final Utente utente;
    private String fotoPath;

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

    public void setCommento(String commento) {
        this.commento = commento;
    }
    
    

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
    }

    /**
     * Aggiunge un commento alla recensione, scritto dal proprietario del ristorante a cui è riferica
     * @param commento il commento da aggiungere
     * @return true se il commento è stato inserito/aggiuornato con successo, false altrimenti
     */
    public boolean addComment(String commento) {
        return manager.addComment(this, commento);
    }

    /**
     * Per ottenere il ristorante su cui è stata scritta questa recensione
     * @return il ristorante a cui è riferita questa recensione
     */
    public Ristorante getRistorante(){
        return ristorante;
    }

    public boolean justSegnalato(){
        return manager.justSegnalato(this);
    }
    
    /**
     * Aggiunge/Aggiorna la foto di questa recensione
     * @param pathFoto il path della foto da aggiungere/aggiornare
     * @return true se la foto è stata aggiunta/aggiornata con successo, false altrimenti
     */
    public boolean addFoto(String pathFoto) {
        return manager.addFoto(this, pathFoto);
    }
    
    /**
     * Elimina la foto di questa recensione
     * @return true se la foto è stata eliminata con successo, false altrimenti
     */
    public boolean removeFoto(){
        return manager.removeFoto(this);
    }

    /**
     * Per ottenere la valutazione di questa recensione come media delle valutazioni lasciate dagli altri utenti
     * @return float tra 0 e 5 che indica la media delle valutazioni lasciate a questa recensione
     */
    public float getMediaVoti() {
        return manager.getMediaVoti(this);
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
        return manager.addVoto(this, utente, voto);
    }
}
