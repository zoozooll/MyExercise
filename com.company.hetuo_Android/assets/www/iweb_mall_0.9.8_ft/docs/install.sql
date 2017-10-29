
/*Table structure for table `imall_admin_group` */

DROP TABLE IF EXISTS `imall_admin_group`;

CREATE TABLE `imall_admin_group` (
  `id` tinyint(4) NOT NULL auto_increment,
  `group_name` varchar(255) NOT NULL default '',
  `del_flg` tinyint(4) default '0',
  `rights` text,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_admin_log` */

DROP TABLE IF EXISTS `imall_admin_log`;

CREATE TABLE `imall_admin_log` (
  `log_id` int(10) unsigned NOT NULL auto_increment COMMENT '日志id',
  `log_time` datetime default NULL COMMENT '日志时间',
  `admin_id` tinyint(3) unsigned NOT NULL default '0' COMMENT '管理员id',
  `log_info` varchar(255) NOT NULL default '' COMMENT '日志内容',
  `ip_address` varchar(15) NOT NULL default '' COMMENT '记录ip',
  PRIMARY KEY  (`log_id`),
  KEY `user_id` (`admin_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_admin_user` */

DROP TABLE IF EXISTS `imall_admin_user`;

CREATE TABLE `imall_admin_user` (
  `admin_id` smallint(5) unsigned NOT NULL auto_increment COMMENT '管理员id',
  `admin_name` varchar(60) NOT NULL default '' COMMENT '管理员用户名',
  `admin_email` varchar(60) NOT NULL default '' COMMENT '管理员email',
  `admin_password` varchar(32) NOT NULL default '' COMMENT '管理员密码',
  `add_time` datetime default NULL COMMENT '添加时间',
  `last_login` datetime default NULL COMMENT '最后登陆时间',
  `last_ip` varchar(15) NOT NULL default '' COMMENT '最后登陆ip',
  `group_id` tinyint(4) default NULL COMMENT '管理组',
  PRIMARY KEY  (`admin_id`),
  UNIQUE KEY `admin_name` (`admin_name`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_areas` */

DROP TABLE IF EXISTS `imall_areas`;

CREATE TABLE `imall_areas` (
  `area_id` smallint(6) unsigned NOT NULL auto_increment COMMENT '地区id',
  `parent_id` smallint(6) unsigned NOT NULL default '0' COMMENT '地区父id',
  `area_name` varchar(120) NOT NULL default '' COMMENT '地区名称',
  `area_type` tinyint(1) NOT NULL default '2' COMMENT '地区类型 0:country,1:province,2:city,3:district',
  PRIMARY KEY  (`area_id`),
  KEY `parent_id` (`parent_id`),
  KEY `area_type` (`area_type`)
) ENGINE=MyISAM AUTO_INCREMENT=3429 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_article` */

DROP TABLE IF EXISTS `imall_article`;

CREATE TABLE `imall_article` (
  `article_id` int(10) unsigned NOT NULL auto_increment COMMENT '文章id',
  `cat_id` int(10) NOT NULL default '0' COMMENT '分类id',
  `title` varchar(255) NOT NULL default '' COMMENT '标题',
  `content` text COMMENT '内容',
  `thumb` varchar(255) NOT NULL default '' COMMENT '图片',
  `admin_id` int(10) NOT NULL default '0' COMMENT '管理员id',
  `add_time` datetime default NULL COMMENT '添加时间',
  `is_link` tinyint(1) NOT NULL default '0' COMMENT '0不跳转，1跳转',
  `link_url` varchar(255) NOT NULL default '' COMMENT '跳转url',
  `is_show` tinyint(1) NOT NULL default '1' COMMENT '1显示，0隐藏',
  PRIMARY KEY  (`article_id`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_article_cat` */

DROP TABLE IF EXISTS `imall_article_cat`;

CREATE TABLE `imall_article_cat` (
  `cat_id` int(10) unsigned NOT NULL auto_increment COMMENT '分类id',
  `cat_name` varchar(255) NOT NULL default '' COMMENT '分类名称',
  `parent_id` int(10) NOT NULL default '0' COMMENT '父id ， -1为系统分类',
  `sort_order` tinyint(1) NOT NULL default '0' COMMENT '分类排序',
  PRIMARY KEY  (`cat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_asd_content` */

DROP TABLE IF EXISTS `imall_asd_content`;

CREATE TABLE `imall_asd_content` (
  `asd_id` int(10) unsigned NOT NULL auto_increment COMMENT '广告id',
  `position_id` int(10) unsigned NOT NULL default '0' COMMENT '广告位id',
  `media_type` tinyint(3) unsigned NOT NULL default '1' COMMENT '广告类型 1:图片,2.falsh,3:html',
  `asd_name` varchar(60) NOT NULL default '' COMMENT '广告名称',
  `asd_link` varchar(255) NOT NULL default '' COMMENT '广告链接',
  `asd_content` text NOT NULL COMMENT '广告内容, 根据类型里头存放不一样的内容',
  `remark` text NOT NULL COMMENT '备注',
  `enabled` tinyint(3) unsigned NOT NULL default '1' COMMENT '是否开启,1:开启,2:关闭',
  `last_update_time` datetime default NULL COMMENT '最后更新时间',
  PRIMARY KEY  (`asd_id`),
  KEY `position_id` (`position_id`),
  KEY `enabled` (`enabled`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_asd_position` */

DROP TABLE IF EXISTS `imall_asd_position`;

CREATE TABLE `imall_asd_position` (
  `position_id` int(10) unsigned NOT NULL auto_increment COMMENT '广告位id',
  `position_name` varchar(60) NOT NULL default '' COMMENT '广告位名称',
  `asd_width` smallint(5) unsigned NOT NULL default '0' COMMENT '广告位宽',
  `asd_height` smallint(5) unsigned NOT NULL default '0' COMMENT '广告位高',
  `position_desc` varchar(255) NOT NULL default '' COMMENT '广告位描述',
  PRIMARY KEY  (`position_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_attribute` */

DROP TABLE IF EXISTS `imall_attribute`;

CREATE TABLE `imall_attribute` (
  `attr_id` int(10) unsigned NOT NULL auto_increment COMMENT '属性id',
  `cat_id` int(10) NOT NULL default '0' COMMENT '分类id',
  `attr_name` varchar(255) NOT NULL default '' COMMENT '属性名称',
  `input_type` tinyint(1) NOT NULL default '0' COMMENT '属性input类型 0:text,1:select,2:radio,3:checkbox',
  `attr_values` text COMMENT '属性值 一行代表一个',
  `sort_order` tinyint(1) NOT NULL default '0' COMMENT '显示排序',
  PRIMARY KEY  (`attr_id`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_brand` */

DROP TABLE IF EXISTS `imall_brand`;

CREATE TABLE `imall_brand` (
  `brand_id` smallint(5) unsigned NOT NULL auto_increment,
  `brand_name` varchar(60) NOT NULL default '' COMMENT '品牌名称',
  `brand_logo` varchar(80) NOT NULL default '' COMMENT '品牌logo',
  `brand_desc` text NOT NULL COMMENT '品牌介绍',
  `site_url` varchar(255) NOT NULL default '' COMMENT '网址',
  `is_show` tinyint(1) unsigned NOT NULL default '1' COMMENT '1为显示',
  PRIMARY KEY  (`brand_id`),
  KEY `is_show` (`is_show`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_brand_category` */

DROP TABLE IF EXISTS `imall_brand_category`;

CREATE TABLE `imall_brand_category` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `brand_id` int(11) default NULL COMMENT '品牌id',
  `cat_id` int(11) default NULL COMMENT '分类id',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=529 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_cart` */

DROP TABLE IF EXISTS `imall_cart`;

CREATE TABLE `imall_cart` (
  `cart_id` int(10) unsigned NOT NULL auto_increment COMMENT '购物车id',
  `user_id` int(10) NOT NULL default '0' COMMENT '购买用户id',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `goods_number` int(5) NOT NULL default '1' COMMENT '购买数量',
  `add_time` datetime default NULL COMMENT '添加时间',
  PRIMARY KEY  (`cart_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_category` */

DROP TABLE IF EXISTS `imall_category`;

CREATE TABLE `imall_category` (
   `cat_id` int(10) unsigned NOT NULL auto_increment COMMENT '分类id',
   `cat_name` varchar(50) NOT NULL COMMENT '分类名称',
   `parent_id` int(10) NOT NULL default '0' COMMENT '父分类id: 0为根分类',
   `sort_order` tinyint(1) NOT NULL default '0' COMMENT '分类排序',
   `goods_num` int(10) NOT NULL default '0' COMMENT '产品数量，需要定时更新',
   PRIMARY KEY  (`cat_id`)
 ) ENGINE=MyISAM AUTO_INCREMENT=401 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_complaint_type` */

DROP TABLE IF EXISTS `imall_complaint_type`;

CREATE TABLE `imall_complaint_type` (
  `type_id` int(10) unsigned NOT NULL auto_increment COMMENT '类型id',
  `type_content` varchar(100) NOT NULL COMMENT '投诉类型',
  PRIMARY KEY  (`type_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_complaints` */

DROP TABLE IF EXISTS `imall_complaints`;

CREATE TABLE `imall_complaints` (
  `complaints_id` int(10) unsigned NOT NULL auto_increment COMMENT '投诉id',
  `user_id` int(10) NOT NULL COMMENT '投诉人id',
  `usered_id` int(10) NOT NULL COMMENT '被投诉人id',
  `order_id` int(10) NOT NULL COMMENT '订单id',
  `usered_name` varchar(100) NOT NULL COMMENT '被投诉人姓名或店铺名',
  `goods_id` int(10) NOT NULL COMMENT '相关商品id',
  `goods_name` varchar(255) NOT NULL COMMENT '商品名',
  `complaints_title` varchar(255) NOT NULL COMMENT '投诉标题',
  `complaints_content` text NOT NULL COMMENT '投诉内容',
  `complaints_img` varchar(255) NOT NULL COMMENT '相关图片',
  PRIMARY KEY  (`complaints_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_credit` */

DROP TABLE IF EXISTS `imall_credit`;

CREATE TABLE `imall_credit` (
  `cid` int(10) unsigned NOT NULL auto_increment,
  `order_id` int(10) NOT NULL default '0' COMMENT '订单id',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `buyer` int(10) NOT NULL default '0' COMMENT '买家uid',
  `buyer_credit` tinyint(1) NOT NULL default '0' COMMENT '买家获取的评价 -1差，0中，1好',
  `buyer_evaluate` text COMMENT '买家被评价的内容',
  `buyer_evaltime` datetime default NULL COMMENT '买家被评价时间',
  `buyer_explanation` text COMMENT '买家解释内容',
  `buyer_exptime` datetime default NULL COMMENT '买家解释时间',
  `seller` int(10) NOT NULL default '0' COMMENT '卖家uid',
  `seller_credit` tinyint(1) NOT NULL default '0' COMMENT '卖家获取的评价 -1差，0中，1好',
  `seller_evaluate` text COMMENT '卖家被评价的内容',
  `seller_evaltime` datetime default NULL COMMENT '卖家被评价时间',
  `seller_explanation` text COMMENT '卖家解释内容',
  `seller_exptime` datetime default NULL COMMENT '卖家解释时间',
  PRIMARY KEY  (`cid`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_crons` */

DROP TABLE IF EXISTS `imall_crons`;

CREATE TABLE `imall_crons` (
  `id` smallint(6) unsigned NOT NULL auto_increment,
  `type` tinyint(1) NOT NULL default '1' COMMENT '类型， 1为自己定义，-1为系统',
  `name` varchar(200) NOT NULL default '' COMMENT '计划名称',
  `phpfile` varchar(200) NOT NULL default '' COMMENT 'php执行文件',
  `lastrun` int(11) NOT NULL default '0' COMMENT '最后一次运行时间',
  `nextrun` int(11) NOT NULL default '0' COMMENT '下次运行时间',
  `weekday` tinyint(1) NOT NULL default '0' COMMENT '周几',
  `day` tinyint(2) NOT NULL default '0' COMMENT '几号',
  `hour` tinyint(2) NOT NULL default '0' COMMENT '小时',
  `minute` varchar(200) NOT NULL default '' COMMENT '分钟',
  `available` tinyint(1) NOT NULL default '0' COMMENT '是否可用 1为可用',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_flink` */

DROP TABLE IF EXISTS `imall_flink`;

CREATE TABLE `imall_flink` (
  `brand_id` smallint(5) unsigned NOT NULL auto_increment,
  `brand_name` varchar(60) NOT NULL default '' COMMENT '品牌名称',
  `brand_logo` varchar(80) NOT NULL default '' COMMENT '品牌logo',
  `brand_desc` text NOT NULL COMMENT '品牌介绍',
  `site_url` varchar(255) NOT NULL default '' COMMENT '网址',
  `is_show` tinyint(1) unsigned NOT NULL default '1' COMMENT '1为显示',
  PRIMARY KEY  (`brand_id`),
  KEY `is_show` (`is_show`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_goods` */

DROP TABLE IF EXISTS `imall_goods`;

CREATE TABLE `imall_goods` (
  `goods_id` int(10) unsigned NOT NULL auto_increment,
  `shop_id` int(10) NOT NULL COMMENT '店铺id',
  `goods_name` varchar(255) NOT NULL COMMENT '商品名称',
  `cat_id` int(10) NOT NULL COMMENT '分类id',
  `ucat_id` int(10) NOT NULL COMMENT '用户自定义分类id',
  `brand_id` int(10) NOT NULL default '0' COMMENT '品牌id',
  `type_id` int(10) NOT NULL default '0' COMMENT '属性类型id',
  `goods_intro` text COMMENT '商品详情',
  `goods_wholesale` text COMMENT '批发说明',
  `goods_number` int(5) NOT NULL default '0' COMMENT '库存量',
  `goods_price` decimal(8,2) NOT NULL default '0.00' COMMENT '商品价格',
  `transport_price` decimal(8,2) NOT NULL default '0.00' COMMENT '运费',
  `keyword` varchar(255) NOT NULL default '' COMMENT '关键字',
  `is_delete` tinyint(1) NOT NULL default '1' COMMENT '0为已删除',
  `is_best` tinyint(1) NOT NULL default '0' COMMENT '1为精品',
  `is_new` tinyint(1) NOT NULL default '0' COMMENT '1为新品',
  `is_hot` tinyint(1) NOT NULL default '0' COMMENT '1为热销',
  `is_promote` tinyint(1) NOT NULL default '0' COMMENT '1为特价',
  `is_on_sale` tinyint(1) NOT NULL default '1' COMMENT '0为下架，1为上架',
  `is_set_image` tinyint(1) NOT NULL default '0' COMMENT '是否已设置图片',
  `goods_thumb` varchar(255) NOT NULL default '' COMMENT '缩略图',
  `pv` int(10) NOT NULL default '0' COMMENT '关注度',
  `favpv` int(10) NOT NULL default '0' COMMENT '被收藏次数',
  `sort_order` tinyint(1) NOT NULL default '0' COMMENT '排序',
  `add_time` datetime default NULL COMMENT '添加时间',
  `last_update_time` datetime default NULL COMMENT '最后修改时间',
  `lock_flg` tinyint(1) NOT NULL default '0' COMMENT '锁定，1为锁定',
  `is_transport_template` tinyint(1) default '0' COMMENT '是否启用邮费模版',
  `transport_template_id` int(11) default NULL COMMENT '邮费模版id',
  PRIMARY KEY  (`goods_id`),
  KEY `pv` (`pv`),
  KEY `last_update_time` (`last_update_time`),
  KEY `shop_id` (`shop_id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_goods_attr` */

DROP TABLE IF EXISTS `imall_goods_attr`;

CREATE TABLE `imall_goods_attr` (
  `goods_attr_id` int(10) unsigned NOT NULL auto_increment COMMENT '产品属性id 自增',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `attr_id` int(10) NOT NULL default '0' COMMENT '属性id',
  `attr_values` text COMMENT '属性值',
  PRIMARY KEY  (`goods_attr_id`)
) ENGINE=MyISAM AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_goods_comment` */

DROP TABLE IF EXISTS `imall_goods_comment`;

CREATE TABLE `imall_goods_comment` (
  `comment_id` int(10) unsigned NOT NULL auto_increment,
  `goods_id` int(10) NOT NULL default '0',
  `user_id` int(10) NOT NULL default '0',
  `user_name` varchar(50) NOT NULL default '',
  `content` text,
  `add_time` datetime default NULL,
  `is_verify` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_goods_gallery` */

DROP TABLE IF EXISTS `imall_goods_gallery`;

CREATE TABLE `imall_goods_gallery` (
  `img_id` mediumint(8) unsigned NOT NULL auto_increment,
  `goods_id` int(10) unsigned NOT NULL default '0' COMMENT '商品id',
  `img_url` varchar(255) NOT NULL default '' COMMENT '图片url',
  `img_desc` varchar(255) NOT NULL default '' COMMENT '图片介绍',
  `thumb_url` varchar(255) NOT NULL default '' COMMENT '图片缩略图',
  `img_original` varchar(255) NOT NULL default '' COMMENT '原始图片',
  `is_set` tinyint(1) NOT NULL default '0' COMMENT '设为产品显示图片，1为设置',
  PRIMARY KEY  (`img_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=MyISAM AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_goods_shipping` */

DROP TABLE IF EXISTS `imall_goods_shipping`;

CREATE TABLE `imall_goods_shipping` (
  `goos_shipping_id` int(10) unsigned NOT NULL auto_increment,
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `shipping_id` int(6) NOT NULL default '0' COMMENT '运输id',
  `shipping_fee` decimal(6,2) NOT NULL default '0.00' COMMENT '运费',
  PRIMARY KEY  (`goos_shipping_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_goods_transport` */

DROP TABLE IF EXISTS `imall_goods_transport`;

CREATE TABLE `imall_goods_transport` (
  `id` int(11) NOT NULL auto_increment,
  `shop_id` int(11) default NULL COMMENT '店铺ID',
  `transport_name` varchar(100) default '名称' COMMENT '名称',
  `description` varchar(255) default '描述' COMMENT '描述',
  `content` text COMMENT '内容',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_goods_types` */

DROP TABLE IF EXISTS `imall_goods_types`;

CREATE TABLE `imall_goods_types` (
  `type_id` int(10) unsigned NOT NULL auto_increment COMMENT '类型id',
  `type_name` varchar(255) NOT NULL default '' COMMENT '产品类型名称',
  `is_show` tinyint(1) NOT NULL default '0' COMMENT '0为不显示，1为显示; 显示时用户才可以选到此属性',
  PRIMARY KEY  (`type_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_groupbuy` */

DROP TABLE IF EXISTS `imall_groupbuy`;

CREATE TABLE `imall_groupbuy` (
  `group_id` int(10) unsigned NOT NULL auto_increment,
  `group_name` varchar(255) NOT NULL default '',
  `group_desc` text NOT NULL COMMENT '团购说明',
  `start_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '结束时间',
  `goods_id` int(10) unsigned NOT NULL default '0' COMMENT '产品id',
  `spec_price` decimal(8,2) NOT NULL default '0.00' COMMENT '团购价',
  `min_quantity` smallint(5) unsigned NOT NULL default '0' COMMENT '成团件数',
  `recommended` tinyint(3) unsigned NOT NULL default '0' COMMENT '商家点击完成',
  `views` int(10) unsigned NOT NULL default '0' COMMENT '浏览量',
  `shop_id` int(10) unsigned NOT NULL default '0' COMMENT '店铺id',
  `purchase_num` int(10) unsigned NOT NULL default '0' COMMENT '订购数',
  `order_num` int(10) unsigned NOT NULL default '0' COMMENT '订单数',
  PRIMARY KEY  (`group_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_groupbuy_log` */

DROP TABLE IF EXISTS `imall_groupbuy_log`;

CREATE TABLE `imall_groupbuy_log` (
  `grouplog_id` int(10) unsigned NOT NULL auto_increment,
  `group_id` int(10) unsigned NOT NULL default '0' COMMENT '团购id',
  `user_id` int(10) unsigned NOT NULL default '0' COMMENT '用户id',
  `user_name` varchar(60) NOT NULL default '' COMMENT '用户姓名',
  `quantity` smallint(5) unsigned NOT NULL default '0' COMMENT '购买的数量',
  `linkman` varchar(60) NOT NULL default '' COMMENT '真是姓名',
  `tel` varchar(60) NOT NULL default '' COMMENT '电话',
  `add_time` datetime NOT NULL,
  PRIMARY KEY  (`grouplog_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_img_size` */

DROP TABLE IF EXISTS `imall_img_size`;

CREATE TABLE `imall_img_size` (
  `id` int(11) unsigned NOT NULL auto_increment COMMENT '图片id',
  `uid` int(11) default NULL COMMENT '上传图片的用户id',
  `img_url` varchar(255) default NULL COMMENT '图片url',
  `img_size` varchar(10) default NULL COMMENT '图片大小',
  `upl_time` datetime default NULL COMMENT '图片上传时间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_index_images` */

DROP TABLE IF EXISTS `imall_index_images`;

CREATE TABLE `imall_index_images` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '' COMMENT '介绍',
  `images_url` varchar(255) NOT NULL default '' COMMENT '图片地址',
  `images_link` varchar(255) NOT NULL default '' COMMENT '图片链接',
  `add_time` datetime default NULL COMMENT '添加时间',
  `status` tinyint(1) NOT NULL default '1' COMMENT '状态:1可用,0不可用',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_integral` */

DROP TABLE IF EXISTS `imall_integral`;

CREATE TABLE `imall_integral` (
  `int_id` int(3) unsigned NOT NULL auto_increment,
  `int_min` varchar(20) default NULL,
  `int_max` varchar(20) default NULL,
  `int_grade` int(3) default NULL,
  PRIMARY KEY  (`int_id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_keywords_count` */

DROP TABLE IF EXISTS `imall_keywords_count`;

CREATE TABLE `imall_keywords_count` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `keywords` varchar(255) default NULL,
  `count` int(10) NOT NULL default '0' COMMENT '总搜索次数',
  `day` int(10) NOT NULL default '0' COMMENT '当天',
  `week` int(10) NOT NULL default '0' COMMENT '本周',
  `month` int(10) NOT NULL default '0' COMMENT '本月',
  `dataline` int(11) default NULL COMMENT '最后更新时间',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `keywords` (`keywords`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_mailtpl` */

DROP TABLE IF EXISTS `imall_mailtpl`;

CREATE TABLE `imall_mailtpl` (
  `tpl_id` int(10) unsigned NOT NULL auto_increment,
  `tpl_name` varchar(50) NOT NULL default '' COMMENT '模板名称',
  `tpl_title` varchar(255) NOT NULL default '' COMMENT '发邮件标题',
  `tpl_content` text COMMENT '发邮件内容',
  `last_modify` datetime default NULL COMMENT '最后修改时间',
  PRIMARY KEY  (`tpl_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_order_info` */

DROP TABLE IF EXISTS `imall_order_info`;

CREATE TABLE `imall_order_info` (
  `order_id` int(10) unsigned NOT NULL auto_increment COMMENT '订单id',
  `payid` varchar(20) NOT NULL default '0' COMMENT '支付订单号',
  `shop_id` int(10) NOT NULL default '0' COMMENT '店铺id',
  `pay_id` int(10) NOT NULL default '0' COMMENT '支付方式id',
  `pay_name` varchar(255) NOT NULL default '' COMMENT '支会方式名称',
  `user_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `goods_name` varchar(255) NOT NULL default '' COMMENT '产品名称',
  `goods_price` decimal(8,2) NOT NULL default '0.00' COMMENT '产品价格',
  `transport_price` decimal(6,2) NOT NULL default '0.00' COMMENT '运费',
  `order_num` int(10) NOT NULL default '0' COMMENT '购买数量',
  `order_amount` decimal(10,2) NOT NULL default '0.00' COMMENT '总订单价格',
  `consignee` varchar(50) default NULL COMMENT '联系人',
  `country` smallint(6) NOT NULL default '0' COMMENT '国家',
  `province` smallint(6) default NULL COMMENT '省份',
  `city` smallint(6) default NULL COMMENT '城市',
  `district` smallint(6) default NULL COMMENT '地区',
  `address` varchar(255) NOT NULL default '' COMMENT '详细地址',
  `zipcode` char(6) default NULL COMMENT '邮政编码',
  `telphone` varchar(20) default NULL COMMENT '电话',
  `mobile` varchar(20) default NULL COMMENT '手机',
  `email` varchar(255) default NULL COMMENT '邮箱',
  `shipping_name` varchar(50) default NULL COMMENT '物流公司名称',
  `shipping_no` varchar(50) default NULL COMMENT '物流发货单号',
  `shipping_type` varchar(50) default NULL COMMENT '发货运输类型',
  `message` text COMMENT '订单附言',
  `order_time` datetime default NULL COMMENT '订单时间',
  `pay_time` datetime default NULL COMMENT '支付时间',
  `shipping_time` datetime default NULL COMMENT '发货时间',
  `receive_time` datetime default NULL COMMENT '用户已接收时间',
  `order_status` tinyint(1) default '1' COMMENT '订单状态：0已取消订单，1用户已下订单，2商家已确认订单，3用户验货',
  `pay_status` tinyint(1) default '0' COMMENT '支付状态：0未支付，1已支付',
  `transport_status` tinyint(1) default '0' COMMENT '发货状态：0未发货，1已发货',
  `trade_no` varchar(50) default NULL COMMENT '支付宝返回的，支付宝交易号',
  `seller_reply` tinyint(1) NOT NULL default '0' COMMENT '买家是否回复：0未回复，1回复',
  `buyer_reply` tinyint(1) NOT NULL default '0' COMMENT '卖家是否回复：0未回复，1回复',
  `group_id` int(10) NOT NULL default '0' COMMENT '团购id',
  `pay_message` text COMMENT '支付留言',
  `complaint` tinyint(1) default '0' COMMENT '用户投诉',
  `get_back_time` datetime default NULL COMMENT '申请退款时间',
  PRIMARY KEY  (`order_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_payment` */

DROP TABLE IF EXISTS `imall_payment`;

CREATE TABLE `imall_payment` (
  `pay_id` int(6) unsigned NOT NULL auto_increment COMMENT '支付id',
  `pay_name` varchar(255) NOT NULL default '' COMMENT '支付类型名称',
  `pay_code` varchar(255) NOT NULL COMMENT '文件夹名代码',
  `pay_desc` text COMMENT '描述',
  `author` varchar(200) NOT NULL default 'IWEB TEAM' COMMENT '作者',
  `version` varchar(20) NOT NULL default '1.0' COMMENT '版本',
  `config` text COMMENT '配置信息',
  `enabled` tinyint(1) NOT NULL default '0' COMMENT '是否开启：0关闭，1开启',
  PRIMARY KEY  (`pay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_plugin_url` */

DROP TABLE IF EXISTS `imall_plugin_url`;

CREATE TABLE `imall_plugin_url` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `name` varchar(40) default NULL COMMENT '唯一英文名',
  `layout_id` varchar(20) default NULL COMMENT '调用时的divid',
  `url` varchar(255) default NULL COMMENT '入口文件',
  `sequence` smallint(5) default NULL COMMENT '排序用',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_plugins` */

DROP TABLE IF EXISTS `imall_plugins`;

CREATE TABLE `imall_plugins` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `title` varchar(100) default NULL COMMENT '插件名',
  `name` varchar(50) default NULL COMMENT '物理地址文件夹名-唯一',
  `valid` tinyint(2) default '0' COMMENT '是否激活',
  `autoorder` tinyint(2) default '0' COMMENT '是否允许自由定制',
  `reg_date` datetime NOT NULL COMMENT '注册时间',
  `image` varchar(150) NOT NULL COMMENT '缩略图',
  `comment_num` mediumint(8) unsigned default '0' COMMENT '插件评论数',
  `use_num` mediumint(8) unsigned default '0' COMMENT '插件用户数',
  `info` varchar(300) default NULL COMMENT '简介',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_privilege` */

DROP TABLE IF EXISTS `imall_privilege`;

CREATE TABLE `imall_privilege` (
  `privilege_id` int(10) unsigned NOT NULL auto_increment COMMENT '权限id',
  `privilege_name` varchar(255) NOT NULL default '' COMMENT '权限名称',
  `privilege_remark` text COMMENT '权限备注',
  `privilege_type` tinyint(1) NOT NULL default '0' COMMENT '权限类型，0为普通可选checkbox，1为可设置变量text',
  PRIMARY KEY  (`privilege_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_remind` */

DROP TABLE IF EXISTS `imall_remind`;

CREATE TABLE `imall_remind` (
  `remind_id` int(10) unsigned NOT NULL auto_increment,
  `remind_type` int(10) NOT NULL default '1' COMMENT '提醒类别,1买家，2卖家',
  `remind_name` varchar(200) NOT NULL default '' COMMENT '提醒名称',
  `remind_tpl` text COMMENT '提醒内容',
  `enable` tinyint(1) NOT NULL default '1' COMMENT '是否开启，1开启',
  PRIMARY KEY  (`remind_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_remind_info` */

DROP TABLE IF EXISTS `imall_remind_info`;

CREATE TABLE `imall_remind_info` (
  `rinfo_id` int(10) unsigned NOT NULL auto_increment,
  `user_id` int(10) NOT NULL default '0' COMMENT '用户ID',
  `remind_info` char(200) default NULL COMMENT '提醒内容',
  `remind_time` datetime default NULL COMMENT '发送提醒时间',
  `isread` tinyint(1) NOT NULL default '0' COMMENT '已读标志，1为已读',
  PRIMARY KEY  (`rinfo_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_remind_user` */

DROP TABLE IF EXISTS `imall_remind_user`;

CREATE TABLE `imall_remind_user` (
  `urid` int(10) unsigned NOT NULL auto_increment,
  `user_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `remind_id` int(10) NOT NULL default '0' COMMENT '提醒id',
  `site` tinyint(1) NOT NULL default '0' COMMENT '站点提醒',
  `mail` tinyint(1) NOT NULL default '0' COMMENT '邮件提醒',
  `im` tinyint(1) NOT NULL default '0' COMMENT 'im提醒',
  `mobile` tinyint(1) NOT NULL default '0' COMMENT '手机提醒',
  `add_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '增加时间',
  PRIMARY KEY  (`urid`)
) ENGINE=MyISAM AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_settings` */

DROP TABLE IF EXISTS `imall_settings`;

CREATE TABLE `imall_settings` (
  `variable` varchar(32) NOT NULL default '' COMMENT '系统名称',
  `value` text NOT NULL COMMENT '系统值',
  PRIMARY KEY  (`variable`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shipping` */

DROP TABLE IF EXISTS `imall_shipping`;

CREATE TABLE `imall_shipping` (
  `shipping_id` int(6) unsigned NOT NULL auto_increment COMMENT '配送id',
  `shipping_name` varchar(255) NOT NULL COMMENT '配送名称',
  `shipping_fee` decimal(6,2) NOT NULL default '0.00' COMMENT '配送费用',
  `shipping_desc` text COMMENT '描述',
  `enabled` tinyint(1) NOT NULL default '0' COMMENT '是否开启：0关闭，1开启',
  PRIMARY KEY  (`shipping_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_article` */

DROP TABLE IF EXISTS `imall_shop_article`;

CREATE TABLE `imall_shop_article` (
  `article_id` int(10) unsigned NOT NULL auto_increment COMMENT '文章id',
  `cat_id` int(10) NOT NULL default '0' COMMENT '分类id',
  `title` varchar(255) NOT NULL default '' COMMENT '标题',
  `content` text COMMENT '内容',
  `thumb` varchar(255) NOT NULL default '' COMMENT '图片',
  `shop_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `add_time` datetime default NULL COMMENT '添加时间',
  `is_link` tinyint(1) NOT NULL default '0' COMMENT '0不跳转，1跳转',
  `link_url` varchar(255) NOT NULL default '' COMMENT '跳转url',
  `is_show` tinyint(1) NOT NULL default '1' COMMENT '1显示，0隐藏',
  PRIMARY KEY  (`article_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `imall_shop_category` */

DROP TABLE IF EXISTS `imall_shop_category`;

CREATE TABLE `imall_shop_category` (
  `shop_cat_id` int(10) unsigned NOT NULL auto_increment COMMENT '店铺分类id',
  `shop_id` int(10) NOT NULL COMMENT '店铺id',
  `shop_cat_name` varchar(50) NOT NULL COMMENT '店铺分类名称',
  `shop_cat_unit` varchar(20) NOT NULL default '' COMMENT '店铺分类数量单位',
  `parent_id` int(10) NOT NULL default '0' COMMENT '父分类',
  `sort_order` tinyint(1) NOT NULL default '0' COMMENT '分类排序',
  PRIMARY KEY  (`shop_cat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_guestbook` */

DROP TABLE IF EXISTS `imall_shop_guestbook`;

CREATE TABLE `imall_shop_guestbook` (
  `gid` int(10) unsigned NOT NULL auto_increment,
  `shop_id` int(10) NOT NULL default '0' COMMENT '店铺id',
  `shop_name` varchar(255) default '' COMMENT '店铺名称',
  `user_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `name` varchar(255) default NULL COMMENT '姓名',
  `email` varchar(255) default NULL COMMENT 'email',
  `contact` varchar(255) default NULL COMMENT '其它联系方式',
  `content` text COMMENT '留言内容',
  `add_time` datetime default NULL COMMENT '添加时间',
  `add_ip` varchar(20) default NULL COMMENT '添加时ip',
  `shop_del_status` tinyint(1) NOT NULL default '1' COMMENT '0代表店言已删除留言',
  `user_del_status` tinyint(1) NOT NULL default '1' COMMENT '0代表用户已删除留言',
  `reply` text COMMENT '卖家留言',
  `group_id` int(10) NOT NULL default '0',
  `group_name` varchar(50) NOT NULL,
  `goods_id` int(10) NOT NULL,
  PRIMARY KEY  (`gid`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_honor` */

DROP TABLE IF EXISTS `imall_shop_honor`;

CREATE TABLE `imall_shop_honor` (
  `honor_id` mediumint(8) unsigned NOT NULL auto_increment,
  `shop_id` int(10) unsigned NOT NULL default '0' COMMENT '店铺id',
  `honor_img` varchar(255) NOT NULL default '' COMMENT '荣誉图片url',
  `honor_desc` varchar(255) NOT NULL default '' COMMENT '荣誉图片介绍',
  `honor_thumb` varchar(255) NOT NULL default '' COMMENT '荣誉图片缩略图',
  `honor_original` varchar(255) NOT NULL default '' COMMENT '原始荣誉图片',
  PRIMARY KEY  (`honor_id`),
  KEY `goods_id` (`shop_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_info` */

DROP TABLE IF EXISTS `imall_shop_info`;

CREATE TABLE `imall_shop_info` (
  `shop_id` int(10) unsigned NOT NULL auto_increment COMMENT '店铺id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `shop_name` varchar(50) default NULL COMMENT '店铺名称',
  `shop_country` smallint(6) default NULL COMMENT '店铺所在国家',
  `shop_province` smallint(6) default NULL COMMENT '店铺所在省份',
  `shop_city` smallint(6) default NULL COMMENT '店铺所在城市',
  `shop_district` smallint(6) default NULL COMMENT '店铺所在区',
  `shop_address` varchar(255) default NULL COMMENT '店铺所在详细地址',
  `shop_images` varchar(255) default NULL COMMENT '店铺介绍中的图片',
  `shop_logo` varchar(255) default NULL COMMENT '店铺logo',
  `shop_template_img` varchar(255) default NULL COMMENT '店铺模板大图',
  `shop_template` varchar(20) NOT NULL default 'default' COMMENT '店铺模板',
  `shop_management` varchar(255) default NULL COMMENT '店铺主营',
  `shop_intro` text COMMENT '店铺介绍',
  `shop_notice` text COMMENT '店铺公告',
  `shop_creat_time` datetime default NULL COMMENT '店铺创建时间',
  `goods_num` int(10) NOT NULL default '0' COMMENT '店铺产品数量',
  `open_flg` tinyint(1) NOT NULL default '0' COMMENT '店铺关闭，1为关闭',
  `lock_flg` tinyint(1) NOT NULL default '0' COMMENT '店铺锁定，1为锁定',
  `map_x` varchar(20) default '0' COMMENT '地图经线坐标',
  `map_y` varchar(20) default '0' COMMENT '地图纬线坐标',
  `map_zoom` varchar(3) default '11' COMMENT '地图比例',
  `count_imgsize` int(10) NOT NULL default '0' COMMENT '图片总大小',
  PRIMARY KEY  (`shop_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_inquiry` */

DROP TABLE IF EXISTS `imall_shop_inquiry`;

CREATE TABLE `imall_shop_inquiry` (
  `iid` int(10) unsigned NOT NULL auto_increment,
  `shop_id` int(10) NOT NULL default '0' COMMENT '店铺id',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `goods_name` varchar(255) NOT NULL default '' COMMENT '产品名称',
  `user_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `name` varchar(255) default NULL COMMENT '姓名',
  `email` varchar(255) default NULL COMMENT 'email',
  `mobile` varchar(255) default NULL COMMENT '手机',
  `telphone` varchar(255) default NULL COMMENT '电话',
  `title` text COMMENT '询价标题',
  `content` text COMMENT '询价内容',
  `add_time` datetime default NULL COMMENT '添加时间',
  `add_ip` varchar(20) default NULL COMMENT '添加时ip',
  `shop_del_status` tinyint(1) NOT NULL default '1' COMMENT '0代表店言已删除',
  `user_del_status` tinyint(1) NOT NULL default '1' COMMENT '0代表用户已删除',
  PRIMARY KEY  (`iid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_payment` */

DROP TABLE IF EXISTS `imall_shop_payment`;

CREATE TABLE `imall_shop_payment` (
  `shop_payment_id` int(10) unsigned NOT NULL auto_increment,
  `shop_id` int(10) NOT NULL default '0' COMMENT '店铺id',
  `pay_id` int(6) NOT NULL default '0' COMMENT '支付方式id',
  `pay_desc` text COMMENT '详细信息',
  `pay_config` text COMMENT '配置信息',
  `enabled` tinyint(1) NOT NULL default '0' COMMENT '1.开启 2.关闭',
  PRIMARY KEY  (`shop_payment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_shop_request` */

DROP TABLE IF EXISTS `imall_shop_request`;

CREATE TABLE `imall_shop_request` (
  `request_id` int(10) unsigned NOT NULL auto_increment COMMENT '申请id',
  `user_id` int(10) default NULL COMMENT '用户id',
  `company_name` varchar(255) NOT NULL default '' COMMENT '企业名称',
  `person_name` varchar(255) default NULL COMMENT '法人代表姓名',
  `credit_type` varchar(255) default NULL COMMENT '证件类型',
  `credit_num` varchar(255) default NULL COMMENT '证件号码',
  `credit_commercial` varchar(255) default NULL COMMENT '营业执照',
  `company_area` varchar(255) default NULL COMMENT '公司所在地',
  `company_address` varchar(255) default NULL COMMENT '详细通讯地址',
  `zipcode` int(10) default NULL COMMENT '邮编',
  `mobile` varchar(50) default NULL COMMENT '手机号码',
  `telphone` varchar(50) default NULL COMMENT '联系电话',
  `add_time` datetime default NULL COMMENT '提交时间',
  `status` tinyint(2) default '0' COMMENT '状态：0等待审核，1审核通过，2审核失败',
  PRIMARY KEY  (`request_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_user_address` */

DROP TABLE IF EXISTS `imall_user_address`;

CREATE TABLE `imall_user_address` (
  `address_id` int(11) unsigned NOT NULL auto_increment,
  `user_id` int(11) NOT NULL default '0' COMMENT '购物人id',
  `to_user_name` varchar(60) NOT NULL default '' COMMENT '收件人姓名',
  `full_address` varchar(200) NOT NULL default '' COMMENT '详细地址',
  `zipcode` varchar(10) NOT NULL default '' COMMENT '邮政编码',
  `telphone` varchar(20) NOT NULL default '' COMMENT '电话',
  `mobile` varchar(20) NOT NULL default '' COMMENT '手机',
  `user_country` smallint(6) NOT NULL default '0' COMMENT '国家',
  `user_province` smallint(6) NOT NULL default '0' COMMENT '省',
  `user_city` smallint(6) NOT NULL default '0' COMMENT '城市',
  `user_district` smallint(6) NOT NULL default '0' COMMENT '地区',
  `email` varchar(100) NOT NULL default '' COMMENT '邮箱',
  PRIMARY KEY  (`address_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_user_favorite` */

DROP TABLE IF EXISTS `imall_user_favorite`;

CREATE TABLE `imall_user_favorite` (
  `favorite_id` int(10) unsigned NOT NULL auto_increment COMMENT '收藏id',
  `user_id` int(10) NOT NULL default '0' COMMENT '用户id',
  `goods_id` int(10) NOT NULL default '0' COMMENT '产品id',
  `add_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY  (`favorite_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `imall_user_info` */

DROP TABLE IF EXISTS `imall_user_info`;

CREATE TABLE `imall_user_info` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `user_truename` varchar(255) NOT NULL default '' COMMENT '真实姓名',
  `user_ico` varchar(255) default NULL COMMENT '用户头像',
  `user_marry` tinyint(1) NOT NULL default '0' COMMENT '0保密，1未婚，2已婚',
  `user_gender` tinyint(1) NOT NULL default '0' COMMENT '0保密，1男，2女',
  `user_mobile` varchar(20) default NULL COMMENT '用户手机号',
  `user_telphone` varchar(20) default NULL COMMENT '用户电话',
  `user_country` smallint(6) default NULL COMMENT '国家',
  `user_province` smallint(6) default NULL COMMENT '省份',
  `user_city` smallint(6) default NULL COMMENT '城市',
  `user_district` smallint(6) default NULL COMMENT '区',
  `user_zipcode` char(6) default NULL COMMENT '用户邮编',
  `user_address` varchar(255) default NULL COMMENT '用户地址',
  `user_birthday` date default NULL COMMENT '用户生日',
  `user_qq` varchar(16) default NULL COMMENT '用户qq',
  `user_msn` varchar(50) default NULL COMMENT '用户msn',
  `user_skype` varchar(50) default NULL COMMENT '用户skype',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_user_rank` */

DROP TABLE IF EXISTS `imall_user_rank`;

CREATE TABLE `imall_user_rank` (
  `rank_id` int(10) unsigned NOT NULL auto_increment COMMENT '会员级别id',
  `rank_name` varchar(255) NOT NULL default '' COMMENT '会员级别名称',
  `privilege` varchar(255) NOT NULL default '' COMMENT '会员级别权限',
  PRIMARY KEY  (`rank_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `imall_users` */

DROP TABLE IF EXISTS `imall_users`;

CREATE TABLE `imall_users` (
  `user_id` int(10) unsigned NOT NULL auto_increment COMMENT '用户id',
  `user_email` varchar(100) default NULL COMMENT '用户email',
  `user_name` varchar(50) default NULL COMMENT '用户名',
  `user_passwd` varchar(50) default '' COMMENT '用户密码',
  `user_question` varchar(255) default NULL COMMENT '密码找回问题',
  `user_answer` varchar(255) default NULL COMMENT '密码找回答案',
  `user_ico` varchar(255) NOT NULL default '' COMMENT '用户头像',
  `reg_time` datetime default NULL COMMENT '注册时间',
  `last_login_time` datetime default NULL COMMENT '最后登陆时间',
  `last_ip` char(15) default NULL COMMENT '最后登陆ip',
  `email_check` tinyint(1) NOT NULL default '0' COMMENT '邮件确认',
  `email_check_code` varchar(255) default NULL COMMENT '邮件确认码',
  `forgot_check_code` varchar(255) default NULL COMMENT '密码找回码',
  `rank_id` int(10) NOT NULL default '1' COMMENT '用户级别',
  `locked` tinyint(1) NOT NULL default '0' COMMENT '用户锁定，1为锁定',
  PRIMARY KEY  (`user_id`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*Data for the table `imall_areas` */

insert  into `imall_areas`(`area_id`,`parent_id`,`area_name`,`area_type`) values (1,0,'中国',0),(2,1,'北京',1),(3,1,'安徽',1),(4,1,'福建',1),(5,1,'甘肃',1),(6,1,'广东',1),(7,1,'广西',1),(8,1,'贵州',1),(9,1,'海南',1),(10,1,'河北',1),(11,1,'河南',1),(12,1,'黑龙江',1),(13,1,'湖北',1),(14,1,'湖南',1),(15,1,'吉林',1),(16,1,'江苏',1),(17,1,'江西',1),(18,1,'辽宁',1),(19,1,'内蒙古',1),(20,1,'宁夏',1),(21,1,'青海',1),(22,1,'山东',1),(23,1,'山西',1),(24,1,'陕西',1),(25,1,'上海',1),(26,1,'四川',1),(27,1,'天津',1),(28,1,'西藏',1),(29,1,'新疆',1),(30,1,'云南',1),(31,1,'浙江',1),(32,1,'重庆',1),(33,1,'香港',1),(34,1,'澳门',1),(35,1,'台湾',1),(36,3,'安庆',2),(37,3,'蚌埠',2),(38,3,'巢湖',2),(39,3,'池州',2),(40,3,'滁州',2),(41,3,'阜阳',2),(42,3,'淮北',2),(43,3,'淮南',2),(44,3,'黄山',2),(45,3,'六安',2),(46,3,'马鞍山',2),(47,3,'宿州',2),(48,3,'铜陵',2),(49,3,'芜湖',2),(50,3,'宣城',2),(51,3,'亳州',2),(52,2,'北京',2),(53,4,'福州',2),(54,4,'龙岩',2),(55,4,'南平',2),(56,4,'宁德',2),(57,4,'莆田',2),(58,4,'泉州',2),(59,4,'三明',2),(60,4,'厦门',2),(61,4,'漳州',2),(62,5,'兰州',2),(63,5,'白银',2),(64,5,'定西',2),(65,5,'甘南',2),(66,5,'嘉峪关',2),(67,5,'金昌',2),(68,5,'酒泉',2),(69,5,'临夏',2),(70,5,'陇南',2),(71,5,'平凉',2),(72,5,'庆阳',2),(73,5,'天水',2),(74,5,'武威',2),(75,5,'张掖',2),(76,6,'广州',2),(77,6,'深圳',2),(78,6,'潮州',2),(79,6,'东莞',2),(80,6,'佛山',2),(81,6,'河源',2),(82,6,'惠州',2),(83,6,'江门',2),(84,6,'揭阳',2),(85,6,'茂名',2),(86,6,'梅州',2),(87,6,'清远',2),(88,6,'汕头',2),(89,6,'汕尾',2),(90,6,'韶关',2),(91,6,'阳江',2),(92,6,'云浮',2),(93,6,'湛江',2),(94,6,'肇庆',2),(95,6,'中山',2),(96,6,'珠海',2),(97,7,'南宁',2),(98,7,'桂林',2),(99,7,'百色',2),(100,7,'北海',2),(101,7,'崇左',2),(102,7,'防城港',2),(103,7,'贵港',2),(104,7,'河池',2),(105,7,'贺州',2),(106,7,'来宾',2),(107,7,'柳州',2),(108,7,'钦州',2),(109,7,'梧州',2),(110,7,'玉林',2),(111,8,'贵阳',2),(112,8,'安顺',2),(113,8,'毕节',2),(114,8,'六盘水',2),(115,8,'黔东南',2),(116,8,'黔南',2),(117,8,'黔西南',2),(118,8,'铜仁',2),(119,8,'遵义',2),(120,9,'海口',2),(121,9,'三亚',2),(122,9,'白沙',2),(123,9,'保亭',2),(124,9,'昌江',2),(125,9,'澄迈县',2),(126,9,'定安县',2),(127,9,'东方',2),(128,9,'乐东',2),(129,9,'临高县',2),(130,9,'陵水',2),(131,9,'琼海',2),(132,9,'琼中',2),(133,9,'屯昌县',2),(134,9,'万宁',2),(135,9,'文昌',2),(136,9,'五指山',2),(137,9,'儋州',2),(138,10,'石家庄',2),(139,10,'保定',2),(140,10,'沧州',2),(141,10,'承德',2),(142,10,'邯郸',2),(143,10,'衡水',2),(144,10,'廊坊',2),(145,10,'秦皇岛',2),(146,10,'唐山',2),(147,10,'邢台',2),(148,10,'张家口',2),(149,11,'郑州',2),(150,11,'洛阳',2),(151,11,'开封',2),(152,11,'安阳',2),(153,11,'鹤壁',2),(154,11,'济源',2),(155,11,'焦作',2),(156,11,'南阳',2),(157,11,'平顶山',2),(158,11,'三门峡',2),(159,11,'商丘',2),(160,11,'新乡',2),(161,11,'信阳',2),(162,11,'许昌',2),(163,11,'周口',2),(164,11,'驻马店',2),(165,11,'漯河',2),(166,11,'濮阳',2),(167,12,'哈尔滨',2),(168,12,'大庆',2),(169,12,'大兴安岭',2),(170,12,'鹤岗',2),(171,12,'黑河',2),(172,12,'鸡西',2),(173,12,'佳木斯',2),(174,12,'牡丹江',2),(175,12,'七台河',2),(176,12,'齐齐哈尔',2),(177,12,'双鸭山',2),(178,12,'绥化',2),(179,12,'伊春',2),(180,13,'武汉',2),(181,13,'仙桃',2),(182,13,'鄂州',2),(183,13,'黄冈',2),(184,13,'黄石',2),(185,13,'荆门',2),(186,13,'荆州',2),(187,13,'潜江',2),(188,13,'神农架林区',2),(189,13,'十堰',2),(190,13,'随州',2),(191,13,'天门',2),(192,13,'咸宁',2),(193,13,'襄樊',2),(194,13,'孝感',2),(195,13,'宜昌',2),(196,13,'恩施',2),(197,14,'长沙',2),(198,14,'张家界',2),(199,14,'常德',2),(200,14,'郴州',2),(201,14,'衡阳',2),(202,14,'怀化',2),(203,14,'娄底',2),(204,14,'邵阳',2),(205,14,'湘潭',2),(206,14,'湘西',2),(207,14,'益阳',2),(208,14,'永州',2),(209,14,'岳阳',2),(210,14,'株洲',2),(211,15,'长春',2),(212,15,'吉林',2),(213,15,'白城',2),(214,15,'白山',2),(215,15,'辽源',2),(216,15,'四平',2),(217,15,'松原',2),(218,15,'通化',2),(219,15,'延边',2),(220,16,'南京',2),(221,16,'苏州',2),(222,16,'无锡',2),(223,16,'常州',2),(224,16,'淮安',2),(225,16,'连云港',2),(226,16,'南通',2),(227,16,'宿迁',2),(228,16,'泰州',2),(229,16,'徐州',2),(230,16,'盐城',2),(231,16,'扬州',2),(232,16,'镇江',2),(233,17,'南昌',2),(234,17,'抚州',2),(235,17,'赣州',2),(236,17,'吉安',2),(237,17,'景德镇',2),(238,17,'九江',2),(239,17,'萍乡',2),(240,17,'上饶',2),(241,17,'新余',2),(242,17,'宜春',2),(243,17,'鹰潭',2),(244,18,'沈阳',2),(245,18,'大连',2),(246,18,'鞍山',2),(247,18,'本溪',2),(248,18,'朝阳',2),(249,18,'丹东',2),(250,18,'抚顺',2),(251,18,'阜新',2),(252,18,'葫芦岛',2),(253,18,'锦州',2),(254,18,'辽阳',2),(255,18,'盘锦',2),(256,18,'铁岭',2),(257,18,'营口',2),(258,19,'呼和浩特',2),(259,19,'阿拉善盟',2),(260,19,'巴彦淖尔盟',2),(261,19,'包头',2),(262,19,'赤峰',2),(263,19,'鄂尔多斯',2),(264,19,'呼伦贝尔',2),(265,19,'通辽',2),(266,19,'乌海',2),(267,19,'乌兰察布市',2),(268,19,'锡林郭勒盟',2),(269,19,'兴安盟',2),(270,20,'银川',2),(271,20,'固原',2),(272,20,'石嘴山',2),(273,20,'吴忠',2),(274,20,'中卫',2),(275,21,'西宁',2),(276,21,'果洛',2),(277,21,'海北',2),(278,21,'海东',2),(279,21,'海南',2),(280,21,'海西',2),(281,21,'黄南',2),(282,21,'玉树',2),(283,22,'济南',2),(284,22,'青岛',2),(285,22,'滨州',2),(286,22,'德州',2),(287,22,'东营',2),(288,22,'菏泽',2),(289,22,'济宁',2),(290,22,'莱芜',2),(291,22,'聊城',2),(292,22,'临沂',2),(293,22,'日照',2),(294,22,'泰安',2),(295,22,'威海',2),(296,22,'潍坊',2),(297,22,'烟台',2),(298,22,'枣庄',2),(299,22,'淄博',2),(300,23,'太原',2),(301,23,'长治',2),(302,23,'大同',2),(303,23,'晋城',2),(304,23,'晋中',2),(305,23,'临汾',2),(306,23,'吕梁',2),(307,23,'朔州',2),(308,23,'忻州',2),(309,23,'阳泉',2),(310,23,'运城',2),(311,24,'西安',2),(312,24,'安康',2),(313,24,'宝鸡',2),(314,24,'汉中',2),(315,24,'商洛',2),(316,24,'铜川',2),(317,24,'渭南',2),(318,24,'咸阳',2),(319,24,'延安',2),(320,24,'榆林',2),(321,25,'上海',2),(322,26,'成都',2),(323,26,'绵阳',2),(324,26,'阿坝',2),(325,26,'巴中',2),(326,26,'达州',2),(327,26,'德阳',2),(328,26,'甘孜',2),(329,26,'广安',2),(330,26,'广元',2),(331,26,'乐山',2),(332,26,'凉山',2),(333,26,'眉山',2),(334,26,'南充',2),(335,26,'内江',2),(336,26,'攀枝花',2),(337,26,'遂宁',2),(338,26,'雅安',2),(339,26,'宜宾',2),(340,26,'资阳',2),(341,26,'自贡',2),(342,26,'泸州',2),(343,27,'天津',2),(344,28,'拉萨',2),(345,28,'阿里',2),(346,28,'昌都',2),(347,28,'林芝',2),(348,28,'那曲',2),(349,28,'日喀则',2),(350,28,'山南',2),(351,29,'乌鲁木齐',2),(352,29,'阿克苏',2),(353,29,'阿拉尔',2),(354,29,'巴音郭楞',2),(355,29,'博尔塔拉',2),(356,29,'昌吉',2),(357,29,'哈密',2),(358,29,'和田',2),(359,29,'喀什',2),(360,29,'克拉玛依',2),(361,29,'克孜勒苏',2),(362,29,'石河子',2),(363,29,'图木舒克',2),(364,29,'吐鲁番',2),(365,29,'五家渠',2),(366,29,'伊犁',2),(367,30,'昆明',2),(368,30,'怒江',2),(369,30,'普洱',2),(370,30,'丽江',2),(371,30,'保山',2),(372,30,'楚雄',2),(373,30,'大理',2),(374,30,'德宏',2),(375,30,'迪庆',2),(376,30,'红河',2),(377,30,'临沧',2),(378,30,'曲靖',2),(379,30,'文山',2),(380,30,'西双版纳',2),(381,30,'玉溪',2),(382,30,'昭通',2),(383,31,'杭州',2),(384,31,'湖州',2),(385,31,'嘉兴',2),(386,31,'金华',2),(387,31,'丽水',2),(388,31,'宁波',2),(389,31,'绍兴',2),(390,31,'台州',2),(391,31,'温州',2),(392,31,'舟山',2),(393,31,'衢州',2),(394,32,'重庆',2),(395,33,'香港',2),(396,34,'澳门',2),(397,35,'台湾',2),(398,36,'迎江区',3),(399,36,'大观区',3),(400,36,'宜秀区',3),(401,36,'桐城市',3),(402,36,'怀宁县',3),(403,36,'枞阳县',3),(404,36,'潜山县',3),(405,36,'太湖县',3),(406,36,'宿松县',3),(407,36,'望江县',3),(408,36,'岳西县',3),(409,37,'中市区',3),(410,37,'东市区',3),(411,37,'西市区',3),(412,37,'郊区',3),(413,37,'怀远县',3),(414,37,'五河县',3),(415,37,'固镇县',3),(416,38,'居巢区',3),(417,38,'庐江县',3),(418,38,'无为县',3),(419,38,'含山县',3),(420,38,'和县',3),(421,39,'贵池区',3),(422,39,'东至县',3),(423,39,'石台县',3),(424,39,'青阳县',3),(425,40,'琅琊区',3),(426,40,'南谯区',3),(427,40,'天长市',3),(428,40,'明光市',3),(429,40,'来安县',3),(430,40,'全椒县',3),(431,40,'定远县',3),(432,40,'凤阳县',3),(433,41,'蚌山区',3),(434,41,'龙子湖区',3),(435,41,'禹会区',3),(436,41,'淮上区',3),(437,41,'颍州区',3),(438,41,'颍东区',3),(439,41,'颍泉区',3),(440,41,'界首市',3),(441,41,'临泉县',3),(442,41,'太和县',3),(443,41,'阜南县',3),(444,41,'颖上县',3),(445,42,'相山区',3),(446,42,'杜集区',3),(447,42,'烈山区',3),(448,42,'濉溪县',3),(449,43,'田家庵区',3),(450,43,'大通区',3),(451,43,'谢家集区',3),(452,43,'八公山区',3),(453,43,'潘集区',3),(454,43,'凤台县',3),(455,44,'屯溪区',3),(456,44,'黄山区',3),(457,44,'徽州区',3),(458,44,'歙县',3),(459,44,'休宁县',3),(460,44,'黟县',3),(461,44,'祁门县',3),(462,45,'金安区',3),(463,45,'裕安区',3),(464,45,'寿县',3),(465,45,'霍邱县',3),(466,45,'舒城县',3),(467,45,'金寨县',3),(468,45,'霍山县',3),(469,46,'雨山区',3),(470,46,'花山区',3),(471,46,'金家庄区',3),(472,46,'当涂县',3),(473,47,'埇桥区',3),(474,47,'砀山县',3),(475,47,'萧县',3),(476,47,'灵璧县',3),(477,47,'泗县',3),(478,48,'铜官山区',3),(479,48,'狮子山区',3),(480,48,'郊区',3),(481,48,'铜陵县',3),(482,49,'镜湖区',3),(483,49,'弋江区',3),(484,49,'鸠江区',3),(485,49,'三山区',3),(486,49,'芜湖县',3),(487,49,'繁昌县',3),(488,49,'南陵县',3),(489,50,'宣州区',3),(490,50,'宁国市',3),(491,50,'郎溪县',3),(492,50,'广德县',3),(493,50,'泾县',3),(494,50,'绩溪县',3),(495,50,'旌德县',3),(496,51,'涡阳县',3),(497,51,'蒙城县',3),(498,51,'利辛县',3),(499,51,'谯城区',3),(500,52,'东城区',3),(501,52,'西城区',3),(502,52,'海淀区',3),(503,52,'朝阳区',3),(504,52,'崇文区',3),(505,52,'宣武区',3),(506,52,'丰台区',3),(507,52,'石景山区',3),(508,52,'房山区',3),(509,52,'门头沟区',3),(510,52,'通州区',3),(511,52,'顺义区',3),(512,52,'昌平区',3),(513,52,'怀柔区',3),(514,52,'平谷区',3),(515,52,'大兴区',3),(516,52,'密云县',3),(517,52,'延庆县',3),(518,53,'鼓楼区',3),(519,53,'台江区',3),(520,53,'仓山区',3),(521,53,'马尾区',3),(522,53,'晋安区',3),(523,53,'福清市',3),(524,53,'长乐市',3),(525,53,'闽侯县',3),(526,53,'连江县',3),(527,53,'罗源县',3),(528,53,'闽清县',3),(529,53,'永泰县',3),(530,53,'平潭县',3),(531,54,'新罗区',3),(532,54,'漳平市',3),(533,54,'长汀县',3),(534,54,'永定县',3),(535,54,'上杭县',3),(536,54,'武平县',3),(537,54,'连城县',3),(538,55,'延平区',3),(539,55,'邵武市',3),(540,55,'武夷山市',3),(541,55,'建瓯市',3),(542,55,'建阳市',3),(543,55,'顺昌县',3),(544,55,'浦城县',3),(545,55,'光泽县',3),(546,55,'松溪县',3),(547,55,'政和县',3),(548,56,'蕉城区',3),(549,56,'福安市',3),(550,56,'福鼎市',3),(551,56,'霞浦县',3),(552,56,'古田县',3),(553,56,'屏南县',3),(554,56,'寿宁县',3),(555,56,'周宁县',3),(556,56,'柘荣县',3),(557,57,'城厢区',3),(558,57,'涵江区',3),(559,57,'荔城区',3),(560,57,'秀屿区',3),(561,57,'仙游县',3),(562,58,'鲤城区',3),(563,58,'丰泽区',3),(564,58,'洛江区',3),(565,58,'清濛开发区',3),(566,58,'泉港区',3),(567,58,'石狮市',3),(568,58,'晋江市',3),(569,58,'南安市',3),(570,58,'惠安县',3),(571,58,'安溪县',3),(572,58,'永春县',3),(573,58,'德化县',3),(574,58,'金门县',3),(575,59,'梅列区',3),(576,59,'三元区',3),(577,59,'永安市',3),(578,59,'明溪县',3),(579,59,'清流县',3),(580,59,'宁化县',3),(581,59,'大田县',3),(582,59,'尤溪县',3),(583,59,'沙县',3),(584,59,'将乐县',3),(585,59,'泰宁县',3),(586,59,'建宁县',3),(587,60,'思明区',3),(588,60,'海沧区',3),(589,60,'湖里区',3),(590,60,'集美区',3),(591,60,'同安区',3),(592,60,'翔安区',3),(593,61,'芗城区',3),(594,61,'龙文区',3),(595,61,'龙海市',3),(596,61,'云霄县',3),(597,61,'漳浦县',3),(598,61,'诏安县',3),(599,61,'长泰县',3),(600,61,'东山县',3),(601,61,'南靖县',3),(602,61,'平和县',3),(603,61,'华安县',3),(604,62,'皋兰县',3),(605,62,'城关区',3),(606,62,'七里河区',3),(607,62,'西固区',3),(608,62,'安宁区',3),(609,62,'红古区',3),(610,62,'永登县',3),(611,62,'榆中县',3),(612,63,'白银区',3),(613,63,'平川区',3),(614,63,'会宁县',3),(615,63,'景泰县',3),(616,63,'靖远县',3),(617,64,'临洮县',3),(618,64,'陇西县',3),(619,64,'通渭县',3),(620,64,'渭源县',3),(621,64,'漳县',3),(622,64,'岷县',3),(623,64,'安定区',3),(624,64,'安定区',3),(625,65,'合作市',3),(626,65,'临潭县',3),(627,65,'卓尼县',3),(628,65,'舟曲县',3),(629,65,'迭部县',3),(630,65,'玛曲县',3),(631,65,'碌曲县',3),(632,65,'夏河县',3),(633,66,'嘉峪关市',3),(634,67,'金川区',3),(635,67,'永昌县',3),(636,68,'肃州区',3),(637,68,'玉门市',3),(638,68,'敦煌市',3),(639,68,'金塔县',3),(640,68,'瓜州县',3),(641,68,'肃北',3),(642,68,'阿克塞',3),(643,69,'临夏市',3),(644,69,'临夏县',3),(645,69,'康乐县',3),(646,69,'永靖县',3),(647,69,'广河县',3),(648,69,'和政县',3),(649,69,'东乡族自治县',3),(650,69,'积石山',3),(651,70,'成县',3),(652,70,'徽县',3),(653,70,'康县',3),(654,70,'礼县',3),(655,70,'两当县',3),(656,70,'文县',3),(657,70,'西和县',3),(658,70,'宕昌县',3),(659,70,'武都区',3),(660,71,'崇信县',3),(661,71,'华亭县',3),(662,71,'静宁县',3),(663,71,'灵台县',3),(664,71,'崆峒区',3),(665,71,'庄浪县',3),(666,71,'泾川县',3),(667,72,'合水县',3),(668,72,'华池县',3),(669,72,'环县',3),(670,72,'宁县',3),(671,72,'庆城县',3),(672,72,'西峰区',3),(673,72,'镇原县',3),(674,72,'正宁县',3),(675,73,'甘谷县',3),(676,73,'秦安县',3),(677,73,'清水县',3),(678,73,'秦州区',3),(679,73,'麦积区',3),(680,73,'武山县',3),(681,73,'张家川',3),(682,74,'古浪县',3),(683,74,'民勤县',3),(684,74,'天祝',3),(685,74,'凉州区',3),(686,75,'高台县',3),(687,75,'临泽县',3),(688,75,'民乐县',3),(689,75,'山丹县',3),(690,75,'肃南',3),(691,75,'甘州区',3),(692,76,'从化市',3),(693,76,'天河区',3),(694,76,'东山区',3),(695,76,'白云区',3),(696,76,'海珠区',3),(697,76,'荔湾区',3),(698,76,'越秀区',3),(699,76,'黄埔区',3),(700,76,'番禺区',3),(701,76,'花都区',3),(702,76,'增城区',3),(703,76,'从化区',3),(704,76,'市郊',3),(705,77,'福田区',3),(706,77,'罗湖区',3),(707,77,'南山区',3),(708,77,'宝安区',3),(709,77,'龙岗区',3),(710,77,'盐田区',3),(711,78,'湘桥区',3),(712,78,'潮安县',3),(713,78,'饶平县',3),(714,79,'南城区',3),(715,79,'东城区',3),(716,79,'万江区',3),(717,79,'莞城区',3),(718,79,'石龙镇',3),(719,79,'虎门镇',3),(720,79,'麻涌镇',3),(721,79,'道滘镇',3),(722,79,'石碣镇',3),(723,79,'沙田镇',3),(724,79,'望牛墩镇',3),(725,79,'洪梅镇',3),(726,79,'茶山镇',3),(727,79,'寮步镇',3),(728,79,'大岭山镇',3),(729,79,'大朗镇',3),(730,79,'黄江镇',3),(731,79,'樟木头',3),(732,79,'凤岗镇',3),(733,79,'塘厦镇',3),(734,79,'谢岗镇',3),(735,79,'厚街镇',3),(736,79,'清溪镇',3),(737,79,'常平镇',3),(738,79,'桥头镇',3),(739,79,'横沥镇',3),(740,79,'东坑镇',3),(741,79,'企石镇',3),(742,79,'石排镇',3),(743,79,'长安镇',3),(744,79,'中堂镇',3),(745,79,'高埗镇',3),(746,80,'禅城区',3),(747,80,'南海区',3),(748,80,'顺德区',3),(749,80,'三水区',3),(750,80,'高明区',3),(751,81,'东源县',3),(752,81,'和平县',3),(753,81,'源城区',3),(754,81,'连平县',3),(755,81,'龙川县',3),(756,81,'紫金县',3),(757,82,'惠阳区',3),(758,82,'惠城区',3),(759,82,'大亚湾',3),(760,82,'博罗县',3),(761,82,'惠东县',3),(762,82,'龙门县',3),(763,83,'江海区',3),(764,83,'蓬江区',3),(765,83,'新会区',3),(766,83,'台山市',3),(767,83,'开平市',3),(768,83,'鹤山市',3),(769,83,'恩平市',3),(770,84,'榕城区',3),(771,84,'普宁市',3),(772,84,'揭东县',3),(773,84,'揭西县',3),(774,84,'惠来县',3),(775,85,'茂南区',3),(776,85,'茂港区',3),(777,85,'高州市',3),(778,85,'化州市',3),(779,85,'信宜市',3),(780,85,'电白县',3),(781,86,'梅县',3),(782,86,'梅江区',3),(783,86,'兴宁市',3),(784,86,'大埔县',3),(785,86,'丰顺县',3),(786,86,'五华县',3),(787,86,'平远县',3),(788,86,'蕉岭县',3),(789,87,'清城区',3),(790,87,'英德市',3),(791,87,'连州市',3),(792,87,'佛冈县',3),(793,87,'阳山县',3),(794,87,'清新县',3),(795,87,'连山',3),(796,87,'连南',3),(797,88,'南澳县',3),(798,88,'潮阳区',3),(799,88,'澄海区',3),(800,88,'龙湖区',3),(801,88,'金平区',3),(802,88,'濠江区',3),(803,88,'潮南区',3),(804,89,'城区',3),(805,89,'陆丰市',3),(806,89,'海丰县',3),(807,89,'陆河县',3),(808,90,'曲江县',3),(809,90,'浈江区',3),(810,90,'武江区',3),(811,90,'曲江区',3),(812,90,'乐昌市',3),(813,90,'南雄市',3),(814,90,'始兴县',3),(815,90,'仁化县',3),(816,90,'翁源县',3),(817,90,'新丰县',3),(818,90,'乳源',3),(819,91,'江城区',3),(820,91,'阳春市',3),(821,91,'阳西县',3),(822,91,'阳东县',3),(823,92,'云城区',3),(824,92,'罗定市',3),(825,92,'新兴县',3),(826,92,'郁南县',3),(827,92,'云安县',3),(828,93,'赤坎区',3),(829,93,'霞山区',3),(830,93,'坡头区',3),(831,93,'麻章区',3),(832,93,'廉江市',3),(833,93,'雷州市',3),(834,93,'吴川市',3),(835,93,'遂溪县',3),(836,93,'徐闻县',3),(837,94,'肇庆市',3),(838,94,'高要市',3),(839,94,'四会市',3),(840,94,'广宁县',3),(841,94,'怀集县',3),(842,94,'封开县',3),(843,94,'德庆县',3),(844,95,'石岐街道',3),(845,95,'东区街道',3),(846,95,'西区街道',3),(847,95,'环城街道',3),(848,95,'中山港街道',3),(849,95,'五桂山街道',3),(850,96,'香洲区',3),(851,96,'斗门区',3),(852,96,'金湾区',3),(853,97,'邕宁区',3),(854,97,'青秀区',3),(855,97,'兴宁区',3),(856,97,'良庆区',3),(857,97,'西乡塘区',3),(858,97,'江南区',3),(859,97,'武鸣县',3),(860,97,'隆安县',3),(861,97,'马山县',3),(862,97,'上林县',3),(863,97,'宾阳县',3),(864,97,'横县',3),(865,98,'秀峰区',3),(866,98,'叠彩区',3),(867,98,'象山区',3),(868,98,'七星区',3),(869,98,'雁山区',3),(870,98,'阳朔县',3),(871,98,'临桂县',3),(872,98,'灵川县',3),(873,98,'全州县',3),(874,98,'平乐县',3),(875,98,'兴安县',3),(876,98,'灌阳县',3),(877,98,'荔浦县',3),(878,98,'资源县',3),(879,98,'永福县',3),(880,98,'龙胜',3),(881,98,'恭城',3),(882,99,'右江区',3),(883,99,'凌云县',3),(884,99,'平果县',3),(885,99,'西林县',3),(886,99,'乐业县',3),(887,99,'德保县',3),(888,99,'田林县',3),(889,99,'田阳县',3),(890,99,'靖西县',3),(891,99,'田东县',3),(892,99,'那坡县',3),(893,99,'隆林',3),(894,100,'海城区',3),(895,100,'银海区',3),(896,100,'铁山港区',3),(897,100,'合浦县',3),(898,101,'江州区',3),(899,101,'凭祥市',3),(900,101,'宁明县',3),(901,101,'扶绥县',3),(902,101,'龙州县',3),(903,101,'大新县',3),(904,101,'天等县',3),(905,102,'港口区',3),(906,102,'防城区',3),(907,102,'东兴市',3),(908,102,'上思县',3),(909,103,'港北区',3),(910,103,'港南区',3),(911,103,'覃塘区',3),(912,103,'桂平市',3),(913,103,'平南县',3),(914,104,'金城江区',3),(915,104,'宜州市',3),(916,104,'天峨县',3),(917,104,'凤山县',3),(918,104,'南丹县',3),(919,104,'东兰县',3),(920,104,'都安',3),(921,104,'罗城',3),(922,104,'巴马',3),(923,104,'环江',3),(924,104,'大化',3),(925,105,'八步区',3),(926,105,'钟山县',3),(927,105,'昭平县',3),(928,105,'富川',3),(929,106,'兴宾区',3),(930,106,'合山市',3),(931,106,'象州县',3),(932,106,'武宣县',3),(933,106,'忻城县',3),(934,106,'金秀',3),(935,107,'城中区',3),(936,107,'鱼峰区',3),(937,107,'柳北区',3),(938,107,'柳南区',3),(939,107,'柳江县',3),(940,107,'柳城县',3),(941,107,'鹿寨县',3),(942,107,'融安县',3),(943,107,'融水',3),(944,107,'三江',3),(945,108,'钦南区',3),(946,108,'钦北区',3),(947,108,'灵山县',3),(948,108,'浦北县',3),(949,109,'万秀区',3),(950,109,'蝶山区',3),(951,109,'长洲区',3),(952,109,'岑溪市',3),(953,109,'苍梧县',3),(954,109,'藤县',3),(955,109,'蒙山县',3),(956,110,'玉州区',3),(957,110,'北流市',3),(958,110,'容县',3),(959,110,'陆川县',3),(960,110,'博白县',3),(961,110,'兴业县',3),(962,111,'南明区',3),(963,111,'云岩区',3),(964,111,'花溪区',3),(965,111,'乌当区',3),(966,111,'白云区',3),(967,111,'小河区',3),(968,111,'金阳新区',3),(969,111,'新天园区',3),(970,111,'清镇市',3),(971,111,'开阳县',3),(972,111,'修文县',3),(973,111,'息烽县',3),(974,112,'西秀区',3),(975,112,'关岭',3),(976,112,'镇宁',3),(977,112,'紫云',3),(978,112,'平坝县',3),(979,112,'普定县',3),(980,113,'毕节市',3),(981,113,'大方县',3),(982,113,'黔西县',3),(983,113,'金沙县',3),(984,113,'织金县',3),(985,113,'纳雍县',3),(986,113,'赫章县',3),(987,113,'威宁',3),(988,114,'钟山区',3),(989,114,'六枝特区',3),(990,114,'水城县',3),(991,114,'盘县',3),(992,115,'凯里市',3),(993,115,'黄平县',3),(994,115,'施秉县',3),(995,115,'三穗县',3),(996,115,'镇远县',3),(997,115,'岑巩县',3),(998,115,'天柱县',3),(999,115,'锦屏县',3),(1000,115,'剑河县',3),(1001,115,'台江县',3),(1002,115,'黎平县',3),(1003,115,'榕江县',3),(1004,115,'从江县',3),(1005,115,'雷山县',3),(1006,115,'麻江县',3),(1007,115,'丹寨县',3),(1008,116,'都匀市',3),(1009,116,'福泉市',3),(1010,116,'荔波县',3),(1011,116,'贵定县',3),(1012,116,'瓮安县',3),(1013,116,'独山县',3),(1014,116,'平塘县',3),(1015,116,'罗甸县',3),(1016,116,'长顺县',3),(1017,116,'龙里县',3),(1018,116,'惠水县',3),(1019,116,'三都',3),(1020,117,'兴义市',3),(1021,117,'兴仁县',3),(1022,117,'普安县',3),(1023,117,'晴隆县',3),(1024,117,'贞丰县',3),(1025,117,'望谟县',3),(1026,117,'册亨县',3),(1027,117,'安龙县',3),(1028,118,'铜仁市',3),(1029,118,'江口县',3),(1030,118,'石阡县',3),(1031,118,'思南县',3),(1032,118,'德江县',3),(1033,118,'玉屏',3),(1034,118,'印江',3),(1035,118,'沿河',3),(1036,118,'松桃',3),(1037,118,'万山特区',3),(1038,119,'红花岗区',3),(1039,119,'务川县',3),(1040,119,'道真县',3),(1041,119,'汇川区',3),(1042,119,'赤水市',3),(1043,119,'仁怀市',3),(1044,119,'遵义县',3),(1045,119,'桐梓县',3),(1046,119,'绥阳县',3),(1047,119,'正安县',3),(1048,119,'凤冈县',3),(1049,119,'湄潭县',3),(1050,119,'余庆县',3),(1051,119,'习水县',3),(1052,119,'道真',3),(1053,119,'务川',3),(1054,120,'秀英区',3),(1055,120,'龙华区',3),(1056,120,'琼山区',3),(1057,120,'美兰区',3),(1058,137,'市区',3),(1059,137,'洋浦开发区',3),(1060,137,'那大镇',3),(1061,137,'王五镇',3),(1062,137,'雅星镇',3),(1063,137,'大成镇',3),(1064,137,'中和镇',3),(1065,137,'峨蔓镇',3),(1066,137,'南丰镇',3),(1067,137,'白马井镇',3),(1068,137,'兰洋镇',3),(1069,137,'和庆镇',3),(1070,137,'海头镇',3),(1071,137,'排浦镇',3),(1072,137,'东成镇',3),(1073,137,'光村镇',3),(1074,137,'木棠镇',3),(1075,137,'新州镇',3),(1076,137,'三都镇',3),(1077,137,'其他',3),(1078,138,'长安区',3),(1079,138,'桥东区',3),(1080,138,'桥西区',3),(1081,138,'新华区',3),(1082,138,'裕华区',3),(1083,138,'井陉矿区',3),(1084,138,'高新区',3),(1085,138,'辛集市',3),(1086,138,'藁城市',3),(1087,138,'晋州市',3),(1088,138,'新乐市',3),(1089,138,'鹿泉市',3),(1090,138,'井陉县',3),(1091,138,'正定县',3),(1092,138,'栾城县',3),(1093,138,'行唐县',3),(1094,138,'灵寿县',3),(1095,138,'高邑县',3),(1096,138,'深泽县',3),(1097,138,'赞皇县',3),(1098,138,'无极县',3),(1099,138,'平山县',3),(1100,138,'元氏县',3),(1101,138,'赵县',3),(1102,139,'新市区',3),(1103,139,'南市区',3),(1104,139,'北市区',3),(1105,139,'涿州市',3),(1106,139,'定州市',3),(1107,139,'安国市',3),(1108,139,'高碑店市',3),(1109,139,'满城县',3),(1110,139,'清苑县',3),(1111,139,'涞水县',3),(1112,139,'阜平县',3),(1113,139,'徐水县',3),(1114,139,'定兴县',3),(1115,139,'唐县',3),(1116,139,'高阳县',3),(1117,139,'容城县',3),(1118,139,'涞源县',3),(1119,139,'望都县',3),(1120,139,'安新县',3),(1121,139,'易县',3),(1122,139,'曲阳县',3),(1123,139,'蠡县',3),(1124,139,'顺平县',3),(1125,139,'博野县',3),(1126,139,'雄县',3),(1127,140,'运河区',3),(1128,140,'新华区',3),(1129,140,'泊头市',3),(1130,140,'任丘市',3),(1131,140,'黄骅市',3),(1132,140,'河间市',3),(1133,140,'沧县',3),(1134,140,'青县',3),(1135,140,'东光县',3),(1136,140,'海兴县',3),(1137,140,'盐山县',3),(1138,140,'肃宁县',3),(1139,140,'南皮县',3),(1140,140,'吴桥县',3),(1141,140,'献县',3),(1142,140,'孟村',3),(1143,141,'双桥区',3),(1144,141,'双滦区',3),(1145,141,'鹰手营子矿区',3),(1146,141,'承德县',3),(1147,141,'兴隆县',3),(1148,141,'平泉县',3),(1149,141,'滦平县',3),(1150,141,'隆化县',3),(1151,141,'丰宁',3),(1152,141,'宽城',3),(1153,141,'围场',3),(1154,142,'从台区',3),(1155,142,'复兴区',3),(1156,142,'邯山区',3),(1157,142,'峰峰矿区',3),(1158,142,'武安市',3),(1159,142,'邯郸县',3),(1160,142,'临漳县',3),(1161,142,'成安县',3),(1162,142,'大名县',3),(1163,142,'涉县',3),(1164,142,'磁县',3),(1165,142,'肥乡县',3),(1166,142,'永年县',3),(1167,142,'邱县',3),(1168,142,'鸡泽县',3),(1169,142,'广平县',3),(1170,142,'馆陶县',3),(1171,142,'魏县',3),(1172,142,'曲周县',3),(1173,143,'桃城区',3),(1174,143,'冀州市',3),(1175,143,'深州市',3),(1176,143,'枣强县',3),(1177,143,'武邑县',3),(1178,143,'武强县',3),(1179,143,'饶阳县',3),(1180,143,'安平县',3),(1181,143,'故城县',3),(1182,143,'景县',3),(1183,143,'阜城县',3),(1184,144,'安次区',3),(1185,144,'广阳区',3),(1186,144,'霸州市',3),(1187,144,'三河市',3),(1188,144,'固安县',3),(1189,144,'永清县',3),(1190,144,'香河县',3),(1191,144,'大城县',3),(1192,144,'文安县',3),(1193,144,'大厂',3),(1194,145,'海港区',3),(1195,145,'山海关区',3),(1196,145,'北戴河区',3),(1197,145,'昌黎县',3),(1198,145,'抚宁县',3),(1199,145,'卢龙县',3),(1200,145,'青龙',3),(1201,146,'路北区',3),(1202,146,'路南区',3),(1203,146,'古冶区',3),(1204,146,'开平区',3),(1205,146,'丰南区',3),(1206,146,'丰润区',3),(1207,146,'遵化市',3),(1208,146,'迁安市',3),(1209,146,'滦县',3),(1210,146,'滦南县',3),(1211,146,'乐亭县',3),(1212,146,'迁西县',3),(1213,146,'玉田县',3),(1214,146,'唐海县',3),(1215,147,'桥东区',3),(1216,147,'桥西区',3),(1217,147,'南宫市',3),(1218,147,'沙河市',3),(1219,147,'邢台县',3),(1220,147,'临城县',3),(1221,147,'内丘县',3),(1222,147,'柏乡县',3),(1223,147,'隆尧县',3),(1224,147,'任县',3),(1225,147,'南和县',3),(1226,147,'宁晋县',3),(1227,147,'巨鹿县',3),(1228,147,'新河县',3),(1229,147,'广宗县',3),(1230,147,'平乡县',3),(1231,147,'威县',3),(1232,147,'清河县',3),(1233,147,'临西县',3),(1234,148,'桥西区',3),(1235,148,'桥东区',3),(1236,148,'宣化区',3),(1237,148,'下花园区',3),(1238,148,'宣化县',3),(1239,148,'张北县',3),(1240,148,'康保县',3),(1241,148,'沽源县',3),(1242,148,'尚义县',3),(1243,148,'蔚县',3),(1244,148,'阳原县',3),(1245,148,'怀安县',3),(1246,148,'万全县',3),(1247,148,'怀来县',3),(1248,148,'涿鹿县',3),(1249,148,'赤城县',3),(1250,148,'崇礼县',3),(1251,149,'金水区',3),(1252,149,'邙山区',3),(1253,149,'二七区',3),(1254,149,'管城区',3),(1255,149,'中原区',3),(1256,149,'上街区',3),(1257,149,'惠济区',3),(1258,149,'郑东新区',3),(1259,149,'经济技术开发区',3),(1260,149,'高新开发区',3),(1261,149,'出口加工区',3),(1262,149,'巩义市',3),(1263,149,'荥阳市',3),(1264,149,'新密市',3),(1265,149,'新郑市',3),(1266,149,'登封市',3),(1267,149,'中牟县',3),(1268,150,'西工区',3),(1269,150,'老城区',3),(1270,150,'涧西区',3),(1271,150,'瀍河回族区',3),(1272,150,'洛龙区',3),(1273,150,'吉利区',3),(1274,150,'偃师市',3),(1275,150,'孟津县',3),(1276,150,'新安县',3),(1277,150,'栾川县',3),(1278,150,'嵩县',3),(1279,150,'汝阳县',3),(1280,150,'宜阳县',3),(1281,150,'洛宁县',3),(1282,150,'伊川县',3),(1283,151,'鼓楼区',3),(1284,151,'龙亭区',3),(1285,151,'顺河回族区',3),(1286,151,'金明区',3),(1287,151,'禹王台区',3),(1288,151,'杞县',3),(1289,151,'通许县',3),(1290,151,'尉氏县',3),(1291,151,'开封县',3),(1292,151,'兰考县',3),(1293,152,'北关区',3),(1294,152,'文峰区',3),(1295,152,'殷都区',3),(1296,152,'龙安区',3),(1297,152,'林州市',3),(1298,152,'安阳县',3),(1299,152,'汤阴县',3),(1300,152,'滑县',3),(1301,152,'内黄县',3),(1302,153,'淇滨区',3),(1303,153,'山城区',3),(1304,153,'鹤山区',3),(1305,153,'浚县',3),(1306,153,'淇县',3),(1307,154,'济源市',3),(1308,155,'解放区',3),(1309,155,'中站区',3),(1310,155,'马村区',3),(1311,155,'山阳区',3),(1312,155,'沁阳市',3),(1313,155,'孟州市',3),(1314,155,'修武县',3),(1315,155,'博爱县',3),(1316,155,'武陟县',3),(1317,155,'温县',3),(1318,156,'卧龙区',3),(1319,156,'宛城区',3),(1320,156,'邓州市',3),(1321,156,'南召县',3),(1322,156,'方城县',3),(1323,156,'西峡县',3),(1324,156,'镇平县',3),(1325,156,'内乡县',3),(1326,156,'淅川县',3),(1327,156,'社旗县',3),(1328,156,'唐河县',3),(1329,156,'新野县',3),(1330,156,'桐柏县',3),(1331,157,'新华区',3),(1332,157,'卫东区',3),(1333,157,'湛河区',3),(1334,157,'石龙区',3),(1335,157,'舞钢市',3),(1336,157,'汝州市',3),(1337,157,'宝丰县',3),(1338,157,'叶县',3),(1339,157,'鲁山县',3),(1340,157,'郏县',3),(1341,158,'湖滨区',3),(1342,158,'义马市',3),(1343,158,'灵宝市',3),(1344,158,'渑池县',3),(1345,158,'陕县',3),(1346,158,'卢氏县',3),(1347,159,'梁园区',3),(1348,159,'睢阳区',3),(1349,159,'永城市',3),(1350,159,'民权县',3),(1351,159,'睢县',3),(1352,159,'宁陵县',3),(1353,159,'虞城县',3),(1354,159,'柘城县',3),(1355,159,'夏邑县',3),(1356,160,'卫滨区',3),(1357,160,'红旗区',3),(1358,160,'凤泉区',3),(1359,160,'牧野区',3),(1360,160,'卫辉市',3),(1361,160,'辉县市',3),(1362,160,'新乡县',3),(1363,160,'获嘉县',3),(1364,160,'原阳县',3),(1365,160,'延津县',3),(1366,160,'封丘县',3),(1367,160,'长垣县',3),(1368,161,'浉河区',3),(1369,161,'平桥区',3),(1370,161,'罗山县',3),(1371,161,'光山县',3),(1372,161,'新县',3),(1373,161,'商城县',3),(1374,161,'固始县',3),(1375,161,'潢川县',3),(1376,161,'淮滨县',3),(1377,161,'息县',3),(1378,162,'魏都区',3),(1379,162,'禹州市',3),(1380,162,'长葛市',3),(1381,162,'许昌县',3),(1382,162,'鄢陵县',3),(1383,162,'襄城县',3),(1384,163,'川汇区',3),(1385,163,'项城市',3),(1386,163,'扶沟县',3),(1387,163,'西华县',3),(1388,163,'商水县',3),(1389,163,'沈丘县',3),(1390,163,'郸城县',3),(1391,163,'淮阳县',3),(1392,163,'太康县',3),(1393,163,'鹿邑县',3),(1394,164,'驿城区',3),(1395,164,'西平县',3),(1396,164,'上蔡县',3),(1397,164,'平舆县',3),(1398,164,'正阳县',3),(1399,164,'确山县',3),(1400,164,'泌阳县',3),(1401,164,'汝南县',3),(1402,164,'遂平县',3),(1403,164,'新蔡县',3),(1404,165,'郾城区',3),(1405,165,'源汇区',3),(1406,165,'召陵区',3),(1407,165,'舞阳县',3),(1408,165,'临颍县',3),(1409,166,'华龙区',3),(1410,166,'清丰县',3),(1411,166,'南乐县',3),(1412,166,'范县',3),(1413,166,'台前县',3),(1414,166,'濮阳县',3),(1415,167,'道里区',3),(1416,167,'南岗区',3),(1417,167,'动力区',3),(1418,167,'平房区',3),(1419,167,'香坊区',3),(1420,167,'太平区',3),(1421,167,'道外区',3),(1422,167,'阿城区',3),(1423,167,'呼兰区',3),(1424,167,'松北区',3),(1425,167,'尚志市',3),(1426,167,'双城市',3),(1427,167,'五常市',3),(1428,167,'方正县',3),(1429,167,'宾县',3),(1430,167,'依兰县',3),(1431,167,'巴彦县',3),(1432,167,'通河县',3),(1433,167,'木兰县',3),(1434,167,'延寿县',3),(1435,168,'萨尔图区',3),(1436,168,'红岗区',3),(1437,168,'龙凤区',3),(1438,168,'让胡路区',3),(1439,168,'大同区',3),(1440,168,'肇州县',3),(1441,168,'肇源县',3),(1442,168,'林甸县',3),(1443,168,'杜尔伯特',3),(1444,169,'呼玛县',3),(1445,169,'漠河县',3),(1446,169,'塔河县',3),(1447,170,'兴山区',3),(1448,170,'工农区',3),(1449,170,'南山区',3),(1450,170,'兴安区',3),(1451,170,'向阳区',3),(1452,170,'东山区',3),(1453,170,'萝北县',3),(1454,170,'绥滨县',3),(1455,171,'爱辉区',3),(1456,171,'五大连池市',3),(1457,171,'北安市',3),(1458,171,'嫩江县',3),(1459,171,'逊克县',3),(1460,171,'孙吴县',3),(1461,172,'鸡冠区',3),(1462,172,'恒山区',3),(1463,172,'城子河区',3),(1464,172,'滴道区',3),(1465,172,'梨树区',3),(1466,172,'虎林市',3),(1467,172,'密山市',3),(1468,172,'鸡东县',3),(1469,173,'前进区',3),(1470,173,'郊区',3),(1471,173,'向阳区',3),(1472,173,'东风区',3),(1473,173,'同江市',3),(1474,173,'富锦市',3),(1475,173,'桦南县',3),(1476,173,'桦川县',3),(1477,173,'汤原县',3),(1478,173,'抚远县',3),(1479,174,'爱民区',3),(1480,174,'东安区',3),(1481,174,'阳明区',3),(1482,174,'西安区',3),(1483,174,'绥芬河市',3),(1484,174,'海林市',3),(1485,174,'宁安市',3),(1486,174,'穆棱市',3),(1487,174,'东宁县',3),(1488,174,'林口县',3),(1489,175,'桃山区',3),(1490,175,'新兴区',3),(1491,175,'茄子河区',3),(1492,175,'勃利县',3),(1493,176,'龙沙区',3),(1494,176,'昂昂溪区',3),(1495,176,'铁峰区',3),(1496,176,'建华区',3),(1497,176,'富拉尔基区',3),(1498,176,'碾子山区',3),(1499,176,'梅里斯达斡尔区',3),(1500,176,'讷河市',3),(1501,176,'龙江县',3),(1502,176,'依安县',3),(1503,176,'泰来县',3),(1504,176,'甘南县',3),(1505,176,'富裕县',3),(1506,176,'克山县',3),(1507,176,'克东县',3),(1508,176,'拜泉县',3),(1509,177,'尖山区',3),(1510,177,'岭东区',3),(1511,177,'四方台区',3),(1512,177,'宝山区',3),(1513,177,'集贤县',3),(1514,177,'友谊县',3),(1515,177,'宝清县',3),(1516,177,'饶河县',3),(1517,178,'北林区',3),(1518,178,'安达市',3),(1519,178,'肇东市',3),(1520,178,'海伦市',3),(1521,178,'望奎县',3),(1522,178,'兰西县',3),(1523,178,'青冈县',3),(1524,178,'庆安县',3),(1525,178,'明水县',3),(1526,178,'绥棱县',3),(1527,179,'伊春区',3),(1528,179,'带岭区',3),(1529,179,'南岔区',3),(1530,179,'金山屯区',3),(1531,179,'西林区',3),(1532,179,'美溪区',3),(1533,179,'乌马河区',3),(1534,179,'翠峦区',3),(1535,179,'友好区',3),(1536,179,'上甘岭区',3),(1537,179,'五营区',3),(1538,179,'红星区',3),(1539,179,'新青区',3),(1540,179,'汤旺河区',3),(1541,179,'乌伊岭区',3),(1542,179,'铁力市',3),(1543,179,'嘉荫县',3),(1544,180,'江岸区',3),(1545,180,'武昌区',3),(1546,180,'江汉区',3),(1547,180,'硚口区',3),(1548,180,'汉阳区',3),(1549,180,'青山区',3),(1550,180,'洪山区',3),(1551,180,'东西湖区',3),(1552,180,'汉南区',3),(1553,180,'蔡甸区',3),(1554,180,'江夏区',3),(1555,180,'黄陂区',3),(1556,180,'新洲区',3),(1557,180,'经济开发区',3),(1558,181,'仙桃市',3),(1559,182,'鄂城区',3),(1560,182,'华容区',3),(1561,182,'梁子湖区',3),(1562,183,'黄州区',3),(1563,183,'麻城市',3),(1564,183,'武穴市',3),(1565,183,'团风县',3),(1566,183,'红安县',3),(1567,183,'罗田县',3),(1568,183,'英山县',3),(1569,183,'浠水县',3),(1570,183,'蕲春县',3),(1571,183,'黄梅县',3),(1572,184,'黄石港区',3),(1573,184,'西塞山区',3),(1574,184,'下陆区',3),(1575,184,'铁山区',3),(1576,184,'大冶市',3),(1577,184,'阳新县',3),(1578,185,'东宝区',3),(1579,185,'掇刀区',3),(1580,185,'钟祥市',3),(1581,185,'京山县',3),(1582,185,'沙洋县',3),(1583,186,'沙市区',3),(1584,186,'荆州区',3),(1585,186,'石首市',3),(1586,186,'洪湖市',3),(1587,186,'松滋市',3),(1588,186,'公安县',3),(1589,186,'监利县',3),(1590,186,'江陵县',3),(1591,187,'潜江市',3),(1592,188,'神农架林区',3),(1593,189,'张湾区',3),(1594,189,'茅箭区',3),(1595,189,'丹江口市',3),(1596,189,'郧县',3),(1597,189,'郧西县',3),(1598,189,'竹山县',3),(1599,189,'竹溪县',3),(1600,189,'房县',3),(1601,190,'曾都区',3),(1602,190,'广水市',3),(1603,191,'天门市',3),(1604,192,'咸安区',3),(1605,192,'赤壁市',3),(1606,192,'嘉鱼县',3),(1607,192,'通城县',3),(1608,192,'崇阳县',3),(1609,192,'通山县',3),(1610,193,'襄城区',3),(1611,193,'樊城区',3),(1612,193,'襄阳区',3),(1613,193,'老河口市',3),(1614,193,'枣阳市',3),(1615,193,'宜城市',3),(1616,193,'南漳县',3),(1617,193,'谷城县',3),(1618,193,'保康县',3),(1619,194,'孝南区',3),(1620,194,'应城市',3),(1621,194,'安陆市',3),(1622,194,'汉川市',3),(1623,194,'孝昌县',3),(1624,194,'大悟县',3),(1625,194,'云梦县',3),(1626,195,'长阳',3),(1627,195,'五峰',3),(1628,195,'西陵区',3),(1629,195,'伍家岗区',3),(1630,195,'点军区',3),(1631,195,'猇亭区',3),(1632,195,'夷陵区',3),(1633,195,'宜都市',3),(1634,195,'当阳市',3),(1635,195,'枝江市',3),(1636,195,'远安县',3),(1637,195,'兴山县',3),(1638,195,'秭归县',3),(1639,196,'恩施市',3),(1640,196,'利川市',3),(1641,196,'建始县',3),(1642,196,'巴东县',3),(1643,196,'宣恩县',3),(1644,196,'咸丰县',3),(1645,196,'来凤县',3),(1646,196,'鹤峰县',3),(1647,197,'岳麓区',3),(1648,197,'芙蓉区',3),(1649,197,'天心区',3),(1650,197,'开福区',3),(1651,197,'雨花区',3),(1652,197,'开发区',3),(1653,197,'浏阳市',3),(1654,197,'长沙县',3),(1655,197,'望城县',3),(1656,197,'宁乡县',3),(1657,198,'永定区',3),(1658,198,'武陵源区',3),(1659,198,'慈利县',3),(1660,198,'桑植县',3),(1661,199,'武陵区',3),(1662,199,'鼎城区',3),(1663,199,'津市市',3),(1664,199,'安乡县',3),(1665,199,'汉寿县',3),(1666,199,'澧县',3),(1667,199,'临澧县',3),(1668,199,'桃源县',3),(1669,199,'石门县',3),(1670,200,'北湖区',3),(1671,200,'苏仙区',3),(1672,200,'资兴市',3),(1673,200,'桂阳县',3),(1674,200,'宜章县',3),(1675,200,'永兴县',3),(1676,200,'嘉禾县',3),(1677,200,'临武县',3),(1678,200,'汝城县',3),(1679,200,'桂东县',3),(1680,200,'安仁县',3),(1681,201,'雁峰区',3),(1682,201,'珠晖区',3),(1683,201,'石鼓区',3),(1684,201,'蒸湘区',3),(1685,201,'南岳区',3),(1686,201,'耒阳市',3),(1687,201,'常宁市',3),(1688,201,'衡阳县',3),(1689,201,'衡南县',3),(1690,201,'衡山县',3),(1691,201,'衡东县',3),(1692,201,'祁东县',3),(1693,202,'鹤城区',3),(1694,202,'靖州',3),(1695,202,'麻阳',3),(1696,202,'通道',3),(1697,202,'新晃',3),(1698,202,'芷江',3),(1699,202,'沅陵县',3),(1700,202,'辰溪县',3),(1701,202,'溆浦县',3),(1702,202,'中方县',3),(1703,202,'会同县',3),(1704,202,'洪江市',3),(1705,203,'娄星区',3),(1706,203,'冷水江市',3),(1707,203,'涟源市',3),(1708,203,'双峰县',3),(1709,203,'新化县',3),(1710,204,'城步',3),(1711,204,'双清区',3),(1712,204,'大祥区',3),(1713,204,'北塔区',3),(1714,204,'武冈市',3),(1715,204,'邵东县',3),(1716,204,'新邵县',3),(1717,204,'邵阳县',3),(1718,204,'隆回县',3),(1719,204,'洞口县',3),(1720,204,'绥宁县',3),(1721,204,'新宁县',3),(1722,205,'岳塘区',3),(1723,205,'雨湖区',3),(1724,205,'湘乡市',3),(1725,205,'韶山市',3),(1726,205,'湘潭县',3),(1727,206,'吉首市',3),(1728,206,'泸溪县',3),(1729,206,'凤凰县',3),(1730,206,'花垣县',3),(1731,206,'保靖县',3),(1732,206,'古丈县',3),(1733,206,'永顺县',3),(1734,206,'龙山县',3),(1735,207,'赫山区',3),(1736,207,'资阳区',3),(1737,207,'沅江市',3),(1738,207,'南县',3),(1739,207,'桃江县',3),(1740,207,'安化县',3),(1741,208,'江华',3),(1742,208,'冷水滩区',3),(1743,208,'零陵区',3),(1744,208,'祁阳县',3),(1745,208,'东安县',3),(1746,208,'双牌县',3),(1747,208,'道县',3),(1748,208,'江永县',3),(1749,208,'宁远县',3),(1750,208,'蓝山县',3),(1751,208,'新田县',3),(1752,209,'岳阳楼区',3),(1753,209,'君山区',3),(1754,209,'云溪区',3),(1755,209,'汨罗市',3),(1756,209,'临湘市',3),(1757,209,'岳阳县',3),(1758,209,'华容县',3),(1759,209,'湘阴县',3),(1760,209,'平江县',3),(1761,210,'天元区',3),(1762,210,'荷塘区',3),(1763,210,'芦淞区',3),(1764,210,'石峰区',3),(1765,210,'醴陵市',3),(1766,210,'株洲县',3),(1767,210,'攸县',3),(1768,210,'茶陵县',3),(1769,210,'炎陵县',3),(1770,211,'朝阳区',3),(1771,211,'宽城区',3),(1772,211,'二道区',3),(1773,211,'南关区',3),(1774,211,'绿园区',3),(1775,211,'双阳区',3),(1776,211,'净月潭开发区',3),(1777,211,'高新技术开发区',3),(1778,211,'经济技术开发区',3),(1779,211,'汽车产业开发区',3),(1780,211,'德惠市',3),(1781,211,'九台市',3),(1782,211,'榆树市',3),(1783,211,'农安县',3),(1784,212,'船营区',3),(1785,212,'昌邑区',3),(1786,212,'龙潭区',3),(1787,212,'丰满区',3),(1788,212,'蛟河市',3),(1789,212,'桦甸市',3),(1790,212,'舒兰市',3),(1791,212,'磐石市',3),(1792,212,'永吉县',3),(1793,213,'洮北区',3),(1794,213,'洮南市',3),(1795,213,'大安市',3),(1796,213,'镇赉县',3),(1797,213,'通榆县',3),(1798,214,'江源区',3),(1799,214,'八道江区',3),(1800,214,'长白',3),(1801,214,'临江市',3),(1802,214,'抚松县',3),(1803,214,'靖宇县',3),(1804,215,'龙山区',3),(1805,215,'西安区',3),(1806,215,'东丰县',3),(1807,215,'东辽县',3),(1808,216,'铁西区',3),(1809,216,'铁东区',3),(1810,216,'伊通',3),(1811,216,'公主岭市',3),(1812,216,'双辽市',3),(1813,216,'梨树县',3),(1814,217,'前郭尔罗斯',3),(1815,217,'宁江区',3),(1816,217,'长岭县',3),(1817,217,'乾安县',3),(1818,217,'扶余县',3),(1819,218,'东昌区',3),(1820,218,'二道江区',3),(1821,218,'梅河口市',3),(1822,218,'集安市',3),(1823,218,'通化县',3),(1824,218,'辉南县',3),(1825,218,'柳河县',3),(1826,219,'延吉市',3),(1827,219,'图们市',3),(1828,219,'敦化市',3),(1829,219,'珲春市',3),(1830,219,'龙井市',3),(1831,219,'和龙市',3),(1832,219,'安图县',3),(1833,219,'汪清县',3),(1834,220,'玄武区',3),(1835,220,'鼓楼区',3),(1836,220,'白下区',3),(1837,220,'建邺区',3),(1838,220,'秦淮区',3),(1839,220,'雨花台区',3),(1840,220,'下关区',3),(1841,220,'栖霞区',3),(1842,220,'浦口区',3),(1843,220,'江宁区',3),(1844,220,'六合区',3),(1845,220,'溧水县',3),(1846,220,'高淳县',3),(1847,221,'沧浪区',3),(1848,221,'金阊区',3),(1849,221,'平江区',3),(1850,221,'虎丘区',3),(1851,221,'吴中区',3),(1852,221,'相城区',3),(1853,221,'园区',3),(1854,221,'新区',3),(1855,221,'常熟市',3),(1856,221,'张家港市',3),(1857,221,'玉山镇',3),(1858,221,'巴城镇',3),(1859,221,'周市镇',3),(1860,221,'陆家镇',3),(1861,221,'花桥镇',3),(1862,221,'淀山湖镇',3),(1863,221,'张浦镇',3),(1864,221,'周庄镇',3),(1865,221,'千灯镇',3),(1866,221,'锦溪镇',3),(1867,221,'开发区',3),(1868,221,'吴江市',3),(1869,221,'太仓市',3),(1870,222,'崇安区',3),(1871,222,'北塘区',3),(1872,222,'南长区',3),(1873,222,'锡山区',3),(1874,222,'惠山区',3),(1875,222,'滨湖区',3),(1876,222,'新区',3),(1877,222,'江阴市',3),(1878,222,'宜兴市',3),(1879,223,'天宁区',3),(1880,223,'钟楼区',3),(1881,223,'戚墅堰区',3),(1882,223,'郊区',3),(1883,223,'新北区',3),(1884,223,'武进区',3),(1885,223,'溧阳市',3),(1886,223,'金坛市',3),(1887,224,'清河区',3),(1888,224,'清浦区',3),(1889,224,'楚州区',3),(1890,224,'淮阴区',3),(1891,224,'涟水县',3),(1892,224,'洪泽县',3),(1893,224,'盱眙县',3),(1894,224,'金湖县',3),(1895,225,'新浦区',3),(1896,225,'连云区',3),(1897,225,'海州区',3),(1898,225,'赣榆县',3),(1899,225,'东海县',3),(1900,225,'灌云县',3),(1901,225,'灌南县',3),(1902,226,'崇川区',3),(1903,226,'港闸区',3),(1904,226,'经济开发区',3),(1905,226,'启东市',3),(1906,226,'如皋市',3),(1907,226,'通州市',3),(1908,226,'海门市',3),(1909,226,'海安县',3),(1910,226,'如东县',3),(1911,227,'宿城区',3),(1912,227,'宿豫区',3),(1913,227,'宿豫县',3),(1914,227,'沭阳县',3),(1915,227,'泗阳县',3),(1916,227,'泗洪县',3),(1917,228,'海陵区',3),(1918,228,'高港区',3),(1919,228,'兴化市',3),(1920,228,'靖江市',3),(1921,228,'泰兴市',3),(1922,228,'姜堰市',3),(1923,229,'云龙区',3),(1924,229,'鼓楼区',3),(1925,229,'九里区',3),(1926,229,'贾汪区',3),(1927,229,'泉山区',3),(1928,229,'新沂市',3),(1929,229,'邳州市',3),(1930,229,'丰县',3),(1931,229,'沛县',3),(1932,229,'铜山县',3),(1933,229,'睢宁县',3),(1934,230,'城区',3),(1935,230,'亭湖区',3),(1936,230,'盐都区',3),(1937,230,'盐都县',3),(1938,230,'东台市',3),(1939,230,'大丰市',3),(1940,230,'响水县',3),(1941,230,'滨海县',3),(1942,230,'阜宁县',3),(1943,230,'射阳县',3),(1944,230,'建湖县',3),(1945,231,'广陵区',3),(1946,231,'维扬区',3),(1947,231,'邗江区',3),(1948,231,'仪征市',3),(1949,231,'高邮市',3),(1950,231,'江都市',3),(1951,231,'宝应县',3),(1952,232,'京口区',3),(1953,232,'润州区',3),(1954,232,'丹徒区',3),(1955,232,'丹阳市',3),(1956,232,'扬中市',3),(1957,232,'句容市',3),(1958,233,'东湖区',3),(1959,233,'西湖区',3),(1960,233,'青云谱区',3),(1961,233,'湾里区',3),(1962,233,'青山湖区',3),(1963,233,'红谷滩新区',3),(1964,233,'昌北区',3),(1965,233,'高新区',3),(1966,233,'南昌县',3),(1967,233,'新建县',3),(1968,233,'安义县',3),(1969,233,'进贤县',3),(1970,234,'临川区',3),(1971,234,'南城县',3),(1972,234,'黎川县',3),(1973,234,'南丰县',3),(1974,234,'崇仁县',3),(1975,234,'乐安县',3),(1976,234,'宜黄县',3),(1977,234,'金溪县',3),(1978,234,'资溪县',3),(1979,234,'东乡县',3),(1980,234,'广昌县',3),(1981,235,'章贡区',3),(1982,235,'于都县',3),(1983,235,'瑞金市',3),(1984,235,'南康市',3),(1985,235,'赣县',3),(1986,235,'信丰县',3),(1987,235,'大余县',3),(1988,235,'上犹县',3),(1989,235,'崇义县',3),(1990,235,'安远县',3),(1991,235,'龙南县',3),(1992,235,'定南县',3),(1993,235,'全南县',3),(1994,235,'宁都县',3),(1995,235,'兴国县',3),(1996,235,'会昌县',3),(1997,235,'寻乌县',3),(1998,235,'石城县',3),(1999,236,'安福县',3),(2000,236,'吉州区',3),(2001,236,'青原区',3),(2002,236,'井冈山市',3),(2003,236,'吉安县',3),(2004,236,'吉水县',3),(2005,236,'峡江县',3),(2006,236,'新干县',3),(2007,236,'永丰县',3),(2008,236,'泰和县',3),(2009,236,'遂川县',3),(2010,236,'万安县',3),(2011,236,'永新县',3),(2012,237,'珠山区',3),(2013,237,'昌江区',3),(2014,237,'乐平市',3),(2015,237,'浮梁县',3),(2016,238,'浔阳区',3),(2017,238,'庐山区',3),(2018,238,'瑞昌市',3),(2019,238,'九江县',3),(2020,238,'武宁县',3),(2021,238,'修水县',3),(2022,238,'永修县',3),(2023,238,'德安县',3),(2024,238,'星子县',3),(2025,238,'都昌县',3),(2026,238,'湖口县',3),(2027,238,'彭泽县',3),(2028,239,'安源区',3),(2029,239,'湘东区',3),(2030,239,'莲花县',3),(2031,239,'芦溪县',3),(2032,239,'上栗县',3),(2033,240,'信州区',3),(2034,240,'德兴市',3),(2035,240,'上饶县',3),(2036,240,'广丰县',3),(2037,240,'玉山县',3),(2038,240,'铅山县',3),(2039,240,'横峰县',3),(2040,240,'弋阳县',3),(2041,240,'余干县',3),(2042,240,'波阳县',3),(2043,240,'万年县',3),(2044,240,'婺源县',3),(2045,241,'渝水区',3),(2046,241,'分宜县',3),(2047,242,'袁州区',3),(2048,242,'丰城市',3),(2049,242,'樟树市',3),(2050,242,'高安市',3),(2051,242,'奉新县',3),(2052,242,'万载县',3),(2053,242,'上高县',3),(2054,242,'宜丰县',3),(2055,242,'靖安县',3),(2056,242,'铜鼓县',3),(2057,243,'月湖区',3),(2058,243,'贵溪市',3),(2059,243,'余江县',3),(2060,244,'沈河区',3),(2061,244,'皇姑区',3),(2062,244,'和平区',3),(2063,244,'大东区',3),(2064,244,'铁西区',3),(2065,244,'苏家屯区',3),(2066,244,'东陵区',3),(2067,244,'沈北新区',3),(2068,244,'于洪区',3),(2069,244,'浑南新区',3),(2070,244,'新民市',3),(2071,244,'辽中县',3),(2072,244,'康平县',3),(2073,244,'法库县',3),(2074,245,'西岗区',3),(2075,245,'中山区',3),(2076,245,'沙河口区',3),(2077,245,'甘井子区',3),(2078,245,'旅顺口区',3),(2079,245,'金州区',3),(2080,245,'开发区',3),(2081,245,'瓦房店市',3),(2082,245,'普兰店市',3),(2083,245,'庄河市',3),(2084,245,'长海县',3),(2085,246,'铁东区',3),(2086,246,'铁西区',3),(2087,246,'立山区',3),(2088,246,'千山区',3),(2089,246,'岫岩',3),(2090,246,'海城市',3),(2091,246,'台安县',3),(2092,247,'本溪',3),(2093,247,'平山区',3),(2094,247,'明山区',3),(2095,247,'溪湖区',3),(2096,247,'南芬区',3),(2097,247,'桓仁',3),(2098,248,'双塔区',3),(2099,248,'龙城区',3),(2100,248,'喀喇沁左翼蒙古族自治县',3),(2101,248,'北票市',3),(2102,248,'凌源市',3),(2103,248,'朝阳县',3),(2104,248,'建平县',3),(2105,249,'振兴区',3),(2106,249,'元宝区',3),(2107,249,'振安区',3),(2108,249,'宽甸',3),(2109,249,'东港市',3),(2110,249,'凤城市',3),(2111,250,'顺城区',3),(2112,250,'新抚区',3),(2113,250,'东洲区',3),(2114,250,'望花区',3),(2115,250,'清原',3),(2116,250,'新宾',3),(2117,250,'抚顺县',3),(2118,251,'阜新',3),(2119,251,'海州区',3),(2120,251,'新邱区',3),(2121,251,'太平区',3),(2122,251,'清河门区',3),(2123,251,'细河区',3),(2124,251,'彰武县',3),(2125,252,'龙港区',3),(2126,252,'南票区',3),(2127,252,'连山区',3),(2128,252,'兴城市',3),(2129,252,'绥中县',3),(2130,252,'建昌县',3),(2131,253,'太和区',3),(2132,253,'古塔区',3),(2133,253,'凌河区',3),(2134,253,'凌海市',3),(2135,253,'北镇市',3),(2136,253,'黑山县',3),(2137,253,'义县',3),(2138,254,'白塔区',3),(2139,254,'文圣区',3),(2140,254,'宏伟区',3),(2141,254,'太子河区',3),(2142,254,'弓长岭区',3),(2143,254,'灯塔市',3),(2144,254,'辽阳县',3),(2145,255,'双台子区',3),(2146,255,'兴隆台区',3),(2147,255,'大洼县',3),(2148,255,'盘山县',3),(2149,256,'银州区',3),(2150,256,'清河区',3),(2151,256,'调兵山市',3),(2152,256,'开原市',3),(2153,256,'铁岭县',3),(2154,256,'西丰县',3),(2155,256,'昌图县',3),(2156,257,'站前区',3),(2157,257,'西市区',3),(2158,257,'鲅鱼圈区',3),(2159,257,'老边区',3),(2160,257,'盖州市',3),(2161,257,'大石桥市',3),(2162,258,'回民区',3),(2163,258,'玉泉区',3),(2164,258,'新城区',3),(2165,258,'赛罕区',3),(2166,258,'清水河县',3),(2167,258,'土默特左旗',3),(2168,258,'托克托县',3),(2169,258,'和林格尔县',3),(2170,258,'武川县',3),(2171,259,'阿拉善左旗',3),(2172,259,'阿拉善右旗',3),(2173,259,'额济纳旗',3),(2174,260,'临河区',3),(2175,260,'五原县',3),(2176,260,'磴口县',3),(2177,260,'乌拉特前旗',3),(2178,260,'乌拉特中旗',3),(2179,260,'乌拉特后旗',3),(2180,260,'杭锦后旗',3),(2181,261,'昆都仑区',3),(2182,261,'青山区',3),(2183,261,'东河区',3),(2184,261,'九原区',3),(2185,261,'石拐区',3),(2186,261,'白云矿区',3),(2187,261,'土默特右旗',3),(2188,261,'固阳县',3),(2189,261,'达尔罕茂明安联合旗',3),(2190,262,'红山区',3),(2191,262,'元宝山区',3),(2192,262,'松山区',3),(2193,262,'阿鲁科尔沁旗',3),(2194,262,'巴林左旗',3),(2195,262,'巴林右旗',3),(2196,262,'林西县',3),(2197,262,'克什克腾旗',3),(2198,262,'翁牛特旗',3),(2199,262,'喀喇沁旗',3),(2200,262,'宁城县',3),(2201,262,'敖汉旗',3),(2202,263,'东胜区',3),(2203,263,'达拉特旗',3),(2204,263,'准格尔旗',3),(2205,263,'鄂托克前旗',3),(2206,263,'鄂托克旗',3),(2207,263,'杭锦旗',3),(2208,263,'乌审旗',3),(2209,263,'伊金霍洛旗',3),(2210,264,'海拉尔区',3),(2211,264,'莫力达瓦',3),(2212,264,'满洲里市',3),(2213,264,'牙克石市',3),(2214,264,'扎兰屯市',3),(2215,264,'额尔古纳市',3),(2216,264,'根河市',3),(2217,264,'阿荣旗',3),(2218,264,'鄂伦春自治旗',3),(2219,264,'鄂温克族自治旗',3),(2220,264,'陈巴尔虎旗',3),(2221,264,'新巴尔虎左旗',3),(2222,264,'新巴尔虎右旗',3),(2223,265,'科尔沁区',3),(2224,265,'霍林郭勒市',3),(2225,265,'科尔沁左翼中旗',3),(2226,265,'科尔沁左翼后旗',3),(2227,265,'开鲁县',3),(2228,265,'库伦旗',3),(2229,265,'奈曼旗',3),(2230,265,'扎鲁特旗',3),(2231,266,'海勃湾区',3),(2232,266,'乌达区',3),(2233,266,'海南区',3),(2234,267,'化德县',3),(2235,267,'集宁区',3),(2236,267,'丰镇市',3),(2237,267,'卓资县',3),(2238,267,'商都县',3),(2239,267,'兴和县',3),(2240,267,'凉城县',3),(2241,267,'察哈尔右翼前旗',3),(2242,267,'察哈尔右翼中旗',3),(2243,267,'察哈尔右翼后旗',3),(2244,267,'四子王旗',3),(2245,268,'二连浩特市',3),(2246,268,'锡林浩特市',3),(2247,268,'阿巴嘎旗',3),(2248,268,'苏尼特左旗',3),(2249,268,'苏尼特右旗',3),(2250,268,'东乌珠穆沁旗',3),(2251,268,'西乌珠穆沁旗',3),(2252,268,'太仆寺旗',3),(2253,268,'镶黄旗',3),(2254,268,'正镶白旗',3),(2255,268,'正蓝旗',3),(2256,268,'多伦县',3),(2257,269,'乌兰浩特市',3),(2258,269,'阿尔山市',3),(2259,269,'科尔沁右翼前旗',3),(2260,269,'科尔沁右翼中旗',3),(2261,269,'扎赉特旗',3),(2262,269,'突泉县',3),(2263,270,'西夏区',3),(2264,270,'金凤区',3),(2265,270,'兴庆区',3),(2266,270,'灵武市',3),(2267,270,'永宁县',3),(2268,270,'贺兰县',3),(2269,271,'原州区',3),(2270,271,'海原县',3),(2271,271,'西吉县',3),(2272,271,'隆德县',3),(2273,271,'泾源县',3),(2274,271,'彭阳县',3),(2275,272,'惠农县',3),(2276,272,'大武口区',3),(2277,272,'惠农区',3),(2278,272,'陶乐县',3),(2279,272,'平罗县',3),(2280,273,'利通区',3),(2281,273,'中卫县',3),(2282,273,'青铜峡市',3),(2283,273,'中宁县',3),(2284,273,'盐池县',3),(2285,273,'同心县',3),(2286,274,'沙坡头区',3),(2287,274,'海原县',3),(2288,274,'中宁县',3),(2289,275,'城中区',3),(2290,275,'城东区',3),(2291,275,'城西区',3),(2292,275,'城北区',3),(2293,275,'湟中县',3),(2294,275,'湟源县',3),(2295,275,'大通',3),(2296,276,'玛沁县',3),(2297,276,'班玛县',3),(2298,276,'甘德县',3),(2299,276,'达日县',3),(2300,276,'久治县',3),(2301,276,'玛多县',3),(2302,277,'海晏县',3),(2303,277,'祁连县',3),(2304,277,'刚察县',3),(2305,277,'门源',3),(2306,278,'平安县',3),(2307,278,'乐都县',3),(2308,278,'民和',3),(2309,278,'互助',3),(2310,278,'化隆',3),(2311,278,'循化',3),(2312,279,'共和县',3),(2313,279,'同德县',3),(2314,279,'贵德县',3),(2315,279,'兴海县',3),(2316,279,'贵南县',3),(2317,280,'德令哈市',3),(2318,280,'格尔木市',3),(2319,280,'乌兰县',3),(2320,280,'都兰县',3),(2321,280,'天峻县',3),(2322,281,'同仁县',3),(2323,281,'尖扎县',3),(2324,281,'泽库县',3),(2325,281,'河南蒙古族自治县',3),(2326,282,'玉树县',3),(2327,282,'杂多县',3),(2328,282,'称多县',3),(2329,282,'治多县',3),(2330,282,'囊谦县',3),(2331,282,'曲麻莱县',3),(2332,283,'市中区',3),(2333,283,'历下区',3),(2334,283,'天桥区',3),(2335,283,'槐荫区',3),(2336,283,'历城区',3),(2337,283,'长清区',3),(2338,283,'章丘市',3),(2339,283,'平阴县',3),(2340,283,'济阳县',3),(2341,283,'商河县',3),(2342,284,'市南区',3),(2343,284,'市北区',3),(2344,284,'城阳区',3),(2345,284,'四方区',3),(2346,284,'李沧区',3),(2347,284,'黄岛区',3),(2348,284,'崂山区',3),(2349,284,'胶州市',3),(2350,284,'即墨市',3),(2351,284,'平度市',3),(2352,284,'胶南市',3),(2353,284,'莱西市',3),(2354,285,'滨城区',3),(2355,285,'惠民县',3),(2356,285,'阳信县',3),(2357,285,'无棣县',3),(2358,285,'沾化县',3),(2359,285,'博兴县',3),(2360,285,'邹平县',3),(2361,286,'德城区',3),(2362,286,'陵县',3),(2363,286,'乐陵市',3),(2364,286,'禹城市',3),(2365,286,'宁津县',3),(2366,286,'庆云县',3),(2367,286,'临邑县',3),(2368,286,'齐河县',3),(2369,286,'平原县',3),(2370,286,'夏津县',3),(2371,286,'武城县',3),(2372,287,'东营区',3),(2373,287,'河口区',3),(2374,287,'垦利县',3),(2375,287,'利津县',3),(2376,287,'广饶县',3),(2377,288,'牡丹区',3),(2378,288,'曹县',3),(2379,288,'单县',3),(2380,288,'成武县',3),(2381,288,'巨野县',3),(2382,288,'郓城县',3),(2383,288,'鄄城县',3),(2384,288,'定陶县',3),(2385,288,'东明县',3),(2386,289,'市中区',3),(2387,289,'任城区',3),(2388,289,'曲阜市',3),(2389,289,'兖州市',3),(2390,289,'邹城市',3),(2391,289,'微山县',3),(2392,289,'鱼台县',3),(2393,289,'金乡县',3),(2394,289,'嘉祥县',3),(2395,289,'汶上县',3),(2396,289,'泗水县',3),(2397,289,'梁山县',3),(2398,290,'莱城区',3),(2399,290,'钢城区',3),(2400,291,'东昌府区',3),(2401,291,'临清市',3),(2402,291,'阳谷县',3),(2403,291,'莘县',3),(2404,291,'茌平县',3),(2405,291,'东阿县',3),(2406,291,'冠县',3),(2407,291,'高唐县',3),(2408,292,'兰山区',3),(2409,292,'罗庄区',3),(2410,292,'河东区',3),(2411,292,'沂南县',3),(2412,292,'郯城县',3),(2413,292,'沂水县',3),(2414,292,'苍山县',3),(2415,292,'费县',3),(2416,292,'平邑县',3),(2417,292,'莒南县',3),(2418,292,'蒙阴县',3),(2419,292,'临沭县',3),(2420,293,'东港区',3),(2421,293,'岚山区',3),(2422,293,'五莲县',3),(2423,293,'莒县',3),(2424,294,'泰山区',3),(2425,294,'岱岳区',3),(2426,294,'新泰市',3),(2427,294,'肥城市',3),(2428,294,'宁阳县',3),(2429,294,'东平县',3),(2430,295,'荣成市',3),(2431,295,'乳山市',3),(2432,295,'环翠区',3),(2433,295,'文登市',3),(2434,296,'潍城区',3),(2435,296,'寒亭区',3),(2436,296,'坊子区',3),(2437,296,'奎文区',3),(2438,296,'青州市',3),(2439,296,'诸城市',3),(2440,296,'寿光市',3),(2441,296,'安丘市',3),(2442,296,'高密市',3),(2443,296,'昌邑市',3),(2444,296,'临朐县',3),(2445,296,'昌乐县',3),(2446,297,'芝罘区',3),(2447,297,'福山区',3),(2448,297,'牟平区',3),(2449,297,'莱山区',3),(2450,297,'开发区',3),(2451,297,'龙口市',3),(2452,297,'莱阳市',3),(2453,297,'莱州市',3),(2454,297,'蓬莱市',3),(2455,297,'招远市',3),(2456,297,'栖霞市',3),(2457,297,'海阳市',3),(2458,297,'长岛县',3),(2459,298,'市中区',3),(2460,298,'山亭区',3),(2461,298,'峄城区',3),(2462,298,'台儿庄区',3),(2463,298,'薛城区',3),(2464,298,'滕州市',3),(2465,299,'张店区',3),(2466,299,'临淄区',3),(2467,299,'淄川区',3),(2468,299,'博山区',3),(2469,299,'周村区',3),(2470,299,'桓台县',3),(2471,299,'高青县',3),(2472,299,'沂源县',3),(2473,300,'杏花岭区',3),(2474,300,'小店区',3),(2475,300,'迎泽区',3),(2476,300,'尖草坪区',3),(2477,300,'万柏林区',3),(2478,300,'晋源区',3),(2479,300,'高新开发区',3),(2480,300,'民营经济开发区',3),(2481,300,'经济技术开发区',3),(2482,300,'清徐县',3),(2483,300,'阳曲县',3),(2484,300,'娄烦县',3),(2485,300,'古交市',3),(2486,301,'城区',3),(2487,301,'郊区',3),(2488,301,'沁县',3),(2489,301,'潞城市',3),(2490,301,'长治县',3),(2491,301,'襄垣县',3),(2492,301,'屯留县',3),(2493,301,'平顺县',3),(2494,301,'黎城县',3),(2495,301,'壶关县',3),(2496,301,'长子县',3),(2497,301,'武乡县',3),(2498,301,'沁源县',3),(2499,302,'城区',3),(2500,302,'矿区',3),(2501,302,'南郊区',3),(2502,302,'新荣区',3),(2503,302,'阳高县',3),(2504,302,'天镇县',3),(2505,302,'广灵县',3),(2506,302,'灵丘县',3),(2507,302,'浑源县',3),(2508,302,'左云县',3),(2509,302,'大同县',3),(2510,303,'城区',3),(2511,303,'高平市',3),(2512,303,'沁水县',3),(2513,303,'阳城县',3),(2514,303,'陵川县',3),(2515,303,'泽州县',3),(2516,304,'榆次区',3),(2517,304,'介休市',3),(2518,304,'榆社县',3),(2519,304,'左权县',3),(2520,304,'和顺县',3),(2521,304,'昔阳县',3),(2522,304,'寿阳县',3),(2523,304,'太谷县',3),(2524,304,'祁县',3),(2525,304,'平遥县',3),(2526,304,'灵石县',3),(2527,305,'尧都区',3),(2528,305,'侯马市',3),(2529,305,'霍州市',3),(2530,305,'曲沃县',3),(2531,305,'翼城县',3),(2532,305,'襄汾县',3),(2533,305,'洪洞县',3),(2534,305,'吉县',3),(2535,305,'安泽县',3),(2536,305,'浮山县',3),(2537,305,'古县',3),(2538,305,'乡宁县',3),(2539,305,'大宁县',3),(2540,305,'隰县',3),(2541,305,'永和县',3),(2542,305,'蒲县',3),(2543,305,'汾西县',3),(2544,306,'离石市',3),(2545,306,'离石区',3),(2546,306,'孝义市',3),(2547,306,'汾阳市',3),(2548,306,'文水县',3),(2549,306,'交城县',3),(2550,306,'兴县',3),(2551,306,'临县',3),(2552,306,'柳林县',3),(2553,306,'石楼县',3),(2554,306,'岚县',3),(2555,306,'方山县',3),(2556,306,'中阳县',3),(2557,306,'交口县',3),(2558,307,'朔城区',3),(2559,307,'平鲁区',3),(2560,307,'山阴县',3),(2561,307,'应县',3),(2562,307,'右玉县',3),(2563,307,'怀仁县',3),(2564,308,'忻府区',3),(2565,308,'原平市',3),(2566,308,'定襄县',3),(2567,308,'五台县',3),(2568,308,'代县',3),(2569,308,'繁峙县',3),(2570,308,'宁武县',3),(2571,308,'静乐县',3),(2572,308,'神池县',3),(2573,308,'五寨县',3),(2574,308,'岢岚县',3),(2575,308,'河曲县',3),(2576,308,'保德县',3),(2577,308,'偏关县',3),(2578,309,'城区',3),(2579,309,'矿区',3),(2580,309,'郊区',3),(2581,309,'平定县',3),(2582,309,'盂县',3),(2583,310,'盐湖区',3),(2584,310,'永济市',3),(2585,310,'河津市',3),(2586,310,'临猗县',3),(2587,310,'万荣县',3),(2588,310,'闻喜县',3),(2589,310,'稷山县',3),(2590,310,'新绛县',3),(2591,310,'绛县',3),(2592,310,'垣曲县',3),(2593,310,'夏县',3),(2594,310,'平陆县',3),(2595,310,'芮城县',3),(2596,311,'莲湖区',3),(2597,311,'新城区',3),(2598,311,'碑林区',3),(2599,311,'雁塔区',3),(2600,311,'灞桥区',3),(2601,311,'未央区',3),(2602,311,'阎良区',3),(2603,311,'临潼区',3),(2604,311,'长安区',3),(2605,311,'蓝田县',3),(2606,311,'周至县',3),(2607,311,'户县',3),(2608,311,'高陵县',3),(2609,312,'汉滨区',3),(2610,312,'汉阴县',3),(2611,312,'石泉县',3),(2612,312,'宁陕县',3),(2613,312,'紫阳县',3),(2614,312,'岚皋县',3),(2615,312,'平利县',3),(2616,312,'镇坪县',3),(2617,312,'旬阳县',3),(2618,312,'白河县',3),(2619,313,'陈仓区',3),(2620,313,'渭滨区',3),(2621,313,'金台区',3),(2622,313,'凤翔县',3),(2623,313,'岐山县',3),(2624,313,'扶风县',3),(2625,313,'眉县',3),(2626,313,'陇县',3),(2627,313,'千阳县',3),(2628,313,'麟游县',3),(2629,313,'凤县',3),(2630,313,'太白县',3),(2631,314,'汉台区',3),(2632,314,'南郑县',3),(2633,314,'城固县',3),(2634,314,'洋县',3),(2635,314,'西乡县',3),(2636,314,'勉县',3),(2637,314,'宁强县',3),(2638,314,'略阳县',3),(2639,314,'镇巴县',3),(2640,314,'留坝县',3),(2641,314,'佛坪县',3),(2642,315,'商州区',3),(2643,315,'洛南县',3),(2644,315,'丹凤县',3),(2645,315,'商南县',3),(2646,315,'山阳县',3),(2647,315,'镇安县',3),(2648,315,'柞水县',3),(2649,316,'耀州区',3),(2650,316,'王益区',3),(2651,316,'印台区',3),(2652,316,'宜君县',3),(2653,317,'临渭区',3),(2654,317,'韩城市',3),(2655,317,'华阴市',3),(2656,317,'华县',3),(2657,317,'潼关县',3),(2658,317,'大荔县',3),(2659,317,'合阳县',3),(2660,317,'澄城县',3),(2661,317,'蒲城县',3),(2662,317,'白水县',3),(2663,317,'富平县',3),(2664,318,'秦都区',3),(2665,318,'渭城区',3),(2666,318,'杨陵区',3),(2667,318,'兴平市',3),(2668,318,'三原县',3),(2669,318,'泾阳县',3),(2670,318,'乾县',3),(2671,318,'礼泉县',3),(2672,318,'永寿县',3),(2673,318,'彬县',3),(2674,318,'长武县',3),(2675,318,'旬邑县',3),(2676,318,'淳化县',3),(2677,318,'武功县',3),(2678,319,'吴起县',3),(2679,319,'宝塔区',3),(2680,319,'延长县',3),(2681,319,'延川县',3),(2682,319,'子长县',3),(2683,319,'安塞县',3),(2684,319,'志丹县',3),(2685,319,'甘泉县',3),(2686,319,'富县',3),(2687,319,'洛川县',3),(2688,319,'宜川县',3),(2689,319,'黄龙县',3),(2690,319,'黄陵县',3),(2691,320,'榆阳区',3),(2692,320,'神木县',3),(2693,320,'府谷县',3),(2694,320,'横山县',3),(2695,320,'靖边县',3),(2696,320,'定边县',3),(2697,320,'绥德县',3),(2698,320,'米脂县',3),(2699,320,'佳县',3),(2700,320,'吴堡县',3),(2701,320,'清涧县',3),(2702,320,'子洲县',3),(2703,321,'长宁区',3),(2704,321,'闸北区',3),(2705,321,'闵行区',3),(2706,321,'徐汇区',3),(2707,321,'浦东新区',3),(2708,321,'杨浦区',3),(2709,321,'普陀区',3),(2710,321,'静安区',3),(2711,321,'卢湾区',3),(2712,321,'虹口区',3),(2713,321,'黄浦区',3),(2714,321,'南汇区',3),(2715,321,'松江区',3),(2716,321,'嘉定区',3),(2717,321,'宝山区',3),(2718,321,'青浦区',3),(2719,321,'金山区',3),(2720,321,'奉贤区',3),(2721,321,'崇明县',3),(2722,322,'青羊区',3),(2723,322,'锦江区',3),(2724,322,'金牛区',3),(2725,322,'武侯区',3),(2726,322,'成华区',3),(2727,322,'龙泉驿区',3),(2728,322,'青白江区',3),(2729,322,'新都区',3),(2730,322,'温江区',3),(2731,322,'高新区',3),(2732,322,'高新西区',3),(2733,322,'都江堰市',3),(2734,322,'彭州市',3),(2735,322,'邛崃市',3),(2736,322,'崇州市',3),(2737,322,'金堂县',3),(2738,322,'双流县',3),(2739,322,'郫县',3),(2740,322,'大邑县',3),(2741,322,'蒲江县',3),(2742,322,'新津县',3),(2743,322,'都江堰市',3),(2744,322,'彭州市',3),(2745,322,'邛崃市',3),(2746,322,'崇州市',3),(2747,322,'金堂县',3),(2748,322,'双流县',3),(2749,322,'郫县',3),(2750,322,'大邑县',3),(2751,322,'蒲江县',3),(2752,322,'新津县',3),(2753,323,'涪城区',3),(2754,323,'游仙区',3),(2755,323,'江油市',3),(2756,323,'盐亭县',3),(2757,323,'三台县',3),(2758,323,'平武县',3),(2759,323,'安县',3),(2760,323,'梓潼县',3),(2761,323,'北川县',3),(2762,324,'马尔康县',3),(2763,324,'汶川县',3),(2764,324,'理县',3),(2765,324,'茂县',3),(2766,324,'松潘县',3),(2767,324,'九寨沟县',3),(2768,324,'金川县',3),(2769,324,'小金县',3),(2770,324,'黑水县',3),(2771,324,'壤塘县',3),(2772,324,'阿坝县',3),(2773,324,'若尔盖县',3),(2774,324,'红原县',3),(2775,325,'巴州区',3),(2776,325,'通江县',3),(2777,325,'南江县',3),(2778,325,'平昌县',3),(2779,326,'通川区',3),(2780,326,'万源市',3),(2781,326,'达县',3),(2782,326,'宣汉县',3),(2783,326,'开江县',3),(2784,326,'大竹县',3),(2785,326,'渠县',3),(2786,327,'旌阳区',3),(2787,327,'广汉市',3),(2788,327,'什邡市',3),(2789,327,'绵竹市',3),(2790,327,'罗江县',3),(2791,327,'中江县',3),(2792,328,'康定县',3),(2793,328,'丹巴县',3),(2794,328,'泸定县',3),(2795,328,'炉霍县',3),(2796,328,'九龙县',3),(2797,328,'甘孜县',3),(2798,328,'雅江县',3),(2799,328,'新龙县',3),(2800,328,'道孚县',3),(2801,328,'白玉县',3),(2802,328,'理塘县',3),(2803,328,'德格县',3),(2804,328,'乡城县',3),(2805,328,'石渠县',3),(2806,328,'稻城县',3),(2807,328,'色达县',3),(2808,328,'巴塘县',3),(2809,328,'得荣县',3),(2810,329,'广安区',3),(2811,329,'华蓥市',3),(2812,329,'岳池县',3),(2813,329,'武胜县',3),(2814,329,'邻水县',3),(2815,330,'利州区',3),(2816,330,'元坝区',3),(2817,330,'朝天区',3),(2818,330,'旺苍县',3),(2819,330,'青川县',3),(2820,330,'剑阁县',3),(2821,330,'苍溪县',3),(2822,331,'峨眉山市',3),(2823,331,'乐山市',3),(2824,331,'犍为县',3),(2825,331,'井研县',3),(2826,331,'夹江县',3),(2827,331,'沐川县',3),(2828,331,'峨边',3),(2829,331,'马边',3),(2830,332,'西昌市',3),(2831,332,'盐源县',3),(2832,332,'德昌县',3),(2833,332,'会理县',3),(2834,332,'会东县',3),(2835,332,'宁南县',3),(2836,332,'普格县',3),(2837,332,'布拖县',3),(2838,332,'金阳县',3),(2839,332,'昭觉县',3),(2840,332,'喜德县',3),(2841,332,'冕宁县',3),(2842,332,'越西县',3),(2843,332,'甘洛县',3),(2844,332,'美姑县',3),(2845,332,'雷波县',3),(2846,332,'木里',3),(2847,333,'东坡区',3),(2848,333,'仁寿县',3),(2849,333,'彭山县',3),(2850,333,'洪雅县',3),(2851,333,'丹棱县',3),(2852,333,'青神县',3),(2853,334,'阆中市',3),(2854,334,'南部县',3),(2855,334,'营山县',3),(2856,334,'蓬安县',3),(2857,334,'仪陇县',3),(2858,334,'顺庆区',3),(2859,334,'高坪区',3),(2860,334,'嘉陵区',3),(2861,334,'西充县',3),(2862,335,'市中区',3),(2863,335,'东兴区',3),(2864,335,'威远县',3),(2865,335,'资中县',3),(2866,335,'隆昌县',3),(2867,336,'东  区',3),(2868,336,'西  区',3),(2869,336,'仁和区',3),(2870,336,'米易县',3),(2871,336,'盐边县',3),(2872,337,'船山区',3),(2873,337,'安居区',3),(2874,337,'蓬溪县',3),(2875,337,'射洪县',3),(2876,337,'大英县',3),(2877,338,'雨城区',3),(2878,338,'名山县',3),(2879,338,'荥经县',3),(2880,338,'汉源县',3),(2881,338,'石棉县',3),(2882,338,'天全县',3),(2883,338,'芦山县',3),(2884,338,'宝兴县',3),(2885,339,'翠屏区',3),(2886,339,'宜宾县',3),(2887,339,'南溪县',3),(2888,339,'江安县',3),(2889,339,'长宁县',3),(2890,339,'高县',3),(2891,339,'珙县',3),(2892,339,'筠连县',3),(2893,339,'兴文县',3),(2894,339,'屏山县',3),(2895,340,'雁江区',3),(2896,340,'简阳市',3),(2897,340,'安岳县',3),(2898,340,'乐至县',3),(2899,341,'大安区',3),(2900,341,'自流井区',3),(2901,341,'贡井区',3),(2902,341,'沿滩区',3),(2903,341,'荣县',3),(2904,341,'富顺县',3),(2905,342,'江阳区',3),(2906,342,'纳溪区',3),(2907,342,'龙马潭区',3),(2908,342,'泸县',3),(2909,342,'合江县',3),(2910,342,'叙永县',3),(2911,342,'古蔺县',3),(2912,343,'和平区',3),(2913,343,'河西区',3),(2914,343,'南开区',3),(2915,343,'河北区',3),(2916,343,'河东区',3),(2917,343,'红桥区',3),(2918,343,'东丽区',3),(2919,343,'津南区',3),(2920,343,'西青区',3),(2921,343,'北辰区',3),(2922,343,'塘沽区',3),(2923,343,'汉沽区',3),(2924,343,'大港区',3),(2925,343,'武清区',3),(2926,343,'宝坻区',3),(2927,343,'经济开发区',3),(2928,343,'宁河县',3),(2929,343,'静海县',3),(2930,343,'蓟县',3),(2931,344,'城关区',3),(2932,344,'林周县',3),(2933,344,'当雄县',3),(2934,344,'尼木县',3),(2935,344,'曲水县',3),(2936,344,'堆龙德庆县',3),(2937,344,'达孜县',3),(2938,344,'墨竹工卡县',3),(2939,345,'噶尔县',3),(2940,345,'普兰县',3),(2941,345,'札达县',3),(2942,345,'日土县',3),(2943,345,'革吉县',3),(2944,345,'改则县',3),(2945,345,'措勤县',3),(2946,346,'昌都县',3),(2947,346,'江达县',3),(2948,346,'贡觉县',3),(2949,346,'类乌齐县',3),(2950,346,'丁青县',3),(2951,346,'察雅县',3),(2952,346,'八宿县',3),(2953,346,'左贡县',3),(2954,346,'芒康县',3),(2955,346,'洛隆县',3),(2956,346,'边坝县',3),(2957,347,'林芝县',3),(2958,347,'工布江达县',3),(2959,347,'米林县',3),(2960,347,'墨脱县',3),(2961,347,'波密县',3),(2962,347,'察隅县',3),(2963,347,'朗县',3),(2964,348,'那曲县',3),(2965,348,'嘉黎县',3),(2966,348,'比如县',3),(2967,348,'聂荣县',3),(2968,348,'安多县',3),(2969,348,'申扎县',3),(2970,348,'索县',3),(2971,348,'班戈县',3),(2972,348,'巴青县',3),(2973,348,'尼玛县',3),(2974,349,'日喀则市',3),(2975,349,'南木林县',3),(2976,349,'江孜县',3),(2977,349,'定日县',3),(2978,349,'萨迦县',3),(2979,349,'拉孜县',3),(2980,349,'昂仁县',3),(2981,349,'谢通门县',3),(2982,349,'白朗县',3),(2983,349,'仁布县',3),(2984,349,'康马县',3),(2985,349,'定结县',3),(2986,349,'仲巴县',3),(2987,349,'亚东县',3),(2988,349,'吉隆县',3),(2989,349,'聂拉木县',3),(2990,349,'萨嘎县',3),(2991,349,'岗巴县',3),(2992,350,'乃东县',3),(2993,350,'扎囊县',3),(2994,350,'贡嘎县',3),(2995,350,'桑日县',3),(2996,350,'琼结县',3),(2997,350,'曲松县',3),(2998,350,'措美县',3),(2999,350,'洛扎县',3),(3000,350,'加查县',3),(3001,350,'隆子县',3),(3002,350,'错那县',3),(3003,350,'浪卡子县',3),(3004,351,'天山区',3),(3005,351,'沙依巴克区',3),(3006,351,'新市区',3),(3007,351,'水磨沟区',3),(3008,351,'头屯河区',3),(3009,351,'达坂城区',3),(3010,351,'米东区',3),(3011,351,'乌鲁木齐县',3),(3012,352,'阿克苏市',3),(3013,352,'温宿县',3),(3014,352,'库车县',3),(3015,352,'沙雅县',3),(3016,352,'新和县',3),(3017,352,'拜城县',3),(3018,352,'乌什县',3),(3019,352,'阿瓦提县',3),(3020,352,'柯坪县',3),(3021,353,'阿拉尔市',3),(3022,354,'库尔勒市',3),(3023,354,'轮台县',3),(3024,354,'尉犁县',3),(3025,354,'若羌县',3),(3026,354,'且末县',3),(3027,354,'焉耆',3),(3028,354,'和静县',3),(3029,354,'和硕县',3),(3030,354,'博湖县',3),(3031,355,'博乐市',3),(3032,355,'精河县',3),(3033,355,'温泉县',3),(3034,356,'呼图壁县',3),(3035,356,'米泉市',3),(3036,356,'昌吉市',3),(3037,356,'阜康市',3),(3038,356,'玛纳斯县',3),(3039,356,'奇台县',3),(3040,356,'吉木萨尔县',3),(3041,356,'木垒',3),(3042,357,'哈密市',3),(3043,357,'伊吾县',3),(3044,357,'巴里坤',3),(3045,358,'和田市',3),(3046,358,'和田县',3),(3047,358,'墨玉县',3),(3048,358,'皮山县',3),(3049,358,'洛浦县',3),(3050,358,'策勒县',3),(3051,358,'于田县',3),(3052,358,'民丰县',3),(3053,359,'喀什市',3),(3054,359,'疏附县',3),(3055,359,'疏勒县',3),(3056,359,'英吉沙县',3),(3057,359,'泽普县',3),(3058,359,'莎车县',3),(3059,359,'叶城县',3),(3060,359,'麦盖提县',3),(3061,359,'岳普湖县',3),(3062,359,'伽师县',3),(3063,359,'巴楚县',3),(3064,359,'塔什库尔干',3),(3065,360,'克拉玛依市',3),(3066,361,'阿图什市',3),(3067,361,'阿克陶县',3),(3068,361,'阿合奇县',3),(3069,361,'乌恰县',3),(3070,362,'石河子市',3),(3071,363,'图木舒克市',3),(3072,364,'吐鲁番市',3),(3073,364,'鄯善县',3),(3074,364,'托克逊县',3),(3075,365,'五家渠市',3),(3076,366,'阿勒泰市',3),(3077,366,'布克赛尔',3),(3078,366,'伊宁市',3),(3079,366,'布尔津县',3),(3080,366,'奎屯市',3),(3081,366,'乌苏市',3),(3082,366,'额敏县',3),(3083,366,'富蕴县',3),(3084,366,'伊宁县',3),(3085,366,'福海县',3),(3086,366,'霍城县',3),(3087,366,'沙湾县',3),(3088,366,'巩留县',3),(3089,366,'哈巴河县',3),(3090,366,'托里县',3),(3091,366,'青河县',3),(3092,366,'新源县',3),(3093,366,'裕民县',3),(3094,366,'和布克赛尔',3),(3095,366,'吉木乃县',3),(3096,366,'昭苏县',3),(3097,366,'特克斯县',3),(3098,366,'尼勒克县',3),(3099,366,'察布查尔',3),(3100,367,'盘龙区',3),(3101,367,'五华区',3),(3102,367,'官渡区',3),(3103,367,'西山区',3),(3104,367,'东川区',3),(3105,367,'安宁市',3),(3106,367,'呈贡县',3),(3107,367,'晋宁县',3),(3108,367,'富民县',3),(3109,367,'宜良县',3),(3110,367,'嵩明县',3),(3111,367,'石林县',3),(3112,367,'禄劝',3),(3113,367,'寻甸',3),(3114,368,'兰坪',3),(3115,368,'泸水县',3),(3116,368,'福贡县',3),(3117,368,'贡山',3),(3118,369,'宁洱',3),(3119,369,'思茅区',3),(3120,369,'墨江',3),(3121,369,'景东',3),(3122,369,'景谷',3),(3123,369,'镇沅',3),(3124,369,'江城',3),(3125,369,'孟连',3),(3126,369,'澜沧',3),(3127,369,'西盟',3),(3128,370,'古城区',3),(3129,370,'宁蒗',3),(3130,370,'玉龙',3),(3131,370,'永胜县',3),(3132,370,'华坪县',3),(3133,371,'隆阳区',3),(3134,371,'施甸县',3),(3135,371,'腾冲县',3),(3136,371,'龙陵县',3),(3137,371,'昌宁县',3),(3138,372,'楚雄市',3),(3139,372,'双柏县',3),(3140,372,'牟定县',3),(3141,372,'南华县',3),(3142,372,'姚安县',3),(3143,372,'大姚县',3),(3144,372,'永仁县',3),(3145,372,'元谋县',3),(3146,372,'武定县',3),(3147,372,'禄丰县',3),(3148,373,'大理市',3),(3149,373,'祥云县',3),(3150,373,'宾川县',3),(3151,373,'弥渡县',3),(3152,373,'永平县',3),(3153,373,'云龙县',3),(3154,373,'洱源县',3),(3155,373,'剑川县',3),(3156,373,'鹤庆县',3),(3157,373,'漾濞',3),(3158,373,'南涧',3),(3159,373,'巍山',3),(3160,374,'潞西市',3),(3161,374,'瑞丽市',3),(3162,374,'梁河县',3),(3163,374,'盈江县',3),(3164,374,'陇川县',3),(3165,375,'香格里拉县',3),(3166,375,'德钦县',3),(3167,375,'维西',3),(3168,376,'泸西县',3),(3169,376,'蒙自县',3),(3170,376,'个旧市',3),(3171,376,'开远市',3),(3172,376,'绿春县',3),(3173,376,'建水县',3),(3174,376,'石屏县',3),(3175,376,'弥勒县',3),(3176,376,'元阳县',3),(3177,376,'红河县',3),(3178,376,'金平',3),(3179,376,'河口',3),(3180,376,'屏边',3),(3181,377,'临翔区',3),(3182,377,'凤庆县',3),(3183,377,'云县',3),(3184,377,'永德县',3),(3185,377,'镇康县',3),(3186,377,'双江',3),(3187,377,'耿马',3),(3188,377,'沧源',3),(3189,378,'麒麟区',3),(3190,378,'宣威市',3),(3191,378,'马龙县',3),(3192,378,'陆良县',3),(3193,378,'师宗县',3),(3194,378,'罗平县',3),(3195,378,'富源县',3),(3196,378,'会泽县',3),(3197,378,'沾益县',3),(3198,379,'文山县',3),(3199,379,'砚山县',3),(3200,379,'西畴县',3),(3201,379,'麻栗坡县',3),(3202,379,'马关县',3),(3203,379,'丘北县',3),(3204,379,'广南县',3),(3205,379,'富宁县',3),(3206,380,'景洪市',3),(3207,380,'勐海县',3),(3208,380,'勐腊县',3),(3209,381,'红塔区',3),(3210,381,'江川县',3),(3211,381,'澄江县',3),(3212,381,'通海县',3),(3213,381,'华宁县',3),(3214,381,'易门县',3),(3215,381,'峨山',3),(3216,381,'新平',3),(3217,381,'元江',3),(3218,382,'昭阳区',3),(3219,382,'鲁甸县',3),(3220,382,'巧家县',3),(3221,382,'盐津县',3),(3222,382,'大关县',3),(3223,382,'永善县',3),(3224,382,'绥江县',3),(3225,382,'镇雄县',3),(3226,382,'彝良县',3),(3227,382,'威信县',3),(3228,382,'水富县',3),(3229,383,'西湖区',3),(3230,383,'上城区',3),(3231,383,'下城区',3),(3232,383,'拱墅区',3),(3233,383,'滨江区',3),(3234,383,'江干区',3),(3235,383,'萧山区',3),(3236,383,'余杭区',3),(3237,383,'市郊',3),(3238,383,'建德市',3),(3239,383,'富阳市',3),(3240,383,'临安市',3),(3241,383,'桐庐县',3),(3242,383,'淳安县',3),(3243,384,'吴兴区',3),(3244,384,'南浔区',3),(3245,384,'德清县',3),(3246,384,'长兴县',3),(3247,384,'安吉县',3),(3248,385,'南湖区',3),(3249,385,'秀洲区',3),(3250,385,'海宁市',3),(3251,385,'嘉善县',3),(3252,385,'平湖市',3),(3253,385,'桐乡市',3),(3254,385,'海盐县',3),(3255,386,'婺城区',3),(3256,386,'金东区',3),(3257,386,'兰溪市',3),(3258,386,'市区',3),(3259,386,'佛堂镇',3),(3260,386,'上溪镇',3),(3261,386,'义亭镇',3),(3262,386,'大陈镇',3),(3263,386,'苏溪镇',3),(3264,386,'赤岸镇',3),(3265,386,'东阳市',3),(3266,386,'永康市',3),(3267,386,'武义县',3),(3268,386,'浦江县',3),(3269,386,'磐安县',3),(3270,387,'莲都区',3),(3271,387,'龙泉市',3),(3272,387,'青田县',3),(3273,387,'缙云县',3),(3274,387,'遂昌县',3),(3275,387,'松阳县',3),(3276,387,'云和县',3),(3277,387,'庆元县',3),(3278,387,'景宁',3),(3279,388,'海曙区',3),(3280,388,'江东区',3),(3281,388,'江北区',3),(3282,388,'镇海区',3),(3283,388,'北仑区',3),(3284,388,'鄞州区',3),(3285,388,'余姚市',3),(3286,388,'慈溪市',3),(3287,388,'奉化市',3),(3288,388,'象山县',3),(3289,388,'宁海县',3),(3290,389,'越城区',3),(3291,389,'上虞市',3),(3292,389,'嵊州市',3),(3293,389,'绍兴县',3),(3294,389,'新昌县',3),(3295,389,'诸暨市',3),(3296,390,'椒江区',3),(3297,390,'黄岩区',3),(3298,390,'路桥区',3),(3299,390,'温岭市',3),(3300,390,'临海市',3),(3301,390,'玉环县',3),(3302,390,'三门县',3),(3303,390,'天台县',3),(3304,390,'仙居县',3),(3305,391,'鹿城区',3),(3306,391,'龙湾区',3),(3307,391,'瓯海区',3),(3308,391,'瑞安市',3),(3309,391,'乐清市',3),(3310,391,'洞头县',3),(3311,391,'永嘉县',3),(3312,391,'平阳县',3),(3313,391,'苍南县',3),(3314,391,'文成县',3),(3315,391,'泰顺县',3),(3316,392,'定海区',3),(3317,392,'普陀区',3),(3318,392,'岱山县',3),(3319,392,'嵊泗县',3),(3320,393,'衢州市',3),(3321,393,'江山市',3),(3322,393,'常山县',3),(3323,393,'开化县',3),(3324,393,'龙游县',3),(3325,394,'合川区',3),(3326,394,'江津区',3),(3327,394,'南川区',3),(3328,394,'永川区',3),(3329,394,'南岸区',3),(3330,394,'渝北区',3),(3331,394,'万盛区',3),(3332,394,'大渡口区',3),(3333,394,'万州区',3),(3334,394,'北碚区',3),(3335,394,'沙坪坝区',3),(3336,394,'巴南区',3),(3337,394,'涪陵区',3),(3338,394,'江北区',3),(3339,394,'九龙坡区',3),(3340,394,'渝中区',3),(3341,394,'黔江开发区',3),(3342,394,'长寿区',3),(3343,394,'双桥区',3),(3344,394,'綦江县',3),(3345,394,'潼南县',3),(3346,394,'铜梁县',3),(3347,394,'大足县',3),(3348,394,'荣昌县',3),(3349,394,'璧山县',3),(3350,394,'垫江县',3),(3351,394,'武隆县',3),(3352,394,'丰都县',3),(3353,394,'城口县',3),(3354,394,'梁平县',3),(3355,394,'开县',3),(3356,394,'巫溪县',3),(3357,394,'巫山县',3),(3358,394,'奉节县',3),(3359,394,'云阳县',3),(3360,394,'忠县',3),(3361,394,'石柱',3),(3362,394,'彭水',3),(3363,394,'酉阳',3),(3364,394,'秀山',3),(3365,395,'沙田区',3),(3366,395,'东区',3),(3367,395,'观塘区',3),(3368,395,'黄大仙区',3),(3369,395,'九龙城区',3),(3370,395,'屯门区',3),(3371,395,'葵青区',3),(3372,395,'元朗区',3),(3373,395,'深水埗区',3),(3374,395,'西贡区',3),(3375,395,'大埔区',3),(3376,395,'湾仔区',3),(3377,395,'油尖旺区',3),(3378,395,'北区',3),(3379,395,'南区',3),(3380,395,'荃湾区',3),(3381,395,'中西区',3),(3382,395,'离岛区',3),(3383,396,'澳门',3),(3384,397,'台北',3),(3385,397,'高雄',3),(3386,397,'基隆',3),(3387,397,'台中',3),(3388,397,'台南',3),(3389,397,'新竹',3),(3390,397,'嘉义',3),(3391,397,'宜兰县',3),(3392,397,'桃园县',3),(3393,397,'苗栗县',3),(3394,397,'彰化县',3),(3395,397,'南投县',3),(3396,397,'云林县',3),(3397,397,'屏东县',3),(3398,397,'台东县',3),(3399,397,'花莲县',3),(3400,397,'澎湖县',3),(3401,3,'合肥',2),(3402,3401,'庐阳区',3),(3403,3401,'瑶海区',3),(3404,3401,'蜀山区',3),(3405,3401,'包河区',3),(3406,3401,'长丰县',3),(3407,3401,'肥东县',3),(3408,3401,'肥西县',3),(3424,0,'111',0),(3425,3424,'222',1),(3426,1,'www',2),(3428,0,'1234',0);

/*Data for the table `imall_article_cat` */

insert  into `imall_article_cat`(`cat_id`,`cat_name`,`parent_id`,`sort_order`) values (1,'关于商城',-1,0),(2,'商城帮助',-1,0),(3,'公告栏',-1,0),(4,'其它文章',-1,10),(5,'活动促销',-1,0),(6,'我是买家',-1,0),(7,'我是卖家',-1,0);

/*Data for the table `imall_asd_content` */

insert  into `imall_asd_content`(`asd_id`,`position_id`,`media_type`,`asd_name`,`asd_link`,`asd_content`,`remark`,`enabled`,`last_update_time`) values (1,2,1,'Jooyea','http://www.jooyea.net','docs/images/asd/2009/12/26/2009122609305415.jpg','Jooyea',1,'2009-12-26 17:30:54'),(2,3,1,'广告','http://www.jooyea.net','docs/images/asd/2009/12/26/2009122609394762.jpg','广告',1,'2009-12-26 17:39:47'),(3,1,1,'iwebshop','http://www.jooyea.net','docs/images/asd/2009/12/28/2009122801533971.jpg','',1,'2009-12-28 09:53:39'),(4,1,1,'广告名称','http://localhost','./uploadfiles/asd/2010/04/17/2010041703345551.jpg','',1,'2010-04-17 11:34:55');

/*Data for the table `imall_asd_position` */

insert  into `imall_asd_position`(`position_id`,`position_name`,`asd_width`,`asd_height`,`position_desc`) values (1,'首页-通栏-1',960,90,'首页-通栏-1'),(2,'首页-通栏-2',765,90,'首页-通栏-2'),(3,'首页-右侧-下',185,185,'首页-右侧-下'),(4,'广告位名称',100,100,'广告位描述');

/*Data for the table `imall_brand` */

insert  into `imall_brand`(`brand_id`,`brand_name`,`brand_logo`,`brand_desc`,`site_url`,`is_show`) values (2,'戴尔','docs/images/brand/2.jpg','','http://www.dell.com',1),(3,'IBM','docs/images/brand/3.jpg','','http://www.ibm.com/cn/zh/',1),(4,'联想','docs/images/brand/4.jpg','','http://www.lenovo.com.cn/',1),(5,'三星','docs/images/brand/5.jpg','','http://www.samsung.com/cn/',1),(6,'诺基亚','docs/images/brand/6.jpg','','http://www.nokia.com.cn/',1),(7,'惠普','docs/images/brand/7.jpg','','http://www.hp.com.cn',1),(8,'华硕','docs/images/brand/8.jpg','','http://www.asus.com.cn/',1),(9,'苹果','docs/images/brand/9.jpg','','http://www.apple.com/',1),(10,'大水牛','docs/images/brand/10.jpg','','http://www.hedy.com.cn/',1),(11,'希捷','docs/images/brand/11.jpg','','http://www.seagate.com/',1),(12,'金士顿','docs/images/brand/12.jpg','','http://www.kingston.com/china/',1),(13,'AMD','docs/images/brand/13.jpg','','http://www.amd.com.cn/',1),(18,'swarovski','./docs/brand/2010/04/09/2010040907374587.jpg','http://www.swarovski.com','http://www.swarovski.com',1),(17,'阿迪达斯','./docs/brand/2010/04/09/2010040907354277.jpg','阿迪达斯','http://www.adidas.com',1),(19,'劳力士','./docs/brand/2010/04/09/2010040907563397.jpg','劳力士','http://www.rolex.com',1);

/*Data for the table `imall_crons` */

insert  into `imall_crons`(`id`,`type`,`name`,`phpfile`,`lastrun`,`nextrun`,`weekday`,`day`,`hour`,`minute`,`available`) values (2,-1,'更新各分类下所包涵的产品数量','category_update_goodsnum.php',1272110283,1272111000,-1,-1,-1,'10',1),(3,-1,'更新各店铺下所包涵的产品数量','update_goodsnum.php',1272102008,1272104400,-1,-1,-1,'20',1),(4,-1,'更新热门Tag','update_keyword.php',1272102010,1272105000,-1,-1,-1,'30',1),(5,-1,'自动评价','update_evaluate.php',1272102029,1272105600,-1,-1,-1,'40',1),(6,-1,'自动确认收货','update_confirm.php',1272102849,1272106200,-1,-1,-1,'50',1);

/*Data for the table `imall_index_images` */

insert  into `imall_index_images`(`id`,`name`,`images_url`,`images_link`,`add_time`,`status`) values (1,'公益','./docs/index/2010/04/09/2010040908174846.jpg','http://www.jooyea.net','2010-04-09 16:17:48',1),(2,'iwebMall','./docs/index/2010/04/09/2010040908174840.jpg','http://www.jooyea.net','2010-04-09 16:17:48',1),(3,'为了孩子','./docs/index/2010/04/09/2010040911423249.jpg','http://www.jooyea.net','2010-04-09 19:42:32',1),(4,'廉谱','./docs/index/2010/04/09/2010040911440123.jpg','http://www.jooyea.net','2010-04-09 19:44:01',1),(5,'产品展示','./docs/index/2010/04/09/2010040911471785.jpg','http://www.jooyea.net','2010-04-09 19:47:17',1),(6,'产品展示','./docs/index/2010/04/09/2010040911471732.jpg','http://www.jooyea.net','2010-04-09 19:47:17',1);

/*Data for the table `imall_integral` */

insert  into `imall_integral`(`int_id`,`int_min`,`int_max`,`int_grade`) values (1,'4','10',1),(2,'11','40',2),(3,'41','90',3),(4,'91','150',4),(5,'151','250',5),(6,'251','500',6),(7,'501','1000',7),(8,'1001','2000',8),(9,'2001','5000',9),(10,'5001','10000',10),(11,'10001','20000',11),(12,'20001','50000',12),(13,'50001','100000',13),(14,'100001','200000',14),(15,'200001','500000',15);

/*Data for the table `imall_mailtpl` */

insert  into `imall_mailtpl`(`tpl_id`,`tpl_name`,`tpl_title`,`tpl_content`,`last_modify`) values (1,'找回密码邮件模板','来自{site_name}的密码找回邮件','{user_name}您好！<br /><br />您已经进行了密码重置的操作，请点击以下链接(或者复制到您的浏览器):<br /><br /><a href=\"{forgot_check_code_url}\" target=_blank>{forgot_check_code_url}</a><br /><br />以确认您的新密码重置操作！<br /><br />{send_date} <br /><br />（此邮件由系统自动产生，请勿回复。）<br />','2010-02-09 10:08:54'),(2,'帐号激活邮件模版','来自{site_name}的帐号激活邮件','亲爱的{user_name}，欢迎您成为{site_name}的一员，现在开始商城之旅。<BR>激活你的帐号请<A href=\"{baseUrl}modules.php?app=reg3&amp;uid={user_id}&amp;ucode={email_check_code}\" target=_blank>点击这里</A>， 如果无法点击，请将下列地址复制到地址栏进行激活。<BR>{baseUrl}modules.php?app=reg3&amp;uid={user_id}&amp;ucode={email_check_code}<BR>感谢对{site_name}的支持，再次希望您在{site_name}完成交易！<BR><BR>( 这是系统自动产生的邮件，请勿回复。) <BR>','2010-04-23 10:32:50');

/*Data for the table `imall_payment` */

insert  into `imall_payment`(`pay_id`,`pay_name`,`pay_code`,`pay_desc`,`author`,`version`,`config`,`enabled`) values (1,'支付宝纯担保','alipay','支付宝(接口网站：www.alipay.com) 是国内先进的网上支付平台，无预付/年费，单笔费率1.5%，无流量限制。 <a href=\"https://www.alipay.com/himalayas/product_info.htm\" style=\"color:red;font-weight:bold;\" target=\"_blank\">立即在线申请</a>','IWEB TEAM','1.0','partner|合作伙伴IDsecurity_code|安全检验码seller_email|卖家支付宝帐户',1),(2,'财付通即时到帐','tenpay','财付通（www.tenpay.com） - 本即时到账接口无需预付费，任何订单金额均即时到达您的账户，只收单笔 1% 手续费。<a href=\"http://union.tenpay.com/mch/mch_register_b2c.shtml\" style=\"color:red; font-weight:bold;\" target=\"_blank\">立即在线申请</a>','IWEB TEAM','1.0','bargainor_id|财付通商户号key|财付通密钥',1),(3,'财付通中介担保','tenpay2','财付通（www.tenpay.com） - 腾讯旗下在线支付平台，通过国家权威安全认证，支持各大银行网上支付，免支付手续费。','IWEB TEAM','1.0','bargainor_id|财付通商户号key|财付通密钥',1),(4,'邮局汇款','post','收款人信息: 姓名 ***; 地址:***; 邮编 ***','IWEB TEAM','1.0','',1),(5,'银行汇款','bank','收款人信息：银行 ***； 帐号 ***； 姓名 ***；','IWEB TEAM','1.0','',1);

/*Data for the table `imall_privilege` */

insert  into `imall_privilege`(`privilege_id`,`privilege_name`,`privilege_remark`,`privilege_type`) values (1,'开设商铺','',0),(2,'可上传商品','',1),(4,'设置精品数量','',1),(5,'设置特价数量','',1),(6,'设置新品数量','',1),(7,'设置热销数量','',1),(8,'上传图片空间(M)','',1);

/*Data for the table `imall_remind` */

insert  into `imall_remind`(`remind_id`,`remind_type`,`remind_name`,`remind_tpl`,`enable`) values (1,2,'当我的产品被管理员删除时，请通知我 ','您的{goods}，在{time}被管理员删除了!',1),(2,2,'当我的产品被管理员下架时，请通知我 ','您的{goods}，在{time}被管理员下架了!',1),(3,2,'买家在我的店铺中留言时，请通知我 ','在{time}，您的店铺收到新的留言。',1),(4,2,'当我的产品被管理员锁定时，请通知我 ','您的{goods}，在{time}被管理员锁定了!',1),(5,2,'当我的商铺被管理员锁定时，请通知我 ','在{time}，您的店铺被管理员锁定了!',1),(6,2,'当我的商铺被管理员解除锁定时，请通知我 ','在{time}，您的店铺被管理员解除锁定了!',1),(7,2,'当我的产品被管理员解除锁定时，请通知我 ','您的{goods}，在{time}被管理员解除锁定了!',0);

/*Data for the table `imall_settings` */

insert  into `imall_settings`(`variable`,`value`) values ('lastupdate','2010-04-24 14:59:15'),('sys_name','iweb Mall&shop'),('sys_title','iweb Mall'),('sys_keywords','iweb mall|iweb shop|iweb shop&mall'),('sys_description','iweb mall|iweb shop|iweb shop&mall'),('sys_company','聚易开放式技术实验室'),('sys_copyright','Copyright © 2005-2009'),('sys_icp','鲁ICP备01000010号'),('sys_kftelphone','0531-'),('sys_kfqq',''),('sys_smtpserver','smtp.163.com'),('sys_smtpserverport','25'),('sys_smtpusermail',''),('sys_smtpuser',''),('sys_smtppass',''),('sys_registerinfo',''),('templates','default'),('page','10'),('session','iweb_'),('lp','zh'),('web','http://localhost/iweb_mall/'),('url_r','false'),('seller_page','10'),('search_page','10'),('product_page','10'),('article_page','10'),('height1','84'),('width1','84'),('height2','300'),('width2','300'),('im_enable','false'),('sys_countjs',''),('email_send','false'),('timezone','8'),('offline','true'),('off_info','站点维护中。。。'),('map','false'),('map_key',''),('sys_logo','skin/default/images/malllogo.gif'),('template_mode','debug');

/*Data for the table `imall_user_rank` */

insert  into `imall_user_rank`(`rank_id`,`rank_name`,`privilege`) values (1,'普通会员','a:0:{}'),(2,'企业普通会员','a:7:{i:1;s:1:\"1\";i:2;s:2:\"30\";i:4;s:1:\"5\";i:5;s:1:\"5\";i:6;s:1:\"5\";i:7;s:1:\"5\";i:8;s:1:\"5\";}'),(3,'企业认证会员','a:7:{i:1;s:1:\"1\";i:2;s:3:\"300\";i:4;s:1:\"5\";i:5;s:1:\"5\";i:6;s:1:\"5\";i:7;s:1:\"5\";i:8;s:2:\"80\";}'),(4,'超级企业会员','a:7:{i:1;s:1:\"1\";i:2;s:3:\"500\";i:4;s:2:\"20\";i:5;s:2:\"20\";i:6;s:2:\"20\";i:7;s:2:\"20\";i:8;s:3:\"200\";}'),(7,'级别1','a:7:{i:1;s:1:\"1\";i:2;s:1:\"1\";i:4;s:1:\"1\";i:5;s:1:\"1\";i:6;s:1:\"1\";i:7;s:1:\"1\";i:8;s:1:\"1\";}');

/*Data for the table `imall_goods_transport` */

insert  into `imall_goods_transport`(`id`,`shop_id`,`transport_name`,`description`,`content`) values (1,2,'默认模板','默认模板','a:1:{s:3:\"ems\";a:36:{s:5:\"frist\";s:5:\"10.00\";s:6:\"second\";s:4:\"4.00\";i:2;a:2:{s:5:\"frist\";s:5:\"20.00\";s:6:\"second\";s:5:\"20.00\";}i:7;a:2:{s:5:\"frist\";s:5:\"20.00\";s:6:\"second\";s:5:\"20.00\";}i:12;a:2:{s:5:\"frist\";s:5:\"20.00\";s:6:\"second\";s:5:\"20.00\";}i:17;a:2:{s:5:\"frist\";s:5:\"20.00\";s:6:\"second\";s:5:\"20.00\";}i:3;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:4;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:8;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:9;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:13;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:14;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:18;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:19;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:22;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:23;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:24;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:27;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:28;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:29;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:32;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:33;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:34;a:2:{s:5:\"frist\";s:5:\"15.00\";s:6:\"second\";s:5:\"10.00\";}i:5;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:6;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:10;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:11;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:15;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:16;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:20;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:21;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:25;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:26;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:31;a:2:{s:5:\"frist\";s:5:\"12.00\";s:6:\"second\";s:4:\"5.00\";}i:30;a:2:{s:5:\"frist\";s:6:\"150.00\";s:6:\"second\";s:5:\"10.00\";}i:35;a:2:{s:5:\"frist\";s:6:\"150.00\";s:6:\"second\";s:5:\"10.00\";}}}'),(2,1,'全国','全国','a:3:{s:3:\"ems\";a:2:{s:5:\"frist\";s:5:\"50.00\";s:6:\"second\";s:5:\"10.00\";}s:3:\"pst\";a:2:{s:5:\"frist\";s:5:\"50.00\";s:6:\"second\";s:5:\"10.00\";}s:2:\"ex\";a:2:{s:5:\"frist\";s:5:\"50.00\";s:6:\"second\";s:5:\"10.00\";}}');

/*Data for the table `imall_category` */

insert  into `imall_category`(`cat_id`,`cat_name`,`parent_id`,`sort_order`,`goods_num`) values (1,'配饰',0,0,0),(2,'数码',0,0,0),(3,'文体',0,0,0),(108,'中性表',25,0,0),(5,'男女箱包',1,0,6),(6,'肩包',5,0,2),(7,'钱包',5,0,0),(8,'男包',5,0,4),(9,'旅行',5,0,0),(10,'服饰配件',1,0,1),(11,'围巾',10,0,0),(12,'帽子',10,0,1),(13,'皮带',10,0,0),(14,'颈环',10,0,0),(15,'珠宝',1,0,1),(16,'婚戒',15,0,0),(17,'翡翠',15,0,1),(18,'施华洛',15,0,0),(19,'千足金',15,0,0),(20,'饰品',1,0,0),(21,'项链',20,0,0),(22,'耳饰',20,0,0),(23,'发饰',20,0,0),(24,'手链',20,0,0),(25,'手表',1,0,1),(26,'男表',25,0,1),(27,'女表',25,0,0),(28,'机械表',25,0,0),(29,'电子表',25,0,0),(30,'眼镜',1,0,0),(31,'太阳镜',30,0,0),(32,'眼镜架',30,0,0),(33,'Zippo',30,0,0),(34,'烟具',30,0,0),(35,'手提',5,0,0),(36,'双肩',5,0,0),(42,'手机',2,0,1),(41,'手镯',20,0,0),(39,'项链',20,0,0),(40,'耳环',20,0,0),(43,'双卡双待',42,0,0),(44,'智能手机',42,0,1),(45,'学生机',42,0,0),(46,'炒股',42,0,0),(47,'数码相机',2,0,0),(48,'单反',47,0,0),(49,'摄像机',47,0,0),(50,'卡片机',47,0,0),(51,'镜头',47,0,0),(107,'对表',25,0,0),(53,'笔记本',2,0,2),(54,'上网本',53,0,0),(55,'苹果',53,0,2),(56,'东芝',53,0,0),(57,'神舟',53,0,0),(58,'电脑配件',2,0,0),(59,'CPU',58,0,0),(60,'主板',58,0,0),(61,'机箱',58,0,0),(62,'显示器',58,0,0),(63,'输入设备',58,0,0),(64,'办公',2,0,0),(65,'打印机',64,0,0),(66,'传真机',64,0,0),(67,'墨盒',64,0,0),(68,'碎纸机',64,0,0),(69,'MP4',2,0,0),(70,'蓝魔',69,0,0),(71,'艾诺',69,0,0),(72,'索尼',69,0,0),(73,'户外',3,0,0),(74,'冲锋衣',73,0,0),(75,'登山鞋',73,0,0),(76,'帐篷',73,0,0),(77,'背包',73,0,0),(78,'运动',3,0,0),(79,'羽拍',78,0,0),(80,'瑜伽',78,0,0),(81,'器械',78,0,0),(82,'玩具',3,0,0),(83,'木偶',82,0,0),(84,'娃娃',82,0,0),(85,'变形金刚',82,0,0),(86,'毛绒',82,0,0),(87,'书籍',3,0,0),(88,'工具书',87,0,0),(89,'畅销书',87,0,0),(90,'文史',87,0,0),(91,'理工',87,0,0),(92,'收藏',3,0,0),(93,'邮票',92,0,0),(94,'和田玉',92,0,0),(95,'古玩',92,0,0),(96,'钱币',92,0,0),(97,'宠物',3,0,0),(98,'狗粮',97,0,0),(99,'猫粮',97,0,0),(100,'服饰',97,0,0),(101,'零食',97,0,0),(102,'单肩包',5,0,0),(103,'脚链',20,0,0),(104,'玛瑙',15,0,0),(105,'宝石',15,0,0),(106,'白金',15,0,0),(109,'怀表',25,0,0),(110,'偏光镜',30,0,0),(111,'夜视镜',30,0,0),(112,'眼镜片',30,0,0),(113,'投影仪',64,0,0),(114,'扫描仪',64,0,0),(115,'显卡',58,0,0),(116,'硬盘',58,0,0),(117,'棒球',78,0,0),(118,'羽毛球',78,0,0),(119,'抱抱熊',82,0,0),(120,'芭比娃娃',82,0,0),(121,'迪斯尼',82,0,0),(122,'录音笔',69,0,0),(123,'车载',69,0,0),(124,'烧烤用具',73,0,0),(125,'指南针',73,0,0),(126,'净水器',73,0,0),(127,'足球',78,0,0),(128,'护具',78,0,0),(129,'小说',87,0,0),(130,'外语',87,0,0),(131,'财经',87,0,0),(132,'社会人文',87,0,0),(133,'紫砂/瓷器',92,0,0),(134,'个性收藏',92,0,0),(135,'日用品',97,0,0),(136,'宠物玩具',97,0,0),(137,'纽曼',69,0,0),(138,'清华紫光',69,0,0),(139,'闪光灯',47,0,0),(140,'三角架',47,0,0),(141,'联想',53,0,0);

/*Data for the table `imall_article` */

insert  into `imall_article`(`article_id`,`cat_id`,`title`,`content`,`thumb`,`admin_id`,`add_time`,`is_link`,`link_url`,`is_show`) values (1,1,'关于我们','关于我们','',1,'2009-10-22 15:50:53',0,'http://',1),(2,1,'常见问题','常见问题','',1,'2009-10-22 15:52:11',0,'http://',1),(3,1,'安全交易','安全交易','',1,'2009-10-22 15:52:21',0,'http://',1),(4,1,'购买流程','购买流程','',1,'2009-10-22 15:52:30',0,'http://',1),(5,1,'如何付款','如何付款','',1,'2009-10-22 15:52:45',0,'http://',1),(6,1,'联系我们','联系我们','',1,'2009-10-22 15:52:56',0,'http://',1),(7,1,'合作提案','合作提案','',1,'2009-10-22 15:53:06',0,'http://',1),(8,1,'网站地图','网站地图','',1,'2009-10-22 16:51:54',0,'http://',1),(11,2,'免费注册','免费注册','',1,'2009-10-23 08:29:33',0,'http://',1),(12,2,'开通帐户','开通帐户','',1,'2009-10-23 08:29:48',0,'http://',1),(13,2,'如何购买','如何购买','',1,'2009-10-23 08:29:59',0,'http://',1),(14,2,'购物车','购物车','',1,'2009-10-23 08:30:37',0,'http://',1),(15,2,'我买到的宝贝','我买到的宝贝','',1,'2009-10-23 08:30:49',0,'http://',1),(16,2,'聚合性商城特色','聚合性商城特色','',1,'2009-10-23 08:31:00',0,'http://',1),(17,2,'商品配送规则','商品配送规则 ','',1,'2009-10-23 08:31:12',0,'http://',1),(18,2,'搜索帮助','搜索帮助','',1,'2009-10-23 09:28:32',0,'http://',1),(19,3,'最新公告！','最新公告！','',1,'2009-12-23 14:55:49',0,'http://',1),(20,3,'iwebMALL即将上线','iwebMALL即将上线iwebMALL即将上线','',1,'2009-12-23 14:57:55',0,'http://',1),(21,3,'iwebMALL0.6测试版上线了~~~','iwebMALL0.6测试版上线了~~~','',1,'2009-12-23 15:06:50',0,'http://',1),(22,3,'iwebSNS0.9已发布~','iwebSNS0.9已发布~','',1,'2009-12-23 14:58:40',0,'http://',1),(23,6,'怎么购买？','怎么购买？<BR>','',1,'2010-04-10 10:42:57',0,'http://',1),(24,6,'费用贵不贵？','<BR>费用贵不贵？','',1,'2010-04-10 10:43:15',0,'http://',1),(25,6,'付款不发货怎么办？','<BR>付款不发货怎么办？','',1,'2010-04-10 10:43:40',0,'http://',1),(26,7,'如何推广？','<BR>如何推广？','',1,'2010-04-10 10:44:18',0,'http://',1),(27,7,'怎么才能提高流量！','怎么才能提高流量！<BR>','',1,'2010-04-10 10:44:46',0,'http://',1),(28,5,'欢迎大家使用iwebMall','<BR>欢迎大家使用iwebMall','',1,'2010-04-10 10:45:25',0,'http://',1),(29,3,'更新公告','<BR>更新公告','',1,'2010-04-10 10:46:32',0,'http://',0),(30,3,'发布公告','<BR>发布公告','',1,'2010-04-10 10:46:07',0,'http://',1),(31,3,'新版发布','新版发布','',1,'2010-04-10 10:46:44',0,'http://',1),(32,3,'试运营公告','试运营公告','',1,'2010-04-10 10:47:16',0,'http://',0);

/*Data for the table `imall_brand_category` */

insert  into `imall_brand_category`(`id`,`brand_id`,`cat_id`) values (1,2,2),(2,3,2),(3,8,2),(5,5,401),(6,13,401),(7,4,401),(8,2,401),(9,3,401),(10,2,54),(11,3,54),(12,4,54),(13,5,54),(14,18,1),(15,2,2),(16,3,2),(17,4,2),(18,5,2),(19,6,2),(20,7,2),(21,8,2),(22,9,2),(23,11,2),(24,12,2),(25,13,2),(26,17,3),(27,17,4),(28,18,5),(29,18,6),(30,18,7),(31,18,8),(32,18,9),(33,18,10),(34,18,11),(35,18,12),(36,18,13),(37,18,14),(38,18,15),(39,18,16),(40,18,17),(41,18,18),(42,18,19),(43,18,20),(44,18,21),(45,18,22),(46,18,23),(47,18,24),(48,19,25),(49,19,26),(50,19,27),(51,19,28),(52,19,29),(53,18,30),(54,18,31),(55,18,32),(56,18,33),(57,18,34),(58,18,35),(59,18,36),(60,18,37),(61,18,38),(62,18,39),(63,18,40),(64,18,41),(65,5,42),(66,8,42),(67,5,43),(68,8,43),(69,5,44),(70,8,44),(71,5,45),(72,8,45),(73,5,46),(74,8,46),(75,2,47),(76,3,47),(77,4,47),(78,5,47),(79,6,47),(80,7,47),(81,8,47),(82,9,47),(83,11,47),(84,12,47),(85,13,47),(86,2,48),(87,3,48),(88,4,48),(89,5,48),(90,6,48),(91,7,48),(92,8,48),(93,9,48),(94,11,48),(95,12,48),(96,13,48),(97,2,49),(98,3,49),(99,4,49),(100,5,49),(101,6,49),(102,7,49),(103,8,49),(104,9,49),(105,11,49),(106,12,49),(107,13,49),(108,2,50),(109,3,50),(110,4,50),(111,5,50),(112,6,50),(113,7,50),(114,8,50),(115,9,50),(116,11,50),(117,12,50),(118,13,50),(119,2,51),(120,3,51),(121,4,51),(122,5,51),(123,6,51),(124,7,51),(125,8,51),(126,9,51),(127,11,51),(128,12,51),(129,13,51),(130,2,53),(131,3,53),(132,4,53),(133,5,53),(134,6,53),(135,7,53),(136,8,53),(137,9,53),(138,11,53),(139,12,53),(140,13,53),(141,2,54),(142,3,54),(143,4,54),(144,5,54),(145,6,54),(146,7,54),(147,8,54),(148,9,54),(149,11,54),(150,12,54),(151,13,54),(152,2,55),(153,3,55),(154,4,55),(155,5,55),(156,6,55),(157,7,55),(158,8,55),(159,9,55),(160,11,55),(161,12,55),(162,13,55),(163,2,56),(164,3,56),(165,4,56),(166,5,56),(167,6,56),(168,7,56),(169,8,56),(170,9,56),(171,11,56),(172,12,56),(173,13,56),(174,2,57),(175,3,57),(176,4,57),(177,5,57),(178,6,57),(179,7,57),(180,8,57),(181,9,57),(182,11,57),(183,12,57),(184,13,57),(185,2,58),(186,3,58),(187,4,58),(188,5,58),(189,6,58),(190,7,58),(191,8,58),(192,9,58),(193,11,58),(194,12,58),(195,13,58),(196,2,59),(197,3,59),(198,4,59),(199,5,59),(200,6,59),(201,7,59),(202,8,59),(203,9,59),(204,11,59),(205,12,59),(206,13,59),(207,2,60),(208,3,60),(209,4,60),(210,5,60),(211,6,60),(212,7,60),(213,8,60),(214,9,60),(215,11,60),(216,12,60),(217,13,60),(218,2,61),(219,3,61),(220,4,61),(221,5,61),(222,6,61),(223,7,61),(224,8,61),(225,9,61),(226,11,61),(227,12,61),(228,13,61),(229,2,62),(230,3,62),(231,4,62),(232,5,62),(233,6,62),(234,7,62),(235,8,62),(236,9,62),(237,11,62),(238,12,62),(239,13,62),(240,2,63),(241,3,63),(242,4,63),(243,5,63),(244,6,63),(245,7,63),(246,8,63),(247,9,63),(248,11,63),(249,12,63),(250,13,63),(251,2,64),(252,3,64),(253,4,64),(254,5,64),(255,6,64),(256,7,64),(257,8,64),(258,9,64),(259,11,64),(260,12,64),(261,13,64),(262,2,65),(263,3,65),(264,4,65),(265,5,65),(266,6,65),(267,7,65),(268,8,65),(269,9,65),(270,11,65),(271,12,65),(272,13,65),(273,2,66),(274,3,66),(275,4,66),(276,5,66),(277,6,66),(278,7,66),(279,8,66),(280,9,66),(281,11,66),(282,12,66),(283,13,66),(284,2,67),(285,3,67),(286,4,67),(287,5,67),(288,6,67),(289,7,67),(290,8,67),(291,9,67),(292,11,67),(293,12,67),(294,13,67),(295,2,68),(296,3,68),(297,4,68),(298,5,68),(299,6,68),(300,7,68),(301,8,68),(302,9,68),(303,11,68),(304,12,68),(305,13,68),(306,2,69),(307,3,69),(308,4,69),(309,5,69),(310,6,69),(311,7,69),(312,8,69),(313,9,69),(314,11,69),(315,12,69),(316,13,69),(317,2,70),(318,3,70),(319,4,70),(320,5,70),(321,6,70),(322,7,70),(323,8,70),(324,9,70),(325,11,70),(326,12,70),(327,13,70),(328,2,71),(329,3,71),(330,4,71),(331,5,71),(332,6,71),(333,7,71),(334,8,71),(335,9,71),(336,11,71),(337,12,71),(338,13,71),(339,2,72),(340,3,72),(341,4,72),(342,5,72),(343,6,72),(344,7,72),(345,8,72),(346,9,72),(347,11,72),(348,12,72),(349,13,72),(350,17,73),(351,17,74),(352,17,75),(353,17,76),(354,17,77),(355,17,78),(356,17,79),(357,17,80),(358,17,81),(359,17,82),(360,17,83),(361,17,84),(362,17,85),(363,17,86),(364,17,87),(365,17,88),(366,17,89),(367,17,90),(368,17,91),(369,17,92),(370,17,93),(371,17,94),(372,17,95),(373,17,96),(374,17,97),(375,17,98),(376,17,99),(377,17,100),(378,17,101),(379,18,102),(380,18,103),(381,18,104),(382,18,105),(383,18,106),(384,19,107),(385,19,108),(386,19,109),(387,18,110),(388,18,111),(389,18,112),(390,2,113),(391,3,113),(392,4,113),(393,5,113),(394,6,113),(395,7,113),(396,8,113),(397,9,113),(398,11,113),(399,12,113),(400,13,113),(401,2,114),(402,3,114),(403,4,114),(404,5,114),(405,6,114),(406,7,114),(407,8,114),(408,9,114),(409,11,114),(410,12,114),(411,13,114),(412,2,115),(413,3,115),(414,4,115),(415,5,115),(416,6,115),(417,7,115),(418,8,115),(419,9,115),(420,11,115),(421,12,115),(422,13,115),(423,2,116),(424,3,116),(425,4,116),(426,5,116),(427,6,116),(428,7,116),(429,8,116),(430,9,116),(431,11,116),(432,12,116),(433,13,116),(434,17,117),(435,17,118),(436,17,119),(437,17,120),(438,17,121),(439,2,122),(440,3,122),(441,4,122),(442,5,122),(443,6,122),(444,7,122),(445,8,122),(446,9,122),(447,11,122),(448,12,122),(449,13,122),(450,2,123),(451,3,123),(452,4,123),(453,5,123),(454,6,123),(455,7,123),(456,8,123),(457,9,123),(458,11,123),(459,12,123),(460,13,123),(461,17,124),(462,17,125),(463,17,126),(464,17,127),(465,17,128),(466,17,129),(467,17,130),(468,17,131),(469,17,132),(470,17,133),(471,17,134),(472,17,135),(473,17,136),(474,2,137),(475,3,137),(476,4,137),(477,5,137),(478,6,137),(479,7,137),(480,8,137),(481,9,137),(482,11,137),(483,12,137),(484,13,137),(485,2,138),(486,3,138),(487,4,138),(488,5,138),(489,6,138),(490,7,138),(491,8,138),(492,9,138),(493,11,138),(494,12,138),(495,13,138),(496,2,139),(497,3,139),(498,4,139),(499,5,139),(500,6,139),(501,7,139),(502,8,139),(503,9,139),(504,11,139),(505,12,139),(506,13,139),(507,2,140),(508,3,140),(509,4,140),(510,5,140),(511,6,140),(512,7,140),(513,8,140),(514,9,140),(515,11,140),(516,12,140),(517,13,140),(518,2,141),(519,3,141),(520,4,141),(521,5,141),(522,6,141),(523,7,141),(524,8,141),(525,9,141),(526,11,141),(527,12,141),(528,13,141);
