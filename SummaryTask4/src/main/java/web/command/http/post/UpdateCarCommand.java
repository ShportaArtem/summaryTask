package web.command.http.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Car;
import service.CarService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class UpdateCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(UpdateCarCommand.class);

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		CarService carServ = CarService.getInstance();
		Car carNow = (Car) session.getAttribute("carNow");
		LOG.trace("Found in session atrubutes: carNow --> " + carNow);
		
		Car car = new Car();
		if(request.getParameter("firm")!=null) {
		car.setFirmId(carServ.findFirmByName(request.getParameter("firm")).getId());
		LOG.trace("Found in request parameters: firm --> " + car.getFirmId());
		
		}else {
			car.setFirmId(carNow.getFirmId());
		}
		car.setId(carNow.getId());
		if(!"".equals(request.getParameter("carryingCapacity"))) {
		car.setCarryinfCapacity(Integer.parseInt(request.getParameter("carryingCapacity")));
		LOG.trace("Found in request parameters: carryingCapacity --> " + car.getCarryinfCapacity());
		
		}else {
			car.setCarryinfCapacity(carNow.getCarryinfCapacity());
		}
		if(!"".equals(request.getParameter("passengersCapacity"))) {
		car.setPassangersCapacity(Integer.parseInt(request.getParameter("passengersCapacity")));
		LOG.trace("Found in request parameters: passengersCapacity --> " + car.getPassangersCapacity());
		
		}else{
			car.setPassangersCapacity(carNow.getPassangersCapacity());
		}
		if(request.getParameter("vehicleCondition")!=null) {
			car.setVehicleCondition(request.getParameter("vehicleCondition"));
			LOG.trace("Found in request parameters: vehicleCondition --> " + car.getVehicleCondition());
			
		}else {
			car.setVehicleCondition(carNow.getVehicleCondition());
		}
		if(!"".equals(request.getParameter("model")))	{
			car.setModel(request.getParameter("model"));
			LOG.trace("Found in request parameters: model --> " + car.getModel());
		
		}else {
			car.setModel(carNow.getModel());
		}
		car.setStatus(carNow.getStatus());
		carServ.updateCar(car);
		LOG.trace("Update car -->"+ car );
		
		int methodCar = 2;
		request.getSession().setAttribute("methodCar", methodCar);
		CommandResult cr = new HttpCommandResult(RequestType.POST,Path.PAGE_CARS_POST);
		LOG.trace("Set the session attribute: methodCar --> " + methodCar);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
