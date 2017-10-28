package com.action;
 

import java.util.Collection;
import java.util.Iterator;
import java.util.List; 
import javax.servlet.http.HttpSession; 
import com.bo.CartBoImpl;
import com.bo.ICartBo;
import com.vo.CartItem;
import com.vo.Product;
 

//购物车action
public class CartAction extends BasicAction {
     
	private CartBoImpl cartBo;
	 
	private int productId;  //产品id
	private int productSum;    //产品数量
	 
	 
    //加入购物车
	public String  addProductToCart() {
		//System.out.println("action 加入购物车......productId........."+productId);
		  Product p = null; 
			HttpSession session = this.getRequest().getSession();
			 
			 cartBo = (CartBoImpl) session.getAttribute("cart");
			if (cartBo == null) {
				System.out.println("cartbo 是空的........");
				cartBo = new CartBoImpl();
				session.setAttribute("cart", cartBo);
			}
			 
			//是在查看产品详细信息的入口进入的
			Product product=(Product) session.getAttribute("product"); 
			if(product!=null){
				System.out.println("cart查看产品详细信息的入口查看产品详细信息的入口........");
				 p=product;
			 }else{
				//不是在查看产品详细信息的入口进入的
				 System.out.println("===========>不是在查看产品详细信息的入口....传过来的id是...."+productId);
					List<Product> list = (List<Product>) session.getAttribute("products"); 
					Iterator<Product> products = list.iterator(); 
					while (products.hasNext()) {
						Product pro = products.next();
						System.out.println("内存中得到的产品id是===>"+pro.getProId());
						
						if (pro.getProId() == productId) {
							p = pro;
						}
					}
			 } 
			cartBo.addProductToCart(p);
		
		return "shopcart";
	}
	//删除购物车中的产品
	public String delProductFormCart(){

		try { 
			//在session中得到购物车
			cartBo = (CartBoImpl) this.getRequest().getSession().getAttribute("cart");
			Collection<CartItem> list = cartBo.getMapValue();
			if (cartBo == null) {
				cartBo = new CartBoImpl();
				this.getRequest().getSession().setAttribute("cart", cartBo);
			}
			Product p = null;
			for (CartItem c : list) {
				//System.out.println("页面传过来的id是"+productId);
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

	//修改购物车中产品数量
	public String  updateProductSumFromCart() {
		try {
			System.out.println("修改页面传过来的id是"+productId);
			System.out.println("修改页面传过来的数量是========"+productSum); 
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
			System.out.println("产品是===>"+p);
			 System.out.println("action=======>"+cartBo);
			 
			cartBo.updateProductSumFromCart(p.getProId(),productSum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "shopcart";
	}
	//清空购物车
	public String removeCart(){
		try {
			 
			CartBoImpl cart=(CartBoImpl)this.getRequest().getSession().getAttribute("cart");
			System.out.println("清空购物车==cart==>"+cart);
			System.out.println("清空购物车==cart.getCart==>"+cart.getCart());
			 
			cart.getCart().clear();
			this.getRequest().getSession().setAttribute("cart", cart);
			 System.out.println("清空成功!");
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return  "shopcart"; 
	}
	//显示购物车的所有产品价格
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
			System.out.println("购物车的总金额是====>"+AllProductSumMoney);
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
