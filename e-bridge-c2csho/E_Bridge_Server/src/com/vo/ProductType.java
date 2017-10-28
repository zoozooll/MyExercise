package com.vo;
// default package

import java.util.HashSet;
import java.util.Set;


/**
 * 产品种类
 */

public class ProductType  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 4697012910014942504L;
	/**
	 * 
	 */

	private Integer protypeId;
     private ProductType productType;
     private String typeName;
     private String typeCode;
     private Set<ProductType> productTypes = new HashSet<ProductType>();
     
     private Set<Product> products=new HashSet<Product>();


    // Constructors

    /** default constructor */
    public ProductType() {
    }

    
    /** full constructor */
    public ProductType(ProductType productType, String typeName, String typeCode, Set<ProductType> productTypes) {
        this.productType = productType;
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.productTypes = productTypes;
    }

   
    // Property accessors

    public Integer getProtypeId() {
        return this.protypeId;
    }
    
    public void setProtypeId(Integer protypeId) {
        this.protypeId = protypeId;
    }

    public ProductType getProductType() {
        return this.productType;
    }
    
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getTypeName() {
        return this.typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return this.typeCode;
    }
    
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

   


	public Set<ProductType> getProductTypes() {
		return productTypes;
	}


	public void setProductTypes(Set<ProductType> productTypes) {
		this.productTypes = productTypes;
	}


	/**
	 * @return the products
	 */
	public Set<Product> getProducts() {
		return products;
	}


	/**
	 * @param products the products to set
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
   








}