package web.command.http.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import service.RequestService;
import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class DeleteMyRequestCommand implements Command{

	private static Logger LOG = Logger.getLogger(DeleteMyRequestCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, AppException {

		LOG.debug("Command starts");
		
		Integer myRequestId = Integer.parseInt(request.getParameter("myRequestId"));
		LOG.trace("Found in request parameters: myRequestId --> " + myRequestId);
		
		RequestService service = RequestService.getInstance();
		service.deleteRequestById(myRequestId);
		LOG.trace("Delete request by id --> " + myRequestId);
		
		CommandResult cr = new HttpCommandResult(RequestType.POST, Path.PAGE_MY_REQUESTS_POST);

		LOG.debug("Commands finished");
		return cr;
	}

}
