package shared.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class CheckResult {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private BigDecimal x;

	@Column
	private BigDecimal y;

	@Column
	private BigDecimal r;

	@Column
	private boolean hits;

	@ManyToOne
	@JoinColumn(name="owner")
	@JsonIgnore
	private UserAccount owner;

	public UserAccount getOwner() {
		return owner;
	}

	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}

	public CheckResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hits) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.hits = hits;
	}

	public CheckResult() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public BigDecimal getR() {
		return r;
	}

	public void setR(BigDecimal r) {
		this.r = r;
	}

	public boolean isHits() {
		return hits;
	}

	public void setHits(boolean hits) {
		this.hits = hits;
	}
}