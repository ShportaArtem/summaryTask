package web.command.http.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.CarService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class DeleteCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(DeleteCarCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		Integer idCar= Integer.parseInt(request.getParameter("carId"));
		LOG.trace("Found in request parameters: carId --> " + idCar);
		
		CarService service = CarService.getInstance();
		service.deleteCartById(idCar);
		LOG.trace("Delete car by id --> " + idCar);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST, Path.PAGE_CARS_POST);

		LOG.debug("Commands finished");
		return cr;
	}

}
