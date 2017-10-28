package com.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;

import com.bo.IDeliveryBillBo;
import com.bo.IOrderBo;
import com.vo.DeliveryBill;
import com.vo.ProductDesc;
import com.vo.Purchaser;

/*
 * ������action
 */
public class DeliveryBillAction extends BasicAction {

	private HttpServletRequest request;
	
	//������bo
	private IDeliveryBillBo deliveryBillBo;
	
	private int orderid;//����ID
	private String deliveryCode; //��������� varchar(20)
	private int decimal;//'������',
	private String clientname;//�ͻ�����'
	private String clientcode;//'�ͻ����'
	private String invoiceno;//'��Ʊ���',
	private String province;
	private String city;
	private String address; 
	private String contactor;//'��ϵ��',
	private String sendtype;//'���ͷ�ʽ',
	private String carryplace;//���͵ص�',
	private String specialnote;//����˵��
	private String signstandard;//ǩ�ձ�׼
	private String sendcarnote; //�������ɳ���ע',
	private String signnote;//ǩ�ձ�ע'
	private String pickupman;//�����',
	private String contacttype;//��ϵ��ʽ
	private String contactphone;//�ʹ﷽��ϵ�˵绰' 
			
	/**��Ʒ��ϸ��*****/
	
	private String productcode;  //��Ʒ���� 
	private String productname; //��Ʒ����
	private int productcount;  //��Ʒ����
	private double weight;   //ë��(��Ʒ����)',
	private double solidity; //��Ʒ���
	private String storeaddress; //����ַ
	private String factory;  //��Ʒ����(�ĸ�������)',��
	

    //�ڴ���������ǰ�ҵ����������,��Ʊ���
   public String beforeCreateDeliveryBill(){ 
	deliveryBillBo=(IDeliveryBillBo) this.getBean("deliveryBillBo");  
	DeliveryBill deliveryBill=deliveryBillBo.beforeDeliveryBillBoImpl();
	
	System.out.println("�ڴ���������ǰ�õ��Ľ��������=="+(deliveryBill.getDeliveryCode())+1);
	System.out.println("��Ʊ���=="+(deliveryBill.getInvoiceno())+1);
	 
	System.out.println("this .get request()"+ServletActionContext.getRequest());
	
	ServletActionContext.getRequest().setAttribute("deliveryBillCode", (Integer.valueOf(deliveryBill.getDeliveryCode())+1));
	ServletActionContext.getRequest().setAttribute("invoicenoCode", (Integer.valueOf(deliveryBill.getInvoiceno())+1));
   	
   	         return "DeliveryBill";
   	
   }
   
	//����������
	public String createDeliveryBillBoImpl(){
		DeliveryBill deliveryBill=new DeliveryBill();
		         deliveryBill.setOrderLineId(orderid);
		         deliveryBill.setDeliveryCode(deliveryCode);
		         deliveryBill.setAmount(decimal);
		         deliveryBill.setClientname(clientname);
		         deliveryBill.setClientcode(clientcode);
		         deliveryBill.setInvoiceno(invoiceno);
		         deliveryBill.setProvince(province);
		         deliveryBill.setCity(city);
		         deliveryBill.setAddress(address);
		         deliveryBill.setContactor(contactor);
		         deliveryBill.setSendtype(sendtype);
		         deliveryBill.setCarryplace(carryplace);
		         deliveryBill.setSpecialnote(specialnote);
		         deliveryBill.setSignstandard(signstandard);
		         deliveryBill.setSignnote(signnote);
		         deliveryBill.setPickupman(pickupman);
		         deliveryBill.setContacttype(contacttype);
		         deliveryBill.setContactphone(contactphone);
		         
		ProductDesc  desc=new ProductDesc(); 
				desc.setProductCode(productcode);
				desc.setProductName(productname);
				desc.setProductCount(productcount);
				desc.setWeight(weight);
				desc.setSolidity(solidity);
				desc.setStoreaddress(storeaddress);
				desc.setFactory(factory);
    
				       
		deliveryBillBo.createDeliverybill(deliveryBill);
		return "success";	
	}

	//�鿴���еĽ�����
	public String findAllDeliveryBill(){
		List<DeliveryBill> deliveryBills=deliveryBillBo.findAllDeliveryBill();
		if(deliveryBills!=null&&deliveryBills.size()>0){
			this.getRequest().getSession().setAttribute("deliveryBills", "deliveryBills");
		} 
		return "success";
	}
	 
	
	public IDeliveryBillBo getDeliveryBillBo() {
		return deliveryBillBo;
	}

	public void setDeliveryBillBo(IDeliveryBillBo deliveryBillBo) {
		this.deliveryBillBo = deliveryBillBo;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
 

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public int getDecimal() {
		return decimal;
	}

	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getClientcode() {
		return clientcode;
	}

	public void setClientcode(String clientcode) {
		this.clientcode = clientcode;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public String getCarryplace() {
		return carryplace;
	}

	public void setCarryplace(String carryplace) {
		this.carryplace = carryplace;
	}

	public String getSpecialnote() {
		return specialnote;
	}

	public void setSpecialnote(String specialnote) {
		this.specialnote = specialnote;
	}

	public String getSignstandard() {
		return signstandard;
	}

	public void setSignstandard(String signstandard) {
		this.signstandard = signstandard;
	}

	public String getSendcarnote() {
		return sendcarnote;
	}

	public void setSendcarnote(String sendcarnote) {
		this.sendcarnote = sendcarnote;
	}

	public String getSignnote() {
		return signnote;
	}

	public void setSignnote(String signnote) {
		this.signnote = signnote;
	}

	public String getPickupman() {
		return pickupman;
	}

	public void setPickupman(String pickupman) {
		this.pickupman = pickupman;
	}

	public String getContacttype() {
		return contacttype;
	}

	public void setContacttype(String contacttype) {
		this.contacttype = contacttype;
	}

	public String getContactphone() {
		return contactphone;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public int getProductcount() {
		return productcount;
	}

	public void setProductcount(int productcount) {
		this.productcount = productcount;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getSolidity() {
		return solidity;
	}

	public void setSolidity(double solidity) {
		this.solidity = solidity;
	}

	public String getStoreaddress() {
		return storeaddress;
	}

	public void setStoreaddress(String storeaddress) {
		this.storeaddress = storeaddress;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}
 
	
	
	
	
}
