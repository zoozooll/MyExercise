var $ = function (id) {
	return "string" == typeof id ? document.getElementById(id) : id;
}
function promote_change(obj) {
	var parent_node = obj.parentNode;
	var elements = parent_node.children;
	for(var i=0; i<elements.length; i++) {
		elements[i].className = '';
	}
	obj.className = "selected";
}
function nTabs(tabObj,obj){
	var tabList = $(tabObj).getElementsByTagName('li');
	for(i=0;i<tabList.length;i++){
	   if(tabList[i].id == obj.id)
	   {
		    $(tabObj+"_title"+i).className = "selected"; 
		    $(tabObj+"_content"+i).style.display = "";
	   }else{
	   		$(tabObj+"_title"+i).className = ""; 
			$(tabObj+"_content"+i).style.display = "none";
	   }
	} 
}
function changeMenu(){
	var allElements = document.getElementsByTagName('ul');
	for(i=0;i<allElements.length;i++){
		if(allElements[i].className == 'mainnav'){
			var childElements = allElements[i].getElementsByTagName('li');
			for(var j=0;j<childElements.length;j++){
				childElements[j].onclick = changeStyle;
			}
		}
	}
}
function changeStyle2(classname,obj){
	var tagList = obj.parentNode;
	var tagOptions = tagList.getElementsByTagName('a');
	for(i=0;i<tagOptions.length;i++){
		if(tagOptions[i].className.indexOf('selected')>=0){
			tagOptions[i].className = '';
		}
	}
	obj.className = 'selected';
	var list = document.getElementById('listcontent');
	if(classname=='list'){
		list.className = 'c_m';
	}else{
		list.className += ' window_type';
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
//window.onload = changeMenu;
