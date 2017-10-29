// JavaScript Document

function myshow(id, fill, top) {  
	if(fill == null) tofill(false);
	top = top == null ? 50 : top;
	var divshow = document.getElementById(id);
	divshow.style.display = "block";
	divshow.style.zIndex = "10001";
	
	
	if (divshow.getBoundingClientRect) {
		if (navigator.platform == "Win32" && navigator.appName == "Microsoft Internet Explorer") {
			divshow.style.top = Math.max(document.documentElement.scrollTop, document.body.scrollTop)+top+'px';
		} else {
			divshow.style.top = top+'px';
		}
	} else {
		divshow.style.top = top+'px';
	}
	divshow.style.left = (document.body.clientWidth-divshow.clientWidth)/2+'px';
	document.body.style.overflow = "hidden";
	document.documentElement.style.overflow = "hidden";
}  

function dispalymyshow(id, fill) {  
	if(fill == null) tofill(true);
	document.getElementById(id).style.display="none";
	document.body.style.overflow = "";
	document.documentElement.style.overflow = "";
} 
var p = 0;
function tofill(val) {  
	if(val == true) {
		var bgObj = document.getElementById('bgDiv');
		p = 0;
		document.body.removeChild(bgObj);
		setSelectElement(document, true);
	} else {
		var bgObj=document.createElement("div");
		bgObj.setAttribute('id','bgDiv');
		bgObj.style.position="absolute";
		bgObj.style.top="0";
		bgObj.style.background="#000000";
		bgObj.style.left="0";
		if (bgObj.getBoundingClientRect) {
			//bgObj.style.top=document.documentElement.scrollTop+"px";
		}
		bgObj.style.width="100%";
		//bgObj.style.height=document.documentElement.scrollTop+screen.height+"px";
		bgObj.style.height=Math.max(document.body.scrollHeight, document.documentElement.scrollTop+screen.height)+'px';//document.body.scrollHeight+"px";
		document.body.appendChild(bgObj);
		setSelectElement(document, false);
		setalpha();
		setTimeout("setalpha()",100);
		setTimeout("setalpha()",200);
		setTimeout("setalpha()",300);
		setTimeout("setalpha()",400);
		setTimeout("setalpha()",500);
		p = 0;
		bgObj.style.zIndex = "10000";
	}
}
function setalpha() {
	var bgObj = document.getElementById('bgDiv');
	bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity="+p+")";
	bgObj.style.opacity=p*0.01;
	p+=20;
}

function setSelectElement(node, dis) { 
	var total = 0;
	if(node.nodeType == 1) { 
		if(node.tagName == 'SELECT') {
			total++;			
			if(dis == false) {
				node.style.display = "none";
			} else {
				node.style.display = "";
			}
		}
	}
	var childrens = node.childNodes;		
	for(var i=0;i<childrens.length;i++) {
		total += setSelectElement(childrens[i], dis); 
	}
	return null;
}

function drag(target ,dragHandle) {
	dragHandle = dragHandle || target;
	dragHandle.onmousedown = function (e){
		e = window.event || e;
		var _xy = parseInt(target.offsetTop) - e.clientY;
		var _xx = parseInt(target.offsetLeft) - e.clientX;
		document.onmouseup = function(){
			this.onmousemove = null;
			this.onmouseup=null;
		}
		if(e.preventDefault){
			e.preventDefault();
		}
		document.onmousemove = function(e){
			var e = window.event || e;
			target.style.top = _xy + e.clientY + "px";
			target.style.left = _xx + e.clientX + "px";
		}
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
	};
}