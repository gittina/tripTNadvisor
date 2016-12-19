package DataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DBManager implements Serializable {

    //transient == non viene serializzato
    public transient Connection con;
    private String googleKey = "AIzaSyA7spDhgAtLeyh6b0F6MQI2I5fldqrR6oM";
    public String contextPath;
    public String completePath;

    public DBManager(String dburl, String contextPath, String completePath) {
        this.contextPath = contextPath;
        this.completePath = completePath;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
        String user = "progettoTNadvisor";
        String pass = "bRjhdsoR56ve";
        try {
            con = DriverManager.getConnection(dburl, user, pass);
        } catch (SQLException ex) {
            try {
                con = DriverManager.getConnection(dburl, user, pass);
            } catch (SQLException ex1) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Invocato per spegnere la connessione al DB
     */
    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }

    /////////////////// METODI GENERALI //////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    /**
     * Metodo per verificare l'autenticazione di un utente
     *
     * @param email email dell'utente a cui fare il login
     * @param password password dell'utente
     * @return un oggetto Utente se la email e la password corrispondono ad un
     * Utente nel DB, null altrimenti
     */
    public Utente authenticate(String email, String password) {
        PreparedStatement stm = null;
        ResultSet rs;
        Utente res = null;
        try {
            stm = con.prepareStatement("SELECT id FROM UTENTE WHERE email = ? AND password = ?");
            stm.setString(1, email);
            stm.setString(2, password);

            rs = stm.executeQuery();
            if (rs.next()) {
                res = getUtente(rs.getInt("id"));
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
        }
        return res;
    }

    /**
     * Per ottenere le ultime (fino a) 5 recensioni lasciate sul portale
     *
     * @return un ArrayList di 5 recensioni
     */
    public ArrayList<Recensione> getUltimeRecensioni() {
        ArrayList<Recensione> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //int id, String titolo, String testo, Date data, String commento, String fotoPath, DBManager manager
            stm = con.prepareStatement("select * from recensione order by data desc { limit 5 }");
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Recensione(rs.getInt("id"), getRistorante(rs.getInt("id_rist")), getUtente(rs.getInt("id_utente")), rs.getString("titolo"), rs.getString("testo"), rs.getDate("data"), rs.getString("commento"), rs.getString("fotopath"), this));
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
     * Per ottenere fino a 5 ristoranti con migliori voti (calcolati come media
     * dei voti lasciati dagli utenti ad ogni ristorante)
     *
     * @return un ArrayList di 5 Ristoranti
     */
    public ArrayList<Ristorante> getRistorantiPiuVotati() {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //int id, String titolo, String testo, Date data, String commento, String fotoPath, DBManager manager
            stm = con.prepareStatement("select * from( select ristorante.ID, avg(rating) as media from votorist, ristorante where ristorante.ID = votorist.ID_RIST group by ristorante.ID) as res, ristorante as ristorante where res.id = ristorante.ID order by res.media DESC");
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
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
     * Per ottenere fino a 5 ristoranti che hanno ricevuto più visite
     *
     * @return un ArrayList di 5 Ristoranti
     */
    public ArrayList<Ristorante> getRistorantiPiuVisitati() {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //int id, String titolo, String testo, Date data, String commento, String fotoPath, DBManager manager
            stm = con.prepareStatement("SELECT * FROM ristorante order by ristorante.VISITE desc { limit 5 }");
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
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
     * Per controllare se un utente con quella mail è già registrato
     *
     * @param email mail da controllare
     * @return true se esiste già un utente registrato con quella mail, false
     * altrimenti
     */
    public boolean esisteMail(String email) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("select * from Utente where email = ?");
            stm.setString(1, email);
            rs = stm.executeQuery();
            res = rs.next();
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

    public boolean addKey(Utente utente, String key) {
        PreparedStatement stm = null;
        Boolean res = false;
        try {
            stm = con.prepareStatement("insert into Validation (id_utente,chiave) values (?,?)");
            stm.setInt(1, utente.getId());
            stm.setString(2, key);
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
     * Per controllare se esiste già un Ristorante con quel nome registrato nel
     * portale
     *
     * @param nome nome del ristorante da controllare
     * @return true se esiste già nel portale un ristorante con quel nome, false
     * altrimenti
     */
    public boolean esisteNomeRistorante(String nome) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("select * from Ristorante where nome = ?");
            stm.setString(1, nome);
            rs = stm.executeQuery();
            res = rs.next();
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
     * Per controllare se esiste già un ristorante con quel link al sito web nel
     * portale
     *
     * @param link link da controllare
     * @return true se esiste già nel portale un ristorante con quel link al
     * sito web, false altrimenti
     */
    public boolean esisteLinkSitoRistorante(String link) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("select * from Ristorante where linksito = ?");
            stm.setString(1, link);
            rs = stm.executeQuery();
            res = rs.next();
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
     * Aggiunte un nuovo utente al portale
     *
     * @param nome nome del nuovo utente
     * @param cognome cognome del nuovo utente
     * @param email email del nuovo utente
     * @param password password del nuovo utente
     * @return l'oggetto Utente per la sessione
     */
    public Utente addRegistrato(String nome, String cognome, String email, String password) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Utente utente = null;
        try {
            stm = con.prepareStatement("INSERT INTO UTENTE (nome,cognome,email,password,attivato,avpath) VALUES (?,?,?,?,?,?)");
            stm.setString(1, nome);
            stm.setString(2, cognome);
            stm.setString(3, email);
            stm.setString(4, password);
            stm.setBoolean(5, false);
            stm.setString(6, "/img/utente-generico.jpg");
            stm.executeUpdate();

            stm = con.prepareStatement("select * from utente where email = ?");
            stm.setString(1, email);
            rs = stm.executeQuery();
            if (rs.next()) {
                utente = (getUtente(rs.getInt("id")));
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
        return utente;
    }

    /**
     * Questa funzione calcola la classifica istantanea degli utenti. Il
     * punteggio di ogni utente è calcolato come media dei voti che sono stati
     * dati alle sue recensioni
     *
     * @return un ArrayList di tutti gli utentu del portale, ordinati per
     * Classifica
     */
    public ArrayList<Utente> getClassificaUtenti() {
        ArrayList<Utente> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = con.prepareStatement("select id from utente where amministratore = ?");
            stm.setBoolean(1, false);
            rs = stm.executeQuery();

            while (rs.next()) {
                Utente u = getUtente(rs.getInt("id"));
                res.add(u);
            }
            Comparator c = (Comparator<Utente>) (Utente o1, Utente o2) -> {
                float rep1 = o1.getReputazione(), rep2 = o2.getReputazione();
                if (rep1 > rep2) {
                    return -1;
                } else if (rep1 < rep2) {
                    return 1;
                } else {
                    return 0;
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
     * Controlla che un indirizzo venga riconosciuto correttamente da Google
     * Maps
     *
     * @param address indirizzo da controllare
     * @return true se l'indirizzo può essere elaborato e trasformato in
     * coordinate geografiche da google maps, false altrimenti
     */
    public boolean okLuogo(String address) {
        boolean res = false;
        try {
            String req = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + address.replace(' ', '+') + "&key=" + googleKey;
            URL website = new URL(req);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream(completePath + "/geo/georef.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            File fXmlFile = new File(completePath + "/geo/georef.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            double lat = 0, lng = 0;
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("location");
            Node nNode = nList.item(0);

            if (nNode == null) {
                res = false;
            } else if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                lat = Double.parseDouble(eElement.getElementsByTagName("lat").item(0).getTextContent());
                lng = Double.parseDouble(eElement.getElementsByTagName("lng").item(0).getTextContent());
                res = true;
            }

        } catch (ParserConfigurationException | IOException | SAXException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * Costruisce un oggetto di tipo Luogo sulla base dell'indirizzo fornito
     *
     * @param address indirizzo su cui costruire l'oggetto Luogo
     * @return l'oggetto Luogo costruito
     */
    public Luogo getLuogo(String address) {
        Luogo res = null;
        try {
            String req = "https://maps.googleapis.com/maps/api/geocode/xml?" + "address=" + address.replace(' ', '+') + "&key=" + googleKey;
            URL website = new URL(req);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream(completePath + "/geo/georef.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            File fXmlFile = new File(completePath + "/geo/georef.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            double lat = 0, lng = 0;
            String formatted_address = "";
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("location");
            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                lat = Double.parseDouble(eElement.getElementsByTagName("lat").item(0).getTextContent());
                lng = Double.parseDouble(eElement.getElementsByTagName("lng").item(0).getTextContent());
            }

            nList = doc.getElementsByTagName("result");
            nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                formatted_address = eElement.getElementsByTagName("formatted_address").item(0).getTextContent();
            }
            res = new Luogo(lat, lng, formatted_address);

        } catch (IOException | SAXException | ParserConfigurationException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * Funzione di ricerca di base, implementata nella barra di ricerca
     * secondaria e non specifica (quella in alto sulla nav-bar). Esegue la
     * ricerca del campo research tra tutti i nomi dei ristoranti e tra tutti
     * gli indirizzi dei ristoranti. E' possibile quindi utilizzarla per cercare
     * ristoranti per nome p per località
     *
     * @param research campo di ricerca
     * @return un ArrayList dei Ristoranti trovati
     */
    public ArrayList<Ristorante> simpleSearch(String research) {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            if (research != null) {

                research = research.toLowerCase();
                stm = con.prepareStatement("SELECT * FROM RISTORANTE");
                rs = stm.executeQuery();
                while (rs.next()) {
                    res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
                }
                Iterator i = res.iterator();
                while (i.hasNext()) {
                    Ristorante r = (Ristorante) i.next();
                    String name = r.getName().toLowerCase();
                    String addr = r.getLuogo().getAddress().toLowerCase();
                    String cucina = r.getCucina().toLowerCase();
                    if (!name.contains((CharSequence) research) && !addr.contains((CharSequence) research) && !cucina.contains((CharSequence) research)) {
                        i.remove();
                    }
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
     * Per recupare l'oggetto ristorante con quell'id
     *
     * @param id id del ristorante da ottenere
     * @return l'oggetto ristorante
     */
    public Ristorante getRistorante(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Ristorante res = null;
        try {
            stm = con.prepareStatement("select * from Ristorante where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Ristorante(id, rs.getString("nome"), rs.getString("descr"), rs.getString("linksito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite"));
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
     * Per ottenere la password di un utente
     *
     * @param utente utente di cui si vuole sapere la password
     * @return
     */
    public String getPassUtente(Utente utente) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        String res = null;
        try {
            stm = con.prepareStatement("select * from Utente where id = ?");
            stm.setInt(1, utente.getId());
            rs = stm.executeQuery();
            if (rs.next()) {
                res = rs.getString("password");
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
     * Per recupare l'oggetto recensione con quell'id
     *
     * @param id id della recensione da ottenere
     * @return l'oggetto recensione
     */
    public Recensione getRecensione(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Recensione res = null;
        try {
            stm = con.prepareStatement("select * from Recensione where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Recensione(rs.getInt("id"), getRistorante(rs.getInt("id_rist")), getUtente(rs.getInt("id_utente")), rs.getString("titolo"), rs.getString("testo"), rs.getDate("data"), rs.getString("commento"), rs.getString("fotopath"), this);
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
     * Per recupare l'oggetto utente con quell'id. Ad esso verrà effettuato un
     * downcast ad utente Registrato, Ristoratore, Amministratore secondo le
     * informazioni contenute nel DB
     *
     * @param id id dell'utente da ottenere
     * @return l'oggetto utente
     */
    public Utente getUtente(int id) {
        PreparedStatement stm = null;
        Utente res = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            stm = con.prepareStatement("select * from Utente where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("amministratore")) {
                    res = new Amministratore(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("avpath"), this);
                } else {
                    stm = con.prepareStatement("SELECT COUNT(*) as res FROM RISTORANTE WHERE id_utente = ?");
                    stm.setInt(1, id);
                    rs2 = stm.executeQuery();
                    if (rs2.next()) {
                        //Ristoratore(int id, String nome, String cognome, String email, String avpath){
                        if (rs2.getInt("res") > 0) {
                            res = new Ristoratore(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("avpath"), rs.getBoolean("attivato"), this);
                        } else {
                            res = new Registrato(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("avpath"), rs.getBoolean("attivato"), this);
                        }
                    }
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
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }

    /**
     * Attiva l'utente con il codice hash corrispondente nella tabella
     * Validation
     *
     * @param hash codice hash dell'utente da attivare
     * @return true se l'attivazione ha avuto successo, false altrimenti
     */
    public boolean activate(String hash) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("update utente set attivato = ? where id = (select id_utente from validation where chiave = ?)");
            stm.setBoolean(1, true);
            stm.setString(2, hash);
            stm.executeUpdate();
            stm = con.prepareStatement("delete from validation where chiave = ?");
            stm.setString(1, hash);
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
     * Assegna la proprietà di un ristorante ad un utente
     *
     * @param ristorante che verrà assegnato
     * @param utente che riceverà la proprietà del ristorante
     * @return
     */
    public boolean linkRistorante(Ristorante ristorante, Utente utente) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("update ristorante set id_utente = ? where id = ?");
            stm.setInt(1, utente.getId());
            stm.setInt(2, ristorante.getId());
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
     * Per recupare l'oggetto foto con quell'id
     *
     * @param id id della foto da ottenere
     * @return l'oggetto foto
     */
    public Foto getFoto(int id) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Foto res = null;
        try {
            stm = con.prepareStatement("select * from foto where id = ?");
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                res = new Foto(rs.getInt("id"), rs.getString("fotopath"), rs.getString("descr"), rs.getDate("data"), getUtente(rs.getInt("id_utente")), getRistorante(rs.getInt("id_rist")), this);
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

    public ArrayList<Ristorante> advSearch1(String research, String spec) {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            research = research.toLowerCase();
            stm = con.prepareStatement("SELECT * FROM RISTORANTE");
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
            }
            Iterator i = res.iterator();
            while (i.hasNext()) {
                Ristorante r = (Ristorante) i.next();
                switch (spec) {
                    case "nome":
                        String name = r.getName().toLowerCase();
                        if (!name.contains((CharSequence) research)) {
                            i.remove();
                        }
                        break;
                    case "addr":
                    case "zona":
                        String addr = r.getLuogo().getAddress().toLowerCase();
                        if (!addr.contains((CharSequence) research)) {
                            i.remove();
                        }
                        break;
                    case "spec":
                        String cucina = r.getCucina().toLowerCase();
                        if (!cucina.contains((CharSequence) research)) {
                            i.remove();
                        }
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

    public ArrayList<Ristorante> advSearch2(double lat, double lng) {
        ArrayList<Ristorante> res = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("select * from ristorante, (SELECT ristorante.id , sqrt((?-l.LAT)*(?-l.lat) + (?-l.LNG)*(?-l.LNG)) as distance FROM RISTORANTE as ristorante, Luogo as l where ristorante.id_luogo = l.id) as res where res.id = ristorante.id order by distance asc { limit 20 }");
            stm.setDouble(1, lat);
            stm.setDouble(2, lat);
            stm.setDouble(3, lng);
            stm.setDouble(4, lng);
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
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

    public ArrayList<Ristorante> advSearch3(String tipo) {
        ArrayList<Ristorante> res = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = con.prepareStatement("SELECT * FROM RISTORANTE where cucina = ?");
            stm.setString(1, tipo);
            rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), rs.getString("descr"), rs.getString("linkSito"), rs.getString("fascia"), rs.getString("cucina"), this, getUtente(rs.getInt("id_utente")), rs.getInt("visite")));
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

    public boolean updateAutocomplete() {
        String path = "/autocomplete.txt";
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("select ristorante.nome, l.address from ristorante as ristorante, luogo as l where ristorante.id_luogo = l.id");
            rs = stm.executeQuery();
            File file = new File(this.completePath + path);

            String content = "var ristoranti = {\n";
            int counter = 0;

            while (rs.next()) {
                content = content + "\t \"" + counter++ + "\": " + "\"" + rs.getString("nome") + "\", \n";
                content = content + "\t \"" + counter++ + "\": " + "\"" + rs.getString("address") + "\", \n";
            }

            stm = con.prepareStatement("select cucina from ristorante group by cucina");
            rs = stm.executeQuery();

            while (rs.next()) {
                content = content + "\t \"" + counter++ + "\": " + "\"" + rs.getString("cucina") + "\", \n";
            }

            content = content + "}";

            // if file doesn't exists, then create it
            try (FileOutputStream fop = new FileOutputStream(file)) {
                // if file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                // get the content in bytes
                byte[] contentInBytes = content.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                res = true;
            } catch (IOException e) {
                res = false;
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


    ///////////////// METODI PER NOTIFICHE ///////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    /**
     * Crea una nuova notifica di tipo NuovaRecensione sul DB, per notificare un
     * utente ristoratore che un suo ristorante ha ricevuto una nuova foto
     *
     * @param foto la foto che è stata aggiunta al suo ristorante
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotNuovaFoto(Foto foto) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into nuovafoto(id_foto, id_dest, data) values (?,?,?)");
            stm.setInt(1, foto.getId());
            stm.setInt(2, foto.getRistorante().getUtente().getId());
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

    /**
     * Crea una nuova notifica di tipo NuovaRecensione sul DB, per notificare un
     * utente ristoratore che un suo ristorante ha ricevuto una nuova recensione
     *
     * @param recensione la nuova recensione al suo ristorante
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotNuovaRecensione(Recensione recensione) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into nuovarecensione(id_rec, id_dest, data) values (?,?,?)");
            stm.setInt(1, recensione.getId());
            stm.setInt(2, recensione.getRistorante().getUtente().getId());
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
    
    /**
     * Crea una nuova notifica di tipo SegnalaFotoRecensione sul DB, verrà
     * estratta poi da un utente amministratore per essere verificata. Questa
     * notifica permette di far decidere ad un amministratore se la foto di
     * questa recensione è da togliere o meno
     *
     * @param recensione la recensione la cui foto vuole essere rimossa
     * dall'utente proprietario del ristorante sul quale è inserita la
     * recensione
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotSegnalaFotoRecensione(Recensione recensione) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into segnalafotorecensione(id_rec, data) values (?,?)");
            stm.setInt(1, recensione.getId());
            stm.setDate(2, new Date(System.currentTimeMillis()));
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
     * Crea una nuova notifica di tipo SegnalaFotoRistorante sul DB, verrà
     * estratta poi da un utente amministratore per essere verificata. Permette
     * ad un utente Ristoratore di segnalare una fotografia non consona alla
     * pagina del suo ristorante
     *
     * @param foto la fotografia del ristorante che si vuole segnalare
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotSegnalaFotoRistorante(Foto foto) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into segnalafotoristorante(id_foto, data) values (?,?)");
            stm.setInt(1, foto.getId());
            stm.setDate(2, new Date(System.currentTimeMillis()));
            stm.executeUpdate();
            res = false;
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
     * Crea una nuova notifica di tipo CommentoRecensione sul DB, verrà estratta
     * poi da un utente amministratore per essere verificata
     *
     * @param recensione la recensione a cui un utente ristoratore vuole
     * aggiungere il suo commento
     * @param commento il commento da aggiungere alla recensione
     * @return true se la notifica è stata registrata con successo sul DB, false
     * altriementi
     */
    public boolean newNotCommentoRecensione(Recensione recensione, String commento) {
        PreparedStatement stm = null;
        boolean res = false;
        try {
            stm = con.prepareStatement("insert into RispostaRecensione(id_rec, commento, data) values (?,?,?)");
            stm.setInt(1, recensione.getId());
            stm.setString(2, commento);
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
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
}
