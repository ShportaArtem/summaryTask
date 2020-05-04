package web.command.http.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.DispatcherService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;
/**
 * Delete dispatcher command
 * 
 * @author A.Shporta
 */
public class DeleteDispatcherCommand implements Command{

	private static Logger LOG = Logger.getLogger(DeleteDispatcherCommand.class);
	
	private DispatcherService service;
	
	public DeleteDispatcherCommand(DispatcherService service) {
		super();
		this.service = service;
	}

	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		Integer dispatcherId = Integer.parseInt(request.getParameter("dispatcherId"));
		LOG.trace("Found in request parameters: dispatcherId --> " + dispatcherId);
		
		service.deleteDispatcherById(dispatcherId);
		LOG.trace("Delete dispatcher by id --> " +  dispatcherId);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST, Path.PAGE_DISPATCHERS_POST);
		
		LOG.debug("Commands finished");
		return cr;
	}

}
