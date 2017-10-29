function changSpanDisplay(id){
	id = objCheck(id);
	var spans = id.getElementsByTagName('span');
	for (var i=0;i<spans.length;i++) {
		if (spans[i].style.display == 'none') {
			spans[i].style.display = '';
		} else {
			spans[i].style.display = 'none';
		}
	}
}
function showEidt(dtid,u){
	var li = getObj('dt_'+dtid);
	if(getObj('dnum_'+dtid)){
		getObj('dnum_'+dtid).style.display = 'none';
	}
	li.onmouseover = '';
	li.onmouseout = '';
	var spans = li.getElementsByTagName('span');
	for (var i=0;i<spans.length;i++) {
		spans[i].style.display = 'none';
	}
	var a = li.getElementsByTagName('a');
	for (var i=0;i<a.length;i++) {
		a[i].style.display = 'none';
	}

	var text= elementBind('input','','input','width:90px;margin-right:10px;');
	text.setAttribute('type','text');
	text.value	= a[i-1].innerHTML;
	li.appendChild(text);
	text.focus();

	var ok	= elementBind('input','','bt3','');
	ok.setAttribute('type','button');
	ok.value= '确定';

	var cancel	= elementBind('input','','bt','margin:0 0 0 .3em');
	cancel.setAttribute('type','button');
	cancel.value= '取消';

	ok.onclick	= function () {
		var name = char_cv(text.value);
		if (name.length<1) {
			showDialog('error','类型名称不能为空');
			return false;
		}
		ajax.send('apps.php?q=ajax&a=eidtdiarytype','u='+u+'&name='+name+'&dtid='+dtid,function(){
			var rText = ajax.request.responseText;
			if (rText == 'success') {
				if(getObj('dnum_'+dtid)){
					getObj('dnum_'+dtid).style.display = 'none';
				}
				cancelEdit(li,name);
			} else {
				ajax.guide();
			}
		});
	}

	cancel.onclick	= function () {
		getObj('dnum_'+dtid).style.display = '';
		cancelEdit(li);
		
	}
	
	li.appendChild(ok);
	li.appendChild(cancel);
}
function cancelEdit(li,name){
	li = objCheck(li);
	li.onmouseover = function () {
		changSpanDisplay(li);
	}
	li.onmouseout = function () {
		changSpanDisplay(li);
	}
	var input = li.getElementsByTagName('input');
	while (input[0]) {
		delElement(input[0]);
	}
	var a = li.getElementsByTagName('a');
	for (var i=0;i<a.length;i++) {
		if (name) {
			a[i].innerHTML	= name;
		}
		a[i].style.display	= '';
	}
}
function addType(id,u){/*创建日志分类*/
	if (getObj('dt_add')) {
		changeCreatInputDisplay('dt_add');
		return true;
	}
	creatDiaryTypeInput(id,u);
}
function creatDiaryTypeInput(id,u){/*创建日志分类*/
	var parent	= getObj(id);
	var li	= addChild(parent,'li','dt_add');//生成<li id='dt_add'></li>

	var text= elementBind('input','','input','width:90px;margin-right:10px;');//生成<input class="input" type="text">
	text.setAttribute('type','text');
	li.appendChild(text);//生成<li id='dt_add'><input class="txa dtwidth" type="text"></li>
	text.focus();

	var ok	= elementBind('input','','bt3','');//生成<input class="bt3" type="button" value="确定" onclick="">
	ok.setAttribute('type','button');
	ok.value= '确定';
	ok.onclick	= function () {
		var name = char_cv(text.value);
		if (name.length<1) {
			showDialog('error','类型名称不能为空');
			return false;
		}
		ajax.send('apps.php?q=ajax&a=adddiarytype','u='+u+'&name='+ajax.convert(name),function(){
			var rText = ajax.request.responseText.split('\t');//类似php中的explode
			if (rText[0] == 'success') {
				var dtid = rText[1] - 0;
				if (isNaN(dtid)==false) {
					creatDiaryType('dt_null',dtid,name,u);
					delElement('dt_add');
				} else {
					showDialog('error','非法错误');
				}
			} else {
				ajax.guide();
			}
		});
	}
	li.appendChild(ok);//生成<li id='dt_add'><input class="bt3" type="button" value="确定" onclick=""></li>
	var cancel	= elementBind('input','','bt','margin:0 0 0 5px');//生成<input class="bt" type="input" value="取消" style="padding:.2em 0;margin:0 0 0 .3em" onclick="">
	cancel.setAttribute('type','button');
	cancel.value= '取消';
	cancel.onclick	= function () {
		text.value = '';
		li.style.display = 'none';
	}
	li.appendChild(cancel);
}

function changeCreatInputDisplay(id){
	var dt_add = getObj(id);
	if (dt_add.style.display =='none') {
		dt_add.style.display = '';
	} else {
		dt_add.style.display = 'none';
	}
}
function creatDiaryType(latter,dtid,name,u){/*创建日志分类*/
	latter = objCheck(latter);
	var parent = latter.parentNode;//ul
	var li = elementBind('li','dt_'+dtid);//生成<li id='dt_1' onmouseover="changSpanDisplay(this);" onmouseout="changSpanDisplay(this);"></li>
	li.onmouseover = function () {
		changSpanDisplay(li);
	}
	li.onmouseout = function () {
		changSpanDisplay(li);
	}
	li.innerHTML = '<span class="del fr" style="cursor: pointer;display:none" onclick="pwConfirm(\'是否确认删除\',this,function(){delDiaryType(\''+dtid+'\',\''+u+'\')})">删除</span><span class="fr mr10 gray" style="cursor: pointer;display:none" onclick="showEidt(\''+dtid+'\',\''+u+'\');">编辑</span><a href="mode.php?m=o&q=diary&dtid='+dtid+'">'+name+'</a>';
	parent.insertBefore(li,latter);//在ul中，id="dt_null"的li前动态插入新li一行
}
function delDiaryType(dtid,u){/*删除日志分类*/
	ajax.send('apps.php?q=ajax&a=deldiarytype','u='+u+'&dtid='+dtid,function(){
		var rText = ajax.request.responseText;
		if (rText == 'success') {
			delElement('dt_'+dtid);
		} else {
			ajax.guide();
		}
	});
}

function add_dtid(u,id) {/*写日志页分类添加*/
	if (isNaN(u) && winduid != u) {
		showDialog('error','非法操作');
		read.menu.style.top="450px";
	}
	ajax.send('apps.php?q=ajax&a=adddiarytype','u='+u+'&b=1',function(){
		var rText = ajax.request.responseText.split('\t');
		if (rText[0]=='success') {
			if (rText[1]=='1') {
				read.setMenu(create_dtid(id));
				read.menupz();
				read.menu.style.top="450px";
			} else {
				showDialog('error','非法操作');
				read.menu.style.top="450px";
			}
		}
	});
}

function create_dtid(id) {/*写日志页分类添加*/
	selid = objCheck(id);
	var maindiv	= elementBind('div','','','width:300px;height:100%');
	var title = elementBind('div','','h');
	title.innerHTML = '增加日志分类';
	maindiv.appendChild(title);
	var innerdiv = addChild(maindiv,'div','','menu-text p10');
	var ul = addChild(innerdiv,'ul','');
	var li = addChild(ul,'li');
	li.innerHTML = '分类名称';

	var text= elementBind('input','','input','margin-left:10px');
	text.setAttribute('type','text');
	li.appendChild(text);
	var innername = document.createTextNode(' 小于20字节');
	li.appendChild(innername);
	text.focus();

	var footer	= addChild(maindiv,'div','','bdtA tr2','padding:5px');
	var tar	= addChild(footer,'div','','tar');
	var ok	= elementBind('input','','bt3');
	ok.setAttribute('type','button');
	ok.setAttribute('value','确定');

	ok.onclick	= function () {
		var name = char_cv(text.value);
		if (name.length<1) {
			showDialog('error','类型名称不能为空');
			read.menu.style.top="450px";
			return false;
		}
		ajax.send('apps.php?q=ajax&a=adddiarytype','u='+winduid+'&name='+ajax.convert(name),function(){
			var rText = ajax.request.responseText.split('\t');
			if (rText[0] == 'success') {
				var dtid = rText[1] - 0;
				if (isNaN(dtid)==false) {
					var option = elementBind('option');
					option.innerHTML = name;
					//var innername = document.createTextNode(name);
					//option.appendChild(innername);
					option.value = dtid;
					option.selected = 'selected';
					selid.insertBefore(option,null);
					showDialog('success','分类添加成功!',2);
					read.menu.style.top="450px";
				} else {
					showDialog('error','非法错误');
					read.menu.style.top="450px";
				}
			} else {
				ajax.guide();
			}
		});
	}

	var cansel	= elementBind('input','','bt','margin-left:8px');
	cansel.type	= 'button';
	cansel.value= '取消';
	cansel.onclick	= closep;

	tar.appendChild(ok);
	tar.appendChild(cansel);

	return maindiv;
}

function optionsel(id) {/*权限选择*/
	copy = objCheck('if_copy');
	if (isNaN(id)) {
		showDialog('error','非法操作');
	}
	if (id == '0') {
		copy.disabled = '';
		copy.checked = 'checked';
	} else if (id == '1') {
		copy.disabled = '';
		copy.checked = 'checked';
	} else if (id == '2') {
		copy.disabled = 'disabled';
		copy.checked = '';
	}
}

function delDiary(id,u,space){/*删除日志*/
	ajax.send('apps.php?q=ajax&a=deldiary&id='+id,'',function(){
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			var element = document.getElementById('diary_'+id);
			if (element) {
				element.parentNode.removeChild(element);
				if (space != 2) {
					window.location.href = basename;
				}
			} else {
					window.location.reload();
			}
		} else {
			ajax.guide();
		}
	});
}

function Copydiary(did,dtid,privacy) {/*日志转载*/
	ajax.send('apps.php?q=ajax&a=copydiary&did='+did+'&dtid='+dtid+'&privacy='+privacy,'',function(){
		var rText = ajax.request.responseText;
		if (rText=='success') {
			read.setMenu(create_copy());
			read.menupz();
		} else {
			ajax.guide();
		}
	});
}

function create_copy() {/*转载提示*/
	var maindiv	= elementBind('div','','','width:300px;height:100%');
	var title = elementBind('div','','h');
	title.innerHTML = '转载提示';
	maindiv.appendChild(title);
	var innerdiv = addChild(maindiv,'div','','menu-text p10');
	var ul = addChild(innerdiv,'ul','');
	var li = addChild(ul,'li');
	li.innerHTML = '转载成功，日志已存在我的日志中，是否要去浏览？';

	var footer	= addChild(maindiv,'div','','tr2','padding:5px;');
	var tar	= addChild(footer,'div','','tar');
	var ok	= elementBind('input','','bt3');
	ok.setAttribute('type','button');
	ok.setAttribute('value','浏览');

	ok.onclick	= function () {
		window.location.href = temp_basename+'q=diary';
	}

	var cansel	= elementBind('input','','bt','margin-left:8px');
	cansel.type	= 'button';
	cansel.value= '关闭';
	cansel.onclick	= closep;

	tar.appendChild(ok);
	tar.appendChild(cansel);

	return maindiv;
}

function ajaxpage(url,type,u,space) {/*浏览日志*/
	var tourl = basename;
	ajax.send(url,'',function() {
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			if (rText[1]) {
				window.location.href = tourl + 'did=' + rText[1];
			} else {
				ajax.request.responseText = type == 'next' ? '已经是最后一篇日志' : '已经是第一篇日志';
				ajax.guide();
			}
		} else {
			ajax.guide();
		}
	});
	return false;
}