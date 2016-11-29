/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 *
 *
 * @author Luca
 */
public class Ristorante implements Serializable {

    private final DBManager manager;
    private final Connection con;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getLinksito() {
        return linksito;
    }

    public void setLinksito(String linksito) {
        this.linksito = linksito;
    }

    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
    }

    public String getCucina() {
        return cucina;
    }

    public void setCucina(String cucina) {
        this.cucina = cucina;
    }

    public int getVisite() {
        return visite;
    }

    public void setVisite(int visite) {
        this.visite = visite;
    }

    /**
     * Per ottenere il proprietario del ristorante
     *
     * @return
     */
    public Utente getUtente() throws NullPointerException {
        if (utente != null) {
            return (Ristoratore) utente;
        } else {
            return null;
        }
    }

    private int id;
    private String name;
    private String descr;
    private String linksito;
    private String fascia;
    private String cucina;
    public String dirName;
    private final Utente utente;
    private int visite;

    /**
     * Crea un nuovo oggetto di tipo Ristorante
     *
     * @param id id del ristorante
     * @param name nome del ristorante
     * @param descr descrizione del ristorante
     * @param linksito link al sito web del ristorante
     * @param fascia fascia di prezzo del ristorante
     * @param cucina tipo di cucina del ristorante
     * @param manager oggetto DBManager per la connessione e l'uso del DB
     * @param utente utente che possiede il ristorante, null altrimenti
     * @param visite numero di visite del ristorante
     */
    public Ristorante(int id, String name, String descr, String linksito, String fascia, String cucina, DBManager manager, Utente utente, int visite) {
        con = manager.con;
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.linksito = linksito;
        this.fascia = fascia;
        this.cucina = cucina;
        this.utente = utente;
        this.manager = manager;
        this.visite = visite;

    }

    /**
     * Aggiunge una visita al ristorante
     */
    public void addVisita() {
        manager.addVisita(this);
        visite++;
    }

    /**
     *
     * @param nome nuovo nome
     * @param address nuovo indirizzo
     * @param linksito nuovo sito web
     * @param descr nuova descrizione
     * @param cucina nuova cucina
     * @param fascia nuova fascia
     * @return true se i dati sono stati aggiornati correttamente, false
     * altrimenti
     */
    public boolean updateData(String nome, String address, String linksito, String descr, String cucina, String fascia) {
        return manager.updateData(this, nome, address, linksito, descr, cucina, fascia);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (getClass() != obj.getClass()) {
                return false;
            }
            Ristorante ristorante = (Ristorante) obj;
            if (id != ristorante.id) {
                return false;
            }
            if (!name.equals(ristorante.name)) {
                return false;
            }
            if (!linksito.equals(ristorante.linksito)) {
                return false;
            }
            if (!fascia.equals(ristorante.fascia)) {
                return false;
            }
            if (!cucina.equals(ristorante.cucina)) {
                return false;
            }
            if (!descr.equals(ristorante.descr)) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.linksito);
        return hash;
    }

    /**
     * Per sapere se il ristorante è già stato reclamato da un utente
     *
     * @return true se il ristorante appartiene ad un utente, false altrimenti
     */
    public boolean reclamato() {
        return utente != null;
    }

    /**
     * Per settare l'indirizzo del ristorante
     *
     * @param address l'indirizzo del ristorante
     * @return true se la posizione del ristorante è stata impostata
     * correttamente, false altrimenti
     */
    public boolean setLuogo(String address) {
        return manager.setLuogo(this, address);
    }

    /**
     * Funzione che calcola il voto del ristorante come media di tutte le
     * valutazioni lasciate dagli utenti
     *
     * @return un float tra 0 e 5 che valuta la qualità del ristorante
     * @throws SQLException
     */
    public float getVoto() throws SQLException {
        return manager.getVoto(this);
    }

    /**
     * Funzione che calcola la posizione in classifica instantanea di un
     * ristorante
     *
     * @return la posizione in classifica del ristorante
     */
    public int getPosizioneClassifica() {
        return manager.getPosizioneClassifica(this);
    }

    /**
     *
     * @param giorno giorno a cui è riferito l'orario, 1 = Lunedì, 2 = Martedì,
     * .... , 7 = Domenica
     * @param inizio Time dell'inizio dell'orario di apertura
     * @param fine Time della fine dell'orario di apertura
     * @return
     * @throws SQLException
     */
    public boolean addTimes(int giorno, Time inizio, Time fine) throws SQLException {
        return manager.addTimes(this, giorno, inizio, fine);
    }

    public boolean addDays(int giorno) {
        return manager.addDays(this, giorno);
    }

    public boolean removeTimes(int id_times) {
        return manager.removeTimes(id_times);
    }
    

    /**
     * Per ricevere l'oggetto Luogo riferito a questo ristorante
     *
     * @return L'oggetto Luogo riferito a questo ristorante
     */
    public Luogo getLuogo() {
        return manager.getLuogo(this);
    }

    public ArrayList<Ristorante> getVicini() {
        return manager.advSearch2(this.getLuogo().getLat(), this.getLuogo().getLng());
    }

    /**
     * Per ottenere tutti gli orari di questo ristorante
     *
     * @return tutti gli orari del ristorante
     * @throws SQLException
     */
    public ArrayList<Days> getDays() throws SQLException {
        return manager.getDays(this);
    }

    /**
     * Per ottenere tutte le recensioni lasciate dagli utenti a questo
     * ristorante
     *
     * @return Lista di Recensioni
     * @throws SQLException
     */
    public ArrayList<Recensione> getRecensioni() throws SQLException {
        return manager.getRecensioni(this);
    }

    /**
     * Questa funzione permette di aggiungere una recensione a questo ristorante
     *
     * @param titolo titolo della recensione
     * @param testo testo o corpo della recensione
     * @param utente utente che scrive la recensione
     * @return l'oggetto recensione appena creato
     * @throws SQLException
     */
    public Recensione addRecensione(String titolo, String testo, Utente utente) throws SQLException {
        return manager.addRecensione(this, titolo, testo, utente);
    }

    /**
     * Funzione per l'aggiunta di una foto a questo ristorante
     *
     * @param path path della foto da aggiungere
     * @param descr piccola descrizione della foto
     * @param utente utente che aggiunge la foto
     * @return true se l'aggiunta ha avuto successo, false altrimenti
     * @throws SQLException
     */
    public boolean addFoto(String path, String descr, Utente utente) throws SQLException {
        return manager.addFoto(this, path, descr, utente);
    }

    /**
     * Funzione per rimuovere una foto da questo ristorante
     *
     * @param foto foto da rimuovere
     * @return true se la rimozione ha avuto successo, false altrimenti
     */
    public boolean removeFoto(Foto foto) {
        return manager.removeFoto(this,foto);
    }

    /**
     * Funione per ottenere una lista di tutte le foto che sono state aggiunte a
     * questo ristorante
     *
     * @return ArrayList di foto di questo ristorante
     * @throws SQLException
     */
    public ArrayList<Foto> getFoto() throws SQLException {
        return manager.getFoto(this);
    }

    /**
     * Crea un immagine QR con le seguenti informazioni: - Nome - Indirizzo -
     * Orari di apertura
     *
     * @return il path per accedere all'immagine creata
     * @throws SQLException
     */
    public String creaQR() throws SQLException {
           return manager.creaQR(this);
    }

    /**
     * Aggiunge un voto a questo ristorante
     *
     * @param user utente che aggiunge il voto
     * @param rating voto da 0 a 5 compresi
     * @return true se il voto è stato aggiunto con successo, false altrimenti
     */
    public boolean addVoto(Utente user, int rating) {
        return manager.addVoto(this, user, rating);
    }

    //da lavorare
    public boolean nowOpen() {
        return false;
    }
}
