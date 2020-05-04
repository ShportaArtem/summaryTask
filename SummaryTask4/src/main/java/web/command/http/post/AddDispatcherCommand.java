package web.command.http.post;


import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import service.DispatcherService;
import utils.HashUtil;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Add dispatcher command
 * 
 * @author A.Shporta
 */
public class AddDispatcherCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(AddDispatcherCommand.class);
	
	private DispatcherService dispServ;
	
	public AddDispatcherCommand(DispatcherService dispServ) {
		super();
		this.dispServ = dispServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		User dispatcher = new User();
		dispatcher.setLogin(request.getParameter("login"));
		LOG.trace("Found in request parameters: login --> " + dispatcher.getLogin());
		
		try {
			dispatcher.setPassword(new String(HashUtil.getSHA(request.getParameter("password"))));
			
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
		}
		dispatcher.setRoleId(3);
		dispatcher.setName(request.getParameter("name"));
		LOG.trace("Found in request parameters: name --> " + dispatcher.getName());
		
		dispatcher.setSurname(request.getParameter("surname"));
		LOG.trace("Found in request parameters: surname --> " + dispatcher.getSurname());
		
		dispServ.insertDispatcher(dispatcher);
		LOG.trace("Insert in DB dispatcher --> " + dispatcher);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_DISPATCHERS_POST);
		int methodDispatcher = 2;
		session.setAttribute("methodDispatcher", methodDispatcher);
		LOG.trace("Set the session attribute: methodDispatcher --> " + methodDispatcher);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
