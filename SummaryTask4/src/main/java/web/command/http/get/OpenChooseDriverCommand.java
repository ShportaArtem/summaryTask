package web.command.http.get;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.DriverShippingRequest;
import modelView.RequestView;
import service.LoginService;
import service.RequestService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class OpenChooseDriverCommand implements Command{

	private static Logger LOG = Logger.getLogger(OpenChooseDriverCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_REQUESTS);
		RequestService requestServ = RequestService.getInstance();
		Integer flightId=null;
		int chooseRequestStage= Integer.parseInt(request.getParameter("chooseStage"));
		if(chooseRequestStage==1) {
			flightId = Integer.parseInt(request.getParameter("flightId"));
			LOG.trace("Found in request parameters: flightId --> " + flightId);
			
			session.setAttribute("flightIdForChoose", flightId);
			LOG.trace("Set the session attribute: flightIdForChoose --> " + flightId);
			
		}else{
			flightId = (Integer) session.getAttribute("flightIdForChoose");
		}
		List<DriverShippingRequest> requests = requestServ.findRequestByShippingId(flightId);
		LOG.trace("Found in DB: requests --> " + requests );
		
		List<RequestView> requestsView = new ArrayList<>();
		int methodRequests = 1;
		for(DriverShippingRequest req : requests) {
			requestsView.add(extractRequestView(req));
		}
		session.setAttribute("methodRequests", methodRequests);
		LOG.trace("Set the session attribute: methodRequests --> " + methodRequests);
		
		session.setAttribute("chooseRequestStage", chooseRequestStage);
		LOG.trace("Set the session attribute: chooseRequestStage --> " + chooseRequestStage);
		
		session.setAttribute("requests", requests);
		LOG.trace("Set the session attribute: requests --> " + requests);
		
		session.setAttribute("requestsView", requestsView);
		LOG.trace("Set the session attribute: requestsView --> " + requestsView);
		
		LOG.debug("Commands finished");
		return cr;
	}
	
	private RequestView extractRequestView(DriverShippingRequest req) throws AppException {
		RequestView view = new RequestView();
		view.setCarryinfCapacity(req.getCarryinfCapacity());
		view.setId(req.getId());
		view.setPassangersCapacity(req.getPassangersCapacity());
		view.setVehicleCondition(req.getVehicleCondition());
		view.setShippingId(req.getShippingId());
		LoginService logServ = LoginService.getInstance();
		
		view.setDriverLogin(logServ.findUserById(req.getDriverId()).getLogin());
		return view;
	}
	
}
