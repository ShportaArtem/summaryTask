package db.exception;

public class DBException extends AppException{

	private static final long serialVersionUID = -3514203421201131038L;
	
	public DBException() {
		super();
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
}
