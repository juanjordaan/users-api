package cloud.jordaan.juan.kinetic.application.scqresque.query.model;

public class UserContactDetails {
	private Long id;
	private String username;
	private String phone;

	public UserContactDetails() {

	}

	public UserContactDetails(Long id, String username, String phone) {
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
		return "UserContactDetails[id=" + id + ",username=" + username + ",phone=" + phone + "]";
	}
}
