package utils;

import db.exception.AppException;
import model.Car;
import model.Driver;
import model.DriverShippingRequest;
import model.Shipping;
import model.User;
import modelView.CarView;
import modelView.DriverView;
import modelView.RequestView;
import modelView.ShippingView;
import service.CarService;
import service.DriverService;
import service.FlightService;
import service.LoginService;

/**
 * Class with util method for extract views from models
 * @author A.Shporta
 *
 */
public class ExtractUtils {
	
	/**
	 * Extract carView from car model
	 * @param car
	 * @param carServ
	 * @return	carView
	 * @throws AppException
	 */
	public static CarView extractCarView(Car car, CarService carServ) throws AppException {
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
	
	
	/**
	 * Extract driverView from driver model
	 * 
	 * @param userD
	 * @param driverServ
	 * @return driverView
	 * @throws AppException
	 */
	public static DriverView extractDriverView( User userD, DriverService driverServ) throws AppException {
		DriverView driverV = new DriverView();
		Driver driver = driverServ.findDriverByUserId(userD.getId());
		driverV.setId(userD.getId());
		driverV.setLogin(userD.getLogin());
		driverV.setName(userD.getName());
		driverV.setPassword(userD.getPassword());
		driverV.setSurname(userD.getSurname());
		driverV.setPassport(driver.getPassport());
		driverV.setPhone(driver.getPhone());
		return driverV;
	}
	
	/**
	 * Extract shippingView from shipping model
	 * 
	 * @param ship
	 * @param logServ
	 * @param flightServ
	 * @param carServ
	 * @return shippingView
	 * @throws AppException
	 */
	public static ShippingView extractShippingView(Shipping ship, LoginService logServ, FlightService flightServ, CarService carServ) throws AppException {
		ShippingView sh = new ShippingView();
		sh.setArrivalCity(ship.getArrivalCity());
		if(ship.getCarId()!=0) {
			Car car = carServ.findCarById(ship.getCarId());
			sh.setCarId(car.getModel());
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
		return sh;
	}
	
	/**
	 * Extract requestView from driverShippingRequest model
	 * @param req
	 * @param logServ
	 * @return
	 * @throws AppException
	 */
	public static RequestView extractRequestView(DriverShippingRequest req, LoginService logServ) throws AppException {
		RequestView view = new RequestView();
		view.setCarryinfCapacity(req.getCarryinfCapacity());
		view.setId(req.getId());
		view.setPassangersCapacity(req.getPassangersCapacity());
		view.setVehicleCondition(req.getVehicleCondition());
		view.setShippingId(req.getShippingId());
		
		view.setDriverLogin(logServ.findUserById(req.getDriverId()).getLogin());
		return view;
	}
}
