package web.command.http.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Driver;
import model.User;
import modelView.DriverView;
import service.DriverService;
import service.LoginService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class OpenUpdateDriverCommand implements Command {
	private static Logger LOG = Logger.getLogger(OpenUpdateDriverCommand.class);
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		String login = request.getParameter("driverLogin");
		LOG.trace("Found in request parameters: driverLogin --> " + login);
		
		DriverService driverServ = DriverService.getInstance();
		LoginService loginServ = LoginService.getInstance();
		User userDriverNow = loginServ.findUserByLogin(login);
		LOG.trace("Found in DB: userDriverNow --> " + userDriverNow);
		
		Driver driverNow = driverServ.findDriverByUserId(userDriverNow.getId());
		LOG.trace("Found in DB: driverNow --> " + driverNow);
		
		DriverView driverView = new DriverView();
		driverView.setId(userDriverNow.getId());
		driverView.setLogin(userDriverNow.getLogin());
		driverView.setName(userDriverNow.getName());
		driverView.setPassport(driverNow.getPassport());
		driverView.setPassword(userDriverNow.getPasswordView());
		driverView.setPhone(driverNow.getPhone());
		driverView.setSurname(userDriverNow.getSurname());
		session.setAttribute("userDriverNow", userDriverNow);
		LOG.trace("Set the session attribute: userDriverNow --> " + userDriverNow);
		
		session.setAttribute("driverNow", driverNow);
		LOG.trace("Set the session attribute: driverNow --> " + driverNow);
		
		session.setAttribute("driverViewNow", driverView);
		LOG.trace("Set the session attribute: driverViewNow --> " + driverView);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_UPDATE_DRIVER);

		LOG.debug("Commands finished");
		return cr;
	}

}
