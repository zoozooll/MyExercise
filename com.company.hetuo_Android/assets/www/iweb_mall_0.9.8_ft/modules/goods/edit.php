<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/goods/edit.html
 * 如果您的模型要进行修改，请修改 models/modules/goods/edit.php
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
if(filemtime("templates/default/modules/goods/edit.html") > filemtime(__file__) || (file_exists("models/modules/goods/edit.php") && filemtime("models/modules/goods/edit.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/goods/edit.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_category.php");
require("foundation/module_goods.php");
require("foundation/module_attr.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_category = $tablePreStr."category";
$t_attribute = $tablePreStr."attribute";
$t_shop_category = $tablePreStr."shop_category";
$t_goods_attr = $tablePreStr."goods_attr";
$t_goods_transport = $tablePreStr."goods_transport";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";

$goods_id = intval(get_args('id'));

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$goods_info = get_goods_info($dbo,$t_goods,'*',$goods_id,$shop_id);
if(empty($goods_info)) {
	exit("非法操作！");
}
$transport_template_list = get_transport_template_list($dbo,$t_goods_transport);
$category_info = get_category_info($dbo,$t_category);
foreach($category_info as $value) {
	if($goods_info['cat_id']==$value['cat_id']) {
		$select_category_name = $value['cat_name'];
		break;
	}
}

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$goods_info['ucat_id']);

$attribute_info = array();
if($goods_info['cat_id']) {
	$attribute_info = get_attribute_info($dbo,$t_attribute,$goods_info['cat_id']);
}
$js_attribute_info = json_encode($attribute_info);

$goods_attr = get_goods_attr($dbo,$t_goods_attr,$goods_id);
/* 判断支付方式 */
$sql = "SELECT b.pay_id,b.pay_code FROM $t_shop_payment AS a, $t_payment AS b WHERE a.pay_id=b.pay_id AND a.shop_id=$shop_id AND a.enabled=1";
$isset_payment = $dbo->getRs($sql);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.red { color:red; }
.edit span{background:#efefef;}
.search {margin:5px;}
.search input {color:#444;}
.clear {clear:both;}
td{text-align:left;}
#bgdiv { background-color:#333; position:absolute; width:980px; left:0px; top:0px; opacity:0.4; filter:alpha(opacity=40); height:1000px; z-index:960}
#category_select { width:800px; z-index:961; position:absolute; filter:alpha(opacity=95); left:100px; top:160px; background-color:#fff; height:270px}
.category_title_1 {background:#FFE1C2; color:#F67A06; padding-left:10px; line-height:25px; font-weight:bold; font-size:14px;}
.category_title_1 span {float:right; padding-right:5px; cursor:pointer;}
.ulselect {width:198px; height:210px; overflow-x:hidden; overflow-y:scroll; border:1px solid #efefef; float:left;}
.ulselect li {line-height:21px; padding-left:5px; cursor:pointer;width:100%;float:left;text-align:left;}
.ulselect li:hover {background:#F6A248; color:#fff;}
.ulselect li.select {background:#F6A248; color:#fff;}
.category_com {height:30px; line-height:30px; text-align:center;}
.attr_class { background:#FFF2E6; }
.attr_class div.div {border:2px solid #fff; padding:3px;}
.attr_class div span.left{display:block; width:150px; float:left; margin-left:10px; text-align:right; _line-height:24px;}
.attr_class div span.right{display:block; width:350px; float:left; margin-left:5px; text-align:left;}
.attr_class div span.right input {margin-left:5px;}

#picspan {width:82px; height:82px; padding:1px; border:1px solid #efefef; line-height:80px; text-align:center; display:inline-block; overflow:hidden; float:right;}
</style>
<script type="text/javascript" src="servtools/nicedit/nicEdit.js"></script>
<script type="text/javascript">
bkLib.onDomLoaded(function() {
	new nicEditor({iconsPath : 'servtools/nicedit/nicEditorIcons.gif'}).panelInstance('goods_intro');
});

function AddContentImg(ImgName,classId){
	var obj = document.getElementById("goods_intro").previousSibling.children[0];
	obj.innerHTML = obj.innerHTML + "<br><IMG src='./"+ImgName+"' /><br>";
}

function checkForm() {
	var goods_name = document.getElementsByName("goods_name")[0];

	if(goods_name.value=='') {
		alert("<?php echo  $m_langpackage->m_goodsname_notnone;?>");
		title.focus();
		return false;
	} else if(document.getElementsByName("cat_id")[0].value==0) {
		alert("<?php echo   $m_langpackage->m_select_categorypl;?>");
		return false;
	}
	return true;
}
</script>
</head>
<body onload="bodyonload();">
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=goods_list" style="color:#F67A06;"><?php echo $m_langpackage->m_back_list;?></a>&nbsp;&nbsp;</span><?php echo $m_langpackage->m_edit_goods;?></h3></div>
		<form action="do.php?act=goods_edit" method="post" name="form_goods_edit" onsubmit="return checkForm();" enctype="multipart/form-data">
			<table width="98%" class="form_table">
				<tr><td class="textright" ><?php echo  $m_langpackage->m_goods_category;?>：</td>
					<td align="left">&nbsp;<span id="show_cat_name"><?php echo $select_category_name;?></span> &nbsp;&nbsp;<a href="javascript:;" onclick="showbgdiv();" style="color:#F1A24C; font-weight:bold;text-decoration:underline;"><?php echo $m_langpackage->m_edit_cateogry;?></a>
					<input name="cat_id" value="<?php echo  $goods_info['cat_id'];?>" type="hidden"></td>
				</tr>
				<tr>
					<td class="textright"><?php echo $m_langpackage->m_goods_brand;?>:</td>
					<td align="left" id="brand_box">&nbsp;</td>
				</tr>
				<tr>
					<td class="textright"><?php echo $m_langpackage->m_goods_type;?>：</td>
					<td align="left"><input type="radio" name="type_id" value="1" <?php if($goods_info['type_id']=='1'){?>checked<?php }?> /><?php echo $m_langpackage->m_goods_new;?> &nbsp;&nbsp;
					<input type="radio" name="type_id" value="2" <?php if($goods_info['type_id']=='2'){?>checked<?php }?> /><?php echo $m_langpackage->m_goods_notnew;?> &nbsp;&nbsp;
					<input type="radio" name="type_id" value="3" <?php if($goods_info['type_id']=='3'){?>checked<?php }?> /><?php echo $m_langpackage->m_goods_isnull;?> </span></td>
				</tr>
				<tr id="goods_attr_tr">
					<td class="textright"><?php echo $m_langpackage->m_goods_attr;?>：</td>
					<td align="left" class="attr_class" id="attr_content"></td>
				</tr>
				<tr>
					<td class="textright"><?php echo  $m_langpackage->m_goods_name;?>：</td>
					<td align="left"><input type="text" name="goods_name" value="<?php echo  $goods_info['goods_name'];?>" style="width:395px;" maxlength="200" /> <span class="red">*</span></td>
				</tr>
				<tr><td class="textright"><?php echo  $m_langpackage->m_ucategory;?>：</td>
					<td align="left"><select name="ucat_id">
						<option value="0"><?php echo $m_langpackage->m_select_cateogry;?></option>
						<?php echo  $html_shop_category;?>
					</select> <span class="red">*</span>
					<span id="ucate_add"><a href="javascript:;" onclick="showinput();"><?php echo   $m_langpackage->m_add_cat;?></a></span>
					<span id="ucate_span" style="display:none;"><input type="text" value="" style="width:115px;" id="ucat_input" /><input type="button" value="<?php echo   $m_langpackage->m_add;?>" onclick="addnewucat();" /></span></td>
				</tr>
				<tr>
					<td class="textright"><?php echo   $m_langpackage->m_goods_price;?>：</td>
					<td align="left"><input type="type" name="goods_price" value="<?php echo   $goods_info['goods_price'];?>" style="width:80px;
						text-align:right;" maxlength="8" /> <?php echo   $m_langpackage->m_yuan;?> (<?php echo   $m_langpackage->m_goods_pricezero;?>)</td></tr>
				<tr>
					<td class="textright">
						<input type="radio" value="1" id="is_transport_template" onclick="chostransporttype(1)" <?php if($goods_info['is_transport_template']){?> checked  <?php }?>   name="is_transport_template" /> <?php echo $m_langpackage->m_is_transport_template;?></td>
					<td align="left">
					<?php if(is_array($transport_template_list)){?>
						<?php foreach($transport_template_list as $value){?>
							<input type="radio" name="transport_template_id" onclick="chostransporttype(1)"  value="<?php echo $value['id'];?>" <?php if($goods_info['transport_template_id']==$value['id']){?> checked <?php }?> /><?php echo $value['transport_name'];?>&nbsp;<a href="modules.php?app=edit_transport_template&id=<?php echo $value['id'];?>" target="_blank"><?php echo  $m_langpackage->m_eidt_transport_template;?></a><br />
						<?php }?>
					<?php }?>
					<br />
					<a href="modules.php?app=add_transport_template" target="_blank"><?php echo  $m_langpackage->m_add_transport_template;?></a>
					</td>
				</tr>
				<tr>
					<td class="textright"><input type="radio"  onclick="chostransporttype(0)" value="0" <?php if(!$goods_info['is_transport_template']){?> checked  <?php }?>   name="is_transport_template" /><?php echo   $m_langpackage->m_transport_price;?>：</td>
					<td align="left"><input type="type" name="transport_price" value="<?php echo   $goods_info['transport_price'];?>" style="width:80px; text-align:right;" maxlength="8" /> <?php echo $m_langpackage->m_yuan;?></td>
				</tr>
				<tr><td class="textright"><span id="picspan"><?php if($goods_info['goods_thumb']){?><img src="<?php echo $goods_info['goods_thumb'];?>" /><?php  }else{?><?php echo  $m_langpackage->m_showgoods_photo;?><?php }?></span></td><td align="left"><input type="file" name="attach[]" onchange="showimg(this)" />
				<!-- <input type="button" value="使用已上传图片" onclick="get_img_list('img_select1','1','img_list1',this)"> <span class="red">*</span> --><br />
					图片应小于1M，jpg、png或gif格式。建议为<?php echo $SYSINFO['height2'];?></>x<?php echo $SYSINFO['width2'];?>像素
					<!-- <div id="img_select1" style="width:600px;  display:none;">
                    	<span id="img_list1">

                    	</span>
					</div> -->
				</td></tr>
				<tr><td align="left" class="textright"><?php echo  $m_langpackage->m_goods_intro;?>：</td>
					<td>
				<textarea name="goods_intro" id="goods_intro" cols="75" rows="5"><?php echo   $goods_info['goods_intro'];?></textarea>
				<iframe name="KindImageIframe" id="KindImageIframe" width="100%" height=30 align="top" allowTransparency="true" scrolling="no" src='modules.php?app=upload_form' frameborder=0></iframe>
				<input type="button" value="使用已上传图片" onclick="get_img_list('img_select2','2','img_list2',this)">
					<div id="img_select2" style="width:600px;  display:none;">
                    	<div id="img_list2">

                    	</div>
					</div>
				</td></tr>
				<tr><td class="textright"><?php echo   $m_langpackage->m_wholesale;?>：</td>
					<td align="left">
				<textarea name="goods_wholesale" cols="45" rows="3"><?php echo   $goods_info['goods_wholesale'];?></textarea>
				</td></tr>
				<tr><td class="textright"><?php echo   $m_langpackage->m_goods_number;?>：</td>
					<td align="left"><input type="type" name="goods_number" value="<?php echo   $goods_info['goods_number'];?>" style="width:80px; text-align:right;" maxlength="5" /> <?php echo   $m_langpackage->m_jian;?></td></tr>
				<tr><td class="textright"><?php echo   $m_langpackage->m_keyword;?>：</td>
					<td align="left"><input type="type" name="keyword" value="<?php echo   $goods_info['keyword'];?>" style="width:250px;" maxlength="200" /> <?php echo $m_langpackage->m_more_keyword_exp;?></td></tr>
				<tr><td class="textright"><?php echo   $m_langpackage->m_on_sale;?>：</td>
					<td align="left"><input type="checkbox" name="is_on_sale" value="1" <?php if($goods_info['is_on_sale']){ echo "checked";}?>/> <?php echo   $m_langpackage->m_view_status;?></td></tr>
				<tr><td class="textright"><?php echo   $m_langpackage->m_add_recommend;?>：</td>
					<td align="left"><input type="checkbox" name="is_best" value="1" <?php if($goods_info['is_best']){ echo "checked";}?> />
					<?php echo   $m_langpackage->m_best;?>&nbsp;
					<input type="checkbox" name="is_promote" value="1" <?php if($goods_info['is_promote']){ echo "checked";}?> />
					<?php echo  $m_langpackage->m_promote;?>&nbsp;
					<input type="checkbox" name="is_new" value="1" <?php if($goods_info['is_new']){ echo "checked";}?> />
					<?php echo   $m_langpackage->m_new;?>&nbsp;
					<input type="checkbox" name="is_hot" value="1" <?php if($goods_info['is_hot']){ echo "checked";}?> />
					<?php echo   $m_langpackage->m_hot;?>&nbsp;
					<?php echo  $m_langpackage->m_set_num;?><?php echo $user_privilege['4'];?><?php echo  $m_langpackage->m_best;?>，<?php echo $user_privilege['5'];?><?php echo  $m_langpackage->m_promote;?>，<?php echo $user_privilege['6'];?><?php echo  $m_langpackage->m_new;?>，<?php echo $user_privilege['7'];?><?php echo  $m_langpackage->m_hot;?>，<?php echo  $m_langpackage->m_num_much;?>
				</td></tr>
				<tr><td colspan="2" class="center"><input type="hidden" name="goods_id" value="<?php echo   $goods_info['goods_id'];?>" />
				<input type="submit" name="submit" value="<?php echo  $m_langpackage->m_edit_goods;?>" /></td></tr>
			</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
    <div id="bgdiv" style="display:none;"></div>
	<div id="category_select" style="display:none;">
		<div class="category_title_1"><span onclick="hidebgdiv();"><?php echo $m_langpackage->m_close;?></span><?php echo $m_langpackage->m_plss_select_cateogry;?></div>
		<?php if($isset_payment){?>
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
		<?php  } else {?>
		<div align="center"><?php echo $m_langpackage->m_isset_payment;?></div>
		<?php }?>
	</div>
</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var attribute = <?php echo  $js_attribute_info;?>;
var goodsAttr = new Array();
<?php if($goods_attr) {
	foreach($goods_attr as $v) {
		$v['attr_values'] = preg_replace("/[\r\n]+/",'|',$v['attr_values']);
		if(substr($v['attr_values'],'-1') == '|') {
			$v['attr_values'] = substr($v['attr_values'],'0','-1');
		}
		echo "goodsAttr['".$v['attr_id']."'] = '".$v['attr_values']."'; \r\n";
	}
}?>

var select_value = {'first':'','second':'','third':'','fourth':'','value':'0'};

function showbgdiv() {
	var bgdiv = document.getElementById("bgdiv");
	var goods_attr_tr = document.getElementById("goods_attr_tr");
	var category_select = document.getElementById("category_select");
	var ucat_id = document.getElementsByName("ucat_id")[0];
	var brand_box = document.getElementById("brand_box");
	ucat_id.style.display = "none";
	goods_attr_tr.style.display = "none";
	bgdiv.style.display = '';
	brand_box.style.display = 'none';
	category_select.style.display = '';
	var width = document.body.clientWidth;
	var left = "100";
	if(width) {
		left = (width-800)/2;
	}
	category_select.style.left = left+"px";
	bgdiv.style.left = (width-980)/2+"px";
}

function hidebgdiv() {
	var bgdiv = document.getElementById("bgdiv");
	var category_select = document.getElementById("category_select");
	var goods_attr_tr = document.getElementById("goods_attr_tr");
	var ucat_id = document.getElementsByName("ucat_id")[0];
	var brand_box = document.getElementById("brand_box");
	ucat_id.style.display = "";
	bgdiv.style.display = 'none';
	category_select.style.display = 'none';
	brand_box.style.display = '';
	goods_attr_tr.style.display = "";
}

function bodyonload() {
	var cat_id = document.getElementsByName("cat_id")[0];
	if(cat_id.value>0) {
		get_brand_list(cat_id.value)
	} else {
		showbgdiv();
	}


	var lis = document.getElementsByTagName("li");
	for(var i=0; i<lis.length; i++) {
		lis[i].onclick = selectli;
	}
}

function selectli() {
	var tlis = this.parentNode.childNodes;
	for(var j=0; j<tlis.length; j++) {
		tlis[j].className = '';
	}
	this.className = "select";

	var nextul = null;
	if(this.parentNode.id=='select_first') {
		nextul = document.getElementById("select_second");
		select_value.first = this.innerHTML;
		select_value.second = '';
		select_value.third = '';
		select_value.fourth = '';
		nextul.innerHTML = '';
		document.getElementById("select_third").innerHTML = '';
		document.getElementById("select_fourth").innerHTML = '';
	} else if(this.parentNode.id=='select_second') {
		nextul = document.getElementById("select_third");
		select_value.second = ' > '+this.innerHTML;
		select_value.third = '';
		select_value.fourth = '';
		nextul.innerHTML = '';
		document.getElementById("select_fourth").innerHTML = '';
	} else if(this.parentNode.id=='select_third') {
		nextul = document.getElementById("select_fourth");
		select_value.third = ' > '+this.innerHTML;
		select_value.fourth = '';
		nextul.innerHTML = '';
	} else if(this.parentNode.id=='select_fourth'){
		select_value.fourth = ' > '+this.innerHTML;
	}
	select_value.value = this.title;
	if(nextul) {
		var cat_id = this.title;
		ajax("do.php?act=category_get_catlist","POST","cat_id="+cat_id,function(data){
			if(data!='-1') {
				var newli;
				for(var i=0; i<data.length; i++) {
					newli = document.createElement("li");
					newli.onclick = selectli;
					newli.title = data[i].cat_id;
					newli.innerHTML = data[i].cat_name;
					nextul.appendChild(newli);
				}
			}
		},'JSON');
	}
}

function postcatid() {
	var cat_id = document.getElementsByName("cat_id")[0];
	var show_cat_name = document.getElementById("show_cat_name");
	show_cat_name.innerHTML = select_value.first + select_value.second + select_value.third + select_value.fourth;
	cat_id.value = select_value.value;
	changeAttr(select_value.value);
	hidebgdiv();
}

function changeAttr(value) {
	ajax("do.php?act=goods_attr_list","POST","v="+value,function(data){
		changeAttrTr(data);
	},'JSON');
}

function changeAttrTr(objvalue) {
	var attr_content = document.getElementById("attr_content");
	attr_content.innerHTML = '';
	var html = '';
	var temp = '';
	for(var i=0; i<objvalue.length; i++) {
		temp = formatFormElement(objvalue[i].attr_id,objvalue[i].input_type,objvalue[i].attr_name,objvalue[i].attr_values);
		html += '<div class="div"><span class="left">'+objvalue[i].attr_name+'：</span><span class="right">'+temp+'</span><div class="clear"></div></div>';
	}
	attr_content.innerHTML = html;
}

//属性input类型 0:TEXT,1:SELECT,2:radio,3:checkbox
function formatFormElement(id,type,name,value) {
	var optionValue;
	var cValue = str = '';
	if(goodsAttr[id]) {
		cValue = goodsAttr[id];
	}
	if(type==0) {
		str = '<input type="text" name="attr[' + id + ']" value="'+cValue+'" maxlength="200" />';
	} else if (type==1 && value!='') {
		optionValue = value.split("\r\n");
		str = '<select name="attr[' + id + ']">';
		str += '<option value="0"><?php echo  $m_langpackage->m_select_pl;?>' + name + '</option>';
		for(var i=0; i<optionValue.length; i++) {
			if(optionValue[i] == cValue) {
				str += '<option value="'+optionValue[i]+'" selected>' + optionValue[i] + '</option>';
			} else {
				str += '<option value="'+optionValue[i]+'">' + optionValue[i] + '</option>';
			}
		}
		str += '</select>';
	} else if (type==2 && value!='') {
		optionValue = value.split("\r\n");
		for(var i=0; i<optionValue.length; i++) {
			if(optionValue[i] == cValue) {
				str += '<input type="radio" name="attr[' + id + ']" value="'+optionValue[i]+'" checked />' + optionValue[i] + ' ';
			} else {
				str += '<input type="radio" name="attr[' + id + ']" value="'+optionValue[i]+'" />' + optionValue[i] + ' ';
			}
		}
	} else if (type==3 && value!='') {
		var regv = cValue.replace(/[\r\n]/g,"|");
		if(regv) {
			var re = new RegExp("(("+regv+")|([^\r\n]+))[\r\n]*","g");
		} else {
			var re = new RegExp("((iwebshop)|([^\r\n]+))[\r\n]*","g");
		}
		var str = value.replace(re,"<input type='checkbox' name='attr[" + id + "][]' value='$1' checked$3 />$1 ");
	}
	return str;
}

changeAttrTr(attribute);

function showimg(obj) {
	var picspan = document.getElementById("picspan");
	picspan.innerHTML = '';
	var Img = new Image();
	Img.id = "goods_pic";

	if(navigator.userAgent.indexOf("MSIE")>0) {
		Img.src = obj.value;
	} else {
		Img.src = obj['files'][0].getAsDataURL();
	}
	picspan.appendChild(Img);
	//imgwh();
	setTimeout("imgwh()",100);
}

function imgwh() {
	var Img = document.getElementById("goods_pic");
	var w = Img.width;
	var h = Img.height
	if(w>h) {
		Img.height = h*80/w;
		Img.width = '80';
		Img.style.marginTop = (80-Img.height)/2+'px';
	} else {
		Img.width = w*80/h;
		Img.height = '80';
	}
}

function showinput() {
	var ucate_add = document.getElementById("ucate_add");
	var ucate_span = document.getElementById("ucate_span");
	ucate_add.style.display = 'none';
	ucate_span.style.display = '';
}
function hideinput() {
	var ucate_add = document.getElementById("ucate_add");
	var ucate_span = document.getElementById("ucate_span");
	ucate_add.style.display = '';
	ucate_span.style.display = 'none';
}

function addnewucat() {
	var ucat_input = document.getElementById("ucat_input");
	var ucat_id = document.getElementsByName("ucat_id")[0];
	v = ucat_input.value;
	if(v=='') {
		alert("<?php echo  $m_langpackage->m_categoryname_notnone;?>");
		return false;
	} else {
		ajax("do.php?act=goods_ucategory_add","POST","name="+v,function(data){
			if(data!='-1') {
				var newoption = document.createElement("option");
				newoption.value = data;
				newoption.innerHTML = v;
				newoption.selected = 'selectes';
				ucat_id.appendChild(newoption);
				alert("<?php echo  $m_langpackage->m_add_success;?>");
			} else {
				alert("<?php echo  $m_langpackage->m_add_fail;?>");
			}
		},'JSON');
		hideinput();
	}
}
function get_brand_list(cat_id_value){
	ajax("do.php?act=get_brand_list&cat_id="+cat_id_value,"POST","",function(data){
		var str="<select name='brand_id'>";
		for(i=0;i<data.length;i++){
			str+="<option value='"+data[i].brand_id+"'";
			if(data[i].brand_id==<?php echo $goods_info['brand_id'];?>){
				str+="selected";
			}
			str+=">"+data[i].brand_name+"</option>";
		}
		str+="<option value='0'>其他品牌</option>";
		str+="</select>";
		document.getElementById("brand_box").innerHTML=str;
		document.getElementById("brand_box").style.display='';
	},'JSON');
}
function chostransporttype(m){
	var obj=document.form_goods_edit;
	var radioobj=document.getElementsByName("transport_template_id");
//	alert(radioobj.length);
	if(m==1){
		obj.transport_price.value=0.00;
		document.getElementById("is_transport_template").checked=true;
		var b=0;
		for(i=0;i<radioobj.length;i++){
			if(radioobj[i].checked){
				b++;
			}
		}
		if(b==0){
			radioobj[0].checked=true;
		}
	}else{
		for(i=0;i<radioobj.length;i++){
			radioobj[i].checked=false;
		}
	}
}

function get_img_list(itemid,i,imgid,obj){
	if(document.getElementById(itemid).style.display=='') {
		document.getElementById(itemid).style.display="none";
	} else {
 		document.getElementById(itemid).style.display="";
		ajax("do.php?act=shop_img_select","POST","i="+i,function(data){
			document.getElementById(imgid).innerHTML = data;
		});
	}
}
//-->
</script>
</body>
</html><?php } ?>