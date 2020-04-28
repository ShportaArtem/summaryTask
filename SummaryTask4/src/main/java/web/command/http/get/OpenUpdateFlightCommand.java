package web.command.http.get;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Car;
import model.Shipping;
import model.User;
import modelView.ShippingView;
import service.CarService;
import service.FlightService;
import service.LoginService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class OpenUpdateFlightCommand implements Command {
	
	private static Logger LOG = Logger.getLogger(OpenUpdateFlightCommand.class);
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		Integer idFlight = Integer.parseInt(request.getParameter("flightId"));
		LOG.trace("Found in request parameters: flightId --> " + idFlight);
		
		FlightService flightServ = FlightService.getInstance();
		LoginService logServ = LoginService.getInstance();
		Shipping ship = flightServ.findFlightById(idFlight);
		LOG.trace("Found in DB: ship --> " + ship);
		
		ShippingView sh = new ShippingView();
		sh.setArrivalCity(ship.getArrivalCity());
		if(ship.getCarId()!=0) {
		sh.setCarId(ship.getCarId().toString());
		}else {
			sh.setCarId("In search");
		}
		sh.setCreationTimestamp(ship.getCreationTimestamp());
		sh.setDepartureCity(ship.getDepartureCity());
		User us = logServ.findUserById(ship.getDispathcerId());
		sh.setDispatcherLogin(us.getLogin());
		if (ship.getDriverShippngRequestId() == 0) {
			sh.setDriverShippngRequestId("In search");
		} else {
			User driver = flightServ.findUserByShippingRequestId(ship.getDriverShippngRequestId());
			sh.setDriverShippngRequestId(driver.getLogin());
		}
		sh.setId(ship.getId());
		sh.setStatus(ship.getStatus());
		sh.setDepartureTime(ship.getDepartureTime());
		
		session.setAttribute("flightViewNow", sh);
		LOG.trace("Set the session attribute: flightViewNow --> " + sh);
		
		session.setAttribute("flightNow", ship);
		LOG.trace("Set the session attribute: flightNow --> " + ship);
		
		CarService carServ = CarService.getInstance();
		List<Car> cars = carServ.findAllCarsNotInTrip();
		LOG.trace("Found in DB: cars --> " + cars );
		
		session.setAttribute("cars", cars);
		LOG.trace("Set the session attribute: cars --> " + cars);
		
		List<User> usersByRequest=null;
		try {
			usersByRequest= flightServ.findUsersByShippingId(idFlight);
			LOG.trace("Found in DB: usersByRequest --> " + usersByRequest );
			
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		session.setAttribute("usersByRequest",usersByRequest );
		LOG.trace("Set the session attribute: usersByRequest --> " + usersByRequest);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_UPDATE_FLIGHT);
		return cr;
	}

}
