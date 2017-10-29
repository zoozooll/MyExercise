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
<script language="JavaScript">
function setPic(val) {
	document.form1.value4.value = val;
}
</script>
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站名称:</div>
		<div><input name="value1" type="text" class="txInput" id="value1" value="<?php echo $this->__muant["value"]["value1"] ?>" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Copyright:</div>
		<div><input name="value2" type="text" class="txInput" id="value2" value="<?php echo $this->__muant["value"]["value2"] ?>" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站 URL(www.example.com):</div>
		<div><input name="value3" type="text" class="txInput" id="value3" value="<?php echo $this->__muant["value"]["value3"] ?>" >(不要http://开头 和 / 结尾 例:www.myshop.com/shop)</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站 logo:</div>
		<div class="fLeft"><input name="value4" type="text" class="txInput" id="value4" value="<?php echo $this->__muant["value"]["value4"] ?>" > </div>
		<div>&nbsp;<iframe src="./admin/adminajaxset.php?switch=uploadlogo&lid=<?php echo $this->__muant["lid"] ?>" width="380" frameborder="0" height="25" allowTransparency='true' name="uploadpic"></iframe></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站风格:</div>
		<div><?php $dirinum = count($this->__muant["dir"]); for($diri = 0; $diri<$dirinum; $diri++) { ?><input name="value11" type="radio" value="<?php echo $this->__muant["dir"]["$diri"] ?>" id="value11"<?php if($this->__muant["dir"]["$diri"]==$this->__muant["value"]["value11"]) { ?> checked<?php } ?>><?php echo $this->__muant["dir"]["$diri"] ?> <?php } ?>
		</div>
	</div>	
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站风格编译模式:</div>
		<div><input name="value12" type="radio" value="" id="value11"<?php if($this->__muant["value"]["value12"]=='') { ?> checked<?php } ?>> 关(风格已经编译)
		<input name="value12" type="radio" value="no" id="value11"<?php if($this->__muant["value"]["value12"]=='no') { ?> checked<?php } ?>> 开(风格未编译) [此功能可用来调试风格]
		</div>
	</div>	
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站备案信息代码:</div>
		<div><input name="value5" type="text" class="txInput" id="value5" value="<?php echo $this->__muant["value"]["value5"] ?>" >
		页面底部可以显示 ICP 备案信息，如果网站已备案，在此输入您的授权码
		</div>
	</div>
	<!--div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站语言:</div>
		<div>
		<select name="value6">
		<option value="gbk"<?php if($this->__muant["value"]["value6"]=='gbk') { ?> selected<?php } ?>>简体中文</option>
		<option value="big5"<?php if($this->__muant["value"]["value6"]=='big5') { ?> selected<?php } ?>>繁体中文</option>
		<option value="English"<?php if($this->__muant["value"]["value6"]=='English') { ?> selected<?php } ?>>English</option>
		</select>
		</div>
	</div-->
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站时区:</div>
		<div>
		<select name="value16">
		<option value="-12"<?php if($this->__muant["value"]["value16"]=='-12') { ?> selected<?php } ?>>(GMT -12:00) Eniwetok, Kwajalein</option>
		<option value="-11"<?php if($this->__muant["value"]["value16"]=='-11') { ?> selected<?php } ?>>(GMT -11:00) Midway Island, Samoa</option>
		<option value="-10"<?php if($this->__muant["value"]["value16"]=='-10') { ?> selected<?php } ?>>(GMT -10:00) Hawaii</option>
		<option value="-9"<?php if($this->__muant["value"]["value16"]=='-9') { ?> selected<?php } ?>>(GMT -09:00) Alaska</option>
		<option value="-8"<?php if($this->__muant["value"]["value16"]=='-8') { ?> selected<?php } ?>>(GMT -08:00) Pacific Time (US &amp; Canada), Tijuana</option>
		<option value="-7"<?php if($this->__muant["value"]["value16"]=='-7') { ?> selected<?php } ?>>(GMT -07:00) Mountain Time (US &amp; Canada), Arizona</option>
		<option value="-6"<?php if($this->__muant["value"]["value16"]=='-6') { ?> selected<?php } ?>>(GMT -06:00) Central Time (US &amp; Canada), Mexico City</option>
		<option value="-5"<?php if($this->__muant["value"]["value16"]=='-5') { ?> selected<?php } ?>>(GMT -05:00) Eastern Time (US &amp; Canada), Bogota, Lima, Quito</option>
		<option value="-4"<?php if($this->__muant["value"]["value16"]=='-4') { ?> selected<?php } ?>>(GMT -04:00) Atlantic Time (Canada), Caracas, La Paz</option>
		<option value="-3"<?php if($this->__muant["value"]["value16"]=='-3') { ?> selected<?php } ?>>(GMT -03:00) Brassila, Buenos Aires, Georgetown, Falkland Is</option>
		<option value="-2"<?php if($this->__muant["value"]["value16"]=='-2') { ?> selected<?php } ?>>(GMT -02:00) Mid-Atlantic, Ascension Is., St. Helena</option>
		<option value="-1"<?php if($this->__muant["value"]["value16"]=='-1') { ?> selected<?php } ?>>(GMT -01:00) Azores, Cape Verde Islands</option>
		<option value="0"<?php if($this->__muant["value"]["value16"]=='0') { ?> selected<?php } ?>>(GMT) Casablanca, Dublin, Edinburgh, London, Lisbon, Monrovia</option>
		<option value="1"<?php if($this->__muant["value"]["value16"]=='1') { ?> selected<?php } ?>>(GMT +01:00) Amsterdam, Berlin, Brussels, Madrid, Paris, Rome</option>
		<option value="2"<?php if($this->__muant["value"]["value16"]=='2') { ?> selected<?php } ?>>(GMT +02:00) Cairo, Helsinki, Kaliningrad, South Africa</option>
		<option value="3"<?php if($this->__muant["value"]["value16"]=='3') { ?> selected<?php } ?>>(GMT +03:00) Baghdad, Riyadh, Moscow, Nairobi</option>
		<option value="4"<?php if($this->__muant["value"]["value16"]=='4') { ?> selected<?php } ?>>(GMT +04:00) Abu Dhabi, Baku, Muscat, Tbilisi</option>
		<option value="5"<?php if($this->__muant["value"]["value16"]=='5') { ?> selected<?php } ?>>(GMT +05:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option>
		<option value="6"<?php if($this->__muant["value"]["value16"]=='6') { ?> selected<?php } ?>>(GMT +06:00) Almaty, Colombo, Dhaka, Novosibirsk</option>
		<option value="7"<?php if($this->__muant["value"]["value16"]=='7') { ?> selected<?php } ?>>(GMT +07:00) Bangkok, Hanoi, Jakarta</option>
		<option value="8"<?php if($this->__muant["value"]["value16"]=='8') { ?> selected<?php } ?>>(GMT +08:00) Beijing, Hong Kong, Perth, Singapore, Taipei</option>
		<option value="9"<?php if($this->__muant["value"]["value16"]=='9') { ?> selected<?php } ?>>(GMT +09:00) Osaka, Sapporo, Seoul, Tokyo, Yakutsk</option>
		<option value="10"<?php if($this->__muant["value"]["value16"]=='10') { ?> selected<?php } ?>>(GMT +10:00) Canberra, Guam, Melbourne, Sydney, Vladivostok</option>
		<option value="11"<?php if($this->__muant["value"]["value16"]=='11') { ?> selected<?php } ?>>(GMT +11:00) Magadan, New Caledonia, Solomon Islands</option>
		<option value="12"<?php if($this->__muant["value"]["value16"]=='12') { ?> selected<?php } ?>>(GMT +12:00) Auckland, Wellington, Fiji, Marshall Island</option>
		</select>
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站关闭:</div>
		<div><input type="radio" name="value7" value="1"<?php if($this->__muant["value"]["value7"]==1) { ?> checked<?php } ?>>开放
			 <input name="value7" type="radio" value="0"<?php if($this->__muant["value"]["value7"]==0) { ?> checked<?php } ?>>关闭
			 暂时将网站关闭，其他人无法访问，但不影响管理员访问
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">网站关闭的原因:</div>
		<div><textarea name="value8" cols="35" rows="3" class="txInput" id="value8"><?php echo $this->__muant["value"]["value8"] ?></textarea>
			网站关闭时出现的提示信息
		</div>
	</div>
    <div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">第三方网站统计代码:</div>
		<div><textarea name="value9" cols="35" rows="3" class="txInput" id="value9"><?php echo $this->__muant["value"]["value9"] ?></textarea>
		</div>
	</div>
	<!--
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">缓存设置</div>	
		<div>是 <input type="radio" name="value9" value="1"<?php if($this->__muant["value"]["value9"]==1) { ?> checked<?php } ?>>
		否<input type="radio" name="value9" value="0"<?php if($this->__muant["value"]["value9"]==0) { ?> checked<?php } ?>>
		站内所有的页面将缓存，在缓存时间内，页面不更新。
		</div>	
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">全站缓存时间(秒):</div>
		<div><input name="value10" type="text" class="txInput" value="<?php echo $this->__muant["value"]["value10"] ?>" ></div>  
	</div>
	-->
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>