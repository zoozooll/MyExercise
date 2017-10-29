<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_areas.php");
require_once("../foundation/module_users.php");
//引入语言包
$a_langpackage=new adminlp;
//数据表定义区
$t_shop_info = $tablePreStr."shop_info";
$t_areas = $tablePreStr."areas";
$t_user_rank = $tablePreStr."user_rank";
$t_users = $tablePreStr."users";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
/* 地区信息 */
$areainfo = get_areas_kv($dbo,$t_areas);

$sql = "select * from `$t_shop_info` as a, `$t_users` as b where a.user_id=b.user_id";
$name = get_args('name');
if($name) {
	//权限管理
	$right=check_rights("shop_search");
	if(!$right){
		header('location:m.php?app=error');
	}
	$sql .= " and a.shop_name like '%$name%' ";
}
$sql .= " order by a.shop_creat_time desc";
$result = $dbo->fetch_page($sql,13);
//print_r($result);
$userrank = get_userrank_list($dbo,$t_user_rank);

$right_array=array(
    "shop_unsale"    =>   "0",
    "shop_update"    =>   "0",
    "shop_lock"    =>   "0",
    "shop_search"    =>   "0",
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
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_shop_mengament;?> &gt;&gt; <?php echo $a_langpackage->a_shop_list;?></div>
        <hr />
	<div class="seachbox">
        <div class="content2">
        	<form action="m.php?app=shop_list" name="searchForm" method="get">
            <table class="form-table">
            	<tbody>
            	  <tr>
                   	<td width="10px">
                   		<span style="margin:1px -5px 0px 0px; float:right; color: #000" >
                   			<img src="skin/images/icon_search.gif" border="0" alt="SEARCH" />
                   		</span>
                   	</td>
                    <td width="180px">
                    	<span style="margin:1px 2px 0px -10px; float:left; color:#000" >
                    		<?php echo $a_langpackage->a_shop_name;?>:
                    	</span>
                    	<input type="text" class="small-text" name="name" value="<?php echo $name; ?>" style="width:100px" />
						<input type="hidden" name="app" value="shop_list" />
                    </td>
                    <td>
                    <span style="margin:-2px 0px 0px -10px; float:left;" >
                    	<input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_serach;?>" />
                    </span>
                    </td>
                  </tr>
                </tbody>
            </table>
            </form>
        </div>
    </div>
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_shop_list;?></h3>
    <div class="content2">
			<form action="a.php?act=lock_shop" name="form1" id="form1" method="post">
			<table class="list_table">
			  <thead>
				<tr style="text-align: center;">
					<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
					<th width="40px"><?php echo $a_langpackage->a_ID;?></th>
					<th align="left"><?php echo $a_langpackage->a_shopkeeper;?>/<?php echo $a_langpackage->a_shop_name;?></th>
					<th width="120px"><?php echo $a_langpackage->a_shop_area;?></th>
					<th width="100px" align="left"><?php echo $a_langpackage->a_shop_range;?></th>
					<th width="60px"><?php echo $a_langpackage->a_shop_status;?></th>
					<th width="80px"><?php echo $a_langpackage->a_shop_approve;?></th>
					<th width="85px"><?php echo $a_langpackage->a_shop_time;?></th>
					<th width="61px"><?php echo $a_langpackage->a_operate;?></th>
				</tr>
			  </thead>
			  <tbody>
				<?php if($result['result']) {
				foreach($result['result'] as $value) { ?>
				<tr style="text-align:center;">
					<td width="2px"><input type="checkbox" name="shop_id[]" value="<?php echo $value['shop_id'];?>" /></td>
					<td width="40px"><?php echo $value['shop_id'];?>.</td>
					<td width="" align="left"><?php echo $value['user_name']?><br />
						<a href="../shop.php?shopid=<?php echo $value['shop_id'];?>&app=index" target="_blank"><?php echo $value['shop_name'];?></a>
						(<a href="m.php?app=goods_list&shopid=<?php echo $value['shop_id'];?>"><?php echo $a_langpackage->a_view_product;?></a>)
					</td>
					<td><?php echo $areainfo[$value['shop_province']].','.$areainfo[$value['shop_city']].','.$areainfo[$value['shop_district']];?></td>
					<td align="left"><?php echo $value['shop_management'];?></td>
					<td>
						<?php if($value['lock_flg']==0) {?>
						<?php echo $a_langpackage->a_open;?>
						<?php }else{?>
						<?php echo $a_langpackage->a_close;?>
						<?php }?>
						&nbsp;&nbsp;
					</td>
					<td><?php if($value['rank_id']==1) {echo "<span>".$a_langpackage->a_shop_pass_fail."</span>";} else {echo $userrank[$value['rank_id']]['rank_name'];} ?></td>
					<td><?php echo substr($value['shop_creat_time'],0,10);?></td>
					<td>
						<a href="a.php?act=shop_goodsdownsale&id=<?php echo $value['shop_id']; ?>" onclick="return confirm('<?php echo $a_langpackage->a_goods_out_sale_message1.$value['shop_name'].$a_langpackage->a_goods_out_sale_message2;?>');"><?php echo $a_langpackage->a_goods_out_sale;?></a><br />
						<a href="m.php?app=member_reinfo&id=<?php echo $value['user_id']; ?>"><?php echo $a_langpackage->a_update_rank;?></a><br />
						<?php if($value['lock_flg']==0){?>
						<a href="a.php?act=lock_shop&id=<?php echo $value['shop_id']; ?>&v=1" onclick="return confirm('<?php echo $a_langpackage->a_free_shop_mess;?>')"><?php echo $a_langpackage->a_lock_shop;?></a>
						<?php }else{?>
						<a href="a.php?act=lock_shop&id=<?php echo $value['shop_id']; ?>&v=0" ><?php echo $a_langpackage->a_free_shop;?></a>
						<?php }?>
					</td>
				</tr>
				<?php }?>
				<tr>
					<td colspan="15">
						<span style="margin:14px 14px 0px 0px; float:left;" >
							<select name="v">
								<option value="1"><?php echo $a_langpackage->a_lock_shop;?></option>
								<option value="0"><?php echo $a_langpackage->a_free_shop;?></option>
							</select>
						</span>
						<span class="button-container"><input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_exe_message;?>');" value="<?php echo $a_langpackage->a_batch_handle;?>" /></span>
						<span class="button-container"><input class="regular-button" type='button' onclick='jump_diff("a.php?act=shop_goodsdownsale");' value='<?php echo $a_langpackage->a_batch_down;?>' /></span>
					</td>
				</tr>
				<?php } else { ?>
				<tr>
					<td colspan="9"><?php echo $a_langpackage->a_no_list;?></td>
				</tr>
				<?php } ?>
				<tr>
					<td colspan="9"><?php include("m/page.php"); ?></td>
				</tr>
				</tbody>
			</table>
			</form>
		</div>
	  </div>
	</div>
</div>
</body>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
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
function jump_diff(link_href){
	document.getElementById('form1').action=link_href;
	document.getElementById('form1').submit();
}
//-->
</script>
</html>