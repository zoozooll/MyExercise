function pwConfirm(title,position,callback){
	if (typeof(callback) != 'function') alert('error');

	if (objCheck('oldinfo')) {
		getObj('oldinfo').parentNode.removeChild(getObj('oldinfo'));
	}
	var container 	= elementBind('div','oldinfo','','width:200px;height:100px;margin:20px auto;position: absolute;');
	($('upPanel')||document.body).appendChild(container);

	var center_con	= elementBind('div','','menu-post','width:200px;position:absolute;');
	var inner_con	= elementBind('div','','menu-b');
	var content		= elementBind('div','','f14 b p10');
	content.innerHTML = title;
	var input_con	= elementBind('div','','tar pdD');
	var ok	= elementBind('input','','bt3','width:80px;');
	ok.type = 'button';
	ok.value= '确 定';
	ok.onclick = function(){
		container.parentNode.removeChild(container);
		callback();
	}
	input_con.appendChild(ok);
	var cancel = elementBind('input','','bt','margin-left:4px;');
	cancel.type = 'button';
	cancel.value= '取消';
	cancel.onclick = function(){
		container.parentNode.removeChild(container);
	}
	input_con.appendChild(cancel);
	var angle = elementBind('b','','angle');
	inner_con.appendChild(content);
	inner_con.appendChild(input_con);
	inner_con.appendChild(angle);
	center_con.appendChild(inner_con);
	container.appendChild(center_con);
	if (typeof(position)!='object') {
		container.style.top  = (ietruebody().clientHeight - container.offsetHeight)/3 + getTop() + 'px';
		container.style.left = (ietruebody().clientWidth - container.offsetWidth)/2 + 'px';
	} else {
		var top  = findPosY(position);
		var left = findPosX(position);
		top = getTop() + top - container.offsetHeight;
		
		if (ietruebody().clientWidth <left + container.offsetWidth)
		{
			
			left = left - container.offsetWidth;
		}

		container.style.top  = top  + 'px';
		container.style.left = left + 'px';
	}
	container.style.display = '';
}


function dateFormat(date,format){
	var o = {
		"Y+" :  date.getFullYear(),
		"M+" :  date.getMonth()+1,  //month
		"m+" :  date.getMonth()+1,  //month
		"d+" :  date.getDate(),     //day
		"h+" :  date.getHours(),    //hour
		"i+" :  date.getMinutes(),  //minute
		"s+" :  date.getSeconds(), //second
		"q+" :  Math.floor((date.getMonth()+3)/3),  //quarter
		"S"  :  date.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("("+ k +")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}
	return format;
}

function postShareOtherType(type,id,ifhidden){
	var textarea	= read.menu.getElementsByTagName('textarea');
	var descrip	= textarea[0].value;
	var gdcode = getObj('gdcode').value;
	var qanswer = getObj('qanswer').value;
	var qkey = getObj('qkey').value;
	ajax.send('mode.php?m=o&q=sharelink','type='+type+'&id='+id+'&descrip='+ajax.convert(descrip)+'&gdcode='+ajax.convert(gdcode)+'&qanswer='+ajax.convert(qanswer)+'&qkey='+ajax.convert(qkey)+'&ifhidden='+ifhidden,function(){
		var rText = ajax.request.responseText;
		if (rText=='success') {
			if (ifhidden == 1) {
				showDialog('success','收藏成功!',2);
			} else {
				showDialog('success','分享成功!',2);
			}
		} else {
			ajax.guide();
		}
	});
}


var linknum = 1;
function createLinkBox(){
	var div = getObj('linkbox');

	div.innerHTML = '<div class="menu-post"><div class="menu-b"><table width="380" cellspacing="0" cellpadding="0"><tbody id="linkmode" style="display:none;"><tr><td  style="padding:8px 0 0 8px;"><input class="input" id="linkdiscrip" size="20" value="" /></td><td style="padding:8px 0 0 0;"><input class="input" id="linkaddress" value="http://" size="35" /></td></tr></tbody><tr><th class="h" colspan="2" style="cursor:move" onmousedown="read.move(event);"><div class="fr" style="cursor:pointer;" onclick="closep();" title="关闭"><img src='+imgpath+'/close.gif></div>插入url链接</th></tr><tr><td width="40%" style="padding:8px 0 0 8px;">链接说明</td><td width="60%" class="tal"><a class="fr" style="margin:5px 10px 0 0;" style="color:blue;cursor:pointer;" onclick="addlink();">添加</a><span style="line-height:28px;">链接地址</span></td></tr><tr><td style="padding-left:8px;"><input class="input" id="linkdiscrip1" size="20" value="" /></td><td><input class="input" id="linkaddress1" value="http://" size="35" /></td></tr><tbody id="linkbody"></tbody></table><ul><li style="text-align:center;padding:10px 0;"><span class="button"><span><button type="button" onclick="return insertlink();">提 交</button></span></span></li></ul></div></div>';

	read.open('linkbox','createlinkid','2');
}


function addlink(){
	var s = getObj('linkmode').firstChild.cloneNode(true);
	var temp_linknum = ++linknum;
	var tags = s.getElementsByTagName('input');
	for (var i=0;i<tags.length;i++) {
		if (tags[i].id == 'linkdiscrip') {
			tags[i].id = 'linkdiscrip' + linknum;
		}
		if (tags[i].id == 'linkaddress') {
			tags[i].id = 'linkaddress' + linknum;
		}
	}
	getObj('linkbody').appendChild(s);
}

function insertlink(){

	var AddTxt = '';
	var temp_linknum = linknum;

	for (var i=1;i<=linknum;i++) {
		if (getObj('linkdiscrip'+i).value.length == 0 && getObj("linkaddress"+i).value == 'http://') continue;
		if (getObj('linkdiscrip'+i).value){
			AddTxt += "[url=" + encodeURI(getObj("linkaddress"+i).value) + "]" + getObj("linkdiscrip"+i).value + "[/url]";
		} else {
			AddTxt += "[url=" + encodeURI(getObj("linkaddress"+i).value) + "]" + getObj("linkaddress"+i).value + "[/url]";
		}
	}

	document.FORM.atc_content.value += AddTxt;
	linknum = 1;
	closep();
}