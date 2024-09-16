package fr.nilpo.pan.models.boulders;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fr.nilpo.pan.models.PanEntity;
import fr.nilpo.pan.models.users.AppUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DefaultPhoto implements PanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false)
	private byte[] data;
	
	@Column(nullable = false)
	private int width;
	
	@Column(nullable = false)
	private int height;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private AppUser createdBy;
	
	public void setData(String data) {
		var imageDetails = ImageHelper.parseDataUrl(data);
		
		this.data = imageDetails.getData();
		this.width = imageDetails.getWidth();
		this.height = imageDetails.getHeight();
	}
	
	public String getData() {
		return ImageHelper.toDataURL(this.data);
	}
	
	public void setRawData(byte[] data) {
		this.data = data;
	}
}
