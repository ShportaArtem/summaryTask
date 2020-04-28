package web.command.http.get;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.Driver;
import model.User;
import modelView.DriverView;
import service.DriverService;
import service.RequestService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class GetDriversCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(GetDriversCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_DRIVERS);
		DriverService driverServ = DriverService.getInstance();
		List<User> users = driverServ.findAllUsersByRoleId(2);
		LOG.trace("Found in DB: users --> " + users);
		
		List<DriverView> driverViews = new ArrayList<>();
		for(User us : users) {
			driverViews.add(extractDriverView(us, driverServ));
		}
		RequestService reqServ = RequestService.getInstance();
		List<User> driversNotUsed = driverServ.findAllUsersByRoleId(2);
		LOG.trace("Found in DB: driversNotUsed --> " + driversNotUsed );
		
		for(User u : driversNotUsed) {
			if(reqServ.findRequestInProcessByDriverId(u.getId())!=null) {
				driversNotUsed.remove(u);
			}
		}
		
		session.setAttribute("driverViews", driverViews);
		LOG.trace("Set the session attribute: driverViews --> " + driverViews);
		
		session.setAttribute("driversNotUsed",driversNotUsed);
		LOG.trace("Set the session attribute: driversNotUsed --> " + driversNotUsed);
		
		int methodDriver = 1;
		session.setAttribute("methodDriver", methodDriver);
		LOG.trace("Set the session attribute: methodDriver --> " + methodDriver);
		
		LOG.debug("Commands finished");
		return cr;
	}
	
	private DriverView extractDriverView( User userD, DriverService driverServ) throws AppException {
		DriverView driverV = new DriverView();
		Driver driver = driverServ.findDriverByUserId(userD.getId());
		driverV.setId(userD.getId());
		driverV.setLogin(userD.getLogin());
		driverV.setName(userD.getName());
		driverV.setPassword(userD.getPasswordView());
		driverV.setSurname(userD.getSurname());
		driverV.setPassport(driver.getPassport());
		driverV.setPhone(driver.getPhone());
		return driverV;
	}
}
