package com.cgi.pscatalog.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;

/**
 * A DTO for the Products entity.
 */
public class PersonalCatalogDTO implements Serializable {

	private Long id;

	@NotNull
	private String productName;

	private String productDescription;

	@NotNull
	private BigDecimal productPrice;

	@NotNull
	private ProductTypeEnum productType;

	@Lob
	private byte[] productImg;
	private String productImgContentType;

	@NotNull
	private Integer orderQuantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public ProductTypeEnum getProductType() {
		return productType;
	}

	public void setProductType(ProductTypeEnum productType) {
		this.productType = productType;
	}

	public byte[] getProductImg() {
		return productImg;
	}

	public void setProductImg(byte[] productImg) {
		this.productImg = productImg;
	}

	public String getProductImgContentType() {
		return productImgContentType;
	}

	public void setProductImgContentType(String productImgContentType) {
		this.productImgContentType = productImgContentType;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PersonalCatalogDTO personalCatalogDTO = (PersonalCatalogDTO) o;
		if (personalCatalogDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), personalCatalogDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "PersonalCatalogDTO{" + "id=" + getId() + ", productName='" + getProductName() + "'"
				+ ", productDescription='" + getProductDescription() + "'" + ", productPrice=" + getProductPrice()
				+ ", productType='" + getProductType() + "'" + ", productImg='" + getProductImg() + "'"
				+ ", orderQuantity='" + getOrderQuantity() + "'" + "}";
	}
}
