<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_shop_request = $tablePreStr."shop_request";
$t_users = $tablePreStr."users";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$s = intval(get_args('s'));

$sql = "select * from `$t_shop_request` where 1";
if($s) {
	//权限管理
	$right=check_rights("company_search");
	if(!$right){
		header('location:m.php?app=error');
	}
	$status = $s;
	if($s=='100') { $status=0; }
	$sql .= " and status='$status' ";
}

$sql .= " order by add_time desc";

$result = $dbo->fetch_page($sql,13);

$right_array=array(
	"company_search"    =>   "0",
    "company_oper"    =>   "0",
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <?php echo $a_langpackage->a_m_check_commember;?></div>
        <hr />
        <div class="seachbox">
            <div class="content2">
            	<form action="m.php?app=shop_request" name="searchForm" method="get">
                <table class="form-table">
                	<tbody>
                	<tr>
                		<td width="10px"><img src="skin/images/icon_search.gif" border="0" alt="SEARCH" /></td>
                       	<td width="36px">
                       		<?php echo $a_langpackage->a_status;?>：
                       	</td>
                       	<td width="50px">
                       		<select name="s">
							<option value='0' <?php if($s=='0'){echo "selected";} ?> ><?php echo $a_langpackage->a_show_all;?></option>
							<option value='1' <?php if($s=='1'){echo "selected";} ?>><?php echo $a_langpackage->a_audit_succ;?></option>
							<option value='100' <?php if($s=='100'){echo "selected";} ?>><?php echo $a_langpackage->a_audit_wait;?></option>
							<option value='2' <?php if($s=='2'){echo "selected";} ?>><?php echo $a_langpackage->a_audit_refuse;?></option>
							</select>
							<input type="hidden" name="app" value="shop_request" />
                       	</td>
                        <td ><span class="button-container"><input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_serach;?>" /></span></td>
                      </tr>
                    </tbody>
                </table>
                </form>
            </div>
        </div>
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_m_check_commember;?></h3>
    <div class="content2">
		<form action="a.php?act=shop_request" name="form1" method="post">
		<table class="list_table">
		  <thead>
			<tr style="text-align:center;">
				<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="20px"><?php echo $a_langpackage->a_ID;?></th>
				<th width="" align="left"><?php echo $a_langpackage->a_company_name;?></th>
				<th width="145px"><?php echo $a_langpackage->a_certificate_kind;?>/<?php echo $a_langpackage->a_certificate_code;?></th>
				<th width="55px"><?php echo $a_langpackage->a_trading_license;?></th>
				<th width="121px"><?php echo $a_langpackage->a_company_time;?></th>
				<th width="60px"><?php echo $a_langpackage->a_status;?></th>
				<th width="70px"><?php echo $a_langpackage->a_operate;?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align:center;">
				<td><input type="checkbox" name="request_id[]" value="<?php echo $value['request_id'];?>" /></td>
				<td><?php echo $value['request_id'];?>.</td>
				<td align="left"><?php echo $value['company_name'];?></td>
				<td><?php echo $value['credit_type'];?><br /><?php echo $value['credit_num'];?></td>
				<td><a href="../<?php echo $value['credit_commercial'];?>" target="_blank"><?php echo $a_langpackage->a_look;?></a></td>
				<td><?php echo substr($value['add_time'],0,16);?></td>
				<td><?php if($value['status']==1) {echo "<span class='green'>".$a_langpackage->a_audit_successed."</span>";}
					elseif($value['status']==2) {echo "<span class='red'>".$a_langpackage->a_audit_fail."</span>";}
					else {echo $a_langpackage->a_audit_wait;} ?>
				</td>
				<td class="center">
				<a href="m.php?app=shop_request_view&id=<?php echo $value['request_id']; ?>" ><?php echo $a_langpackage->a_look;?></a><br />
				<?php if($value['status']!=1) { ?>
				<a href="a.php?act=shop_request&s=1&id=<?php echo $value['request_id']; ?>"><?php echo $a_langpackage->a_shop_pass_success;?></a>
				<a href="a.php?act=shop_request&s=2&id=<?php echo $value['request_id']; ?>"><?php echo $a_langpackage->a_shop_pass_fail;?></a>
				<?php } ?>
				</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="15">
				<span style="margin:14px 14px 0px 0px; float:left;" >
					<select name="s">
						<option value="1"><?php echo $a_langpackage->a_shop_pass_success;?></option>
						<option value="2"><?php echo $a_langpackage->a_no_pass_success;?></option>
					</select>
				</span>
					<span class="button-container"><input type="submit" name="" class="regular-button"  onclick="return confirm('<?php echo $a_langpackage->a_exe_message;?>');" value="<?php echo $a_langpackage->a_batch_handle;?>" /></span>
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="15"><?php echo $a_langpackage->a_no_list;?></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="15"><?php include("m/page.php"); ?></td>
			</tr>
		</table>
		</form>
	   </div>
	  </div>
	</div>
</div>
<script>
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
</script>
</body>
</html>