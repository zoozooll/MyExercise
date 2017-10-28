package com.action;

import java.util.ArrayList;
import java.util.List;

import com.bo.IAdminBo;
import com.bo.IUserBo;
import com.vo.Admin;
import com.vo.ProductGroup;
import com.vo.Purchaser;
import com.vo.Vender;

public class AdminAction extends BasicAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8664392204253387157L;

	private String adminName;

	private String adminPassword;
	
	private String adminEmail;
	
	private String adminRemark;
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminRemark() {
		return adminRemark;
	}

	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}

	public String getAdminAddress() {
		return adminAddress;
	}

	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}

	public String getAdminIdcard() {
		return adminIdcard;
	}

	public void setAdminIdcard(String adminIdcard) {
		this.adminIdcard = adminIdcard;
	}

	public String getAdminPhone() {
		return adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	private String adminAddress;
	
	private String adminIdcard;
	
	private String adminPhone;

	private String adminBeforePassword;

	public String getAdminBeforePassword() {
		return adminBeforePassword;
	}

	public void setAdminBeforePassword(String adminBeforePassword) {
		this.adminBeforePassword = adminBeforePassword;
	}

	private String password;

	private Integer adminId;

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

	private IAdminBo adminBo;

	private IUserBo userBo;

	private String delcondition;

	private String typeName;

	public String getDelcondition() {
		return delcondition;
	}

	public void setDelcondition(String delcondition) {
		this.delcondition = delcondition;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public IUserBo getUserBo() {
		return userBo;
	}

	public void setUserBo(IUserBo userBo) {
		this.userBo = userBo;
	}

	public IAdminBo getAdminBo() {
		return adminBo;
	}

	public void setAdminBo(IAdminBo adminBo) {
		this.adminBo = adminBo;
	}

	public String adminLogin() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			String strings[] = { adminName, adminPassword };
			System.out.println("---------------" + adminName + adminPassword);
			Admin admin = adminBo.adminLogin(strings);
			getRequest().getSession().setAttribute("admin", admin);
			if (admin == null) {
				result = ERROR;
				this.setMessage("用户名或密码不正确,请重新输入!");
			} else {
				result = SUCCESS;
			}
		} catch (Exception e) {
			result = ERROR;
			e.printStackTrace();
			System.out.println("登录失败。。。");
			throw new RuntimeException();
		}
		return result;
	}

	public String addUser() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			Purchaser purchaser = new Purchaser();
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

			Vender vender = new Vender();
			vender.setProductgroup(productgroup);
			vender.setPurchaser(purchaser);
			vender.setVenShortname(venShortname);
			vender.setVenShopcard(venShopcard);
			vender.setVenFax(venFax);
			vender.setVenLinkman(venLinkman);
			vender.setVenLinkmanphone(venLinkmanphone);
			vender.setVenEmail(venEmail);
			vender.setVenStatus(-1);
			Object[] objects = { purchaser, productgroup, vender };
			adminBo.addUser(objects);
			queryUser();
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

	public String deleteUser() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			adminBo.deleteUser(purId);
			queryUser();
			result = SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = ERROR;
			System.out.println("删除失败。。。已回滚。。。");
			throw new RuntimeException();
		}
		return result;
	}

	public String modifyUser() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			Purchaser purchaser = findUserById(adminBo);
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
			adminBo.modifyUser(purchaser);
			queryUser();
			result = SUCCESS;
		} catch (Exception e) {
			this.setMessage("系统故障,请与管理员联系!");
			result = ERROR;
			e.printStackTrace();
			System.out.println("更新失败。。。已回滚。。。");
			throw new RuntimeException();
		}
		return result;
	}

	private Purchaser findUserById(IAdminBo adminBo) {
		Purchaser purchaser = null;
		try {
			purchaser = adminBo.findUserById(purId);
			System.out.println("11---------"
					+ purchaser.getVender().getVenEmail()
					+ purchaser.getVender().getVenShortname());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaser;
	}

	public String findUserByName() {
		String result = null;
		Purchaser purchaser = null;
		try {
			userBo = (IUserBo) getBean("userBo");
			purchaser = userBo.findByName(purName);
			System.out.println("22---------");
			this.getRequest().getSession().setAttribute("purchaser", purchaser);
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		}
		return result;
	}

	public String deleteUserByBlur() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			if ("purId".equals(typeName)) {
				adminBo.deleteUser(Integer.parseInt(delcondition));
			} else if ("purName".equals(typeName)) {
				adminBo.deleteUserByName(delcondition);
			}
			result = SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.setMessage("您输入的不是数字ID!");
			result = ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage("删除用户失败!");
			result = ERROR;
		}
		return result;
	}

	public String QueryUserBlur() {
		String result = null;
		List<Purchaser> list = new ArrayList<Purchaser>();
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			if ("purId".equals(typeName)) {
				list.add(adminBo.findUserById(Integer.parseInt(condition)));
			} else if ("purName".equals(typeName)) {
				list = adminBo.findUserByName(condition);
			} else if ("purNameBlur".equals(typeName)) {
				list = adminBo.findUserByNameBlur(condition);
			}
			this.getRequest().getSession().setAttribute("purchasers", list);
			result = SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.setMessage("您输入的不是数字ID!");
			result = ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage("删除用户失败!");
			result = ERROR;
		}
		return result;
	}

	public String findVenById() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			Purchaser purchaser = findUserById(adminBo);
			System.out.println("22---------");
			this.getRequest().getSession().setAttribute("purchaser", purchaser);
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		}
		return result;
	}

	public String findAdminById() {
		Admin admin = null;
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			admin = (Admin) (getRequest().getSession().getAttribute("admin"));
			adminId = admin.getAdminId();
			admin = adminBo.findAdminById(adminId);
			queryUser();
			this.getRequest().getSession().setAttribute("admin", admin);
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		}
		return result;
	}

	public String modifyUserStatus() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			adminBo.modifyStatus(venStatus, venId);
			queryUser();
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		}
		return result;
	}

	public String modifyPassword() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			Admin admin = (Admin) (getRequest().getSession()
					.getAttribute("admin"));
			if (!adminBeforePassword.equals(admin.getAdminPassword())) {
				this.setMessage("原密码输入错误,请重新输入!");
				result = ERROR;
				return result;
			}
			adminId = admin.getAdminId();
			adminBo.modifyPassword(adminPassword, adminId);
			findAdminById();
			queryUser();
			result = SUCCESS;
		} catch (Exception e) {
			result = ERROR;
			e.printStackTrace();
		}
		return result;
	}

	public String queryUser() {
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			List<Purchaser> list = adminBo.queryUser();
			this.getRequest().getSession().setAttribute("purchasers", list);
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
			throw new RuntimeException();

		}
		return result;
	}
	
	public String modifyAdmin(){
		String result = null;
		try {
			adminBo = (IAdminBo) getBean("adminBo");
			Integer adminId=Integer.parseInt(this.getRequest().getParameter("adminId"));
			Admin admin=adminBo.findAdminById(adminId);
			admin.setAdminAddress(adminAddress);
			admin.setAdminEmail(adminEmail);
			admin.setAdminIdcard(adminIdcard);
			admin.setAdminName(adminName);
			admin.setAdminPassword(adminPassword);
			admin.setAdminPhone(adminPhone);
			admin.setAdminRemark(adminRemark);			
			Admin admin1 = adminBo.modifyAdmin(admin);
			this.getRequest().getSession().setAttribute("admin", admin1);
			result = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
			throw new RuntimeException();

		}
		return result;
		
	}

	public Integer getPurId() {
		return purId;
	}

	public void setPurId(Integer purId) {
		this.purId = purId;
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

	public String getPurTelephone() {
		return purTelephone;
	}

	public void setPurTelephone(String purTelephone) {
		this.purTelephone = purTelephone;
	}

	public String getPurProvince() {
		return purProvince;
	}

	public void setPurProvince(String purProvince) {
		this.purProvince = purProvince;
	}

	public String getPurCity() {
		return purCity;
	}

	public void setPurCity(String purCity) {
		this.purCity = purCity;
	}

	public String getPurAddress() {
		return purAddress;
	}

	public void setPurAddress(String purAddress) {
		this.purAddress = purAddress;
	}

	public String getPurPostalcode() {
		return purPostalcode;
	}

	public void setPurPostalcode(String purPostalcode) {
		this.purPostalcode = purPostalcode;
	}

	public String getPurRemark() {
		return purRemark;
	}

	public void setPurRemark(String purRemark) {
		this.purRemark = purRemark;
	}

	public String getPurIsvendot() {
		return purIsvendot;
	}

	public void setPurIsvendot(String purIsvendot) {
		this.purIsvendot = purIsvendot;
	}

	public Integer getVenId() {
		return venId;
	}

	public void setVenId(Integer venId) {
		this.venId = venId;
	}

	public Integer getPurchaserId() {
		return purchaserId;
	}

	public void setPurchaserId(Integer purchaserId) {
		this.purchaserId = purchaserId;
	}

	public Integer getProductgroupId() {
		return productgroupId;
	}

	public void setProductgroupId(Integer productgroupId) {
		this.productgroupId = productgroupId;
	}

	public String getVenShortname() {
		return venShortname;
	}

	public void setVenShortname(String venShortname) {
		this.venShortname = venShortname;
	}

	public Integer getVenShopcard() {
		return venShopcard;
	}

	public void setVenShopcard(Integer venShopcard) {
		this.venShopcard = venShopcard;
	}

	public String getVenFax() {
		return venFax;
	}

	public void setVenFax(String venFax) {
		this.venFax = venFax;
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

	public String getVenEmail() {
		return venEmail;
	}

	public void setVenEmail(String venEmail) {
		this.venEmail = venEmail;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	private String condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

}
