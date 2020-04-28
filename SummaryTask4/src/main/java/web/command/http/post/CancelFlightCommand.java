package web.command.http.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.FlightService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class CancelFlightCommand implements Command{

	private static Logger LOG = Logger.getLogger(CancelFlightCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		Integer idFlight = Integer.parseInt(request.getParameter("flightId"));
		LOG.trace("Found in request parameters: flightId --> " + idFlight);
		
		FlightService service = FlightService.getInstance();
		service.cancelFlightById(idFlight);
		LOG.trace("Cancel flight by id --> " + idFlight);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST, Path.PAGE_FLIGHTS_POST);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
