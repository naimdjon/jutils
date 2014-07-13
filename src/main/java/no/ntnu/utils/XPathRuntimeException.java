package no.ntnu.utils;

public class XPathRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 8753444095051285379L;

	/**
     * Contructs new XPathRuntimeException with the root cause. Usually the cause is the instance of {@link javax.xml.transform.TransformerException}
     *
     * @param cause the cause.
     */
    public XPathRuntimeException(Throwable cause) {
        super(cause);
    }
}