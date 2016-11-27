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
import java.sql.ResultSet;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("update ristorante set visite = visite+1 where id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
            stm.close();
            visite++;
        } catch (Exception ex) {
        }
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
     * @throws IOException
     */
    public boolean updateData(String nome, String address, String linksito, String descr, String cucina, String fascia) throws IOException {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("update ristorante set nome = ?, linksito = ?, descr = ?, cucina = ?, fascia = ? where id = ?");
            stm.setString(1, nome);
            stm.setString(2, linksito);
            stm.setString(3, descr);
            stm.setString(4, cucina);
            stm.setString(5, fascia);
            stm.setInt(6, id);
            stm.executeUpdate();
            setLuogo(address);
            stm.close();

        } catch (SQLException ex) {
            System.out.println("Update non riuscito del profilo");
            return false;
        }
        manager.update();
        return true;
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
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean setLuogo(String address) throws SQLException, FileNotFoundException, IOException {
        PreparedStatement stm;
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
            ResultSet rs = stm.executeQuery();
            int luogo_id;
            if (rs.next()) {
                luogo_id = rs.getInt("id");
            } else {
                throw new SQLException();
            }

            stm = con.prepareStatement("update ristorante set id_luogo = ? where id = ?");
            stm.setInt(1, luogo_id);
            stm.setInt(2, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Impossibile modificare luogo");
            return false;
        }
        return true;
    }

    /**
     * Funzione che calcola il voto del ristorante come media di tutte le
     * valutazioni lasciate dagli utenti
     *
     * @return un float tra 0 e 5 che valuta la qualità del ristorante
     * @throws SQLException
     */
    public float getVoto() throws SQLException {
        PreparedStatement stm;
        ResultSet rs;
        try {
            stm = con.prepareStatement("SELECT avg(1.0 * rating) AS mediavoto FROM votorist WHERE id_rist=?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            float res;
            if (rs.next()) {
                res = rs.getFloat("mediavoto");
            } else {
                res = 0;
            }
            stm.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione voto");
            return 0;
        }
    }

    /**
     * Funzione che calcola la posizione in classifica instantanea di un
     * ristorante
     *
     * @return la posizione in classifica del ristorante
     * @throws SQLException
     */
    public int getPosizioneClassifica() throws SQLException {
        PreparedStatement stm;
        ResultSet rs;
        try {
            stm = con.prepareStatement("select avg(1.0 * votorist.rating) as media, ristorante.ID as id_rist from (Ristorante) Left Join (votorist) on (ristorante.ID = votorist.ID_RIST) group by (ristorante.ID) order by media asc");
            rs = stm.executeQuery();
            int counter = 0;
            while (rs.next()) {
                counter++;
                if (rs.getInt("id_rist") == id) {
                    return counter;
                }
            }
            stm.close();
        } catch (SQLException ex) {
            return -1;
        }

        return -1;
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
    public boolean addOrario(int giorno, Time inizio, Time fine) throws SQLException {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("insert into orario (id_rist,giorno,apertura,chiusura) values (?,?,?,?)");
            stm.setInt(1, id);
            stm.setInt(2, giorno);
            stm.setTime(3, inizio);
            stm.setTime(4, fine);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("problema aggiunta orario");
            return false;
        }
        return true;
    }

    public boolean removeOrario(int id_orario) {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("delete from orario where id = ?");
            stm.setInt(1, id_orario);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("problema rimozione orario con id: " + id_orario);
            return false;
        }
        return true;
    }

    /**
     * Per ricevere l'oggetto Luogo riferito a questo ristorante
     *
     * @return L'oggetto Luogo riferito a questo ristorante
     */
    public Luogo getLuogo() {
        PreparedStatement stm;
        ResultSet rs;
        Luogo res = null;
        try {
            stm = con.prepareStatement("select * from Luogo, Ristorante where ristorante.id = ? AND ristorante.id_luogo = luogo.id");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Luogo(rs.getDouble("lat"), rs.getDouble("lng"), rs.getString("address"));
            }
            stm.close();
        } catch (SQLException ex) {
            System.out.println("problema estrazione luogo");
        }
        return res;
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
    public ArrayList<Orario> getOrario() throws SQLException {
        PreparedStatement stm;
        ArrayList<Orario> res = new ArrayList<>();
        ResultSet rs;

        try {
            stm = con.prepareStatement("SELECT * FROM Orario WHERE id_rist = ? ORDER BY giorno ASC");
            stm.setInt(1, id);
            rs = stm.executeQuery();

            while (rs.next()) {
                res.add(new Orario(rs.getInt("id"), manager.getRistorante(rs.getInt("id_rist")), rs.getInt("giorno"), rs.getTime("apertura"), rs.getTime("chiusura")));
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione orari");
            return null;
        }
        Comparator c = (Comparator<Orario>) new Comparator<Orario>() {
            @Override
            public int compare(Orario o1, Orario o2) {
                if (o1.getGiorno() == o2.getGiorno()) {
                    return o1.getApertura().after(o2.getApertura()) ? 1 : -1;
                } else {
                    return o1.getGiorno() > o2.getGiorno() ? 1 : -1;
                }
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
     * @throws SQLException
     */
    public ArrayList<Recensione> getRecensioni() throws SQLException {
        PreparedStatement stm;
        ArrayList<Recensione> res = new ArrayList<>();
        ResultSet rs;

        try {
            stm = con.prepareStatement("SELECT * FROM RECENSIONE WHERE id_rist = ?");
            stm.setString(1, String.valueOf(id));
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
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione recensioni");
            return null;
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
     * @throws SQLException
     */
    public Recensione addRecensione(String titolo, String testo, Utente utente) throws SQLException {
        Recensione res;
        PreparedStatement stm;
        ResultSet rs;
        Date current = Date.valueOf(LocalDate.now());
        try {
            stm = con.prepareStatement("INSERT INTO RECENSIONE (titolo,testo,data,id_utente,id_rist) VALUES (?,?,?,?,?)");
            stm.setString(1, titolo);
            stm.setString(2, testo);
            stm.setDate(3, current);
            stm.setInt(4, utente.id);
            stm.setInt(5, id);
            stm.executeUpdate();

            stm = con.prepareStatement("SELECT * FROM RECENSIONE where id_utente = ? AND id_rist = ? ");
            stm.setInt(1, utente.id);
            stm.setInt(2, id);
            rs = stm.executeQuery();
            rs.next();
            res = new Recensione(rs.getInt("id"), this, utente, rs.getString("titolo"), rs.getString("testo"), rs.getDate("data"), rs.getString("commento"), rs.getString("fotopath"), manager);
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("Fallita estrazione recensione su DB" + ex.toString());
            return null;
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
     * @throws SQLException
     */
    public boolean addFoto(String path, String descr, Utente utente) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO FOTO (fotopath, descr, data, id_rist, id_utente) VALUES (?,?,?,?,?)");
        try {
            stm.setString(1, path);
            stm.setString(2, descr);
            Date current = Date.valueOf(LocalDate.now());
            stm.setDate(3, current);
            stm.setInt(4, id);
            stm.setInt(5, utente.id);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("Fallita aggiunta foto a ristorante su DB");
            return false;
        }
    }

    /**
     * Funzione per rimuovere una foto da questo ristorante
     *
     * @param foto foto da rimuovere
     * @return true se la rimozione ha avuto successo, false altrimenti
     */
    public boolean removeFoto(Foto foto) {
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("DELETE FROM FOTO WHERE id = ?");
            stm.setInt(1, foto.getId());
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("Fallita rimozione foto id: " + id);
            return false;
        }
    }

    /**
     * Funione per ottenere una lista di tutte le foto che sono state aggiunte a
     * questo ristorante
     *
     * @return ArrayList di foto di questo ristorante
     * @throws SQLException
     */
    public ArrayList<Foto> getFoto() throws SQLException {
        ArrayList<Foto> res = new ArrayList<>();
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("select * from foto where id_rist = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { //int id, String fotopath, String descr, Date data
                res.add(new Foto(rs.getInt("id"), rs.getString("fotopath"), rs.getString("descr"), rs.getDate("data"), manager.getUtente(rs.getInt("id_utente")), manager.getRistorante(rs.getInt("id_rist")), manager));
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
            stm.close();
        } catch (SQLException ex) {
            return null;
        }
        return res;
    }

    /**
     * Crea un immagine QR con le seguenti informazioni: - Nome - Indirizzo -
     * Orari di apertura
     *
     * @return il path per accedere all'immagine creata
     * @throws SQLException
     */
    public String creaQR() throws SQLException {

        String pos = "/qr/" + name.replace(' ', '_') + ".jpg";
        String savePath = manager.completePath + pos;

        ArrayList<Orario> orari = getOrario();

        String forQR = "Nome ristorante: " + name.trim() + "\n" + "Indirizzo: " + getLuogo().getAddress() + "\n";

        for (Orario o : orari) {
            forQR = forQR + o.toString();
        }

        ByteArrayOutputStream out = QRCode.from(forQR).to(ImageType.JPG).stream();
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File(savePath));
            fout.write(out.toByteArray());

            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            System.out.println("Errore: " + e.toString());
            System.out.println("Errore nella creazione dell'immagine QR");
        } catch (IOException e) {
            System.out.println("Errore nella creazione QR");
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
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("INSERT INTO votorist (id_utente, id_rist, data, rating) VALUES (?,?,?,?)");
            stm.setInt(1, user.getId());
            stm.setInt(2, id);
            Date current = Date.valueOf(LocalDate.now());
            if (user.justVotatoOggi(this)) {
                return false;
            }
            stm.setDate(3, current);
            stm.setInt(4, rating);
            stm.executeUpdate();
            stm.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("Fallita aggiunta voto");
            return false;
        }
    }

}
