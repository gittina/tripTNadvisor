/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.Notifica;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Luca
 */
public abstract class Utente implements Serializable{

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String avpath;
    transient protected final DBManager manager;

    public Utente(int id, String nome, String cognome, String email, String avpath, DBManager manager) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.avpath = avpath;
        this.manager = manager;
    }

    /**
     * Funzione che raccoglie tutte le notifiche destinate all'utente
     *
     * @return Un ArrayList di Notifica che non sono ancora state
     * confermate/eliminate
     */
    public abstract ArrayList<Notifica> getNotifiche();

    /**
     * Funzione per cambiare i dati registrati di un utente tranne la password,
     * per quella di veda modificaPassword()
     *
     * @param nome nuovo nome
     * @param cognome nuovo cognome
     * @param email nuova email
     * @param avpath nuovo path della foto del profilo
     * @return true se è andato tutto bene, false, altrimenti
     */
    public boolean modificaProfilo(String nome, String cognome, String email, String avpath) {
        return manager.modificaProfilo(this, nome, cognome, email, avpath);
    }

    /**
     * Serve a permettere ad un utente di cambiare la sua password
     *
     * @param nuovaPass nuova password dell'utente
     * @return true se è andato tutto bene, false altrimenti
     * @throws SQLException
     */
    public boolean cambiaPassword(String nuovaPass) throws SQLException {
        return manager.cambiaPassword(this, nuovaPass);
    }

    
    public int numeroRecensioni(){
        return manager.numeroRecensioni(this);
    }
    
    
    public boolean justSegnalataFoto(Foto foto){
        return manager.justSegnalataFoto(foto);
    }
    
    /**
     * Calcola la reputazione di un utente sulla media dei voti delle recensioni
     * che ha lasciato
     *
     * @return un float tra 0 e 5 alle recensioni di questo utente
     */
    public float getReputazione() {
        return manager.getReputazione(this);
    }

    /**
     *
     * @param ristorante ristorante per cui ci vuole controllare se l'utente ha
     * già rilasciato una recensione
     * @return true se questo utente ha già recensito questo ristorante, false
     * altrimenti
     */
    public boolean justRecensito(Ristorante ristorante) {
        return manager.justRecensito(this, ristorante);
    }

    /**
     * Questa funzione controlla se l'utente è proprietario di un ristorante
     *
     * @param ristorante ristorante
     * @return true se l'utente è proprietario del ristorante, false altrimentiw
     * @throws SQLException
     */
    public boolean proprietario(Ristorante ristorante) throws SQLException {
        System.out.println(this);
        System.out.println(ristorante);
        if (ristorante.getUtente() == null) {
            return false;
        } else {
            return ristorante.getUtente().equals(this);
        }
    }

    /**
     * Serve a verificare se un utente ha già votato un ristorante oggi
     *
     * @param ristorante il ristorante in questione
     * @return true se l'utente ha già votato quel ristorante oggi, false
     * altrimenti
     */
    public boolean justVotatoOggi(Ristorante ristorante) {
        return manager.justVotatoOggi(this, ristorante);
    }

    /**
     * Serve a verificare se un utente è proprietario di una recensione
     *
     * @param rec
     * @return
     * @throws SQLException
     */
    public boolean proprietario(Recensione rec) throws SQLException {
        return rec.getUtente().equals(this);
    }

    /**
     * Per controllare se un utente è attivato, ovvero ha confermato la sua
     * registrazione via mail Un amministratore è sempre verificato, quindi per
     * utenti amministratori verrà ritornato sempre true
     *
     * @return true se l'utente è attivato, false altrimenti
     * @throws SQLException
     */
    public boolean isActivate() throws SQLException {
        return manager.isActivate(this);
    }

    /**
     *
     * @param nome nome completo del ristorante
     * @param desc descrizione del ristorante
     * @param linkSito link al sito web del ristorante
     * @param fascia fascia del ristorante ("Economica","Normale","Lussuoso")
     * @param spec specialità del ristorante
     * @param address indirizzo del ristorante
     * @param fotoPath prima foto del ristorante (sarà possibile aggiungerne
     * altre)
     * @param fotoDescr descrizione della prima foto
     * @return true se la registrazione del ristorante sul db ha avuto successo,
     * false altrimenti
     */
    public boolean addRistorante(String nome, String desc, String linkSito, String fascia, String spec, String address, String fotoPath, String fotoDescr) {
        return manager.addRistorante(this, nome, desc, linkSito, fascia, spec, address, fotoPath, fotoDescr);
    }

    @Override
    public String toString() {
        return getNomeCognome();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utente other = (Utente) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.cognome, other.cognome)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.avpath, other.avpath);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.nome);
        hash = 17 * hash + Objects.hashCode(this.email);
        return hash;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeCognome() {
        return nome + " " + cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvpath() {
        return avpath;
    }

    public void setAvpath(String avpath) {
        this.avpath = avpath;
    }

    /**
     * Per sapere se un utente è di tipo registrato
     *
     * @return true se l'utente è di tipo Registrato, false altrimenti
     */
    public boolean isRegistrato() {
        return this instanceof Registrato;
    }

    /**
     * Per sapere se un utente è di tipo Amministratore
     *
     * @return true se l'utente è di tipo Amministratore, false altrimenti
     */
    public boolean isAmministratore() {
        return this instanceof Amministratore;
    }

    /**
     * Per sapere se un utente è di tipo Ristoratore
     *
     * @return true se l'utente è di tipo Ristoratore, false altrimenti
     */
    public boolean isRistoratore() {
        return this instanceof Ristoratore;
    }
    
    /**
     * Per sapere se un utente è uno registrato, amministratore o ristoratore
     *
     * @return true se l'utente è loggato
     */
    public boolean isLogged() {
        return isRegistrato() || isAmministratore() || isRistoratore();
    }
    

}
