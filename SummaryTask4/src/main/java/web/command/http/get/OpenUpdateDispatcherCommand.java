package web.command.http.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import service.LoginService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Open update dispatcher command
 * 
 * @author A.Shporta
 */
public class OpenUpdateDispatcherCommand implements Command {
	
	private static Logger LOG = Logger.getLogger(OpenUpdateDispatcherCommand.class);
	
	private LoginService loginServ;
	
	public OpenUpdateDispatcherCommand(LoginService loginServ) {
		super();
		this.loginServ = loginServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		Integer id = Integer.parseInt(request.getParameter("dispatcherId"));
		LOG.trace("Found in request parametrs: dispatcherId --> " + id);
		
		User dispatcherNow = loginServ.findUserById(id);
		LOG.trace("Found in DB: dispatcherNow --> " + dispatcherNow);
		
		session.setAttribute("dispatcherNow", dispatcherNow);
		LOG.trace("Set the session attribute: dispatcherNow --> " + dispatcherNow);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_UPDATE_DISPATCHER);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
