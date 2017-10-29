<?php

require('inc/function/confirm_manager.php');
require('inc/function/get_date.php');
if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
  err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
}


//连接mysqkl数据库
if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  echo mysql_errno() . ": " . mysql_error() . "\n";
  err('数据库['.$sql['host'].']连接不成功！');
}
//选择数据库并判断
if(!@mysql_select_db($sql['name'],$db)){
  @mysql_query('CREATE DATABASE IF NOT EXISTS '.$sql['name'].'',$db); //如果数据库不存在则创建
  if(!@mysql_select_db($sql['name'],$db)){
    err('数据库['.$sql['name'].']连接不成功！');
  }
}
if(version_compare(mysql_get_server_info(), '4.1.0', '>=')){
  $char=' DEFAULT CHARSET='.$sql['char'].'';
}

mysql_query('SET NAMES '.$sql['char'].'');

//判断表是不是已存在
if($result=@mysql_query('SELECT * FROM yzsoulistdata',$db)){
  $out.='<br />信息库表[yzsoulistdata]已存在！是否：<a href="run.php?run=sql_table_del&table=yzsoulistdata" onclick="return confirm(\'确定删除信息库表[yzsoulistdata]么？\')" target="_blank">删除信息库表</a><br />';
  mysql_free_result($result);
}else{
  //建表语句（列表）
  //题目|内容|图|影片|附件|日期|浏览|类目|会员或IP|GUEST密码|置顶截止日期|精华
  $topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
  $date=get_date($topdate,10);
  $table_born='CREATE TABLE `yzsoulistdata`(
`id` int(10) NOT NULL auto_increment,
`title` varchar(200) NOT NULL default "",
`text` text NOT NULL,
`pic` varchar(40) NOT NULL default "",
`fil` varchar(40) NOT NULL default "",
`enc` varchar(40) NOT NULL default "",
`date` varchar(40) NOT NULL default "",
`reply` int(10) NOT NULL default 0,
`views` int(10) NOT NULL default 0,
`area_id` varchar(40) NOT NULL default "",
`author_ip` varchar(40) NOT NULL default "",
`guestpsw` varchar(40) NOT NULL default "",
`topdate` varchar(40) NOT NULL default "",
`good` varchar(40) NOT NULL default "",
`other1` varchar(200) NOT NULL default "",
`other2` varchar(200) NOT NULL default "",
`other3` varchar(200) NOT NULL default "",
`other4` varchar(200) NOT NULL default "",
PRIMARY KEY (`id`)
) ENGINE=MyISAM'.$char.';';
  $table_into='INSERT INTO `yzsoulistdata`(`id`,`title`,`text`,`date`,`area_id`,`author_ip`,`topdate`) values
(1,"162100网址导航","162100网址导航，是中国最好的绿色专业网址站，收尽精彩实用网址，开启上网方便之门。162100网址导航源码免费下载<br />地址：<A href=\"http://www.162100.com/\">http://www.162100.com/</A>","'.$date.'","96_60","'.$web['manager'].'","'.$topdate.'"),
(2,"保洁公司","保洁为物业服务范畴中之重要主营项目，一些机关、企业、商厦、院校、医院等需要保洁员定时定点日常清洁维护，繁重的工作交给专业的保洁公司来完成。由保洁公司统筹管理，业主只需与保洁公司签定协议，按月结算服务费用即可。<BR><BR><A href=\"http://www.furuijinzhao.com\">福瑞今朝</A>提供专业、精准的保洁服务：<BR><BR> <OL> <br>·制订全面高效的保洁计划及工作流程，并严格执行 <br>·培训保洁员专业技能，提供现场技术指导，让企业感受价值所在 <br>·统一着装，注重文明礼仪，接轨并提升企业品牌、地位及形象 <br>·保洁员定制式用工，完全按照业主需求及质量要求配置，免去业主后顾之忧 <br>·实施“零干扰”清洁服务模式，让您拥有一个清洁安静的工作环境 <br>·我们的保洁员工还可兼任“监督员”、“信息员”、“服务员”等多重角色，让您切实体会增值服务 <br></OL><BR> <TABLE cellSpacing=0 cellPadding=0 border=0> <TBODY> <TR> <TD  style=\"text-align:left\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_1.jpg\" width=160></TD> <TD align=middle><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_2.jpg\" width=160></TD> <TD  style=\"text-align:right\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_3.jpg\" width=160></TD></TR> <TR> <TD align=middle>物业保洁</TD> <TD align=middle>日常保洁</TD> <TD align=middle>开荒保洁</TD></TR> <TR> <TD   style=\"text-align:left\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_4.jpg\" width=160></TD> <TD align=middle><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_5.jpg\" width=160></TD> <TD  style=\"text-align:right\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_6.jpg\" width=160></TD></TR> <TR> <TD align=middle>高空清洁</TD> <TD align=middle>地毯清洗</TD> <TD align=middle>地面清洗</TD></TR> <TR> <TD  style=\"text-align:left\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_7.jpg\" width=160></TD> <TD align=middle><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_8.jpg\" width=160></TD> <TD  style=\"text-align:right\" ><IMG height=120 src=\"http://www.furuijinzhao.com/images/service_cleaning_9.jpg\" width=160></TD></TR> <TR> <TD align=middle>石材养护</TD> <TD align=middle>地板打蜡</TD> <TD align=middle>家庭保洁</TD></TR></TBODY></TABLE>","'.$date.'","96_60","'.$web['manager'].'","'.$topdate.'"),
(3,"物业公司","福瑞今朝企业服务有限公司在中国提供专业化的物业管理服务，致力于维护信誉和为有偿服务提供最大价值的回报。我们不断地评估服务质量标准，确保我们具有行业最高的管理服务标准。</p><p>凭借在物业管理、租赁管理和技术支持服务等方面的广泛专业优势，我们能够提高所有在我们管理下的物产的资本和租金价值。</p><p>我们的物业管理服务包括：</p><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#cleaning\">保洁服务</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#safe\">安全服务</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#engineering\">工程修理和维护</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#parking\">汽车停车场管理</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#facilities\">环境及公共设施维护</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#green\">景物、绿化管理</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#dining\">食堂餐饮管理</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#meeting\">会务接待及服务</a></div><div>·<a href=\"http://www.furuijinzhao.com/gb/property.html#materials\">物料配送</a></div>","'.$date.'","96_60","'.$web['manager'].'","'.$topdate.'"),
(4,"一站搜网","一站搜网，最好的分类信息、企业名录、友情链接、网址导航平台<br />欢迎光临：<A href=\"http://www.yzsou.com/\">http://www.162100.com/</A>","'.$date.'","96_60","'.$web['manager'].'","'.$topdate.'"),
(5,"162100简约论坛","<p>【程序简介】</p><p> 1、PHP+Mysql构架论坛，162100.com又一原创作品；<br />  2、短小精悍(安装包仅320KB)；<br />  3、无限制设立分论坛、栏目；<br />  4、允许过客发言设置；<br />  5、所见即所得的在线编辑器；<br />  6、随心更换的站点名称、logo，便于应用；<br />  7、轻松管理广告位；<br />  8、轻松管理友情链接功能；<br />  9、轻松在线直接修改文件功能；<br />  10、打开压缩包直接上传至支持PHP+Mysql的空间即可使用。  <br />  11、登录后点击管理员小钥匙图标进入后台→设置数据库→建立数据库表，完毕。<br />  12、管理后台地址：admin.php。基本管理员名称admin，基本管理员密码admin。数据库安装完毕后可更改数据库中的密码副本。<br />  13、使用反馈请至：http://www.162100.com/bbs</p><br />欢迎光临：<A href=\"http://www.162100.com/bbs\">http://www.162100.com/bbs</A>","'.$date.'","96_60","'.$web['manager'].'","'.$topdate.'");
';

  if(mysql_query($table_born)){ //创建表并判断
    mysql_query($table_into);
    $out.='<br />信息库表[yzsoulistdata]创建成功！';
  }else{
    echo mysql_errno() . ": " . mysql_error() . "\n";
    $out.='<br />信息库表[yzsoulistdata]创建失败！';
  }
}
unset($table_into,$table_into);





if($result=@mysql_query('SELECT * FROM yzsoureply',$db)){
  $out.='<br />回帖表[yzsoureply]已存在！是否：<a href="run.php?run=sql_table_del&table=yzsoureply" onclick="return confirm(\'确定删除回帖表[yzsoureply]么？\')" target="_blank">删除回帖表</a><br />';
  mysql_free_result($result);
}else{
  //建表语句（评论或回复）
  //题目ID|内容|日期|会员或IP
  $date=gmdate('Y/m/d H:i:s',time()+(floatval($web['time_pos'])*3600));
  $table_born='CREATE TABLE `yzsoureply`(
`id` int(10) NOT NULL auto_increment,
`r_id` varchar(40) NOT NULL default "",
`text` text NOT NULL,
`date` varchar(40) NOT NULL default "",
`author_ip` varchar(40) NOT NULL default "",
`other1` varchar(200) NOT NULL default "",
`other2` varchar(200) NOT NULL default "",
`other3` varchar(200) NOT NULL default "",
`other4` varchar(200) NOT NULL default "",
PRIMARY KEY (`id`)
) ENGINE=MyISAM'.$char.';';
  $table_into='INSERT INTO `yzsoureply`(`id`,`r_id`,`text`,`date`,`author_ip`) values
(1,"1","欢迎光临","'.$date.'","'.$web['manager'].'");

';

  if(mysql_query($table_born)){ //创建表并判断
    mysql_query($table_into);
    $out.='<br />回帖表[yzsoureply]创建成功！';
  }else{
    echo mysql_errno() . ": " . mysql_error() . "\n";
    $out.='<br />回帖表[yzsoureply]创建失败！';
  }
}
unset($table_into,$table_into);



if($result=@mysql_query('SELECT * FROM yzsoumember',$db)){
  $out.='<br />用户表[yzsoumember]已存在！是否：<a href="run.php?run=sql_table_del&table=yzsoumember" onclick="return confirm(\'确定删除用户表[yzsoumember]么？\')" target="_blank">删除用户表</a><br />';
  mysql_free_result($result);
}else{
  //建表语句（会员）
  //ID|用户名|密码|邮箱|密码问题|密码答案|权力|积分|发表数量|注册日期|最后访问
  //真名|性别|年龄|手机|座机|公司|地址|邮编|QQ|网址|置顶条数|置顶限期
  $table_born='CREATE TABLE `yzsoumember`(
`id` int(10) NOT NULL auto_increment,
`username` varchar(200) NOT NULL default "",
`password` varchar(200) NOT NULL default "",
`email` varchar(200) NOT NULL default "",
`password_question` varchar(200) NOT NULL default "",
`password_answer` varchar(40) NOT NULL default "",
`power` varchar(40) NOT NULL default "",
`point` int(10) NOT NULL default 0,
`writecount` int(10) NOT NULL default 0,
`regdate` varchar(40) NOT NULL default "",
`thisdate` varchar(40) NOT NULL default "",
`realname` varchar(40) NOT NULL default "",
`sex` varchar(40) NOT NULL default "",
`birthday` varchar(40) NOT NULL default "",
`handtel` varchar(20) NOT NULL default "",
`hometel` varchar(20) NOT NULL default "",
`company` varchar(200) NOT NULL default "",
`address` varchar(200) NOT NULL default "",
`zip` varchar(20) NOT NULL default "",
`qq` varchar(20) NOT NULL default "",
`sign` varchar(200) NOT NULL default "",
`topcount` varchar(20) NOT NULL default "",
`topdate` varchar(40) NOT NULL default "",
`other1` varchar(200) NOT NULL default "",
`other2` varchar(200) NOT NULL default "",
`other3` varchar(200) NOT NULL default "",
`other4` varchar(200) NOT NULL default "",
PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1'.$char.';';
  $table_into='INSERT INTO `yzsoumember`(`id`,`username`,`password`,`email`,`power`,`point`,`writecount`,`regdate`,`thisdate`) values
(1,"'.$web['manager'].'","'.$web['password'].'","162100.com@163.com","manager",10000,0,"'.($thisdate=gmdate('Y/m/d H:i:s',time()+(floatval($web['time_pos'])*3600))).'","'.$thisdate.'");

';

  if(mysql_query($table_born)){ //创建表并判断
    mysql_query($table_into);
    $out.='<br />用户表[yzsoumember]创建成功！';
  }else{
  echo mysql_errno() . ": " . mysql_error() . "\n";
    $out.='<br />用户表[yzsoumember]创建失败！';
  }
}
unset($table_into,$table_into);



err($out.'<a href="admin.php">进入admin.php</a>');


?>



