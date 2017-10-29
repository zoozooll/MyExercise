<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_goods = $tablePreStr."goods";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$name = short_check(get_args('name'));
$best = intval(get_args('best'));
$new = intval(get_args('new'));
$hot = intval(get_args('hot'));
$promote = intval(get_args('promote'));
$sale = intval(get_args('sale'));
$shop_id = intval(get_args('shopid'));

$sql = "select * from `$t_goods` where 1 ";
//权限管理
$right=check_rights("goods_search");

if($name) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and goods_name like '%$name%' ";
	}
}
if($best) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and is_best=1 ";
	}
}
if($new) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and is_new=1 ";
	}
}
if($hot) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and is_hot=1 ";
	}
}
if($promote) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and is_promote=1 ";
	}
}
if($sale) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and is_on_sale=1 ";
	}
}
if ($shop_id){
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and shop_id ='$shop_id' ";
	}
}

$sql .= " order by add_time desc;";

$result = $dbo->fetch_page($sql,13);

$right_array=array(
	"goods_search"    =>   "0",
    "goods_on_sale"    =>   "0",
    "goods_best"    =>   "0",
    "goods_new"    =>   "0",
    "goods_hot"    =>   "0",
    "goods_promote"    =>   "0",
    "goods_look"    =>   "0",
    "goods_lock"    =>   "0",
    "goods_unlock"    =>   "0",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
td span {color:red;}
.green {color:green;}
.red {color:red;}
td img {cursor:pointer;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_aboutgoods_management;?> &gt;&gt; <?php echo $a_langpackage->a_goods_list; ?></div>
        <hr />
	<div class="seachbox">
        <div class="content2">
        	<form action="m.php?app=goods_list" name="searchForm" method="get">
				<table class="form_table">
				  <tbody>
				  	<tr>
				  		<td width="20px">
							<img src="skin/images/icon_search.gif" border="0" alt="SEARCH" />
							<input type="hidden" id="on_sale" value="<?php echo $right_array['goods_on_sale']?>">
							<input type="hidden" id="best" value="<?php echo $right_array['goods_best']?>">
							<input type="hidden" id="new" value="<?php echo $right_array['goods_new']?>">
							<input type="hidden" id="hot" value="<?php echo $right_array['goods_hot']?>">
							<input type="hidden" id="promote" value="<?php echo $right_array['goods_promote']?>">
							<input type="hidden" id="shop_id" value="<?php echo $shop_id?>">
						</td>
						<td width="180px">
							<span style="margin:1px 0px 0px 0px; float:left; color: #000" >
								<?php echo $a_langpackage->a_goods_name; ?>:
							</span>
							<input class="small-text" type="text" name="name" value="<?php echo $name; ?>" />
						</td>
					    <td width="50px">
					    	<?php echo $a_langpackage->a_best; ?><input type="checkbox" name="best" value="1" <?php if($best) echo "checked";?> />
						</td>
						<td width="50px">
							<?php echo $a_langpackage->a_new; ?><input type="checkbox" name="new" value="1" <?php if($new) echo "checked";?> />
						</td>
						<td width="50px">
							<?php echo $a_langpackage->a_hot; ?><input type="checkbox" name="hot" value="1" <?php if($hot) echo "checked";?> />
						</td>
						<td width="50px">
							<?php echo $a_langpackage->a_promote; ?><input type="checkbox" name="promote" value="1" <?php if($promote) echo "checked";?> />
						</td>
						<td width="50px">
						<?php echo $a_langpackage->a_on_sale; ?><input type="checkbox" name="sale" value="1" <?php if($sale) echo "checked";?> />
							<input type="hidden" name="app" value="goods_list" />
						</td>
						<td>
							<input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_search; ?>" />
						</td>
				    </tr>
				  </tbody>
				</table>
            </form>
        </div>
    </div>
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_goods_list; ?></h3>
    <div class="content2">
		<form action="a.php?act=lock_goods" name="form1" method="post">
		<table class="list_table">
		  <thead>
			<tr style="text-align: center;">
				<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="20px">ID</th>
				<th align="left" width=""><?php echo $a_langpackage->a_goods_name; ?></th>
				<th width="80px"><?php echo $a_langpackage->a_goods_price; ?></th>
				<th width="60px"><?php echo $a_langpackage->a_transport_price; ?></th>
				<th width="60px"><?php echo $a_langpackage->a_goods_number; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_on_sale; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_best; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_new; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_hot; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_promote; ?></th>
				<th width="45px"><?php echo $a_langpackage->a_goods_pv; ?></th>
				<th width="90px"><?php echo $a_langpackage->a_add_time; ?></th>
				<th width="40px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align: center;">
				<td width="2px"><input type="checkbox" name="goods_id[]" value="<?php echo $value['goods_id'];?>" /></td>
				<td width="20px"><?php echo $value['goods_id'];?>.</td>
				<td style=" text-align:left;" width=""><a href="../goods.php?id=<?php echo $value['goods_id'];?>" target="_blank"><?php echo $value['goods_name'];?></a></td>
				<td style=" text-align:center;" width="80px"><?php echo $value['goods_price'];?></td>
				<td style=" text-align:center;" width="60px"><?php echo $value['transport_price'];?></td>
				<td style=" text-align:center;" width="60px"><?php echo $value['goods_number'];?></td>
				<td style=" text-align:center;" width="35px">
					<?php if($value['is_on_sale']) { ?><img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'on_sale','<?php echo $value['goods_id']; ?>')" /><?php } else { ?><img src="../skin/default/images/no.gif" onclick="toggle_show(this,'on_sale','<?php echo $value['goods_id']; ?>')" /><?php } ?>
				</td>
				<td style=" text-align:center;" width="35px">
					<?php if($value['is_best']) { ?><img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'best','<?php echo $value['goods_id']; ?>')" /><?php } else { ?><img src="../skin/default/images/no.gif" onclick="toggle_show(this,'best','<?php echo $value['goods_id']; ?>')" /><?php } ?>
				</td>
				<td style=" text-align:center;" width="35px">
					<?php if($value['is_new']) { ?><img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'new','<?php echo $value['goods_id']; ?>')" /><?php } else { ?><img src="../skin/default/images/no.gif" onclick="toggle_show(this,'new','<?php echo $value['goods_id']; ?>')" /><?php } ?>
				</td>
				<td style=" text-align:center;" width="35px">
					<?php if($value['is_hot']) { ?><img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'hot','<?php echo $value['goods_id']; ?>')" /><?php } else { ?><img src="../skin/default/images/no.gif" onclick="toggle_show(this,'hot','<?php echo $value['goods_id']; ?>')" /><?php } ?>
				</td>
				<td style=" text-align:center;" width="35px">
					<?php if($value['is_promote']) { ?><img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'promote','<?php echo $value['goods_id']; ?>')" /><?php } else { ?><img src="../skin/default/images/no.gif" onclick="toggle_show(this,'promote','<?php echo $value['goods_id']; ?>')" /><?php } ?>
				</td>
				<td style=" text-align:center;" width="68px"><?php echo $value['pv'];?></td>
				<td style=" text-align:center;" width="90px"><?php echo substr($value['add_time'],0,10);?></td>
				<td style=" text-align:center;" width="40px">
					<?php if($right_array['goods_look']){?>
					<a href="../goods.php?id=<?php echo $value['goods_id'];?>" target="_blank"><?php echo $a_langpackage->a_view; ?></a><br />
					<?php }?>
					<?php if($value['lock_flg']==0){?>
					<a href="a.php?act=lock_goods&id=<?php echo $value['goods_id']; ?>&v=1" onclick="return confirm('<?php echo $a_langpackage->a_operate_message;?>')"><?php echo $a_langpackage->a_lock; ?></a><br />
					<?php }else{?>
					<a href="a.php?act=lock_goods&id=<?php echo $value['goods_id']; ?>&v=0" ><?php echo $a_langpackage->a_unlock; ?></a>
					<?php }?>
					</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="15">
					<span style="margin:14px 14px 0px 0px; float:left;" >
						<select name="v">
							<option value="1"><?php echo $a_langpackage->a_lock; ?></option>
							<option value="0"><?php echo $a_langpackage->a_unlock; ?></option>
						</select>
					</span>
					<span class="button-container"><input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_operate_message;?>');" value="<?php echo $a_langpackage->a_batch_handle;?>" /></span>
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="13"><?php echo $a_langpackage->a_no_list; ?></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="13" class="center"><?php include("m/page.php"); ?></td>
			</tr>
			</tbody>
		</table>
		</form>
	   </div>
	  </div>
	</div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
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
function toggle_show(obj,name,id) {
	var right=document.getElementById(name).value;
	if(right != '0'){
		var re = /yes/i;
		var src = obj.src;
		var s = 1;
		var searchv = src.search(re);
		if(searchv > 0) {
			s = 0;
		}
		var rights=document.getElementById(name).value;
		ajax("a.php?act=goods_toggle","POST","id="+id+"&s="+s+"&name="+name,function(data){
			if(data) {
				obj.src = '../skin/default/images/'+data+'.gif';
			}
		});
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}

}
//-->
</script>
</body>
</html>