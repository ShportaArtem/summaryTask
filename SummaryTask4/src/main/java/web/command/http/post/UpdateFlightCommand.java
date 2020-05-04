package web.command.http.post;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Shipping;
import model.User;
import service.FlightService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

/**
 * Update flight command
 * 
 * @author A.Shporta
 */
public class UpdateFlightCommand implements Command{

	private static Logger LOG = Logger.getLogger(UpdateFlightCommand.class);
	
	private FlightService flightServ;
	
	public UpdateFlightCommand(FlightService flightServ) {
		super();
		this.flightServ = flightServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		LOG.trace("Found in session atributes: user --> " + user);
		
		Shipping shipNow = (Shipping)session.getAttribute("flightNow");
		LOG.trace("Found in session atributes: flightNow --> " + shipNow);
		
		Integer dispathcerId = user.getId();
		String arrivalCity= !"".equals(request.getParameter("arrivalCity"))? request.getParameter("arrivalCity"): shipNow.getArrivalCity();
		LOG.trace("Found in request parameters: arrivalCity --> " + arrivalCity);
		
		String departureCity =  !"".equals(request.getParameter("departureCity")) ? request.getParameter("departureCity") : shipNow.getDepartureCity();
		LOG.trace("Found in request parameters: departureCity --> " + departureCity);
		
		String status = shipNow.getStatus();	
		Date departureTime;
		if(request.getParameter("departureDate") !=null && request.getParameter("departureDate").length()>4) {
			departureTime = Date.valueOf(request.getParameter("departureDate"));
				java.util.Date date1=null;
				try {
					date1 = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("departureDate"));
					Calendar cal = Calendar.getInstance();
					cal.setTime(date1);
					cal.add(Calendar.DATE, 1);
					date1 = cal.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				departureTime = new Date(date1.getTime());
				LOG.trace("Found in request parameters: departureDate --> " + departureTime);
		}
		else{
			departureTime = shipNow.getDepartureTime();
		}
		
		Integer reqId= shipNow.getDriverShippngRequestId();
		
		Integer carId= shipNow.getCarId();

		flightServ.updateShipping(dispathcerId, status, reqId ,carId, arrivalCity, departureCity, departureTime, shipNow.getId());
		LOG.trace("Update flight" );
		
		int methodFlight = 2;
		request.getSession().setAttribute("methodFlight", methodFlight);
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_FLIGHTS_POST);
		LOG.trace("Set the session attribute: methodFlight --> " + methodFlight);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
