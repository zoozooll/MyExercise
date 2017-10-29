/*
MySQL Data Transfer
Source Host: localhost
Source Database: student
Target Host: localhost
Target Database: student
Date: 2009-9-22 2:00:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for archives
-- ----------------------------
DROP TABLE IF EXISTS `archives`;
CREATE TABLE `archives` (
  `id` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL COMMENT '获得何种奖惩',
  `reward_punish` bit(1) DEFAULT NULL,
  `achieve_time` varchar(20) DEFAULT NULL COMMENT '获得奖惩时间',
  `content` varchar(200) DEFAULT NULL COMMENT '奖惩内容',
  `grantor` varchar(20) DEFAULT NULL COMMENT '授予者',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='\r\n\r\n奖惩表; InnoDB free: 9216 kB';

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` int(11) NOT NULL COMMENT '专业',
  `class_name` varchar(255) NOT NULL,
  `special` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='班级学院信息表';

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college` (
  `id` int(11) NOT NULL,
  `col_name` varchar(20) DEFAULT NULL,
  `col_no` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='学院信息';

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `course_name` varchar(20) NOT NULL DEFAULT '课程名',
  `credit` int(11) DEFAULT NULL COMMENT '学分',
  `category` varchar(20) DEFAULT '是否必修',
  `tea_id` int(11) DEFAULT NULL,
  `course_no` varchar(20) DEFAULT '课程编号',
  `openterm_id` int(11) DEFAULT NULL COMMENT '开课学期外键',
  `checkup` varchar(20) DEFAULT '考核制度',
  `tolta` int(11) NOT NULL DEFAULT '100' COMMENT '总分',
  `remark` varchar(100) DEFAULT NULL,
  `open` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否启用',
  `study_time` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='学生成绩表 InnoDB free: 9216 kB';

-- ----------------------------
-- Table structure for special
-- ----------------------------
DROP TABLE IF EXISTS `special`;
CREATE TABLE `special` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '专业或者系名',
  `series` int(11) DEFAULT NULL COMMENT '系ID',
  `college_id` int(11) DEFAULT NULL COMMENT '学院ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='系，专业表';

-- ----------------------------
-- Table structure for stu_course
-- ----------------------------
DROP TABLE IF EXISTS `stu_course`;
CREATE TABLE `stu_course` (
  `id` int(11) NOT NULL,
  `stu_id` int(11) DEFAULT NULL,
  `course_id` int(11) DEFAULT NULL,
  `chose_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '学生选中该课的时间',
  `score` int(11) DEFAULT NULL COMMENT '考试分数',
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='学生选课表';

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `interest` varchar(40) DEFAULT NULL,
  `identity` char(18) NOT NULL,
  `classid` int(11) DEFAULT NULL,
  `stuno` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `enteryear` year(4) DEFAULT NULL,
  `ptpath` varchar(20) DEFAULT NULL,
  `tchno` varchar(20) DEFAULT NULL,
  `identity` char(18) DEFAULT NULL,
  `profess` varchar(20) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for term
-- ----------------------------
DROP TABLE IF EXISTS `term`;
CREATE TABLE `term` (
  `id` int(11) NOT NULL,
  `year` year(4) DEFAULT NULL,
  `au_spr` varchar(10) DEFAULT NULL COMMENT '春季或者秋季',
  `sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `student` VALUES ('1', 'Mary', 'female', '1978-06-07', 'book', '44123456780011', '20080601', null, '123456', null, null);
INSERT INTO `student` VALUES ('2', 'Jone', 'male', '1978-06-10', 'games', '44123456780012', '20080602', null, '123456', null, null);
INSERT INTO `student` VALUES ('3', 'Lily', 'female', '1983-06-07', 'book', '44123456780013', '20080603', null, '123456', null, null);
INSERT INTO `student` VALUES ('4', 'Kim', 'male', '1976-06-07', 'book', '44123456780014', '20080604', null, '123456', null, null);
INSERT INTO `t_user` VALUES ('1', 'stone', '123', 'admin');
INSERT INTO `t_user` VALUES ('2', 'ivan', '123', 'user');
