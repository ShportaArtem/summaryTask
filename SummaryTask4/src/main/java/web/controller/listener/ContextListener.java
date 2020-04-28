package web.controller.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class ContextListener implements ServletContextListener {
	
	private static final Logger LOG = Logger.getLogger(ContextListener.class);

    public void contextDestroyed(ServletContextEvent event)  { 
    	log("Servlet context destruction starts");
		log("Servlet context destruction finished");
    }

    public void contextInitialized(ServletContextEvent event)  { 
    	log("Servlet context initialization starts");
    	ServletContext context = event.getServletContext();
    	String localesFileName = context.getInitParameter("locales");
    	
    	initLog4J(context);
    	// obtain reale path on server
    	String localesFileRealPath = context.getRealPath(localesFileName);
    	// locad descriptions
    	Properties locales = new Properties();
    	try {
			locales.load(new FileInputStream(localesFileRealPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// save descriptions to servlet context
    	context.setAttribute("locales", locales);
    	locales.list(System.out);
    	log("Servlet context initialization finished");
    }
	
    private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(
				servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			log("Cannot configure Log4j");
			ex.printStackTrace();
		}		
		log("Log4J initialization finished");
	}
    
    private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
}
