function flash(flash_url, imgLink, imgUrl, imgText, focus_width, focus_height, text_height) {
	var num = order.length - 1;

	var xb; 
	var pics=""; 
	var links=""; 
	var texts=""; 

	var swf_height = focus_height+text_height; 

	var j=0; 
	for (i=1;i<=num;i++) { 
		xb=order[i]; 
		if( (imgUrl[xb]!="") && (imgLink[xb]!="") ) { 
			if(j !=0){
				pics=pics+"|";
				links=links+"|";
				texts=texts+"|";
			}
			pics=pics+imgUrl[xb];
			links=links+imgLink[xb];
			texts=texts+imgText[xb];
			j++;
		}
	}
	//alert(pics); 
	document.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+ focus_width +'" height="'+ swf_height +'">');
	document.write('<param name="allowScriptAccess" value="sameDomain">');
	document.write('<param name="movie" value="' + flash_url + '">');
	document.write('<param name="quality" value="high">');
	document.write('<param name="bgcolor" value="#DADADA">');
	document.write('<param name="menu" value="false">');
	document.write('<param name="wmode" value="opaque">');
	document.write('<param name="FlashVars" value="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'">');
	document.write('<embed src="' + flash_url + '" wmode="opaque" FlashVars="pics='+pics+'&amp;links='+links+'&amp;texts='+texts+'&amp;borderwidth='+focus_width+'&amp;borderheight='+focus_height+'&textheight='+text_height+'" menu="false" bgcolor="#DADADA" quality="high" width="'+ focus_width +'" height="'+ swf_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"></embed>');
	document.write('</object>');
}

var flash_url = "skin/default/images/pixviewer.swf";
var imgUrl=new Array();
var imgLink=new Array();
var imgText=new Array();