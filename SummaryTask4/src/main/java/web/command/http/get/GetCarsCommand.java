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
import model.Firm;
import modelView.CarView;
import service.CarService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class GetCarsCommand implements Command{
	
	private static final Logger LOG = Logger.getLogger(GetCarsCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_CARS);
		CarService carServ = CarService.getInstance();
		List<Car> cars = carServ.findAllCars();
		LOG.trace("Found in DB: cars --> " + cars);
		List<CarView> carViews = new ArrayList<>();
		for (Car car: cars) {
			CarView carV= extractCarView(car,carServ);
			carViews.add(carV);
		}
		List<Car> carsNotUsed = carServ.findAllCarsNotInTrip();
		List<Firm> firms = carServ.findAllFrims();
		int methodCar = 1;
		session.setAttribute("carsNotUsed", carsNotUsed);
		LOG.trace("Set the session attribute: carsNotUsed --> " + carsNotUsed);
		session.setAttribute("methodCar", methodCar);
		LOG.trace("Set the session attribute: methodCar --> " + methodCar);
		session.setAttribute("firms", firms);
		LOG.trace("Set the session attribute: firms --> " + firms);
		session.setAttribute("carViews", carViews);
		LOG.trace("Set the session attribute: carViews --> " + carViews);
		session.setAttribute("cars", cars);
		LOG.trace("Set the session attribute: cars --> " + cars);
		LOG.debug("Command finished");
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
		
		return carV;
	}
}
