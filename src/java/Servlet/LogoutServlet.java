package Servlet;

import DataBase.Language;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author @version 1.0
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGetPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGetPost(req, resp);
    }

    public void doGetPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            try {
                Language lan = (Language) session.getAttribute("lan");
                String s = (String) session.getAttribute("lastPage");
                session.invalidate();
                session = req.getSession();
                session.setAttribute("lan", lan);
                if(s!=null){
                    req.getRequestDispatcher(s).forward(req, resp);
                } else req.getRequestDispatcher("/HomeServlet").forward(req, resp);
            } catch (IllegalStateException e) {
            }
        } else req.getRequestDispatcher("/HomeServlet").forward(req, resp);
    }
}
