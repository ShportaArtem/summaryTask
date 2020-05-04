package web.controller.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import db.DBManager;
import db.exception.DBException;
import db.repository.CarRep;
import db.repository.DriverRep;
import db.repository.DriverShippingRequestRep;
import db.repository.FirmRep;
import db.repository.ShippingRep;
import db.repository.UserRep;
import service.CarService;
import service.DispatcherService;
import service.DriverService;
import service.FlightService;
import service.LoginService;
import service.RequestService;
import web.command.http.HttpCommandDispatcher;
import web.command.http.get.DefaultCommand;
import web.command.http.get.GetCarsCommand;
import web.command.http.get.GetDispatchersCommand;
import web.command.http.get.GetDriversCommand;
import web.command.http.get.GetFlightsCommand;
import web.command.http.get.GetMyRequestsCommand;
import web.command.http.get.OpenAddCarCommand;
import web.command.http.get.OpenAddDispatcherCommand;
import web.command.http.get.OpenAddDriverCommand;
import web.command.http.get.OpenAddFlightCommand;
import web.command.http.get.OpenAddRequestCommand;
import web.command.http.get.OpenChooseCarCommand;
import web.command.http.get.OpenChooseDriverCommand;
import web.command.http.get.OpenFinishFlightCommand;
import web.command.http.get.OpenUpdateCarCommand;
import web.command.http.get.OpenUpdateDispatcherCommand;
import web.command.http.get.OpenUpdateDriverCommand;
import web.command.http.get.OpenUpdateFlightCommand;
import web.command.http.post.AddCarCommand;
import web.command.http.post.AddDispatcherCommand;
import web.command.http.post.AddDriverCommand;
import web.command.http.post.AddFlightCommand;
import web.command.http.post.AddRequestCommand;
import web.command.http.post.CancelFlightCommand;
import web.command.http.post.ChooseCarCommand;
import web.command.http.post.ChooseRequestCommand;
import web.command.http.post.DeleteCarCommand;
import web.command.http.post.DeleteDispatcherCommand;
import web.command.http.post.DeleteDriverCommand;
import web.command.http.post.DeleteMyRequestCommand;
import web.command.http.post.FinishFlightCommand;
import web.command.http.post.LoginCommand;
import web.command.http.post.LogoutCommand;
import web.command.http.post.UpdateCarCommand;
import web.command.http.post.UpdateDispatcherCommand;
import web.command.http.post.UpdateDriverCommand;
import web.command.http.post.UpdateFlightCommand;

/**
 * Context listener.
 * 
 * @author A.Shporta
 * 
 */
public class ContextListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		log("Servlet context destruction starts");
		log("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent event) {
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

		initContext(context);
		log("Servlet context initialization finished");
	}
	
	/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 */
	private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			log("Cannot configure Log4j");
			ex.printStackTrace();
		}
		log("Log4J initialization finished");
	}
	
	/**
	 * Initializes CommandDispatcher with repositories and services.
	 * 
	 * @param servletContext
	 */
	private void initContext(ServletContext context) {
		DBManager dbManager = null;
		try {
			dbManager = new DBManager();
		} catch (DBException e) {
			LOG.error(e);
		}

		UserRep userRep = new UserRep();
		CarRep carRep = new CarRep();
		FirmRep firmRep = new FirmRep();
		DriverRep driverRep = new DriverRep();
		DriverShippingRequestRep requestRep = new DriverShippingRequestRep();
		ShippingRep shipRep = new ShippingRep();
		
		LoginService loginService = new LoginService(dbManager, userRep);
		DispatcherService dispatcherService = new DispatcherService(dbManager, userRep);
		CarService carService = new CarService(dbManager, carRep, firmRep);
		DriverService driverService = new DriverService(dbManager, userRep, driverRep);
		FlightService flightService = new FlightService(dbManager, userRep, carRep, requestRep, shipRep);
		RequestService requestService = new RequestService(dbManager, requestRep, shipRep, firmRep, carRep);
		
		HttpCommandDispatcher dispatcher = new HttpCommandDispatcher(new DefaultCommand());

		dispatcher.addCommand("login", new LoginCommand(loginService));
		dispatcher.addCommand("logout", new LogoutCommand());
		dispatcher.addCommand("getFlights", new GetFlightsCommand(loginService, flightService, carService));
		dispatcher.addCommand("openAddFlight", new OpenAddFlightCommand());
		dispatcher.addCommand("openAddCar", new OpenAddCarCommand());
		dispatcher.addCommand("openAddDriver", new OpenAddDriverCommand());
		dispatcher.addCommand("openAddRequest", new OpenAddRequestCommand());
		dispatcher.addCommand("openChooseDriver", new OpenChooseDriverCommand(requestService, loginService));
		dispatcher.addCommand("addFlight", new AddFlightCommand(flightService));
		dispatcher.addCommand("addCar", new AddCarCommand(carService));
		dispatcher.addCommand("addDriver", new AddDriverCommand(driverService));
		dispatcher.addCommand("addRequest", new AddRequestCommand(requestService));
		dispatcher.addCommand("cancelFlight", new CancelFlightCommand(flightService));
		dispatcher.addCommand("openUpdateFlight", new OpenUpdateFlightCommand(flightService, loginService, carService));
		dispatcher.addCommand("updateFlight", new UpdateFlightCommand(flightService));
		dispatcher.addCommand("getCars", new GetCarsCommand(carService));
		dispatcher.addCommand("deleteCar", new DeleteCarCommand(carService));
		dispatcher.addCommand("openUpdateCar", new OpenUpdateCarCommand(carService));
		dispatcher.addCommand("openUpdateDriver", new OpenUpdateDriverCommand(driverService, loginService));
		dispatcher.addCommand("updateCar", new UpdateCarCommand(carService));
		dispatcher.addCommand("updateDriver", new UpdateDriverCommand(driverService));
		dispatcher.addCommand("getDrivers", new GetDriversCommand(driverService, requestService));
		dispatcher.addCommand("getMyRequests", new GetMyRequestsCommand(requestService));
		dispatcher.addCommand("deleteDriver", new DeleteDriverCommand(driverService, loginService));
		dispatcher.addCommand("deleteMyRequest", new DeleteMyRequestCommand(requestService));
		dispatcher.addCommand("chooseRequest", new ChooseRequestCommand(requestService));
		dispatcher.addCommand("openChooseCar", new OpenChooseCarCommand(carService));
		dispatcher.addCommand("chooseCar", new ChooseCarCommand(requestService));
		dispatcher.addCommand("openFinishFligth", new OpenFinishFlightCommand());
		dispatcher.addCommand("finishFlight", new FinishFlightCommand(flightService));
		dispatcher.addCommand("getDispathers", new GetDispatchersCommand(driverService));
		dispatcher.addCommand("deleteDispatcher", new DeleteDispatcherCommand(dispatcherService));
		dispatcher.addCommand("openAddDispatcher", new OpenAddDispatcherCommand());
		dispatcher.addCommand("addDispatcher", new AddDispatcherCommand(dispatcherService));
		dispatcher.addCommand("openUpdateDispatcher", new OpenUpdateDispatcherCommand(loginService));
		dispatcher.addCommand("updateDispatcher", new UpdateDispatcherCommand(dispatcherService));
		context.setAttribute("dispatcher", dispatcher);
		
		LOG.debug("Command dispatcher was successfully initialized");
	}

	private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
}
