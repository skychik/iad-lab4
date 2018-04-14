package shared.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserAccount {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String login;

	@Column
	private String password;

	@OneToMany(mappedBy="owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<CheckResult> allUserHits;

	public void addHit(CheckResult checkResult) {
		allUserHits.add(checkResult);
	}

	public List<CheckResult> getAllUserHits() {
		return allUserHits;
	}

	public void setAllUserHits(List<CheckResult> allUserHits) {
		this.allUserHits = allUserHits;
	}

	public UserAccount() {
	}

	public UserAccount(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}