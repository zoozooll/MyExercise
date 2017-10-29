// JavaScript Document
function callServer(strURL,id, nextURL,nextId){
	var xmlHttp = getXMLHttpRequest();
	xmlHttp.open('GET', strURL, true);
	xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xmlHttp.onreadystatechange = function(){
		if (xmlHttp.readyState == 4){
			if(xmlHttp.status == 200) {
				if(id == false) {
					if(xmlHttp.responseText != '' || xmlHttp.responseText != null) {
						alert(xmlHttp.responseText);
						if(nextId != null) {
							callServer(nextURL,nextId, null, null);
						}
						return;
					}
				} else if(nextURL != null && id != null) {
					callServer(nextURL,id, null);
				} else {
					updatepage(xmlHttp.responseText,id);
					return;
				}
			}
		}
	}
	xmlHttp.send(null);
}
function updatepage(content, id){
	if(id == null) {
		return content;
	}
	document.getElementById(id).innerHTML = content;
	return true;
}

function postServer(strURL,val,id, nextURL,nextId) {
	var xmlHttp = getXMLHttpRequest();
	xmlHttp.open('POST', strURL, true);
	xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xmlHttp.send(val);
	xmlHttp.onreadystatechange = function(){
		if (xmlHttp.readyState == 4){
			if(id == false) {
				if(xmlHttp.responseText != '' || xmlHttp.responseText != null) {
					alert(xmlHttp.responseText);
					if(nextId != null) {
						callServer(nextURL,nextId, null, null);
					}
					return;
				}
			} else if(id == null || id == '') {
				return updatepage(xmlHttp.responseText,id);
			} else if(nextURL != null && id != null) {
				callServer(nextURL,id, null, null);
			} else {
				updatepage(xmlHttp.responseText,id);
				return;
			}
		}
	}
}

function getXMLHttpRequest(){
	var xmlHttp;
	if(window.XMLHttpRequest){ 
		var xmlHttp = new XMLHttpRequest();
	}else if(window.ActiveXObject){ 
		var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlHttp;
}