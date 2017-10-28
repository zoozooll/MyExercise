package com.action;
 

import java.util.Collection;
import java.util.Iterator;
import java.util.List; 
import javax.servlet.http.HttpSession; 
import com.bo.CartBoImpl;
import com.bo.ICartBo;
import com.vo.CartItem;
import com.vo.Product;
 

//���ﳵaction
public class CartAction extends BasicAction {
     
	private CartBoImpl cartBo;
	 
	private int productId;  //��Ʒid
	private int productSum;    //��Ʒ����
	 
	 
    //���빺�ﳵ
	public String  addProductToCart() {
		//System.out.println("action ���빺�ﳵ......productId........."+productId);
		  Product p = null; 
			HttpSession session = this.getRequest().getSession();
			 
			 cartBo = (CartBoImpl) session.getAttribute("cart");
			if (cartBo == null) {
				System.out.println("cartbo �ǿյ�........");
				cartBo = new CartBoImpl();
				session.setAttribute("cart", cartBo);
			}
			 
			//���ڲ鿴��Ʒ��ϸ��Ϣ����ڽ����
			Product product=(Product) session.getAttribute("product"); 
			if(product!=null){
				System.out.println("cart�鿴��Ʒ��ϸ��Ϣ����ڲ鿴��Ʒ��ϸ��Ϣ�����........");
				 p=product;
			 }else{
				//�����ڲ鿴��Ʒ��ϸ��Ϣ����ڽ����
				 System.out.println("===========>�����ڲ鿴��Ʒ��ϸ��Ϣ�����....��������id��...."+productId);
					List<Product> list = (List<Product>) session.getAttribute("products"); 
					Iterator<Product> products = list.iterator(); 
					while (products.hasNext()) {
						Product pro = products.next();
						System.out.println("�ڴ��еõ��Ĳ�Ʒid��===>"+pro.getProId());
						
						if (pro.getProId() == productId) {
							p = pro;
						}
					}
			 } 
			cartBo.addProductToCart(p);
		
		return "shopcart";
	}
	//ɾ�����ﳵ�еĲ�Ʒ
	public String delProductFormCart(){

		try { 
			//��session�еõ����ﳵ
			cartBo = (CartBoImpl) this.getRequest().getSession().getAttribute("cart");
			Collection<CartItem> list = cartBo.getMapValue();
			if (cartBo == null) {
				cartBo = new CartBoImpl();
				this.getRequest().getSession().setAttribute("cart", cartBo);
			}
			Product p = null;
			for (CartItem c : list) {
				//System.out.println("ҳ�洫������id��"+productId);
				if (c.getProduct().getProId() == productId) {
					p = c.getProduct();
					break;
				}
			}
			cartBo.delProductFormCart(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "shopcart";
	}

	//�޸Ĺ��ﳵ�в�Ʒ����
	public String  updateProductSumFromCart() {
		try {
			System.out.println("�޸�ҳ�洫������id��"+productId);
			System.out.println("�޸�ҳ�洫������������========"+productSum); 
			HttpSession session = this.getRequest().getSession();
			cartBo=(CartBoImpl) session.getAttribute("cart");
			Collection<CartItem> list = cartBo.getMapValue();
			System.out.println(" cartbo========"+session.getAttribute("cart"));
			System.out.println(" cartbo=cartBo.getMapValue()======="+cartBo.getMapValue());
			if(cartBo==null)
			{
				cartBo=new CartBoImpl();
				session.setAttribute("cart", cartBo);
			} 
			Product p=null;
			
			for(CartItem c: list){
				if(c.getProduct().getProId()==productId){
					p=c.getProduct();
					break;
				} 
			} 
			System.out.println("��Ʒ��===>"+p);
			 System.out.println("action=======>"+cartBo);
			 
			cartBo.updateProductSumFromCart(p.getProId(),productSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "shopcart";
	}
	//��չ��ﳵ
	public String removeCart(){
		try {
			 
			CartBoImpl cart=(CartBoImpl)this.getRequest().getSession().getAttribute("cart");
			System.out.println("��չ��ﳵ==cart==>"+cart);
			System.out.println("��չ��ﳵ==cart.getCart==>"+cart.getCart());
			 
			cart.getCart().clear();
			this.getRequest().getSession().setAttribute("cart", cart);
			 System.out.println("��ճɹ�!");
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return  "shopcart"; 
	}
	//��ʾ���ﳵ�����в�Ʒ�۸�
	public String showCar()
	{
		try {
			 
			cartBo=(CartBoImpl)this.getRequest().getSession().getAttribute("car");
			if(cartBo==null)
			{
				cartBo=new CartBoImpl();
				this.getRequest().getSession().setAttribute("cart", cartBo);
			} 
			Collection<CartItem> list=cartBo.getMapValue();
			double AllProductSumMoney=0;
			for(CartItem c: list){
				AllProductSumMoney+=c.getSumMoney();
			} 
			System.out.println("���ﳵ���ܽ����====>"+AllProductSumMoney);
			this.getRequest().setAttribute("total", AllProductSumMoney);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return  "shopcar";
		
	}
 
	public CartBoImpl getCartBo() {
		return cartBo;
	}
	public void setCartBo(CartBoImpl cartBo) {
		this.cartBo = cartBo;
	}
 

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductSum() {
		return productSum;
	}
	public void setProductSum(int productSum) {
		this.productSum = productSum;
	}
}
