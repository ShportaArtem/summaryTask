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

public class UpdateDispatcherCommand implements Command{

	private static Logger LOG = Logger.getLogger(UpdateDispatcherCommand.class);

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		DispatcherService dispServ = DispatcherService.getInstance();
		User dispatcherNow = (User) session.getAttribute("dispatcherNow");
		LOG.trace("Found in session atributes: dispatcherNow --> " + dispatcherNow);
		
		if(!"".equals(request.getParameter("login"))) {
			dispatcherNow.setLogin(request.getParameter("login"));
			LOG.trace("Found in request parameters: login --> " + dispatcherNow.getLogin());
			
		}
		if(!"".equals(request.getParameter("password"))) {
		try {
			dispatcherNow.setPassword(request.getParameter("password"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		}
		if(!"".equals(request.getParameter("name"))) {
			dispatcherNow.setName(request.getParameter("name"));
			LOG.trace("Found in request parameters: name --> " + dispatcherNow.getName());
		}
		if(!"".equals(request.getParameter("surname"))) {
			dispatcherNow.setSurname(request.getParameter("surname"));
			LOG.trace("Found in request parameters: surname --> " + dispatcherNow.getSurname());
		}
		
		dispServ.updateDriver(dispatcherNow);
		LOG.trace("Update dispatcher" );
		
		int methodDispatcher = 2;
		request.getSession().setAttribute("methodDispatcher", methodDispatcher);
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_DISPATCHERS_POST);
		LOG.trace("Set the session attribute: methodDispatcher --> " + methodDispatcher);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
