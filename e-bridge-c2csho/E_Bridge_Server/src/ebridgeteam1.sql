/*

MySQL Data Transfer

Source Host: localhost

Source Database: ebridge

Target Host: localhost

Target Database: ebridge

Date: 2009/4/24 17:42:21

*/



SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------

-- Table structure for admin1

-- ----------------------------

DROP TABLE IF EXISTS `admin1`;

CREATE TABLE `admin1` (
  `admin_id` int(11) NOT NULL auto_increment COMMENT '管理员ID',
  `admin_name` varchar(20) default NULL COMMENT '管理员姓名',
  `admin_password` varchar(40) default NULL COMMENT '管理员密码',
  `admin_address` varchar(100) default NULL COMMENT '管理员地址',
  `admin_idcard` varchar(60) default NULL COMMENT '管理员身份证号',
  `admin_phone` varchar(20) default NULL COMMENT '管理员电话',
  `admin_email` varchar(40) default NULL COMMENT '管理员邮箱',
  `admin_remark` varchar(200) default NULL COMMENT '管理员备注',
  `prductgroup_id` int(11) default NULL,
  `channel_id` int(11) default NULL,
  `organization_id` int(11) default NULL,
  PRIMARY KEY  (`admin_id`),
  KEY `fk_pro_pid` (`prductgroup_id`),
  KEY `fk_or_oid` (`organization_id`),
  KEY `fk_cha_cid` (`channel_id`),
  CONSTRAINT `fk_cha_cid` FOREIGN KEY (`channel_id`) REFERENCES `channel1` (`cha_id`),
  CONSTRAINT `fk_or_oid` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
  CONSTRAINT `fk_pro_pid` FOREIGN KEY (`prductgroup_id`) REFERENCES `productgroup1` (`prog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='管理员表';



-- ----------------------------

-- Table structure for bconfig

-- ----------------------------

DROP TABLE IF EXISTS `bconfig`;

CREATE TABLE `bconfig` (
  `id` int(11) NOT NULL auto_increment,
  `bodyfixed` varchar(255) default '0' COMMENT '背景模式',
  `skin` varchar(255) default '1' COMMENT '模板选择',
  `loginskin` varchar(255) default '1' COMMENT '登陆框样式',
  `yzm_skin` varchar(255) default '1' COMMENT '验证码样式',
  `sitename` varchar(255) default '"网站名称"' COMMENT '站点资料',
  `sitekeywords` varchar(1000) default '"sfkong,gjun,广州,长沙,网店系统,购物系统,网络商城,网店代码,商城源码,电子商城,在线购物,在线支付,网站建设"' COMMENT '站点关键字',
  `sitedescription` varchar(255) default '' COMMENT '站点描述',
  `siteurl` varchar(50) default NULL COMMENT '域名',
  `adm_address` varchar(200) default NULL COMMENT '联系地址',
  `adm_post` varchar(50) default NULL COMMENT '邮编',
  `adm_name` varchar(50) default NULL COMMENT '联系人',
  `adm_mail` varchar(50) default NULL COMMENT '邮箱',
  `adm_tel` varchar(50) default NULL COMMENT '电话',
  `adm_qq` varchar(50) default NULL COMMENT 'QQ',
  `adm_qq_name` varchar(50) default NULL COMMENT '昵称',
  `qqonline` int(11) default '1' COMMENT '是否显示',
  `whereqq` int(11) default '1' COMMENT '左侧还是右侧',
  `kefuskin` varchar(50) default '1' COMMENT '样式',
  `qqskin` varchar(50) default '1' COMMENT '头像',
  `qqmsg_off` varchar(255) default '"客服不在线，请留言"' COMMENT '离线提示',
  `qqmsg_on` varchar(255) default '"客服在线，点击交谈"' COMMENT '在线提示',
  `adm_msn` varchar(50) default NULL COMMENT 'MSN',
  `adm_icp` varchar(50) default NULL COMMENT 'ICP',
  `adm_comp` varchar(50) default NULL COMMENT '公司名称',
  `adm_fax` varchar(50) default NULL COMMENT '传真',
  `adm_mob` varchar(50) default NULL COMMENT '移动电话',
  `adm_kf` varchar(255) default NULL COMMENT '联系方式',
  `jsq` int(11) default NULL,
  `help_hang` int(11) default '3' COMMENT '帮助表格的行数',
  `usertype1` varchar(50) default '"普通会员"' COMMENT '用户等级',
  `kou1` float default '10' COMMENT '折扣',
  `usertype2` varchar(50) default NULL,
  `kou2` float default '10',
  `usertype3` varchar(50) default NULL,
  `kou3` float default '10',
  `usertype4` varchar(50) default NULL,
  `kou4` float default '10',
  `usertype5` varchar(50) default NULL,
  `kou5` float default '10',
  `usertype6` varchar(50) default NULL,
  `kou6` float default '10',
  `prompt_num` int(11) default '8' COMMENT '新商品数量',
  `newprod_num` int(11) default '8' COMMENT '推荐商品数量',
  `renmen_num` int(11) default '10' COMMENT '热门商品数量',
  `fenlei_num` int(11) default '10' COMMENT '内页商品数量',
  `mosi` int(11) default '1' COMMENT '首页商品模式',
  `pic_xiaogao` varchar(1) default '1' COMMENT '图片边框',
  `quehuo` varchar(50) default '"货品准备中"' COMMENT '缺货提示',
  `wujiage` varchar(50) default '"价格请咨询"' COMMENT '无价格提示',
  `huiyuanjia` varchar(50) default '"请先登陆"' COMMENT '员会价提示',
  `lar_color` varchar(10) default NULL COMMENT '一级分类颜色',
  `mid_color` varchar(10) default NULL COMMENT '二级分类颜色',
  `index_tishi` varchar(50) default '0' COMMENT '鼠标指向首页图片时的提示',
  `tree_num` int(11) default '0' COMMENT '是否显示分类下的商品总数',
  `tree_view` int(11) default '1' COMMENT '是否隐藏分类',
  `tree_display` int(11) default '0' COMMENT '展开还是收起',
  `reg` int(11) default '1' COMMENT '是否强制注册',
  `bbs` int(11) default '0' COMMENT '导航栏显示BBS还是留言板',
  `menu` int(11) default '1' COMMENT '主页右键菜单',
  `topmenu` varchar(255) default NULL COMMENT '页顶导航工具条内容',
  `newsmove` int(11) default '1' COMMENT '新闻是否滚动',
  `news_skin` varchar(1) default '1' COMMENT '新闻皮肤',
  `kf_color` varchar(10) default '"red"' COMMENT '客服在线的颜色',
  `pei1` varchar(50) default NULL COMMENT '送配方式1',
  `pei2` varchar(50) default NULL COMMENT '送配方式2',
  `pei3` varchar(50) default NULL COMMENT '送配方式3',
  `pei4` varchar(50) default NULL COMMENT '送配方式4',
  `pei5` varchar(50) default NULL COMMENT '送配方式6',
  `pei6` varchar(50) default '0' COMMENT '送配方式6',
  `fei1` int(11) default '0' COMMENT '运费1',
  `fei2` int(11) default '0' COMMENT '运费2',
  `fei3` int(11) default '0' COMMENT '运费3',
  `fei4` int(11) default '0' COMMENT '运费4',
  `fei5` int(11) default '0' COMMENT '运费5',
  `fei6` int(11) default '0' COMMENT '运费6',
  `mianyoufei` int(11) default '1000' COMMENT '免邮费设置',
  `mianyoufei_msg` varchar(255) default NULL COMMENT '免邮费设置',
  `newstitle1` varchar(255) default NULL COMMENT '新闻分类名称1',
  `newstitle2` varchar(255) default NULL COMMENT '新闻分类名称2',
  `newstitle3` varchar(255) default NULL COMMENT '新闻分类名称3',
  `newstitle4` varchar(255) default NULL COMMENT '新闻分类名称4',
  `newstitle5` varchar(255) default NULL COMMENT '新闻分类名称5',
  `kaiguan` int(11) default '1' COMMENT '网站开关',
  `guanbi` varchar(255) default NULL COMMENT '网站关闭时的提示',
  `reg_xieyi` varchar(1000) default NULL COMMENT '注册协议',
  `lockip` varchar(1) default NULL COMMENT '是否启用锁定IP功能',
  `ip` varchar(100) default NULL COMMENT '锁定IP的列表',
  `other1` varchar(100) default NULL COMMENT '预留',
  `other2` varchar(50) default NULL COMMENT '预留',
  `flash1` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;



-- ----------------------------

-- Table structure for brand1

-- ----------------------------

DROP TABLE IF EXISTS `brand1`;

CREATE TABLE `brand1` (
  `brand_id` int(11) NOT NULL auto_increment COMMENT '品牌ID',
  `brand_name` varchar(20) default NULL COMMENT '品牌名',
  `brand_desc` varchar(300) default NULL COMMENT '品牌描述',
  `brand_image_path` varchar(200) default NULL COMMENT '牌品图片路径',
  PRIMARY KEY  (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='品牌';



-- ----------------------------

-- Table structure for channel1

-- ----------------------------

DROP TABLE IF EXISTS `channel1`;

CREATE TABLE `channel1` (
  `cha_id` int(11) NOT NULL auto_increment,
  `channelcode` varchar(20) default NULL COMMENT '道渠代码',
  `channelname` varchar(40) default NULL COMMENT '渠道名称',
  PRIMARY KEY  (`cha_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;



-- ----------------------------

-- Table structure for city

-- ----------------------------

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `id` int(11) NOT NULL auto_increment,
  `organization_id` int(11) default NULL,
  `province` varchar(40) default NULL,
  `cityName` varchar(40) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_city_orid` (`organization_id`),
  CONSTRAINT `fk_city_orid` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;



-- ----------------------------

-- Table structure for deliverybill1

-- ----------------------------

DROP TABLE IF EXISTS `deliverybill1`;

CREATE TABLE `deliverybill1` (
  `deli_id` int(11) NOT NULL auto_increment COMMENT '交货单ID',
  `order_id` int(11) default NULL COMMENT '订单ID',
  `delivery_code` varchar(20) default NULL COMMENT '交货单编号',
  `amount` decimal(8,0) default NULL COMMENT '总数量',
  `clientname` varchar(100) default NULL COMMENT '客户名称',
  `clientcode` varchar(20) default NULL COMMENT '客户编号',
  `invoiceno` varchar(20) default NULL COMMENT '发票编号',
  `province` varchar(10) default NULL COMMENT '到货省份',
  `city` varchar(10) default NULL COMMENT '到货城市',
  `address` varchar(100) default NULL COMMENT '收货地址',
  `contactor` varchar(10) default NULL COMMENT '联系人',
  `sendtype` varchar(10) default NULL COMMENT '发送方式',
  `carryplace` varchar(100) default NULL COMMENT '运送地点',
  `specialnote` mediumtext COMMENT '特殊说明',
  `signstandard` varchar(100) default NULL COMMENT '签收标准',
  `sendcarnote` mediumtext COMMENT '承运商派车备注',
  `signnote` mediumtext COMMENT '签收备注',
  `pickupman` varchar(10) default NULL COMMENT '提货人',
  `contacttype` varchar(20) default NULL COMMENT '联系方式',
  `contactphone` varchar(20) default NULL COMMENT '送达方联系人电话',
  PRIMARY KEY  (`deli_id`),
  KEY `fk_ref_order` (`order_id`),
  CONSTRAINT `fk_ref_order` FOREIGN KEY (`order_id`) REFERENCES `order1` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='交货单; ; InnoDB free: 9216 kB';



-- ----------------------------

-- Table structure for image1

-- ----------------------------

DROP TABLE IF EXISTS `image1`;

CREATE TABLE `image1` (
  `img_id` int(11) NOT NULL auto_increment COMMENT '图片id',
  `product_id` int(11) default NULL COMMENT '产品id',
  `img_name` varchar(20) default NULL COMMENT '图片名称',
  `img_path` varchar(100) default NULL COMMENT '图片路径',
  `img_memo` varchar(1000) default NULL COMMENT '图片说明',
  PRIMARY KEY  (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='图片';



-- ----------------------------

-- Table structure for order1

-- ----------------------------

DROP TABLE IF EXISTS `order1`;

CREATE TABLE `order1` (
  `order_id` int(11) NOT NULL auto_increment COMMENT '订单id',
  `order_code` int(20) default '1000' COMMENT '订单编号',
  `order_name` varchar(20) default NULL COMMENT '订单名称',
  `order_source` varchar(20) default NULL COMMENT '订单来源',
  `payway` varchar(20) default NULL COMMENT '付款方式',
  `paylater` varchar(40) default NULL COMMENT '帐期',
  `invoicetype` varchar(20) default NULL COMMENT '发票类型:1 增值税专用发票、2 货物销售统一发票、3 货物运输业专用发票 ',
  `invoicehead` varchar(100) default NULL COMMENT '发票抬头:发票抬头是指收取发票的公司名称或个人姓名。',
  `sendto` varchar(100) default NULL COMMENT '送达方',
  `arrivetime` varchar(100) default NULL COMMENT '要求到贷时间',
  `doselfcode` varchar(60) default NULL COMMENT '客户自主编号',
  `ordermemo` mediumtext COMMENT '订单备注',
  `seensms` varchar(10) default NULL COMMENT '是否需要短信通知',
  `mobile1` varchar(15) default NULL COMMENT '手机号码1',
  `mobile2` varchar(15) default NULL COMMENT '手机号码2',
  `createddate` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='订单; ; InnoDB free: 9216 kB';



-- ----------------------------

-- Table structure for orderline1

-- ----------------------------

DROP TABLE IF EXISTS `orderline1`;

CREATE TABLE `orderline1` (
  `line_id` int(11) NOT NULL auto_increment COMMENT '订单明细id',
  `order_id` int(11) default NULL COMMENT '订单id',
  `pro_id` int(11) default NULL COMMENT '产品id',
  `amount` int(11) default NULL COMMENT '数量',
  `vender_name` varchar(60) default NULL COMMENT '卖家公司名称',
  `vender_code` varchar(20) default NULL COMMENT '卖家编号',
  `storeaddres` varchar(40) default NULL COMMENT '库存地',
  `summoney` double(15,2) default NULL COMMENT '总金额',
  `orderline_state` varchar(15) default '待审核' COMMENT '订单状态: 0: 待审核；1:审核通过(发货)；2:待收货回执；3:已回执',
  PRIMARY KEY  (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='订单明细';



-- ----------------------------

-- Table structure for organization

-- ----------------------------

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `id` int(11) NOT NULL,
  `orani_name` varchar(40) default NULL,
  `org_code` varchar(40) default NULL,
  `org_shortname` varchar(100) default NULL,
  `org_remark` varchar(1000) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;



-- ----------------------------

-- Table structure for payway1

-- ----------------------------

DROP TABLE IF EXISTS `payway1`;

CREATE TABLE `payway1` (
  `pay_id` int(11) NOT NULL COMMENT '付款方式id',
  `pay_name` varchar(40) default NULL COMMENT '付款方式名称',
  PRIMARY KEY  (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='付款方式表';



-- ----------------------------

-- Table structure for product1

-- ----------------------------

DROP TABLE IF EXISTS `product1`;

CREATE TABLE `product1` (
  `pro_id` int(11) NOT NULL auto_increment COMMENT '产品ID',
  `pro_name` varchar(100) default NULL COMMENT '产品名称',
  `pro_code` varchar(100) default NULL COMMENT '产品代码',
  `sign_id` int(11) default NULL COMMENT '品牌ID',
  `type_id` int(11) default NULL COMMENT '产品类别ID',
  `progroup_id` int(11) default NULL COMMENT '产品组ID',
  `pro_price` double(8,2) default NULL COMMENT '产品单价',
  `pro_unit` varchar(10) default NULL COMMENT '产品单位',
  `pro_imagepath` varchar(100) default NULL COMMENT '图片路径',
  `pro_feature` varchar(200) default NULL COMMENT '产品特点',
  `pro_remark` varchar(255) default NULL COMMENT '产品备注',
  PRIMARY KEY  (`pro_id`),
  KEY `fk_ref_brand` (`sign_id`),
  KEY `fk_ref_protype` (`type_id`),
  KEY `fk_ref_progroup` (`progroup_id`),
  CONSTRAINT `fk_ref_brand` FOREIGN KEY (`sign_id`) REFERENCES `brand1` (`brand_id`),
  CONSTRAINT `fk_ref_progroup` FOREIGN KEY (`progroup_id`) REFERENCES `productgroup1` (`prog_id`),
  CONSTRAINT `fk_ref_protype` FOREIGN KEY (`type_id`) REFERENCES `producttype1` (`protype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品; InnoDB free: 9216 kB; (`barndid`) REFER `e-bridge/br';



-- ----------------------------

-- Table structure for productdesc1

-- ----------------------------

DROP TABLE IF EXISTS `productdesc1`;

CREATE TABLE `productdesc1` (
  `prodec_id` int(11) NOT NULL auto_increment COMMENT '产品详细信息id',
  `deliverybill_id` int(11) default NULL COMMENT '发贷单id',
  `product_code` varchar(40) default NULL COMMENT '产品代码',
  `product_name` varchar(40) default NULL COMMENT '产品名称',
  `product_count` int(11) default NULL COMMENT '产品数量',
  `weight` double(10,2) default NULL COMMENT '毛重(产品重量)',
  `solidity` double(10,2) default NULL COMMENT '产品体积',
  `factory` varchar(60) default NULL COMMENT '产品工厂(哪个工厂造)',
  `storeaddress` varchar(100) default NULL COMMENT '库存地址 ',
  PRIMARY KEY  (`prodec_id`),
  KEY `fk_ref_deliv_id` (`deliverybill_id`),
  CONSTRAINT `fk_ref_deliv_id` FOREIGN KEY (`deliverybill_id`) REFERENCES `deliverybill3` (`deli_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品明细表';



-- ----------------------------

-- Table structure for productgroup1

-- ----------------------------

DROP TABLE IF EXISTS `productgroup1`;

CREATE TABLE `productgroup1` (
  `prog_id` int(11) NOT NULL auto_increment,
  `pro_groupcode` varchar(20) default NULL,
  `prog_groupname` varchar(20) default NULL,
  `prog_fullname` varchar(100) default NULL,
  `prog_path` varchar(100) default NULL,
  `vender_id` int(11) default NULL,
  PRIMARY KEY  (`prog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品组';



-- ----------------------------

-- Table structure for producttype1

-- ----------------------------

DROP TABLE IF EXISTS `producttype1`;

CREATE TABLE `producttype1` (
  `protype_id` int(11) NOT NULL auto_increment COMMENT '产品类别ID',
  `parenttype_id` int(11) default NULL COMMENT '父产品类别ID',
  `type_name` varchar(20) default NULL COMMENT '产品类别名',
  `type_code` varchar(20) default NULL COMMENT '产品类别代码',
  PRIMARY KEY  (`protype_id`),
  KEY `fk_ref_type` (`parenttype_id`),
  CONSTRAINT `fk_ref_type` FOREIGN KEY (`parenttype_id`) REFERENCES `producttype1` (`protype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品类别;';



-- ----------------------------

-- Table structure for purchaser1

-- ----------------------------

DROP TABLE IF EXISTS `purchaser1`;

CREATE TABLE `purchaser1` (
  `pur_id` int(11) NOT NULL auto_increment,
  `pur_name` varchar(100) NOT NULL COMMENT '公司名字',
  `pur_password` varchar(40) NOT NULL COMMENT '公司密码',
  `pur_telephone` varchar(20) default NULL COMMENT '公司电话',
  `pur_province` varchar(100) default NULL COMMENT '公司所在省份',
  `pur_city` varchar(20) default NULL COMMENT '公司所在城市',
  `pur_address` varchar(100) default NULL,
  `pur_postalcode` varchar(10) default NULL COMMENT '邮编',
  `pur_remark` varchar(200) default NULL COMMENT '公司备注(详细信息,主要负责人)',
  `pur_isvendot` varchar(5) default NULL COMMENT '是否是申请为卖家',
  PRIMARY KEY  (`pur_id`),
  UNIQUE KEY `pur_name` (`pur_name`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='买家表;';



-- ----------------------------

-- Table structure for receiptbill1

-- ----------------------------

DROP TABLE IF EXISTS `receiptbill1`;

CREATE TABLE `receiptbill1` (
  `recei_id` int(11) NOT NULL auto_increment COMMENT '收款单id',
  `orderline_id` int(11) default NULL COMMENT '订单明细ID',
  `order_code` varchar(20) default NULL COMMENT '订单编号',
  `receiptcode` varchar(20) default NULL COMMENT '收款单编号',
  `invoiceno` varchar(20) default NULL COMMENT '发票号',
  `invoicedate` varchar(20) default NULL COMMENT '核票日期',
  `productcode` varchar(20) default NULL COMMENT '产品代码',
  `purchasername` varchar(100) default NULL COMMENT '卖家',
  `amount` int(11) default NULL COMMENT '数量',
  `price` double(15,2) default NULL COMMENT '销售价',
  `money` double(15,2) default NULL COMMENT '金额',
  `receiptdate` varchar(20) default NULL COMMENT '收款日期',
  `salesdate` varchar(20) default NULL COMMENT '售销日期',
  `detailmemo` varchar(1000) default NULL COMMENT '详细说明',
  `owemoney` double(10,2) default NULL COMMENT '欠款金额',
  `alreadymoney` double(15,2) default NULL COMMENT '已付金额',
  `termdate` varchar(20) default NULL COMMENT '到期日',
  `happendate` varchar(20) default NULL COMMENT '发生日期',
  PRIMARY KEY  (`recei_id`),
  KEY `orderline_id` (`orderline_id`),
  CONSTRAINT `receiptbill1_ibfk_1` FOREIGN KEY (`orderline_id`) REFERENCES `orderline1` (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='收款单';



-- ----------------------------

-- Table structure for right_

-- ----------------------------

DROP TABLE IF EXISTS `right_`;

CREATE TABLE `right_` (
  `right_id` int(11) NOT NULL auto_increment,
  `parentid` int(11) default NULL,
  `rolename` varchar(30) default NULL,
  `action` varchar(50) default NULL,
  `rolecode` varchar(20) default NULL,
  `memo` mediumtext,
  PRIMARY KEY  (`right_id`),
  KEY `fk_right_pid` (`parentid`),
  CONSTRAINT `fk_right_pid` FOREIGN KEY (`parentid`) REFERENCES `right_` (`right_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='权限表';



-- ----------------------------

-- Table structure for role

-- ----------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL auto_increment,
  `rolename` varchar(30) default NULL,
  `rolecode` varchar(20) default NULL,
  `memo` varchar(1000) default NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='角色表';



-- ----------------------------

-- Table structure for roleright

-- ----------------------------

DROP TABLE IF EXISTS `roleright`;

CREATE TABLE `roleright` (
  `roleright_id` int(11) NOT NULL auto_increment,
  `rightid` int(11) default NULL,
  `roleid` int(11) default NULL,
  PRIMARY KEY  (`roleright_id`),
  KEY `fk_roleright_rid` (`rightid`),
  KEY `fk_roleright_roid` (`roleid`),
  CONSTRAINT `fk_roleright_rid` FOREIGN KEY (`rightid`) REFERENCES `right_` (`right_id`),
  CONSTRAINT `fk_roleright_roid` FOREIGN KEY (`roleid`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='角色权限';



-- ----------------------------

-- Table structure for specs1

-- ----------------------------

DROP TABLE IF EXISTS `specs1`;

CREATE TABLE `specs1` (
  `spec_id` int(11) NOT NULL auto_increment COMMENT '参数规格id',
  `product_id` int(11) default NULL COMMENT '产品id',
  `spec_name` varchar(20) default NULL COMMENT '参数规格名称',
  `spec_param` varchar(100) default NULL COMMENT '性能参数',
  PRIMARY KEY  (`spec_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `specs1_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product1` (`pro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='参数规格';



-- ----------------------------

-- Table structure for stock1

-- ----------------------------

DROP TABLE IF EXISTS `stock1`;

CREATE TABLE `stock1` (
  `sto_id` int(11) NOT NULL auto_increment COMMENT '库存ID',
  `product_id` int(11) default NULL COMMENT '产品ID',
  `store_id` int(11) default NULL COMMENT '仓库ID',
  `sto_amount` decimal(8,0) default NULL COMMENT '总数',
  `sto_max` decimal(8,0) default NULL COMMENT '最大库存',
  `sto_min` decimal(8,0) default NULL COMMENT '最小库存',
  `sto_buyprice` decimal(8,0) default NULL COMMENT '进货价',
  `sto_sellprice` decimal(8,0) default NULL COMMENT '出货价',
  PRIMARY KEY  (`sto_id`),
  KEY `fk_ref_pro` (`product_id`),
  KEY `fk_ref_store` (`store_id`),
  CONSTRAINT `fk_ref_pro` FOREIGN KEY (`product_id`) REFERENCES `product1` (`pro_id`),
  CONSTRAINT `fk_ref_store` FOREIGN KEY (`store_id`) REFERENCES `storehouse1` (`stor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品库存';



-- ----------------------------

-- Table structure for storehouse1

-- ----------------------------

DROP TABLE IF EXISTS `storehouse1`;

CREATE TABLE `storehouse1` (
  `stor_id` int(11) NOT NULL auto_increment COMMENT '仓库id',
  `store_name` varchar(40) default NULL COMMENT '仓库名称',
  `store_charger` varchar(10) default NULL COMMENT '仓库负责人',
  `store_contactphone` varchar(40) default NULL COMMENT '联系人电话',
  `store_address` varchar(100) default NULL COMMENT '仓库地址',
  `store_memo` mediumtext COMMENT '仓库备注',
  PRIMARY KEY  (`stor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='仓库';



-- ----------------------------

-- Table structure for userrole

-- ----------------------------

DROP TABLE IF EXISTS `userrole`;

CREATE TABLE `userrole` (
  `userrole_id` int(11) NOT NULL auto_increment,
  `roleid` int(11) default NULL,
  `userid` int(11) default NULL,
  PRIMARY KEY  (`userrole_id`),
  KEY `fk_role_rid` (`roleid`),
  KEY `fk_admin_aid` (`userid`),
  CONSTRAINT `fk_admin_aid` FOREIGN KEY (`userid`) REFERENCES `admin1` (`admin_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_role_rid` FOREIGN KEY (`roleid`) REFERENCES `role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='人员角色';



-- ----------------------------

-- Table structure for vender_credit1

-- ----------------------------

DROP TABLE IF EXISTS `vender_credit1`;

CREATE TABLE `vender_credit1` (
  `ven_cre_id` int(11) NOT NULL auto_increment COMMENT '信用ID',
  `ven_id` int(11) NOT NULL COMMENT '外键,卖家ID,一对一',
  `ven_cre_rank` varchar(20) default NULL COMMENT '信用级别',
  `ven_cre_time` varchar(20) default NULL COMMENT '信用帐期',
  `ven_cre_money` double(10,2) default NULL COMMENT '信用金额 ',
  `ven_cre_balance` double(10,2) default NULL COMMENT '信用余额',
  `ven_cre_returnpoint` int(11) default NULL COMMENT '返还点数',
  PRIMARY KEY  (`ven_cre_id`),
  KEY `fk_ref_ven` (`ven_id`),
  CONSTRAINT `fk_ref_ven` FOREIGN KEY (`ven_id`) REFERENCES `vender1` (`ven_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='卖家信用';



-- ----------------------------

-- Table structure for vender1

-- ----------------------------

DROP TABLE IF EXISTS `vender1`;

CREATE TABLE `vender1` (
  `ven_id` int(11) NOT NULL auto_increment,
  `pur_id` int(11) default NULL,
  `productgroup_id` int(11) default NULL COMMENT '外键,产品组ID',
  `ven_shortname` varchar(20) default NULL COMMENT '公司名称',
  `ven_shopcard` int(11) default NULL COMMENT '营业执照',
  `ven_fax` varchar(30) default NULL COMMENT '公司传真',
  `ven_linkman` varchar(10) default NULL COMMENT '公司联系人',
  `ven_linkmanphone` varchar(50) default NULL COMMENT '联系人电话',
  `ven_email` varchar(100) default NULL COMMENT '公司电子邮件地址',
  `ven_status` int(11) default NULL COMMENT '-1是审核未通过,0为没有审核 ,1为审核已通过',
  PRIMARY KEY  (`ven_id`),
  UNIQUE KEY `pur_id` (`pur_id`),
  KEY `FK14B5DE5F428BFE62` (`productgroup_id`),
  KEY `FK14B5DE5FB327556` (`pur_id`),
  CONSTRAINT `FK14B5DE5F428BFE62` FOREIGN KEY (`productgroup_id`) REFERENCES `productgroup1` (`prog_id`),
  CONSTRAINT `vender1_ibfk_2` FOREIGN KEY (`pur_id`) REFERENCES `purchaser1` (`pur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='卖家';



-- ----------------------------

-- Records 

-- ----------------------------

INSERT INTO `admin1` VALUES ('1', 'admin', '123456', '广州', '321231231', '1322222222', 'schrq@yahoo.com', '这是管理员的备注', null, null, null);

INSERT INTO `bconfig` VALUES ('1', '0', '1', '1', '1', 'sitename\r\n飞达网上商城购物网站系统源代码,网络在线购物网站模板,免费的asp购物系统源码,电子商务网站后台程序', 'sitekeywords\r\n购物网站模板,购物网模板,购物网页模板,网上购物网站模板,网上购物模板,免费购物网站模板,购物系统模板,购物模板下载,购物网站模板下载,韩国购物网站模板,购物模板素材,购物车模板,php购物网站模板,网上购物系统,网上购物系统论文,网络购物系统,在线购物系统,免费购物系统,网域网络购物系统,asp购物系统,网域网上购物系统,购物车系统,asp网上购物系统,网域商城购物系统,网上购物系统设计,网域购物系统,购物系统,网上购物系统下载,jsp网上购物系统,网上商城购物系统,php购物系统,免费网上购物系统,网上购物系统流程图,电子购物系统,网上购物系统分析,购物系统论文,购物网站系统,在线购物系统概述,网上购物管理系统,购物系统下载,网上购物系统的设计,购物系统,网络购物系统论文,网上购物系统源码,电视购物系统,jsp购物系统,网上购物系统,网域网络购物系统,网上购物系统源代码,商城购物系统,超市购物系统,购物系统模板,net网上购物系统,购物系统免费版,购物系统源码,网上购物系统破解版,网上购物系统功能,多用户购物系统,网域网上购物系统,网吧购物系统,网上购物系统 介绍,asp购物车系统,java网上购物系统,免费 网络购物系统,购物系统设计,网上购物系统意义,jsp购物车系统,电脑购物系统,网上购物系统的构建,购物系统 html,免费下载购物系统,简单购物系统,多用户网上购物系统,asp免费购物系统,网上购物系统结构,淘宝购物系统,在线购物系统论文,asp简单购物系统,校园购物系统,网上购物系统的特点,asp,net 购物系统,网上购物系统的发展,网上购物系统发展,网上购物系统源文件,网域购物系统,网上购物系统描述,网上购物系统摘要,网络购物系统代码,购物系统论文下载,asp购', 'sitedescription\r\n网络商城购物网站管理系统源码,电子商务网站系统源代码，购物网站系统模板,商城购物系统模板，免费版网上购物系统后台程序，在线购物系统模板正式版，购物系统网页模板，购物网站建站软件，电子商务购物网站程序，破解版网上开店建站软件,电子商务商城购物网站下载，电子商务网站,网络商城购物，电子购物系统，网上购物车系统，asp购物系统，asp网上商店开店系统源站，虚拟购物系统，购物网站平台源码，购物网站系统下载，购物软件下载版，网域高科电子商务网站系统', 'http://www.pcschool.com.tw/', '广东省广州市天河区展望KB数码18楼巨匠IT', '412212', '王勇', 'wangyong31893189@163.com', '020-34506590,34700400', '406897011,515961153,781803766', '网站客服,网站客服,网站客服', '1', '1', '1', '2', 'qqmsg_off\r\n给我留言呀', '有问题找我们呀', 'wangyong31893189@126.com', 'adm_icp\r\nICP备案登记号0505012001', '巨匠IT', '13760746489', '13760746489', '1', '1', '33', '普通会员', '10', '高级会员', '9.5', '钻石会员', '9', null, '10', null, '10', null, '10', '16', '5', '12', '10', '1', '1', '\"货品准备中\"', '\"价格请咨询\"', '\"请先登陆\"', '77CC00', '0066ff', '1', '1', '1', '0', '1', '1', '1', 'topmenu\r\n\"<P><A href=\"\"class.jsp?LarCode=音像-百科类\"\"></A>&nbsp;</P>\"', '1', '1', 'blue', '货到付款', '普通邮寄', '特快专递', '申通快运', '0', '0', '0', '5', '15', '15', '0', '0', '2000', 'mianyoufei_msg\r\n购物满2000元免邮费', '商城新闻', '广告管理', '行业信息', null, null, '1', 'guanbi\r\n网站正在维护，请稍后访问，给您带来的不便，请谅解。', 'reg_xieyi\r\n　　欢迎您注册为本站会员，您在访问本站（含论坛）时，请您自觉遵守以下条款。<BR><BR>一、不得利用本站危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本站制作、复制和传播下列信息：&nbsp;<BR>　　（一）煽动抗拒、破坏宪法和法律、行政法规实施的；<BR>　　（二）煽动颠覆国家政权，推翻社会主义制度的；<BR>　　（三）煽动分裂国家、破坏国家统一的；<BR>　　（四）煽动民族仇恨、民族歧视，破坏民族团结的；<BR>　　（五）捏造或者歪曲事实，散布谣言，扰乱社会秩序的；<BR>　　（六）宣扬封建迷信、淫秽、色情、赌博、暴力、凶杀、恐怖、教唆犯罪的；<BR>　　（七）公然侮辱他人或者捏造事实诽谤他人的，或者进行其他恶意攻击的；<BR>　　（八）损害国家机关信誉的；<BR>　　（九）其他违反宪法和法律行政法规的；<BR>　　（十）进行商业广告行为的。<BR><BR>二、互相尊重，对自己的言论和行为负责。<BR>', '0', null, '0', null, '0');

INSERT INTO `brand1` VALUES ('1', '海尔', '家电-电视-CRT电视/液晶', 'images/brands/Button_pp_amd.jpg');

INSERT INTO `brand1` VALUES ('2', '康佳', '家电-电视-CRT电视', 'images/brands/Button_pp_apple.jpg');

INSERT INTO `brand1` VALUES ('3', '三星', '家电-电视-CRT电视/液晶', 'images/brands/Button_pp_cre.jpg');

INSERT INTO `brand1` VALUES ('4', 'JVC', '家电-电视-等离子电视', 'images/brands/Button_pp_DLINK.jpg');

INSERT INTO `brand1` VALUES ('5', '松下', '家电-电视-等离子电视', 'images/brands/Button_pp_hair.jpg');

INSERT INTO `brand1` VALUES ('6', '长虹', '家电-电视-液晶电视', 'images/brands/Button_pp_hp.jpg');

INSERT INTO `brand1` VALUES ('7', 'JBL', '家电-音响-CRT音响', 'images/brands/Button_pp_joyoung.jpg');

INSERT INTO `brand1` VALUES ('8', 'Nakamichi', '家电-音响-CRT音响', 'images/brands/Button_pp_motorola.jpg');

INSERT INTO `brand1` VALUES ('9', 'Sony', '家电-音响-CRT音响', 'images/brands/Button_pp_netac.jpg');

INSERT INTO `brand1` VALUES ('10', '惠威', '家电-音响-CRT音响', 'images/brands/Button_pp_nokia.jpg');

INSERT INTO `brand1` VALUES ('11', '威锋', '家电-电视-mini电视', 'images/brands/Button_pp_philips.jpg');

INSERT INTO `deliverybill1` VALUES ('1', '1', '1001', '20', 'gjun', '43021', '10023', '广东省', '广州市', '广东省广州市天河区', 'wy', '空运', '北京1号仓库', '要快速', '本人签收', '1辆车', '持本人身份证', 'gjun', '手机', '13760746489');

INSERT INTO `deliverybill1` VALUES ('2', '2', '1001', '20', 'gjun', '43021', '10023', '广东省', '广州市', '广东省广州市天河区', 'wy', '空运', '北京1号仓库', '要快速', '本人签收', '1辆车', '持本人身份证', 'gjun', '手机', '13760746489');

INSERT INTO `deliverybill1` VALUES ('3', '3', '1001', '20', 'gjun', '43021', '10023', '广东省', '广州市', '广东省广州市天河区', 'wy', '空运', '北京1号仓库', '要快速', '本人签收', '1辆车', '持本人身份证', 'gjun', '手机', '13760746489');

INSERT INTO `deliverybill1` VALUES ('4', '4', '1001', '20', 'gjun', '43021', '10023', '广东省', '广州市', '广东省广州市天河区', 'wy', '空运', '北京1号仓库', '要快速', '本人签收', '1辆车', '持本人身份证', 'gjun', '手机', '13760746489');

INSERT INTO `deliverybill1` VALUES ('5', '5', '1001', '20', 'gjun', '43021', '10023', '广东省', '广州市', '广东省广州市天河区', 'wy', '空运', '北京1号仓库', '要快速', '本人签收', '1辆车', '持本人身份证', 'gjun', '手机', '13760746489');

INSERT INTO `image1` VALUES ('1', '1', 'IBMA3108笔记本', '/computer/notebook/IBM/IBMA3108.jpg', '这是图片说明');

INSERT INTO `order1` VALUES ('1', '1000', '广州巨匠电脑订单', 'gjun', '货到付款', '2009-1-2', '货物销售统一发票', '广州巨匠公司', '广州巨匠公司', '2009-12-12', 'gjun2009', '这是备注', 'yes', '15919191919', '1322222222', '2009-01-02 00:00:00');

INSERT INTO `order1` VALUES ('2', '1001', 'kuaisu', 'gjun ', '货到付款', '2009-5-20', '增值税专用发票', 'gjun', '广州', '2009-5-20', 'dsfsfsdf', 'sadasdada', 'yes', '13760746489', '15874963512', '2009-04-13 11:05:49');

INSERT INTO `order1` VALUES ('3', '1001', 'kuaisu', 'gjun', '货到付款', '2009-5-20', '增值税专用发票', 'gjun', '广州', '2009-5-20', 'dsfsfsdf', 'sadasdada', 'yes', '13760746489', '15874963512', '2009-04-13 11:06:11');

INSERT INTO `order1` VALUES ('4', '1002', 'kuaisuwe', 'gjun', '货到付款', '2009-5-20', '增值税专用发票', 'gjun', '广州', '2009-5-20', 'dsfsfsdf', 'dasfsdf', 'yes', '13760746489', '15874963512', '2009-04-13 11:57:56');

INSERT INTO `order1` VALUES ('5', '1002', 'kuaisuwe', 'gjun', '货到付款', '2009-5-20', '增值税专用发票', 'gjun', '广州', '2009-5-20', 'dsfsfsdf', 'dasfsdf', 'yes', '13760746489', '15874963512', '2009-04-13 12:00:08');

INSERT INTO `order1` VALUES ('7', '1004', 'wy12', '123456', '货到付款', '2009-4-15', '增值税专用发票', '123456', 'gz', '2009-4-30', '1231333', 'sdfsfs', 'yes', '13760746489', '', '2009-04-15 14:08:26');

INSERT INTO `order1` VALUES ('10', '1005', 'wer', '广州巨匠', '货到付款', '2009-4-16', '增值税专用发票', '广州巨匠', '广州巨匠', '2009-4-30', '12313', 'sdfsf', 'yes', '13789456781', '', '2009-04-16 21:34:59');

INSERT INTO `order1` VALUES ('11', '1006', 'sd', 'liushan2', '货到付款', '2009-4-17', '增值税专用发票', 'liushan2', 'ds', '2009-4-17', 'ewrew', 'sdfds', 'yes', '13789456781', '', '2009-04-17 01:02:40');

INSERT INTO `order1` VALUES ('12', '1007', 'kuansu', 'gjunna', '货到付款', '2099-4-19', '增值税专用发票', 'gjunna', 'gz', '2099-4-30', '1231312', 'ksjfks', 'yes', '13789456781', '', '2099-04-19 15:06:41');

INSERT INTO `orderline1` VALUES ('1', '1', '5', '1', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '待审核');

INSERT INTO `orderline1` VALUES ('2', '1', '8', '1', '北京卖电脑公司', '123456', '库存地中山', '400.00', '审核通过');

INSERT INTO `orderline1` VALUES ('3', '1', '4', '1', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '待审核');

INSERT INTO `orderline1` VALUES ('4', '1', '6', '1', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '审核通过');

INSERT INTO `orderline1` VALUES ('5', '2', '11', '56', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '待审核');

INSERT INTO `orderline1` VALUES ('6', '1', '15', '23', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '待审核');

INSERT INTO `orderline1` VALUES ('7', '1', '18', '12', '北京卖电脑公司', '123456', '库存地中山', '400.00', '审核通过');

INSERT INTO `orderline1` VALUES ('8', '3', '21', '32', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '审核通过');

INSERT INTO `orderline1` VALUES ('9', '1', '1', '11', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '待审核');

INSERT INTO `orderline1` VALUES ('10', '1', '2', '13', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '审核通过');

INSERT INTO `orderline1` VALUES ('11', '3', '4', '21', '北京卖电脑公司', '123456', '库存地中山', '400.00', '待审核');

INSERT INTO `orderline1` VALUES ('12', '4', '23', '1', '北京卖电脑公司', '123456', '库存地深圳', '200.00', '审核通过');

INSERT INTO `orderline1` VALUES ('14', '7', '2', '5', '卖家公司简称1', '2', '北京长安街2', '249995.00', '待审核');

INSERT INTO `orderline1` VALUES ('15', '7', '3', '1', '卖家公司简称1', '3', '北京长安街3', '46800.00', '审核通过');

INSERT INTO `orderline1` VALUES ('22', '10', '2', '1', '卖家公司简称1', '2', '北京长安街2', '49999.00', '待审核');

INSERT INTO `orderline1` VALUES ('23', '11', '3', '1', '卖家公司简称1', '3', '北京长安街3', '46800.00', '待审核');

INSERT INTO `orderline1` VALUES ('24', '12', '8', '5', '卖家公司简称1', '8', '北京长安街', '175495.00', '待审核');

INSERT INTO `payway1` VALUES ('1', '货到付款');

INSERT INTO `payway1` VALUES ('2', '分期付款');

INSERT INTO `payway1` VALUES ('3', '欠款订单');

INSERT INTO `product1` VALUES ('1', 'JBL 4348监听音响', '91db', '7', '8', '1', '4654.50', '台', 'images/products/y2.JPG', '', '');

INSERT INTO `product1` VALUES ('2', 'Nakamichi(中道音响) Sonudspace12 (3碟连放)SS-12', '0158', '8', '9', '1', '49999.00', '台', 'images/products/y3.JPG', '', '');

INSERT INTO `product1` VALUES ('3', 'VICTOR胜利中央音响', 'M800', '9', '10', '1', '46800.00', '台', 'images/products/y4.JPG', '功能超强', '铝合金面板,HIFI级收音系统,全系统安全低压传输,十六分区独立控制,四种音源同时播放,十二种动态显示模式,动态萤光中英文显示界面,二十四路高保真输出并支持多路扩展,U盘Mp3 单曲、随机、选曲、循环播放（支持硬盘播放）');

INSERT INTO `product1` VALUES ('4', 'BOSE音响 悠闲48IV家庭影院音箱', '48IV', '9', '10', '1', '45800.00', '台', 'images/products/y5.JPG', '实体展厅', '落地式音箱,前置功率：100w以上,阻抗：8欧姆,灵敏度：91db及以上');

INSERT INTO `product1` VALUES ('5', '惠威HiViF4i', 'F4HT', '10', '11', '1', '39000.00', '台', 'images/products/y6.JPG', '数字家庭影院音箱音响 澎湃演绎 完美无暇', '系统形式 ： 四路四阶倒相式音箱系统,箱体外观 ： 实木木纹/豪华钢琴烤漆,净重(公斤/只) ： 71.5kg ,');

INSERT INTO `product1` VALUES ('6', 'Nakamichi(中道音响) Sonudspace21(5碟连放)SS-21', '0152', '8', '9', '1', '45654.00', '台', 'images/products/y1.JPG', '', '');

INSERT INTO `product1` VALUES ('7', 'JBLBG书架式音响', '800AY', '7', '8', '1', '38000.50', '台', 'images/products/y7.JPG', '独特的梯形箱体以及带有 4 阶声学转换和超低损耗电感器的独特分频系统，令书架式扬声器系统再现真实的影院效果', '功率：100w以上,阻抗：8欧姆,灵敏度：86-90db');

INSERT INTO `product1` VALUES ('8', '惠威HiViF2.2', 'HTifi', '10', '11', '1', '35099.00', '台', 'images/products/y8.JPG', '动态声压输出,高灵敏度宽频设计,完全独立声室设计,可以彻底避免单元间的调制干扰提高重放音乐的解析力', '高质量电子零件配置设计，时尚高贵的外观无不体现了HiVi惠威工程师对时代完美影音的追求理念和在电声开发、研制扬声器上的强大技术实力');

INSERT INTO `product1` VALUES ('9', 'DENON(天龙)', 'CX3', '8', '9', '1', '268000.00', '台', 'images/products/y9.JPG', '高品质组合音响【香港行货 商家保修】', '功能：CD播放,声道：2.0声道');

INSERT INTO `product1` VALUES ('10', 'BOSE书架式音响', '901VI', '9', '10', '1', '157000.00', '台', 'images/products/y10.JPG', '', '类型：前置,功率：100w以上,阻抗：8欧姆,灵敏度：86-90db');

INSERT INTO `product1` VALUES ('11', 'JAMO/尊宝音响', 'C807', '9', '10', '1', '146000.00', '台', 'images/products/y11.JPG', '全新行货，实体店', '类型：前置,功率：100w以上,阻抗：8欧姆,灵敏度：86-90db');

INSERT INTO `product1` VALUES ('12', 'SONY索尼家庭影院', 'DAV-LF1H', '9', '10', '1', '154000.00', '台', 'images/products/y12.JPG', '无线悬浮外观 全国保修 厂家直销', '声道：5.1声道');

INSERT INTO `product1` VALUES ('13', '惠威HiVihifi家庭影院音箱音响', 'Diva6.2HT', '10', '11', '1', '13808.00', '台', 'images/products/y13.JPG', '家庭影院', '单元配置 ： 低 音 ： YT8N×2,失 真 度 ： 47Hz ～ 20kHz ≤1%(2.83V/1m) ,灵敏度（2.83V/m） ： 90dB,功率范围 ： 10 ～ 200W,箱体外观 ： 豪华天然酸枝木皮配黑色钢琴烤漆,净 重 ： 35 kg ');

INSERT INTO `product1` VALUES ('14', 'JBL音响', 'E90', '7', '8', '1', '12000.00', '台', 'images/products/y14.JPG', '全新正品，实体店铺', '声道：5.1声道,音箱外观：古典/经典式音响,带否DVD播放机：不带DVD机');

INSERT INTO `product1` VALUES ('15', 'SCCH', 'SC-HT901W', '9', '10', '1', '9880.00', '台', 'images/products/y15.JPG', '', '组合音响');

INSERT INTO `product1` VALUES ('16', 'JBL落地式音箱', 'L890', '7', '8', '1', '9200.00', '台', 'images/products/y16.JPG', '', '类型：前置,功率：100w以上,阻抗：8欧姆,灵敏度：86-90db');

INSERT INTO `product1` VALUES ('17', 'JVC', 'HD-Z70RF7', '4', '6', '2', '29999.00', '台', 'images/products/d1.JPG', '', '新款70寸超大光硅晶高清电视机');

INSERT INTO `product1` VALUES ('18', '三星液晶电视机', 'LA52A650A1R', '3', '6', '2', '19999.00', '台', 'images/products/d2.JPG', '图像性能:三星数码自然影像技术DNIe（Digital Natural Image engine）,100Hz 智能动感技术,音频性能 SRS TruSurround XT,屏幕尺寸 52英寸,屏幕比例 16:9 ,分辨率 1920×1080 ,反应时间 0ms,屏幕性能 Ultra Clear Panel 黑水晶超清晰面板,机身重量 34.8kg（含底座）', '正品行货带发票全国联保');

INSERT INTO `product1` VALUES ('19', '海尔专卖-海尔液晶电视机', 'HD-Z70RF7', '1', '6', '2', '45654.00', '台', 'images/products/d3.JPG', '屏幕尺寸：47英寸,屏幕比例：宽屏16:9,液晶价格区间：15000-20000元,响应速度：3-6毫秒(含3毫秒),分辨率：1920×1080输入输出', '全国联保');

INSERT INTO `product1` VALUES ('20', '夏普工程液晶电视机', 'LCD-46F63', '6', '6', '2', '12900.00', '台', 'images/products/d4.JPG', '屏幕尺寸：46英寸,屏幕比例：宽屏16:9,液晶价格区间：10000-15000元,清晰度：1080(全高清),响应速度：6-8毫秒(含6毫秒),分辨率：1920×1080,输入输出接口：hdmi接口', '全国联保 工程商用 优惠 正品');

INSERT INTO `product1` VALUES ('21', '海尔专卖-海尔液晶电视机', 'L42R1', '1', '6', '2', '9999.00', '台', 'images/products/d5.JPG', '屏幕尺寸：42英寸,屏幕比例：宽屏16:9,液晶价格区间：8000-10000元,清晰度：1080(全高清),响应速度：3-6毫秒(含3毫秒),分辨率：1366×768', '');

INSERT INTO `product1` VALUES ('22', '松下等离子电视机', 'TH-50PV80C', '5', '6', '2', '9000.00', '台', 'images/products/d6.JPG', '类型  PDP等离子,象素  1,049,088(1,366x768)像素,屏幕  G11自发光屏幕,处理器/驱动器  V-real 3,画面尺寸  50\"(127cm)对角线,不包括支架 1210x790.5x95mm,包括支架 1210x844x387mm', '');

INSERT INTO `product1` VALUES ('23', '吉林家电批发三星液晶电视机', 'LA40A350C1', '3', '6', '2', '6500.00', '台', 'images/products/d7.JPG', '屏幕尺寸：40英寸,屏幕比例：宽屏16:9,液晶价格区间：6000-8000元,清晰度：720p(准高清),响应速度：3-6毫秒(含3毫秒),分辨率：1366×768', '全国联保');

INSERT INTO `product1` VALUES ('24', '海信液晶电视机', 'TLM4236P', '6', '6', '2', '4990.00', '台', 'images/products/d8.JPG', '液晶电视尺寸 42寸,比例 16:9,分辨率 1920×1080,亮度 800cd/㎡,对比度 8000:1,响应时间 4ms,可视角度 178°/178°', '正规发票 全国联保 山东泰安专卖');

INSERT INTO `product1` VALUES ('25', '100%好评的47寸液晶电视机', '100G', '5', '6', '2', '4670.00', '台', 'images/products/d9.JPG', '屏幕尺寸：47英寸,屏幕比例：宽屏16:9,液晶价格区间：4000-6000元,清晰度：1080(全高清),响应速度：8-16毫秒(含8毫秒),分辨率：1280×1024,输入输出接口：av接口', '店铺三包');

INSERT INTO `product1` VALUES ('26', '埃克佩迪 电视机柜', 'Tord Bj?rklund', '5', '6', '2', '1799.00', '台', 'images/products/d10.JPG', '宽度: 185 厘米,深度: 39 厘米,高度: 185 厘米,最大承重量: 13 公斤', '【乾天量贩】【皇冠/宜家代购】');

INSERT INTO `product1` VALUES ('27', '长虹电视', 'CHD29155', '6', '6', '2', '1599.00', '台', 'images/products/d11.JPG', '净重： 约66kg,尺寸： 宽917×高700×厚565mm,颜色： 珍珠黑/闪银罩光', '量子芯电视 高清电视');

INSERT INTO `product1` VALUES ('28', '长虹29寸纯平电视机', 'PF29399', '6', '6', '2', '1198.00', '台', 'images/products/d12.JPG', '电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：纯平,输入输出接口：av接口', '');

INSERT INTO `product1` VALUES ('29', '海尔专卖-海尔纯平彩色电视机', '21FA11-AM', '1', '6', '2', '1099.00', '台', 'images/products/d13.JPG', '电视尺寸：21英寸,屏幕比例：4:3,普屏显像管：纯平', '');

INSERT INTO `product1` VALUES ('30', '全新22寸液晶电视机', 'XX', '6', '6', '2', '860.00', '台', 'images/products/d14.JPG', '屏幕尺寸：22英寸,屏幕比例：宽屏16:10,液晶价格区间：4000元以下,清晰度：720p(准高清),响应速度：3-6毫秒(含3毫秒),分辨率：1680×1050,输入输出接口：av接口', '店铺三包');

INSERT INTO `product1` VALUES ('31', '三洋29寸数字高清电视机', '8C9', '3', '6', '2', '859.00', '台', 'images/products/d15.JPG', '电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口', '【润眼不闪】');

INSERT INTO `product1` VALUES ('32', '高光中国红', '8B9', '3', '6', '2', '859.00', '台', 'images/products/d16.JPG', '电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口', '三星29寸数字高清润眼不闪电视机');

INSERT INTO `product1` VALUES ('33', '海尔彩电', 'HD-Z70RF7', '1', '5', '2', '2399.00', '台', 'images/products/cd1.JPG', '超薄数字高清晰显像管,DSM+睿驰三核芯引擎,数字高清格式全兼容,逐点晶晰扫描,像素增强技术', '图像制式 PAL、NTSC;伴音制式 DK,I,B/G,M;整机功率(W) 180W ;电压范围(V) 160-250V ;端子 AV输入*2、AV输出、DVD分量输入、S端子、耳机插口、Ypbpr+音频、VGA+音频、HDMI ;附件 说明书、遥控器、电池*2、电源线;长*宽*高(含底座)mm 800*444*596.5 ');

INSERT INTO `product1` VALUES ('34', '海尔', 'D29FT1A', '1', '5', '2', '1999.00', '台', 'images/products/cd2.JPG', '', '电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：纯平');

INSERT INTO `product1` VALUES ('35', '海尔', 'D29FA12-AKM', '1', '5', '2', '1999.00', '台', 'images/products/cd3.JPG', '', '电视尺寸：29英寸屏幕比例：4:3普屏显像管：纯平');

INSERT INTO `product1` VALUES ('36', '海尔纯平数字高清电视', 'D29FK1', '1', '5', '2', '1859.00', '台', 'image/products/cd4.JPG', '', '电视尺寸：29英寸屏幕比例：4:3普屏显像管：纯平');

INSERT INTO `product1` VALUES ('37', '海尔', '29FA12-AM', '1', '5', '2', '1799.00', '台', 'images/products/cd5.JPG', '显象管 超薄防爆显像管,系统 图象制式 PAL 、NTSC,存储频道数量 218', '电视尺寸：29英寸屏幕比例：4:3普屏显像管：纯平');

INSERT INTO `product1` VALUES ('38', '供应欧标SCART端子电视机', 'SJHU', '2', '5', '2', '29999.00', '台', 'images/products/k1.JPG', '', '品牌：康佳/KONKACRT,电视尺寸：21英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('39', '康佳彩色电视机', 'SCART', '2', '5', '2', '799.00', '台', 'images/products/k2.JPG', '', '保修：无 ,编号：b72698291d4324100b618f6b ');

INSERT INTO `product1` VALUES ('40', '康佳彩色电视机', 'SCARTB', '2', '5', '2', '786.00', '台', 'images/products/k3.JPG', '', '品牌：康佳/KONKACRT,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('41', '北京二手电视机二手彩电25寸康佳超平八成新免费送货', '2H', '2', '5', '2', '540.00', '台', 'images/products/k4.JPG', '', '品牌：康佳/KONKACRT,电视尺寸：25英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('42', '三星29健康超平数码高清彩色电视机', '29SXC', '3', '5', '2', '950.00', '台', 'images/products/s1.JPG', '超薄液晶款式', '品牌：三星/SAMSUNGCRT,电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('43', '三星29寸数字高清润眼不闪电视机', '28KL', '3', '5', '2', '856.00', '台', 'images/products/s2.JPG', '高光中国红', '品牌：三星/SAMSUNGCRT,电视尺寸：29英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('44', '三星25寸数字高清电视机', 'SOPA', '3', '5', '2', '699.00', '台', 'images/products/s3.JPG', '润眼不闪', '品牌：三星/SAMSUNGCRT,电视尺寸：25英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('45', '三星25寸数字高清电视机', 'SCN', '3', '5', '2', '669.00', '台', 'images/products/s4.JPG', '新品黑金刚', '三星/SAMSUNGCRT,电视尺寸：25英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：av接口');

INSERT INTO `product1` VALUES ('46', '三星25超平健康数码高清彩色电视机', 'SDHJ', '3', '5', '2', '530.00', '台', 'images/products/s5.JPG', '大众款式', '品牌：三星/SAMSUNGCRT,电视尺寸：25英寸,屏幕比例：4:3,普屏显像管：普通,输入输出接口：tv接口');

INSERT INTO `product1` VALUES ('47', '真彩14寸彩壳彩色迷你电视机', 'XINYU', '11', '7', '2', '378.00', '台', 'images/products/m1.JPG', '彩色电视机特惠378', '保修：有, 编号：10cbbdeadea9dad12dbc280c');

INSERT INTO `product1` VALUES ('48', '威锋9寸纯平仿背投黑白迷你小电视', 'MWCX', '11', '7', '2', '124.00', '台', 'images/products/m2.JPG', '9寸高清晰度动感画面,外接AV插口 可接DVD等播放器,中英电脑学习机兼容,卡拉OK、闭路电视监视兼容,交直流两用电源,欧式超酷外观设计，超薄外包围，适合现代家居,双声道喇叭立体声设计', '');

INSERT INTO `product1` VALUES ('49', '威锋8寸纯平仿背投黑白迷你小电视', 'MMCX', '11', '7', '2', '108.00', '台', 'images/products/m3.JPG', '高清晰度动感画面,外接AV插口，可接DVD，游戏机等外接设备,中英电脑学习机兼容,卡拉OK、闭路电视监视兼容,交流电源,带耳机插座功能,全频道电视节目接收功能', '纸箱规格(mm):46.7（L）＊45.7（W）＊53.5（H）,2272 pcs / 20-ft,5088 pcs / 40-ft HQ');

INSERT INTO `product1` VALUES ('50', '7寸纯平黑白迷你小电视', 'WF-727', '11', '7', '2', '105.00', '台', 'images/products/m4.JPG', '高清晰度动感画面,外接AV插口,中英电脑学习机兼容,卡拉OK、闭路电视监视兼容,交直流两用电源,可调式转向底座,全频道电视节目接收功能', '');

INSERT INTO `productdesc1` VALUES ('1', '1', '产品代码', 'IBM笔记本', '300', '320.00', '323.00', '东莞工厂', '库存地北京');

INSERT INTO `productgroup1` VALUES ('1', 'YX', '音响', '家电-音响', null, '1');

INSERT INTO `productgroup1` VALUES ('2', 'DS', '电视', '家电-电视', null, '1');

INSERT INTO `productgroup1` VALUES ('3', null, null, '11111111111111', null, null);

INSERT INTO `productgroup1` VALUES ('4', null, null, '11111111111111', null, null);

INSERT INTO `productgroup1` VALUES ('15', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('16', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('17', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('18', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('19', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('20', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('21', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('22', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('23', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('24', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('25', null, null, null, null, null);

INSERT INTO `productgroup1` VALUES ('26', null, null, null, null, null);

INSERT INTO `producttype1` VALUES ('1', null, '家电', 'JD');

INSERT INTO `producttype1` VALUES ('2', null, '数码', 'SM');

INSERT INTO `producttype1` VALUES ('3', '1', '音响', 'JDYX');

INSERT INTO `producttype1` VALUES ('4', '2', '电视', 'JDDS');

INSERT INTO `producttype1` VALUES ('5', '4', 'CRT电视', 'CRTDS');

INSERT INTO `producttype1` VALUES ('6', '4', '等离子液晶电视', 'DLZYJDS');

INSERT INTO `producttype1` VALUES ('7', '4', '迷你液晶电视', 'MNDS');

INSERT INTO `producttype1` VALUES ('8', '3', 'JBL音响', 'JYX');

INSERT INTO `producttype1` VALUES ('9', '3', 'Nakamichi音响', 'NYX');

INSERT INTO `producttype1` VALUES ('10', '3', 'Sony音响', 'SYX');

INSERT INTO `producttype1` VALUES ('11', '3', '惠威音响', 'WYX');

INSERT INTO `purchaser1` VALUES ('1', 'gjun', '123456', '020-1111111', '广东', '广州', '广州天河石牌西路8号', '510630', '公司备注', 'yes');

INSERT INTO `purchaser1` VALUES ('3', 'wangyong', '123456', '020-1111111', '广东', '广州', '广州天河石牌西路8号', '510630', '公司备注', 'yes');

INSERT INTO `purchaser1` VALUES ('4', 'liushan', '123456', '020-1111111', '广东', '广州', '广州天河石牌西路8号', '510630', '公司备注', 'yes');

INSERT INTO `purchaser1` VALUES ('13', '广州巨匠IT', '1234567', '0733-3189449', '220000', '220200', '广州市', '412212', '天河区', 'no');

INSERT INTO `purchaser1` VALUES ('14', '1232131', '123456', '0733-3189449', '210000', '210300', '广州市', '412212', 'ksdjfkdsjfsk', 'no');

INSERT INTO `purchaser1` VALUES ('17', 'leijun12', '123456', '0733-3189449', '130000', '-1', 'sdfdsfa', '412212', 'sdfsdfdsf', 'yes');

INSERT INTO `receiptbill1` VALUES ('1', '1', '544444', '12312', '20003', '2009-6-6', 'A7333', '北京卖电脑公司', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '详细说明', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('2', '2', '234234', '324324', '435354', '2009-5-6', 'B33434', '广州电脑公司', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '详细说明', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('3', '3', '234234', '324324', '435354', '2009-5-6', 'B33434', '广州电脑公司', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '详细说明', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('4', '4', '544444', '12312', '20003', '2009-6-6', 'A7333', '北京卖电脑公司', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '详细说明', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('5', '5', '544544', '12352', '20053', '2009-6-6', 'A7333', '北京卖电脑公司', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '详细说明', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('6', '6', '234234', '324324', '435354', '2009-5-6', 'B33434', '广州电脑公司', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '详细说明', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('7', '7', '544444', '12312', '20003', '2009-6-6', 'A7333', '北京卖电脑公司', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '详细说明', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('8', '8', '544544', '12352', '20053', '2009-6-6', 'A7333', '北京卖电脑公司', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '详细说明', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('9', '9', '234234', '324324', '435354', '2009-5-6', 'B33434', '广州电脑公司', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '详细说明', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('10', '24', null, null, null, null, null, null, null, '0.00', '0.00', null, null, null, null, null, null, null);

INSERT INTO `specs1` VALUES ('1', '1', '规格名称', '性能参数');

INSERT INTO `stock1` VALUES ('1', '1', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('2', '2', '2', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('3', '3', '3', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('4', '4', '4', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('5', '5', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('6', '6', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('7', '7', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('8', '8', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('9', '9', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('10', '10', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('11', '11', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('12', '12', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('13', '13', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('14', '14', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('15', '15', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('16', '16', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('17', '17', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('18', '18', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('19', '19', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('20', '20', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('21', '21', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('23', '22', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('24', '23', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('25', '24', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('26', '25', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('27', '26', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('28', '27', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('29', '28', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('30', '29', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('31', '30', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('32', '31', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('33', '32', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('34', '33', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('35', '34', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('36', '35', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('37', '36', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('38', '37', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('39', '38', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('40', '39', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('41', '40', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('42', '41', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('43', '42', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('44', '43', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('45', '44', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `stock1` VALUES ('46', '45', '1', '400000', '20000000', '400', '400', '6000');

INSERT INTO `storehouse1` VALUES ('1', '北京12仓库', '仓库负责人李四', '132999999999', '北京长安街', '仓库备注');

INSERT INTO `storehouse1` VALUES ('2', '北京13仓库', '仓库负责人李四', '132999999999', '北京长安街2', '仓库备注');

INSERT INTO `storehouse1` VALUES ('3', '北京15仓库', '仓库负责人李四', '132999999999', '北京长安街3', '仓库备注');

INSERT INTO `storehouse1` VALUES ('4', '北京16仓库', '仓库负责人李四', '132999999999', '北京长安街4', '仓库备注');

INSERT INTO `storehouse1` VALUES ('5', '北京17仓库', '仓库负责人李四', '132999999999', '北京长安街5', '仓库备注');

INSERT INTO `storehouse1` VALUES ('6', '北京18仓库', '仓库负责人李四', '132999999999', '北京长安街6', '仓库备注');

INSERT INTO `storehouse1` VALUES ('7', '北京10仓库', '仓库负责人李四', '132999999999', '北京长安街7', '仓库备注');

INSERT INTO `vender_credit1` VALUES ('1', '1', '信用级别5', '信用帐期2009-12-12', '5000000.00', '3333.00', '200');

INSERT INTO `vender1` VALUES ('1', '1', '1', '卖家公司简称1', '7676767', '010-13222222', '张三', '13222222222', 'bj@yahoo.com', '-1');

INSERT INTO `vender1` VALUES ('8', '3', '15', '1231', '342423', 'wrewrwr', '1231231', '12312313', '3324324234', '-1');

INSERT INTO `vender1` VALUES ('9', '4', '3', 'gz', '12313', '3189449', 'sdfjsf', '13760746489', 'wangyong31893189@163.com', '0');

INSERT INTO `vender1` VALUES ('10', '13', '16', 'gjun', '123213123', '0733-3189449', 'jdskf', '13760746489', 'wangyong31893189@163.com', '-1');

INSERT INTO `vender1` VALUES ('11', '14', '17', 'gjun', '123213123', '0733-3189449', 'sdfjsf', '13760746489', 'liushan@qq.com', '-1');

INSERT INTO `vender1` VALUES ('12', '17', '26', 'wang', '123123', '0733-3189449', 'sdfkafka', '13760746489', 'wangyong31893189@163.com', '0');

