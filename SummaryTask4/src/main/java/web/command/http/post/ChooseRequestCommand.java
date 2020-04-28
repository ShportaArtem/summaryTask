package web.command.http.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.RequestService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class ChooseRequestCommand implements Command{

	private static Logger LOG = Logger.getLogger(ChooseRequestCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		RequestService reqServ = RequestService.getInstance();
		Integer requestId = Integer.parseInt(request.getParameter("requestId"));
		LOG.trace("Found in request parameters: requestId --> " + requestId);
		
		reqServ.chooseRequestByRequestId(requestId);
		LOG.trace("Choose request by id --> " + requestId);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_FLIGHTS_POST);
		int methodFlight = 2;
		session.setAttribute("methodFlight", methodFlight);
		LOG.trace("Set the session attribute: methodFlight --> " + methodFlight);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
