package web.command;

import web.controller.RequestType;

public interface CommandResult {
	RequestType getType();
	String getResult();
}
