package Servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DataBase.DBManager;
import DataBase.Recensione;
import DataBase.Ristorante;
import DataBase.Utente;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InserisciRecensioniServlet extends HttpServlet {

    private DBManager manager;
    private String dirName;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");

        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");
        if (dirName == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        Ristorante ristorante = (Ristorante) session.getAttribute("ristorante");

        MultipartRequest multi = new MultipartRequest(request, manager.completePath + "/web" + dirName, 10 * 1024 * 1024, "ISO-8859-1", new DefaultFileRenamePolicy());
        Enumeration files = multi.getFileNames();
        String name = null;
        while (files.hasMoreElements()) {
            name = (String) files.nextElement();
        }

        int voto = Integer.parseInt(multi.getParameter("voto"));
        String titolo = multi.getParameter("titolo");
        String recensione = multi.getParameter("rec");

        boolean tornaIndietro = false;

        if (titolo.length() < 3) {
            request.setAttribute("recMessage1", "Titolo troppo corto");
            tornaIndietro = true;
        }

        if (recensione.length() < 10) {
            request.setAttribute("recMessage2", "Recensione troppo corta");
            tornaIndietro = true;
        }

        if (tornaIndietro) {
            request.getRequestDispatcher("/private/scriviRecensione.jsp").forward(request, response);
        }
        String fotoPath;
        if(multi.getFilesystemName(name) == null || multi.getFilesystemName(name).equals("")) fotoPath = manager.defaultFolder + "/rec_default.jpg";
        else fotoPath = dirName + "/" + multi.getFilesystemName(name);

        Recensione rec = ristorante.addRecensione(titolo, recensione, utente);
        rec.addFoto(fotoPath);

        request.getRequestDispatcher("/info.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InserisciRecensioniServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InserisciRecensioniServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
