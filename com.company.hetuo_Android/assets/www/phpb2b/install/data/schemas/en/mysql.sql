-- 
-- Table structure `pb_adminfields`
-- 

DROP TABLE IF EXISTS `pb_adminfields`;
CREATE TABLE `pb_adminfields` (
  `member_id` int(10) NOT NULL,
  `depart_id` tinyint(1) NOT NULL default '0',
  `first_name` varchar(25) NOT NULL default '',
  `last_name` varchar(25) NOT NULL default '',
  `level` tinyint(1) NOT NULL default '0',
  `last_login` int(10) NOT NULL default '0',
  `last_ip` varchar(25) NOT NULL default '',
  `expired` int(10) NOT NULL default '0',
  `permissions` text NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adminmodules`
-- 

DROP TABLE IF EXISTS `pb_adminmodules`;
CREATE TABLE `pb_adminmodules` (
  `id` smallint(3) NOT NULL auto_increment,
  `parent_id` smallint(3) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adminnotes`
-- 

DROP TABLE IF EXISTS `pb_adminnotes`;
CREATE TABLE `pb_adminnotes` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '0',
  `title` varchar(100) NOT NULL default '',
  `content` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adminprivileges`
-- 

DROP TABLE IF EXISTS `pb_adminprivileges`;
CREATE TABLE `pb_adminprivileges` (
  `id` int(5) NOT NULL auto_increment,
  `adminmodule_id` int(5) NOT NULL default '0',
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adminroles`
-- 

DROP TABLE IF EXISTS `pb_adminroles`;
CREATE TABLE `pb_adminroles` (
  `id` tinyint(2) NOT NULL auto_increment,
  `name` varchar(25) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adses`
-- 

DROP TABLE IF EXISTS `pb_adses`;
CREATE TABLE `pb_adses` (
  `id` smallint(6) NOT NULL auto_increment,
  `adzone_id` smallint(3) NOT NULL default '0',
  `title` varchar(50) NOT NULL default '',
  `description` text,
  `is_image` tinyint(1) NOT NULL default '1',
  `source_name` varchar(100) NOT NULL default '',
  `source_type` varchar(100) NOT NULL default '',
  `source_url` varchar(255) NOT NULL default '',
  `target_url` varchar(255) NOT NULL default '',
  `width` smallint(6) NOT NULL default '0',
  `height` smallint(6) NOT NULL default '0',
  `alt_words` varchar(25) NOT NULL default '',
  `start_date` int(10) NOT NULL default '0',
  `end_date` int(10) NOT NULL default '0',
  `priority` tinyint(1) NOT NULL default '0',
  `clicked` smallint(6) NOT NULL default '1',
  `target` enum('_parent','_self','_blank') NOT NULL default '_blank',
  `seq` tinyint(1) NOT NULL default '0',
  `state` tinyint(1) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '0',
  `picture_replace` varchar(255) NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_adzones`
-- 

DROP TABLE IF EXISTS `pb_adzones`;
CREATE TABLE `pb_adzones` (
  `id` smallint(6) NOT NULL auto_increment,
  `membergroup_ids` varchar(50) NOT NULL default '',
  `what` varchar(10) NOT NULL default '',
  `style` tinyint(1) NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `description` text,
  `additional_adwords` text,
  `price` float(9,2) NOT NULL default '0',
  `file_name` varchar(100) NOT NULL default '',
  `width` smallint(6) NOT NULL default '0',
  `height` smallint(6) NOT NULL default '0',
  `wrap` smallint(6) NOT NULL default '0',
  `max_ad` smallint(6) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_albums`
-- 

DROP TABLE IF EXISTS `pb_albums`;
CREATE TABLE `pb_albums` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '0',
  `attachment_id` int(10) NOT NULL default '0',
  `type_id` smallint(3) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_announcements`
-- 

DROP TABLE IF EXISTS `pb_announcements`;
CREATE TABLE `pb_announcements` (
  `id` smallint(6) unsigned NOT NULL auto_increment,
  `announcetype_id` smallint(3) NOT NULL default '0',
  `subject` varchar(255) NOT NULL default '',
  `message` text,
  `display_order` tinyint(1) NOT NULL default '0',
  `display_expiration` int(10) unsigned NOT NULL default '0',
  `created` int(10) unsigned NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_announcementtypes`
-- 

DROP TABLE IF EXISTS `pb_announcementtypes`;
CREATE TABLE `pb_announcementtypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_areas`
-- 

DROP TABLE IF EXISTS `pb_areas`;
CREATE TABLE `pb_areas` (
  `id` smallint(6) NOT NULL auto_increment,
  `attachment_id` int(10) NOT NULL default '0',
  `areatype_id` smallint(3) NOT NULL default '0',
  `child_ids` text,
  `top_parentid` smallint(6) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `name` varchar(255) NOT NULL default '',
  `url` varchar(255) NOT NULL default '',
  `alias_name` varchar(255) NOT NULL default '',
  `highlight` tinyint(1) NOT NULL default '0',
  `parent_id` smallint(6) NOT NULL default '0',
  `display_order` tinyint(1) NOT NULL default '0',
  `description` text,
  `available` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_areatypes`
-- 

DROP TABLE IF EXISTS `pb_areatypes`;
CREATE TABLE `pb_areatypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_attachments`
-- 

DROP TABLE IF EXISTS `pb_attachments`;
CREATE TABLE `pb_attachments` (
  `id` int(10) NOT NULL auto_increment,
  `attachmenttype_id` smallint(3) NOT NULL default '0',
  `member_id` int(10) NOT NULL default '-1',
  `file_name` char(100) NOT NULL default '',
  `attachment` char(255) NOT NULL default '',
  `title` char(100) NOT NULL default '',
  `description` text,
  `file_type` char(50) NOT NULL default '0',
  `file_size` mediumint(8) NOT NULL default '0',
  `thumb` varchar(100) NOT NULL default '',
  `remote` varchar(100) NOT NULL default '',
  `is_image` tinyint(1) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_companies`
-- 

DROP TABLE IF EXISTS `pb_companies`;
CREATE TABLE `pb_companies` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '0',
  `cache_spacename` varchar(255) NOT NULL default '',
  `cache_membergroupid` smallint(3) NOT NULL default '0',
  `cache_credits` smallint(6) NOT NULL default '0',
  `topleveldomain` varchar(255) NOT NULL default '',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `area_id1` char(6) NOT NULL default '0',
  `area_id2` char(6) NOT NULL default '0',
  `area_id3` char(6) NOT NULL default '0',
  `type_id` tinyint(2) NOT NULL default '0',
  `name` char(255) NOT NULL default '',
  `description` text,
  `english_name` char(100) NOT NULL default '',
  `keywords` varchar(50) NOT NULL default '',
  `boss_name` varchar(25) NOT NULL default '',
  `manage_type` varchar(25) NOT NULL default '',
  `year_annual` tinyint(2) NOT NULL default '0',
  `property` tinyint(1) NOT NULL default '0',
  `configs` text,
  `bank_from` varchar(50) NOT NULL default '',
  `bank_account` varchar(50) NOT NULL default '',
  `main_prod` varchar(100) NOT NULL default '',
  `employee_amount` varchar(25) NOT NULL default '',
  `found_date` char(10) NOT NULL default '0',
  `reg_fund` tinyint(2) NOT NULL default '0',
  `reg_address` varchar(200) NOT NULL default '',
  `address` varchar(200) NOT NULL default '',
  `zipcode` varchar(15) NOT NULL default '',
  `main_brand` varchar(100) NOT NULL default '',
  `main_market` varchar(200) NOT NULL default '',
  `main_biz_place` varchar(50) NOT NULL default '',
  `main_customer` varchar(200) NOT NULL default '',
  `link_man` varchar(25) NOT NULL default '',
  `link_man_gender` tinyint(1) NOT NULL default '0',
  `position` tinyint(1) NOT NULL default '0',
  `tel` varchar(25) NOT NULL default '',
  `fax` varchar(25) NOT NULL default '',
  `mobile` varchar(25) NOT NULL default '',
  `email` varchar(100) NOT NULL default '',
  `site_url` varchar(100) NOT NULL default '',
  `picture` varchar(50) NOT NULL default '',
  `status` tinyint(1) NOT NULL default '0',
  `first_letter` char(2) NOT NULL default '',
  `if_commend` tinyint(1) NOT NULL default '0',
  `clicked` int(5) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `member_id` (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_companyfields`
-- 

DROP TABLE IF EXISTS `pb_companyfields`;
CREATE TABLE `pb_companyfields` (
  `company_id` int(10) NOT NULL default '0',
  `map_longitude` varchar(25) NOT NULL default '',
  `map_latitude` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`company_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_companynewses`
-- 

DROP TABLE IF EXISTS `pb_companynewses`;
CREATE TABLE `pb_companynewses` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '-1',
  `company_id` int(10) NOT NULL default '-1',
  `type_id` smallint(3) NOT NULL default '0',
  `title` varchar(100) NOT NULL default '',
  `content` text,
  `picture` varchar(100) NOT NULL default '',
  `status` tinyint(1) NOT NULL default '0',
  `clicked` int(5) NOT NULL default '1',
  `created` int(11) NOT NULL default '0',
  `modified` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_expoes`
-- 

DROP TABLE IF EXISTS `pb_expoes`;
CREATE TABLE `pb_expoes` (
  `id` int(10) NOT NULL auto_increment,
  `expotype_id` smallint(3) NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `description` text,
  `begin_time` int(10) NOT NULL default '0',
  `end_time` int(10) NOT NULL default '0',
  `industry_ids` varchar(100) NOT NULL default '0',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `address` varchar(100) NOT NULL default '',
  `stadium_name` varchar(100) NOT NULL default '',
  `refresh_method` varchar(100) NOT NULL default '',
  `scope` varchar(100) NOT NULL default '',
  `hosts` varchar(255) NOT NULL default '',
  `organisers` varchar(255) NOT NULL default '',
  `co_organisers` varchar(255) NOT NULL default '',
  `sponsors` varchar(255) NOT NULL default '',
  `contacts` text,
  `important_notice` text,
  `picture` varchar(100) NOT NULL default '',
  `if_commend` tinyint(1) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '0',
  `hits` smallint(6) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_expomembers`
-- 

DROP TABLE IF EXISTS `pb_expomembers`;
CREATE TABLE `pb_expomembers` (
  `id` smallint(6) NOT NULL auto_increment,
  `expo_id` smallint(6) NOT NULL default '0',
  `member_id` int(10) NOT NULL,
  `company_id` int(10) NOT NULL,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `expo_id` (`expo_id`,`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_expostadiums`
-- 

DROP TABLE IF EXISTS `pb_expostadiums`;
CREATE TABLE `pb_expostadiums` (
  `id` smallint(6) NOT NULL auto_increment,
  `sa` varchar(100) default '',
  `country_id` smallint(6) default '0',
  `province_id` smallint(6) default '0',
  `city_id` smallint(6) default '0',
  `sb` varchar(200) default '',
  `sc` varchar(150) default '',
  `sd` varchar(150) default '',
  `se` varchar(150) default '',
  `sf` varchar(150) default '',
  `sg` text,
  `sh` smallint(6) default '0',
  `created` int(10) default NULL,
  `modified` int(10) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_expotypes`
-- 

DROP TABLE IF EXISTS `pb_expotypes`;
CREATE TABLE `pb_expotypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_favorites`
-- 

DROP TABLE IF EXISTS `pb_favorites`;
CREATE TABLE `pb_favorites` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '-1',
  `target_id` int(10) NOT NULL,
  `type_id` tinyint(1) NOT NULL,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `member_id` (`member_id`,`target_id`,`type_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_formattributes`
-- 

DROP TABLE IF EXISTS `pb_formattributes`;
CREATE TABLE `pb_formattributes` (
  `id` int(10) NOT NULL auto_increment,
  `type_id` tinyint(1) NOT NULL,
  `form_id` smallint(3) NOT NULL default '0',
  `formitem_id` smallint(3) NOT NULL default '0',
  `primary_id` int(10) NOT NULL,
  `attribute` text,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_formitems`
-- 

DROP TABLE IF EXISTS `pb_formitems`;
CREATE TABLE `pb_formitems` (
  `id` smallint(3) NOT NULL auto_increment,
  `form_id` smallint(3) NOT NULL default '0',
  `title` varchar(100) NOT NULL default '',
  `description` text,
  `identifier` varchar(50) NOT NULL default '',
  `type` enum('checkbox','select','radio','calendar','url','image','textarea','email','number','text') NOT NULL default 'text',
  `rules` text,
  `display_order` tinyint(1) NOT NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_forms`
-- 

DROP TABLE IF EXISTS `pb_forms`;
CREATE TABLE `pb_forms` (
  `id` smallint(3) NOT NULL auto_increment,
  `type_id` tinyint(1) NOT NULL,
  `name` varchar(100) NOT NULL default '',
  `items` text,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_friendlinks`
-- 

DROP TABLE IF EXISTS `pb_friendlinks`;
CREATE TABLE `pb_friendlinks` (
  `id` smallint(3) NOT NULL auto_increment,
  `friendlinktype_id` tinyint(1) NOT NULL default '0',
  `industry_id` smallint(6) NOT NULL default '0',
  `area_id` smallint(6) NOT NULL default '0',
  `title` varchar(50) NOT NULL default '',
  `logo` varchar(100) NOT NULL default '',
  `url` varchar(50) NOT NULL default '',
  `priority` smallint(3) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '1',
  `description` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_friendlinktypes`
-- 

DROP TABLE IF EXISTS `pb_friendlinktypes`;
CREATE TABLE `pb_friendlinktypes` (
  `id` tinyint(1) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_goods`
-- 

DROP TABLE IF EXISTS `pb_goods`;
CREATE TABLE `pb_goods` (
  `id` smallint(6) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `description` text,
  `price` float(9,2) NOT NULL default '0',
  `closed` tinyint(1) NOT NULL default '1',
  `picture` varchar(100) NOT NULL default '',
  `if_commend` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_helps`
-- 

DROP TABLE IF EXISTS `pb_helps`;
CREATE TABLE `pb_helps` (
  `id` smallint(6) NOT NULL auto_increment,
  `helptype_id` smallint(3) NOT NULL default '0',
  `title` varchar(100) NOT NULL default '',
  `content` text,
  `highlight` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_helptypes`
-- 

DROP TABLE IF EXISTS `pb_helptypes`;
CREATE TABLE `pb_helptypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `title` varchar(100) NOT NULL default '',
  `description` varchar(100) NOT NULL default '',
  `parent_id` smallint(3) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '0',
  `display_order` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_htmlcaches`
-- 

DROP TABLE IF EXISTS `pb_htmlcaches`;
CREATE TABLE `pb_htmlcaches` (
  `id` int(5) NOT NULL auto_increment,
  `page_name` varchar(100) NOT NULL default '',
  `last_cached_time` int(10) NOT NULL default '0',
  `cache_cycle_time` int(10) NOT NULL default '86400',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_industries`
-- 

DROP TABLE IF EXISTS `pb_industries`;
CREATE TABLE `pb_industries` (
  `id` smallint(6) NOT NULL auto_increment,
  `attachment_id` int(9) NOT NULL default '0',
  `industrytype_id` smallint(3) NOT NULL default '0',
  `child_ids` text,
  `name` varchar(255) NOT NULL default '',
  `url` varchar(255) NOT NULL default '',
  `alias_name` varchar(255) NOT NULL default '',
  `highlight` tinyint(1) NOT NULL default '0',
  `parent_id` smallint(6) NOT NULL default '0',
  `top_parentid` smallint(6) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `display_order` tinyint(1) NOT NULL default '0',
  `description` text,
  `available` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_industryfields`
-- 

DROP TABLE IF EXISTS `pb_industryfields`;
CREATE TABLE `pb_industryfields` (
  `industry_id` smallint(6) NOT NULL,
  `type_id` enum('offer','company','product','fair','market','news','hr') NOT NULL default 'offer',
  `keyword_ids` text,
  PRIMARY KEY  (`industry_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_industrymodels`
-- 

DROP TABLE IF EXISTS `pb_industrymodels`;
CREATE TABLE `pb_industrymodels` (
  `industry_id` smallint(6) NOT NULL,
  `model_id` enum('offers','products','companies') NOT NULL default 'offers',
  PRIMARY KEY  (`industry_id`,`model_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_industrytypes`
-- 

DROP TABLE IF EXISTS `pb_industrytypes`;
CREATE TABLE `pb_industrytypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_inqueries`
-- 

DROP TABLE IF EXISTS `pb_inqueries`;
CREATE TABLE `pb_inqueries` (
  `id` int(10) NOT NULL auto_increment,
  `to_member_id` int(10) default NULL,
  `to_company_id` int(10) default NULL,
  `title` varchar(50) NOT NULL default '',
  `content` text,
  `send_achive` tinyint(1) default NULL,
  `know_more` varchar(50) NOT NULL default '',
  `exp_quantity` varchar(15) NOT NULL default '',
  `exp_price` float(9,2) NOT NULL default '0.00',
  `contacts` text,
  `user_ip` varchar(11) default '',
  `created` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_banned`
-- 

DROP TABLE IF EXISTS `pb_banned`;
CREATE TABLE `pb_banned` (
  `id` smallint(6) NOT NULL auto_increment,
  `ip1` char(3) NOT NULL default '',
  `ip2` char(3) NOT NULL default '',
  `ip3` char(3) NOT NULL default '',
  `ip4` char(3) NOT NULL default '',
  `expiration` int(10) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `ip1` (`ip1`,`ip2`,`ip3`,`ip4`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_jobs`
-- 

DROP TABLE IF EXISTS `pb_jobs`;
CREATE TABLE `pb_jobs` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '-1',
  `company_id` int(10) NOT NULL default '-1',
  `cache_spacename` varchar(25) NOT NULL default '',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `name` varchar(150) NOT NULL default '',
  `work_station` varchar(50) NOT NULL default '',
  `content` text,
  `require_gender_id` tinyint(1) NOT NULL default '0',
  `peoples` varchar(5) NOT NULL default '',
  `require_education_id` tinyint(1) NOT NULL default '0',
  `require_age` varchar(10) NOT NULL default '',
  `salary_id` tinyint(1) NOT NULL default '0',
  `worktype_id` tinyint(1) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '0',
  `clicked` int(5) NOT NULL default '1',
  `expire_time` int(10) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_jobtypes`
-- 

DROP TABLE IF EXISTS `pb_jobtypes`;
CREATE TABLE `pb_jobtypes` (
  `id` smallint(6) NOT NULL auto_increment,
  `parent_id` smallint(6) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_keywords`
-- 

DROP TABLE IF EXISTS `pb_keywords`;
CREATE TABLE `pb_keywords` (
  `id` int(5) NOT NULL auto_increment,
  `title` varchar(25) NOT NULL default '',
  `target_id` int(10) NOT NULL default '0',
  `target_position` tinyint(1) NOT NULL default '0',
  `type_name` enum('trades','companies','newses','products') NOT NULL default 'trades',
  `hits` smallint(6) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `title` (`title`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_logs`
-- 

DROP TABLE IF EXISTS `pb_logs`;
CREATE TABLE `pb_logs` (
  `id` int(10) NOT NULL auto_increment,
  `handle_type` enum('error','info','warning') NOT NULL default 'info',
  `source_module` varchar(50) NOT NULL default '',
  `description` text,
  `ip_address` int(10) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_markets`
-- 

DROP TABLE IF EXISTS `pb_markets`;
CREATE TABLE `pb_markets` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `content` text,
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `picture` varchar(50) NOT NULL default '',
  `ip_address` int(10) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '0',
  `clicked` smallint(6) NOT NULL default '1',
  `if_commend` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_memberfields`
-- 

DROP TABLE IF EXISTS `pb_memberfields`;
CREATE TABLE `pb_memberfields` (
  `member_id` int(10) NOT NULL default '0',
  `today_logins` smallint(6) NOT NULL default '0',
  `total_logins` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `first_name` varchar(25) NOT NULL default '',
  `last_name` varchar(25) NOT NULL default '',
  `gender` tinyint(1) NOT NULL default '0',
  `tel` varchar(25) NOT NULL default '',
  `fax` varchar(25) NOT NULL default '',
  `mobile` varchar(25) NOT NULL default '',
  `qq` varchar(12) NOT NULL default '',
  `msn` varchar(50) NOT NULL default '',
  `icq` varchar(12) NOT NULL default '',
  `yahoo` varchar(50) NOT NULL default '',
  `skype` varchar(50) NOT NULL default '',
  `address` varchar(50) NOT NULL default '',
  `zipcode` varchar(16) NOT NULL default '',
  `site_url` varchar(100) NOT NULL default '',
  `question` varchar(50) NOT NULL default '',
  `answer` varchar(50) NOT NULL default '',
  `reg_ip` varchar(25) NOT NULL default '0',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_membergroups`
-- 

DROP TABLE IF EXISTS `pb_membergroups`;
CREATE TABLE `pb_membergroups` (
  `id` smallint(3) NOT NULL auto_increment,
  `membertype_id` tinyint(1) NOT NULL default '-1',
  `name` varchar(50) NOT NULL default '',
  `description` text,
  `type` enum('define','special','system') NOT NULL default 'define',
  `system` enum('private','public') NOT NULL default 'private',
  `picture` varchar(50) NOT NULL default 'default.gif',
  `point_max` smallint(6) NOT NULL default '0',
  `point_min` smallint(6) NOT NULL default '0',
  `max_offer` smallint(3) NOT NULL default '0',
  `max_product` smallint(3) NOT NULL default '0',
  `max_job` smallint(3) NOT NULL default '0',
  `max_companynews` smallint(3) NOT NULL default '0',
  `max_producttype` smallint(3) NOT NULL default '3',
  `max_album` smallint(3) NOT NULL default '0',
  `max_attach_size` smallint(6) NOT NULL default '0',
  `max_size_perday` smallint(6) NOT NULL default '0',
  `max_favorite` smallint(3) NOT NULL default '0',
  `is_default` tinyint(1) NOT NULL default '0',
  `allow_offer` tinyint(1) NOT NULL default '0',
  `allow_market` tinyint(1) NOT NULL default '0',
  `allow_company` tinyint(1) NOT NULL default '0',
  `allow_product` tinyint(1) NOT NULL default '0',
  `allow_job` tinyint(1) NOT NULL default '0',
  `allow_companynews` tinyint(1) NOT NULL default '1',
  `allow_album` tinyint(1) NOT NULL default '0',
  `allow_space` tinyint(1) NOT NULL default '1',
  `default_live_time` tinyint(1) NOT NULL default '1',
  `after_live_time` tinyint(1) NOT NULL default '1',
  `exempt` tinyint(1) unsigned zerofill NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_members`
-- 

DROP TABLE IF EXISTS `pb_members`;
CREATE TABLE `pb_members` (
  `id` int(10) NOT NULL auto_increment,
  `space_name` varchar(255) NOT NULL default '',
  `templet_id` smallint(3) NOT NULL default '0',
  `username` varchar(25) NOT NULL default '',
  `userpass` varchar(50) NOT NULL default '',
  `email` varchar(100) NOT NULL default '',
  `points` smallint(6) NOT NULL default '0',
  `credits` smallint(6) NOT NULL default '0',
  `balance_amount` float(7,2) NOT NULL default '0.00',
  `trusttype_ids` varchar(25) NOT NULL default '',
  `status` enum('3','2','1','0') NOT NULL default '0',
  `photo` varchar(100) NOT NULL default '',
  `membertype_id` smallint(3) NOT NULL default '0',
  `membergroup_id` smallint(3) NOT NULL default '0',
  `last_login` varchar(11) NOT NULL default '0',
  `last_ip` varchar(25) NOT NULL default '0',
  `service_start_date` varchar(11) NOT NULL default '0',
  `service_end_date` varchar(11) NOT NULL default '0',
  `office_redirect` smallint(6) NOT NULL default '0',
  `created` varchar(10) NOT NULL default '0',
  `modified` varchar(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_membertypes`
-- 

DROP TABLE IF EXISTS `pb_membertypes`;
CREATE TABLE `pb_membertypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `default_membergroup_id` smallint(3) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `description` text,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_messages`
-- 

DROP TABLE IF EXISTS `pb_messages`;
CREATE TABLE `pb_messages` (
  `id` int(10) NOT NULL auto_increment,
  `type` enum('system','user','inquery') NOT NULL default 'user',
  `from_member_id` int(10) NOT NULL default '-1',
  `cache_from_username` varchar(25) NOT NULL default '',
  `to_member_id` int(10) NOT NULL default '-1',
  `cache_to_username` varchar(25) NOT NULL default '',
  `title` varchar(255) NOT NULL default '',
  `content` text,
  `status` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_navs`
-- 

DROP TABLE IF EXISTS `pb_navs`;
CREATE TABLE `pb_navs` (
  `id` smallint(3) NOT NULL auto_increment,
  `parent_id` smallint(3) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `description` varchar(255) NOT NULL default '',
  `url` varchar(255) NOT NULL default '',
  `target` enum('_blank','_self') NOT NULL default '_self',
  `status` tinyint(1) NOT NULL default '1',
  `display_order` smallint(3) NOT NULL default '0',
  `highlight` tinyint(1) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '0',
  `class_name` varchar(25) NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_newscomments`
-- 

DROP TABLE IF EXISTS `pb_newscomments`;
CREATE TABLE `pb_newscomments` (
  `id` int(10) NOT NULL auto_increment,
  `news_id` int(10) NOT NULL default '0',
  `member_id` int(10) NOT NULL default '-1',
  `cache_username` varchar(25) NOT NULL default '',
  `message` text,
  `ip_address` char(15) NOT NULL default '',
  `invisible` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `date_line` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_newses`
-- 

DROP TABLE IF EXISTS `pb_newses`;
CREATE TABLE `pb_newses` (
  `id` int(10) NOT NULL auto_increment,
  `type_id` smallint(3) NOT NULL default '0',
  `type` tinyint(1) NOT NULL default '0',
  `industry_id` smallint(3) NOT NULL default '0',
  `area_id` smallint(3) NOT NULL default '0',
  `title` varchar(255) NOT NULL default '',
  `content` text,
  `source` varchar(25) NOT NULL default '',
  `picture` varchar(50) NOT NULL default '',
  `if_focus` tinyint(1) NOT NULL default '0',
  `if_commend` tinyint(1) NOT NULL default '0',
  `highlight` tinyint(1) NOT NULL default '0',
  `clicked` int(10) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '1',
  `flag` tinyint(1) NOT NULL default '0',
  `require_membertype` varchar(15) NOT NULL default '0',
  `tag_ids` varchar(255) default '',
  `created` int(10) NOT NULL default '0',
  `create_time` datetime NOT NULL default '0000-00-00 00:00:00',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_newstypes`
-- 

DROP TABLE IF EXISTS `pb_newstypes`;
CREATE TABLE `pb_newstypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  `level_id` tinyint(1) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '1',
  `parent_id` smallint(3) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_ordergoods`
-- 

DROP TABLE IF EXISTS `pb_ordergoods`;
CREATE TABLE `pb_ordergoods` (
  `goods_id` smallint(6) NOT NULL default '0',
  `order_id` smallint(6) unsigned zerofill NOT NULL default '000000',
  `amount` smallint(3) NOT NULL default '1',
  PRIMARY KEY  (`goods_id`,`order_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_orders`
-- 

DROP TABLE IF EXISTS `pb_orders`;
CREATE TABLE `pb_orders` (
  `id` smallint(6) unsigned zerofill NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '-1',
  `anonymous` tinyint(1) NOT NULL default '0',
  `cache_username` varchar(25) NOT NULL default '',
  `total_price` float(9,2) NOT NULL default '0.00',
  `content` text,
  `status` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_passports`
-- 

DROP TABLE IF EXISTS `pb_passports`;
CREATE TABLE `pb_passports` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  `title` varchar(25) NOT NULL default '',
  `description` text,
  `url` varchar(25) NOT NULL default '',
  `config` text,
  `available` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_payments`
-- 

DROP TABLE IF EXISTS `pb_payments`;
CREATE TABLE `pb_payments` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  `title` varchar(25) NOT NULL default '',
  `description` text,
  `config` text,
  `available` tinyint(1) NOT NULL default '1',
  `if_online_support` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_personals`
-- 

DROP TABLE IF EXISTS `pb_personals`;
CREATE TABLE `pb_personals` (
  `member_id` int(10) NOT NULL,
  `resume_status` tinyint(1) NOT NULL default '0',
  `max_education` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_plugins`
-- 

DROP TABLE IF EXISTS `pb_plugins`;
CREATE TABLE `pb_plugins` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  `title` varchar(25) NOT NULL default '',
  `description` text,
  `copyright` varchar(25) NOT NULL default '',
  `version` varchar(15) NOT NULL default '',
  `pluginvar` text,
  `available` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_pointlogs`
-- 

DROP TABLE IF EXISTS `pb_pointlogs`;
CREATE TABLE `pb_pointlogs` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `action_name` varchar(25) NOT NULL default '',
  `points` smallint(3) NOT NULL default '0',
  `description` text,
  `ip_address` varchar(15) NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_products`
-- 

DROP TABLE IF EXISTS `pb_products`;
CREATE TABLE `pb_products` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `company_id` int(10) NOT NULL default '0',
  `cache_companyname` varchar(100) NOT NULL default '',
  `sort_id` tinyint(1) NOT NULL default '1',
  `brand_id` smallint(6) NOT NULL default '0',
  `category_id` smallint(6) NOT NULL default '0',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `name` varchar(255) NOT NULL default '',
  `price` float(9,2) NOT NULL default '0.00',
  `sn` varchar(20) NOT NULL default '',
  `spec` varchar(20) NOT NULL default '',
  `produce_area` varchar(50) NOT NULL default '',
  `packing_content` varchar(100) NOT NULL default '',
  `picture` varchar(50) NOT NULL default '',
  `content` text,
  `producttype_id` smallint(6) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '0',
  `state` tinyint(1) NOT NULL default '1',
  `ifnew` tinyint(1) NOT NULL default '0',
  `ifcommend` tinyint(1) NOT NULL default '0',
  `priority` tinyint(1) NOT NULL default '0',
  `tag_ids` varchar(255) default '',
  `clicked` smallint(6) NOT NULL default '1',
  `formattribute_ids` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_producttypes`
-- 

DROP TABLE IF EXISTS `pb_producttypes`;
CREATE TABLE `pb_producttypes` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `company_id` int(10) NOT NULL,
  `name` varchar(25) NOT NULL default '',
  `level` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_roleadminers`
-- 

DROP TABLE IF EXISTS `pb_roleadminers`;
CREATE TABLE `pb_roleadminers` (
  `id` int(5) NOT NULL auto_increment,
  `adminrole_id` int(2) default NULL,
  `adminer_id` int(2) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_roleprivileges`
-- 

DROP TABLE IF EXISTS `pb_roleprivileges`;
CREATE TABLE `pb_roleprivileges` (
  `id` int(5) NOT NULL auto_increment,
  `adminrole_id` int(2) default NULL,
  `adminprivilege_id` int(2) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_segmentcaches`
-- 

DROP TABLE IF EXISTS `pb_segmentcaches`;
CREATE TABLE `pb_segmentcaches` (
  `id` int(10) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL default '',
  `data` text,
  `display_order` smallint(3) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_segmentdicts`
-- 

DROP TABLE IF EXISTS `pb_segmentdicts`;
CREATE TABLE `pb_segmentdicts` (
  `id` int(10) NOT NULL auto_increment,
  `word` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_services`
-- 

DROP TABLE IF EXISTS `pb_services`;
CREATE TABLE `pb_services` (
  `id` smallint(6) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `title` varchar(25) NOT NULL default '',
  `content` text,
  `nick_name` varchar(25) default '',
  `email` varchar(25) NOT NULL default '',
  `user_ip` varchar(11) NOT NULL default '',
  `type_id` tinyint(1) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  `revert_content` text,
  `revert_date` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_sessions`
-- 

DROP TABLE IF EXISTS `pb_sessions`;
CREATE TABLE `pb_sessions` (
  `sesskey` char(32) NOT NULL default '',
  `expiry` int(10) NOT NULL default '0',
  `expireref` char(64) NOT NULL default '',
  `data` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  KEY `sess2_expiry` (`expiry`),
  KEY `sess2_expireref` (`expireref`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_settings`
-- 

DROP TABLE IF EXISTS `pb_settings`;
CREATE TABLE `pb_settings` (
  `id` smallint(3) NOT NULL auto_increment,
  `type_id` tinyint(1) NOT NULL default '0',
  `variable` varchar(150) NOT NULL default '',
  `valued` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `variable` (`variable`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_spacelinks`
-- 

DROP TABLE IF EXISTS `pb_spacelinks`;
CREATE TABLE `pb_spacelinks` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `company_id` int(10) NOT NULL,
  `display_order` smallint(3) NOT NULL default '0',
  `title` varchar(100) NOT NULL default '',
  `url` varchar(255) NOT NULL default '',
  `is_outlink` tinyint(1) NOT NULL default '0',
  `description` varchar(100) NOT NULL default '',
  `logo` varchar(255) NOT NULL default '',
  `highlight` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_spreads`
-- 

DROP TABLE IF EXISTS `pb_spreads`;
CREATE TABLE `pb_spreads` (
  `keyword_id` int(10) NOT NULL,
  `target_id` int(10) NOT NULL,
  `type_name` enum('trades','companies','newses','products') NOT NULL default 'trades',
  `expiration` int(10) NOT NULL default '0',
  `display_order` tinyint(1) NOT NULL,
  PRIMARY KEY  (`keyword_id`),
  KEY `spread` (`keyword_id`,`target_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_stats`
-- 

DROP TABLE IF EXISTS `pb_stats`;
CREATE TABLE `pb_stats` (
  `id` smallint(6) NOT NULL auto_increment,
  `sa` varchar(25) default '',
  `sb` varchar(50) default '',
  `description` varchar(50) NOT NULL default '',
  `sc` int(10) default NULL,
  `sd` int(10) default NULL,
  `se` smallint(6) default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_standards`
-- 

DROP TABLE IF EXISTS `pb_standards`;
CREATE TABLE `pb_standards` (
  `id` smallint(6) NOT NULL auto_increment,
  `attachment_id` smallint(6) NOT NULL default '0',
  `type_id` smallint(6) NOT NULL default '0',
  `title` varchar(255) NOT NULL default '',
  `source` varchar(255) NOT NULL,
  `digest` varchar(255) NOT NULL default '' ,
  `content` text NOT NULL,
  `publish_time` int(10) NOT NULL default '0',
  `force_time` int(10) NOT NULL default '0',
  `clicked` smallint(6) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_standardtypes`
-- 

DROP TABLE IF EXISTS `pb_standardtypes`;
CREATE TABLE `pb_standardtypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_tags`
-- 

DROP TABLE IF EXISTS `pb_tags`;
CREATE TABLE `pb_tags` (
  `id` int(10) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL,
  `name` varchar(255) NOT NULL default '',
  `numbers` smallint(6) NOT NULL default '0',
  `closed` tinyint(1) NOT NULL default '0',
  `flag` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `title` (`name`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_templets`
-- 

DROP TABLE IF EXISTS `pb_templets`;
CREATE TABLE `pb_templets` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  `title` varchar(25) NOT NULL default '',
  `directory` varchar(100) NOT NULL default '',
  `type` enum('system','user') NOT NULL default 'system',
  `author` varchar(100) NOT NULL default '',
  `style` varchar(255) NOT NULL default '',
  `description` text,
  `is_default` tinyint(1) NOT NULL default '0',
  `require_membertype` varchar(100) NOT NULL default '0',
  `require_membergroups` varchar(100) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_topicnews`
-- 

DROP TABLE IF EXISTS `pb_topicnews`;
CREATE TABLE `pb_topicnews` (
  `topic_id` smallint(6) NOT NULL default '0',
  `news_id` smallint(6) NOT NULL default '0'
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_topics`
-- 

DROP TABLE IF EXISTS `pb_topics`;
CREATE TABLE `pb_topics` (
  `id` smallint(6) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL default '',
  `picture` varchar(255) NOT NULL default '',
  `description` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_tradefields`
-- 

DROP TABLE IF EXISTS `pb_tradefields`;
CREATE TABLE `pb_tradefields` (
  `trade_id` int(10) NOT NULL default '0',
  `member_id` int(10) NOT NULL default '0',
  `link_man` varchar(100) NOT NULL default '',
  `address` varchar(100) NOT NULL default '',
  `company_name` varchar(100) NOT NULL default '',
  `email` varchar(100) NOT NULL default '',
  `prim_tel` tinyint(1) NOT NULL default '0',
  `prim_telnumber` varchar(25) NOT NULL default '',
  `prim_im` tinyint(1) NOT NULL default '0',
  `prim_imaccount` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`trade_id`),
  UNIQUE KEY `trade_id` (`trade_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_trades`
-- 

DROP TABLE IF EXISTS `pb_trades`;
CREATE TABLE `pb_trades` (
  `id` int(10) NOT NULL auto_increment,
  `type_id` enum('8','7','6','5','4','3','2','1') NOT NULL default '1',
  `industry_id1` smallint(6) NOT NULL default '0',
  `industry_id2` smallint(6) NOT NULL default '0',
  `industry_id3` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `member_id` int(10) NOT NULL default '0',
  `company_id` int(5) NOT NULL default '0',
  `cache_username` varchar(25) NOT NULL default '',
  `cache_companyname` varchar(100) NOT NULL default '',
  `cache_contacts` varchar(255) NOT NULL default '',
  `title` varchar(100) NOT NULL default '',
  `content` text,
  `price` float(9,2) NOT NULL default '0.00',
  `measuring_unit` varchar(15) NOT NULL default '0',
  `monetary_unit` varchar(15) NOT NULL default '0',
  `packing` varchar(150) NOT NULL default '',
  `quantity` varchar(25) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  `display_expiration` int(10) NOT NULL default '0',
  `spec` varchar(200) NOT NULL default '',
  `sn` varchar(25) NOT NULL default '',
  `picture` varchar(50) NOT NULL default '',
  `picture_remote` varchar(50) NOT NULL default '',
  `status` tinyint(2) NOT NULL default '0',
  `submit_time` int(10) NOT NULL default '0',
  `expire_time` int(10) NOT NULL default '0',
  `expire_days` int(3) NOT NULL default '0',
  `if_commend` tinyint(1) NOT NULL default '0',
  `if_urgent` enum('0','1') NOT NULL default '0',
  `if_locked` enum('0','1') NOT NULL default '0',
  `require_point` smallint(6) NOT NULL default '0',
  `require_membertype` smallint(6) NOT NULL default '0',
  `require_freedate` int(10) NOT NULL default '0',
  `ip_addr` varchar(15) NOT NULL default '',
  `clicked` int(10) NOT NULL default '1',
  `tag_ids` varchar(255) default '',
  `formattribute_ids` text,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_tradetypes`
-- 

DROP TABLE IF EXISTS `pb_tradetypes`;
CREATE TABLE `pb_tradetypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `parent_id` smallint(3) NOT NULL default '0',
  `name` varchar(25) NOT NULL default '',
  `level` tinyint(1) NOT NULL default '1',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_trustlogs`
-- 

DROP TABLE IF EXISTS `pb_trustlogs`;
CREATE TABLE `pb_trustlogs` (
  `member_id` int(10) NOT NULL auto_increment,
  `trusttype_id` smallint(3) NOT NULL default '0',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_trusttypes`
-- 

DROP TABLE IF EXISTS `pb_trusttypes`;
CREATE TABLE `pb_trusttypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `description` text,
  `image` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  `status` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_typemodels`
-- 

DROP TABLE IF EXISTS `pb_typemodels`;
CREATE TABLE `pb_typemodels` (
  `id` smallint(3) NOT NULL auto_increment,
  `title` varchar(50) NOT NULL default '',
  `type_name` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_typeoptions`
-- 

DROP TABLE IF EXISTS `pb_typeoptions`;
CREATE TABLE `pb_typeoptions` (
  `id` smallint(3) NOT NULL auto_increment,
  `typemodel_id` smallint(3) NOT NULL default '0',
  `option_value` varchar(50) NOT NULL default '',
  `option_label` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_userpages`
-- 

DROP TABLE IF EXISTS `pb_userpages`;
CREATE TABLE `pb_userpages` (
  `id` int(5) NOT NULL auto_increment,
  `templet_name` varchar(50) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL default '',
  `title` varchar(50) NOT NULL default '',
  `digest` varchar(50) NOT NULL default '',
  `content` text,
  `url` varchar(100) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_visitlogs`
-- 

DROP TABLE IF EXISTS `pb_visitlogs`;
CREATE TABLE `pb_visitlogs` (
  `id` smallint(6) NOT NULL auto_increment,
  `salt` varchar(32) NOT NULL default '',
  `date_line` varchar(15) NOT NULL default '',
  `type_name` varchar(15) NOT NULL default '',
  PRIMARY KEY  (`id`),
  KEY `salt` (`salt`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_words`
-- 

DROP TABLE IF EXISTS `pb_words`;
CREATE TABLE `pb_words` (
  `id` smallint(6) NOT NULL auto_increment,
  `title` varchar(50) NOT NULL default '',
  `replace_to` varchar(50) NOT NULL default '',
  `expiration` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `word` (`title`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_companytypes`
-- 

DROP TABLE IF EXISTS `pb_companytypes`;
CREATE TABLE `pb_companytypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `display_order` tinyint(1) NOT NULL,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_attachmenttypes`
-- 

DROP TABLE IF EXISTS `pb_attachmenttypes`;
CREATE TABLE `pb_attachmenttypes` (
  `id` tinyint(1) NOT NULL auto_increment,
  `name` varchar(25) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_dicts`
-- 

DROP TABLE IF EXISTS `pb_dicts`;
CREATE TABLE `pb_dicts` (
  `id` int(10) NOT NULL auto_increment,
  `dicttype_id` smallint(6) NOT NULL default '0',
  `extend_dicttypeid` varchar(25) NOT NULL default '',
  `word` varchar(255) NOT NULL default '',
  `word_name` varchar(255) NOT NULL default '',
  `digest` varchar(255) NOT NULL default '',
  `content` text,
  `picture` varchar(255) NOT NULL default '',
  `refer` tinytext,
  `hits` int(10) NOT NULL default '1',
  `closed` tinyint(1) NOT NULL default '0',
  `if_commend` tinyint(1) NOT NULL default '0',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_dicttypes`
-- 

DROP TABLE IF EXISTS `pb_dicttypes`;
CREATE TABLE `pb_dicttypes` (
  `id` smallint(6) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `parent_id` smallint(6) NOT NULL default '0',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_membercaches`
-- 

DROP TABLE IF EXISTS `pb_membercaches`;
CREATE TABLE `pb_membercaches` (
  `member_id` int(10) NOT NULL default '-1',
  `data1` text NOT NULL,
  `data2` text NOT NULL,
  `expiration` int(10) NOT NULL default '0',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_spacecaches`
-- 

DROP TABLE IF EXISTS `pb_spacecaches`;
CREATE TABLE `pb_spacecaches` (
  `cache_spacename` varchar(255) NOT NULL default '',
  `company_id` int(10) NOT NULL default '-1',
  `data1` text NOT NULL,
  `data2` text NOT NULL,
  `expiration` int(10) NOT NULL default '0',
  PRIMARY KEY  (`company_id`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Table structure `pb_albumtypes`
-- 

DROP TABLE IF EXISTS `pb_albumtypes`;
CREATE TABLE `pb_albumtypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_brands`
-- 

DROP TABLE IF EXISTS `pb_brands`;
CREATE TABLE `pb_brands` (
  `id` smallint(6) NOT NULL auto_increment,
  `member_id` int(10) NOT NULL default '-1',
  `company_id` int(10) NOT NULL default '-1',
  `type_id` smallint(3) NOT NULL default '0',
  `if_commend` tinyint(1) NOT NULL default '0',
  `name` varchar(100) NOT NULL default '',
  `alias_name` varchar(100) NOT NULL default '',
  `picture` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `hits` smallint(6) NOT NULL default '0',
  `ranks` smallint(3) NOT NULL default '0',
  `letter` varchar(2) NOT NULL default '',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_brandtypes`
-- 

DROP TABLE IF EXISTS `pb_brandtypes`;
CREATE TABLE `pb_brandtypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `parent_id` smallint(3) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `name` varchar(100) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_companynewstypes`
-- 

DROP TABLE IF EXISTS `pb_companynewstypes`;
CREATE TABLE `pb_companynewstypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_feeds`
-- 

DROP TABLE IF EXISTS `pb_feeds`;
CREATE TABLE `pb_feeds` (
  `id` int(10) NOT NULL auto_increment,
  `type_id` tinyint(1) NOT NULL default '0',
  `type` varchar(100) NOT NULL default '',
  `member_id` int(10) NOT NULL default '0',
  `username` varchar(100) NOT NULL default '',
  `data` text NOT NULL,
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_markettypes`
-- 

DROP TABLE IF EXISTS `pb_markettypes`;
CREATE TABLE `pb_markettypes` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_productcategories`
-- 

DROP TABLE IF EXISTS `pb_productcategories`;
CREATE TABLE `pb_productcategories` (
  `id` smallint(6) NOT NULL auto_increment,
  `parent_id` smallint(6) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_productprices`
-- 

DROP TABLE IF EXISTS `pb_productprices`;
CREATE TABLE `pb_productprices` (
  `id` int(10) NOT NULL auto_increment,
  `type_id` tinyint(1) NOT NULL default '1',
  `product_id` int(10) NOT NULL default '-1',
  `brand_id` smallint(6) NOT NULL default '-1',
  `member_id` int(10) NOT NULL default '-1',
  `company_id` int(10) NOT NULL default '-1',
  `area_id` smallint(6) NOT NULL default '0',
  `price_trends` tinyint(1) NOT NULL default '0',
  `category_id` smallint(6) NOT NULL default '0',
  `source` varchar(255) NOT NULL default '',
  `title` varchar(255) NOT NULL default '',
  `description` text NOT NULL,
  `units` tinyint(1) NOT NULL default '1',
  `currency` tinyint(1) NOT NULL default '1',
  `price` float(9,2) NOT NULL default '0.00',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_productsorts`
-- 

DROP TABLE IF EXISTS `pb_productsorts`;
CREATE TABLE `pb_productsorts` (
  `id` smallint(3) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_quotes`
-- 

DROP TABLE IF EXISTS `pb_quotes`;
CREATE TABLE `pb_quotes` (
  `id` smallint(6) NOT NULL auto_increment,
  `product_id` int(10) NOT NULL default '-1',
  `market_id` smallint(6) NOT NULL default '-1',
  `type_id` smallint(6) NOT NULL default '0',
  `title` varchar(255) NOT NULL default '',
  `content` text NOT NULL,
  `area_id` smallint(6) NOT NULL default '0',
  `area_id1` smallint(6) NOT NULL default '0',
  `area_id2` smallint(6) NOT NULL default '0',
  `area_id3` smallint(6) NOT NULL default '0',
  `max_price` float(9,2) NOT NULL default '0.00',
  `min_price` float(9,2) NOT NULL default '0.00',
  `units` tinyint(1) NOT NULL default '1',
  `currency` tinyint(1) NOT NULL default '1',
  `created` int(10) NOT NULL default '0',
  `modified` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;

-- --------------------------------------------------------

-- 
-- Table structure `pb_quotetypes`
-- 

DROP TABLE IF EXISTS `pb_quotetypes`;
CREATE TABLE `pb_quotetypes` (
  `id` smallint(6) NOT NULL auto_increment,
  `parent_id` smallint(6) NOT NULL default '0',
  `level` tinyint(1) NOT NULL default '1',
  `name` varchar(255) NOT NULL default '',
  `display_order` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM ;
