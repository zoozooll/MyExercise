var agt = navigator.userAgent.toLowerCase();
var is_ie = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
var is_gecko= (navigator.product == "Gecko");
document.write("<script src='js/desktop/Compatibility.js'></sc"+"ript>");
document.write("<script src='js/lang/zh_cn.js'></sc"+"ript>");

var gIsPost = true;
window.getObj?0:getObj=function(s){return document.getElementById(s)};
$=getObj;

if (location.href.indexOf('/simple/') != -1) {
	getObj('headbase')?getObj('headbase').href = location.href.substr(0,location.href.indexOf('/simple/')+1):0;
} else if (location.href.indexOf('.html')!=-1) {
	var base = location.href.replace(/^(http(s)?:\/\/(.*?)\/)[^\/]*\/[0-9]+\/[0-9]{4,6}\/[0-9]+\.html$/i,'$1');
	if (base != location.href) {
		getObj('headbase')?getObj('headbase').href = base:0;
	}
}
~function()
{
	var FNArray=[];
	var D = document;
	/**
	 * 使用举例：
		window.onReady(FunctionName[,argu1,[argu2,[....]]]);
	 */
    window.onReady = function(fallBackFunction)
    {


		var argu=[];
		for (var i=1,len=arguments.length; i<len; i++)
		{
			argu.push(arguments[i]);
		}
		if (window.readyBound) return fallBackFunction.apply(this,argu);
		if(!is_ie) return 	fallBackFunction.apply(this,argu);
		FNArray.push(fallBackFunction);
        readyBound = true;
        var ready = 0;
        // Mozilla, Opera and webkit nightlies currently support this event
        if (D.addEventListener)
        {
            // Use the handy event callback
            D.addEventListener("DOMContentLoaded",
            function()
            {
                D.removeEventListener("DOMContentLoaded", arguments.callee, false);
                if (ready) return;
                ready = 1;
				for (var i=0,len=FNArray.length; i<len; i++)
				{
					FNArray[i] ? FNArray[i].apply(this,argu) : 0;
				}

            },
            false);

            // If IE event model is used
        } else if (D.attachEvent)
        {
            // ensure firing before onload,
            // maybe late but safe also for iframes
            D.attachEvent("onreadystatechange",
            function()
            {
                if (D.readyState === "complete")
                {
                    D.detachEvent("onreadystatechange", arguments.callee);

                    if (ready) return;
                    ready = 1;
                    for (var i=0,len=FNArray.length; i<len; i++)
					{
						FNArray[i] ? FNArray[i].apply(this,argu) : 0;
					}
                }
            });

            // If IE and not an iframe
            // continually check to see if the D is ready
            if (D.documentElement.doScroll && window == window.top)(function()
            {
                if (ready) return;
                try
                {
                    // If IE is used, use the trick by Diego Perini
                    // http://javascript.nwbox.com/IEContentLoaded/
                    D.documentElement.doScroll("left");
                } catch(error)
                {
                    setTimeout(arguments.callee, 0);
                    return;
                }
                ready = 1;
                for (var i=0,len=FNArray.length; i<len; i++)
				{
					FNArray[i] ? FNArray[i].apply(this,argu) : 0;
				}

            })();
        }
    };
}();
/**
 *验证码的，点其他地方消失的事件添加。
 */
function PW_popEvent (obj)
{
	if (typeof(obj) != 'object'){
		return false;
	}
	var a=obj.getElementsByTagName("*");
	for (var i=0,len=a.length; i<len; i++)
	{
		a[i].setAttribute("s",1);
	}
   document.body.onmousedown=function()
	{
	   if(!event.srcElement.getAttribute("s"))
		{
		   obj.style.display="none";
		}

	};

}
function getObj(id) {
	return document.getElementById(id);
}
function getElementsByClassName (className, parentElement){
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

function ietruebody() {
	/*
	if (getObj('upPanel')) {
		return getObj('upPanel');
	}
	*/
	return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body;
}
function getTop() {
	return typeof window.pageYOffset != 'undefined' ? window.pageYOffset:ietruebody().scrollTop;
}
function getLeft() {
	return (typeof window.pageXOffset != 'undefined' ? window.pageXOffset:ietruebody().scrollLeft)
}
function IsElement(id) {
	return document.getElementById(id) != null ? true : false;
}
function CopyCode(obj) {
	if (typeof obj != 'object') {
		if (is_ie) {
			window.clipboardData.setData("Text",obj);
			alert('复制成功!');
		} else {
			prompt('按Ctrl+C复制内容', obj);
		}
	} else if (is_ie) {
		var js = document.body.createTextRange();
		js.moveToElementText(obj);
		js.select();
		js.execCommand("Copy");
		alert('复制成功!');
	}
	return false;
}

~function() {
	var ifcheck = true;
	CheckAll = function (form,match) {
		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];
			if (e.type == 'checkbox' && (typeof match == 'undefined' || e.name.match(match))) {
				e.checked = ifcheck;
			}
		}
		ifcheck = ifcheck == true ? false : true;
	}
}();
/*
function CheckAll(form){
	for (var i=0;i<form.elements.length-5;i++){
		var e = form.elements[i];
		e.checked == true ? e.checked = false : e.checked = true;
	}
}
*/
function showcustomquest(qid,id){
	var id = id ? id : 'customquest';
	getObj(id).style.display = qid==-1 ? '' : 'none';
}
function showCK(){
	getObj('ckcode').style.display="";
	getObj('ckcode').style.zIndex="1000000";
	if (getObj('ckcode').src.indexOf('ck.php') == -1) {
		getObj('ckcode').src = 'ck.php?nowtime=' + new Date().getTime();
	}
}
function setTab(m,n){
	var tli=document.getElementById("menu"+m).getElementsByTagName("li");
	var mli=document.getElementById("main"+m).getElementsByTagName("div");
	for(i=0;i<tli.length;i++){
		tli[i].className=i==n?"hover":"";
		mli[i].style.display=i==n?"block":"none";
	}
}
function changeState() {
	var msg = ajax.request.responseText;
	if (msg == 1){
		getObj('stealth').className = '';
		getObj('iconimg').title = HEADER_HIDE;
		getObj('online_state').innerHTML = '<img src="'+IMG_PATH+'/stealth.png" align="absmiddle" />隐身';
	}else{
		getObj('stealth').className = 'hide';
		getObj('iconimg').title = HEADER_ONLINE;
		getObj('online_state').innerHTML = '<img src="'+IMG_PATH+'/online.png" align="absmiddle" />在线';
	}
}
function showcustomquest_l(qid){
	getObj('customquest_l').name = 'customquest';
	getObj('customquest_l').style.display = qid==-1 ? '' : 'none';
}

function checkinput(obj,val){
	if (obj.className.indexOf('gray')!=-1) {
		obj.value = '';
		obj.className = obj.className.replace('gray', 'black');
	} else if (val && obj.value=='') {
		obj.value = obj.defaultValue = val;
		if (obj.className.indexOf('black') == -1) {
			obj.className += ' gray';
		} else {
			obj.className = obj.className.replace('black', 'gray');
		}
	}
}
var mt;
function showLoginDiv(){
	mt = setTimeout('read.open(\'user-login\',\'show-login\',2,26);getObj(\'pwuser\').focus();',200);
	document.onmousedown = function (e) {
		var o = is_ie ? window.event.srcElement : e.target;
		if (!issrc(o)) {
			read.close();
			document.onmousedown = '';
		}
	}
}
function issrc(o) {
	var k = 0;
	while (o) {
		if (o == read.menu) {
			return true;
		}
		if (o.tagName.toLowerCase() == 'body' || ++k>10) {
			break;
		}
		o = o.parentNode;
	}
	return false;
}

function imgResize(o, size) {
	if (o.width > o.height) {
		if (o.width > size) o.width = size;
	} else {
		if (o.height > size) o.height = size;
	}
}
function ajaxurl(o) {
	read.obj = o;
	ajax.send(o.href,'',ajax.get);
	return false;
}

function sendurl(o,id) {
	read.obj = o;
	sendmsg(o.href,'',id);
	return false;
}
function showAnnouce(){
	var annouce = getObj('annouce_div');
	if (annouce.style.display == 'none') {
		annouce.style.display = '';
	} else {
		annouce.style.display = 'none';
	}
}

function showCK(){
	var a=getObj('ckcode2');
	if(!a)
	{
		a=	getObj('ckcode');
	}
	a.style.display="";
	if (a.src.indexOf('ck.php') == -1) {
		a.src = 'ck.php?nowtime=' + new Date().getTime();
	}
}
function showConInfo(uid,cyid){
	ajax.send('mode.php?m=o&q=group&a=uintro&cyid='+cyid+'&uid='+uid,'',ajax.get);
}

userCard = {
	t1	 : null,
	t2	 : null,
	menu : null,
	uids : '',
	data : null,
	init : function() {
		var els = getElementsByClassName('userCard');
		for (i = 0; i < els.length; i++) {
			if (els[i].id) {
				var sx = els[i].id.split('_');
				userCard.uids += (userCard.uids ? ',' : '') + sx[3];
				els[i].onmouseover = function() {
					var _ = this;
					clearTimeout(userCard.t2);
					userCard.t1 = setTimeout(function(){userCard.show(_.id);}, 800);
				}
				els[i].onmouseout = function() {
					clearTimeout(userCard.t1);
					if (userCard.menu) userCard.t2 = setTimeout(function(){userCard.menu.close();},500);
				}
			}
		}
	},
	show : function(id) {
		var sx = id.split('_');
		if (userCard.data == null) {
			try {
				ajax.send($('headbase').href + 'pw_ajax.php?action=showcard&uids=' + userCard.uids, '', function() {
					userCard.data = elementBind('div');
					userCard.data.innerHTML = ajax.runscript(ajax.request.responseText);
					userCard.show(id);
				})
			} catch(e){}
			return;
		}
		if (userCard.data.hasChildNodes()) {
			for (var i = 0; i < userCard.data.childNodes.length; i++) {
				if (userCard.data.childNodes[i].nodeType == 1 && userCard.data.childNodes[i].id != '' && userCard.data.childNodes[i].id.substr(9) == sx[3]) {
					userCard.menu ? 0 : userCard.menu = new PwMenu('userCard');
					userCard.menu.obj = $(sx[1] + '_' + sx[2]) || $(id);
					userCard.menu.setMenu(userCard.data.childNodes[i].outerHTML, '', 1);
					userCard.menu.menupz(userCard.menu.obj);
				}
			}
		}
	}
}

Class = function(aBaseClass, aClassDefine) {
	function class_() {
		this.Inherit = aBaseClass;
		for (var member in aClassDefine) {
			try{this[member] = aClassDefine[member];}catch(e){}		//针对opera,safri浏览器的只读属性的过滤
		}
	}
	class_.prototype = aBaseClass;
	return  new class_();
};
New = function(aClass, aParams) {
	function new_()	{
		this.Inherit = aClass;
		if (aClass.Create) {
			aClass.Create.apply(this, aParams);
		}
	}
	new_.prototype = aClass;
	return  new new_();
};
/* end */

function imgLoopClass(){
	this.timeout   = 2000;
	this.currentId = 0;
	this.tmp       = null;
	this.wrapId    = 'x-pics';
	this.tag       = 'A';
	this.wrapNum   = 0;
	this.total     = 10;
}
imgLoopClass.prototype = {
	/*对象选择器*/
	$ : function(id){
		return document.getElementById(id);
	},
	/*图片显示*/
	display : function(pics,currentId){
		for(i=0;i<pics.length;i++){
			if(i==currentId){
				var current = pics[i];
			}
			pics[i].style.display = "none";
		}
		current.style.display = "";
	},
	/*获取所有标签对象*/
	gets : function(){
		var wrapId = this.wrapId+this.wrapNum;
		var obj = this.$(wrapId);
		if(!obj){
			return false;
		}
		return this.$(wrapId).getElementsByTagName(this.tag);
	},
	/*轮显*/
	alternate : function(){
		var pictures = this.gets();
		if(!pictures){
			return false;
		}
		var length = pictures.length;
		this.currentId = this.currentId ? this.currentId : 0;
		if(this.currentId+1>length){
			this.currentId = 0;
		}
		this.display(pictures,this.currentId);
		this.currentId = this.currentId+1;
	},
	/*循环器*/
	loop : function(){
		this.alternate();
		var _this = this;
		t = setTimeout(function(){
			_this.loop();
		},this.timeout);
	},

	/*单页面多个图片轮播，最多十个*/
	imginit : function(){
		for(i=0;i<this.total;i++){
			var obj = this.$(this.wrapId+i);
			if(!obj){
				continue;
			}
			imgloop(i);/*调用外部通用接口*/
		}
	},

	init : function(){

	}
}
/*图片轮播调用接口*/
var imgloops = new imgLoopClass();
/*特殊图片轮播调用*/
function imgloop(num){
	var imgloops = new imgLoopClass();
	imgloops.wrapNum = num;
	imgloops.loop();
}
/*任务中心弹出控制*/
showJobPOP = function(){
	var pop = getObj("jobpop") || 0;
	var newjob = getObj("newjob");
	if(newjob){
		var num = newjob.getAttribute("num");
		if(!num){
			window.location.href = "jobcenter.php";
			return false;
		}
	}
	if(pop){
		pop.style.display='';
	}else{
		openjobpop("&job=show");/*必须显示*/
	}
	return false;
}
/*弹出任务中心界面*/
function openjobpop(param){
	var param = param ? param : '';
	ajax.send('pw_ajax.php?action=jobpop',param,function(){
		jobCenterRun(ajax.request.responseText);
	});
}