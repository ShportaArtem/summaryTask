package web.command;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandDispatcher {
	protected Map<String, Command> commandMap;
	protected Command defaultCommand;
	
	public CommandDispatcher( Command defaultCommand) {
		this.commandMap = new HashMap<>();
		this.defaultCommand = defaultCommand;
	}

	public Command getDefaultCommand() {
		return defaultCommand;
	}

	public void setDefaultCommand(Command defaultCommand) {
		this.defaultCommand = defaultCommand;
	}
	
	public abstract boolean addCommand(String commandName, Command command);
	public abstract Command getCommand(String commandName);
}