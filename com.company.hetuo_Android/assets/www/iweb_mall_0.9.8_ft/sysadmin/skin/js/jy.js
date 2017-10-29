//定义$函数
function nTabs(){
	var allElements = document.getElementsByTagName('ul');
	for(i=0;i<allElements.length;i++){
		if(allElements[i].className == 'menu'){
			var childElements = allElements[i].getElementsByTagName('li');
			for(var j=0;j<childElements.length;j++){
				childElements[j].onclick = changeStyle;
			}
		}
	}
}
function changeStyle(){
	var tagList = this.parentNode;
	var tagOptions = tagList.getElementsByTagName("li");
	for(i=0;i<tagOptions.length;i++){
		if(tagOptions[i].className.indexOf('active')>=0){
			tagOptions[i].className = '';
		}
	}
	this.className = 'active';
}
function changeMenu(obj){
	var tagList = obj.parentNode;
	var tagOptions = tagList.getElementsByTagName("li");
	var tagOptionsLen = tagOptions.length;
	for(i=1;i<tagOptionsLen;i++){
		if(tagOptions[i].className.indexOf('active')>=0){
			tagOptions[i].className = '';
		}
	}
	obj.className = 'active';
}

function addLoadEvent(func){
	var oldonload=window.onload;
	if(typeof window.onload!="function"){window.onload=func;}else{window.onload=function(){oldonload();func();}};
}