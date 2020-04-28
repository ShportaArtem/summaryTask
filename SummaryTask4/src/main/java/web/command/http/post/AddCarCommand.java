package web.command.http.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.CarService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class AddCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(AddCarCommand.class);

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		CarService carServ = CarService.getInstance();
		String model = request.getParameter("model");
		LOG.trace("Found in request parameters: model --> " + model);
		
		Integer carryingCapacity = Integer.parseInt(request.getParameter("carryingCapacity"));
		LOG.trace("Found in request parameters: carryingCapacity --> " + carryingCapacity);
		
		Integer passengersCapacity = Integer.parseInt(request.getParameter("passengersCapacity"));
		LOG.trace("Found in request parameters: passengersCapacity --> " + passengersCapacity);
		
		String firmName = request.getParameter("firm");
		LOG.trace("Found in request parameters: firm --> " + firmName);
		
		Integer firmId = carServ.findFirmByName(firmName).getId();
		LOG.trace("Found in DB: firmId --> " + firmId);
		
		String vehicleCondition = request.getParameter("vehicleCondition");
		LOG.trace("Found in request parameters: vehicleCondition --> " + vehicleCondition);
		
		carServ.insertCar(model, carryingCapacity, passengersCapacity, firmId, 0, vehicleCondition);
		LOG.trace("Insert in DB car" );
		
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_CARS_POST);
		int methodCar = 2;
		session.setAttribute("methodCar", methodCar);
		LOG.trace("Set the session attribute: methodCar --> " + methodCar);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
