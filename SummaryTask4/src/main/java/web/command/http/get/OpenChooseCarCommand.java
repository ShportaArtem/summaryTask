package web.command.http.get;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Car;
import modelView.CarView;
import service.CarService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class OpenChooseCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(OpenChooseCarCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_CHOOSE_CAR);
		int methodChooseCar = 1;
		CarService carServ = CarService.getInstance();
		List<Car> carsNotUsed = carServ.findAllCarsNotInTrip();
		LOG.trace("Found in DB: users --> " + carsNotUsed);
		
		List<CarView> carViews = new ArrayList<>();
		for (Car car: carsNotUsed) {
			CarView carV= extractCarView(car,carServ);
			carViews.add(carV);
		}
		session.setAttribute("methodChooseCar", methodChooseCar);
		LOG.trace("Set the session attribute: methodChooseCar --> " + methodChooseCar);
		
		session.setAttribute("chooseCarViews", carViews);
		LOG.trace("Set the session attribute: carViews --> " + carViews);
		
		LOG.debug("Commands finished");
		return cr;
	}
	
	private CarView extractCarView(Car car, CarService carServ) throws AppException {
		CarView carV = new CarView();
		carV.setId(car.getId());
		carV.setCarryingCapacity(car.getCarryinfCapacity());
		carV.setFirmName(carServ.findFirmById(car.getFirmId()).getName());
		carV.setModel(car.getModel());
		if(car.getStatus()==0) {
			carV.setStatus("Parked");
		}else {
			carV.setStatus("In trip");
		}
		carV.setPassangersCapacity(car.getPassangersCapacity());
		carV.setVehicleCondition(car.getVehicleCondition());

		LOG.debug("Commands finished");
		return carV;
	}
}
