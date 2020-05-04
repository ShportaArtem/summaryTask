package web.command.http.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.DriverShippingRequest;
import service.FlightService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

/**
 * Finish flight command
 * 
 * @author A.Shporta
 */
public class FinishFlightCommand implements Command{

	private static Logger LOG = Logger.getLogger(FinishFlightCommand.class);
	
	private FlightService reqServ;
	
	public FinishFlightCommand(FlightService reqServ) {
		super();
		this.reqServ = reqServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		int methodMyRequests = 2;
		session.setAttribute("methodMyRequests", methodMyRequests);
		LOG.trace("Set the session attribute: methodMyRequests --> " + methodMyRequests);
		
		DriverShippingRequest myRequestNow = (DriverShippingRequest) session.getAttribute("myRequestNow");
		LOG.trace("Found in session atributes: myRequestNow --> " + myRequestNow);
			
		String vehicleCondition =  request.getParameter("vehicleCondition");
		LOG.trace("Found in request parameters: vehicleCondition --> " + vehicleCondition);
		
		reqServ.finishShipping(vehicleCondition, myRequestNow);
		LOG.trace("Finish flight" );
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_MY_REQUESTS_POST);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
