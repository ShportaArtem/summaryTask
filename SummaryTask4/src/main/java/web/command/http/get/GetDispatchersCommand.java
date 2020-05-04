package web.command.http.get;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import service.DriverService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Get dispatchers command
 * 
 * @author A.Shporta
 */
public class GetDispatchersCommand implements Command{

	private static final Logger LOG = Logger.getLogger(GetDispatchersCommand.class);
	
	private DriverService driverServ;
	
	public GetDispatchersCommand(DriverService driverServ) {
		super();
		this.driverServ = driverServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_DISPATCHERS);
		List<User> dispatchers = driverServ.findAllUsersByRoleId(3);
		LOG.trace("Found in DB: dispatchers --> " + dispatchers);
		int methodDispatcher = 1;
		session.setAttribute("methodDispatcher", methodDispatcher);
		LOG.trace("Set the session attribute: methodDispatcher --> " + methodDispatcher);
		session.setAttribute("dispatchers", dispatchers);
		LOG.trace("Set the session attribute: dispatchers --> " + dispatchers);
		
		LOG.debug("Command finished");
		return cr;
	}
	
}
