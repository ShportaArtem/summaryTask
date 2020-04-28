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
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class AddDispatcherCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(AddDispatcherCommand.class);
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		DispatcherService dispServ = DispatcherService.getInstance();
		User dispatcher = new User();
		dispatcher.setLogin(request.getParameter("login"));
		LOG.trace("Found in request parameters: login --> " + dispatcher.getLogin());
		
		try {
			dispatcher.setPassword(request.getParameter("password"));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
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
