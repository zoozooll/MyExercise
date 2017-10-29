<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/cart.html
 * 如果您的模型要进行修改，请修改 models/modules/user/cart.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/user/cart.html") > filemtime(__file__) || (file_exists("models/modules/user/cart.php") && filemtime("models/modules/user/cart.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/cart.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入文件
require_once("foundation/fsqlitem_set.php");
require_once("foundation/module_goods.php");
//引入语言包
$m_langpackage=new moduleslp;

$k = short_check(get_args('k'));
$cat = intval(get_args('cat'));

//数据表定义区
$t_cart = $tablePreStr."cart";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$user_id = get_sess_user_id();
//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
$sql="SELECT goods_id FROM $t_cart WHERE user_id='$user_id'";
$rs = $dbo->getRs($sql);
$arr = array();
foreach ($rs as $k=>$v){
	$arr[]=$v['goods_id'];
}
$goods_ids="";
$dbo = new dbex;
dbtarget('w',$dbServs);
if (isset($_SESSION['cart'])) {
	foreach ($_SESSION['cart'] as $key=>$value){
		if ($user_id) {
			if (!in_array($key,$arr)) {
				$insert_array = array(
					'user_id' => $user_id,
					'goods_id' => $key,
					'goods_number' => $_SESSION['cart'][$key]['num'],
					'add_time' => $ctime->long_time(),
				);
				$goods_info = get_goods_info($dbo,$t_goods,array('goods_name','goods_price','goods_number'),$key);
				$item_sql = get_insert_item($insert_array);
				$sql = "insert into `$t_cart` $item_sql ";
				if($dbo->exeUpdate($sql)) {
					$new_goods_num = $goods_info['goods_number'] - $_SESSION['cart'][$key]['num'];
					$sql = "update `$t_goods` set goods_number='$new_goods_num' where goods_id='$key'";
					$dbo->exeUpdate($sql);
				}
			}
		}
		$goods_ids.="($key,";
	}
}
$goods_ids = substr($goods_ids,0,-1);
$goods_ids.=")";
//读写分离定义方法
if ($user_id) {
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	
	//$sql = "select * from `$t_cart` where user_id='$user_id'";
	//
	//$sql .= " order by add_time desc";
	//$result = $dbo->fetch_page($sql,13);
	$sql = "SELECT c.user_id,a.goods_id,a.cart_id,a.add_time,a.goods_number,b.shop_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_cart` AS a, `$t_goods` AS b, `$t_shop_info` as c WHERE a.goods_id=b.goods_id AND b.shop_id=c.shop_id AND a.user_id='$user_id'";
	$sql .= " order by a.add_time desc";
	$result = $dbo->fetch_page($sql,10);
}else{
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	$sql = "SELECT c.user_id,b.shop_id,b.goods_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_goods` AS b, `$t_shop_info` as c WHERE b.shop_id=c.shop_id AND b.goods_id IN $goods_ids";
	$sql .= " order by b.goods_id desc";
	$result = $dbo->fetch_page($sql,10);
	foreach ($result['result'] as $k=>$v){
		$result['result'][$k]['add_time']=$_SESSION['cart'][$v['goods_id']]['add_time'];
		$result['result'][$k]['goods_number']=$_SESSION['cart'][$v['goods_id']]['num'];
		$result['result'][$k]['cart_id']=0;
	}
}
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.edit span{background:#efefef;}
.search {margin:5px;}
.search input {color:#444;}
td.img img{cursor:pointer}
</style>
</head>
<body>
<form action="do.php?act=user_cart_del" name="form" method="post" onsubmit="return submitform();">
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
        <div class="title_uc"><h3><?php echo $m_langpackage->m_cart;?></h3></div>
		<table width="98%" class="form_table">
			<tr class="center">
				<th width="20"><input type="checkbox" onclick="checkall(this);" /></th>
				<th width="90"><?php echo  $m_langpackage->m_goods_image;?></th>
				<th><?php echo  $m_langpackage->m_goods_info;?></th>
				<th><?php echo  $m_langpackage->m_buy_num;?></th>
				<th width="30"><?php echo  $m_langpackage->m_manage;?></th></tr>
			<tbody>
			<?php if(!empty($result['result'])) {
				foreach($result['result'] as $v) {?>
			<tr>
				<td><input type="checkbox" name="goods[]" value="<?php echo  $v['cart_id'];?>" /></td>
				<td class="center"><a href="<?php echo  goods_url($v['goods_id']);?>" target="_blank"><img src="<?php echo  $v['goods_thumb'];?>" width="80" height="80"></a></td>
				<td class="textleft"><a href="<?php echo  goods_url($v['goods_id']);?>" target="_blank" style="font-size:14px; font-weight:bold; color:#0044DD"><?php echo  $v['goods_name'];?></a>
				<br /> <?php echo  $m_langpackage->m_order_shops;?>：<a href="<?php echo shop_url($v['shop_id']);?>" target="_blank" style="color:#0044DD;"><?php echo  $v['shop_name'];?></a> &nbsp;&nbsp; <?php echo  $m_langpackage->m_price;?>：￥<span style="color:#FF6600; font-weight:bold;"><?php echo  $v['goods_price'];?></span><?php echo  $m_langpackage->m_yuan;?>
				<br /> <?php echo  $m_langpackage->m_add_time;?>：<?php echo  substr($v['add_time'],0,16);?> &nbsp;&nbsp; <script src="imshow.php?u=<?php echo  $v['user_id'];?>"></script>
				</td>
				<td class="center edit"  id="goodssortid_<?php echo  $v['cart_id'];?>">
					<span onclick="edit_sort(this,<?php echo  $v['cart_id'];?>)">&nbsp;<?php echo  $v['goods_number'];?>&nbsp;</span></td>
				<td class="center"><a href="do.php?act=user_cart_del&id=<?php echo  $v['cart_id'];?><?php if($v['cart_id']==0){?>&goods_id=<?php echo $v['goods_id'];?><?php }?>" onclick="return confirm('<?php echo  $m_langpackage->m_sure_delcartgoods;?>');"><?php echo  $m_langpackage->m_del;?></a><br />
				<a id="num_<?php echo  $v['cart_id'];?>" href="modules.php?app=user_order&gid=<?php echo  $v['goods_id'];?>&v=<?php echo  $v['goods_number'];?>"><?php echo  $m_langpackage->m_ccbuy;?></a></td>
			</tr>
			<?php }?>
			<tr><td colspan="5" class="textleft"><INPUT onclick="return confirm('<?php echo $m_langpackage->m_manage_sure_del;?>');" type=submit value=<?php echo $m_langpackage->m_pl_del;?> name=deletesubmit> </td></tr>
			<tr><td colspan="5" class="center"><?php  require("modules/page.php");?></td></tr>
			<?php  } else {?>
			<tr><td colspan="5" class="center"><?php echo  $m_langpackage->m_nolist_record;?></td></tr>
			<?php }?>
			</tbody>
		</table>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</form>
</body>
</html>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function toggle_show(obj,id) {
	var re = /yes/i;
	var src = obj.src;
	var isshow = 1;
	var sss = src.search(re);
	if(sss > 0) {
		isshow = 0;
	}
	ajax("do.php?act=shop_isshow_toggle","POST","id="+id+"&s="+isshow,function(data){
		if(data) {
			obj.src = 'skin/default/images/'+data+'.gif';
		}
	});
}

var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("goods");
	var checknum = 0;
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=='checkbox') {
			if(inputs[i].checked) {
				checknum++;
			}
		}
	}
	if(checknum==0) {
		alert("<?php echo $m_langpackage->m_selceted_one;?>");
		return false;
	}
	return true;
}
function checkall(obj) {
	if(obj.checked) {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = true;
			}
		}
	} else {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = false;
			}
		}
	}
}

var sort_value,number_value,price_value;
function edit_sort(span,id) {
	obj = document.getElementById("goodssortid_"+id);
	sort_value = span.innerHTML;
	sort_value = sort_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:35px" type="text" id="input_goodssortid_' + id + '" value="' + sort_value + '" onblur="edit_sort_post(this,' + id + ')"  maxlength="2" />';
	document.getElementById("input_goodssortid_"+id).focus();
}

function edit_sort_post(input,id) {
	var obj = document.getElementById("goodssortid_"+id);
	var num = document.getElementById("num_"+id);
	var re = /v=[0-9]+/g;
	if(isNaN(input.value)) {
		alert("<?php echo $m_langpackage->m_input_numpl;?>");
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
		return ;
	}
	if(sort_value==input.value) {
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=goods_num_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				num.href = num.href.replace(re, "v="+input.value);
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
			}
		});
	}
}
//-->
</script><?php } ?>