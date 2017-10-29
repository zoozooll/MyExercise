<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script type="text/javascript" src="./jscript/editor/tiny_mce.js"></script>
<script language="javascript">
function setPic(val) {
	tinyMCE.execCommand('mceInsertContent',false,'<img src="'+val+'" border="0">');
	//document.form1.news_content.value += ;
}
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid="+val;
	// onChange="MP_jumpMenu(this.value)"
}
<!-- TinyMCE -->
	// O2k7 skin
	tinyMCE.init({
		// General options
		mode : "exact",
		elements : "news_content",
		theme : "advanced",
		skin : "o2k7",
		plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,inlinepopups",

		// Theme options
		theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,|,fontselect,fontsizeselect,|,forecolor,backcolor,|,removeformat,fullscreen,code,preview",
		theme_advanced_buttons2 : "cut,copy,paste,|,search,replace,|,undo,redo,|,link,unlink,image,cleanup,|,insertdate,inserttime,|,tablecontrols,|,sub,sup,charmap,media,advhr",
		theme_advanced_buttons3 : "",
		//theme_advanced_buttons4 : "",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : true,

		// Example content CSS (should be your site CSS)
		content_css : "css/content.css",

		// Drop lists for link/image/media/template dialogs
		template_external_list_url : "lists/template_list.js",
		external_link_list_url : "lists/link_list.js",
		external_image_list_url : "lists/image_list.js",
		media_external_list_url : "lists/media_list.js",

		// Replace values for the template plugin
		template_replace_values : {
			username : "Some User",
			staffid : "991234"
		}
	});
<!-- /TinyMCE -->
function checkData() {
	if(document.form1.cid.value == '') {
		alert('类别没有选择！');
		document.form1.cid.focus;
		return false;
	}
	if(document.form1.news_title.value == '') {
		alert('标题不能为空！');
		document.form1.news_title.focus;
		return false;
	}
	 
	if(document.form1.news_content.value == '') {
		if(tinyMCE.get('news_content').getContent() != '') {
			document.form1.news_content.value = tinyMCE.get('news_content').getContent();
		} else {
			alert('内容不能为空！');
			document.form1.news_content.focus;
			return false;	
		}
	}
}
</script>
</head>
<body>
<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1" onSubmit="return checkData();">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"><?php if($this->__muant["pid"]>1) { ?>修改商品<?php } ?></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">新闻类别:</div>
	    <div class="fLeft" id="hiddenselect">
		<select class="txInput" name="cid" id="cid">
		  <option value="">....请选择</option>
		  <?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		  <option value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"<?php if($this->__muant["value"]["$valuei"]["id"]==$this->__muant["news"]["cid"]) { ?> selected<?php } ?>>
		  <?php echo $this->__muant["value"]["$valuei"]["_name"] ?></option>
		  <?php } ?>
		</select>
		</div>
		<div class="cls"></div>
    </div>	
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">标题:</div>
      <div class="fLeft"><input class="txInput" name="news_title" type="text" value="<?php echo $this->__muant["news"]["title"] ?>"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 作者:</div>
	    <div class="fLeft">
		    <input class="txInput" name="news_author" type="text" value="<?php echo $this->__muant["news"]["author"] ?>">
		    来源:
		    <input class="txInput" name="news_source" type="text" value="<?php echo $this->__muant["news"]["source_from"] ?>"> 
		</div>
		<div class="cls"></div>
    </div>
	
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 内容:</div>
	    <div class="fLeft" style="height:300px;"><textarea id="news_content" name="news_content"  rows="15" cols="35" style="width:98%"><?php echo $this->__muant["news"]["content"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 上传图片:</div>
		<div class="fLeft"><iframe src="./admin/adminajaxset.php?switch=uploadnewspic&lid=<?php echo $this->__muant["lid"] ?>" width="260" frameborder="0" height="22" allowTransparency='true' name="uploadpic"></iframe></div>
		<div class="cls"></div>
    </div>
	<div class="cls"></div>
</div>
<div id="divMainBodyContentSumbit">
<input name="nid" type="hidden" value="<?php echo $this->__muant["news"]["id"] ?>">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>