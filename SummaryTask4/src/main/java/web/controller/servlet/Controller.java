package web.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandDispatcher;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1061389653307692446L;
	
	private static final Logger LOG = Logger.getLogger(Controller.class);
	
	public Controller() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("Controller starts");
		
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);
		
		HttpSession session = request.getSession(true);
		
		HttpCommandDispatcher dispatcher = (HttpCommandDispatcher) session.getAttribute("dispatcher");
		
		
		Command command = dispatcher.getCommand(commandName);
		LOG.trace("Obtained command --> " + command);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_ERROR_PAGE);
		try {
			
			cr = command.execute(request, response);
		} catch (AppException e) {
			System.out.println(e.getMessage());
			request.setAttribute("errorMessage", e.getMessage());
		}
		LOG.trace("Forward address --> " + cr.getResult());
		LOG.debug("Controller finished, now go to forward address --> " + cr.getResult());
		if (cr.getType().equals(RequestType.GET)) {
			request.getRequestDispatcher(cr.getResult()).forward(request, response);
		} else {
			response.sendRedirect(cr.getResult());
		}
	}
	
	

}
