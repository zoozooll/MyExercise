function InitAjax(){
	var ajax = false;
	//开始初始化XMLHttpRequest对象
	if(window.XMLHttpRequest){ //Mozilla 浏览器
		ajax = new XMLHttpRequest();
		if (ajax.overrideMimeType) {//设置MiME类别
			ajax.overrideMimeType("text/xml");
		}
	}else if(window.ActiveXObject){ // IE浏览器
		try{
			ajax = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				ajax = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){}
		}
	}

	if(!ajax){ // 异常，创建对象实例失败
		window.alert("不能创建XMLHttpRequest对象实例.");
		return false;
	}

	return ajax;
}

function ajax(url,action,data,callback,type,async) {
	// action: 提交方式 get,post
	// url: 请求地址
	// data: 提交数据
	// callback: 回调函数 function(data)
	// type: 返回数据类型 text,xml,json
	// async: 是否异步传输 true,false
	if(!url) { return false; }
	if(!action) { action = 'get'; }
	if(!callback) { callback = ajaxCallback; }
	if(!type) { type = 'text'; }
	if(!async) { async = true; }
	action = action.toLowerCase();
	type = type.toLowerCase();

	var ajaxmessageid = document.getElementById("ajaxmessageid");
	if(ajaxmessageid) {
		ajaxmessageid.style.display = '';
	}
	var xmlhttp = InitAjax();
	xmlhttp.open(action,url,async);
	xmlhttp.setRequestHeader("x-requested-with","XMLHttpRequest");
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.onreadystatechange = function(){
		if(xmlhttp.readyState==4 && xmlhttp.status==200) {
			var returndata = '';
			if(type=='json') {
				returndata = eval("(" + xmlhttp.responseText + ")");
			} else if (type=='xml') {
				returndata = xmlhttp.responseXML;
			} else {
				returndata = xmlhttp.responseText;
			}
			callback(returndata);
			if(ajaxmessageid) {
				ajaxmessageid.style.display = 'none';
			}
		}
	};
	xmlhttp.send(data);
}


function Ajax(){
	var HttpRequest=false;
	var Url=null;
	var ContentType="text";
	this.init=function ()//创建XMLHttpRequest的功能函数
	{
		if (window.ActiveXObject && !window.XMLHttpRequest)
 		{
			window.XMLHttpRequest = function()
	 		{
				var msxmls = ['Msxml2.XMLHTTP.5.0','Msxml2.XMLHTTP.4.0','Msxml2.XMLHTTP.3.0','Msxml2.XMLHTTP','Microsoft.XMLHTTP'];
				for (var i = 0; i < msxmls.length; i++)
		 		{
					try {
							return new ActiveXObject(msxmls[i]);
						}
					catch (e){}
				}
				return null;
			};
		}
		HttpRequest = new XMLHttpRequest();
		if(!HttpRequest)
			{
				return false;
			}
		return HttpRequest;
	}
	this.getType=function (type)//得到请求的类型
	{
		type=type.toUpperCase();
		if(type!="HEAD" && type!="POST" && type!="GET") type="HEAD";
		return type;	
	}
	this.getContentType=function (type)//要得到内容的类型XML/TEXT
	{
		type=type.toLowerCase();
		if("xml"==type)
		{
			ContentType="xml";
			return "text/xml";
		}
		else
		{
			ContentType="text";
		}
		if("text"==type) return "text/plain";
		if("app"==type) return "application/x-www-form-urlencoded";
		return "text/plain";
	}
	this.getInfo=function (url,type,content,send,id,unready)//主要的函数得到内容
	{
		HttpRequest=this.init();
		send=send.replace(/(^\s*)|(\s*$)/g,"");
		type=this.getType(type);
		if(type=='GET')
		{
			 if(url.indexOf("?")>0)
			 {
			 	if(send.substring(0,1)=='&')url=url+send;
			 	else url=url+"&"+send;
			 }
			 else url=url+"?"+send;
		}
		HttpRequest.open(type,url,true);
		HttpRequest.onreadystatechange=function ()//得到更新内容
		{
			
			if(HttpRequest.readyState==4)
			{
				if(HttpRequest.status==200)
				{
					if(!id) return;
					if("HEAD"==type)
					{
						if(id instanceof Function)
						{
							id(HttpRequest.getAllResponseHeaders());
						}
						else
						{
							if(document.getElementById(id))
							{
								document.getElementById(id).innerHTML=HttpRequest.getAllResponseHeaders();
							}
						}
					}
					else
					{
						if("text"==ContentType)
						{
							if(id instanceof Function)
							{
								id(HttpRequest.responseText.replace(/(^\s*)|(\s*$)|(　*)/g , ""));
							}
							else
							{
								if(document.getElementById(id))
								{
									document.getElementById(id).innerHTML="";
									document.getElementById(id).appendChild(changeHTML(HttpRequest.responseText.replace(/(^\s*)|(\s*$)|(　*)/g , "")));
								}
							}
						}
						else
						{
							if(id instanceof Function)
							{
								id(HttpRequest.responseXML);
							}
							else
							{
								if(document.getElementById(id))
								{
									document.getElementById(id).innerHTML=HttpRequest.responseXML;
								}
							}
						}
					}
				}
				else if(HttpRequest.status==404)
				{
					alert("请求的URL地址不存在！");
				}
				else if(HttpRequest.status==403)
				{
					alert("请求的URL地址禁止访问！");
				}
				else if(HttpRequest.status==401)
				{
					alert("请求的URL地址未经受权！");
				}
				else
				{
					alert("在请求URL的过程中，发生了如下错误："+HttpRequest.status);
				}
			}else{
					if(unready instanceof Function)
					{
						unready();
					}
			}
		}
		HttpRequest.setRequestHeader("cache-control","no-cache"); 
		if(this.getType(type)=="POST")
		{
			send=encodeURI(send);
			content="app";
		}
		else
		{
			send=null;
		}
		HttpRequest.setRequestHeader("Content-Type",this.getContentType(content)+";encoding=utf-8");
		HttpRequest.send(send);
	};
}
function changeHTML(html)
{
	var div=document.createElement("div");
	div.innerHTML=html;
	return div;
}