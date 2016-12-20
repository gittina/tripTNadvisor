package Listener;

import DataBase.DBManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Luca
 */
public class WebAppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String dburl = sce.getServletContext().getInitParameter("dburl");
        String contextPath = sce.getServletContext().getInitParameter("contextPath");
        String completePath = sce.getServletContext().getInitParameter("completePath");
        DBManager manager = new DBManager(dburl, contextPath, completePath);
        manager.updateAutocomplete();
        sce.getServletContext().setAttribute("dbmanager", manager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DBManager.shutdown();
            //niente da fare
        } catch (Throwable ex) {
            System.out.println("Failed to stop DataBase: " + ex.toString());
        }
    }

}
