INSERT INTO `pb_brands` (`id`, `member_id`, `company_id`, `type_id`, `if_commend`, `name`, `alias_name`, `picture`, `description`, `hits`, `ranks`, `letter`, `created`, `modified`) VALUES 
(1, -1, 0, 1, 1, 'palm', 'palm', 'sample/brand/1.jpg', '', 1, 0, 'p', unix_timestamp(now()), 0),
(2, -1, 0, 1, 1, 'LG', 'LG', 'sample/brand/2.jpg', '', 1, 0, 'l', unix_timestamp(now()), 0),
(3, -1, 0, 1, 0, 'Motorola', 'motolola', 'sample/brand/3.jpg', '', 1, 0, 'm', unix_timestamp(now()), 0),
(4, -1, 1, 1, 1, 'Nokia', 'nokia', 'sample/brand/4.jpg', '', 1, 0, 'n', unix_timestamp(now()), 0),
(5, 1, 1, 4, 1, 'Philips', 'philips', 'sample/brand/5.jpg', '', 0, 0, 'f', unix_timestamp(now()), 0),
(26, -1, 1, 4, 0, 'Samsung', 'samsung', 'sample/brand/6.jpg', '', 0, 0, 's', unix_timestamp(now()), 0),
(27, -1, -1, 4, 0, 'Matsushita', 'panasonic', 'sample/brand/7.jpg', '', 0, 0, 's', unix_timestamp(now()), 0),
(28, -1, 0, 4, 0, 'Sony Ericsson', 'sony', 'sample/brand/8.jpg', '', 0, 0, 's', unix_timestamp(now()), 0),
(29, -1, -1, 4, 0, 'Siemens', 'simens', 'sample/brand/9.jpg', '', 0, 0, 'x', unix_timestamp(now()), 0),
(32, 1, 1, 275, 0, 'Alcatel', 'alcatel', 'sample/brand/10.jpg', 'asdf', 0, 0, 'a', unix_timestamp(now()), 0);

-- 
-- Export data in the table `pb_expoes`
-- 

INSERT INTO `pb_expoes` (`id`, `expotype_id`, `name`, `description`, `begin_time`, `end_time`, `industry_ids`, `industry_id1`, `industry_id2`, `industry_id3`, `area_id1`, `area_id2`, `area_id3`, `address`, `stadium_name`, `refresh_method`, `scope`, `hosts`, `organisers`, `co_organisers`, `sponsors`, `contacts`, `important_notice`, `picture`, `if_commend`, `status`, `hits`, `created`, `modified`) VALUES 
(1, 1, 'The 14th China Beauty Expo opening
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(2, 1, 'The 29th Guangzhou International Beauty Fair Aspect wonderful show
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(3, 1, 'Nutrition and Health of the Tenth China International Industry Fair
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(4, 1, 'Shanghai, China, Textiles and Nonwovens Exhibition
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(5, 2, 'Tokyo International Exhibition of footwear and leather products
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(6, 2, 'Th China International Glass Industrial Technical Exhibition
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(7, 1, 'Fifth Inner Mongolia, China Food Processing and Packaging Machinery Exhibition
', '', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', '', '', '', 0, 1, 1, unix_timestamp(now()), 0),
(8, 2, 'Seventh China International Food Processing and Packaging Equipment Exhibition
', NULL, unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', NULL, NULL, '', 0, 1, 1, unix_timestamp(now()), 0),
(9, 1, 'Tenth China Beijing International Green Food and Organic Food Fair
', NULL, unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', NULL, NULL, '', 0, 1, 1, unix_timestamp(now()), 0),
(10, 2, '15th China International Building Decoration Materials Exhibition
', NULL, unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', NULL, NULL, '', 0, 1, 1, unix_timestamp(now()), 0),
(11, 1, 'Third Western China Building Technology Exhibition
', NULL, unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', NULL, NULL, 'sample/other/fair-2.jpg', 1, 1, 1, unix_timestamp(now()), 0),
(12, 1, 'World Expo 2010 Shanghai China
', 'Shanghai World Expo is our opportunity
', unix_timestamp(now()), unix_timestamp(now())+2592000, '0', 0, 0, 0, 0, 0, 0, '', '', '', '', '', '', '', '', NULL, NULL, 'sample/other/fair-1.jpg', 1, 1, 1, unix_timestamp(now()), 0);

-- 
-- Export data in the table `pb_dicts`
-- 

INSERT INTO `pb_dicts` (`id`, `dicttype_id`, `extend_dicttypeid`, `word`, `word_name`, `digest`, `content`, `picture`, `refer`, `hits`, `closed`, `if_commend`, `created`, `modified`) VALUES 
(1, 7, '', 'Cost and freight
', 'Cost and Freight', 'Cost and freight
（Cost and Freight）(named port of shipment)，Named port of destination
。</p>', '<p>Cost and freight
（Cost and Freight）(named port of shipment)，Named port of destination
。It means the seller must pay the goods to the named port of destination, cost and freight, but goods to the ship deck, the cargo risk, loss or damage and accidents resulting from the additional expenditure in the goods pass the designated port ship, the burden on the buyer by the seller．Also requires the seller of goods customs clearance procedures. The term for sea or inland waterway transport.', '', '', 2, 0, 0, unix_timestamp(), unix_timestamp()),
(2, 0, '', 'Multimodal transport', '', 'Multimodal transport, is the shipment to destination from the transport process contains two or more modes of transport, sea, land, air, river and so on.', '', '', '', 1, 0, 0, unix_timestamp(), unix_timestamp());

-- 
-- Export data in the table `pb_dicttypes`
-- 

INSERT INTO `pb_dicttypes` (`id`, `name`, `parent_id`, `display_order`) VALUES 
(1, 'Logistics knowledge', 0, 0),
(2, 'Logistics Management', 0, 0),
(3, 'Logistics', 0, 0),
(4, 'Laws and regulations', 0, 0),
(5, 'E-commerce', 0, 0),
(6, 'Knowledge', 0, 0),
(7, 'Common Terms', 1, 0),
(8, 'Transport knowledge', 1, 0),
(9, 'Storage of knowledge', 1, 0),
(10, 'Logistics Equipment', 1, 0),
(11, 'Logistics Insurance', 1, 0),
(12, 'Third Party Logistics', 1, 0),
(13, 'Supply Chain', 1, 0),
(14, 'Other', 1, 0);


-- 
-- Export data in the table `pb_expotypes`
-- 

INSERT INTO `pb_expotypes` VALUES (1, 'China Exhibition', 0, 0);
INSERT INTO `pb_expotypes` VALUES (2, 'International Exhibition', 0, 0);

-- 
-- Export data in the table `pb_companies`
-- 

INSERT INTO `pb_companies` (`id`, `member_id`, `cache_spacename`, `cache_membergroupid`, `cache_credits`, `topleveldomain`, `industry_id1`, `industry_id2`, `industry_id3`, `area_id1`, `area_id2`, `area_id3`, `type_id`, `name`, `description`, `english_name`, `keywords`, `boss_name`, `manage_type`, `year_annual`, `property`, `configs`, `bank_from`, `bank_account`, `main_prod`, `employee_amount`, `found_date`, `reg_fund`, `reg_address`, `address`, `zipcode`, `main_brand`, `main_market`, `main_biz_place`, `main_customer`, `link_man`, `link_man_gender`, `position`, `tel`, `fax`, `mobile`, `email`, `site_url`, `picture`, `status`, `first_letter`, `if_commend`, `clicked`, `created`, `modified`) VALUES 
(13, 1, 'admin', 9, 0, '', 0, 0, 0, '1', '0', '0', 2, 'Beijing Ualink E-Commerce Inc.', 'Beijing Ualink E-Commerce Inc.', 'Ualink', '', '', '0', 0, 1, NULL, '', '', 'Ualink', '', '', 5, 'Beijing', 'Dongcheng District, Beijing', '100010', 'Ualink', '', 'Beijing', '', '', 1, 4, '(000)10-84128912', '(000)10-84128912', '', 'service@phpb2b.com', 'http://www.phpb2b.com/', 'sample/company/1.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(3, 2, 'athena1', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Dongguan Chong Yi Plastics Co', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(4, 2, 'athena2', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Copper Co., Ltd. Ningbo Jiangdong Xing Feng', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(5, 2, 'athena3', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Shenzhen Electronic Technology Co., Ltd. letter Noda', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(6, 2, 'athena4', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Friction Material Co., Ltd. Hangzhou Jonah', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(7, 2, 'athena5', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Shenzhen Hualian Trade Co., Ltd.', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(8, 2, 'athena6', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Building Decoration Co., Ltd. Beijing Xuan St.', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(9, 2, 'athena7', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'On Haixibosi Flow Control Co., Ltd.', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(10, 2, 'athena8', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Sichuan Jin Ming Building Material Co., Ltd.', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(11, 2, 'athena9', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Master Automation Suzhou Environmental Protection Equipment Co., Ltd. 100', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(1, 2, 'athena10', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Guangzhou Yuan Yang Machinery Co., Ltd.', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(12, 2, 'athena12', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Marco Polo Ceramics Co., Ltd. Guangdong', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(14, 2, 'athena14', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'Guangdong Foshan Ceramic Co., Ltd. letter Deli', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0),
(2, 2, 'athena11', 9, 0, '', 0, 0, 0, '1', '2', '3', 0, 'jiangsu Haian petrochemical plant', 'This is the presentation of data, do not guarantee the authenticity of the data', '', '', '', '0', 0, 1, NULL, '', '', '', '', '', 0, '', '', '', '', '', '', '', '', 1, 7, '', '', '', '', '', 'sample/company/no.jpg', 1, 'Z', 1, 1, unix_timestamp(now()), 0);

-- 
-- 
-- Export data in the table `pb_newses`
-- 

INSERT INTO `pb_newses` (`id`, `type_id`, `type`, `industry_id`, `area_id`, `title`, `content`, `source`, `picture`, `if_focus`, `if_commend`, `highlight`, `clicked`, `status`, `flag`, `require_membertype`, `tag_ids`, `created`, `create_time`, `modified`) VALUES 
(1, 1, 0, 0, 0, 'Oral and cultural development of domestic opportunities motivate', '', '', '', 0, 0, 0, 1, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(2, 1, 0, 0, 0, 'Q CEP certification issues related to EU', '', '', '', 0, 0, 0, 1, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(3, 1, 0, 0, 0, 'Health Products counterfeit drug sales in the six ways', '', '', '', 0, 0, 0, 4, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(4, 1, 0, 0, 0, 'Gift ideas to promote business in China', '', '', 'sample/news/1.jpg', 0, 0, 0, 1, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(5, 2, 0, 0, 0, 'Iron ore project works against flotation Tender Notice', '', '', '', 0, 0, 0, 2, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(6, 2, 0, 0, 0, 'Public shoe brand marketing war started', '', '', '', 0, 0, 0, 3, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(7, 2, 0, 0, 0, 'Stupid rat autumn and winter will be a successful battle order', '', '', '', 0, 0, 0, 1, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(8, 2, 0, 0, 0, 'Cocoa Magic Paradise duck song sounded happy childhood', '', '', 'sample/news/1.jpg', 0, 0, 0, 2, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(9, 2, 0, 0, 0, 'Interview with Blue Star Glass Company Sales Director Han Lijun', '', '', 'sample/news/2.jpg', 0, 0, 0, 4, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(10, 2, 0, 0, 0, 'Zhejiang businessmen settled two million items of glass diamonds Wuning', '', '', 'sample/news/3.jpg', 0, 0, 0, 4, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(11, 2, 0, 0, 0, 'Blue Show to enhance its brand image', '', '', 'sample/news/4.jpg', 0, 0, 0, 3, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(12, 2, 0, 0, 0, '2010 Nanjing Stone Market Status and Future Analysis
', '', '', 'sample/news/5.jpg', 0, 0, 0, 6, 1, 0, '0', '', unix_timestamp(now()), now(), 0),
(13, 2, 0, 0, 0, 'China, the revision of professional standards of pens', '', '', 'sample/news/6.jpg', 0, 0, 0, 13, 1, 0, '0', '', unix_timestamp(now()), now(), 0);


-- 
-- Export data in the table `pb_newstypes`
-- 

INSERT INTO `pb_newstypes` (`name`, `level_id`, `status`, `parent_id`, `created`) VALUES 
('Industry Focus', 1, 1, 0, unix_timestamp(now())),
('Headline News', 1, 1, 0, unix_timestamp(now())),
('Site News', 1, 1, 0, unix_timestamp(now())),
('Company reports', 1, 1, 0, unix_timestamp(now())),
('Media Highlights', 1, 1, 0, unix_timestamp(now())),
('Hot topics', 1, 1, 0, unix_timestamp(now())),
('High-end interview', 1, 1, 0, unix_timestamp(now())),
('News Express', 1, 1, 0, unix_timestamp(now()));

-- 
-- Export data in the table `pb_topics`
-- 

INSERT INTO `pb_topics` (`id`, `title`, `picture`, `description`, `created`, `modified`) VALUES (1, 'Explore the mysterious red planet Mars', 'sample/news/topic1.jpg', '', unix_timestamp(now()), 0);
INSERT INTO `pb_topics` (`id`, `title`, `picture`, `description`, `created`, `modified`) VALUES (2, 'Volcanic eruption in southern Iceland again', 'sample/news/topic2.jpg', '', unix_timestamp(now()), 0);
INSERT INTO `pb_topics` (`id`, `title`, `picture`, `description`, `created`, `modified`) VALUES (3, 'Explore the whole Earth Hour Live ', 'sample/news/topic3.jpg', '', unix_timestamp(now()), 0);

-- 
-- Export data in the table `pb_areatypes`
-- 

INSERT INTO `pb_areatypes` VALUES (1, 'North China');
INSERT INTO `pb_areatypes` VALUES (2, 'Northeast');
INSERT INTO `pb_areatypes` VALUES (3, 'East China');
INSERT INTO `pb_areatypes` VALUES (4, 'Central China');
INSERT INTO `pb_areatypes` VALUES (5, 'Southwest');
INSERT INTO `pb_areatypes` VALUES (6, 'Northwest');
INSERT INTO `pb_areatypes` VALUES (7, 'South China');
INSERT INTO `pb_areatypes` VALUES (8, 'SAR');

-- 
-- Export data in the table `pb_areas`
-- 

INSERT INTO `pb_areas` (`id`, `attachment_id`, `areatype_id`, `child_ids`, `top_parentid`, `level`, `name`, `url`, `alias_name`, `highlight`, `parent_id`, `display_order`, `description`, `available`, `created`, `modified`) VALUES 
(1, 0, 0, NULL, 0, 1, 'Australia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(2, 0, 0, NULL, 0, 1, 'Indonesia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(3, 0, 0, NULL, 0, 1, 'New Zealand', '', '', 0, 0, 0, NULL, 1, 0, 0),
(4, 0, 0, NULL, 0, 1, 'South Korea', '', '', 0, 0, 0, NULL, 1, 0, 0),
(5, 0, 0, NULL, 0, 1, 'China', '', '', 0, 0, 0, NULL, 1, 0, 0),
(6, 0, 0, NULL, 0, 1, 'Iran', '', '', 0, 0, 0, NULL, 1, 0, 0),
(7, 0, 0, NULL, 0, 1, 'Pakistan', '', '', 0, 0, 0, NULL, 1, 0, 0),
(8, 0, 0, NULL, 0, 1, 'Taiwan', '', '', 0, 0, 0, NULL, 1, 0, 0),
(9, 0, 0, NULL, 0, 1, 'Hong Kong', '', '', 0, 0, 0, NULL, 1, 0, 0),
(10, 0, 0, NULL, 0, 1, 'Japan', '', '', 0, 0, 0, NULL, 1, 0, 0),
(11, 0, 0, NULL, 0, 1, 'Philippines', '', '', 0, 0, 0, NULL, 1, 0, 0),
(12, 0, 0, NULL, 0, 1, 'Thailand', '', '', 0, 0, 0, NULL, 1, 0, 0),
(13, 0, 0, NULL, 0, 1, 'India', '', '', 0, 0, 0, NULL, 1, 0, 0),
(14, 0, 0, NULL, 0, 1, 'Malaysia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(15, 0, 0, NULL, 0, 1, 'Singapore', '', '', 0, 0, 0, NULL, 1, 0, 0),
(16, 0, 0, NULL, 0, 1, 'Vietnam', '', '', 0, 0, 0, NULL, 1, 0, 0),
(17, 0, 0, NULL, 0, 1, 'Argentina', '', '', 0, 0, 0, NULL, 1, 0, 0),
(18, 0, 0, NULL, 0, 1, 'Canada', '', '', 0, 0, 0, NULL, 1, 0, 0),
(19, 0, 0, NULL, 0, 1, 'Colombia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(20, 0, 0, NULL, 0, 1, 'Peru', '', '', 0, 0, 0, NULL, 1, 0, 0),
(21, 0, 0, NULL, 0, 1, 'Brazil', '', '', 0, 0, 0, NULL, 1, 0, 0),
(22, 0, 0, NULL, 0, 1, 'Chile', '', '', 0, 0, 0, NULL, 1, 0, 0),
(23, 0, 0, NULL, 0, 1, 'Mexico', '', '', 0, 0, 0, NULL, 1, 0, 0),
(24, 0, 0, NULL, 0, 1, 'United States', '', '', 0, 0, 0, NULL, 1, 0, 0),
(25, 0, 0, NULL, 0, 1, 'Belgium', '', '', 0, 0, 0, NULL, 1, 0, 0),
(26, 0, 0, NULL, 0, 1, 'Germany', '', '', 0, 0, 0, NULL, 1, 0, 0),
(27, 0, 0, NULL, 0, 1, 'Poland', '', '', 0, 0, 0, NULL, 1, 0, 0),
(28, 0, 0, NULL, 0, 1, 'Sweden', '', '', 0, 0, 0, NULL, 1, 0, 0),
(29, 0, 0, NULL, 0, 1, 'Bulgaria', '', '', 0, 0, 0, NULL, 1, 0, 0),
(30, 0, 0, NULL, 0, 1, 'Greece', '', '', 0, 0, 0, NULL, 1, 0, 0),
(31, 0, 0, NULL, 0, 1, 'Portugal', '', '', 0, 0, 0, NULL, 1, 0, 0),
(32, 0, 0, NULL, 0, 1, 'Switzerland', '', '', 0, 0, 0, NULL, 1, 0, 0),
(33, 0, 0, NULL, 0, 1, 'Czech Republic', '', '', 0, 0, 0, NULL, 1, 0, 0),
(34, 0, 0, NULL, 0, 1, 'Iceland', '', '', 0, 0, 0, NULL, 1, 0, 0),
(35, 0, 0, NULL, 0, 1, 'Romania', '', '', 0, 0, 0, NULL, 1, 0, 0),
(36, 0, 0, NULL, 0, 1, 'Turkey', '', '', 0, 0, 0, NULL, 1, 0, 0),
(37, 0, 0, NULL, 0, 1, 'Denmark', '', '', 0, 0, 0, NULL, 1, 0, 0),
(38, 0, 0, NULL, 0, 1, 'Italy', '', '', 0, 0, 0, NULL, 1, 0, 0),
(39, 0, 0, NULL, 0, 1, 'Russia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(40, 0, 0, NULL, 0, 1, 'Ukraine', '', '', 0, 0, 0, NULL, 1, 0, 0),
(41, 0, 0, NULL, 0, 1, 'France', '', '', 0, 0, 0, NULL, 1, 0, 0),
(42, 0, 0, NULL, 0, 1, 'Netherlands', '', '', 0, 0, 0, NULL, 1, 0, 0),
(43, 0, 0, NULL, 0, 1, 'Spain', '', '', 0, 0, 0, NULL, 1, 0, 0),
(44, 0, 0, NULL, 0, 1, 'United Kingdom', '', '', 0, 0, 0, NULL, 1, 0, 0),
(45, 0, 0, NULL, 0, 1, 'Israel', '', '', 0, 0, 0, NULL, 1, 0, 0),
(46, 0, 0, NULL, 0, 1, 'Syria', '', '', 0, 0, 0, NULL, 1, 0, 0),
(47, 0, 0, NULL, 0, 1, 'United Arab ', '', '', 0, 0, 0, NULL, 1, 0, 0),
(48, 0, 0, NULL, 0, 1, 'Emirates', '', '', 0, 0, 0, NULL, 1, 0, 0),
(49, 0, 0, NULL, 0, 1, 'Saudi Arabia', '', '', 0, 0, 0, NULL, 1, 0, 0),
(50, 0, 0, NULL, 0, 1, 'Egypt', '', '', 0, 0, 0, NULL, 1, 0, 0),
(51, 0, 0, NULL, 0, 1, 'South Africa', '', '', 0, 0, 0, NULL, 1, 0, 0);

-- 
-- Export data in the table `pb_industrytypes`
-- 

INSERT INTO `pb_industrytypes` VALUES (1, 'Industrial');
INSERT INTO `pb_industrytypes` VALUES (2, 'Consumer goods');
INSERT INTO `pb_industrytypes` VALUES (3, 'Raw materials');
INSERT INTO `pb_industrytypes` VALUES (4, 'Business Services');
INSERT INTO `pb_industrytypes` VALUES (5, 'Other');

-- 
-- Export data in the table `pb_industries`
-- 

INSERT INTO `pb_industries` (`id`, `attachment_id`, `industrytype_id`, `child_ids`, `name`, `url`, `alias_name`, `highlight`, `parent_id`, `top_parentid`, `level`, `display_order`, `description`, `available`, `created`, `modified`) VALUES 
(1, 0, 0, NULL, 'Agriculture', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(2, 0, 0, NULL, 'Apparel&Fashion', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(3, 0, 0, NULL, 'Automobile', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(4, 0, 0, NULL, 'BusinessServices', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(5, 0, 0, NULL, 'Chemicals', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(6, 0, 0, NULL, 'ComputerHardwareSoftware', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(7, 0, 0, NULL, 'Construction&RealEstate', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(8, 0, 0, NULL, 'Electronics&Electrical', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(9, 0, 0, NULL, 'Energy', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(10, 0, 0, NULL, 'Environment', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(11, 0, 0, NULL, 'ExcessInventory', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(12, 0, 0, NULL, 'Food&Beverage', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(13, 0, 0, NULL, 'Gifts&Crafts', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(14, 0, 0, NULL, 'Health&Beauty', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(15, 0, 0, NULL, 'HomeAppliances', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(16, 0, 0, NULL, 'HomeSupplies', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(17, 0, 0, NULL, 'IndustrialSupplies', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(18, 0, 0, NULL, 'Minerals,Metals&Materials', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(19, 0, 0, NULL, 'OfficeSupplies', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(20, 0, 0, NULL, 'Packaging&Paper', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(21, 0, 0, NULL, 'Printing&Publishing', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(22, 0, 0, NULL, 'Security&Protection', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(23, 0, 0, NULL, 'Sports&Entertainment', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(24, 0, 0, NULL, 'Telecommunications', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(25, 0, 0, NULL, 'Textiles&LeatherProducts', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(26, 0, 0, NULL, 'Toys', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(27, 0, 0, NULL, 'Transportation', '', '', 0, 0, 0, 1, 0, NULL, 1, 0, 0),
(28, 0, 0, NULL, 'Agriculture&By-productAgent', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(29, 0, 0, NULL, 'AgricultureProductStocks', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(30, 0, 0, NULL, 'Agrochemicals&Pesticides', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(31, 0, 0, NULL, 'AnimalExtract', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(32, 0, 0, NULL, 'AnimalFodders', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(33, 0, 0, NULL, 'AnimalHusbandry', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(34, 0, 0, NULL, 'Bamboo&RattanProducts', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(35, 0, 0, NULL, 'Beans', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(36, 0, 0, NULL, 'Cigarette&Tobacco', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(37, 0, 0, NULL, 'Eggs', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(38, 0, 0, NULL, 'FarmMachines&Tools', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(39, 0, 0, NULL, 'FisheryMachinery', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(40, 0, 0, NULL, 'Flowers&Plant', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(41, 0, 0, NULL, 'ForestMachinery', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(42, 0, 0, NULL, 'Fruit', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(43, 0, 0, NULL, 'Grain', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(44, 0, 0, NULL, 'Mushroom&Truffle', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(45, 0, 0, NULL, 'Nuts&Kernels', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(46, 0, 0, NULL, 'Others', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(47, 0, 0, NULL, 'Plant&AnimalOil', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(48, 0, 0, NULL, 'PlantExtract', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(49, 0, 0, NULL, 'PlantSeed', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(50, 0, 0, NULL, 'Poultry&Livestock', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(51, 0, 0, NULL, 'Vegetable', '', '', 0, 1, 1, 2, 0, NULL, 1, 0, 0),
(52, 0, 0, NULL, 'Apparel & Fashion Agents', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(53, 0, 0, NULL, 'Apparel Stocks', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(54, 0, 0, NULL, 'Athletic Wear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(55, 0, 0, NULL, 'Bathrobe', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(56, 0, 0, NULL, 'Children Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(57, 0, 0, NULL, 'Costume & Ceremony', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(58, 0, 0, NULL, 'Down Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(59, 0, 0, NULL, 'Ethnic Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(60, 0, 0, NULL, 'Fashion Accessories', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(61, 0, 0, NULL, 'Belts & Accessories Hats & Caps moreFootwear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(62, 0, 0, NULL, 'Sports Shoes Boots moreFur Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(63, 0, 0, NULL, 'Garment Accessories', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(64, 0, 0, NULL, 'Buttons & Buckles Others moreGloves & Mittens', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(65, 0, 0, NULL, 'Infant Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(66, 0, 0, NULL, 'Jacket', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(67, 0, 0, NULL, 'Jeans', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(68, 0, 0, NULL, 'Leather Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(69, 0, 0, NULL, 'Leisure Wear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(70, 0, 0, NULL, 'Others', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(71, 0, 0, NULL, 'Outerwear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(72, 0, 0, NULL, 'Pants & Trousers', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(73, 0, 0, NULL, 'Related Machine', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(74, 0, 0, NULL, 'Sewing Machinery', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(75, 0, 0, NULL, 'Shirts & Blouses', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(76, 0, 0, NULL, 'Silk Garment', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(77, 0, 0, NULL, 'Skirts & Dresses', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(78, 0, 0, NULL, 'Socks & Stockings', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(79, 0, 0, NULL, 'Speciality', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(80, 0, 0, NULL, 'Suits & Tuxedo', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(81, 0, 0, NULL, 'Sweaters', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(82, 0, 0, NULL, 'Swimwear & Beachwear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(83, 0, 0, NULL, 'T-Shirts', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(84, 0, 0, NULL, 'Underwear & Nightwear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(85, 0, 0, NULL, 'Bras & Lingerie Underwear Set moreUniforms & Workwear', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0),
(86, 0, 0, NULL, 'Used Clothes', '', '', 0, 16, 16, 2, 0, NULL, 1, 0, 0);

-- 
-- Export data in the table `pb_announcements`
-- 

INSERT INTO `pb_announcements` (`id`, `announcetype_id`, `subject`, `message`, `display_order`, `display_expiration`, `created`, `modified`) VALUES (13, 0, 'How to position your product closer to the top', NULL, 0, 0, unix_timestamp(now()), 0);
INSERT INTO `pb_announcements` (`id`, `announcetype_id`, `subject`, `message`, `display_order`, `display_expiration`, `created`, `modified`) VALUES (14, 0, 'Web Member Services', 'By Gold member, you can not just their own business, product and other commercial information published on the Internet, can also take the initiative to establish contact with many buyers.', 0, 0, unix_timestamp(now()), 0);
INSERT INTO `pb_announcements` (`id`, `announcetype_id`, `subject`, `message`, `display_order`, `display_expiration`, `created`, `modified`) VALUES (11, 0, '2009, 500 Chinese enterprises released', NULL, 0, 0, unix_timestamp(now()), 0);
INSERT INTO `pb_announcements` (`id`, `announcetype_id`, `subject`, `message`, `display_order`, `display_expiration`, `created`, `modified`) VALUES (12, 0, 'Open gold medal Wang Pu', NULL, 0, 0, unix_timestamp(now()), 0);
INSERT INTO `pb_announcements` (`id`, `announcetype_id`, `subject`, `message`, `display_order`, `display_expiration`, `created`, `modified`) VALUES (4, 1, 'Wonderful Expo, concerned about environmental protection ', NULL, 0, 0, unix_timestamp(now()), 0);

-- 
-- Export data in the table `pb_announcementtypes`
-- 

INSERT INTO `pb_announcementtypes` VALUES (1, 'Site Notice');
INSERT INTO `pb_announcementtypes` VALUES (2, 'Advertising time');

-- 
-- Export data in the table `pb_markets`
-- 

INSERT INTO `pb_markets` (`name`, `content`, `area_id1`, `area_id2`, `area_id3`, `industry_id1`, `industry_id2`, `industry_id3`, `picture`, `ip_address`, `status`, `clicked`, `if_commend`, `created`, `modified`) VALUES 
('Hardware products market', '', 0, 0, 0, 2, 0, 0, 'sample/market/01.jpg', 0, 1, 1, 1, unix_timestamp(), 0),
('Fiber professional market', '', 0, 0, 0, 2, 0, 0, 'sample/market/02.jpg', 0, 1, 1, 1, unix_timestamp(), 0),
('Haining China Leather Town', '', 0, 0, 0, 0, 0, 0, 'sample/market/03.jpg', 0, 1, 1, 1, unix_timestamp(), 0),
('Fur World, Zhejiang Chongfu', '', 0, 0, 0, 0, 0, 0, 'sample/market/04.jpg', 0, 1, 1, 1, unix_timestamp(), 0);

-- 
-- Export data in the table `pb_products`
-- 

INSERT INTO `pb_products` (`id`, `member_id`, `company_id`, `cache_companyname`, `sort_id`, `brand_id`, `industry_id1`, `industry_id2`, `industry_id3`, `area_id1`, `area_id2`, `area_id3`, `name`, `price`, `sn`, `spec`, `produce_area`, `packing_content`, `picture`, `content`, `producttype_id`, `status`, `state`, `ifnew`, `ifcommend`, `priority`, `tag_ids`, `clicked`, `formattribute_ids`, `created`, `modified`) VALUES 
(1, 1, 13, 'Ualink E-Commerce Inc.', 0, 0, 5, 196, 0, 1, 0, 0, '32-inch HD LCD TV', '0', '', '', '', '', 'sample/product/1.jpg', NULL, 0, 1, 1, 0, 1, 0, '', 1, NULL, unix_timestamp(now()), 1261978847),
(2, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 1, 0, 0, 'dv_sx_c10', '0', '', '', '', '', 'sample/product/2.jpg', NULL, 0, 1, 1, 0, 1, 0, NULL, 1, NULL, unix_timestamp(now()), 1261992494),
(3, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 1, 0, 0, 'Electronic Dictionary', '0', '', '', '', '', 'sample/product/3.jpg', '', 0, 1, 1, 0, 1, 0, NULL, 2, NULL, unix_timestamp(now()), 1261992725),
(4, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 1, 0, 0, 'e navigation', '0', '', '', '', '', 'sample/product/4.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, unix_timestamp(now()), 1261992649),
(5, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 0, 0, 0, 'Value independence were Notebook', '0', '', '', '', '', 'sample/product/5.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, unix_timestamp(now()), 0),
(6, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 0, 0, 0, 'MP4 video player', '0', '', '', '', '', 'sample/product/6.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, unix_timestamp(now()), 0),
(7, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 0, 0, 0, 'Poor nobility HD 1080', '0', '', '', '', '', 'sample/product/7.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, unix_timestamp(now()), 0),
(8, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 0, 0, 0, 'Can be back-type bag', '0', '', '', '', '', 'sample/product/8.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 3, NULL, unix_timestamp(now()), 0),
(9, 1, 13, 'Ualink E-Commerce Inc.', 1, 0, 1, 0, 0, 1, 24, 0, '10 million pixel digital camera
', '0', '', '', '', '', 'sample/product/9.jpg', NULL, 0, 1, 1, 0, 0, 0, NULL, 1, NULL, unix_timestamp(now()), 0),
(10, 1, 13, '友邻电子商务网', 1, 0, 1, 0, 0, 1, 28, 0, 'Fashion DV', '0', '', '', '', '', 'sample/product/10.jpg', NULL, 0, 1, 1, 0, 1, 0, NULL, 11, NULL, unix_timestamp(now()), 0);


-- 
-- Export data in the table `pb_trades`
-- 

INSERT INTO `pb_trades` (`id`, `type_id`, `industry_id1`, `industry_id2`, `industry_id3`, `area_id1`, `area_id2`, `area_id3`, `member_id`, `company_id`, `cache_username`, `cache_companyname`, `cache_contacts`, `title`, `content`, `price`, `measuring_unit`, `monetary_unit`, `display_order`, `display_expiration`, `picture`, `picture_remote`, `status`, `submit_time`, `expire_time`, `expire_days`, `if_commend`, `if_urgent`, `if_locked`, `require_point`, `require_membertype`, `require_freedate`, `ip_addr`, `clicked`, `tag_ids`, `formattribute_ids`, `created`, `modified`) VALUES 
(1, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Buy double-door stainless steel table ', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/12.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(2, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Buy rice, peanuts, red bean corn', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(3, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Estonian businessmen purchase freshwater bass', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(4, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'SLEEPCOCompany Buy', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(5, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Buy camphor pine recruits and', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(6, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Buy watermelon', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(7, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Asian business Chinese-made metal furniture Buy', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(8, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Nepal Tobacco tender', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(9, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Asian business Chinese-made food packaging equipment Buy', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(10, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Asia Business Buy China fabric', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/8.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 1, '1', '0', 0, 0, 0, '', 2, '', NULL, unix_timestamp(now()), 0),
(11, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Asia Business Buy computer parts made in China', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/9.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(12, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Libyan businessmen interested in buying Chinese-made French fries production equipment', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/11.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(13, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'The supply of Japanese larch seedlings', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/12.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(14, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Supply all kinds of flowers', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(15, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Aspen Trust Group to seek cooperation with Chinese companies', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(16, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Find buyers honey', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(17, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Croatia yacht company wants to sell the yacht', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(18, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Israeli fruits and vegetables and agricultural companies to seek domestic buyers', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(19, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Section 1 cocoa processing companies to seek buyers in China', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(20, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Ireland ECOBUILD Exhibition', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(21, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'You kind of young fruit tree supply Taiwan', NULL, 0.00, '0', '0', 0, 0, '', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(22, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Supply the entire paragraph 7 yuan T shirt wholesale clothing inventory', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/7.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(23, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Promotional Wooden Comb ', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/6.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(24, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Sell Aida Arm Chairs ', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/5.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(25, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Vacuum Dehydrator For Emulsified', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/4.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(26, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Samsung Cartridge Chips ', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/3.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(27, '2', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Sell Polarized Lens', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/2.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(28, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Buy Cattle Fence From', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/12.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0),
(29, '1', 0, 0, 0, 0, 0, 0, 1, 13, '', '', '', 'Bosch Engine', NULL, 0.00, '0', '0', 0, 0, 'sample/offer/1.jpg', '', 1, unix_timestamp(now()), unix_timestamp(now())+86400, 0, 0, '0', '0', 0, 0, 0, '', 1, '', NULL, unix_timestamp(now()), 0);

-- 
-- Export data in the table `pb_goods`
-- 

INSERT INTO `pb_goods` VALUES (2, 'Senior Member Upgrade', '', 1500.00, 1, '', 0, unix_timestamp(now()), unix_timestamp(now()));
INSERT INTO `pb_goods` VALUES (1, 'General Membership Upgrade', '', 1000.00, 1, '', 0, unix_timestamp(now()), unix_timestamp(now()));

INSERT INTO `pb_standards` (`id`, `attachment_id`, `type_id`, `title`, `source`, `digest`, `content`, `publish_time`, `force_time`, `clicked`, `created`, `modified`) VALUES 
(1, 0, 1, 'Building walls with ACP', '', '', 0, 0, 1000, unix_timestamp(), 0),
(2, 0, 2, 'Standard terms of contract processing of hollow glass', '', '', '', 0, 0, 2, unix_timestamp(), 0),
(3, 0, 3, '10 indoor national mandatory standards for hazardous substances', '', '', '', 0, 0, 1, unix_timestamp(), 0),
(4, 0, 4, 'Wood Packaging Material in International Trade Management Guidelines', '', '', '', 0, 0, 3, unix_timestamp(), 0),
(5, 0, 5, 'Construction crane safety supervision and management regulations', '', '', '', 0, 0, 2, unix_timestamp(), 0),
(6, 0, 6, 'Pollution-free green food knowledge of organic food standards', '', '', '', 0, 0, 3, unix_timestamp(), 0);

-- 
-- Export data in the table `pb_brandtypes`
-- 

INSERT INTO `pb_brandtypes` (`id`, `parent_id`, `level`, `name`, `display_order`) VALUES 
(1, 0, 1, 'Decorative building materials', 0),
(2, 0, 1, 'Clothing and shoes', 0),
(3, 0, 1, 'Household Furniture', 0),
(4, 0, 1, 'Food', 0),
(5, 0, 1, 'Digital home appliances', 0),
(6, 0, 1, 'Auto Real Estate', 0),
(7, 0, 1, 'Dining Entertainment', 0),
(8, 0, 1, 'Mechanical Chemical', 0),
(9, 5, 2, 'Washing machine', 0),
(10, 5, 2, 'Drinking', 0),
(11, 5, 2, 'Computer', 0),
(12, 5, 2, 'Mobile', 0);

-- 
-- Export data in the table `pb_productcategories`
-- 

INSERT INTO `pb_productcategories` (`id`, `parent_id`, `level`, `name`, `display_order`) VALUES 
(1, 0, 1, 'ELECTRONIC ELEMENTS', 0), 
(2, 0, 1, 'beauty treatment', 0), 
(3, 0, 1, 'medical care', 0), 
(4, 0, 1, 'instrument', 0), 
(5, 0, 1, 'household goods', 0), 
(6, 0, 1, 'gifts and toys', 0), 
(7, 0, 1, 'Automobiles and distribution', 0), 
(8, 0, 1, 'Metallurgy of steel', 0), 
(9, 0, 1, 'package', 0), 
(10, 0, 1, 'computer software', 0), 
(11, 0, 1, 'building materials', 0), 
(12, 0, 1, 'print', 0), 
(13, 0, 1, 'appliances', 0), 
(14, 0, 1, 'transport', 0), 
(15, 0, 1, 'communication', 0), 
(16, 0, 1, 'light industry', 0), 
(17, 0, 1, 'Electric Electric', 0), 
(18, 0, 1, 'petrochemicals', 0), 
(19, 0, 1, 'Painting and the table', 0), 
(20, 0, 1, 'rubber, plastics', 0), 
(21, 0, 1, 'security', 0), 
(22, 0, 1, 'Office Supplies', 0), 
(23, 0, 1, 'media', 0), 
(24, 0, 1, 'environmental protection and water treatment', 0), 
(25, 0, 1, 'machinery', 0), 
(26, 0, 1, 'Hardware', 0), 
(27, 0, 1, 'leisure', 0), 
(28, 0, 1, 'paper', 0), 
(29, 0, 1, 'textile and leather', 0), 
(30, 0, 1, 'clothing', 0), 
(31, 0, 1, 'agriculture', 0), 
(32, 0, 1, 'food and drink', 0), 
(33, 0, 1, 'clothing', 0), 
(34, 0, 1, 'business services', 0), 
(35, 0, 1, 'machinery industries', 0), 
(36, 1, 2, 'the insurance component', 0), 
(37, 1, 2, 'semiconductors', 0), 
(38, 1, 2, 'other electronic materials', 0), 
(39, 1, 2, 'capacitor', 0), 
(40, 1, 2, 'other electronic components', 0), 
(41, 1, 2, 'transistor', 0), 
(42, 1, 2, 'diodes', 0), 
(43, 1, 2, 'frequency components', 0),
(44, 1, 2, 'inverter', 0), 
(45, 1, 2, 'inductors', 0), 
(46, 1, 2, 'electro-acoustic and components', 0), 
(47, 1, 2, 'electro-acoustic devices', 0), 
(48, 1, 2, 'electronic vacuum devices', 0), 
(49, 1, 2, 'connect generating units', 0), 
(50, 1, 2, 'relay', 0), 
(51, 1, 2, 'piezoelectric crystal materials', 0), 
(52, 1, 2, 'electronic paste', 0), 
(53, 1, 2, 'electronic plastic products', 0), 
(54, 1, 2, 'FET', 0), 
(55, 1, 2, 'electronic processing', 0), 
(56, 1, 2, 'magnetic and components', 0), 
(57, 1, 2, 'PCB', 0), 
(58, 1, 2, 'switching element', 0), 
(59, 1, 2, 'Optoelectronics and Display Devices', 0), 
(60, 1, 2, 'potentiometer', 0), 
(61, 1, 2, 'display device', 0), 
(62, 1, 2, 'sensors', 0), 
(63, 1, 2, 'electrical measuring instrument', 0), 
(64, 1, 2, 'electric ceramic materials', 0), 
(65, 1, 2, 'IC', 0), 
(66, 1, 2, 'resistor', 0), 
(67, 1, 2, 'flap', 0), 
(68, 1, 2, 'shielding', 0), 
(69, 1, 2, 'Infrared Technology and Application', 0), 
(70, 1, 2, 'e-project cooperation', 0), 
(71, 1, 2, 'Transformers', 0), 
(72, 1, 2, 'laser devices', 0), 
(73, 1, 2, 'electronic hardware', 0), 
(74, 1, 2, 'industrial encoder', 0), 
(75, 1, 2, 'copper clad laminate materials', 0);

-- 
-- Export data in the table `pb_jobtypes`
-- 

INSERT INTO `pb_jobtypes` (`id`, `parent_id`, `level`, `name`, `display_order`) VALUES 
(1, 0, 1, 'software category', 0),
(2, 0, 1, 'site network management', 0),
(3, 0, 1, 'Database type', 0),
(4, 0, 1, 'IT Management', 0),
(5, 0, 1, 'IT Quality Assurance and technical support', 0),
(6, 0, 1, 'communication category', 0),
(7, 0, 1, 'design class', 0),
(8, 0, 1, 'customer service class', 0),
(9, 0, 1, 'Sales Consultant Class', 0),
(10, 0, 1, 'market type', 0),
(11, 0, 1, 'other', 0),
(12, 1, 2, 'Software Engineer', 0),
(13, 1, 2, 'Senior Software Engineer', 0),
(14, 1, 2, 'systems architect', 0),
(15, 1, 2, 'systems analyst', 0),
(16, 1, 2, 'system integration engineers', 0),
(17, 1, 2, 'software testing', 0),
(18, 1, 2, 'other', 0);