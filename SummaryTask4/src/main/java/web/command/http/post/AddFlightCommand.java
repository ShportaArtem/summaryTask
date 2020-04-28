package web.command.http.post;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import service.FlightService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class AddFlightCommand implements Command {

	private static Logger LOG = Logger.getLogger(AddFlightCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		LOG.trace("Found in session atributes: user --> " + user);
		
		Integer dispathcerId = user.getId();
		String arrivalCity = request.getParameter("arrivalCity");
		LOG.trace("Found in request parameters: arrivalCity --> " + arrivalCity);
		
		String departureCity = request.getParameter("departureCity");
		LOG.trace("Found in request parameters: departureCity --> " + departureCity);
		
		Date departureTime = Date.valueOf(request.getParameter("departureDate"));
		LOG.trace("Found in request parameters: departureDate --> " + departureTime);
		
		FlightService flightServ = FlightService.getInstance();
		flightServ.insertShipping(dispathcerId, arrivalCity, departureCity, departureTime);
		LOG.trace("Insert in DB flight" );

		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_FLIGHTS_POST);
		int methodFlight = 2;
		request.getSession().setAttribute("methodFlight", methodFlight);
		LOG.trace("Set the session attribute: methodFlight --> " + methodFlight);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
