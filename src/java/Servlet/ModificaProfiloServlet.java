/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DataBase.DBManager;
import DataBase.Utente;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

/**
 *
 * @author bazza
 */
@MultipartConfig
public class ModificaProfiloServlet extends HttpServlet {

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

    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        response.setContentType("text/plain"); //tipo di file di upload
        Utente utente = (Utente) session.getAttribute("utente");
        RequestDispatcher rd = request.getRequestDispatcher("/private/ConfigurazioneProfilo");
        if (utente != null) {
            MultipartRequest multi = new MultipartRequest(request, manager.completePath + "/web" + dirName, 10 * 1024 * 1024, "ISO-8859-1", new DefaultFileRenamePolicy());
            Enumeration files = multi.getFileNames();
            String name = null;
            while (files.hasMoreElements()) {
                name = (String) files.nextElement();
            }

            String newNome = null;
            String newCognome = null;
            String newMail = null;
            String newPass = null;
            String newAvPath;

            boolean tornaIndietro = false;

            if (multi.getParameter("nome").length() == 0) {
                newNome = utente.getNome();
            } else if (multi.getParameter("nome").length() < 2) {
                request.setAttribute("message", "Nome troppo corto");
                tornaIndietro = true;
            } else {
                newNome = multi.getParameter("nome");
            }

            if (multi.getParameter("cognome").length() == 0) {
                newCognome = utente.getCognome();
            } else if (multi.getParameter("cognome").length() < 3) {
                request.setAttribute("message", "Cognome troppo corto");
                tornaIndietro = true;
            } else {
                newCognome = multi.getParameter("cognome");
            }

            if (multi.getParameter("mail").length() == 0) {
                newMail = utente.getEmail();
            } else if (multi.getParameter("mail").length() < 8 || !multi.getParameter("mail").contains("@")) {
                request.setAttribute("message", "Devi immettere una mail valida");
                tornaIndietro = true;
            } else {
                newMail = multi.getParameter("mail");
            }

            if (multi.getParameter("passOld").length() == 0 && multi.getParameter("passOld").length() == 0 && multi.getParameter("passOld").length() == 0) {
                newPass = manager.getPassUtente(utente);
            } else if (!multi.getParameter("passOld").equals(manager.getPassUtente(utente))) {
                request.setAttribute("message", "La vecchia password è errata");
                tornaIndietro = true;
            } else if (!(multi.getParameter("pass1").equals(multi.getParameter("pass2")))) {
                request.setAttribute("message", "Le nuove password non corrispondono");
                tornaIndietro = true;
            } else if (multi.getParameter("pass1").length() < 8) {
                request.setAttribute("message", "La password deve essere lunga almeno 8 caratteri");
                tornaIndietro = true;
            } else {
                newPass = multi.getParameter("pass1");
            }
            if (multi.getFilesystemName(name) == null) {
                newAvPath = utente.getAvpath();
            } else {
                newAvPath = dirName + "/" + multi.getFilesystemName(name);
            }

            if (tornaIndietro) {
                rd = request.getRequestDispatcher("/private/modifica.jsp");
            } else {

                utente.modificaProfilo(newNome, newCognome, newMail, newAvPath);
                utente.cambiaPassword(newPass);

                Utente utenteNew = manager.authenticate(newMail, newPass);
                session.removeAttribute("utente");
                session.setAttribute("utente", utenteNew);
            }
        }
        rd.forward(request, response);
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
            Logger.getLogger(ModificaProfiloServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ModificaProfiloServlet.class.getName()).log(Level.SEVERE, null, ex);
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
