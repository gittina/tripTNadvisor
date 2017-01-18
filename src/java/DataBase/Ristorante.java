/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 *
 *
 * @author Luca
 */
public class Ristorante implements Serializable {

    transient private final DBManager manager;
    transient private final Connection con;

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
    public Utente getUtente() {
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
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.linksito = linksito;
        this.fascia = fascia;
        this.cucina = cucina;
        this.utente = utente;
        this.manager = manager;
        this.visite = visite;
        this.con = manager.con;
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
     * Aggiunge una visita al ristorante
     *
     * @return
     */
    public boolean addVisita() {
        boolean res = false;
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("update ristorante set visite = visite+1 where id = ?");
            stm.setInt(1, getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        visite++;
        return res;
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
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("update ristorante set nome = ?, linksito = ?, descr = ?, cucina = ?, fascia = ? where id = ?");
            stm.setString(1, nome);
            stm.setString(2, linksito);
            stm.setString(3, descr);
            stm.setString(4, cucina);
            stm.setString(5, fascia);
            stm.setInt(6, getId());
            stm.executeUpdate();
            res = setLuogo(address);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (res) {
            res = manager.updateAutocomplete();
        }
        return res;
    }

    /**
     * Per settare l'indirizzo del ristorante
     *
     * @param address l'indirizzo del ristorante
     * @return true se la posizione del ristorante è stata impostata
     * correttamente, false altrimenti
     */
    public boolean setLuogo(String address) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("delete from Luogo where id = (select id_luogo from ristorante where id = ?)");
            stm.executeUpdate();

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

            stm = con.prepareStatement("update ristorante set id_luogo = ? where id = ?");
            stm.setInt(1, luogo_id);
            stm.setInt(2, getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    /**
     * Funzione che calcola il voto del ristorante come media di tutte le
     * valutazioni lasciate dagli utenti
     *
     * @return un float tra 0 e 5 che valuta la qualità del ristorante
     */
    public float getVoto() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        float res = 0;
        try {
            stm = con.prepareStatement("SELECT avg(1.0 * rating) AS mediavoto FROM votorist WHERE id_rist=?");
            stm.setInt(1, getId());
            rs = stm.executeQuery();
            if (rs.next()) {
                res = rs.getFloat("mediavoto");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Funzione che calcola la posizione in classifica instantanea di un
     * ristorante
     *
     * @return la posizione in classifica del ristorante
     */
    public int getPosizioneClassifica() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        int res = 0;
        try {
            stm = con.prepareStatement("select avg(1.0 * votorist.rating) as media, ristorante.ID as id_rist from (Ristorante) Left Join (votorist) on (ristorante.ID = votorist.ID_RIST) group by (ristorante.ID) order by media asc");
            rs = stm.executeQuery();
            while (rs.next()) {
                res++;
                if (rs.getInt("id_rist") == getId()) {
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     *
     * @param giorno giorno a cui è riferito l'orario, 1 = Lunedì, 2 = Martedì,
     * .... , 7 = Domenica
     * @param inizio Time dell'inizio dell'orario di apertura
     * @param fine Time della fine dell'orario di apertura
     * @return
     */
    public boolean addTimesToRistorante(int giorno, Time inizio, Time fine) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("select * from days where id_rist = ? AND giorno = ?");
            stm.setInt(1, getId());
            stm.setInt(2, giorno);
            rs = stm.executeQuery();
            if (rs.next()) {
                Days d = new Days(rs.getInt("id"), manager.getRistorante(rs.getInt("id_rist")), rs.getInt("giorno"), manager); //////il secondo parametro this??
                d.addTimes(inizio, fine);
            } else if (addDays(giorno)) {
                stm = con.prepareStatement("select * from days where id_rist = ? AND giorno = ?");
                stm.setInt(1, getId());
                stm.setInt(2, giorno);
                rs = stm.executeQuery();
                if (rs.next()) {
                    Days d = new Days(rs.getInt("id"), manager.getRistorante(rs.getInt("id_rist")), rs.getInt("giorno"), manager); //////il secondo parametro this??
                    d.addTimes(inizio, fine);
                }
            }
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    public boolean addDays(int giorno) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into days (giorno, id_rist) values (?,?)");
            stm.setInt(1, giorno);
            stm.setInt(2, getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    public boolean removeTimes(int id_times) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("delete from times where id = ?");
            stm.setInt(1, id_times);
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Per ricevere l'oggetto Luogo riferito a questo ristorante
     *
     * @return L'oggetto Luogo riferito a questo ristorante
     */
    public Luogo getLuogo() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Luogo res = null;
        try {
            stm = con.prepareStatement("select * from Luogo, Ristorante where ristorante.id = ? AND ristorante.id_luogo = luogo.id");
            stm.setInt(1, getId());
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Luogo(rs.getDouble("lat"), rs.getDouble("lng"), rs.getString("address"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Per ottenere tutti gli orari di questo ristorante
     *
     * @return tutti gli orari del ristorante
     */
    public ArrayList<Days> getDays() {
        PreparedStatement stm = null;
        ArrayList<Days> res = new ArrayList<>();
        ResultSet rs = null;
        try {
            stm = con.prepareStatement("SELECT * FROM Days WHERE id_rist = ?");
            stm.setInt(1, getId());
            rs = stm.executeQuery();

            while (rs.next()) {
                res.add(new Days(rs.getInt("id"), manager.getRistorante(rs.getInt("id_rist")), rs.getInt("giorno"), manager));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Comparator c = (Comparator<Days>) new Comparator<Days>() {
            @Override
            public int compare(Days o1, Days o2) {
                return o1.getGiorno() > o2.getGiorno() ? 1 : -1;
            }
        };
        res.sort(c);
        return res;
    }

    /**
     * Per ottenere tutte le recensioni lasciate dagli utenti a questo
     * ristorante
     *
     * @return Lista di Recensioni
     */
    public ArrayList<Recensione> getRecensioni() {
        PreparedStatement stm = null;
        ArrayList<Recensione> res = new ArrayList<>();
        ResultSet rs = null;
        try {
            stm = con.prepareStatement("SELECT * FROM RECENSIONE WHERE id_rist = ?");
            stm.setInt(1, getId());
            rs = stm.executeQuery();

            while (rs.next()) {
                res.add(new Recensione(rs.getInt("id"), this, manager.getUtente(rs.getInt("id_utente")), rs.getString("titolo"), rs.getString("testo"), rs.getDate("data"), rs.getString("commento"), rs.getString("fotopath"), manager));
            }
            Comparator c = (Comparator<Recensione>) (Recensione o1, Recensione o2) -> {
                if (o1.getData().after(o2.getData())) {
                    return -1;
                } else if (o1.getData().equals(o2.getData())) {
                    return 0;
                } else {
                    return 1;
                }
            };
            res.sort(c);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Questa funzione permette di aggiungere una recensione a questo ristorante
     *
     * @param titolo titolo della recensione
     * @param testo testo o corpo della recensione
     * @param utente utente che scrive la recensione
     * @return l'oggetto recensione appena creato
     */
    public Recensione addRecensione(String titolo, String testo, Utente utente) {
        Recensione res = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Date current = Date.valueOf(LocalDate.now());
        try {
            stm = con.prepareStatement("INSERT INTO RECENSIONE (titolo,testo,data,id_utente,id_rist) VALUES (?,?,?,?,?)");
            stm.setString(1, titolo);
            stm.setString(2, testo);
            stm.setDate(3, current);
            stm.setInt(4, utente.getId());
            stm.setInt(5, getId());
            stm.executeUpdate();

            stm = con.prepareStatement("SELECT * FROM RECENSIONE where id_utente = ? AND id_rist = ? ");
            stm.setInt(1, utente.getId());
            stm.setInt(2, getId());
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Recensione(rs.getInt("id"), this, utente, rs.getString("titolo"), rs.getString("testo"), rs.getDate("data"), rs.getString("commento"), rs.getString("fotopath"), manager);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Funzione per l'aggiunta di una foto a questo ristorante
     *
     * @param path path della foto da aggiungere
     * @param descr piccola descrizione della foto
     * @param utente utente che aggiunge la foto
     * @return true se l'aggiunta ha avuto successo, false altrimenti
     */
    public boolean addFoto(String path, String descr, Utente utente) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("INSERT INTO FOTO (fotopath, descr, data, id_rist, id_utente) VALUES (?,?,?,?,?)");
            stm.setString(1, path);
            stm.setString(2, descr);
            Date current = Date.valueOf(LocalDate.now());
            stm.setDate(3, current);
            stm.setInt(4, getId());
            stm.setInt(5, utente.getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Funzione per rimuovere una foto da questo ristorante
     *
     * @param foto foto da rimuovere
     * @return true se la rimozione ha avuto successo, false altrimenti
     */
    public boolean removeFoto(Foto foto) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("DELETE FROM FOTO WHERE id = ?");
            stm.setInt(1, foto.getId());
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Funione per ottenere una lista di tutte le foto che sono state aggiunte a
     * questo ristorante
     *
     * @return ArrayList di foto di questo ristorante
     */
    public ArrayList<Foto> getFoto() {
        ArrayList<Foto> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = con.prepareStatement("select * from foto where id_rist = ?");
            stm.setInt(1, getId());
            rs = stm.executeQuery();
            while (rs.next()) { //int id, String fotopath, String descr, Date data
                res.add(new Foto(rs.getInt("id"), rs.getString("fotopath"), rs.getString("descr"), rs.getDate("data"), manager.getUtente(rs.getInt("id_utente")), this, manager));
            }
            Comparator c = (Comparator<Foto>) (Foto o1, Foto o2) -> {
                if (o1.getData().after(o2.getData())) {
                    return -1;
                } else if (o1.getData().equals(o2.getData())) {
                    return 0;
                } else {
                    return 1;
                }
            };
            res.sort(c);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Crea un immagine QR con le seguenti informazioni: - Nome - Indirizzo -
     * Orari di apertura
     *
     * @return il path per accedere all'immagine creata
     */
    public String creaQR() {

        String pos = "/qr/" + this.getName().replace(' ', '_') + ".jpg";
        String savePath = manager.completePath + "/web" + pos;
        System.out.println(savePath);

        ArrayList<Days> days = getDays();

        String forQR = "Nome ristorante: " + this.getName().trim() + "\n" + "Indirizzo: " + getLuogo().getAddress() + "\n";

        for (Days o : days) {
            for (Times t : o.getTimes()) {
                forQR = forQR + t.toString() + '\n';
            }
        }

        ByteArrayOutputStream out = QRCode.from(forQR).to(ImageType.JPG).stream();
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File(savePath));
            fout.write(out.toByteArray());

            fout.flush();
            fout.close();
        } catch (IOException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            pos = null;
        }
        return pos;
    }

    /**
     * Aggiunge un voto a questo ristorante
     *
     * @param user utente che aggiunge il voto
     * @param rating voto da 0 a 5 compresi
     * @return true se il voto è stato aggiunto con successo, false altrimenti
     */
    public boolean addVoto(Utente user, int rating) {
        PreparedStatement stm = null;
        Date current = Date.valueOf(LocalDate.now());
        boolean res = false;
        if (user.justVotatoOggi(this)) {
            res = false;
        } else {
            try {

                stm = con.prepareStatement("INSERT INTO votorist (id_utente, id_rist, data, rating) VALUES (?,?,?,?)");
                stm.setInt(1, user.getId());
                stm.setInt(2, getId());
                stm.setDate(3, current);
                stm.setInt(4, rating);
                stm.executeUpdate();
                res = true;
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Crea una nuova notifica di tipo ReclamaRistorante sul DB per permettere
     * ad un amministratore di verificare se questo utente è il reale
     * proprietario del ristorante
     *
     * @param utente l'utente che vuole reclamare quel ristorante
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotReclamaRistorante(Utente utente) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into richiestaristorante(id_rist, id_utente, data) values (?,?,?)");
            stm.setInt(1, getId());
            stm.setInt(2, utente.getId());
            stm.setDate(3, new Date(System.currentTimeMillis()));
            stm.executeUpdate();
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    public ArrayList<Ristorante> getVicini() {
        return manager.advSearch2(this.getLuogo().getLat(), this.getLuogo().getLng());
    }

    //da lavorare
    public boolean nowOpen() {
        return true;
    }
}
