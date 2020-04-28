package web.command.http.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.Path;
import web.command.Command;
import web.command.CommandResult;
import web.command.http.HttpCommandResult;
import web.controller.RequestType;

public class DefaultCommand implements Command{
	
	private static final Logger LOG = Logger.getLogger(DefaultCommand.class);
	
	@Override
	public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Command starts");
		String errorMessage = "No such command";
		request.setAttribute("errorMessage", errorMessage);
		LOG.error("Set the request attribute: errorMessage --> " + errorMessage);
		
		CommandResult cr = new HttpCommandResult(RequestType.GET, Path.PAGE_ERROR_PAGE);
		LOG.debug("Command finished");
		return cr;
	}

}
