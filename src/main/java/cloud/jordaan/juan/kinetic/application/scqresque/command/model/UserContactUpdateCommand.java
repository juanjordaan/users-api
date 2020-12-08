package cloud.jordaan.juan.kinetic.application.scqresque.command.model;

import java.io.Serializable;

public class UserContactUpdateCommand implements Serializable {
	private static final long serialVersionUID = -1170647007595609785L;

	protected String phone;

	public UserContactUpdateCommand() {

	}

	public UserContactUpdateCommand(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
