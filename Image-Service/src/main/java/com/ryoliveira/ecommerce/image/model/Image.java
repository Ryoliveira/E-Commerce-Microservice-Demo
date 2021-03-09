package com.ryoliveira.ecommerce.image.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product_image")
public class Image {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="product_id")
	private int productId;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="file_type")
	private String fileType;
	
	@Lob
	@Column(name="image_bytes", columnDefinition = "LONGBLOB")
	private String image;

}
