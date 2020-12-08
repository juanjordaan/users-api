package cloud.jordaan.juan.kinetic.application.scqresque.command.model;

public class UserCommandResponse {
	private Long id;
	private String username;
	private String phone;

	public UserCommandResponse() {

	}

	public UserCommandResponse(Long id, String username, String phone) {
		super();
		this.id = id;
		this.username = username;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "UserCommandResponse[id=" + id + ",username=" + username + ",phone=" + phone + "]";
	}
}
