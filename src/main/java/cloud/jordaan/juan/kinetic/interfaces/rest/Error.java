package cloud.jordaan.juan.kinetic.interfaces.rest;

public class Error {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Error() {
		
	}

	public Error(String message) {
		super();
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
