package web.command.http.post;


import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Driver;
import model.User;
import service.DriverService;
import utils.HashUtil;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

/**
 * Update driver command
 * 
 * @author A.Shporta
 */
public class UpdateDriverCommand implements Command{

	private static Logger LOG = Logger.getLogger(UpdateDriverCommand.class);
	
	private DriverService driverServ;
	
	public UpdateDriverCommand(DriverService driverServ) {
		super();
		this.driverServ = driverServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		Driver driverNow = (Driver) session.getAttribute("driverNow");
		LOG.trace("Found in session atributes: driverNow --> " + driverNow);
		
		User userDriver = (User) session.getAttribute("userDriverNow");
		LOG.trace("Found in session atributes: userDriverNow --> " + userDriver);
		
		if(!"".equals(request.getParameter("login"))) {
		userDriver.setLogin(request.getParameter("login"));
		LOG.trace("Found in request parameters: login --> " + userDriver.getLogin());
		
		}
		if(!"".equals(request.getParameter("password"))) {
		try {
			userDriver.setPassword(new String(HashUtil.getSHA(request.getParameter("password"))));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		}
		if(!"".equals(request.getParameter("name"))) {
			userDriver.setName(request.getParameter("name"));
			LOG.trace("Found in request parameters: name --> " + userDriver.getName());
			
		}
		if(!"".equals(request.getParameter("surname"))) {
			userDriver.setSurname(request.getParameter("surname"));
			LOG.trace("Found in request parameters: surname --> " + userDriver.getSurname());
			
		}
		if(!"".equals(request.getParameter("passport"))) {
			driverNow.setPassport(request.getParameter("passport"));
			LOG.trace("Found in request parameters: passport --> " + driverNow.getPassport());
			
		}
		if(!"".equals(request.getParameter("phone"))) {
			driverNow.setPhone(request.getParameter("phone"));
			LOG.trace("Found in request parameters: phone --> " + driverNow.getPhone());
			
		}
		
		driverServ.updateDriver(userDriver, driverNow);
		LOG.trace("Update driver" );
		
		int methodDriver = 2;
		request.getSession().setAttribute("methodDriver", methodDriver);
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_DRIVERS_POST);
		LOG.trace("Set the session attribute: methodDriver --> " + methodDriver);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
