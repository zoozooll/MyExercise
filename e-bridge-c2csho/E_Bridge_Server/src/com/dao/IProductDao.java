package com.dao;

import java.util.List;

import org.hibernate.Query;
 
import com.vo.Brand;
import com.vo.DeliveryBill;
import com.vo.Product;
import com.vo.ProductGroup;
import com.vo.ProductType;
import com.vo.ReceiptBill;
import com.vo.Vender;

public interface IProductDao {
	
	 //产品列表
	public List<Product> gerPerProdcut(String hql,int start,int size) throws Exception;
	//产品类别列表
	public List<ProductType> getType(String hql) throws Exception;
	//品牌列表
	public List<Brand> getBrand(String hql, int start, int size) throws Exception;
	//获取总数
	public int getCount(String hql) throws Exception;
	//产品相信信息
	public Product getProduct(int id)throws Exception;
	//增加产品
	public void addProduct(Product p)throws Exception;
	//删除产品
	public void delProduct(Product p)throws Exception;
	//修改产品
	public void updateProduct(Product p)throws Exception;
	//获取该卖家的产品组；
	public List<ProductGroup> getPerGroup(int venderId)throws Exception;
	//为该卖家增加产品组
	public void addGroup(ProductGroup productGroup)throws Exception;
	//为该卖家删除产品组
	public void delGroup(ProductGroup productGroup)throws Exception;
	//产品组的详细信息
	public ProductGroup getGroupById(int productGroupId)throws Exception;
	//新产品列表
	public List<Product> newProducts(String hql) throws Exception;
	//按照价格高低排名查询产品
	public List<Product> cheapProducts(String hql) throws Exception;
	//热卖产品列表；
	public List<Product> hotProducts(String hql) throws Exception;
	//获取收款单列表
	public List<ReceiptBill> findAllRecivery()throws Exception;
	//获取送货单
	public List<DeliveryBill> findAllDelivery()throws Exception;
	//搜索产品
	public String[] findProductName(String name) throws Exception ;
}	
