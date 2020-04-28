package web.command.http.post;


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

public class AddRequestCommand implements Command{

	private static Logger LOG = Logger.getLogger(AddRequestCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		RequestService reqServ = RequestService.getInstance();
		DriverShippingRequest req = new DriverShippingRequest();
		User driverNow = (User) session.getAttribute("user");
		LOG.trace("Found in session atriburte: user --> " + driverNow);
		
		req.setDriverId(driverNow.getId());
		req.setShippingId((Integer) session.getAttribute("flightIdForRequest")); 
		LOG.trace("Found in request parameters: flightIdForRequest --> " + req.getShippingId());
		
		req.setCarryinfCapacity(Integer.parseInt(request.getParameter("carryingCapacity")));
		LOG.trace("Found in request parameters: carryingCapacity --> " + req.getCarryinfCapacity());
		
		req.setPassangersCapacity(Integer.parseInt(request.getParameter("passengersCapacity")));
		LOG.trace("Found in request parameters: passengersCapacity --> " + req.getPassangersCapacity());
		
		req.setVehicleCondition(request.getParameter("vehicleCondition"));
		LOG.trace("Found in request parameters: vehicleCondition --> " + req.getVehicleCondition());
		
		reqServ.insertRequest(req);
		LOG.trace("Insert in DB request --> " + req);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_FLIGHTS_POST);
		int methodFlight = 2;
		request.getSession().setAttribute("methodFlight", methodFlight);
		LOG.trace("Set the session attribute: methodFlight --> " + methodFlight);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
