package no.ntnu.utils;

@SuppressWarnings("serial")
public class AssertionFailedError extends RuntimeException{
	public AssertionFailedError (String msg){
		super(msg);
	}

	public AssertionFailedError(String message, String expected,String actual) {
		super(String.format("%s expected ['%s'], actual ['%s']",message,expected,actual));
	}
}