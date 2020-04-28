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

public class ChooseCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(ChooseCarCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		Integer carId = Integer.parseInt(request.getParameter("carId"));
		LOG.trace("Found in request parameters: carId --> " + carId);
		
		Integer flightId = (Integer) session.getAttribute("flightIdForChoose");
		LOG.trace("Found in request parameters: flightIdForChoose --> " + flightId);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_REQUESTS_POST);
		RequestService reqServ = RequestService.getInstance();
		reqServ.chooseCarForShipping(carId, flightId);
		LOG.trace("Choose car fro flight" );
		
		LOG.debug("Commands finished");
		return cr;
	}

}
