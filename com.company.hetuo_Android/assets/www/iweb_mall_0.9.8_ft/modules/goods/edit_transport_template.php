<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/goods/edit_transport_template.html
 * 如果您的模型要进行修改，请修改 models/modules/goods/edit_transport_template.php
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
if(filemtime("templates/default/modules/goods/edit_transport_template.html") > filemtime(__file__) || (file_exists("models/modules/goods/edit_transport_template.php") && filemtime("models/modules/goods/edit_transport_template.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/goods/edit_transport_template.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
	/*
	***********************************************
	*$ID:eidt_transport_template
	*$NAME:eidt_transport_template
	*$AUTHOR:E.T.Wei
	*DATE:Sat Apr 03 10:40:43 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require("foundation/module_goods.php");
	require("foundation/module_areas.php");
	//引入语言包
	$m_langpackage = new moduleslp;
	//数据表定义区
	$t_areas = $tablePreStr."areas";
	$t_transport_template = $tablePreStr."goods_transport";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$id = intval(get_args("id"));
	$sql = "SELECT * FROM $t_transport_template WHERE id='$id'";
	$transport_template_info = $dbo->getRow($sql);
	$transport_type = unserialize($transport_template_info['content']);
	foreach ($transport_type as $value){
		$transport_type[]=$value;
	}
	$arr = unserialize($transport_template_info['content']);
	//ems
	$emsstr='';
	if (isset($arr['ems'])&&count($arr['ems'])>1) {
		$emsarea=array();
		foreach ($arr['ems'] as $key=>$value){
			if(is_numeric($key)){
				$emsarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($emsarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$emsstr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordems[]' value='$area_name_list' onclick=\"eidtarea('ems','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destems[]' value='$idlist' />的";
			$emsstr.=$m_langpackage->m_transport_template_frist;
			$emsstr.="<input type='text' name='ord_area_fristems[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondems[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//pst
	$pststr='';
	if(isset($arr['pst'])&&count($arr['pst'])>1){
		$pstarea=array();
		foreach ($arr['pst'] as $key=>$value){
			if(is_numeric($key)){
				$pstarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($pstarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$pststr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordpst[]' value='$area_name_list' onclick=\"eidtarea('pst','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destpst[]' value='$idlist' />的";
			$pststr.=$m_langpackage->m_transport_template_frist;
			$pststr.="<input type='text' name='ord_area_fristpst[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondpst[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//ex
	$exstr='';
	if(isset($arr['ex'])&&count($arr['ex'])>1){
		$exarea=array();
		foreach ($arr['ex'] as $key=>$value){
			if(is_numeric($key)){
				$exarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($exarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$exstr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordex[]' value='$area_name_list' onclick=\"eidtarea('ex','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destex[]' value='$idlist' />的";
			$exstr.=$m_langpackage->m_transport_template_frist;
			$exstr.="<input type='text' name='ord_area_fristex[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondex[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//生成json
	$emsobj="''";
	if (isset($transport_type['ems'])) {
		$emsobj = json_encode($transport_type['ems']);
	}
	$exobj="''";
	if (isset($transport_type['ex'])) {
		$exobj = json_encode($transport_type['ex']);
	}
	$pstobj="''";
	if (isset($transport_type['pst'])) {
		$pstobj = json_encode($transport_type['pst']);
	}
	$area_list = get_area_list_bytype($dbo,$t_areas,1);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
</head>
<body onload="bodyonload()">
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=transport_template_list" style="color:#F67A06;"><?php echo $m_langpackage->m_list_transport_template;?></a>&nbsp;&nbsp;</span><?php echo  $m_langpackage->m_edit_transport_template;?></h3></div>
		<form action="do.php?act=edit_transport_template" method="post" name="form_transport_template" onsubmit="return checkForm(this);">
			<input type="hidden" name="id" value="<?php echo $id;?>" />
			<table id="boxtable" class="form_table">
				<tr>
					<td><?php echo  $m_langpackage->m_transport_name;?>:</td><td><input type="text" name="transport_name" value="<?php echo $transport_template_info['transport_name'];?>" /></td><td><?php echo $m_langpackage->m_transport_name_message;?></td>
				</tr>
				<tr>
					<td><?php echo  $m_langpackage->m_transport_description;?>:</td><td><input type="text" name="description" value="<?php echo $transport_template_info['description'];?>" /></td><td><?php echo $m_langpackage->m_transport_description_message;?></td>
				</tr>
				<tr>
					<td colspan="2"><b><?php echo  $m_langpackage->m_choose_transport_type;?></b></td>
					<td><?php echo  $m_langpackage->m_choose_transport_type_message;?></td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="transport_type[]" value="ems" id="ems" onclick="showtype('ems')" />EMS:</td>
				</tr>
				<tr id="ems_box" style="display:none;">
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;
						<?php echo $m_langpackage->m_transport_template_frist;?><input type="text" name="emsfrist" value="0.00" />
						<?php echo $m_langpackage->m_transport_template_second;?><input type="text" name="emssecond" value="0.00" /><br />
						<ul id="wordbox_ems">
						<?php echo $emsstr;?>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" onclick="showareslist('ems')" ><b><?php echo $m_langpackage->m_transport_cont;?></b></a>
					</td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="transport_type[]" value="pst" id="pst" onclick="showtype('pst')" /><?php echo  $m_langpackage->m_surface;?>:</td>
				</tr>
				<tr id="pst_box" style="display:none;">
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;
						<?php echo $m_langpackage->m_transport_template_frist;?><input type="text" name="pstfrist" value="0.00" />
						<?php echo $m_langpackage->m_transport_template_second;?><input type="text" name="pstsecond" value="0.00" /><br />
						<ul id="wordbox_pst">
							<?php echo $pststr;?>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" onclick="showareslist('pst')" ><b><?php echo $m_langpackage->m_transport_cont;?></b></a>
					</td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="transport_type[]" value="ex" id="ex" onclick="showtype('ex')" /><?php echo  $m_langpackage->m_express_delivery;?>:</td>
				</tr>
				<tr id="ex_box" style="display:none;">
					<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;
						<?php echo $m_langpackage->m_transport_template_frist;?><input type="text" name="exfrist" value="0.00" />
						<?php echo $m_langpackage->m_transport_template_second;?><input type="text" name="exsecond" value="0.00" /><br />
						<ul id="wordbox_ex">
							<?php echo $exstr;?>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" onclick="showareslist('ex')" ><b><?php echo $m_langpackage->m_transport_cont;?></b></a>
					</td>
				</tr>
				<tr>
					<td colspan="3"><input type="submit" name="sub" value="<?php echo $m_langpackage->m_shure;?>"  /></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="clear"></div>
</div>
<div id="bgdiv" style="display:none;"></div>
<div id="category_select" style="display:none;">
	<div class="category_title_1"><span onclick="hidebgdiv();"><?php echo $m_langpackage->m_close;?></span><?php echo $m_langpackage->m_plss_select_cateogry;?></div>
	<ul id="select_first" class="ulselect">
		<?php foreach($category_info as $k=>$v){?>
			<?php if($v['parent_id']==0){?>
			<li title="<?php echo  $v['cat_id'];?>"><?php echo  $v['cat_name'];?></li>
			<?php }?>
		<?php }?>
	</ul>
	<ul id="select_second" class="ulselect"></ul>
	<ul id="select_third" class="ulselect"></ul>
	<ul id="select_fourth" class="ulselect"></ul>
	<div class="category_com"><input type="button" value="<?php echo $m_langpackage->m_post;?>" onclick="postcatid();" /></div>
</div>
<div style="position:absolute;style:top:0px;left:0px; padding:5px; background:#f1f1f1; display:none; width:420px;" id="mydiv">
	<?php if(is_array($area_list)){?>
		<?php foreach($area_list as $k=>$value){?>
			<div style="float:left; width:75px;"><input type="checkbox" name="area[]" value="<?php echo $value['area_id'];?>"  /><?php echo $value['area_name'];?></div>
		<?php }?>
	<?php }?>
	<input type="hidden" name="hdnum" value="0" id="hdnum" />
	<input type="hidden" name="hdtxt" value="" id="hdtxt" />
	<input type="hidden" name="iseidt" id="iseidt" value="" />
	<input type="button" name="doselect" value="<?php echo $m_langpackage->m_shure;?>"  onclick="doselect()" />
</div>
<div class="footer"><?php require("modules/footer.php");?></div>
</body>
</html>
<script language="JavaScript" type="text/javascript">
	function showtype(txt){
		var chosid = txt+"_box";
		var actobj = document.getElementById(txt);
		var obj = document.getElementById(chosid);
		if(actobj.checked){
			obj.style.display="";
		}else{
			obj.style.display="none";
		}
	}
	function showareslist(txt){
		var obj = document.getElementById('mydiv');
		var areaobj = document.getElementsByName("area[]");
		var inputname = "ord_item_dest"+txt+"[]";
		var conobj = document.getElementsByName(inputname);
		var arrstr = "";
		for(i=0;i<conobj.length;i++){
			arrstr+=conobj[i].value;
		}
		var arrstr = arrstr.substring(0,arrstr.length-1);
		var strarr = arrstr.split(",");
		if(arrstr.length>0){
			for (a=0;a<areaobj.length;a++){
				for(b=0;b<strarr.length;b++){
					if(areaobj[a].value==strarr[b]){
						areaobj[a].disabled=true;
					}
				}
			}
		}
		document.getElementById('hdtxt').value=txt;

		obj.style.left = document.body.clientWidth/2-(parseInt(500)/2)+"px";
		obj.style.top = document.body.clientHeight/2-(parseInt(300)/2)-30+document.documentElement.scrollTop/2+"px";
		obj.style.display="";
	}
	function doselect(){
		var obj = document.getElementsByName("area[]");
		var str="";
		var num=document.getElementById('hdnum').value;
		var hdtxt=document.getElementById('hdtxt').value;
		var idlist ="";
		for(i=0;i<obj.length;i++){
			if(obj[i].checked){
				str+=obj[i].nextSibling.nodeValue+",";
				idlist+=obj[i].value+",";
			}
			obj[i].disabled=false;
		}
		var boxid = "wordbox_"+hdtxt;
		var boxobj = document.getElementById(boxid);
		if(document.getElementById('iseidt').value==''){
			if(str.length>0){
				boxobj.innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;<li >至<input type='text' name='ord_item_word"+(hdtxt)+"[]' value='"+str+"' "+"onclick=\"eidtarea('"+hdtxt+"','"+idlist+"','"+(num+1)+"')\""+" id='item"+(num+1)+"'  /><input type='hidden' id='itemvalue"+(num+1)+"' name='ord_item_dest"+(hdtxt)+"[]' value='"+idlist+"' />的<?php echo $m_langpackage->m_transport_template_frist;?><input type='text' name='ord_area_frist"+(hdtxt)+"[]' value='' /><?php echo $m_langpackage->m_transport_template_second;?><input type='text' name='ord_area_second"+(hdtxt)+"[]' value='' /></li>";

				document.getElementById('hdnum').value=parseInt(num)+1;
				for(i=0;i<obj.length;i++){
					obj[i].checked=false;
				}
			}
		}else{
			var chosid = "itemvalue"+document.getElementById('iseidt').value;
			var chostext = "item"+document.getElementById('iseidt').value;
			var idobj = document.getElementById(chosid);
			var txtobj = document.getElementById(chostext);
			idobj.value=idlist;
			txtobj.value=str;
		}
		document.getElementById('iseidt').value='';
		document.getElementById('mydiv').style.display="none";
	}
	function remove(obj,dx){
		if(isNaN(dx)||dx>obj.length){
			return false;
		}
		for(i=0,n=0;i<obj.length;i++){
			if(obj[i]!=obj[dx]){
				obj[n++]=obj[i];
			}
		}
		obj.length-=1;
	}
	function eidtarea(txt,ids,num){
		var obj = document.getElementById('mydiv');
		var areaobj = document.getElementsByName("area[]");
		var strarr = ids.split(",");
		document.getElementById('iseidt').value=num;
		showareslist(txt);
		for(i=0;i<areaobj.length;i++){
			areaobj[i].disabled=false;
			for(j=0;j<strarr.length;j++){
				if(areaobj[i].value==strarr[j]){
					areaobj[i].checked=true;
				}
			}
		}
	}
	function bodyonload(){
		var ems_obj = <?php echo $emsobj;?>;
		var ex_obj = <?php echo $exobj;?>;
		var pst_obj = <?php echo $pstobj;?>;
		if(ems_obj.frist>0){
			document.form_transport_template.emsfrist.value=ems_obj.frist;
			document.form_transport_template.emssecond.value=ems_obj.second;
			document.getElementById("ems").checked=true;
			document.getElementById("ems_box").style.display='';
		}
		if(ex_obj.frist>0){
			document.form_transport_template.exfrist.value=ex_obj.frist;
			document.form_transport_template.exsecond.value=ex_obj.second;
			document.getElementById("ex").checked=true;
			document.getElementById("ex_box").style.display='';
		}
		if(pst_obj.frist>0){
			document.form_transport_template.pstfrist.value=pst_obj.frist;
			document.form_transport_template.pstsecond.value=pst_obj.second;
			document.getElementById("pst").checked=true;
			document.getElementById("pst_box").style.display='';
		}
	}
	function checkForm(obj){
		var transport_type =document.getElementsByName('transport_type[]');
		var a=3;
		for(i=0;i<transport_type.length;i++){
			if(!transport_type[i].checked){
				a--;
			}
		}
		if(obj.transport_name.value==''){
			alert('<?php echo $m_langpackage->m_in_template_name;?>');
			return false;
		}
		if(obj.description.value==''){
			alert('<?php echo $m_langpackage->m_in_template_description;?>');
			return false;
		}
		if(a<=0){
			alert('<?php echo $m_langpackage->m_choose_shipping_method;?>');
			return false;
		}
		if(transport_type[0].checked&&(parseInt(obj.emsfrist.value)==0||parseInt(obj.emssecond.value)==0)){
			alert('<?php echo $m_langpackage->m_set_shipping_cost;?>');
			return false;
		}
		if(transport_type[1].checked&&(parseInt(obj.pstfrist.value)==0||parseInt(obj.pstsecond.value)==0)){
			alert('<?php echo $m_langpackage->m_set_shipping_cost;?>');
			return false;
		}
		if(transport_type[2].checked&&(parseInt(obj.exfrist.value)==0||parseInt(obj.exsecond.value)==0)){
			alert('<?php echo $m_langpackage->m_set_shipping_cost;?>');
			return false;
		}
	}
</script>
<?php } ?>