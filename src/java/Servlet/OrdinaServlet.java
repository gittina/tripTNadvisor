/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DataBase.DBManager;
import DataBase.Ristorante;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lucadiliello
 */
public class OrdinaServlet extends HttpServlet {

    private DBManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(true);
            session.removeAttribute("result");

            String research = (String) session.getAttribute("research");
            ArrayList<Ristorante> res = manager.simpleSearch(research);
            System.out.println("Size:" + res.size());
            Comparator c;

            String tipo = (String) request.getParameter("tipo");
            String ordine = (String) request.getParameter("ordine");
            switch (tipo) {
                case "NoOrdine":
                    break;

                case "pos":
                    c = (Comparator<Ristorante>) (Ristorante o1, Ristorante o2) -> {
                try {
                    int res1;
                    if (o1.getPosizioneClassifica() > o2.getPosizioneClassifica()) {
                        res1 = 1;
                    } else if (o1.getPosizioneClassifica() < o2.getPosizioneClassifica()) {
                        res1 = -1;
                    } else {
                        res1 = 0;
                    }
                    if ("1".equals(ordine)) {
                        return res1;
                    } else {
                        return -res1;
                    }
                }catch (SQLException ex) {
                    return 0;
                }
            };
                    res.sort(c);
                    break;
                case "pre":
                    c = new Comparator<Ristorante>() {
                        int toInt(String s) {
                            switch (s) {
                                case "Economica":
                                    return 1;
                                case "Normale":
                                    return 2;
                                default:
                                    return 3;
                            }
                        }

                        @Override
                        public int compare(Ristorante o1, Ristorante o2) {
                            int res;
                            if (toInt(o1.getFascia()) > toInt(o2.getFascia())) {
                                res = 1;
                            } else if (toInt(o1.getFascia()) < toInt(o2.getFascia())) {
                                res = -1;
                            } else {
                                res = 0;
                            }
                            if ("1".equals(ordine)) {
                                return res;
                            } else {
                                return -res;
                            }
                        }
                    };
                    res.sort(c);
                    break;
                case "alf":
                    c = (Comparator<Ristorante>) (Ristorante o1, Ristorante o2) -> {
                        int res1 = o1.getName().compareTo(o2.getName());
                if ("1".equals(ordine)) {
                    return res1;
                } else {
                    return -res1;
                }
            };
                    res.sort(c);
                    break;
            }

            String fascia = (String) request.getParameter("fascia");
            Iterator<Ristorante> iter;
            iter = res.iterator();
            if (!"TuttiFascia".equals(fascia)) {
                while (iter.hasNext()) {
                    Ristorante r = iter.next();
                    System.out.print("Fasce:"  + r.getFascia() + " " +fascia);
                    if(!r.getFascia().equals(fascia)) {
                        iter.remove();
                    }
                }
            }
            

            String spec = (String) request.getParameter("spec");
            iter = res.iterator();
            if (!"TuttiSpec".equals(spec)) {
                while (iter.hasNext()) {
                    Ristorante r = iter.next();
                    System.out.print("Cucina:"  + r.getCucina() + " " + spec);
                    if (!r.getCucina().equals(spec)) {
                        iter.remove();
                    }
                }
            }

            session.setAttribute("result", res);
        } catch (SQLException ex) {
            System.out.println("Problemi nel filtraggio o ordinamento della ricerca in result.jsp");
        }

        request.getRequestDispatcher("/result.jsp").forward(request, response);

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
        processRequest(request, response);
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
        processRequest(request, response);
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
