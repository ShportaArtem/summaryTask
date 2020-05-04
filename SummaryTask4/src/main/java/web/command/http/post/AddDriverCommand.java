package web.command.http.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.DriverService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Add driver command
 * 
 * @author A.Shporta
 */
public class AddDriverCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(AddDriverCommand.class);
	
	private DriverService driverServ;
	
	
	public AddDriverCommand(DriverService driverServ) {
		super();
		this.driverServ = driverServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		String login = request.getParameter("login");
		LOG.trace("Found in request parameters: login --> " + login);
		
		String password = request.getParameter("password");
		LOG.trace("Found in request parameters: password --> " + password);
		
		String name = request.getParameter("name");
		LOG.trace("Found in request parameters: name --> " + name);
		
		String surname = request.getParameter("surname");
		LOG.trace("Found in request parameters: surname --> " + surname);
		
		String passport = request.getParameter("passport");
		LOG.trace("Found in request parameters: passport --> " + passport);
		
		String phone = request.getParameter("phone");
		
		driverServ.insertDriver(login, password, name, surname, passport, phone);
		LOG.trace("Insert in DB driver" );
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_DRIVERS_POST);
		int methodDriver = 2;
		session.setAttribute("methodDriver", methodDriver);
		LOG.trace("Set the session attribute: methodDriver --> " + methodDriver);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
