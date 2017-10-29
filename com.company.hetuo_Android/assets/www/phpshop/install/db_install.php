<?php
	function createTable($host, $username, $password, $db_name, $db_) {
		if(@mysql_pconnect($host, $username, $password) == false) {
			alert("错误：连接不上数据库,请检查设置。");
			return;
		} 
		if(@mysql_select_db($db_name) == false) {
			alert("错误：连接不上数据库 $db_name,请检查设置。");
			return;
		} 
		mysql_query('set names utf8;');
	
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "admin`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "admin` (
                `id` int(11) NOT NULL auto_increment,                                                 
                `email` varchar(250) default NULL,                                                    
                `name` varchar(50) NOT NULL,                                                          
                `pass` varchar(50) NOT NULL,                                                          
                `power` varchar(50) default NULL,                                                     
                `lastloginip` varchar(30) default NULL,                                               
                `lastlogintime` datetime default NULL,                                                
                `question` varchar(250) default NULL,                                                 
                `answer` varchar(250) default NULL,                                                   
                `logintimes` int(11) default '1',                                                     
                `loginip` varchar(30) default NULL,                                                   
                `logintime` datetime default NULL,                                                    
                `add_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
                PRIMARY KEY  (`id`),                                                                  
                UNIQUE KEY `name` (`name`)                                      
            ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
		
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "advertising`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "advertising` (
                      `cid` int(11) NOT NULL,                                                               
                      `title` varchar(250) default NULL,                                                    
                      `value1` text,                                                                        
                      `value2` text,                                                                        
                      `value3` text,                                                                        
                      `value4` text,                                                                        
                      `value5` text,                                                                        
                      `value6` text,                                                                        
                      `value7` text,                                                                        
                      `value8` text,                                                                        
                      `value9` text,                                                                        
                      `value10` text,                                                                       
                      `value11` text,                                                                       
                      `value12` text,                                                                       
                      `value13` text,                                                                       
                      `value14` text,                                                                       
                      `value15` text,                                                                       
                      `value16` text,                                                                       
                      `value17` text,                                                                       
                      `value18` text,                                                                       
                      `value19` text,                                                                       
                      `value20` text,                                                                       
                      `add_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
                      UNIQUE KEY `cid` (`cid`)                                                      
                  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
	
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "attribute`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "attribute` (
                    `id` int(11) NOT NULL auto_increment,                  
                    `cid` int(11) NOT NULL default '0',                    
                    `name` varchar(250) NOT NULL,                          
                    `mainatr` enum('0','1') NOT NULL default '0',          
                    `isfilter` enum('0','1') NOT NULL default '0',         
                    `orderid` int(11) default NULL,                        
                    PRIMARY KEY  (`id`)                               
                ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
		
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "baseset`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "baseset` (
                  `id` int(11) NOT NULL auto_increment,                
                  `type` varchar(50) NOT NULL,                         
                  `fun` varchar(250) NOT NULL,                         
                  `cid` int(11) default NULL,                          
                  `value1` text,                                       
                  `value2` text,                                       
                  `value3` text,                                       
                  `value4` text,                                       
                  `value5` text,                                       
                  `value6` text,                                       
                  `value7` text,                                       
                  `value8` text,                                       
                  `value9` text,                                       
                  `value10` text,                                      
                  `value11` text,                                      
                  `value12` text,                                      
                  `value13` text,                                      
                  `value14` text,                                      
                  `value15` text,                                      
                  `value16` text,                                      
                  `value17` text,                                      
                  `value18` text,                                      
                  `value19` text,                                      
                  `value20` text,                                      
                  `value21` text,                                      
                  `value22` text,                                      
                  `value23` text,                                      
                  `value24` text,                                      
                  `value25` text,                                      
                  `value26` text,                                      
                  `value27` text,                                      
                  `value28` text,                                      
                  `value29` text,                                      
                  `value30` text,                                      
                  PRIMARY KEY  (`id`),                                 
                  UNIQUE KEY `fun` (`fun`)
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
		
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "classes`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "classes` (
                  `id` int(11) NOT NULL auto_increment,                 
                  `cid` int(11) default '0',                            
                  `cpid` int(11) default '0',                           
                  `ccidnum` int(11) default '0',                        
                  `orderid` int(11) NOT NULL default '0',               
                  `sortid` varchar(3) default '0',                      
                  `sort` tinyint(3) default '0',                        
                  `depth` tinyint(3) default '0',                       
                  `name` varchar(250) NOT NULL,                         
                  `enname` varchar(250) default NULL,                   
                  `des` varchar(125) default NULL,                      
                  `spliter` varchar(125) default NULL,                  
                  `spliterchar` varchar(125) default NULL,              
                  `picurl` varchar(250) default NULL,                   
                  `url` varchar(250) default NULL,                      
                  `hidden` enum('1','0') default '1',                   
                  `espegroup` varchar(250) default NULL,                
                  `espeuser` varchar(250) default NULL,                 
                  `affiche` text,                                       
                  `advalue` varchar(250) default NULL,                  
                  PRIMARY KEY  (`id`) 
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
	
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "function`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "function` (
                   `id` int(11) NOT NULL auto_increment,                 
                   `fsid` int(11) default '0',                           
                   `orderid` int(11) default '0',                        
                   `name` varchar(250) NOT NULL,                         
                   `enname` varchar(250) NOT NULL,                       
                   `fun` varchar(250) default NULL,                      
                   `display` enum('0','1') default '0',                  
                   PRIMARY KEY  (`id`) 
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
		
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "news`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "news` (
               `id` int(11) NOT NULL auto_increment,                                                 
               `cid` int(11) default NULL,                                                           
               `title` varchar(250) default NULL,                                                    
               `content` text,                                                                       
               `add_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
               `author` varchar(50) default NULL,                                                    
               `source_from` varchar(100) default NULL,                                              
               PRIMARY KEY  (`id`)
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		if (false == mysql_query($sqlstr)) {
			alert("安装数据失败,请检查设置。");
			return;
		}
		
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "order_bill`;";
		mysql_query($sqlstr);
		
		$sqlstr = "CREATE TABLE `" . $db_ . "order_bill` (
                     `id` int(11) NOT NULL auto_increment,                       
                     `billno` varchar(50) default NULL,                          
                     `uid` varchar(11) default NULL,                             
                     `sellprice` varchar(50) default NULL,                       
                     `pay_price` varchar(50) default NULL,                       
                     `state` enum('0','1','2','3','4','5') default NULL,         
                     `add_date` timestamp NOT NULL default CURRENT_TIMESTAMP,    
                     `md5id` varchar(50) default NULL,                           
                     `freight` varchar(50) default NULL,                         
                     `freight_price` varchar(50) default NULL,                   
                     `freight_no` varchar(50) default NULL,                      
                     `paymethod` varchar(50) default NULL,                       
                     `pay_success` enum('0','1','2','3','4','5','6') NOT NULL default '0' COMMENT '0 表示等待买家付款;1 表示买家付款成功,等待卖家发货;2 卖家已经发货等待买家确认;3 表示交易已经成功结束',  
                     `web_pay_code` varchar(10) default NULL,                    
                     `name` varchar(100) default NULL,                           
                     `email` varchar(100) NOT NULL,                              
                     `phone` char(50) default NULL,                              
                     `mobile` char(50) default NULL,                             
                     `address` varchar(100) default NULL,                        
                     `postcode` char(50) default NULL,                           
                     `userip` char(50) default NULL,                             
                     `ss` text,                                                  
                     `shipment_date` datetime default NULL,                      
                     `remark` text,                                              
                     `host` varchar(50) default NULL,                            
                     `cps` int(11) default NULL,                                 
                     PRIMARY KEY  (`id`)  
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "power`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "power` (
	  `aid` tinyint(3) NOT NULL,
	  `fid` tinyint(3) NOT NULL
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product` (
                  `id` int(11) NOT NULL auto_increment,                                     
                  `cid` int(11) default NULL,                                               
                  `psid` int(11) default NULL,                                              
                  `name` varchar(50) default NULL,                                          
                  `enname` varchar(50) default NULL,                                        
                  `store` int(11) default '1',                                              
                  `no_store` enum('0','1') default '0',                                     
                  `describes` text,                                                         
                  `b_pic` varchar(250) default NULL,                                        
                  `m_pic` varchar(250) default NULL,                                        
                  `s_pic` varchar(250) default NULL,                                        
                  `factory_number` varchar(50) default NULL,                                
                  `number` varchar(50) default NULL,                                        
                  `sid` int(11) default NULL,                                               
                  `model` varchar(50) default NULL,                                         
                  `bid` int(11) default '0',                                                
                  `price` varchar(100) default NULL,                                        
                  `price_mill` varchar(100) default NULL,                                   
                  `price_member` varchar(100) default NULL,                                 
                  `price_special` varchar(100) default NULL,                                
                  `price_market` varchar(100) default NULL,                                 
                  `hit_times` int(11) default '0',                                          
                  `order_times` int(11) default '0',                                        
                  `online` enum('1','0') NOT NULL default '0',                              
                  `special_offer` enum('1','0') NOT NULL default '0',                       
                  `market_offer` enum('1','0') default '0',                                 
                  `member_offer` enum('1','0') default '0',                                 
                  `buy_times` int(11) default '0',                                          
                  `hot_times` int(11) default '0',                                          
                  `product_related` varchar(50) default '0',                                
                  `atr1` int(11) default NULL,                                              
                  `value1` varchar(100) default NULL,                                       
                  `atr2` int(11) default NULL,                                              
                  `value2` varchar(100) default NULL,                                       
                  `atr3` int(11) default NULL,                                              
                  `value3` varchar(100) default NULL,                                       
                  `atr4` int(11) default NULL,                                              
                  `value4` varchar(100) default NULL,                                       
                  `atr5` int(11) default NULL,                                              
                  `value5` varchar(100) default NULL,                                       
                  `atr6` int(11) default NULL,                                              
                  `value6` varchar(100) default NULL,                                       
                  `atr7` int(11) default NULL,                                              
                  `value7` varchar(100) default NULL,                                       
                  `atr8` int(11) default NULL,                                              
                  `value8` varchar(100) default NULL,                                       
                  PRIMARY KEY  (`id`),                                                      
                  UNIQUE KEY `cid` (`id`,`cid`),                                            
                  KEY `price` (`price`),                                                    
                  KEY `bid` (`bid`),                                                        
                  KEY `classesid` (`cid`),                                                  
                  KEY `hit_times` (`hit_times`),                                            
                  KEY `atr1` (`atr1`),                                                      
                  KEY `value1` (`value1`),                                                  
                  KEY `atr2` (`atr2`),                                                      
                  KEY `atr3` (`atr3`),                                                      
                  KEY `atr4` (`atr4`),                                                      
                  KEY `atr5` (`atr5`),                                                      
                  KEY `atr6` (`atr6`),                                                      
                  KEY `atr7` (`atr7`),                                                      
                  KEY `atr8` (`atr8`),                                                      
                  FULLTEXT KEY `name` (`name`),                                             
                  FULLTEXT KEY `dis` (`describes`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_attribute`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_attribute` (
                            `cid` int(11) default NULL,              
                            `pid` int(11) default NULL,              
                            `atrid` int(11) default NULL,            
                            `value` varchar(100) default NULL,       
                            `name` varchar(100) default NULL,        
                            UNIQUE KEY `pid` (`pid`,`atrid`),        
                            KEY `atrid` (`atrid`),                   
                            FULLTEXT KEY `name` (`name`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_bill`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_bill` (
                       `id` int(11) NOT NULL default '0',                        
                       `cid` int(11) default NULL,                               
                       `psid` int(11) default NULL,                              
                       `name` varchar(50) default NULL,                          
                       `enname` varchar(50) default NULL,                        
                       `store` int(11) default NULL,                             
                       `no_store` enum('0','1') default '0',                     
                       `describes` text,                                         
                       `b_pic` varchar(250) default NULL,                        
                       `m_pic` varchar(250) default NULL,                        
                       `s_pic` varchar(250) default NULL,                        
                       `factory_number` varchar(50) default NULL,                
                       `number` varchar(50) default NULL,                        
                       `sid` int(11) default NULL,                               
                       `model` varchar(50) default NULL,                         
                       `bid` int(11) default '0',                                
                       `price` varchar(100) default NULL,                        
                       `price_mill` varchar(100) default NULL,                   
                       `price_member` varchar(100) default NULL,                 
                       `price_special` varchar(100) default NULL,                
                       `price_market` varchar(100) default NULL,                 
                       `hit_times` int(11) default '0',                          
                       `order_times` int(11) default '0',                        
                       `online` enum('1','0') NOT NULL default '0',              
                       `special_offer` enum('1','0') NOT NULL default '0',       
                       `market_offer` enum('1','0') default '0',                 
                       `member_offer` enum('1','0') default '0',                 
                       `buy_times` int(11) default '0',                          
                       `hot_times` int(11) default '0',                          
                       `product_related` varchar(50) default '0',                
                       `atr1` int(11) default NULL,                              
                       `value1` varchar(100) default NULL,                       
                       `atr2` int(11) default NULL,                              
                       `value2` varchar(100) default NULL,                       
                       `atr3` int(11) default NULL,                              
                       `value3` varchar(100) default NULL,                       
                       `atr4` int(11) default NULL,                              
                       `value4` varchar(100) default NULL,                       
                       `atr5` int(11) default NULL,                              
                       `value5` varchar(100) default NULL,                       
                       `atr6` int(11) default NULL,                              
                       `value6` varchar(100) default NULL,                       
                       `atr7` int(11) default NULL,                              
                       `value7` varchar(100) default NULL,                       
                       `atr8` int(11) default NULL,                              
                       `value8` varchar(100) default NULL,                       
                       `num` char(20) default NULL,                              
                       `billno` char(50) NOT NULL,                               
                       `md5id` char(50) default NULL,                            
                       `sellprice` char(50) default NULL COMMENT '销售价格' 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_brand`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_brand` (
                        `id` int(11) NOT NULL auto_increment,                 
                        `name` varchar(250) default NULL,                     
                        `enname` varchar(250) default NULL,                   
                        PRIMARY KEY  (`id`) 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_fliter`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_fliter` (
                         `cid` int(11) default NULL,           
                         `pid` int(11) default NULL,           
                         `name` varchar(100) default NULL,     
                         `price` varchar(20) default NULL,     
                         `atr1` int(11) default NULL,          
                         `value1` varchar(100) default NULL,   
                         `atr2` int(11) default NULL,          
                         `value2` varchar(100) default NULL,   
                         `atr3` int(11) default NULL,          
                         `value3` varchar(100) default NULL,   
                         `atr4` int(11) default NULL,          
                         `value4` varchar(100) default NULL,   
                         `atr5` int(11) default NULL,          
                         `value5` varchar(100) default NULL,   
                         `atr6` int(11) default NULL,          
                         `value6` varchar(100) default NULL,   
                         `atr7` int(11) default NULL,          
                         `value7` varchar(100) default NULL,   
                         `atr8` int(11) default NULL,          
                         `value8` varchar(100) default NULL,   
                         UNIQUE KEY `pid` (`pid`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_news`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_news` (
                       `cid` int(11) default '0',          
                       `nid` int(11) default NULL,         
                       `pid` int(11) default NULL
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_review`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_review` (
                         `id` int(11) NOT NULL auto_increment,                
                         `pid` int(11) default NULL,                          
                         `uid` varchar(11) default NULL,                      
                         `title` varchar(250) default NULL,                   
                         `good` varchar(250) default NULL,                    
                         `bad` varchar(250) default NULL,                     
                         `contents` varchar(500) default NULL,                
                         `is_help` int(11) default '0',                       
                         `no_help` int(11) default '0',                       
                         `point` int(11) default '0',                         
                         `billno` bigint(11) default NULL,                    
                         `ip` varchar(250) default NULL,                      
                         `init` enum('0','1') NOT NULL default '0',           
                         `add_date` datetime default NULL,                    
                         PRIMARY KEY  (`id`) 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
		$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "product_series`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "product_series` (
                         `id` int(11) NOT NULL auto_increment,                
                         `name` varchar(250) default NULL,                    
                         `enname` varchar(250) default NULL,                  
                         `bid` int(11) default '0',                           
                         PRIMARY KEY  (`id`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "shop_bank`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "shop_bank` (
                    `id` int(11) NOT NULL auto_increment,      
                    `name` varchar(100) default NULL,          
                    `payee` varchar(100) default NULL,         
                    `accounts` varchar(100) default NULL,      
                    `describes` varchar(250) default NULL,     
                    `type` enum('B','W','Y','H') default 'B',  
                    PRIMARY KEY  (`id`) 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "shop_freight`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "shop_freight` (
                       `id` int(11) NOT NULL auto_increment,                
                       `name` varchar(100) default NULL,                    
                       `describes` varchar(250) default NULL,               
                       `freight` varchar(100) default NULL,                 
                       `shop_price` varchar(50) default NULL,               
                       PRIMARY KEY  (`id`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "shop_supply`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "shop_supply` (
                      `id` int(11) NOT NULL auto_increment,   
                      `name` varchar(100) default NULL,       
                      `phone` varchar(100) default NULL,      
                      `mobile` varchar(100) default NULL,     
                      `email` varchar(100) default NULL,      
                      `address` varchar(100) default NULL,    
                      `describes` varchar(250) default NULL,  
                      PRIMARY KEY  (`id`) 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "tem_shop`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "tem_shop` (
                   `pid` int(11) default NULL,                                                           
                   `uid` varchar(50) default NULL,                                                       
                   `num` int(11) default '1',                                                            
                   `order_ip` char(50) default NULL,                                                     
                   `md5id` char(250) default NULL,                                                       
                   `add_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
                   UNIQUE KEY `md5id` (`pid`,`md5id`) 
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "DROP TABLE IF EXISTS `" . $db_ . "user`;";
	mysql_query($sqlstr);
	
	$sqlstr = "CREATE TABLE `" . $db_ . "user` (
               `id` int(11) NOT NULL auto_increment,                                                 
               `mtid` tinyint(3) default NULL,                                                       
               `name` varchar(250) NOT NULL,                                                         
               `active` enum('2','1','0') NOT NULL default '1',                                      
               `mypic` varchar(250) default NULL,                                                    
               `email` varchar(100) NOT NULL,                                                        
               `password` varchar(100) NOT NULL,                                                     
               `phone` varchar(50) default NULL,                                                     
               `mobile` varchar(50) default NULL,                                                    
               `address` varchar(250) default NULL,                                                  
               `postcode` varchar(50) default NULL,                                                  
               `sex` varchar(50) default NULL,                                                       
               `birthday` varchar(50) default NULL,                                                  
               `question` varchar(250) default NULL,                                                 
               `answer` varchar(250) default NULL,                                                   
               `islock` enum('1','0') NOT NULL default '0',                                          
               `reg_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,  
               `rember_times` varchar(50) default NULL,                                              
               `log_logindate` datetime default NULL,                                                
               `log_loginip` varchar(32) default NULL,                                               
               `lastloginip` varchar(32) default NULL,                                               
               `lastlogindate` datetime default NULL,                                                
               `regip` varchar(100) default NULL,                                                    
               `qq` varchar(50) default NULL,                                                        
               `msn` varchar(100) default NULL,                                                      
               `taobao` varchar(100) default NULL,                                                   
               `alibaba` varchar(100) default NULL,                                                  
               `sina` varchar(100) default NULL,                                                     
               `163` varchar(100) default NULL,                                                      
               `sohu` varchar(100) default NULL,                                                     
               `skype` varchar(100) default NULL,                                                    
               `loginid` varchar(100) default NULL,                                                  
               `isreg` enum('0','1') NOT NULL default '1',                                           
               PRIMARY KEY  (`id`),                                                                  
               UNIQUE KEY `email` (`email`),                                                         
               KEY `name` (`name`),                                                                  
               KEY `ucid` (`mtid`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	
	$sqlstr = "insert  into `" . $db_ . "baseset`(`id`,`type`,`fun`) values (1,'Baseset','baseset'),(2,'Baseset','regAndCon'),(3,'Baseset','interface'),(4,'Baseset','searchoptim'),(5,'Baseset','watermark'),(6,'Baseset','service'),(7,'Merchandise','commend');";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "UPDATE `" . $db_ . "baseset` SET value11 = 'beauty' WHERE id = 1;";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "insert  into `" . $db_ . "function`(`id`,`fsid`,`orderid`,`name`,`enname`,`fun`,`display`) values (1,1,0,'基本设置','Baseset','Baseset','1'),(2,1,1,'基本设置','baseset','baseset','1'),(3,3,0,'类别设置','ClasssSet','ClasssSet','1'),(4,3,1,'商品类别设置','setclasses','setclasses','1'),(5,5,0,'商品管理','Merchandise','Merchandise','1'),(6,1,9,'数据备份','vend','vend','0'),(7,7,0,'订单管理','OrderForm','OrderForm','1'),(8,7,1,'订单处理','check','check','1'),(9,99,2,'订单状态','Stat','stat','0'),(10,99,3,'促销计划','plan','plan','0'),(11,99,4,'1','shipmented','shipmented','0'),(12,1,4,'网站相关页面','regAndCon','regAndCon','1'),(13,1,5,'Email服务器设置','interface','interface','1'),(14,1,2,'搜索引擎优化','searchoptim','searchoptim','1'),(15,5,1,'商品管理','goods','goods','1'),(16,16,0,'会员管理','Member','Member','1'),(17,16,1,'会员管理','modifymenber','modifymenber','1'),(18,99,2,'1','integral','integral','0'),(19,19,0,'新闻中心','news','News','1'),(20,19,1,'新闻管理','productnews','productnews','1'),(21,5,3,'页面商品参数','commend','commend','1'),(22,22,0,'营销管理','Spread','Spread','1'),(23,99,1,'email营销','email','emailSpread','0'),(24,5,4,'商品评价管理','ProductReview','review','1'),(25,24,1,'管理留言','modifyguestbook','modifyguestbook','0'),(26,3,2,'类别商品属性','attribute','attribute','1'),(27,5,2,'添加商品','addgoods','addgoods','1'),(28,3,3,'品牌类别','Brand','brand','1'),(29,1,3,'图片水印','watermark','watermark','1'),(30,3,4,'品牌系列','series','series','1'),(31,3,5,'货源商家类别','supply','supply','1'),(32,3,6,'会员类别','menberclass','menberclass','0'),(33,99,3,'1','indagate','indagate','0'),(34,22,2,'广告营销','ad','ad','1'),(35,1,6,'地址电话及客服设置','service','service','1'),(36,19,2,'添加新闻','addnews','addnews','1'),(37,3,7,'配送方式类别','freight','freight','1'),(38,3,8,'付款类别','bank','bank','1'),(39,1,8,'我的帐户','Password','password','0'),(40,1,7,'网站管理员管理','admin','admin','1');
";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	
	
	UpdateCommonDao::updateSiteInfo("searchoptim");
	UpdateCommonDao::updateSiteInfo();
	UpdateCommonDao::updateSiteInfo("regAndCon");
	UpdateCommonDao::updateSiteInfo("service");
	UpdateCommonDao::updateSiteInfo("interface");
	UpdateCommonDao::updateSiteInfo("watermark");
	UpdateCommonDao::updateClassesInfo();
	UpdateCommonDao::updatePayInfo('payinfo', '');
	UpdateCommonDao::updateSiteInfo("commend");
	return true;
}

?>