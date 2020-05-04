package web.command.http.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import service.DriverService;
import service.LoginService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Delete driver command
 * 
 * @author A.Shporta
 */
public class DeleteDriverCommand implements Command{

	private static Logger LOG = Logger.getLogger(AddCarCommand.class);
	
	private DriverService service;
	private LoginService serv;
	
	public DeleteDriverCommand(DriverService service, LoginService serv) {
		super();
		this.service = service;
		this.serv = serv;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		String driverLogin = request.getParameter("driverLogin");
		LOG.trace("Found in request parameters: driverLogin --> " + driverLogin);
		
		User user = serv.findUserByLogin(driverLogin);
		LOG.trace("Found in DB user --> " + user);
		
		service.deleteDriverById(user.getId());
		LOG.trace("Delete driver by id --> " + user.getId() );
		
		CommandResult cr = new HttpCommandResult(RequestType.POST, Path.PAGE_DRIVERS_POST);

		LOG.debug("Commands finished");
		return cr;
	}

}
