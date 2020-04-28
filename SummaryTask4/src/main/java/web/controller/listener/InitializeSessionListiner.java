package web.controller.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import web.command.http.HttpCommandDispatcher;
import web.command.http.post.*;
import web.command.http.get.*;

public class InitializeSessionListiner implements HttpSessionListener {
	
	private static final Logger LOG = Logger.getLogger(InitializeSessionListiner.class);
	
    public InitializeSessionListiner() {
    }

    public void sessionCreated(HttpSessionEvent se)  { 
    	LOG.debug("Initialize Session Listiner started");
    	LOG.debug("Session created");
    	HttpSession s =se.getSession();
    	HttpCommandDispatcher dispatcher = new HttpCommandDispatcher(new DefaultCommand()); 
    	dispatcher.addCommand("login", new LoginCommand());
    	dispatcher.addCommand("logout", new LogoutCommand());
    	dispatcher.addCommand("getFlights", new GetFlightsCommand());
    	dispatcher.addCommand("openAddFlight", new OpenAddFlightCommand());
    	dispatcher.addCommand("openAddCar", new OpenAddCarCommand());
    	dispatcher.addCommand("openAddDriver", new OpenAddDriverCommand());
    	dispatcher.addCommand("openAddRequest", new OpenAddRequestCommand());
    	dispatcher.addCommand("openChooseDriver", new OpenChooseDriverCommand());
    	dispatcher.addCommand("addFlight", new AddFlightCommand());
    	dispatcher.addCommand("addCar", new AddCarCommand());
    	dispatcher.addCommand("addDriver", new AddDriverCommand());
    	dispatcher.addCommand("addRequest", new AddRequestCommand());
    	dispatcher.addCommand("cancelFlight", new CancelFlightCommand());
    	dispatcher.addCommand("openUpdateFlight", new OpenUpdateFlightCommand());
    	dispatcher.addCommand("updateFlight", new UpdateFlightCommand());
    	dispatcher.addCommand("getCars", new GetCarsCommand());
    	dispatcher.addCommand("deleteCar", new DeleteCarCommand());
    	dispatcher.addCommand("openUpdateCar", new OpenUpdateCarCommand());
    	dispatcher.addCommand("openUpdateDriver", new OpenUpdateDriverCommand());
    	dispatcher.addCommand("updateCar", new UpdateCarCommand());
    	dispatcher.addCommand("updateDriver", new UpdateDriverCommand());
    	dispatcher.addCommand("getDrivers", new GetDriversCommand());
    	dispatcher.addCommand("getMyRequests", new GetMyRequestsCommand());
    	dispatcher.addCommand("deleteDriver", new DeleteDriverCommand());
    	dispatcher.addCommand("deleteMyRequest", new DeleteMyRequestCommand());
    	dispatcher.addCommand("chooseRequest", new ChooseRequestCommand());
    	dispatcher.addCommand("openChooseCar", new OpenChooseCarCommand());
    	dispatcher.addCommand("chooseCar", new ChooseCarCommand());
    	dispatcher.addCommand("openFinishFligth", new OpenFinishFlightCommand());
    	dispatcher.addCommand("finishFlight", new FinishFlightCommand());
    	dispatcher.addCommand("getDispathers", new GetDispatchersCommand());
    	dispatcher.addCommand("deleteDispatcher", new DeleteDispatcherCommand());
    	dispatcher.addCommand("openAddDispatcher", new OpenAddDispatcherCommand());
    	dispatcher.addCommand("addDispatcher", new AddDispatcherCommand());
    	dispatcher.addCommand("openUpdateDispatcher", new OpenUpdateDispatcherCommand());
    	dispatcher.addCommand("updateDispatcher", new UpdateDispatcherCommand());
    	LOG.debug("Command dispatcher was successfully initialized");
    	
    	s.setAttribute("dispatcher", dispatcher);
    	LOG.trace("Set the session attribute: dispatcher --> " + dispatcher);
    }

    public void sessionDestroyed(HttpSessionEvent se)  { 
    	LOG.debug("Session destroyer started");
    	LOG.debug("Session destroyer finished");
    }
	
}
