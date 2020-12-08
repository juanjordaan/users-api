package cloud.jordaan.juan.kinetic.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user")
public class User {
	@Id
	private Long id;
	private String username;
	private String password;
	private String phone;

	public User() {
		
	}

	public User(String username, String password, String phone) {
		this.username = username;
		this.password = password;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public User setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	@Override
	public String toString() {
		return "User[id=" + id + ",username=" + username + ",password=" + password + ",phone=" + phone + "]";
	}
}
