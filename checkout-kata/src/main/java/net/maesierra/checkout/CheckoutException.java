package net.maesierra.checkout;

/**
 * Exception related to the checkout service
 *
 */
public class CheckoutException extends Exception {


	public CheckoutException(String message) {
		super(message);
	}


	public CheckoutException(String message, Throwable cause) {
		super(message, cause);
	}

}
