function delYesOrNo(msn) {
	if(msn == null) msn = '您确定要删除么？';
	x=confirm(msn);
	if(x) {
	} else {
		return false;
	}
}
function menuFix() {
    var sfEls = document.getElementById("ulTopNav").getElementsByTagName("li");
    for (var i=0; i<sfEls.length; i++) {
        sfEls[i].onmouseover=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseDown=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseUp=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onmouseout=function() {
        this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"),"");
        }
    }
}
function classesFix() {
    var sfEls = document.getElementById("ClassesMenu").getElementsByTagName("li");
    for (var i=0; i<sfEls.length; i++) {
        sfEls[i].onmouseover=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseDown=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseUp=function() {
        this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onmouseout=function() {
        this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"),"");
        }
    }
}
function callLoad(id, msg) {
	if(msg==null) {
		msg = '正在载入,请稍后...';
	}
	document.getElementById(id).innerHTML = '<div id="load"><img src="images/default/icon_loading.gif" /><br/><br/>'+msg+'<br/></div>';
}
function setHtml(id, show) {
	document.getElementById(id).innerHTML = show;
}
function displayDiv(id, show) {
	var div = document.getElementById(id);
	if(show == false) {
		div.style.display = 'none';
	} else {
		div.style.display = 'block';
	}
}