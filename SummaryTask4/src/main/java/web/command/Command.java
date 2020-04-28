package web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.exception.AppException;
import db.exception.DBException;

public interface Command {
	CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws DBException, AppException;
}
