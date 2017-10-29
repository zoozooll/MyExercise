//<![CDATA[ 
function fDragging(obj, e, limit){ 
	if(!e) e=window.event; 
	var x=parseInt(obj.style.left); 
	var y=parseInt(obj.style.top); 
	
	var x_=e.clientX-x; 
	var y_=e.clientY-y; 
	
	if(document.addEventListener){ 
		document.addEventListener('mousemove', inFmove, true); 
		document.addEventListener('mouseup', inFup, true); 
	} else if(document.attachEvent){ 
		document.attachEvent('onmousemove', inFmove); 
		document.attachEvent('onmouseup', inFup); 
} 

inFstop(e); 
inFabort(e) 

function inFmove(e){ 
	var evt; 
	if(!e)e=window.event; 

	if(limit){ 
		var op=obj.parentNode; 
		var opX=parseInt(op.style.left); 
		var opY=parseInt(op.style.top); 
	
		if((e.clientX-x_)<0) return false; 
	else if((e.clientX-x_+obj.offsetWidth+opX)>(opX+op.offsetWidth)) return false; 

	if(e.clientY-y_<0) return false; 
	else if((e.clientY-y_+obj.offsetHeight+opY)>(opY+op.offsetHeight)) return false; 
	//status=e.clientY-y_; 
} 

	obj.style.left=e.clientX-x_+'px'; 
	obj.style.top=e.clientY-y_+'px'; 
	
	inFstop(e); 
} // shawl.qiu script 
function inFup(e){ 
	var evt; 
	if(!e)e=window.event; 

	if(document.removeEventListener){ 
		document.removeEventListener('mousemove', inFmove, true); 
		document.removeEventListener('mouseup', inFup, true); 
	} else if(document.detachEvent){ 
		document.detachEvent('onmousemove', inFmove); 
		document.detachEvent('onmouseup', inFup); 
	} 

	inFstop(e); 
} // shawl.qiu script 

function inFstop(e){ 
	if(e.stopPropagation) return e.stopPropagation(); 
	else return e.cancelBubble=true; 
} // shawl.qiu script 
function inFabort(e){ 
	if(e.preventDefault) return e.preventDefault(); 
	else return e.returnValue=false; 
	} // shawl.qiu script 
} 
//]]> 
