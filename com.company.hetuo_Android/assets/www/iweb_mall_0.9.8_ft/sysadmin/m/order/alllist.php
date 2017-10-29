<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_payment.php");
//引入语言包
$a_langpackage=new adminlp;
//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_shop_info = $tablePreStr."shop_info";
$t_payment = $tablePreStr."payment";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$payid = short_check(get_args('payid'));
$pay_status = intval(get_args('pay_status'));
$transport_status = intval(get_args('transport_status'));
$pay_id = intval(get_args('pay_id'));
$order_status = intval(get_args('order_status'));

$sql = "select * from `$t_order_info` as a, `$t_shop_info` as b where a.shop_id=b.shop_id ";
//权限管理
$right=check_rights("order_search");

if($payid) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and a.payid like '%$payid%' ";
	}
}
if($pay_status) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and a.pay_status=1 ";
	}
}
if($transport_status) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and a.transport_status=1 ";
	}
}
if($pay_id) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and a.pay_id='$pay_id' ";
	}
}
if($order_status) {
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}else {
		$sql .= " and a.order_status='$order_status' ";
	}
}

$sql .= " order by a.order_time desc;";
$result = $dbo->fetch_page($sql,13);
$payment = get_payment_info($dbo,$t_payment);

$right_array=array(
	"order_search"    =>   "0",
    "order_browse"    =>   "0",
    "order_status"    =>   "0",
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
.black {color:#cccccc;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_m_order_mengament;?></a> &gt;&gt; <a href=""><?php echo $a_langpackage->a_alllist;?></a></div>
        <hr />
	<div class="seachbox">
        <div class="content2">
        	<form action="m.php?app=order_alllist" name="searchForm" method="get">
            <table class="form-table">
            	<tbody>
            	<tr>
                   	<td width="500px" colspan="2">
                   		<img src="skin/images/icon_search.gif" border="0" alt="SEARCH" />
                   		<?php echo $a_langpackage->a_orderID;?>：
                   		<input class="small-text" type="text" name="payid" value="<?php echo $payid; ?>" style="width:120px" />
                   	</td>
                </tr>
                <tr>
                    <td width="540px">
                    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<?php echo $a_langpackage->a_shop_gather_mode;?>：
                    	<select name="pay_id">
							<option value="0"><?php echo $a_langpackage->a_gather_mode_check;?></option>
							<?php foreach($payment as $value) { ?>
							<option value="<?php echo $value['pay_id']; ?>" <?php if($pay_id==$value['pay_id']){echo "selected";} ?> ><?php echo $value['pay_name'];?></option>
							<?php } ?>
						</select>
                    	<?php echo $a_langpackage->a_order_status;?>：
						<select name="order_status">
							<option value="0"><?php echo $a_langpackage->a_all;?></option>
							<option value="1" <?php if($order_status==1){echo "selected";} ?> ><?php echo $a_langpackage->a_customer_order;?></option>
							<option value="2" <?php if($order_status==2){echo "selected";} ?> ><?php echo $a_langpackage->a_shop_confirm_order;?></option>
							<option value="3" <?php if($order_status==3){echo "selected";} ?> ><?php echo $a_langpackage->a_customer_confirm_goods;?></option>
						</select>
						<?php echo $a_langpackage->a_pay;?><input type="checkbox" name="pay_status" value="1" <?php if($pay_status) echo "checked";?> />
						<?php echo $a_langpackage->a_send_goods;?><input type="checkbox" name="transport_status" value="1" <?php if($transport_status) echo "checked";?> />
						<input type="hidden" name="app" value="order_alllist">
					</td>
					<td><input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_serach;?>" /></td>
                  </tr>
                </tbody>
            </table>
           </form>
        </div>
    </div>
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_alllist;?></h3>
    <div class="content2">
		<form action="a.php?act=order_set_status" name="postForm" method="post" onsubmit="return submitform();">
		<table class="list_table">
			<thead>
			<tr style=" text-align:center">
				<th width="5px"><input type="checkbox" onclick="checkall(this);" /></th>
				<th width="115px"><?php echo $a_langpackage->a_orderID;?></th>
				<th width="" align="left"><?php echo $a_langpackage->a_goods;?>/<?php echo $a_langpackage->a_shop_name2;?></th>
				<th width="90px"><?php echo $a_langpackage->a_goods_price;?>/<?php echo $a_langpackage->a_trans_price;?></th>
				<th width="120px"><?php echo $a_langpackage->a_order_num;?>/<?php echo $a_langpackage->a_order_count;?></th>
				<th width="115px"><?php echo $a_langpackage->a_gather_mode;?>/<?php echo $a_langpackage->a_order_time;?></th>
				<th width="60px"><?php echo $a_langpackage->a_status;?></th>
				<th width="45px"><?php echo $a_langpackage->a_operate;?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style=" text-align:center">
				<td><input type="checkbox" name="order_id[]" value="<?php echo $value['order_id'];?>" /></td>
				<td><font size="-6"><?php echo $value['payid'];?></font></td>
				<td align="left">
					<a href="../goods.php?id=<?php echo $value['goods_id'];?>" target="_blank"><?php echo $value['goods_name'];?></a><br />
					<?php echo $a_langpackage->a_shop_name2;?>:<a href="../shop.php?shopid=<?php echo $value['shop_id'];?>" target="_blank"><?php echo $value['shop_name'];?></a>
				</td>
				<td><?php echo $value['goods_price'];?><br /><?php echo $value['transport_price'];?></td>
				<td><?php echo $value['order_num'];?><br /><?php echo $value['order_amount'];?></td>
				<td><?php echo $value['pay_name'];?><br /><?php echo substr($value['order_time'],0,16);?></td>
				<td>
				<?php if($value['order_status']==0){
					echo "<span class='black'>".$a_langpackage->a_order_cancelled."</span><br />";
				} elseif($value['order_status']==3) {
					echo "<span class='green'>".$a_langpackage->a_sales."</span><br />";
				} else {
					if($value['order_status']==1) {
						echo "<span class='red'>".$a_langpackage->a_unconfirm."</span><br />";
					} else {
						echo "<span class='green'>".$a_langpackage->a_confirm."</span><br />";
					}
					if($value['pay_status']) {
						echo "<span class='green'>".$a_langpackage->a_pay."</span><br />";
					} else {
						echo "<span class='red'>".$a_langpackage->a_not_pay."</span><br />";
					}
					if($value['transport_status']) {
						echo "<span class='green'>".$a_langpackage->a_send_goods."</span><br />";
					} else {
						echo "<span class='red'>".$a_langpackage->a_not_send_goods."</span>";
					}
				}?></td>
				<td>
					<a href="m.php?app=order_view&id=<?php echo $value['order_id'];?>"><?php echo $a_langpackage->a_look;?></a>
				</td>
			</tr>
			<?php }} else { ?>
			<tr>
				<td colspan="11"><?php echo $a_langpackage->a_no_list;?></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="11"><?php include("m/page.php"); ?></td>
			</tr>
			<tr>
				<td colspan="11">
				<span style="margin:14px 14px 0px 0px; float:left;" >
					<select name="status">
						<option value="0"><?php echo $a_langpackage->a_select;?><?php echo $a_langpackage->a_status;?></option>
						<option value="cancel"><?php echo $a_langpackage->a_cancell_order;?></option>
						<option value="pay"><?php echo $a_langpackage->a_payed;?></option>
						<option value="nopay"><?php echo $a_langpackage->a_unpayed;?></option>
						<option value="transport"><?php echo $a_langpackage->a_send_goods;?></option>
						<option value="notransport"><?php echo $a_langpackage->a_not_send_goods;?></option>
					</select>
				</span>
				<span class="button-container"><input class="regular-button" type="submit" name="pay" value="<?php echo $a_langpackage->a_set;?><?php echo $a_langpackage->a_status;?>" /></span>
				</td>
			</tr>
			</tbody>
		</table>
		</form>
		</div>
	  </div>
	</div>
</div>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("status")[0];
	var checknum = 0;
	if(status.value==0) {
		alert("<?php echo $a_langpackage->a_select_status;?>");
		return false;
	} else {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				if(inputs[i].checked) {
					checknum++;
				}
			}
		}
		if(checknum==0) {
			alert("<?php echo $a_langpackage->a_select_at_least;?>");
			return false;
		}
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
//-->
</script>
</body>
</html>