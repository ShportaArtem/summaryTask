package web.command.http.get;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import model.User;
import modelView.DriverView;
import service.DriverService;
import service.RequestService;
import utils.ExtractUtils;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Get drivers command
 * 
 * @author A.Shporta
 */
public class GetDriversCommand implements Command{
	
	private static Logger LOG = Logger.getLogger(GetDriversCommand.class);
	
	private DriverService driverServ;
	private RequestService reqServ;
	
	public GetDriversCommand(DriverService driverServ, RequestService reqServ) {
		super();
		this.driverServ = driverServ;
		this.reqServ = reqServ;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession(false);
		CommandResult cr = new HttpCommandResult(RequestType.GET,  Path.PAGE_DRIVERS);
		List<User> users = driverServ.findAllUsersByRoleId(2);
		LOG.trace("Found in DB: users --> " + users);
		
		List<DriverView> driverViews = new ArrayList<>();
		for(User us : users) {
			driverViews.add(ExtractUtils.extractDriverView(us, driverServ));
		}
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
	
	
}
