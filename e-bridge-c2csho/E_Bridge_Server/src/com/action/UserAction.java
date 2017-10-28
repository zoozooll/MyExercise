package com.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bo.IUserBo;
import com.vo.ProductGroup;
import com.vo.Purchaser;
import com.vo.Vender;

@SuppressWarnings("serial")
public class UserAction extends BasicAction {
	private HttpServletRequest request;

	private Integer purId;

	private String purName;

	private String purPassword;

	private String purTelephone;

	private String purProvince;

	private String purCity;

	private String purAddress;

	private String purPostalcode;

	private String purRemark;

	private String purIsvendot;

	private Integer venId;

	private Integer purchaserId;

	private Integer productgroupId;

	private String venShortname;

	private Integer venShopcard;

	private String venFax;

	private String venLinkman;

	private String venLinkmanphone;

	private String venEmail;

	private Integer venStatus;

	private String message;

	private IUserBo userBo;

	private Purchaser purchaser;

	private Vender vender;

	private String purBeforePassword;

	public IUserBo getUserBo() {
		return userBo;
	}

	public void setUserBo(IUserBo userBo) {
		this.userBo = userBo;
	}

	public String getPurBeforePassword() {
		return purBeforePassword;
	}

	public void setPurBeforePassword(String purBeforePassword) {
		this.purBeforePassword = purBeforePassword;
	}

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String login() throws Exception {
		String result = null;
		try {
			userBo = (IUserBo) getBean("userBo");

			purchaser = userBo.login(this.getPurName(), this.getPurPassword());
			if (purchaser != null) {
				HttpSession session = request.getSession();
				session.setAttribute("purchaser", purchaser);
				result = SUCCESS;
			} else {
				this.setMessage("<font color='red'>您的帐号或密码有误！</font>");
				result = ERROR;
			}
		} catch (Exception e) {
			this.setMessage("<font color='red'>系统出现故障，请与管理员联系！</font>");
			e.printStackTrace();
			result = ERROR;
			throw new RuntimeException();
		}
		return result;
	}

	public String register() {
		String result = null;
		try {
			userBo = (IUserBo) getBean("userBo");
			purchaser = new Purchaser();
			purchaser.setPurName(purName);
			purchaser.setPurPassword(purPassword);
			purchaser.setPurTelephone(purTelephone);
			purchaser.setPurProvince(purProvince);
			purchaser.setPurCity(purCity);
			purchaser.setPurAddress(purAddress);
			purchaser.setPurPostalcode(purPostalcode);
			purchaser.setPurRemark(purRemark);
			purchaser.setPurIsvendot(purIsvendot);

			ProductGroup productgroup = new ProductGroup();

			vender = new Vender();
			vender.setProductgroup(productgroup);
			vender.setPurchaser(purchaser);
			vender.setVenShortname(venShortname);
			vender.setVenShopcard(venShopcard);
			vender.setVenFax(venFax);
			vender.setVenLinkman(venLinkman);
			vender.setVenLinkmanphone(venLinkmanphone);
			vender.setVenEmail(venEmail);
			vender.setVenStatus(-1);
			Object[] obj = { purchaser, productgroup, vender };
			userBo.register(obj);
			this.getRequest().getSession().setAttribute("purchaser", purchaser);
			System.out.println("---------------" + purchaser.getPurAddress());
			result = SUCCESS;
		} catch (Exception e) {
			this.setMessage("系统故障,请与管理员联系!");
			result = ERROR;
			e.printStackTrace();
			System.out.println("注册失败。。。已回滚。。。");
			throw new RuntimeException();

		}
		return result;
	}

	public String update() {
		String result = null;
		try {
			userBo = (IUserBo) getBean("userBo");
			Purchaser purchaser = userBo.findByName(purName);			
			purchaser.setPurName(purName);
			purchaser.setPurPassword(purPassword);
			purchaser.setPurTelephone(purTelephone);
			purchaser.setPurProvince(purProvince);
			purchaser.setPurCity(purCity);
			purchaser.setPurAddress(purAddress);
			purchaser.setPurPostalcode(purPostalcode);
			purchaser.setPurRemark(purRemark);

			ProductGroup productgroup = new ProductGroup();
			if ("yes".equals(purchaser.getPurIsvendot())) {
				purchaser.getVender().setProductgroup(productgroup);
				purchaser.getVender().setVenShortname(venShortname);
				purchaser.getVender().setVenShopcard(venShopcard);
				purchaser.getVender().setVenFax(venFax);
				purchaser.getVender().setVenLinkman(venLinkman);
				purchaser.getVender().setVenLinkmanphone(venLinkmanphone);
				purchaser.getVender().setVenEmail(venEmail);
				purchaser.getVender().setVenStatus(0);
			}
			Object[] objects = { purchaser, productgroup, vender };
			userBo.update(objects);
			this.getRequest().getSession().setAttribute("purchaser",
					userBo.findByName(purchaser.getPurName()));
			this.setMessage(purchaser.getPurName() + "修改个人信息成功!");
			result = SUCCESS;
		} catch (Exception e) {
			this.setMessage("系统故障,请与管理员联系!");
			result = ERROR;
			e.printStackTrace();
			System.out.println("更新失败。。已回滚。。。");
			throw new RuntimeException();
		}
		return result;
	}

	public  String purchasertoVender() {
		String result=null;
		try {
			userBo = (IUserBo) getBean("userBo");
			Purchaser purchaser = userBo.findByName(purName);
			purchaser.setPurIsvendot("yes");						
			purchaser.setPurName(purName);
			purchaser.setPurPassword(purPassword);
			purchaser.setPurTelephone(purTelephone);
			purchaser.setPurProvince(purProvince);
			purchaser.setPurCity(purCity);
			purchaser.setPurAddress(purAddress);
			purchaser.setPurPostalcode(purPostalcode);
			purchaser.setPurRemark(purRemark);

			ProductGroup productgroup = new ProductGroup();
			if ("yes".equals(purchaser.getPurIsvendot())) {
				purchaser.getVender().setProductgroup(productgroup);
				purchaser.getVender().setVenShortname(venShortname);
				purchaser.getVender().setVenShopcard(venShopcard);
				purchaser.getVender().setVenFax(venFax);
				purchaser.getVender().setVenLinkman(venLinkman);
				purchaser.getVender().setVenLinkmanphone(venLinkmanphone);
				purchaser.getVender().setVenEmail(venEmail);
				purchaser.getVender().setVenStatus(0);
			}
			Object[] objects = { purchaser, productgroup, vender };
			Purchaser pur=userBo.purchasertoVender(objects);
			request.setAttribute("purchaser", pur);
			result=SUCCESS;
		} catch (Exception e) {
			this.setMessage("系统故障,请与管理员联系!");
			result = ERROR;
			e.printStackTrace();
			System.out.println("更新失败。。已回滚。。。");
			throw new RuntimeException();
		}
		return result;

	}
	
	public String remove() {
		this.getRequest().getSession().removeAttribute("purchaser");
		return SUCCESS;
	}

	public String findByName() {
		String result = null;
		try {
			userBo = (IUserBo) getBean("userBo");
			purchaser = userBo.findByName(purName);
			if (purchaser != null) {
				this.getRequest().setAttribute("purchaser", purchaser);
				result = SUCCESS;
			} else {
				this.setMessage("<font color='red'>查找用户失败!<font>");
				result = ERROR;
			}
		} catch (Exception e) {
			this.setMessage("<font color='red'>系统出现故障，请与管理员联系！</font>");
			result = ERROR;
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}

	public String getAllPurchaser() {
		String result = null;
		try {
			userBo = (IUserBo) getBean("userBo");
			List<Purchaser> list = userBo.getAllPurchasers();
			if (list.size() > 0) {
				this.getRequest().getSession().setAttribute("purchasers", list);
				return SUCCESS;
			} else {
				this.setMessage("<font color='red'>查看用户失败!<font>");
				result = ERROR;
			}
		} catch (Exception e) {
			this.setMessage("<font color='red'>系统出现故障，请与管理员联系！</font>");
			result = ERROR;
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}

	public String ModifyPassword() {
		userBo = (IUserBo) getBean("userBo");
		String result = null;
		Purchaser purchaser = (Purchaser) this.getRequest().getSession()
				.getAttribute("purchaser");
		try {
			if (purchaser.getPurPassword().equals(purBeforePassword)) {
				this.setMessage("原密码输入有错,请重新输入!");
				return ERROR;
			}
			userBo.modifyPassword(purPassword, purId);
			this.getRequest().getSession().setAttribute("purchaser",
					userBo.findByName(purchaser.getPurName()));
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}

	public Integer getProductgroupId() {
		return productgroupId;
	}

	public void setProductgroupId(Integer productgroupId) {
		this.productgroupId = productgroupId;
	}

	public String getPurAddress() {
		return purAddress;
	}

	public void setPurAddress(String purAddress) {
		this.purAddress = purAddress;
	}

	public Integer getPurchaserId() {
		return purchaserId;
	}

	public void setPurchaserId(Integer purchaserId) {
		this.purchaserId = purchaserId;
	}

	public String getPurCity() {
		return purCity;
	}

	public void setPurCity(String purCity) {
		this.purCity = purCity;
	}

	public Integer getPurId() {
		return purId;
	}

	public void setPurId(Integer purId) {
		this.purId = purId;
	}

	public String getPurIsvendot() {
		return purIsvendot;
	}

	public void setPurIsvendot(String purIsvendot) {
		this.purIsvendot = purIsvendot;
	}

	public String getPurName() {
		return purName;
	}

	public void setPurName(String purName) {
		this.purName = purName;
	}

	public String getPurPassword() {
		return purPassword;
	}

	public void setPurPassword(String purPassword) {
		this.purPassword = purPassword;
	}

	public String getPurPostalcode() {
		return purPostalcode;
	}

	public void setPurPostalcode(String purPostalcode) {
		this.purPostalcode = purPostalcode;
	}

	public String getPurProvince() {
		return purProvince;
	}

	public void setPurProvince(String purProvince) {
		this.purProvince = purProvince;
	}

	public String getPurRemark() {
		return purRemark;
	}

	public void setPurRemark(String purRemark) {
		this.purRemark = purRemark;
	}

	public String getPurTelephone() {
		return purTelephone;
	}

	public void setPurTelephone(String purTelephone) {
		this.purTelephone = purTelephone;
	}

	public String getVenEmail() {
		return venEmail;
	}

	public void setVenEmail(String venEmail) {
		this.venEmail = venEmail;
	}

	public String getVenFax() {
		return venFax;
	}

	public void setVenFax(String venFax) {
		this.venFax = venFax;
	}

	public Integer getVenId() {
		return venId;
	}

	public void setVenId(Integer venId) {
		this.venId = venId;
	}

	public String getVenLinkman() {
		return venLinkman;
	}

	public void setVenLinkman(String venLinkman) {
		this.venLinkman = venLinkman;
	}

	public String getVenLinkmanphone() {
		return venLinkmanphone;
	}

	public void setVenLinkmanphone(String venLinkmanphone) {
		this.venLinkmanphone = venLinkmanphone;
	}

	public Integer getVenShopcard() {
		return venShopcard;
	}

	public void setVenShopcard(Integer venShopcard) {
		this.venShopcard = venShopcard;
	}

	public String getVenShortname() {
		return venShortname;
	}

	public void setVenShortname(String venShortname) {
		this.venShortname = venShortname;
	}

	public Integer getVenStatus() {
		return venStatus;
	}

	public void setVenStatus(Integer venStatus) {
		this.venStatus = venStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSession(Map arg0) {
		// TODO Auto-generated method stub

	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub

	}

	public Purchaser getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(Purchaser purchaser) {
		this.purchaser = purchaser;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Vender getVender() {
		return vender;
	}

	public void setVender(Vender vender) {
		this.vender = vender;
	}

}
