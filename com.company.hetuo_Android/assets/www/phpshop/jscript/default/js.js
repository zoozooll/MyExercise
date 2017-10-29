function delYesOrNo(msn) {
	if(msn == null) msn = '您确定要删除么？';
	x=confirm(msn);
	if(x) {
	} else {
		return false;
	}
}

function setMsgDiv(id, msg, error) {
	var font = '';
	if(error == null) {
		msg = '<font color="red">'+msg+'</font>';
	}
	msn = '<div id="divmsg">' + msg + '<div>';
	document.getElementById(id).innerHTML = msg;
}

function displayDiv(id, show) {
	var div = document.getElementById(id);
	if(show == false) {
		div.style.display = 'none';
	} else {
		div.style.display = 'block';
	}
}
function changeClass(id, classname) {
	document.getElementById(id).className = classname;
}
function setUserShopFrame(val) {
	var frame = "<iframe src='shop.php?switch=iframeshop&pid="+val+"&act=add' frameborder='0' overflow-y='yes' "
			   +" id='UserShop_iframe' name='UserShop_iframe' allowTransparency='true' width='100%' height='378'>"
			   +" </iframe>";
	document.getElementById("iframeshop").innerHTML = frame;
}
function callLoad(id, msg) {
	if(msg==null) {
		msg = '数据正在载入,请稍后...';
	}
	displayDiv(id);
	document.getElementById(id).innerHTML = '<center><br /><div id="load"><img src="images/default/icon_loading.gif" /><br /><br />'+msg+'<br/></div></center>';
}