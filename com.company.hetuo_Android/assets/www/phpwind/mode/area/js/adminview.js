var frontAdmin = Class({},
{
	Create: function (container,tag) {
		this.container = container;
		this.editClass = 'open-none';
		this.tag = tag;
		this.current = null;
		this._init();
	},
	_init: function () {
		var list = getElementsByClassName(this.tag,this.container);
		var self = this;
		for (var i = 0; i< list.length; i++ ) {
			this._addEvent(list[i],'mouseover',this._mouseover);
			this._addEvent(list[i],'mouseout',this._mouseout);
		}
		this._initEditLink();
	},
	_mouseover: function () {
		this.className = 'cc view-current';
	},
	_initEditLink:	function () {
		var list = getElementsByClassName(this.editClass,this.container);
		for (var i=0;i<list.length; i++ ) {
			if (!list[i].id) continue;
			list[i].onclick = function () {
				sendmsg('mode.php?m=area&q=frontadmin&invokename='+this.id+'&cateid='+cateid,'',this);
			}
		}
	},
	_mouseout: function () {
		this.className = 'cc view-hover';
	},
	_addEvent: function (el,evname,func) {
		var self = this;
		if(document.all) {
			el.attachEvent("on" + evname,function(){
				self._changView(el,func);
			});
		} else {
			el.addEventListener(evname,function(){
				self._changView(el,func);
			},true);
		}
	},
	_changView:function(el,func){
		if (this.current) {
			this.current.className = 'cc view-hover';
		}
		this.current = el;
		func.call(el);
	},
	
	_getElementsByClassName : function (className, parentElement) {
		if (typeof(parentElement)=='object') {
			var elems = parentElement.getElementsByTagName("*");
		} else {
			var elems = (document.getElementById(parentElement)||document.body).getElementsByTagName("*");
		}
		var result=[];
		for (i=0; j=elems[i]; i++) {
		   if ((" "+j.className+" ").indexOf(" "+className+" ")!=-1) {
				result.push(j);
		   }
		}
		return result;
	}
}
);

function countlen(element,num) {
	if (element.value.length>num){
		alert('超过字数限制');
		element.value=element.value.substring(0,num);
	}
	return true;
}

function addPush(invokepieceid,fid,loopid,cateid) {
	
	var url = "mode.php?m=area&q=frontadmin&action=addpush&invokepieceid="+invokepieceid+"&fid="+fid+"&loopid="+loopid+'&cateid='+cateid;

	var pushtid= 0;
	if (getObj('pushtid')) {
		pushtid = getObj('pushtid').value;
	}
	ajax.send(url,'pushtid='+pushtid,ajax.get);
}

function delPush(element,pushid,cateid) {
	if (!confirm("确定要删除此条目吗？")){
		return false;
	}
	var url = "mode.php?m=area&q=frontadmin&action=deletepush";
	ajax.send(url,'pushid='+pushid+'&cateid='+cateid,function () {
		if (ajax.request.responseText=='success') {
			delTr(element);
		} else {
			alert(ajax.request.responseText);
		}
	});
}
