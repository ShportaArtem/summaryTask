package web.command.http.get;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.DriverShippingRequest;
import model.User;
import service.RequestService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class GetMyRequestsCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(GetMyRequestsCommand.class);
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_MY_REQUESTS);
		RequestService requestServ = RequestService.getInstance();
		User driverNow = (User) session.getAttribute("user");
		List<DriverShippingRequest> requests = requestServ.findRequestsByDriverId(driverNow.getId());
		LOG.trace("Found in DB: requests --> " + requests);
		
		DriverShippingRequest myRequestNow = requestServ.findRequestInProcessByDriverId(driverNow.getId());
		LOG.trace("Found in DB: myRequestNow --> " + myRequestNow);
		
		int methodMyRequests = 1;
		session.setAttribute("methodMyRequests", methodMyRequests);
		LOG.trace("Set the session attribute: methodMyRequests --> " + methodMyRequests);
		
		session.setAttribute("myRequests", requests);
		LOG.trace("Set the session attribute: requests --> " + requests);
		
		session.setAttribute("myRequestNow", myRequestNow);
		LOG.trace("Set the session attribute: myRequestNow --> " + myRequestNow);
		
		LOG.debug("Commands finished");
		return cr;
	}
	
}
