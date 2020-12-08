package cloud.jordaan.juan.kinetic.application.scqresque.command.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class UserCreateCommand implements Serializable {
	private static final long serialVersionUID = -8821081167113216522L;

	@NotBlank(message = "username may not be empty")
	private String username;

	@NotBlank(message = "password may not be empty")
	private String password;

	@NotBlank(message = "phone may not be empty")
	private String phone;

	public UserCreateCommand() {

	}

	public UserCreateCommand(String username, String password, String phone) {
		super();
		this.username = username;
		this.password = password;
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "UserCreateCommand[username=" + username + ",password=" + password + ",phone=" + phone + "]";
	}
}
