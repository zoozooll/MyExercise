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

	 //�鿴���в�Ʒ����
	public List<ProductType> getCategory();
    //����id������Ӧ�Ĳ�Ʒ��Ϣ
	public Product getProductById(int productId);
	//��ѯ���в�Ʒ��Ϣ
	public PageBean getPerProduct(int pageSize,int page);
	//�����ѯ��Ʒ��Ϣ(�õ���ҳ)
	public PageBean getPerProductByType(int pageSize,int page,int typeId);
	//���Ӳ�Ʒ
	public void addProduct(Product product);
    //ɾ��ָ���Ĳ�Ʒ��Ϣ
	public void delProductById(int productid);
	//�޸Ĳ�Ʒ��Ϣ
	public void updateProduct(Product product);
	//����Ʒ�Ʋ��Ҳ�Ʒ��
	public PageBean getProductByBrand(int start,int size,int brandId);
	//�鿴���û����еĲ�Ʒ��
	public List<Product> getProductByUser(int venId);
	//�鿴���е�Ʒ��
	public PageBean getBrand(int pageSize,int page);
	//����һ����Ʒ��
	public void addGroup(ProductGroup productGroup);
	//��ѯ���ҵĲ�Ʒ�飻
	public List<ProductGroup> findGroup(Vender vender);
	//ɾ��һ����Ʒ�飻
	public void delGroup(int GroupId);

	//��ѯ���µļ�����Ʒ
	public List<Product> newProducts() throws Exception;
	
	//��ѯ�۸�Ƚϱ��˵Ĳ�Ʒ
	public List<Product> cheapProducts() throws Exception;
	
	//��ѯ���Ų�Ʒ
	public List<Product> hotProducts() throws Exception;
	//�����賿
	public List<DeliveryBill> findAllDelivery();
	//�տ
	public List<ReceiptBill> findAllRecivery();
	public String[] findProductName(String name) throws Exception ;
	
}
