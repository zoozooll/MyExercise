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
  `admin_id` int(11) NOT NULL auto_increment COMMENT '����ԱID',
  `admin_name` varchar(20) default NULL COMMENT '����Ա����',
  `admin_password` varchar(40) default NULL COMMENT '����Ա����',
  `admin_address` varchar(100) default NULL COMMENT '����Ա��ַ',
  `admin_idcard` varchar(60) default NULL COMMENT '����Ա���֤��',
  `admin_phone` varchar(20) default NULL COMMENT '����Ա�绰',
  `admin_email` varchar(40) default NULL COMMENT '����Ա����',
  `admin_remark` varchar(200) default NULL COMMENT '����Ա��ע',
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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='����Ա��';



-- ----------------------------

-- Table structure for bconfig

-- ----------------------------

DROP TABLE IF EXISTS `bconfig`;

CREATE TABLE `bconfig` (
  `id` int(11) NOT NULL auto_increment,
  `bodyfixed` varchar(255) default '0' COMMENT '����ģʽ',
  `skin` varchar(255) default '1' COMMENT 'ģ��ѡ��',
  `loginskin` varchar(255) default '1' COMMENT '��½����ʽ',
  `yzm_skin` varchar(255) default '1' COMMENT '��֤����ʽ',
  `sitename` varchar(255) default '"��վ����"' COMMENT 'վ������',
  `sitekeywords` varchar(1000) default '"sfkong,gjun,����,��ɳ,����ϵͳ,����ϵͳ,�����̳�,�������,�̳�Դ��,�����̳�,���߹���,����֧��,��վ����"' COMMENT 'վ��ؼ���',
  `sitedescription` varchar(255) default '' COMMENT 'վ������',
  `siteurl` varchar(50) default NULL COMMENT '����',
  `adm_address` varchar(200) default NULL COMMENT '��ϵ��ַ',
  `adm_post` varchar(50) default NULL COMMENT '�ʱ�',
  `adm_name` varchar(50) default NULL COMMENT '��ϵ��',
  `adm_mail` varchar(50) default NULL COMMENT '����',
  `adm_tel` varchar(50) default NULL COMMENT '�绰',
  `adm_qq` varchar(50) default NULL COMMENT 'QQ',
  `adm_qq_name` varchar(50) default NULL COMMENT '�ǳ�',
  `qqonline` int(11) default '1' COMMENT '�Ƿ���ʾ',
  `whereqq` int(11) default '1' COMMENT '��໹���Ҳ�',
  `kefuskin` varchar(50) default '1' COMMENT '��ʽ',
  `qqskin` varchar(50) default '1' COMMENT 'ͷ��',
  `qqmsg_off` varchar(255) default '"�ͷ������ߣ�������"' COMMENT '������ʾ',
  `qqmsg_on` varchar(255) default '"�ͷ����ߣ������̸"' COMMENT '������ʾ',
  `adm_msn` varchar(50) default NULL COMMENT 'MSN',
  `adm_icp` varchar(50) default NULL COMMENT 'ICP',
  `adm_comp` varchar(50) default NULL COMMENT '��˾����',
  `adm_fax` varchar(50) default NULL COMMENT '����',
  `adm_mob` varchar(50) default NULL COMMENT '�ƶ��绰',
  `adm_kf` varchar(255) default NULL COMMENT '��ϵ��ʽ',
  `jsq` int(11) default NULL,
  `help_hang` int(11) default '3' COMMENT '������������',
  `usertype1` varchar(50) default '"��ͨ��Ա"' COMMENT '�û��ȼ�',
  `kou1` float default '10' COMMENT '�ۿ�',
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
  `prompt_num` int(11) default '8' COMMENT '����Ʒ����',
  `newprod_num` int(11) default '8' COMMENT '�Ƽ���Ʒ����',
  `renmen_num` int(11) default '10' COMMENT '������Ʒ����',
  `fenlei_num` int(11) default '10' COMMENT '��ҳ��Ʒ����',
  `mosi` int(11) default '1' COMMENT '��ҳ��Ʒģʽ',
  `pic_xiaogao` varchar(1) default '1' COMMENT 'ͼƬ�߿�',
  `quehuo` varchar(50) default '"��Ʒ׼����"' COMMENT 'ȱ����ʾ',
  `wujiage` varchar(50) default '"�۸�����ѯ"' COMMENT '�޼۸���ʾ',
  `huiyuanjia` varchar(50) default '"���ȵ�½"' COMMENT 'Ա�����ʾ',
  `lar_color` varchar(10) default NULL COMMENT 'һ��������ɫ',
  `mid_color` varchar(10) default NULL COMMENT '����������ɫ',
  `index_tishi` varchar(50) default '0' COMMENT '���ָ����ҳͼƬʱ����ʾ',
  `tree_num` int(11) default '0' COMMENT '�Ƿ���ʾ�����µ���Ʒ����',
  `tree_view` int(11) default '1' COMMENT '�Ƿ����ط���',
  `tree_display` int(11) default '0' COMMENT 'չ����������',
  `reg` int(11) default '1' COMMENT '�Ƿ�ǿ��ע��',
  `bbs` int(11) default '0' COMMENT '��������ʾBBS�������԰�',
  `menu` int(11) default '1' COMMENT '��ҳ�Ҽ��˵�',
  `topmenu` varchar(255) default NULL COMMENT 'ҳ����������������',
  `newsmove` int(11) default '1' COMMENT '�����Ƿ����',
  `news_skin` varchar(1) default '1' COMMENT '����Ƥ��',
  `kf_color` varchar(10) default '"red"' COMMENT '�ͷ����ߵ���ɫ',
  `pei1` varchar(50) default NULL COMMENT '���䷽ʽ1',
  `pei2` varchar(50) default NULL COMMENT '���䷽ʽ2',
  `pei3` varchar(50) default NULL COMMENT '���䷽ʽ3',
  `pei4` varchar(50) default NULL COMMENT '���䷽ʽ4',
  `pei5` varchar(50) default NULL COMMENT '���䷽ʽ6',
  `pei6` varchar(50) default '0' COMMENT '���䷽ʽ6',
  `fei1` int(11) default '0' COMMENT '�˷�1',
  `fei2` int(11) default '0' COMMENT '�˷�2',
  `fei3` int(11) default '0' COMMENT '�˷�3',
  `fei4` int(11) default '0' COMMENT '�˷�4',
  `fei5` int(11) default '0' COMMENT '�˷�5',
  `fei6` int(11) default '0' COMMENT '�˷�6',
  `mianyoufei` int(11) default '1000' COMMENT '���ʷ�����',
  `mianyoufei_msg` varchar(255) default NULL COMMENT '���ʷ�����',
  `newstitle1` varchar(255) default NULL COMMENT '���ŷ�������1',
  `newstitle2` varchar(255) default NULL COMMENT '���ŷ�������2',
  `newstitle3` varchar(255) default NULL COMMENT '���ŷ�������3',
  `newstitle4` varchar(255) default NULL COMMENT '���ŷ�������4',
  `newstitle5` varchar(255) default NULL COMMENT '���ŷ�������5',
  `kaiguan` int(11) default '1' COMMENT '��վ����',
  `guanbi` varchar(255) default NULL COMMENT '��վ�ر�ʱ����ʾ',
  `reg_xieyi` varchar(1000) default NULL COMMENT 'ע��Э��',
  `lockip` varchar(1) default NULL COMMENT '�Ƿ���������IP����',
  `ip` varchar(100) default NULL COMMENT '����IP���б�',
  `other1` varchar(100) default NULL COMMENT 'Ԥ��',
  `other2` varchar(50) default NULL COMMENT 'Ԥ��',
  `flash1` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;



-- ----------------------------

-- Table structure for brand1

-- ----------------------------

DROP TABLE IF EXISTS `brand1`;

CREATE TABLE `brand1` (
  `brand_id` int(11) NOT NULL auto_increment COMMENT 'Ʒ��ID',
  `brand_name` varchar(20) default NULL COMMENT 'Ʒ����',
  `brand_desc` varchar(300) default NULL COMMENT 'Ʒ������',
  `brand_image_path` varchar(200) default NULL COMMENT '��ƷͼƬ·��',
  PRIMARY KEY  (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='Ʒ��';



-- ----------------------------

-- Table structure for channel1

-- ----------------------------

DROP TABLE IF EXISTS `channel1`;

CREATE TABLE `channel1` (
  `cha_id` int(11) NOT NULL auto_increment,
  `channelcode` varchar(20) default NULL COMMENT '��������',
  `channelname` varchar(40) default NULL COMMENT '��������',
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
  `deli_id` int(11) NOT NULL auto_increment COMMENT '������ID',
  `order_id` int(11) default NULL COMMENT '����ID',
  `delivery_code` varchar(20) default NULL COMMENT '���������',
  `amount` decimal(8,0) default NULL COMMENT '������',
  `clientname` varchar(100) default NULL COMMENT '�ͻ�����',
  `clientcode` varchar(20) default NULL COMMENT '�ͻ����',
  `invoiceno` varchar(20) default NULL COMMENT '��Ʊ���',
  `province` varchar(10) default NULL COMMENT '����ʡ��',
  `city` varchar(10) default NULL COMMENT '��������',
  `address` varchar(100) default NULL COMMENT '�ջ���ַ',
  `contactor` varchar(10) default NULL COMMENT '��ϵ��',
  `sendtype` varchar(10) default NULL COMMENT '���ͷ�ʽ',
  `carryplace` varchar(100) default NULL COMMENT '���͵ص�',
  `specialnote` mediumtext COMMENT '����˵��',
  `signstandard` varchar(100) default NULL COMMENT 'ǩ�ձ�׼',
  `sendcarnote` mediumtext COMMENT '�������ɳ���ע',
  `signnote` mediumtext COMMENT 'ǩ�ձ�ע',
  `pickupman` varchar(10) default NULL COMMENT '�����',
  `contacttype` varchar(20) default NULL COMMENT '��ϵ��ʽ',
  `contactphone` varchar(20) default NULL COMMENT '�ʹ﷽��ϵ�˵绰',
  PRIMARY KEY  (`deli_id`),
  KEY `fk_ref_order` (`order_id`),
  CONSTRAINT `fk_ref_order` FOREIGN KEY (`order_id`) REFERENCES `order1` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='������; ; InnoDB free: 9216 kB';



-- ----------------------------

-- Table structure for image1

-- ----------------------------

DROP TABLE IF EXISTS `image1`;

CREATE TABLE `image1` (
  `img_id` int(11) NOT NULL auto_increment COMMENT 'ͼƬid',
  `product_id` int(11) default NULL COMMENT '��Ʒid',
  `img_name` varchar(20) default NULL COMMENT 'ͼƬ����',
  `img_path` varchar(100) default NULL COMMENT 'ͼƬ·��',
  `img_memo` varchar(1000) default NULL COMMENT 'ͼƬ˵��',
  PRIMARY KEY  (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='ͼƬ';



-- ----------------------------

-- Table structure for order1

-- ----------------------------

DROP TABLE IF EXISTS `order1`;

CREATE TABLE `order1` (
  `order_id` int(11) NOT NULL auto_increment COMMENT '����id',
  `order_code` int(20) default '1000' COMMENT '�������',
  `order_name` varchar(20) default NULL COMMENT '��������',
  `order_source` varchar(20) default NULL COMMENT '������Դ',
  `payway` varchar(20) default NULL COMMENT '���ʽ',
  `paylater` varchar(40) default NULL COMMENT '����',
  `invoicetype` varchar(20) default NULL COMMENT '��Ʊ����:1 ��ֵ˰ר�÷�Ʊ��2 ��������ͳһ��Ʊ��3 ��������ҵר�÷�Ʊ ',
  `invoicehead` varchar(100) default NULL COMMENT '��Ʊ̧ͷ:��Ʊ̧ͷ��ָ��ȡ��Ʊ�Ĺ�˾���ƻ����������',
  `sendto` varchar(100) default NULL COMMENT '�ʹ﷽',
  `arrivetime` varchar(100) default NULL COMMENT 'Ҫ�󵽴�ʱ��',
  `doselfcode` varchar(60) default NULL COMMENT '�ͻ��������',
  `ordermemo` mediumtext COMMENT '������ע',
  `seensms` varchar(10) default NULL COMMENT '�Ƿ���Ҫ����֪ͨ',
  `mobile1` varchar(15) default NULL COMMENT '�ֻ�����1',
  `mobile2` varchar(15) default NULL COMMENT '�ֻ�����2',
  `createddate` datetime default NULL COMMENT '����ʱ��',
  PRIMARY KEY  (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='����; ; InnoDB free: 9216 kB';



-- ----------------------------

-- Table structure for orderline1

-- ----------------------------

DROP TABLE IF EXISTS `orderline1`;

CREATE TABLE `orderline1` (
  `line_id` int(11) NOT NULL auto_increment COMMENT '������ϸid',
  `order_id` int(11) default NULL COMMENT '����id',
  `pro_id` int(11) default NULL COMMENT '��Ʒid',
  `amount` int(11) default NULL COMMENT '����',
  `vender_name` varchar(60) default NULL COMMENT '���ҹ�˾����',
  `vender_code` varchar(20) default NULL COMMENT '���ұ��',
  `storeaddres` varchar(40) default NULL COMMENT '����',
  `summoney` double(15,2) default NULL COMMENT '�ܽ��',
  `orderline_state` varchar(15) default '�����' COMMENT '����״̬: 0: ����ˣ�1:���ͨ��(����)��2:���ջ���ִ��3:�ѻ�ִ',
  PRIMARY KEY  (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='������ϸ';



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
  `pay_id` int(11) NOT NULL COMMENT '���ʽid',
  `pay_name` varchar(40) default NULL COMMENT '���ʽ����',
  PRIMARY KEY  (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='���ʽ��';



-- ----------------------------

-- Table structure for product1

-- ----------------------------

DROP TABLE IF EXISTS `product1`;

CREATE TABLE `product1` (
  `pro_id` int(11) NOT NULL auto_increment COMMENT '��ƷID',
  `pro_name` varchar(100) default NULL COMMENT '��Ʒ����',
  `pro_code` varchar(100) default NULL COMMENT '��Ʒ����',
  `sign_id` int(11) default NULL COMMENT 'Ʒ��ID',
  `type_id` int(11) default NULL COMMENT '��Ʒ���ID',
  `progroup_id` int(11) default NULL COMMENT '��Ʒ��ID',
  `pro_price` double(8,2) default NULL COMMENT '��Ʒ����',
  `pro_unit` varchar(10) default NULL COMMENT '��Ʒ��λ',
  `pro_imagepath` varchar(100) default NULL COMMENT 'ͼƬ·��',
  `pro_feature` varchar(200) default NULL COMMENT '��Ʒ�ص�',
  `pro_remark` varchar(255) default NULL COMMENT '��Ʒ��ע',
  PRIMARY KEY  (`pro_id`),
  KEY `fk_ref_brand` (`sign_id`),
  KEY `fk_ref_protype` (`type_id`),
  KEY `fk_ref_progroup` (`progroup_id`),
  CONSTRAINT `fk_ref_brand` FOREIGN KEY (`sign_id`) REFERENCES `brand1` (`brand_id`),
  CONSTRAINT `fk_ref_progroup` FOREIGN KEY (`progroup_id`) REFERENCES `productgroup1` (`prog_id`),
  CONSTRAINT `fk_ref_protype` FOREIGN KEY (`type_id`) REFERENCES `producttype1` (`protype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʒ; InnoDB free: 9216 kB; (`barndid`) REFER `e-bridge/br';



-- ----------------------------

-- Table structure for productdesc1

-- ----------------------------

DROP TABLE IF EXISTS `productdesc1`;

CREATE TABLE `productdesc1` (
  `prodec_id` int(11) NOT NULL auto_increment COMMENT '��Ʒ��ϸ��Ϣid',
  `deliverybill_id` int(11) default NULL COMMENT '������id',
  `product_code` varchar(40) default NULL COMMENT '��Ʒ����',
  `product_name` varchar(40) default NULL COMMENT '��Ʒ����',
  `product_count` int(11) default NULL COMMENT '��Ʒ����',
  `weight` double(10,2) default NULL COMMENT 'ë��(��Ʒ����)',
  `solidity` double(10,2) default NULL COMMENT '��Ʒ���',
  `factory` varchar(60) default NULL COMMENT '��Ʒ����(�ĸ�������)',
  `storeaddress` varchar(100) default NULL COMMENT '����ַ ',
  PRIMARY KEY  (`prodec_id`),
  KEY `fk_ref_deliv_id` (`deliverybill_id`),
  CONSTRAINT `fk_ref_deliv_id` FOREIGN KEY (`deliverybill_id`) REFERENCES `deliverybill3` (`deli_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʒ��ϸ��';



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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʒ��';



-- ----------------------------

-- Table structure for producttype1

-- ----------------------------

DROP TABLE IF EXISTS `producttype1`;

CREATE TABLE `producttype1` (
  `protype_id` int(11) NOT NULL auto_increment COMMENT '��Ʒ���ID',
  `parenttype_id` int(11) default NULL COMMENT '����Ʒ���ID',
  `type_name` varchar(20) default NULL COMMENT '��Ʒ�����',
  `type_code` varchar(20) default NULL COMMENT '��Ʒ������',
  PRIMARY KEY  (`protype_id`),
  KEY `fk_ref_type` (`parenttype_id`),
  CONSTRAINT `fk_ref_type` FOREIGN KEY (`parenttype_id`) REFERENCES `producttype1` (`protype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʒ���;';



-- ----------------------------

-- Table structure for purchaser1

-- ----------------------------

DROP TABLE IF EXISTS `purchaser1`;

CREATE TABLE `purchaser1` (
  `pur_id` int(11) NOT NULL auto_increment,
  `pur_name` varchar(100) NOT NULL COMMENT '��˾����',
  `pur_password` varchar(40) NOT NULL COMMENT '��˾����',
  `pur_telephone` varchar(20) default NULL COMMENT '��˾�绰',
  `pur_province` varchar(100) default NULL COMMENT '��˾����ʡ��',
  `pur_city` varchar(20) default NULL COMMENT '��˾���ڳ���',
  `pur_address` varchar(100) default NULL,
  `pur_postalcode` varchar(10) default NULL COMMENT '�ʱ�',
  `pur_remark` varchar(200) default NULL COMMENT '��˾��ע(��ϸ��Ϣ,��Ҫ������)',
  `pur_isvendot` varchar(5) default NULL COMMENT '�Ƿ�������Ϊ����',
  PRIMARY KEY  (`pur_id`),
  UNIQUE KEY `pur_name` (`pur_name`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��ұ�;';



-- ----------------------------

-- Table structure for receiptbill1

-- ----------------------------

DROP TABLE IF EXISTS `receiptbill1`;

CREATE TABLE `receiptbill1` (
  `recei_id` int(11) NOT NULL auto_increment COMMENT '�տid',
  `orderline_id` int(11) default NULL COMMENT '������ϸID',
  `order_code` varchar(20) default NULL COMMENT '�������',
  `receiptcode` varchar(20) default NULL COMMENT '�տ���',
  `invoiceno` varchar(20) default NULL COMMENT '��Ʊ��',
  `invoicedate` varchar(20) default NULL COMMENT '��Ʊ����',
  `productcode` varchar(20) default NULL COMMENT '��Ʒ����',
  `purchasername` varchar(100) default NULL COMMENT '����',
  `amount` int(11) default NULL COMMENT '����',
  `price` double(15,2) default NULL COMMENT '���ۼ�',
  `money` double(15,2) default NULL COMMENT '���',
  `receiptdate` varchar(20) default NULL COMMENT '�տ�����',
  `salesdate` varchar(20) default NULL COMMENT '��������',
  `detailmemo` varchar(1000) default NULL COMMENT '��ϸ˵��',
  `owemoney` double(10,2) default NULL COMMENT 'Ƿ����',
  `alreadymoney` double(15,2) default NULL COMMENT '�Ѹ����',
  `termdate` varchar(20) default NULL COMMENT '������',
  `happendate` varchar(20) default NULL COMMENT '��������',
  PRIMARY KEY  (`recei_id`),
  KEY `orderline_id` (`orderline_id`),
  CONSTRAINT `receiptbill1_ibfk_1` FOREIGN KEY (`orderline_id`) REFERENCES `orderline1` (`line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='�տ';



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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='Ȩ�ޱ�';



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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��ɫ��';



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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��ɫȨ��';



-- ----------------------------

-- Table structure for specs1

-- ----------------------------

DROP TABLE IF EXISTS `specs1`;

CREATE TABLE `specs1` (
  `spec_id` int(11) NOT NULL auto_increment COMMENT '�������id',
  `product_id` int(11) default NULL COMMENT '��Ʒid',
  `spec_name` varchar(20) default NULL COMMENT '�����������',
  `spec_param` varchar(100) default NULL COMMENT '���ܲ���',
  PRIMARY KEY  (`spec_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `specs1_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product1` (`pro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='�������';



-- ----------------------------

-- Table structure for stock1

-- ----------------------------

DROP TABLE IF EXISTS `stock1`;

CREATE TABLE `stock1` (
  `sto_id` int(11) NOT NULL auto_increment COMMENT '���ID',
  `product_id` int(11) default NULL COMMENT '��ƷID',
  `store_id` int(11) default NULL COMMENT '�ֿ�ID',
  `sto_amount` decimal(8,0) default NULL COMMENT '����',
  `sto_max` decimal(8,0) default NULL COMMENT '�����',
  `sto_min` decimal(8,0) default NULL COMMENT '��С���',
  `sto_buyprice` decimal(8,0) default NULL COMMENT '������',
  `sto_sellprice` decimal(8,0) default NULL COMMENT '������',
  PRIMARY KEY  (`sto_id`),
  KEY `fk_ref_pro` (`product_id`),
  KEY `fk_ref_store` (`store_id`),
  CONSTRAINT `fk_ref_pro` FOREIGN KEY (`product_id`) REFERENCES `product1` (`pro_id`),
  CONSTRAINT `fk_ref_store` FOREIGN KEY (`store_id`) REFERENCES `storehouse1` (`stor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʒ���';



-- ----------------------------

-- Table structure for storehouse1

-- ----------------------------

DROP TABLE IF EXISTS `storehouse1`;

CREATE TABLE `storehouse1` (
  `stor_id` int(11) NOT NULL auto_increment COMMENT '�ֿ�id',
  `store_name` varchar(40) default NULL COMMENT '�ֿ�����',
  `store_charger` varchar(10) default NULL COMMENT '�ֿ⸺����',
  `store_contactphone` varchar(40) default NULL COMMENT '��ϵ�˵绰',
  `store_address` varchar(100) default NULL COMMENT '�ֿ��ַ',
  `store_memo` mediumtext COMMENT '�ֿⱸע',
  PRIMARY KEY  (`stor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='�ֿ�';



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
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ա��ɫ';



-- ----------------------------

-- Table structure for vender_credit1

-- ----------------------------

DROP TABLE IF EXISTS `vender_credit1`;

CREATE TABLE `vender_credit1` (
  `ven_cre_id` int(11) NOT NULL auto_increment COMMENT '����ID',
  `ven_id` int(11) NOT NULL COMMENT '���,����ID,һ��һ',
  `ven_cre_rank` varchar(20) default NULL COMMENT '���ü���',
  `ven_cre_time` varchar(20) default NULL COMMENT '��������',
  `ven_cre_money` double(10,2) default NULL COMMENT '���ý�� ',
  `ven_cre_balance` double(10,2) default NULL COMMENT '�������',
  `ven_cre_returnpoint` int(11) default NULL COMMENT '��������',
  PRIMARY KEY  (`ven_cre_id`),
  KEY `fk_ref_ven` (`ven_id`),
  CONSTRAINT `fk_ref_ven` FOREIGN KEY (`ven_id`) REFERENCES `vender1` (`ven_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��������';



-- ----------------------------

-- Table structure for vender1

-- ----------------------------

DROP TABLE IF EXISTS `vender1`;

CREATE TABLE `vender1` (
  `ven_id` int(11) NOT NULL auto_increment,
  `pur_id` int(11) default NULL,
  `productgroup_id` int(11) default NULL COMMENT '���,��Ʒ��ID',
  `ven_shortname` varchar(20) default NULL COMMENT '��˾����',
  `ven_shopcard` int(11) default NULL COMMENT 'Ӫҵִ��',
  `ven_fax` varchar(30) default NULL COMMENT '��˾����',
  `ven_linkman` varchar(10) default NULL COMMENT '��˾��ϵ��',
  `ven_linkmanphone` varchar(50) default NULL COMMENT '��ϵ�˵绰',
  `ven_email` varchar(100) default NULL COMMENT '��˾�����ʼ���ַ',
  `ven_status` int(11) default NULL COMMENT '-1�����δͨ��,0Ϊû����� ,1Ϊ�����ͨ��',
  PRIMARY KEY  (`ven_id`),
  UNIQUE KEY `pur_id` (`pur_id`),
  KEY `FK14B5DE5F428BFE62` (`productgroup_id`),
  KEY `FK14B5DE5FB327556` (`pur_id`),
  CONSTRAINT `FK14B5DE5F428BFE62` FOREIGN KEY (`productgroup_id`) REFERENCES `productgroup1` (`prog_id`),
  CONSTRAINT `vender1_ibfk_2` FOREIGN KEY (`pur_id`) REFERENCES `purchaser1` (`pur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='����';



-- ----------------------------

-- Records 

-- ----------------------------

INSERT INTO `admin1` VALUES ('1', 'admin', '123456', '����', '321231231', '1322222222', 'schrq@yahoo.com', '���ǹ���Ա�ı�ע', null, null, null);

INSERT INTO `bconfig` VALUES ('1', '0', '1', '1', '1', 'sitename\r\n�ɴ������̳ǹ�����վϵͳԴ����,�������߹�����վģ��,��ѵ�asp����ϵͳԴ��,����������վ��̨����', 'sitekeywords\r\n������վģ��,������ģ��,������ҳģ��,���Ϲ�����վģ��,���Ϲ���ģ��,��ѹ�����վģ��,����ϵͳģ��,����ģ������,������վģ������,����������վģ��,����ģ���ز�,���ﳵģ��,php������վģ��,���Ϲ���ϵͳ,���Ϲ���ϵͳ����,���繺��ϵͳ,���߹���ϵͳ,��ѹ���ϵͳ,�������繺��ϵͳ,asp����ϵͳ,�������Ϲ���ϵͳ,���ﳵϵͳ,asp���Ϲ���ϵͳ,�����̳ǹ���ϵͳ,���Ϲ���ϵͳ���,������ϵͳ,����ϵͳ,���Ϲ���ϵͳ����,jsp���Ϲ���ϵͳ,�����̳ǹ���ϵͳ,php����ϵͳ,������Ϲ���ϵͳ,���Ϲ���ϵͳ����ͼ,���ӹ���ϵͳ,���Ϲ���ϵͳ����,����ϵͳ����,������վϵͳ,���߹���ϵͳ����,���Ϲ������ϵͳ,����ϵͳ����,���Ϲ���ϵͳ�����,����ϵͳ,���繺��ϵͳ����,���Ϲ���ϵͳԴ��,���ӹ���ϵͳ,jsp����ϵͳ,���Ϲ���ϵͳ,�������繺��ϵͳ,���Ϲ���ϵͳԴ����,�̳ǹ���ϵͳ,���й���ϵͳ,����ϵͳģ��,net���Ϲ���ϵͳ,����ϵͳ��Ѱ�,����ϵͳԴ��,���Ϲ���ϵͳ�ƽ��,���Ϲ���ϵͳ����,���û�����ϵͳ,�������Ϲ���ϵͳ,���ɹ���ϵͳ,���Ϲ���ϵͳ ����,asp���ﳵϵͳ,java���Ϲ���ϵͳ,��� ���繺��ϵͳ,����ϵͳ���,���Ϲ���ϵͳ����,jsp���ﳵϵͳ,���Թ���ϵͳ,���Ϲ���ϵͳ�Ĺ���,����ϵͳ html,������ع���ϵͳ,�򵥹���ϵͳ,���û����Ϲ���ϵͳ,asp��ѹ���ϵͳ,���Ϲ���ϵͳ�ṹ,�Ա�����ϵͳ,���߹���ϵͳ����,asp�򵥹���ϵͳ,У԰����ϵͳ,���Ϲ���ϵͳ���ص�,asp,net ����ϵͳ,���Ϲ���ϵͳ�ķ�չ,���Ϲ���ϵͳ��չ,���Ϲ���ϵͳԴ�ļ�,������ϵͳ,���Ϲ���ϵͳ����,���Ϲ���ϵͳժҪ,���繺��ϵͳ����,����ϵͳ��������,asp��', 'sitedescription\r\n�����̳ǹ�����վ����ϵͳԴ��,����������վϵͳԴ���룬������վϵͳģ��,�̳ǹ���ϵͳģ�壬��Ѱ����Ϲ���ϵͳ��̨�������߹���ϵͳģ����ʽ�棬����ϵͳ��ҳģ�壬������վ��վ�����������������վ�����ƽ�����Ͽ��꽨վ���,���������̳ǹ�����վ���أ�����������վ,�����̳ǹ�����ӹ���ϵͳ�����Ϲ��ﳵϵͳ��asp����ϵͳ��asp�����̵꿪��ϵͳԴվ�����⹺��ϵͳ��������վƽ̨Դ�룬������վϵͳ���أ�����������ذ棬����߿Ƶ���������վϵͳ', 'http://www.pcschool.com.tw/', '�㶫ʡ�����������չ��KB����18¥�޽�IT', '412212', '����', 'wangyong31893189@163.com', '020-34506590,34700400', '406897011,515961153,781803766', '��վ�ͷ�,��վ�ͷ�,��վ�ͷ�', '1', '1', '1', '2', 'qqmsg_off\r\n��������ѽ', '������������ѽ', 'wangyong31893189@126.com', 'adm_icp\r\nICP�����ǼǺ�0505012001', '�޽�IT', '13760746489', '13760746489', '1', '1', '33', '��ͨ��Ա', '10', '�߼���Ա', '9.5', '��ʯ��Ա', '9', null, '10', null, '10', null, '10', '16', '5', '12', '10', '1', '1', '\"��Ʒ׼����\"', '\"�۸�����ѯ\"', '\"���ȵ�½\"', '77CC00', '0066ff', '1', '1', '1', '0', '1', '1', '1', 'topmenu\r\n\"<P><A href=\"\"class.jsp?LarCode=����-�ٿ���\"\"></A>&nbsp;</P>\"', '1', '1', 'blue', '��������', '��ͨ�ʼ�', '�ؿ�ר��', '��ͨ����', '0', '0', '0', '5', '15', '15', '0', '0', '2000', 'mianyoufei_msg\r\n������2000Ԫ���ʷ�', '�̳�����', '������', '��ҵ��Ϣ', null, null, '1', 'guanbi\r\n��վ����ά�������Ժ���ʣ����������Ĳ��㣬���½⡣', 'reg_xieyi\r\n������ӭ��ע��Ϊ��վ��Ա�����ڷ��ʱ�վ������̳��ʱ�������Ծ������������<BR><BR>һ���������ñ�վΣ�����Ұ�ȫ��й¶�������ܣ������ַ�������Ἧ��ĺ͹���ĺϷ�Ȩ�棬�������ñ�վ���������ƺʹ���������Ϣ��&nbsp;<BR>������һ��ɿ�����ܡ��ƻ��ܷ��ͷ��ɡ���������ʵʩ�ģ�<BR>����������ɿ���߸�������Ȩ���Ʒ���������ƶȵģ�<BR>����������ɿ�����ѹ��ҡ��ƻ�����ͳһ�ģ�<BR>�������ģ�ɿ�������ޡ��������ӣ��ƻ������Ž�ģ�<BR>�������壩�������������ʵ��ɢ��ҥ�ԣ������������ģ�<BR>��������������⽨���š����ࡢɫ�顢�Ĳ�����������ɱ���ֲ�����������ģ�<BR>�������ߣ���Ȼ�������˻���������ʵ�̰����˵ģ����߽����������⹥���ģ�<BR>�������ˣ��𺦹��һ��������ģ�<BR>�������ţ�����Υ���ܷ��ͷ�����������ģ�<BR>������ʮ��������ҵ�����Ϊ�ġ�<BR><BR>�����������أ����Լ������ۺ���Ϊ����<BR>', '0', null, '0', null, '0');

INSERT INTO `brand1` VALUES ('1', '����', '�ҵ�-����-CRT����/Һ��', 'images/brands/Button_pp_amd.jpg');

INSERT INTO `brand1` VALUES ('2', '����', '�ҵ�-����-CRT����', 'images/brands/Button_pp_apple.jpg');

INSERT INTO `brand1` VALUES ('3', '����', '�ҵ�-����-CRT����/Һ��', 'images/brands/Button_pp_cre.jpg');

INSERT INTO `brand1` VALUES ('4', 'JVC', '�ҵ�-����-�����ӵ���', 'images/brands/Button_pp_DLINK.jpg');

INSERT INTO `brand1` VALUES ('5', '����', '�ҵ�-����-�����ӵ���', 'images/brands/Button_pp_hair.jpg');

INSERT INTO `brand1` VALUES ('6', '����', '�ҵ�-����-Һ������', 'images/brands/Button_pp_hp.jpg');

INSERT INTO `brand1` VALUES ('7', 'JBL', '�ҵ�-����-CRT����', 'images/brands/Button_pp_joyoung.jpg');

INSERT INTO `brand1` VALUES ('8', 'Nakamichi', '�ҵ�-����-CRT����', 'images/brands/Button_pp_motorola.jpg');

INSERT INTO `brand1` VALUES ('9', 'Sony', '�ҵ�-����-CRT����', 'images/brands/Button_pp_netac.jpg');

INSERT INTO `brand1` VALUES ('10', '����', '�ҵ�-����-CRT����', 'images/brands/Button_pp_nokia.jpg');

INSERT INTO `brand1` VALUES ('11', '����', '�ҵ�-����-mini����', 'images/brands/Button_pp_philips.jpg');

INSERT INTO `deliverybill1` VALUES ('1', '1', '1001', '20', 'gjun', '43021', '10023', '�㶫ʡ', '������', '�㶫ʡ�����������', 'wy', '����', '����1�Ųֿ�', 'Ҫ����', '����ǩ��', '1����', '�ֱ������֤', 'gjun', '�ֻ�', '13760746489');

INSERT INTO `deliverybill1` VALUES ('2', '2', '1001', '20', 'gjun', '43021', '10023', '�㶫ʡ', '������', '�㶫ʡ�����������', 'wy', '����', '����1�Ųֿ�', 'Ҫ����', '����ǩ��', '1����', '�ֱ������֤', 'gjun', '�ֻ�', '13760746489');

INSERT INTO `deliverybill1` VALUES ('3', '3', '1001', '20', 'gjun', '43021', '10023', '�㶫ʡ', '������', '�㶫ʡ�����������', 'wy', '����', '����1�Ųֿ�', 'Ҫ����', '����ǩ��', '1����', '�ֱ������֤', 'gjun', '�ֻ�', '13760746489');

INSERT INTO `deliverybill1` VALUES ('4', '4', '1001', '20', 'gjun', '43021', '10023', '�㶫ʡ', '������', '�㶫ʡ�����������', 'wy', '����', '����1�Ųֿ�', 'Ҫ����', '����ǩ��', '1����', '�ֱ������֤', 'gjun', '�ֻ�', '13760746489');

INSERT INTO `deliverybill1` VALUES ('5', '5', '1001', '20', 'gjun', '43021', '10023', '�㶫ʡ', '������', '�㶫ʡ�����������', 'wy', '����', '����1�Ųֿ�', 'Ҫ����', '����ǩ��', '1����', '�ֱ������֤', 'gjun', '�ֻ�', '13760746489');

INSERT INTO `image1` VALUES ('1', '1', 'IBMA3108�ʼǱ�', '/computer/notebook/IBM/IBMA3108.jpg', '����ͼƬ˵��');

INSERT INTO `order1` VALUES ('1', '1000', '���ݾ޽����Զ���', 'gjun', '��������', '2009-1-2', '��������ͳһ��Ʊ', '���ݾ޽���˾', '���ݾ޽���˾', '2009-12-12', 'gjun2009', '���Ǳ�ע', 'yes', '15919191919', '1322222222', '2009-01-02 00:00:00');

INSERT INTO `order1` VALUES ('2', '1001', 'kuaisu', 'gjun ', '��������', '2009-5-20', '��ֵ˰ר�÷�Ʊ', 'gjun', '����', '2009-5-20', 'dsfsfsdf', 'sadasdada', 'yes', '13760746489', '15874963512', '2009-04-13 11:05:49');

INSERT INTO `order1` VALUES ('3', '1001', 'kuaisu', 'gjun', '��������', '2009-5-20', '��ֵ˰ר�÷�Ʊ', 'gjun', '����', '2009-5-20', 'dsfsfsdf', 'sadasdada', 'yes', '13760746489', '15874963512', '2009-04-13 11:06:11');

INSERT INTO `order1` VALUES ('4', '1002', 'kuaisuwe', 'gjun', '��������', '2009-5-20', '��ֵ˰ר�÷�Ʊ', 'gjun', '����', '2009-5-20', 'dsfsfsdf', 'dasfsdf', 'yes', '13760746489', '15874963512', '2009-04-13 11:57:56');

INSERT INTO `order1` VALUES ('5', '1002', 'kuaisuwe', 'gjun', '��������', '2009-5-20', '��ֵ˰ר�÷�Ʊ', 'gjun', '����', '2009-5-20', 'dsfsfsdf', 'dasfsdf', 'yes', '13760746489', '15874963512', '2009-04-13 12:00:08');

INSERT INTO `order1` VALUES ('7', '1004', 'wy12', '123456', '��������', '2009-4-15', '��ֵ˰ר�÷�Ʊ', '123456', 'gz', '2009-4-30', '1231333', 'sdfsfs', 'yes', '13760746489', '', '2009-04-15 14:08:26');

INSERT INTO `order1` VALUES ('10', '1005', 'wer', '���ݾ޽�', '��������', '2009-4-16', '��ֵ˰ר�÷�Ʊ', '���ݾ޽�', '���ݾ޽�', '2009-4-30', '12313', 'sdfsf', 'yes', '13789456781', '', '2009-04-16 21:34:59');

INSERT INTO `order1` VALUES ('11', '1006', 'sd', 'liushan2', '��������', '2009-4-17', '��ֵ˰ר�÷�Ʊ', 'liushan2', 'ds', '2009-4-17', 'ewrew', 'sdfds', 'yes', '13789456781', '', '2009-04-17 01:02:40');

INSERT INTO `order1` VALUES ('12', '1007', 'kuansu', 'gjunna', '��������', '2099-4-19', '��ֵ˰ר�÷�Ʊ', 'gjunna', 'gz', '2099-4-30', '1231312', 'ksjfks', 'yes', '13789456781', '', '2099-04-19 15:06:41');

INSERT INTO `orderline1` VALUES ('1', '1', '5', '1', '���������Թ�˾', '123456', '��������', '200.00', '�����');

INSERT INTO `orderline1` VALUES ('2', '1', '8', '1', '���������Թ�˾', '123456', '������ɽ', '400.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('3', '1', '4', '1', '���������Թ�˾', '123456', '��������', '200.00', '�����');

INSERT INTO `orderline1` VALUES ('4', '1', '6', '1', '���������Թ�˾', '123456', '��������', '200.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('5', '2', '11', '56', '���������Թ�˾', '123456', '��������', '200.00', '�����');

INSERT INTO `orderline1` VALUES ('6', '1', '15', '23', '���������Թ�˾', '123456', '��������', '200.00', '�����');

INSERT INTO `orderline1` VALUES ('7', '1', '18', '12', '���������Թ�˾', '123456', '������ɽ', '400.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('8', '3', '21', '32', '���������Թ�˾', '123456', '��������', '200.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('9', '1', '1', '11', '���������Թ�˾', '123456', '��������', '200.00', '�����');

INSERT INTO `orderline1` VALUES ('10', '1', '2', '13', '���������Թ�˾', '123456', '��������', '200.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('11', '3', '4', '21', '���������Թ�˾', '123456', '������ɽ', '400.00', '�����');

INSERT INTO `orderline1` VALUES ('12', '4', '23', '1', '���������Թ�˾', '123456', '��������', '200.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('14', '7', '2', '5', '���ҹ�˾���1', '2', '����������2', '249995.00', '�����');

INSERT INTO `orderline1` VALUES ('15', '7', '3', '1', '���ҹ�˾���1', '3', '����������3', '46800.00', '���ͨ��');

INSERT INTO `orderline1` VALUES ('22', '10', '2', '1', '���ҹ�˾���1', '2', '����������2', '49999.00', '�����');

INSERT INTO `orderline1` VALUES ('23', '11', '3', '1', '���ҹ�˾���1', '3', '����������3', '46800.00', '�����');

INSERT INTO `orderline1` VALUES ('24', '12', '8', '5', '���ҹ�˾���1', '8', '����������', '175495.00', '�����');

INSERT INTO `payway1` VALUES ('1', '��������');

INSERT INTO `payway1` VALUES ('2', '���ڸ���');

INSERT INTO `payway1` VALUES ('3', 'Ƿ���');

INSERT INTO `product1` VALUES ('1', 'JBL 4348��������', '91db', '7', '8', '1', '4654.50', '̨', 'images/products/y2.JPG', '', '');

INSERT INTO `product1` VALUES ('2', 'Nakamichi(�е�����) Sonudspace12 (3������)SS-12', '0158', '8', '9', '1', '49999.00', '̨', 'images/products/y3.JPG', '', '');

INSERT INTO `product1` VALUES ('3', 'VICTORʤ����������', 'M800', '9', '10', '1', '46800.00', '̨', 'images/products/y4.JPG', '���ܳ�ǿ', '���Ͻ����,HIFI������ϵͳ,ȫϵͳ��ȫ��ѹ����,ʮ��������������,������Դͬʱ����,ʮ���ֶ�̬��ʾģʽ,��̬ө����Ӣ����ʾ����,��ʮ��·�߱��������֧�ֶ�·��չ,U��Mp3 �����������ѡ����ѭ�����ţ�֧��Ӳ�̲��ţ�');

INSERT INTO `product1` VALUES ('4', 'BOSE���� ����48IV��ͥӰԺ����', '48IV', '9', '10', '1', '45800.00', '̨', 'images/products/y5.JPG', 'ʵ��չ��', '���ʽ����,ǰ�ù��ʣ�100w����,�迹��8ŷķ,�����ȣ�91db������');

INSERT INTO `product1` VALUES ('5', '����HiViF4i', 'F4HT', '10', '11', '1', '39000.00', '̨', 'images/products/y6.JPG', '���ּ�ͥӰԺ�������� �������� ������Ͼ', 'ϵͳ��ʽ �� ��·�Ľ׵���ʽ����ϵͳ,������� �� ʵľľ��/�������ٿ���,����(����/ֻ) �� 71.5kg ,');

INSERT INTO `product1` VALUES ('6', 'Nakamichi(�е�����) Sonudspace21(5������)SS-21', '0152', '8', '9', '1', '45654.00', '̨', 'images/products/y1.JPG', '', '');

INSERT INTO `product1` VALUES ('7', 'JBLBG���ʽ����', '800AY', '7', '8', '1', '38000.50', '̨', 'images/products/y7.JPG', '���ص����������Լ����� 4 ����ѧת���ͳ�����ĵ�����Ķ��ط�Ƶϵͳ�������ʽ������ϵͳ������ʵ��ӰԺЧ��', '���ʣ�100w����,�迹��8ŷķ,�����ȣ�86-90db');

INSERT INTO `product1` VALUES ('8', '����HiViF2.2', 'HTifi', '10', '11', '1', '35099.00', '̨', 'images/products/y8.JPG', '��̬��ѹ���,�������ȿ�Ƶ���,��ȫ�����������,���Գ��ױ��ⵥԪ��ĵ��Ƹ�������ط����ֵĽ�����', '�������������������ƣ�ʱ�и߹������޲�������HiVi��������ʦ��ʱ������Ӱ����׷��������ڵ��������������������ϵ�ǿ����ʵ��');

INSERT INTO `product1` VALUES ('9', 'DENON(����)', 'CX3', '8', '9', '1', '268000.00', '̨', 'images/products/y9.JPG', '��Ʒ��������졾����л� �̼ұ��ޡ�', '���ܣ�CD����,������2.0����');

INSERT INTO `product1` VALUES ('10', 'BOSE���ʽ����', '901VI', '9', '10', '1', '157000.00', '̨', 'images/products/y10.JPG', '', '���ͣ�ǰ��,���ʣ�100w����,�迹��8ŷķ,�����ȣ�86-90db');

INSERT INTO `product1` VALUES ('11', 'JAMO/������', 'C807', '9', '10', '1', '146000.00', '̨', 'images/products/y11.JPG', 'ȫ���л���ʵ���', '���ͣ�ǰ��,���ʣ�100w����,�迹��8ŷķ,�����ȣ�86-90db');

INSERT INTO `product1` VALUES ('12', 'SONY�����ͥӰԺ', 'DAV-LF1H', '9', '10', '1', '154000.00', '̨', 'images/products/y12.JPG', '����������� ȫ������ ����ֱ��', '������5.1����');

INSERT INTO `product1` VALUES ('13', '����HiVihifi��ͥӰԺ��������', 'Diva6.2HT', '10', '11', '1', '13808.00', '̨', 'images/products/y13.JPG', '��ͥӰԺ', '��Ԫ���� �� �� �� �� YT8N��2,ʧ �� �� �� 47Hz �� 20kHz ��1%(2.83V/1m) ,�����ȣ�2.83V/m�� �� 90dB,���ʷ�Χ �� 10 �� 200W,������� �� ������Ȼ��֦ľƤ���ɫ���ٿ���,�� �� �� 35 kg ');

INSERT INTO `product1` VALUES ('14', 'JBL����', 'E90', '7', '8', '1', '12000.00', '̨', 'images/products/y14.JPG', 'ȫ����Ʒ��ʵ�����', '������5.1����,������ۣ��ŵ�/����ʽ����,����DVD���Ż�������DVD��');

INSERT INTO `product1` VALUES ('15', 'SCCH', 'SC-HT901W', '9', '10', '1', '9880.00', '̨', 'images/products/y15.JPG', '', '�������');

INSERT INTO `product1` VALUES ('16', 'JBL���ʽ����', 'L890', '7', '8', '1', '9200.00', '̨', 'images/products/y16.JPG', '', '���ͣ�ǰ��,���ʣ�100w����,�迹��8ŷķ,�����ȣ�86-90db');

INSERT INTO `product1` VALUES ('17', 'JVC', 'HD-Z70RF7', '4', '6', '2', '29999.00', '̨', 'images/products/d1.JPG', '', '�¿�70�糬���辧������ӻ�');

INSERT INTO `product1` VALUES ('18', '����Һ�����ӻ�', 'LA52A650A1R', '3', '6', '2', '19999.00', '̨', 'images/products/d2.JPG', 'ͼ������:����������ȻӰ����DNIe��Digital Natural Image engine��,100Hz ���ܶ��м���,��Ƶ���� SRS TruSurround XT,��Ļ�ߴ� 52Ӣ��,��Ļ���� 16:9 ,�ֱ��� 1920��1080 ,��Ӧʱ�� 0ms,��Ļ���� Ultra Clear Panel ��ˮ�����������,�������� 34.8kg����������', '��Ʒ�л�����Ʊȫ������');

INSERT INTO `product1` VALUES ('19', '����ר��-����Һ�����ӻ�', 'HD-Z70RF7', '1', '6', '2', '45654.00', '̨', 'images/products/d3.JPG', '��Ļ�ߴ磺47Ӣ��,��Ļ����������16:9,Һ���۸����䣺15000-20000Ԫ,��Ӧ�ٶȣ�3-6����(��3����),�ֱ��ʣ�1920��1080�������', 'ȫ������');

INSERT INTO `product1` VALUES ('20', '���չ���Һ�����ӻ�', 'LCD-46F63', '6', '6', '2', '12900.00', '̨', 'images/products/d4.JPG', '��Ļ�ߴ磺46Ӣ��,��Ļ����������16:9,Һ���۸����䣺10000-15000Ԫ,�����ȣ�1080(ȫ����),��Ӧ�ٶȣ�6-8����(��6����),�ֱ��ʣ�1920��1080,��������ӿڣ�hdmi�ӿ�', 'ȫ������ �������� �Ż� ��Ʒ');

INSERT INTO `product1` VALUES ('21', '����ר��-����Һ�����ӻ�', 'L42R1', '1', '6', '2', '9999.00', '̨', 'images/products/d5.JPG', '��Ļ�ߴ磺42Ӣ��,��Ļ����������16:9,Һ���۸����䣺8000-10000Ԫ,�����ȣ�1080(ȫ����),��Ӧ�ٶȣ�3-6����(��3����),�ֱ��ʣ�1366��768', '');

INSERT INTO `product1` VALUES ('22', '���µ����ӵ��ӻ�', 'TH-50PV80C', '5', '6', '2', '9000.00', '̨', 'images/products/d6.JPG', '����  PDP������,����  1,049,088(1,366x768)����,��Ļ  G11�Է�����Ļ,������/������  V-real 3,����ߴ�  50\"(127cm)�Խ���,������֧�� 1210x790.5x95mm,����֧�� 1210x844x387mm', '');

INSERT INTO `product1` VALUES ('23', '���ּҵ���������Һ�����ӻ�', 'LA40A350C1', '3', '6', '2', '6500.00', '̨', 'images/products/d7.JPG', '��Ļ�ߴ磺40Ӣ��,��Ļ����������16:9,Һ���۸����䣺6000-8000Ԫ,�����ȣ�720p(׼����),��Ӧ�ٶȣ�3-6����(��3����),�ֱ��ʣ�1366��768', 'ȫ������');

INSERT INTO `product1` VALUES ('24', '����Һ�����ӻ�', 'TLM4236P', '6', '6', '2', '4990.00', '̨', 'images/products/d8.JPG', 'Һ�����ӳߴ� 42��,���� 16:9,�ֱ��� 1920��1080,���� 800cd/�O,�Աȶ� 8000:1,��Ӧʱ�� 4ms,���ӽǶ� 178��/178��', '���淢Ʊ ȫ������ ɽ��̩��ר��');

INSERT INTO `product1` VALUES ('25', '100%������47��Һ�����ӻ�', '100G', '5', '6', '2', '4670.00', '̨', 'images/products/d9.JPG', '��Ļ�ߴ磺47Ӣ��,��Ļ����������16:9,Һ���۸����䣺4000-6000Ԫ,�����ȣ�1080(ȫ����),��Ӧ�ٶȣ�8-16����(��8����),�ֱ��ʣ�1280��1024,��������ӿڣ�av�ӿ�', '��������');

INSERT INTO `product1` VALUES ('26', '������� ���ӻ���', 'Tord Bj?rklund', '5', '6', '2', '1799.00', '̨', 'images/products/d10.JPG', '���: 185 ����,���: 39 ����,�߶�: 185 ����,��������: 13 ����', '��Ǭ�����������ʹ�/�˼Ҵ�����');

INSERT INTO `product1` VALUES ('27', '�������', 'CHD29155', '6', '6', '2', '1599.00', '̨', 'images/products/d11.JPG', '���أ� Լ66kg,�ߴ磺 ��917����700����565mm,��ɫ�� �����/�����ֹ�', '����о���� �������');

INSERT INTO `product1` VALUES ('28', '����29�紿ƽ���ӻ�', 'PF29399', '6', '6', '2', '1198.00', '̨', 'images/products/d12.JPG', '���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ƽ,��������ӿڣ�av�ӿ�', '');

INSERT INTO `product1` VALUES ('29', '����ר��-������ƽ��ɫ���ӻ�', '21FA11-AM', '1', '6', '2', '1099.00', '̨', 'images/products/d13.JPG', '���ӳߴ磺21Ӣ��,��Ļ������4:3,��������ܣ���ƽ', '');

INSERT INTO `product1` VALUES ('30', 'ȫ��22��Һ�����ӻ�', 'XX', '6', '6', '2', '860.00', '̨', 'images/products/d14.JPG', '��Ļ�ߴ磺22Ӣ��,��Ļ����������16:10,Һ���۸����䣺4000Ԫ����,�����ȣ�720p(׼����),��Ӧ�ٶȣ�3-6����(��3����),�ֱ��ʣ�1680��1050,��������ӿڣ�av�ӿ�', '��������');

INSERT INTO `product1` VALUES ('31', '����29�����ָ�����ӻ�', '8C9', '3', '6', '2', '859.00', '̨', 'images/products/d15.JPG', '���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�', '�����۲�����');

INSERT INTO `product1` VALUES ('32', '�߹��й���', '8B9', '3', '6', '2', '859.00', '̨', 'images/products/d16.JPG', '���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�', '����29�����ָ������۲������ӻ�');

INSERT INTO `product1` VALUES ('33', '�����ʵ�', 'HD-Z70RF7', '1', '5', '2', '2399.00', '̨', 'images/products/cd1.JPG', '�������ָ����������,DSM+�����о����,���ָ����ʽȫ����,��㾧��ɨ��,������ǿ����', 'ͼ����ʽ PAL��NTSC;������ʽ DK,I,B/G,M;��������(W) 180W ;��ѹ��Χ(V) 160-250V ;���� AV����*2��AV�����DVD�������롢S���ӡ�������ڡ�Ypbpr+��Ƶ��VGA+��Ƶ��HDMI ;���� ˵���顢ң���������*2����Դ��;��*��*��(������)mm 800*444*596.5 ');

INSERT INTO `product1` VALUES ('34', '����', 'D29FT1A', '1', '5', '2', '1999.00', '̨', 'images/products/cd2.JPG', '', '���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ƽ');

INSERT INTO `product1` VALUES ('35', '����', 'D29FA12-AKM', '1', '5', '2', '1999.00', '̨', 'images/products/cd3.JPG', '', '���ӳߴ磺29Ӣ����Ļ������4:3��������ܣ���ƽ');

INSERT INTO `product1` VALUES ('36', '������ƽ���ָ������', 'D29FK1', '1', '5', '2', '1859.00', '̨', 'image/products/cd4.JPG', '', '���ӳߴ磺29Ӣ����Ļ������4:3��������ܣ���ƽ');

INSERT INTO `product1` VALUES ('37', '����', '29FA12-AM', '1', '5', '2', '1799.00', '̨', 'images/products/cd5.JPG', '����� �������������,ϵͳ ͼ����ʽ PAL ��NTSC,�洢Ƶ������ 218', '���ӳߴ磺29Ӣ����Ļ������4:3��������ܣ���ƽ');

INSERT INTO `product1` VALUES ('38', '��Ӧŷ��SCART���ӵ��ӻ�', 'SJHU', '2', '5', '2', '29999.00', '̨', 'images/products/k1.JPG', '', 'Ʒ�ƣ�����/KONKACRT,���ӳߴ磺21Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('39', '���Ѳ�ɫ���ӻ�', 'SCART', '2', '5', '2', '799.00', '̨', 'images/products/k2.JPG', '', '���ޣ��� ,��ţ�b72698291d4324100b618f6b ');

INSERT INTO `product1` VALUES ('40', '���Ѳ�ɫ���ӻ�', 'SCARTB', '2', '5', '2', '786.00', '̨', 'images/products/k3.JPG', '', 'Ʒ�ƣ�����/KONKACRT,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('41', '�������ֵ��ӻ����ֲʵ�25�翵�ѳ�ƽ�˳�������ͻ�', '2H', '2', '5', '2', '540.00', '̨', 'images/products/k4.JPG', '', 'Ʒ�ƣ�����/KONKACRT,���ӳߴ磺25Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('42', '����29������ƽ��������ɫ���ӻ�', '29SXC', '3', '5', '2', '950.00', '̨', 'images/products/s1.JPG', '����Һ����ʽ', 'Ʒ�ƣ�����/SAMSUNGCRT,���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('43', '����29�����ָ������۲������ӻ�', '28KL', '3', '5', '2', '856.00', '̨', 'images/products/s2.JPG', '�߹��й���', 'Ʒ�ƣ�����/SAMSUNGCRT,���ӳߴ磺29Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('44', '����25�����ָ�����ӻ�', 'SOPA', '3', '5', '2', '699.00', '̨', 'images/products/s3.JPG', '���۲���', 'Ʒ�ƣ�����/SAMSUNGCRT,���ӳߴ磺25Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('45', '����25�����ָ�����ӻ�', 'SCN', '3', '5', '2', '669.00', '̨', 'images/products/s4.JPG', '��Ʒ�ڽ��', '����/SAMSUNGCRT,���ӳߴ磺25Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�av�ӿ�');

INSERT INTO `product1` VALUES ('46', '����25��ƽ������������ɫ���ӻ�', 'SDHJ', '3', '5', '2', '530.00', '̨', 'images/products/s5.JPG', '���ڿ�ʽ', 'Ʒ�ƣ�����/SAMSUNGCRT,���ӳߴ磺25Ӣ��,��Ļ������4:3,��������ܣ���ͨ,��������ӿڣ�tv�ӿ�');

INSERT INTO `product1` VALUES ('47', '���14��ʿǲ�ɫ������ӻ�', 'XINYU', '11', '7', '2', '378.00', '̨', 'images/products/m1.JPG', '��ɫ���ӻ��ػ�378', '���ޣ���, ��ţ�10cbbdeadea9dad12dbc280c');

INSERT INTO `product1` VALUES ('48', '����9�紿ƽ�±�Ͷ�ڰ�����С����', 'MWCX', '11', '7', '2', '124.00', '̨', 'images/products/m2.JPG', '9��������ȶ��л���,���AV��� �ɽ�DVD�Ȳ�����,��Ӣ����ѧϰ������,����OK����·���Ӽ��Ӽ���,��ֱ�����õ�Դ,ŷʽ���������ƣ��������Χ���ʺ��ִ��Ҿ�,˫�����������������', '');

INSERT INTO `product1` VALUES ('49', '����8�紿ƽ�±�Ͷ�ڰ�����С����', 'MMCX', '11', '7', '2', '108.00', '̨', 'images/products/m3.JPG', '�������ȶ��л���,���AV��ڣ��ɽ�DVD����Ϸ��������豸,��Ӣ����ѧϰ������,����OK����·���Ӽ��Ӽ���,������Դ,��������������,ȫƵ�����ӽ�Ŀ���չ���', 'ֽ����(mm):46.7��L����45.7��W����53.5��H��,2272 pcs / 20-ft,5088 pcs / 40-ft HQ');

INSERT INTO `product1` VALUES ('50', '7�紿ƽ�ڰ�����С����', 'WF-727', '11', '7', '2', '105.00', '̨', 'images/products/m4.JPG', '�������ȶ��л���,���AV���,��Ӣ����ѧϰ������,����OK����·���Ӽ��Ӽ���,��ֱ�����õ�Դ,�ɵ�ʽת�����,ȫƵ�����ӽ�Ŀ���չ���', '');

INSERT INTO `productdesc1` VALUES ('1', '1', '��Ʒ����', 'IBM�ʼǱ�', '300', '320.00', '323.00', '��ݸ����', '���ر���');

INSERT INTO `productgroup1` VALUES ('1', 'YX', '����', '�ҵ�-����', null, '1');

INSERT INTO `productgroup1` VALUES ('2', 'DS', '����', '�ҵ�-����', null, '1');

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

INSERT INTO `producttype1` VALUES ('1', null, '�ҵ�', 'JD');

INSERT INTO `producttype1` VALUES ('2', null, '����', 'SM');

INSERT INTO `producttype1` VALUES ('3', '1', '����', 'JDYX');

INSERT INTO `producttype1` VALUES ('4', '2', '����', 'JDDS');

INSERT INTO `producttype1` VALUES ('5', '4', 'CRT����', 'CRTDS');

INSERT INTO `producttype1` VALUES ('6', '4', '������Һ������', 'DLZYJDS');

INSERT INTO `producttype1` VALUES ('7', '4', '����Һ������', 'MNDS');

INSERT INTO `producttype1` VALUES ('8', '3', 'JBL����', 'JYX');

INSERT INTO `producttype1` VALUES ('9', '3', 'Nakamichi����', 'NYX');

INSERT INTO `producttype1` VALUES ('10', '3', 'Sony����', 'SYX');

INSERT INTO `producttype1` VALUES ('11', '3', '��������', 'WYX');

INSERT INTO `purchaser1` VALUES ('1', 'gjun', '123456', '020-1111111', '�㶫', '����', '�������ʯ����·8��', '510630', '��˾��ע', 'yes');

INSERT INTO `purchaser1` VALUES ('3', 'wangyong', '123456', '020-1111111', '�㶫', '����', '�������ʯ����·8��', '510630', '��˾��ע', 'yes');

INSERT INTO `purchaser1` VALUES ('4', 'liushan', '123456', '020-1111111', '�㶫', '����', '�������ʯ����·8��', '510630', '��˾��ע', 'yes');

INSERT INTO `purchaser1` VALUES ('13', '���ݾ޽�IT', '1234567', '0733-3189449', '220000', '220200', '������', '412212', '�����', 'no');

INSERT INTO `purchaser1` VALUES ('14', '1232131', '123456', '0733-3189449', '210000', '210300', '������', '412212', 'ksdjfkdsjfsk', 'no');

INSERT INTO `purchaser1` VALUES ('17', 'leijun12', '123456', '0733-3189449', '130000', '-1', 'sdfdsfa', '412212', 'sdfsdfdsf', 'yes');

INSERT INTO `receiptbill1` VALUES ('1', '1', '544444', '12312', '20003', '2009-6-6', 'A7333', '���������Թ�˾', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '��ϸ˵��', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('2', '2', '234234', '324324', '435354', '2009-5-6', 'B33434', '���ݵ��Թ�˾', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '��ϸ˵��', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('3', '3', '234234', '324324', '435354', '2009-5-6', 'B33434', '���ݵ��Թ�˾', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '��ϸ˵��', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('4', '4', '544444', '12312', '20003', '2009-6-6', 'A7333', '���������Թ�˾', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '��ϸ˵��', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('5', '5', '544544', '12352', '20053', '2009-6-6', 'A7333', '���������Թ�˾', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '��ϸ˵��', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('6', '6', '234234', '324324', '435354', '2009-5-6', 'B33434', '���ݵ��Թ�˾', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '��ϸ˵��', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('7', '7', '544444', '12312', '20003', '2009-6-6', 'A7333', '���������Թ�˾', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '��ϸ˵��', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('8', '8', '544544', '12352', '20053', '2009-6-6', 'A7333', '���������Թ�˾', '200', '300.00', '10000.00', '2009-12-12', '2009-2-2', '��ϸ˵��', '900000.00', '1000.00', '2010-2-2', '2009-2-2');

INSERT INTO `receiptbill1` VALUES ('9', '9', '234234', '324324', '435354', '2009-5-6', 'B33434', '���ݵ��Թ�˾', '300', '4000.00', '1200000.00', '2009-9-6', '2009-4-5', '��ϸ˵��', '450000.00', '2000.00', '2009-9-8', '2009-5-6');

INSERT INTO `receiptbill1` VALUES ('10', '24', null, null, null, null, null, null, null, '0.00', '0.00', null, null, null, null, null, null, null);

INSERT INTO `specs1` VALUES ('1', '1', '�������', '���ܲ���');

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

INSERT INTO `storehouse1` VALUES ('1', '����12�ֿ�', '�ֿ⸺��������', '132999999999', '����������', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('2', '����13�ֿ�', '�ֿ⸺��������', '132999999999', '����������2', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('3', '����15�ֿ�', '�ֿ⸺��������', '132999999999', '����������3', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('4', '����16�ֿ�', '�ֿ⸺��������', '132999999999', '����������4', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('5', '����17�ֿ�', '�ֿ⸺��������', '132999999999', '����������5', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('6', '����18�ֿ�', '�ֿ⸺��������', '132999999999', '����������6', '�ֿⱸע');

INSERT INTO `storehouse1` VALUES ('7', '����10�ֿ�', '�ֿ⸺��������', '132999999999', '����������7', '�ֿⱸע');

INSERT INTO `vender_credit1` VALUES ('1', '1', '���ü���5', '��������2009-12-12', '5000000.00', '3333.00', '200');

INSERT INTO `vender1` VALUES ('1', '1', '1', '���ҹ�˾���1', '7676767', '010-13222222', '����', '13222222222', 'bj@yahoo.com', '-1');

INSERT INTO `vender1` VALUES ('8', '3', '15', '1231', '342423', 'wrewrwr', '1231231', '12312313', '3324324234', '-1');

INSERT INTO `vender1` VALUES ('9', '4', '3', 'gz', '12313', '3189449', 'sdfjsf', '13760746489', 'wangyong31893189@163.com', '0');

INSERT INTO `vender1` VALUES ('10', '13', '16', 'gjun', '123213123', '0733-3189449', 'jdskf', '13760746489', 'wangyong31893189@163.com', '-1');

INSERT INTO `vender1` VALUES ('11', '14', '17', 'gjun', '123213123', '0733-3189449', 'sdfjsf', '13760746489', 'liushan@qq.com', '-1');

INSERT INTO `vender1` VALUES ('12', '17', '26', 'wang', '123123', '0733-3189449', 'sdfkafka', '13760746489', 'wangyong31893189@163.com', '0');

