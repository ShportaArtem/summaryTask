package web.command.http.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Open add car command
 * 
 * @author A.Shporta
 */
public class OpenAddCarCommand implements Command{

	private static Logger LOG = Logger.getLogger(OpenAddCarCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {
		
		LOG.debug("Command starts");
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_ADD_CAR);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
