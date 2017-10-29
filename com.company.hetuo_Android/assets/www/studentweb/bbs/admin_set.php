<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 修改系统参数 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" type="text/javascript">
<!--
// 只允许输入数字
function isDigit(obj,starVal){
  if(!/^[\d\.]+$/.test(obj.value)){
    alert("你输入的值不对，只允许输入数字！");
    obj.value=starVal;
  }
}
-->
</script>
<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
-->
</style>
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">
<?php
require('inc/set_area.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()==true && $cookie[0]==$web['manager']){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='修改系统参数';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'<br />
<br />
';
?>

<br /><b>提示：</b>
<br />以下信息必须认真填写，必须
<br />尽量不要用特殊符号，如 \ : ; * ? ' &lt; &gt; | ，必免导致错误。
<br />
<form action="run.php?run=admin_set" method="post">
<ul>
  <div class="title2"><b>基本设置</b></div>
  <li>站点名称 <input type="text" name="sitename" value="<?php echo $web['sitename']; ?>" size="30" maxlength="" /></li>
  <li>站点简介<br /><textarea name="description" cols="40" rows="3"><?php echo $web['description']; ?></textarea></li>
  <li>关键字<br /><textarea name="keywords" cols="40" rows="3"><?php echo $web['keywords']; ?></textarea></li>
  <li>站点logo：请将制作好的logo图片制为gif格式，尺寸为200px×60px，存到images目录中。即：images/logo.gif</li>
  <li>基础管理员名称 <input type="text" name="manager" value="<?php echo $web['manager']; ?>" size="30" />，如还想加管理员请到“管理会员”中设置</li>
  <li>基础管理员密码 <input type="text" name="password" value="<?php echo $web['password']; ?>" size="30" />，如想更改请在数据库设置好后到“用户中心”对付本更改<br /><span style="color:red">提示：系统出现一切故障时再登录以此基础管理员名称和密码为准</span></li>
  <li>服务器时区调整 <select name="time_pos" id="time_pos">
<option value="-12国际日期变更线西">(GMT-12.00)国际日期变更线西</option>
<option value="-11中途岛，萨摩亚群岛">(GMT-11.00)中途岛，萨摩亚群岛</option>
<option value="-10夏威夷">(GMT-10.00)夏威夷</option>
<option value="-9阿拉斯加">(GMT-9.00)阿拉斯加</option>
<option value="-8太平洋时间（美国和加拿大）；蒂华纳">(GMT-8.00)太平洋时间（美国和加拿大）；蒂华纳</option>
<option value="-7奇瓦瓦，拉巴斯，马扎特兰">(GMT-7.00)奇瓦瓦，拉巴斯，马扎特兰</option>
<option value="-7山地时间（美国和加拿大）">(GMT-7.00)山地时间（美国和加拿大）</option>
<option value="-7亚利桑那">(GMT-7.00)亚利桑那</option>
<option value="-6瓜达拉哈拉，墨西哥城，蒙特雷">(GMT-6.00)瓜达拉哈拉，墨西哥城，蒙特雷</option>
<option value="-6萨斯喀彻温">(GMT-6.00)萨斯喀彻温</option>
<option value="-6中部时间（美国和加拿大）">(GMT-6.00)中部时间（美国和加拿大）</option>
<option value="-6中美洲">(GMT-6.00)中美洲</option>
<option value="-5波哥大，利马，基多">(GMT-5.00)波哥大，利马，基多</option>
<option value="-5东部时间（美国和加拿大）">(GMT-5.00)东部时间（美国和加拿大）</option>
<option value="-5印第安那州（东部）">(GMT-5.00)印第安那州（东部）</option>
<option value="-4大西洋时间（加拿大）">(GMT-4.00)大西洋时间（加拿大）</option>
<option value="-4加拉加斯，拉巴斯">(GMT-4.00)加拉加斯，拉巴斯</option>
<option value="-4圣地亚哥">(GMT-4.00)圣地亚哥</option>
<option value="-3纽芬兰">(GMT-3.00)纽芬兰</option>
<option value="-3巴西利亚">(GMT-3.00)巴西利亚</option>
<option value="-3布宜诺斯艾利斯，乔治敦">(GMT-3.00)布宜诺斯艾利斯，乔治敦</option>
<option value="-3格陵兰">(GMT-3.00)格陵兰</option>
<option value="-2中大西洋">(GMT-2.00)中大西洋</option>
<option value="-1佛得角群岛">(GMT-1.00)佛得角群岛</option>
<option value="-1亚速尔群岛">(GMT-1.00)亚速尔群岛</option>
<option value="0格林威治标准时间，都柏林，爱丁堡，伦敦，里斯本">(GMT)格林威治标准时间，都柏林，爱丁堡，伦敦，里斯本</option>
<option value="0卡萨布兰卡，蒙罗维亚">(GMT)卡萨布兰卡，蒙罗维亚</option>
<option value="1阿姆斯特丹，柏林，伯尔尼，罗马，斯德哥尔摩，维也纳">(GMT+1.00)阿姆斯特丹，柏林，伯尔尼，罗马，斯德哥尔摩，维也纳</option>
<option value="1贝尔格莱德，布拉迪斯拉发，布达佩斯，卢布尔雅那，布拉格">(GMT+1.00)贝尔格莱德，布拉迪斯拉发，布达佩斯，卢布尔雅那，布拉格</option>
<option value="1布鲁塞尔，哥本哈根，马德里，巴黎">(GMT+1.00)布鲁塞尔，哥本哈根，马德里，巴黎</option>
<option value="1萨拉热窝，斯科普里，华沙，萨格勒布">(GMT+1.00)萨拉热窝，斯科普里，华沙，萨格勒布</option>
<option value="1中非西部">(GMT+1.00)中非西部</option>
<option value="2布加勒斯特">(GMT+2.00)布加勒斯特</option>
<option value="2哈拉雷，比勒陀利亚">(GMT+2.00)哈拉雷，比勒陀利亚</option>
<option value="2赫尔辛基，基辅，里加，索非亚，塔林，维尔纽斯">(GMT+2.00)赫尔辛基，基辅，里加，索非亚，塔林，维尔纽斯</option>
<option value="2开罗">(GMT+2.00)开罗</option>
<option value="2雅典，贝鲁特，伊斯坦布尔，明斯克">(GMT+2.00)雅典，贝鲁特，伊斯坦布尔，明斯克</option>
<option value="2耶路撒冷">(GMT+2.00)耶路撒冷</option>
<option value="3巴格达">(GMT+3.00)巴格达</option>
<option value="3科威特，利雅得">(GMT+3.00)科威特，利雅得</option>
<option value="3莫斯科，圣彼得堡，伏尔加格勒">(GMT+3.00)莫斯科，圣彼得堡，伏尔加格勒</option>
<option value="3内罗毕">(GMT+3.00)内罗毕</option>
<option value="3德黑兰">(GMT+3.00)德黑兰</option>
<option value="4阿布扎比，马斯喀特">(GMT+4.00)阿布扎比，马斯喀特</option>
<option value="4巴库，第比利斯，埃里温">(GMT+4.00)巴库，第比利斯，埃里温</option>
<option value="4.5喀布尔">(GMT+4.30)喀布尔</option>
<option value="5叶卡捷琳堡">(GMT+5.00)叶卡捷琳堡</option>
<option value="5伊斯兰堡，卡拉奇，塔什干">(GMT+5.00)伊斯兰堡，卡拉奇，塔什干</option>
<option value="5.5马德拉斯，加尔各答，孟买，新德里">(GMT+5.30)马德拉斯，加尔各答，孟买，新德里</option>
<option value="5.75加德满都">(GMT+5.45)加德满都</option>
<option value="6阿拉木图，新西伯利亚">(GMT+6.00)阿拉木图，新西伯利亚</option>
<option value="6阿斯塔纳，达卡">(GMT+6.00)阿斯塔纳，达卡</option>
<option value="6斯里哈亚华登尼普拉">(GMT+6.00)斯里哈亚华登尼普拉</option>
<option value="6仰光">(GMT+6.30)仰光</option>
<option value="7克拉斯诺亚尔斯克">(GMT+7.00)克拉斯诺亚尔斯克</option>
<option value="7曼谷，河内，雅加达">(GMT+7.00)曼谷，河内，雅加达</option>
<option value="8北京，重庆，香港特别行政区，乌鲁木齐，台北">(GMT+8.00)北京，重庆，香港特别行政区，乌鲁木齐，台北</option>
<option value="8吉隆坡，新加坡">(GMT+8.00)吉隆坡，新加坡</option>
<option value="8珀斯">(GMT+8.00)珀斯</option>
<option value="8伊尔库茨克，乌兰巴图">(GMT+8.00)伊尔库茨克，乌兰巴图</option>
<option value="9大坂，东京，札幌">(GMT+9.00)大坂，东京，札幌</option>
<option value="9汉城">(GMT+9.00)汉城</option>
<option value="9雅库茨克">(GMT+9.00)雅库茨克</option>
<option value="9.5阿德莱德">(GMT+9.30)阿德莱德</option>
<option value="9.5达尔文">(GMT+9.30)达尔文</option>
<option value="10布里斯班">(GMT+10.00)布里斯班</option>
<option value="10符拉迪沃斯托克（海参崴）">(GMT+10.00)符拉迪沃斯托克（海参崴）</option>
<option value="10关岛，莫尔兹比港">(GMT+10.00)关岛，莫尔兹比港</option>
<option value="10霍巴特">(GMT+10.00)霍巴特</option>
<option value="10堪塔拉，墨尔本，悉尼">(GMT+10.00)堪塔拉，墨尔本，悉尼</option>
<option value="11马加丹，索罗门群岛，新喀里多尼亚">(GMT+11.00)马加丹，索罗门群岛，新喀里多尼亚</option>
<option value="12奥克兰，惠灵顿">(GMT+12.00)奥克兰，惠灵顿</option>
<option value="12斐济，堪察加半岛，马绍尔群岛">(GMT+12.00)斐济，堪察加半岛，马绍尔群岛</option>
<option value="13努库阿洛法">(GMT+13.00)努库阿洛法</option>
	</select>
<script language="javascript" type="text/javascript">
<!--
document.getElementById('time_pos').value='<?php echo $web['time_pos']; ?>';
-->
</script>
<br />
<font color=green>这是服务器时区时间：<?php echo $here_date=date('Y/m/d H:i:s'); ?></font><br />
<font color=blue>这是北京时区时间：<?php echo $beijing_date=gmdate('Y/m/d H:i:s',time()+(floatval($web['time_pos'])*3600)); ?></font><br />
<font color=red><?php if($here_date!=$beijing_date){ echo '二者时间差别为'.((strtotime($here_date)-strtotime($beijing_date))/3600).'小时，可根据此进行调整'; }else{ echo '二者相同'; } ?></font></li>
</ul>
<br />



<ul>
  <div class="title2"><b>控制设置</b></div>
  <li>禁止用户注册 <input type="checkbox" name="stop_reg" value="1"<?php echo $web['stop_reg']==1?' checked':''; ?> /></li>
  <li>每页显示数量 <input type="text" name="pagesize" value="<?php echo $web['pagesize']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['pagesize']; ?>')" /> 条/页</li>
  <li>文章发表最多字符数 <input type="text" name="list_wordcount" value="<?php echo $web['list_wordcount']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['list_wordcount']; ?>')" /></li>
  <li>评论回复最多字符数 <input type="text" name="re_wordcount" value="<?php echo $web['re_wordcount']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['re_wordcount']; ?>')" /></li>
  <li>过滤词汇（注：请用汉字、数字、字母填写。词汇间请用 / 分开。留空则不过滤）<br /><textarea name="badwords" cols="40" rows="3" onBlur="javascript:if(/[^a-z0-9\s\/]+/i.test(this.value.replace(/[^\x00-\xff]/g,'')) && this.value!=''){alert('要过滤的词汇不该填写除分隔作用的 / 之外的特殊符号！');this.focus();}" /><?php echo $web['badwords']; ?></textarea></li>
  <li>文章中允许加入链接起始分 <input type="text" name="link_start" value="<?php echo $web['link_start']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['link_start']; ?>')" /></li>  <li>限定上传图片基准尺寸 <input type="text" name="max_face_size_15" value="<?php echo $web['max_file_size'][15]; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['max_file_size'][15]; ?>')" /> KB</li>
  <li>限定上传动画基准尺寸 <input type="text" name="max_face_size_16" value="<?php echo $web['max_file_size'][16]; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['max_file_size'][16]; ?>')" /> KB</li>
  <li>限定上传影音文件基准尺寸 <input type="text" name="max_face_size_17" value="<?php echo $web['max_file_size'][17]; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['max_file_size'][17]; ?>')" /> KB</li>
  <li>限定上传其它文件基准尺寸 <input type="text" name="max_face_size_18" value="<?php echo $web['max_file_size'][18]; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['max_file_size'][18]; ?>')" /> KB</li>
  <li>设置允许上传的影音文件类型，用|分开写<br /><input type="text" name="_17_type" value="<?php echo $web['_17_type']; ?>" size="70" onKeyUp="javascript:if(/[^a-zA-Z0-9\|]+/.test(this.value) && this.value!=''){alert('允许上传的影音文件只应用合理的后缀名及分隔符|填写！');this.value='<?php echo $web['_17_type']; ?>';}" /></li>
  <li>设置允许上传的其它文件类型，用|分开写<br /><input type="text" name="_18_type" value="<?php echo $web['_18_type']; ?>" size="70" onKeyUp="javascript:if(/[^a-zA-Z0-9\|]+/.test(this.value) && this.value!=''){alert('允许上传的其它文件只应用合理的后缀名及分隔符|填写！');this.value='<?php echo $web['_18_type']; ?>';}" /></li>
</ul><br />


<ul>
  <div class="title2"><b>图片处理设置</b></div>
<?php
if(extension_loaded("gd")){
  if(!function_exists("gd_info"))
    echo "<font color=red size=3>你的gd版本很低哦，图片处理功能可能受到约束</font><br />";
}else
  echo "<font color=red size=3>你尚未加载gd库，图片处理功能可能受到约束</font>";
?>
  <li>图片加水印否 <input type="text" name="water" value="<?php echo $web['water']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['water']; ?>')" /> 1加 0不加</li>
  <li>缩略图尺寸 <input type="text" name="spic_w" value="<?php echo $web['spic_w']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['spic_w']; ?>')" />×<input type="text" name="spic_h" value="<?php echo $web['spic_h']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['spic_h']; ?>')" /></li>
  <li>水印文字 <input type="text" name="pic_markwords" size="30" value="<?php echo $web['pic_markwords']; ?>" />，留空则图片不加水印。<span style="color:red">注：在linux环境下使用图片处理功能（如加中文水印），请将字体文件一并上传至程序运行目录下。可到本地window系统中c:\windows\fonts\下去找，否则去下载。字体文件本程序默认选用simsun.ttc</span></li>
  <li>图片显示品质 <input type="text" name="pic_quality" value="<?php echo $web['pic_quality']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['pic_quality']; ?>')" />，默认75，最好100</li>
</ul><br />


<ul>
  <div class="title2"><b>发表设置</b></div>
  <li>允许过客发表 <input type="checkbox" name="guest_write" value="1"<?php echo $web['guest_write']==1?' checked':''; ?> /></li>
  <li>发表权限起始分 <input type="text" name="write_start" value="<?php echo $web['write_start']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['write_start']; ?>')" />如：禁止新人发表可设高一点分值</li>
  <li>上传权限起始分 <input type="text" name="up_start" value="<?php echo $web['up_start']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['up_start']; ?>')" /></li>
  <li>回复时将主题文章提前 <input type="checkbox" name="re_update" value="1"<?php echo $web['re_update']==1?' checked':''; ?> /> 否则自然顺序</li>
  <li>用户登录加分 <input type="text" name="loginadd" value="<?php echo $web['loginadd']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['loginadd']; ?>')" /></li>
  <li>用户发表加分 <input type="text" name="writeadd" value="<?php echo $web['writeadd']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['writeadd']; ?>')" /></li>
  <li>铁级用户等级分标准 <input type="text" name="class_iron" value="<?php echo $web['class_iron']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['class_iron']; ?>')" /></li>
  <li>银级用户等级分标准 <input type="text" name="class_silver" value="<?php echo $web['class_silver']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['class_silver']; ?>')" /></li>
  <li>金级用户等级分标准 <input type="text" name="class_gold" value="<?php echo $web['class_gold']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['class_gold']; ?>')" />，大于此数量的为钻级用户</li>
</ul>
<br />

<ul>
  <div class="title2"><b>广告设置</b></div>
  <li>置顶期限 <input type="text" name="top_expires" value="<?php echo $web['top_expires']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['top_expires']; ?>')" />，一般30天</li>
  <li>置顶每条每天价格 <input type="text" name="top_price" value="<?php echo $web['top_price']; ?>" size="5" onKeyUp="isDigit(this,'<?php echo $web['top_price']; ?>')" /></li>
</ul>
<br />



<br />
<br />
<input type="submit" value="设置完毕，提交" onClick="javascript:return confirm('确定提交吗？！');" />
</form>



<?php
}else{
  echo '<img src="images/i.gif" align="absmiddle" /> 请以基本管理员'.$web['manager'].'<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录</b></a>，以获得管理权限';
}

?>

    </td>
  </tr>
</table>
<br />
<br />
<br />
<br />

<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>
