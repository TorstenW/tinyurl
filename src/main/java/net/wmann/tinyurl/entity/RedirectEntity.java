package net.wmann.tinyurl.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;

@Entity
@Getter
public class RedirectEntity {
	
	@Id
	private String id;
	
	@Column(name = "redirecturl", nullable = false)
	private String redirectUrl;
	
	@Column(name = "created", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name = "updated", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	@Column(name = "accessed")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessed;
	
	protected RedirectEntity() {
		this.created = new Date();
		this.updated = new Date();
	}
	
	public RedirectEntity(String id, String redirectUrl) {
		this();
		this.id = id;
		this.redirectUrl = redirectUrl;
	}
	
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	@PreUpdate
	private void onUpdate() {
		this.updated = new Date();
	}
	
	@PostLoad
	private void onLoad() {
		this.accessed = new Date();
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		StringBuilder builder = new StringBuilder();
		builder.append("RedirectEntity [id=");
		builder.append(id);
		builder.append(", redirectUrl=");
		builder.append(redirectUrl);
		builder.append(", created=");
		builder.append(sdf.format(created));
		builder.append(", updated=");
		builder.append(sdf.format(updated));
		builder.append(", accessed=");
		builder.append(accessed == null ? "n/a" : sdf.format(accessed));
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedirectEntity other = (RedirectEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
