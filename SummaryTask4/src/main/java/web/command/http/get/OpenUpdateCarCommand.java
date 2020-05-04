package web.command.http.get;

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
/**
 * Open update car command
 * 
 * @author A.Shporta
 */
public class OpenUpdateCarCommand implements Command {
	
	private static Logger LOG = Logger.getLogger(OpenUpdateCarCommand.class);
	
	private CarService carServ;
	
	public OpenUpdateCarCommand(CarService carServ) {
		super();
		this.carServ = carServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		Integer idCar = Integer.parseInt(request.getParameter("carId"));
		LOG.trace("Found in request parameters: carId --> " + idCar);
		
		Car car = carServ.findCarById(idCar);
		LOG.trace("Found in DB: car --> " + car);
		
		CarView carView = new CarView();
		carView.setCarryingCapacity(car.getCarryinfCapacity());
		carView.setId(car.getId());
		carView.setModel(car.getModel());
		carView.setPassangersCapacity(car.getPassangersCapacity());
		carView.setVehicleCondition(car.getVehicleCondition());
		if(car.getStatus()==0) {
			carView.setStatus("Parked");
		}else {
			carView.setStatus("In trip");
		}
		Firm firm = carServ.findFirmById(car.getFirmId());
		carView.setFirmName(firm.getName());
		session.setAttribute("carViewNow", carView);
		LOG.trace("Set the session attribute: carViewNow --> " + carView);
		
		session.setAttribute("carNow", car);
		LOG.trace("Set the session attribute: carNow --> " + car);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_UPDATE_CAR);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
