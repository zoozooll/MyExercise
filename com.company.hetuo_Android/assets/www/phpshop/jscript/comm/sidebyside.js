//should be removed, check the "id-" for 5 prod cookies
function storeID(theElement){
  var cookiename = "muant_sdid=";
  var oldcookie = readCookie(cookiename);
  var theProdID = "," + theElement.value + "#";
  oldcookie = removeIDstr(oldcookie, theProdID);
  if (theElement.checked){
	  if(oldcookie.length > 0) {
		  var cookienum = oldcookie.split('#');  
		  cookienum = cookienum.length - 1;
		  if(cookienum >=5){  		  		
				alert("您已经选择了5个商品，最多只能比较5个商品。谢谢!");  		
				theElement.checked = false;
				return false;	
		  }
	  }
  	  oldcookie = oldcookie + theProdID;  	
  }
  document.cookie = cookiename + oldcookie;// + getCookieKey();
}

function removeIDstr(myString, pattern) {
   var newString = myString.replace(pattern,"");
   return(newString);
}

function removeID(theID) {
  var cookiename = "muant_sdid=";
  var oldcookie = readCookie(cookiename);
  var theProdID = "," + theID + "#";
  oldcookie = removeIDstr(oldcookie, theProdID);
  document.cookie = cookiename + oldcookie;// + getCookieKey();
}

function removeAllID() {
  var cookiename = "muant_sdid=";
  document.cookie = cookiename + getCookieKey();
}

function readCookie(theCookie) {
  var allcookies = document.cookie;
  var value = "";
  var pos = allcookies.indexOf(theCookie);
  if (pos != -1) {
    var start = pos + theCookie.length;
	var end = allcookies.indexOf(";", start);

	if (end == -1) end = allcookies.length;
	value = allcookies.substring(start, end);
	value = unescape(value);
  }
  return (value);
}

function getCookieKey() {
	domain = location.host;
	if(domain.indexOf("www") == 0) {
		domain = domain.substring(3);
	}
	return "; path=/; domain=" + domain;
}
function checkSelect(tagname) {
	var cookiename = "muant_sdid=";
	var oldcookie = readCookie(cookiename);
	var byside = document.getElementsByName(tagname);

	var val = '';
	for(i = 0; i<byside.length; i++) {
		val = "," + byside[i].value + "#";
		if(oldcookie.indexOf(val) != -1) {
			byside[i].checked = true;
		}
	}
}