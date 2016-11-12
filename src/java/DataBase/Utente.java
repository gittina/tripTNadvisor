/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Notify.Notifica;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Luca
 */
public abstract class Utente {

    protected int id;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String avpath;
    protected final DBManager manager;
    protected final Connection con;

    public Utente(int id, String nome, String cognome, String email, String avpath, DBManager manager) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.avpath = avpath;
        this.manager = manager;
        con = manager.con;
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
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("update utente set nome = ?, cognome = ?, email = ?, avpath = ? where id = ?");
            stm.setString(1, nome);
            stm.setString(2, cognome);
            stm.setString(3, email);
            stm.setString(4, avpath);
            stm.setInt(5, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /**
     * Serve a permettere ad un utente di cambiare la sua password
     *
     * @param nuovaPass nuova password dell'utente
     * @return true se è andato tutto bene, false altrimenti
     * @throws SQLException
     */
    public boolean cambiaPassword(String nuovaPass) throws SQLException {
        PreparedStatement stm;
        ResultSet rs;
        try {
            stm = con.prepareStatement("UPDATE utente SET password = ? where id = ?");
            stm.setString(1, nuovaPass);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    
    public int numeroRecensioni(){
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select count(*) as tot from recensione where id_utente = ?");
            stm.setInt(1, id);
            int res;
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getInt("tot");
                } else {
                    res = -1;
                }
            }
            stm.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione numero recensioni utente");
            return -1;
        }
    }
    
    
    public boolean justSegnalataFoto(Foto foto){
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select ");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
           
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione justSegnalato utente " + this);
            return true;
        }
    }
    
    /**
     * Calcola la reputazione di un utente sulla media dei voti delle recensioni
     * che ha lasciato
     *
     * @return un float tra 0 e 5 alle recensioni di questo utente
     */
    public float getReputazione() {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select avg(res.parz) as media from( select avg(voto.rating) as parz from (select * from recensione where id_utente = ?) as rec, votorec as voto where rec.id = voto.ID_REC group by rec.id) as res group by res.parz");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            float res;
            if (rs.next()) {
                res = rs.getFloat("media");
            } else {
                res = -1;
            }
            stm.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione reputazione utente");
            return -1;
        }
    }

    /**
     *
     * @param ristorante ristorante per cui ci vuole controllare se l'utente ha
     * già rilasciato una recensione
     * @return true se questo utente ha già recensito questo ristorante, false
     * altrimenti
     */
    public boolean justRecensito(Ristorante ristorante) {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from recensione where id_rist = ? AND id_utente = ?");
            stm.setInt(1, ristorante.getId());
            stm.setInt(2, id);
            ResultSet rs = stm.executeQuery();
            boolean res = rs.next();
            stm.close();
            return res;
        } catch (Exception ex) {
            return true;
        }
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
        PreparedStatement stm;
        ResultSet rs;
        boolean res;
        try {
            Date now = new Date(System.currentTimeMillis());
            stm = con.prepareStatement("select data from votorist where id_utente = ? AND id_rist = ? order by data desc { limit 1 }");
            stm.setInt(1, id);
            stm.setInt(2, ristorante.getId());
            rs = stm.executeQuery();
            if (rs.next()) {
                Date d = rs.getDate("data");
                res = d.getDay() == now.getDay() && d.getMonth() == now.getMonth() && d.getYear() == now.getYear();
            } else {
                res = false;
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            return true;
        }
        return res;
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
        if (this.getClass() == Amministratore.class) {
            System.out.println("Sono amministratore dentro isActivate()");
            return true;
        }
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select attivato from Utente where id = ?");
            stm.setInt(1, getId());
            ResultSet rs = stm.executeQuery();
            boolean res;

            if (rs.next()) {
                res = rs.getBoolean("attivato");
                
            } else {
                res = false;
            }
            System.out.println("isActivate().res: "+res);
            stm.close();
            return res;
        } catch (SQLException ex) {
            return false;
        }
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
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean addRistorante(String nome, String desc, String linkSito, String fascia, String spec, String address, String fotoPath, String fotoDescr) throws SQLException, FileNotFoundException, IOException {
        PreparedStatement stm;
        ResultSet rs;
        try {

            Luogo luogo = manager.getLuogo(address);

            stm = con.prepareStatement("INSERT INTO Luogo (address,lat,lng) VALUES (?,?,?)");
            stm.setString(1, luogo.getAddress());
            stm.setDouble(2, luogo.getLat());
            stm.setDouble(3, luogo.getLng());
            stm.executeUpdate();

            stm = con.prepareStatement("select id from Luogo where lat = ? AND lng = ?");
            stm.setDouble(1, luogo.getLat());
            stm.setDouble(2, luogo.getLng());
            rs = stm.executeQuery();
            int luogo_id;
            if (rs.next()) {
                luogo_id = rs.getInt("id");
            } else {
                throw new SQLException();
            }

            stm = con.prepareStatement("INSERT INTO Ristorante (nome,descr,linksito,cucina,fascia,id_luogo) VALUES (?,?,?,?,?,?)");
            stm.setString(1, nome);
            stm.setString(2, desc);
            stm.setString(3, linkSito);
            stm.setString(4, spec);
            stm.setString(5, fascia);
            stm.setInt(6, luogo_id);
            stm.executeUpdate();

            stm = con.prepareStatement("select id from Ristorante where nome = ? AND linkSito = ?");
            stm.setString(1, nome);
            stm.setString(2, linkSito);
            rs = stm.executeQuery();
            Ristorante res;
            if (rs.next()) {
                res = manager.getRistorante(rs.getInt("id"));
            } else {
                throw new SQLException();
            }

            stm = con.prepareStatement("INSERT INTO Foto (fotoPath,data,descr,id_rist,id_utente) VALUES (?,?,?,?,?)");
            Date sqlDate = new Date(System.currentTimeMillis());
            stm.setString(1, fotoPath);
            stm.setDate(2, sqlDate);
            stm.setString(3, fotoDescr);
            stm.setInt(4, res.getId());
            stm.setInt(5, id);
            stm.executeUpdate();
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita registrazione ristorante su DB");
            return false;
        }
        manager.update();
        return true;
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