
function PwTags(){
	this.tagdb	= null;
	this.obj	= null;
	this.input	= null;
	this.hide	= true;
	this.menu	= null;
}

PwTags.prototype = {

	init : function(){
		if(tag.obj == null){
			ajax.send('pw_ajax.php','action=tag',function(){tag.create(1);});
		} else{
			tag.show();
		}
	},
	
	create : function(type){
		tag.tagdb = new Array();
		var s = ajax.request.responseText.split("\t");
		for(var i=0;i<s.length;i++){
			if(s[i]){
				var r = s[i].split(',');
				tag.tagdb[i] = [r[0],r[1]];
			}
		}
		tag.obj	= document.createElement("div");
		tag.obj.onmouseover = function(){tag.hide = false;};
		tag.obj.onmouseout  = function(){tag.hide = true;};
		tag.obj.style.cssText = 'border:1px solid #000;width:263px;display:none;background:#fff';
		tag.input = getObj('atc_tags');
		tag.input.onkeyup = function(){tag.show();};
		tag.input.onblur  = tag.close;
		tag.input.parentNode.appendChild(tag.obj);
		if(type == 1){
			tag.tagmenu();
		} else{
			tag.get();
		}
	},

	tagmenu : function(){
		tag.menu = getObj('tagmenu');
		var html = '';
		for(var i=0;i<tag.tagdb.length;i++){
			var s = tag.tagdb[i];
			html += '<a href="javascript:;" class="gray" onclick="tag.addtag(this);" style="cursor:pointer;margin:5px;">'+s[0]+'</a>';
			if(i>98) break;
		}
		tag.menu.lastChild.innerHTML = html;
		tag.menu.style.display = '';
	},

	show : function(){
		if(tag.menu == null){
			tag.tagmenu();
		}
		var str = tag.input.value;
		str = str.replace(/^\s+/g,'');
		var pos = str.lastIndexOf(' ') + 1;
		str = str.substr(pos,str.length);

		if(str == ''){
			tag.obj.style.display = 'none';
			return;
		}
		var html = '';
		var num  = 0;
		for(var i=0;i<tag.tagdb.length;i++){
			var s = tag.tagdb[i];
			if(s[0].indexOf(str)==0){
				html += '<div onmouseover="this.style.background=\'#00f\'" onmouseout="this.style.background=\'\'" onclick="tag.insert(this);" style="padding:1px 2px"><span class="fr">(' + s[1] + ')</span><span>' + s[0] + '</span></div>';
				if(++num>9) break;
			}
		}
		if(html==''){
			tag.obj.style.display = 'none';
			return;
		}
		var o = getObj("atc_tags");
		var left  = findPosX(o) + getLeft() + 1;
		var top   = findPosY(o) + getTop() + 25;
		
		tag.obj.style.display = '';
		tag.obj.style.top  = top  + 'px';
		tag.obj.style.left = left + 'px';

		tag.obj.innerHTML = html;
	},

	insert : function(o){
		tag.hide = true;
		var str = tag.input.value;
		var pos = str.lastIndexOf(' ') + 1;
		var laststr = str.substr(pos,str.length);
		tag.input.value = str.substr(0,pos) + o.lastChild.innerHTML;
		tag.close();
	},

	addtag : function(o){
		var str = tag.input.value.replace(/^\s+/g,'').replace(/\s+$/g,'');
		str = str.replace(/\s+/g,' ');
		if(str.split(' ').length > 4){
			if (typeof showDialog == 'function') {
				showDialog('warning','最多可以添加5个标签');
			} else {
				alert('最多可以添加5个标签');
			}
		} else{
			tag.input.value = str + (str ? ' ' : '') + o.innerHTML;
		}
	},

	get : function(){
		if(tag.tagdb == null){
			ajax.send('pw_ajax.php','action=tag',function(){tag.create(2);});
			return;
		}
		var num     = 0;
		var gettags = '';
		var subject = document.FORM.atc_title.value;
		var content = editor.getHTML();
		for(var i=0;i<tag.tagdb.length;i++){
			var s = tag.tagdb[i];
			if(subject.indexOf(s[0]) != -1 || content.indexOf(s[0]) != -1){
				gettags += gettags ? ' '+s[0] : s[0];
				if(++num>4) break;
			}
		}
		if(gettags){
			this.input.value = gettags;
		} else{
			if (typeof showDialog == 'function') {
				showDialog('warning','没有可用的标签');
			} else {
				alert('没有可用的标签');
			}
		}
	},

	close : function(){
		if(tag.hide){
			tag.obj.style.display = "none";
		}
	}
}

var tag = new PwTags();