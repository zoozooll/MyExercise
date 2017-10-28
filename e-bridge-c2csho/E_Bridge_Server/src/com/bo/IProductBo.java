package com.bo;

import java.util.List;

import com.vo.Brand;
import com.vo.DeliveryBill;
import com.vo.PageBean;
import com.vo.Product;
import com.vo.ProductGroup;
import com.vo.ProductType;
import com.vo.ReceiptBill;
import com.vo.Vender;

public interface IProductBo {

	 //查看所有产品种类
	public List<ProductType> getCategory();
    //根据id查找相应的产品信息
	public Product getProductById(int productId);
	//查询所有产品信息
	public PageBean getPerProduct(int pageSize,int page);
	//分类查询产品信息(用到分页)
	public PageBean getPerProductByType(int pageSize,int page,int typeId);
	//增加产品
	public void addProduct(Product product);
    //删除指定的产品信息
	public void delProductById(int productid);
	//修改产品信息
	public void updateProduct(Product product);
	//根据品牌查找产品；
	public PageBean getProductByBrand(int start,int size,int brandId);
	//查看该用户所有的产品；
	public List<Product> getProductByUser(int venId);
	//查看所有的品牌
	public PageBean getBrand(int pageSize,int page);
	//增加一个产品组
	public void addGroup(ProductGroup productGroup);
	//查询卖家的产品组；
	public List<ProductGroup> findGroup(Vender vender);
	//删除一个产品组；
	public void delGroup(int GroupId);

	//查询最新的几个产品
	public List<Product> newProducts() throws Exception;
	
	//查询价格比较便宜的产品
	public List<Product> cheapProducts() throws Exception;
	
	//查询热门产品
	public List<Product> hotProducts() throws Exception;
	//交货凌晨
	public List<DeliveryBill> findAllDelivery();
	//收款单
	public List<ReceiptBill> findAllRecivery();
	public String[] findProductName(String name) throws Exception ;
	
}
