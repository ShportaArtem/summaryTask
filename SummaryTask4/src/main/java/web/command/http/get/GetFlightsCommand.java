package web.command.http.get;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Shipping;
import model.User;
import modelView.ShippingView;
import service.CarService;
import service.FlightService;
import service.LoginService;
import utils.ExtractUtils;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Get flights command
 * 
 * @author A.Shporta
 */
public class GetFlightsCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(GetFlightsCommand.class);
	private LoginService logServ;
	private FlightService flightServ;
	private CarService carService;
	
	public GetFlightsCommand(LoginService logServ, FlightService flightServ, CarService carService) {
		this.flightServ = flightServ;
		this.logServ = logServ;
		this.carService = carService;
	}
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_FLIGHTS);
		List<Shipping> shippings = flightServ.findAllShips();
		LOG.trace("Found in DB: shippings --> " + shippings);
		
		List<ShippingView> shippingsViews = new ArrayList<>();
		User user = (User) session.getAttribute("user");
		if(user.getRoleId()==2) {
		List<Shipping> shippingsNotRequested = flightServ.findShippingsByNotDriverId(user.getId());
		LOG.trace("Found in DB: shippingsNotRequested --> " + shippingsNotRequested);
		
		session.setAttribute("shippingsNotRequested", shippingsNotRequested);
		LOG.trace("Set the session attribute: shippingsNotRequested --> " + shippingsNotRequested);
		
		}else if(user.getId()!=2) {
			List<Shipping> shippingsNotUsed = flightServ.findShippingsByStatus("Open");
			LOG.trace("Found in DB: shippingsNotUsed --> " + shippingsNotUsed);
			
			session.setAttribute("shippingsNotUsed", shippingsNotUsed);
			LOG.trace("Set the session attribute: shippingsNotUsed --> " + shippingsNotUsed);
			}
		
		for (Shipping ship : shippings) {
			ShippingView sh = ExtractUtils.extractShippingView(ship,logServ, flightServ, carService);
			shippingsViews.add(sh);
		}
		session.setAttribute("shippings", shippings);
		LOG.trace("Set the session attribute: shippings --> " + shippings);
		
		session.setAttribute("shippingsViews", shippingsViews);
		LOG.trace("Set the session attribute: shippingsViews --> " + shippingsViews);
		
		int methodFlight = 1;
		if(request.getParameter("methodFlight")!=null) {
			methodFlight = Integer.parseInt(request.getParameter("methodFlight"));
		}
		String sortType = request.getParameter("sortType");
		session.setAttribute("methodFlight", methodFlight);
		LOG.trace("Set the session attribute: methodFlight --> " + methodFlight);
		
		session.setAttribute("sortType", sortType);
		LOG.trace("Set the session attribute: sortType --> " + sortType);
		
		LOG.debug("Commands finished");
		return cr;
	}

	
}
