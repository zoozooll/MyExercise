
var newAtt = {
	aid : 0,

	getElements : function (s) {
		var o = new Array();
		var p = s.getElementsByTagName('select');
		for (var i = 0; i < p.length; i++) {
			o.push(p[i]);
		}
		var p = s.getElementsByTagName('input');
		for (var i = 0; i < p.length; i++) {
			o.push(p[i]);
		}
		return o;
	},

	create : function() {
		if (!IsElement('attach'))
			return;
		newAtt.aid++;
		attachnum--;
		var s = getObj('att_mode').getElementsByTagName("tr")[0].cloneNode(true);
		var id = newAtt.aid;
		s.id = 'att_div' + id;

		var tags = newAtt.getElements(s);
		for (var i = 0; i < tags.length; i++) {
			tags[i].name += id;
			tags[i].id = tags[i].name;
			if (tags[i].name == 'attachment_' + id) {
				tags[i].onchange = function(){newAtt.up(id);};
			}
		}
		getObj('attach').appendChild(s);
	},

	up : function(id) {
		var div  = getObj('att_div' + id);
		var path = getObj('attachment_' + id).value;
		var attach_ext = path.substr(path.lastIndexOf('.') + 1, path.length).toLowerCase();
		if (allow_ext != '  ' && (attach_ext == '' || allow_ext.indexOf(' ' + attach_ext + ' ') == -1)) {
			if (IsElement('att_span' + id)) {
				getObj('att_span'+id).parentNode.removeChild(getObj('att_span'+id));
			}
			if (path != '') {
				if (typeof showDialog == 'function') {
					showDialog('warning','附件类型不匹配');
				} else {
					alert('附件类型不匹配!');
				}
			}
			return false;
		}
		getObj('attachment_' + id).onmouseover = function(){newAtt.viewimg(id)};
		if (!IsElement('att_span' + id)) {
			var li = document.createElement("span");
			var s = document.createElement("a");
			s.className    = 'bta';
			s.unselectable = 'on';
			s.onclick      = function(){newAtt.addupload(id)};
			s.innerHTML    = '插入';
			li.appendChild(s);
			var s    = document.createElement("a");
			s.className    = 'bta';
			s.unselectable = 'on';
			s.onclick      = function(){newAtt.delupload(id)};
			s.innerHTML    = '删除';
			li.appendChild(s);
			li.id = 'att_span' + id;
			div.lastChild.appendChild(li);
		}
		if (attachnum > 0 && getObj('attach').lastChild.id == 'att_div' + id) {
			newAtt.create();
		}
	},

	sel : function(o) {
		var p = o.parentNode.parentNode;
		var s = p.getElementsByTagName('select')[1];
		switch (o.value) {
			case '1':
				if (!IsElement('atc_requireenhide') || getObj('atc_requireenhide').disabled == true) {
					if (typeof showDialog == 'function') {
						showDialog('error','您没有权限加密附件');
					} else {
						alert('您没有权限加密附件!');
					}
					o.selectedIndex = 0;return;
				}
				break;
			case '2':
				if (!IsElement('atc_requiresell') || getObj('atc_requiresell').disabled == true) {
					if (typeof showDialog == 'function') {
						showDialog('error','您没有权限出售附件');
					} else {
						alert('您没有权限出售附件!');
					}
					o.selectedIndex = 0;return;
				}
				break;
			default:return;
		}
		var d = getObj('attmode_' + o.value).options;
		s.length = 0;
		for (var i = 0; i < d.length; i++) {
			s.options[i] = new Option(d[i].text,d[i].value);
		}
	},

	viewimg : function(id) {
		var path = getObj('attachment_' + id).value;
		if (!is_ie || !path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig))
			return;
		newAtt.getimage(path, 320, 'attachment_' + id);
	},

	getimage : function(path,maxwh,id) {
		var img = new Image();
		img.src = path+"?ra="+Math.random();
		img.onload = function(){
			getObj('viewimg').innerHTML =  '<div style="padding:6px;"><img src="' + img.src + '"' + ((this.width>maxwh || this.height>maxwh) ? (this.width > this.height ? ' width' : ' height') + '="' + maxwh + '"' : '') + ' /></div>';
			read.open('viewimg', id, 3);
		};
	},

	addupload : function(attid) {
		if (typeof WYSIWYD == 'function') {
			editor.focusEditor();
			if (editor._editMode == 'textmode' || !is_ie) {
				AddCode(' [upload=' + attid + '] ','');
			} else if (IsElement('attachment_' + attid)) {
				var path = getObj('attachment_' + attid).value;
				if (!path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig)) {
					path = imgpath + '/' + stylepath + '/file/zip.gif';
				}
				var img = imgmaxwh(path,320);
				editor.insertHTML('<img src="'+img.src+'" type="upload_'+attid+'" width="'+img.width+'" />');
			}
		} else {
			var atc = document.FORM.atc_content;
			var text = ' [upload=' + attid + '] ';
			if (document.selection) {
				var sel = document.selection.createRange();
				if (sel.parentElement().name == 'atc_content') {
					sel.text = text;
					sel.select();
				} else {
					atc.value += text;
				}
			} else if (typeof atc.selectionStart != 'undefined') {
				var prepos = atc.selectionStart;
				atc.value = atc.value.substr(0,atc.selectionStart) + text + atc.value.substr(atc.selectionEnd);
				atc.selectionStart = prepos + text.length;
				atc.selectionEnd = prepos + text.length;
			} else {
				atc.value += ' [upload=' + attid + '] ';
			}
		}
	},

	delupload : function(id) {
		getObj('att_div' + id).parentNode.removeChild(getObj('att_div' + id));
		attachnum++;
		if (getObj('attach').hasChildNodes() == false) {
			newAtt.create();
		}
	},

	add : function(o) {
		var id = o.parentNode.parentNode.parentNode.id;
		id = id.substr(id.lastIndexOf('_') + 1);
		addattach(id);
	}
}

var oldAtt = {

	init : function() {

		var o = getObj('ajaxtable');
		for (var i in attachs) {
			var s = getObj('att_mode').firstChild.cloneNode(true);
			s.id = 'att_' + i;

			s.getElementsByTagName('select')[0].name	= 'oldatt_special[' + i + ']';
			s.getElementsByTagName('select')[0].value	= attachs[i][4];
			if (attachs[i][4] > 0) {
				newAtt.sel(s.getElementsByTagName('select')[0]);
			}
			s.getElementsByTagName('select')[1].name	= 'oldatt_ctype[' + i + ']';
			s.getElementsByTagName('select')[1].value	= attachs[i][6];
			s.getElementsByTagName('input')[2].name		= 'oldatt_needrvrc[' + i + ']';
			s.getElementsByTagName('input')[2].value	= attachs[i][5];
			s.getElementsByTagName('input')[1].name		= 'oldatt_desc[' + i + ']';
			s.getElementsByTagName('input')[1].value	= attachs[i][7];

			var fname = s.getElementsByTagName('td')[0];
			fname.innerHTML = '<div style="width:220px;overflow:hidden;"><span id="attach_' + i + '" onmouseover="oldAtt.view(this);"><font color="red"><b>' + attachs[i][0] + '</b></font>&nbsp;(' + attachs[i][1] + 'K)</span></div>';

			var li = document.createElement('span');
			li.id = 'atturl_' + i;
			li.innerHTML = attachs[i][2];
			li.style.display = 'none';
			fname.appendChild(li);

			var li = document.createElement("span");
			var a = document.createElement("a");
			a.id = 'md_' + i;
			a.className    = 'bta';
			a.unselectable = 'on';
			a.onclick      = function(){oldAtt.modify(this)};
			a.innerHTML    = '修改';
			li.appendChild(a);
			var a = document.createElement("a");
			a.className    = 'bta';
			a.onclick      = function(){newAtt.add(this)};
			a.innerHTML    = '插入';
			a.unselectable = 'on';
			li.appendChild(a);
			s.lastChild.appendChild(li);

			var li = document.createElement('td');
			li.innerHTML = '<input type="checkbox" name="keep[]" value="' + i + '" checked />';
			s.insertBefore(li, s.firstChild);

			o.appendChild(s);
		}
	},

	modify : function(o) {
		var id = o.parentNode.parentNode.parentNode.id;
		id = id.substr(id.lastIndexOf('_') + 1);
		var s = getObj('attach_' + id);
		var p = oldAtt.create(id);
		s.parentNode.insertBefore(p,s);
		p.select();
		s.style.display = 'none';
		oldAtt.change(id,2);
	},

	create : function(id) {
		var o		= document.createElement('input');
		o.type		= 'file';
		o.className	= 'input file';
		o.size		= 20;
		o.maxLength	= 100;
		o.name		= 'replace_' + id;
		o.id		= 'replace_' + id;
		o.onmouseover = function(){oldAtt.view(this)};
		return o;
	},

	cancle : function(id) {
		var o = getObj('replace_' + id);
		var s = getObj('attach_' + id);
		o.parentNode.removeChild(o);
		s.style.display = '';
		oldAtt.change(id,1);
	},

	change : function(id,type) {
		var s = getObj('md_' + id);
		if (type == 2) {
			s.innerHTML = '取消';
			s.onclick = function(){oldAtt.cancle(id)};
		} else {
			s.innerHTML = '修改';
			s.onclick = function(){oldAtt.modify(this)};
		}
	},

	view : function(o) {
		var id = o.parentNode.parentNode.parentNode.id;
		id = id.substr(id.lastIndexOf('_') + 1);
		if (IsElement('replace_' + id)) {
			var path = getObj('replace_' + id).value;
			if (!is_ie || !path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig))
				return;
			newAtt.getimage(path, 320, 'att_' + id);
		} else {
			var path = getObj('atturl_' + id).innerHTML;
			if (path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig)) {
				newAtt.getimage(path, 320, 'att_' + id);
			}
		}
	}
}

var flashAtt = {

	flashObj : null,

	init : function() {
		if (IsElement('flash')) {
			getObj('flash').parentNode.removeChild(getObj('flash'));
			return;
		}
		var div = document.createElement('div');
		div.id  = 'flash';
		var flashvar = 'url=' + getObj('headbase').href + '/job.php?' + 'action=mutiupload&mutiupload=' + (allowmutinum - mutiupload);
		if (is_ie) {
			var html = '<object id="mutiupload" name="mutiupload" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="250" height="46"><param name="movie" value="' +  imgpath + '/upload.swf" /><param name="FlashVars" value="' + flashvar + '"/><param name="allowScriptAccess" value="always" /><param name="wmode" value="transparent" /></object>';
		} else {
			var html = '<embed type="application/x-shockwave-flash" src="' + imgpath + '/upload.swf" width="250" height="46" id="mutiupload" name="mutiupload" allowScriptAccess="always" wmode="transparent" FlashVars="' + flashvar + '" />';
		}
		html += '';
		getObj('flashUploadPanel').appendChild(div);//getObj('attsize').parentNode.insertBefore(div,getObj('attsize'));
		div.innerHTML = html;
		flashAtt.flashObj = document['mutiupload'];
	},

	show : function() {
		var list = flashAtt.flashObj.getQueue();
		getObj("uploadFileInfo")?getObj("uploadFileInfo").style.display="":0;
		var qlist = getObj('qlist');
		while (qlist.hasChildNodes()) {
			qlist.removeChild(qlist.firstChild);
		}
		for (var i in list)	{
			if (list[i].error == 'null')
				list[i].error = '';
			var tr = document.createElement('tr');
			tr.id  = 'l_' + i;
			var td = document.createElement('td');
			td.innerHTML = list[i].name;
			tr.appendChild(td);

			var td = document.createElement('td');
			td.innerHTML = Math.round(list[i].size/1024) + 'K';
			tr.appendChild(td);

			var td = document.createElement('td');

			if (list[i].error == '') {
				td.innerHTML = (list[i].loaded > -1 ? parseInt(100 * list[i].loaded / list[i].size) : '0') + '%';
			} else {
				switch (list[i].error) {
					case 'exterror':
						list[i].error = '附件类型不匹配';break;
					case 'toobig':
						list[i].error = '附件大小超过限制';break;
					case 'numerror':
						list[i].error = '附件个数超过限制';break;
				}
				td.innerHTML = list[i].error;
			}
			tr.appendChild(td);

			var td = document.createElement('td');
			td.innerHTML = '<span onclick="flashAtt.del(this)" style="cursor:pointer;">x</span>';
			tr.appendChild(td);

			qlist.appendChild(tr);
		}
	},
	
	progress:function(i,percent) {
		document.getElementById('l_'+i).getElementsByTagName('td')[2].innerHTML = percent + '%';
	},

	use : function() {
		ajax.send('pw_ajax.php?action=mutiatt','',function() {
			var rText = ajax.request.responseText.split('\t');
			if (rText[0] == 'ok') {
				eval(rText[1]);
				if (IsElement('flashatt')) {
					var o = getObj('flashatt');
					while (o.hasChildNodes()) {
						o.removeChild(o.firstChild);
					}
				} else {
					var o = document.createElement('tbody');
					o.id  = 'flashatt';
					getObj('attach').parentNode.insertBefore(o,getObj('attach'));
				}
				for (var i in att) {
					var s = getObj('att_mode').firstChild.cloneNode(true);
					s.id = 'flashatt_' + i;
					s.getElementsByTagName('select')[0].name = 'flashatt[' + i + '][special]';
					s.getElementsByTagName('select')[1].name = 'flashatt[' + i + '][ctype]';
					s.getElementsByTagName('input')[2].name = 'flashatt[' + i + '][needrvrc]';
					s.getElementsByTagName('input')[1].name = 'flashatt[' + i + '][desc]';

					var fname = s.getElementsByTagName('td')[0];
					fname.innerHTML = att[i][0] + '&nbsp;(' + att[i][1] + 'K)';
					fname.title = att[i][0] + '\n上传日期：' + att[i][3];
					fname.onmouseover = function() {flashAtt.view(this);};

					var li = document.createElement('span');
					li.id = 'atturl_' + i;
					li.innerHTML = att[i][2];
					li.style.display = 'none';
					fname.appendChild(li);

					var li = document.createElement("span");
					var a = document.createElement("a");
					a.className    = 'bta';
					a.unselectable = 'on';
					a.onclick      = function(){newAtt.add(this)};
					a.innerHTML    = '插入';
					li.appendChild(a);
					var a = document.createElement("a");
					a.saveId = i;
					a.className    = 'bta';
					a.onclick      = function(){flashAtt.remove(this)};
					a.innerHTML    = '移除';
					li.appendChild(a);
					s.lastChild.appendChild(li);

					o.appendChild(s);
				}
			} else {
				ajax.guide();
			}
		});
	},

	clear : function() {
		ajax.send('pw_ajax.php?action=delmutiatt','',function() {
			if (ajax.request.responseText == 'ok') {
				if (IsElement('flashatt')) {
					getObj('flashatt').parentNode.removeChild(getObj('flashatt'));
				}
				getObj('flashAtt_use').style.display = 'none';
				getObj('flashAtt_clear').style.display = 'none';
				//getObj('mutiinfo').parentNode.removeChild(getObj('mutiinfo'));
			} else {
				ajax_guide();
			}
		});
	},

	view : function(o) {
		var id = o.parentNode.id;
		id = id.substr(id.lastIndexOf('_') + 1);
		var path = getObj('atturl_' + id).innerHTML;
		if (path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig)) {
			newAtt.getimage(path, 320, 'flashatt_' + id);
		}
	},

	finish : function() {
		flashAtt.show();
		flashAtt.use();
		getObj("uploadFileInfo")?getObj("uploadFileInfo").style.display="none":0;
	},

	del : function(o) {
		var s  = o.parentNode.parentNode;
		var id = s.id.substr(2);
		flashAtt.flashObj.remove(id);
		flashAtt.show();
	},

	remove : function(o) {
		ajax.send('pw_ajax.php?action=delmutiattone','aid='+o.saveId,function() {
			if (ajax.request.responseText == 'ok') {
				var s = o.parentNode.parentNode.parentNode;
				s.parentNode.removeChild(s);
			} else {
				ajax_guide();
			}
		});
	}
}

var photoflashAtt = {

	baseurl : 'mode.php?m=o&q=photos&',
	photoflashObj : null,

	init : function() {
		if (IsElement('flash')) {
			getObj('flash').parentNode.removeChild(getObj('flash'));
			return;
		}
		var div = document.createElement('div');
		div.id  = 'flash';

		var flashvar = 'url=' + getObj('headbase').href + 'job.php';
		if (is_ie) {
			var html = '<object id="mutiupload" name="mutiupload" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="250" height="46"><param name="movie" value="' +  imgpath + '/uploadphoto.swf" /><param name="FlashVars" value="' + flashvar + '"/><param name="allowScriptAccess" value="always" /><param name="wmode" value="transparent" /></object>';
		} else {
			var html = '<embed type="application/x-shockwave-flash" src="' + imgpath + '/uploadphoto.swf" width="250" height="46" id="mutiupload" name="mutiupload" allowScriptAccess="always" wmode="transparent" FlashVars="' + flashvar + '" />';
		}
		html += '<table width="450"><tr><td>文件名</td><td>描述</td><td width="15%">大小</td><td width="20%">上传进度</td><td width="5%">删</td></tr><tbody id="qlist"></tbody></table>';
		getObj('attsize').parentNode.insertBefore(div,getObj('attsize'));
		div.innerHTML = html;
		photoflashAtt.photoflashObj = document['mutiupload'];
	},

	show : function(s) {

		var list = photoflashAtt.photoflashObj.getQueue();
		var qlist = getObj('qlist');

		var aid = getObj('aidsel_info');
		if (aid.value == '') {
			showDialog('success','请选择相册',2);
			return false;
		}
		photoflashAtt.getaid(aid.value);

		while (qlist.hasChildNodes()) {
			qlist.removeChild(qlist.firstChild);
		}
		for (var i in list)	{
			if (list[i].error == 'null')
				list[i].error = '';
			var tr = document.createElement('tr');
			tr.id  = 'l_' + i;
			var td = document.createElement('td');
			td.innerHTML = list[i].name;
			tr.appendChild(td);

			var td = document.createElement('td');
			var pintro_id = 'pintro_' + i;
			var pintro = list[i].name.split('.');
			if (s) {
				td.innerHTML = '<input name="pintro" id="' + pintro_id + '" type="text" value="' + pintro[0] + '">';
			} else {
				td.innerHTML = '锁定描述';
			}

			tr.appendChild(td);

			var td = document.createElement('td');
			td.innerHTML = Math.round(list[i].size/1024) + 'K';
			tr.appendChild(td);

			var td = document.createElement('td');
			if (list[i].error == '') {
				td.innerHTML = (list[i].loaded > -1 ? parseInt(100 * list[i].loaded / list[i].size) : '0') + '%';
			} else {
				switch (list[i].error) {
					case 'exterror':
						list[i].error = '附件类型不匹配';break;
					case 'toobig':
						list[i].error = '附件大小超过限制';break;
					case 'numerror':
						list[i].error = '附件个数超过限制';break;
				}
				td.innerHTML = list[i].error;
			}
			tr.appendChild(td);

			var td = document.createElement('td');
			td.innerHTML = '<span onclick="photoflashAtt.del(this)" style="cursor:pointer;">x</span>';
			tr.appendChild(td);

			qlist.appendChild(tr);
		}
	},

	getaid : function(aid) {
		photoflashAtt.photoflashObj.getalbumid(aid);
		photoflashAtt.getallowmutinum();
	},

	getallowmutinum : function() {
		var aid = getObj('aidsel_info');
		ajax.send(photoflashAtt.baseurl + 'a=getallowflash&aid='+aid.value,'',function() {
		var rText = ajax.request.responseText.split('\t');
			if (rText[0] == 'ok') {
				if (rText[1]) {
					photoflashAtt.photoflashObj.getallowmutinums(rText[1]);
				}
			} else {
				ajax.guide();
			}
		});
	},

	getAllNames : function() {
		var photolist = photoflashAtt.photoflashObj.getQueue();
		var values="";
		for (var i in photolist) {
			if (photolist[i].error == 'null')
				photolist[i].error = '';
			values += getObj('pintro_' + i).value+'|';
		}
		values = encodeURI(values);
		photoflashAtt.photoflashObj.beginUpload("filenames="+values.slice(0,-1));
	},

	view : function(o) {
		var id = o.parentNode.id;
		id = id.substr(id.lastIndexOf('_') + 1);
		var path = getObj('atturl_' + id).innerHTML;
		if (path.match(/\.(jpg|gif|png|bmp|jpeg)$/ig)) {
			newAtt.getimage(path, 320, 'att_' + id);
		}
	},

	finish : function() {
		photoflashAtt.show();
		var toaid = getObj('aidsel_info').value;
		ajax.send('apps.php?q=ajax&a=mutiuploadphoto','&aid=' + toaid,function(){
			var rText = ajax.request.responseText;
			if (rText == 'success') {
				photoflashAtt.photoflashObj.stopupload();
				read.setMenu(photoflashAtt.jumpphoto(toaid));
				read.menupz();
			} else {
				ajax.guide();
				setTimeout(window.location.reload(),2000);
			}
		});
	},

	jumpphoto : function(toaid) {
		var maindiv	= elementBind('div','','','width:300px;height:100%');
		var title = elementBind('div','','h');
		title.innerHTML = '上传成功!';
		maindiv.appendChild(title);
		var innerdiv = addChild(maindiv,'div','','menu-text p10');
		var ul = addChild(innerdiv,'ul','');
		var li = addChild(ul,'li');
		li.innerHTML = '照片上传成功，是否继续上传？<br />注：附件超过大小或超过相册数将上传不成功！';

		var footer	= addChild(maindiv,'div','','tr2','padding:5px;');
		var tar	= addChild(footer,'div','','tar');
		var ok	= elementBind('input','','bt3');
		ok.setAttribute('type','button');
		ok.setAttribute('value','继续');

		ok.onclick	= function () {
			window.location.href = photoflashAtt.baseurl + 'a=upload&job=flash&aid=' + toaid;
		}

		var toview	= elementBind('input','','bt','margin-left:8px');
		toview.type	= 'button';
		toview.value= '浏览';
		toview.onclick	= function () {
			window.location.href = photoflashAtt.baseurl + 'a=album&aid=' + toaid;
		}

		tar.appendChild(ok);
		tar.appendChild(toview);

		return maindiv;
	},

	del : function(o) {
		var s  = o.parentNode.parentNode;
		var id = s.id.substr(2);
		photoflashAtt.photoflashObj.remove(id);
		photoflashAtt.show(1);
	},

	remove : function(o) {
		var s = o.parentNode.parentNode.parentNode;
		s.parentNode.removeChild(s);
	},
	progress:function(i,percent) {
		document.getElementById('l_'+i).getElementsByTagName('td')[3].innerHTML = percent + '%';
	}
}