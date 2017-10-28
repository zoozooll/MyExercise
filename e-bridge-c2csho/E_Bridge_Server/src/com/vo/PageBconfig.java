package com.vo;

/**
 * PageBconfig entity. @author MyEclipse Persistence Tools
 */

public class PageBconfig implements java.io.Serializable {

	// Fields

	private Integer id;
	private String bodyfixed;
	private String skin;
	private String loginskin;
	private String yzmSkin;
	private String sitename;
	private String sitekeywords;
	private String sitedescription;
	private String siteurl;
	private String admAddress;
	private String admPost;
	private String admName;
	private String admMail;
	private String admTel;
	private String admQq;
	private String admQqName;
	private Integer qqonline;
	private Integer whereqq;
	private String kefuskin;
	private String qqskin;
	private String qqmsgOff;
	private String qqmsgOn;
	private String admMsn;
	private String admIcp;
	private String admComp;
	private String admFax;
	private String admMob;
	private String admKf;
	private Integer jsq;
	private Integer helpHang;
	private String usertype1;
	private Float kou1;
	private String usertype2;
	private Float kou2;
	private String usertype3;
	private Float kou3;
	private String usertype4;
	private Float kou4;
	private String usertype5;
	private Float kou5;
	private String usertype6;
	private Float kou6;
	private Integer promptNum;
	private Integer newprodNum;
	private Integer renmenNum;
	private Integer fenleiNum;
	private Integer mosi;
	private String picXiaogao;
	private String quehuo;
	private String wujiage;
	private String huiyuanjia;
	private String larColor;
	private String midColor;
	private String indexTishi;
	private Integer treeNum;
	private Integer treeView;
	private Integer treeDisplay;
	private Integer reg;
	private Integer bbs;
	private Integer menu;
	private String topmenu;
	private Integer newsmove;
	private String newsSkin;
	private String kfColor;
	private String pei1;
	private String pei2;
	private String pei3;
	private String pei4;
	private String pei5;
	private String pei6;
	private Integer fei1;
	private Integer fei2;
	private Integer fei3;
	private Integer fei4;
	private Integer fei5;
	private Integer fei6;
	private Integer mianyoufei;
	private String mianyoufeiMsg;
	private String newstitle1;
	private String newstitle2;
	private String newstitle3;
	private String newstitle4;
	private String newstitle5;
	private Integer kaiguan;
	private String guanbi;
	private String regXieyi;
	private String lockip;
	private String ip;
	private String other1;
	private String other2;
	private String flash1;

	// Constructors

	/** default constructor */
	public PageBconfig() {
	}

	/** full constructor */
	public PageBconfig(String bodyfixed, String skin, String loginskin,
			String yzmSkin, String sitename, String sitekeywords,
			String sitedescription, String siteurl, String admAddress,
			String admPost, String admName, String admMail, String admTel,
			String admQq, String admQqName, Integer qqonline, Integer whereqq,
			String kefuskin, String qqskin, String qqmsgOff, String qqmsgOn,
			String admMsn, String admIcp, String admComp, String admFax,
			String admMob, String admKf, Integer jsq, Integer helpHang,
			String usertype1, Float kou1, String usertype2, Float kou2,
			String usertype3, Float kou3, String usertype4, Float kou4,
			String usertype5, Float kou5, String usertype6, Float kou6,
			Integer promptNum, Integer newprodNum, Integer renmenNum,
			Integer fenleiNum, Integer mosi, String picXiaogao, String quehuo,
			String wujiage, String huiyuanjia, String larColor,
			String midColor, String indexTishi, Integer treeNum,
			Integer treeView, Integer treeDisplay, Integer reg, Integer bbs,
			Integer menu, String topmenu, Integer newsmove, String newsSkin,
			String kfColor, String pei1, String pei2, String pei3, String pei4,
			String pei5, String pei6, Integer fei1, Integer fei2, Integer fei3,
			Integer fei4, Integer fei5, Integer fei6, Integer mianyoufei,
			String mianyoufeiMsg, String newstitle1, String newstitle2,
			String newstitle3, String newstitle4, String newstitle5,
			Integer kaiguan, String guanbi, String regXieyi, String lockip,
			String ip, String other1, String other2, String flash1) {
		this.bodyfixed = bodyfixed;
		this.skin = skin;
		this.loginskin = loginskin;
		this.yzmSkin = yzmSkin;
		this.sitename = sitename;
		this.sitekeywords = sitekeywords;
		this.sitedescription = sitedescription;
		this.siteurl = siteurl;
		this.admAddress = admAddress;
		this.admPost = admPost;
		this.admName = admName;
		this.admMail = admMail;
		this.admTel = admTel;
		this.admQq = admQq;
		this.admQqName = admQqName;
		this.qqonline = qqonline;
		this.whereqq = whereqq;
		this.kefuskin = kefuskin;
		this.qqskin = qqskin;
		this.qqmsgOff = qqmsgOff;
		this.qqmsgOn = qqmsgOn;
		this.admMsn = admMsn;
		this.admIcp = admIcp;
		this.admComp = admComp;
		this.admFax = admFax;
		this.admMob = admMob;
		this.admKf = admKf;
		this.jsq = jsq;
		this.helpHang = helpHang;
		this.usertype1 = usertype1;
		this.kou1 = kou1;
		this.usertype2 = usertype2;
		this.kou2 = kou2;
		this.usertype3 = usertype3;
		this.kou3 = kou3;
		this.usertype4 = usertype4;
		this.kou4 = kou4;
		this.usertype5 = usertype5;
		this.kou5 = kou5;
		this.usertype6 = usertype6;
		this.kou6 = kou6;
		this.promptNum = promptNum;
		this.newprodNum = newprodNum;
		this.renmenNum = renmenNum;
		this.fenleiNum = fenleiNum;
		this.mosi = mosi;
		this.picXiaogao = picXiaogao;
		this.quehuo = quehuo;
		this.wujiage = wujiage;
		this.huiyuanjia = huiyuanjia;
		this.larColor = larColor;
		this.midColor = midColor;
		this.indexTishi = indexTishi;
		this.treeNum = treeNum;
		this.treeView = treeView;
		this.treeDisplay = treeDisplay;
		this.reg = reg;
		this.bbs = bbs;
		this.menu = menu;
		this.topmenu = topmenu;
		this.newsmove = newsmove;
		this.newsSkin = newsSkin;
		this.kfColor = kfColor;
		this.pei1 = pei1;
		this.pei2 = pei2;
		this.pei3 = pei3;
		this.pei4 = pei4;
		this.pei5 = pei5;
		this.pei6 = pei6;
		this.fei1 = fei1;
		this.fei2 = fei2;
		this.fei3 = fei3;
		this.fei4 = fei4;
		this.fei5 = fei5;
		this.fei6 = fei6;
		this.mianyoufei = mianyoufei;
		this.mianyoufeiMsg = mianyoufeiMsg;
		this.newstitle1 = newstitle1;
		this.newstitle2 = newstitle2;
		this.newstitle3 = newstitle3;
		this.newstitle4 = newstitle4;
		this.newstitle5 = newstitle5;
		this.kaiguan = kaiguan;
		this.guanbi = guanbi;
		this.regXieyi = regXieyi;
		this.lockip = lockip;
		this.ip = ip;
		this.other1 = other1;
		this.other2 = other2;
		this.flash1 = flash1;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBodyfixed() {
		return this.bodyfixed;
	}

	public void setBodyfixed(String bodyfixed) {
		this.bodyfixed = bodyfixed;
	}

	public String getSkin() {
		return this.skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getLoginskin() {
		return this.loginskin;
	}

	public void setLoginskin(String loginskin) {
		this.loginskin = loginskin;
	}

	public String getYzmSkin() {
		return this.yzmSkin;
	}

	public void setYzmSkin(String yzmSkin) {
		this.yzmSkin = yzmSkin;
	}

	public String getSitename() {
		return this.sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getSitekeywords() {
		return this.sitekeywords;
	}

	public void setSitekeywords(String sitekeywords) {
		this.sitekeywords = sitekeywords;
	}

	public String getSitedescription() {
		return this.sitedescription;
	}

	public void setSitedescription(String sitedescription) {
		this.sitedescription = sitedescription;
	}

	public String getSiteurl() {
		return this.siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public String getAdmAddress() {
		return this.admAddress;
	}

	public void setAdmAddress(String admAddress) {
		this.admAddress = admAddress;
	}

	public String getAdmPost() {
		return this.admPost;
	}

	public void setAdmPost(String admPost) {
		this.admPost = admPost;
	}

	public String getAdmName() {
		return this.admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getAdmMail() {
		return this.admMail;
	}

	public void setAdmMail(String admMail) {
		this.admMail = admMail;
	}

	public String getAdmTel() {
		return this.admTel;
	}

	public void setAdmTel(String admTel) {
		this.admTel = admTel;
	}

	public String getAdmQq() {
		return this.admQq;
	}

	public void setAdmQq(String admQq) {
		this.admQq = admQq;
	}

	public String getAdmQqName() {
		return this.admQqName;
	}

	public void setAdmQqName(String admQqName) {
		this.admQqName = admQqName;
	}

	public Integer getQqonline() {
		return this.qqonline;
	}

	public void setQqonline(Integer qqonline) {
		this.qqonline = qqonline;
	}

	public Integer getWhereqq() {
		return this.whereqq;
	}

	public void setWhereqq(Integer whereqq) {
		this.whereqq = whereqq;
	}

	public String getKefuskin() {
		return this.kefuskin;
	}

	public void setKefuskin(String kefuskin) {
		this.kefuskin = kefuskin;
	}

	public String getQqskin() {
		return this.qqskin;
	}

	public void setQqskin(String qqskin) {
		this.qqskin = qqskin;
	}

	public String getQqmsgOff() {
		return this.qqmsgOff;
	}

	public void setQqmsgOff(String qqmsgOff) {
		this.qqmsgOff = qqmsgOff;
	}

	public String getQqmsgOn() {
		return this.qqmsgOn;
	}

	public void setQqmsgOn(String qqmsgOn) {
		this.qqmsgOn = qqmsgOn;
	}

	public String getAdmMsn() {
		return this.admMsn;
	}

	public void setAdmMsn(String admMsn) {
		this.admMsn = admMsn;
	}

	public String getAdmIcp() {
		return this.admIcp;
	}

	public void setAdmIcp(String admIcp) {
		this.admIcp = admIcp;
	}

	public String getAdmComp() {
		return this.admComp;
	}

	public void setAdmComp(String admComp) {
		this.admComp = admComp;
	}

	public String getAdmFax() {
		return this.admFax;
	}

	public void setAdmFax(String admFax) {
		this.admFax = admFax;
	}

	public String getAdmMob() {
		return this.admMob;
	}

	public void setAdmMob(String admMob) {
		this.admMob = admMob;
	}

	public String getAdmKf() {
		return this.admKf;
	}

	public void setAdmKf(String admKf) {
		this.admKf = admKf;
	}

	public Integer getJsq() {
		return this.jsq;
	}

	public void setJsq(Integer jsq) {
		this.jsq = jsq;
	}

	public Integer getHelpHang() {
		return this.helpHang;
	}

	public void setHelpHang(Integer helpHang) {
		this.helpHang = helpHang;
	}

	public String getUsertype1() {
		return this.usertype1;
	}

	public void setUsertype1(String usertype1) {
		this.usertype1 = usertype1;
	}

	public Float getKou1() {
		return this.kou1;
	}

	public void setKou1(Float kou1) {
		this.kou1 = kou1;
	}

	public String getUsertype2() {
		return this.usertype2;
	}

	public void setUsertype2(String usertype2) {
		this.usertype2 = usertype2;
	}

	public Float getKou2() {
		return this.kou2;
	}

	public void setKou2(Float kou2) {
		this.kou2 = kou2;
	}

	public String getUsertype3() {
		return this.usertype3;
	}

	public void setUsertype3(String usertype3) {
		this.usertype3 = usertype3;
	}

	public Float getKou3() {
		return this.kou3;
	}

	public void setKou3(Float kou3) {
		this.kou3 = kou3;
	}

	public String getUsertype4() {
		return this.usertype4;
	}

	public void setUsertype4(String usertype4) {
		this.usertype4 = usertype4;
	}

	public Float getKou4() {
		return this.kou4;
	}

	public void setKou4(Float kou4) {
		this.kou4 = kou4;
	}

	public String getUsertype5() {
		return this.usertype5;
	}

	public void setUsertype5(String usertype5) {
		this.usertype5 = usertype5;
	}

	public Float getKou5() {
		return this.kou5;
	}

	public void setKou5(Float kou5) {
		this.kou5 = kou5;
	}

	public String getUsertype6() {
		return this.usertype6;
	}

	public void setUsertype6(String usertype6) {
		this.usertype6 = usertype6;
	}

	public Float getKou6() {
		return this.kou6;
	}

	public void setKou6(Float kou6) {
		this.kou6 = kou6;
	}

	public Integer getPromptNum() {
		return this.promptNum;
	}

	public void setPromptNum(Integer promptNum) {
		this.promptNum = promptNum;
	}

	public Integer getNewprodNum() {
		return this.newprodNum;
	}

	public void setNewprodNum(Integer newprodNum) {
		this.newprodNum = newprodNum;
	}

	public Integer getRenmenNum() {
		return this.renmenNum;
	}

	public void setRenmenNum(Integer renmenNum) {
		this.renmenNum = renmenNum;
	}

	public Integer getFenleiNum() {
		return this.fenleiNum;
	}

	public void setFenleiNum(Integer fenleiNum) {
		this.fenleiNum = fenleiNum;
	}

	public Integer getMosi() {
		return this.mosi;
	}

	public void setMosi(Integer mosi) {
		this.mosi = mosi;
	}

	public String getPicXiaogao() {
		return this.picXiaogao;
	}

	public void setPicXiaogao(String picXiaogao) {
		this.picXiaogao = picXiaogao;
	}

	public String getQuehuo() {
		return this.quehuo;
	}

	public void setQuehuo(String quehuo) {
		this.quehuo = quehuo;
	}

	public String getWujiage() {
		return this.wujiage;
	}

	public void setWujiage(String wujiage) {
		this.wujiage = wujiage;
	}

	public String getHuiyuanjia() {
		return this.huiyuanjia;
	}

	public void setHuiyuanjia(String huiyuanjia) {
		this.huiyuanjia = huiyuanjia;
	}

	public String getLarColor() {
		return this.larColor;
	}

	public void setLarColor(String larColor) {
		this.larColor = larColor;
	}

	public String getMidColor() {
		return this.midColor;
	}

	public void setMidColor(String midColor) {
		this.midColor = midColor;
	}

	public String getIndexTishi() {
		return this.indexTishi;
	}

	public void setIndexTishi(String indexTishi) {
		this.indexTishi = indexTishi;
	}

	public Integer getTreeNum() {
		return this.treeNum;
	}

	public void setTreeNum(Integer treeNum) {
		this.treeNum = treeNum;
	}

	public Integer getTreeView() {
		return this.treeView;
	}

	public void setTreeView(Integer treeView) {
		this.treeView = treeView;
	}

	public Integer getTreeDisplay() {
		return this.treeDisplay;
	}

	public void setTreeDisplay(Integer treeDisplay) {
		this.treeDisplay = treeDisplay;
	}

	public Integer getReg() {
		return this.reg;
	}

	public void setReg(Integer reg) {
		this.reg = reg;
	}

	public Integer getBbs() {
		return this.bbs;
	}

	public void setBbs(Integer bbs) {
		this.bbs = bbs;
	}

	public Integer getMenu() {
		return this.menu;
	}

	public void setMenu(Integer menu) {
		this.menu = menu;
	}

	public String getTopmenu() {
		return this.topmenu;
	}

	public void setTopmenu(String topmenu) {
		this.topmenu = topmenu;
	}

	public Integer getNewsmove() {
		return this.newsmove;
	}

	public void setNewsmove(Integer newsmove) {
		this.newsmove = newsmove;
	}

	public String getNewsSkin() {
		return this.newsSkin;
	}

	public void setNewsSkin(String newsSkin) {
		this.newsSkin = newsSkin;
	}

	public String getKfColor() {
		return this.kfColor;
	}

	public void setKfColor(String kfColor) {
		this.kfColor = kfColor;
	}

	public String getPei1() {
		return this.pei1;
	}

	public void setPei1(String pei1) {
		this.pei1 = pei1;
	}

	public String getPei2() {
		return this.pei2;
	}

	public void setPei2(String pei2) {
		this.pei2 = pei2;
	}

	public String getPei3() {
		return this.pei3;
	}

	public void setPei3(String pei3) {
		this.pei3 = pei3;
	}

	public String getPei4() {
		return this.pei4;
	}

	public void setPei4(String pei4) {
		this.pei4 = pei4;
	}

	public String getPei5() {
		return this.pei5;
	}

	public void setPei5(String pei5) {
		this.pei5 = pei5;
	}

	public String getPei6() {
		return this.pei6;
	}

	public void setPei6(String pei6) {
		this.pei6 = pei6;
	}

	public Integer getFei1() {
		return this.fei1;
	}

	public void setFei1(Integer fei1) {
		this.fei1 = fei1;
	}

	public Integer getFei2() {
		return this.fei2;
	}

	public void setFei2(Integer fei2) {
		this.fei2 = fei2;
	}

	public Integer getFei3() {
		return this.fei3;
	}

	public void setFei3(Integer fei3) {
		this.fei3 = fei3;
	}

	public Integer getFei4() {
		return this.fei4;
	}

	public void setFei4(Integer fei4) {
		this.fei4 = fei4;
	}

	public Integer getFei5() {
		return this.fei5;
	}

	public void setFei5(Integer fei5) {
		this.fei5 = fei5;
	}

	public Integer getFei6() {
		return this.fei6;
	}

	public void setFei6(Integer fei6) {
		this.fei6 = fei6;
	}

	public Integer getMianyoufei() {
		return this.mianyoufei;
	}

	public void setMianyoufei(Integer mianyoufei) {
		this.mianyoufei = mianyoufei;
	}

	public String getMianyoufeiMsg() {
		return this.mianyoufeiMsg;
	}

	public void setMianyoufeiMsg(String mianyoufeiMsg) {
		this.mianyoufeiMsg = mianyoufeiMsg;
	}

	public String getNewstitle1() {
		return this.newstitle1;
	}

	public void setNewstitle1(String newstitle1) {
		this.newstitle1 = newstitle1;
	}

	public String getNewstitle2() {
		return this.newstitle2;
	}

	public void setNewstitle2(String newstitle2) {
		this.newstitle2 = newstitle2;
	}

	public String getNewstitle3() {
		return this.newstitle3;
	}

	public void setNewstitle3(String newstitle3) {
		this.newstitle3 = newstitle3;
	}

	public String getNewstitle4() {
		return this.newstitle4;
	}

	public void setNewstitle4(String newstitle4) {
		this.newstitle4 = newstitle4;
	}

	public String getNewstitle5() {
		return this.newstitle5;
	}

	public void setNewstitle5(String newstitle5) {
		this.newstitle5 = newstitle5;
	}

	public Integer getKaiguan() {
		return this.kaiguan;
	}

	public void setKaiguan(Integer kaiguan) {
		this.kaiguan = kaiguan;
	}

	public String getGuanbi() {
		return this.guanbi;
	}

	public void setGuanbi(String guanbi) {
		this.guanbi = guanbi;
	}

	public String getRegXieyi() {
		return this.regXieyi;
	}

	public void setRegXieyi(String regXieyi) {
		this.regXieyi = regXieyi;
	}

	public String getLockip() {
		return this.lockip;
	}

	public void setLockip(String lockip) {
		this.lockip = lockip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOther1() {
		return this.other1;
	}

	public void setOther1(String other1) {
		this.other1 = other1;
	}

	public String getOther2() {
		return this.other2;
	}

	public void setOther2(String other2) {
		this.other2 = other2;
	}

	public String getFlash1() {
		return this.flash1;
	}

	public void setFlash1(String flash1) {
		this.flash1 = flash1;
	}

}