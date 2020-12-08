package cloud.jordaan.juan.kinetic.interfaces.rest;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
	private List<Error> errors;

	public ErrorResponse() {
		
	}

	public ErrorResponse(List<Error> errors) {
		this.errors = errors;
	}

	public ErrorResponse(Error error) {
		addError(error);
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public void addError(Error error) {
		if(errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(error);
	}

	@Override
	public String toString() {
		return "ErrorResponse[" + errors + "]";
	}
}
