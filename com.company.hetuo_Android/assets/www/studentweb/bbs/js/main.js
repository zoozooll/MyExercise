var path='';

if(bbsStyleCookie=getCookie('162100bbsStyle')){
  document.write('<link rel="stylesheet" type="text/css" href="css/style'+bbsStyleCookie+'.css">');
}

//取cookies函数unescape
function getCookie(name){
  var arr=document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
  if(arr!=null && arr!=false) return decodeURIComponent(arr[2]); return false;
}

//两个参数，一个是cookie的名子，一个是值escape
var cookieEnabled=(typeof navigator.cookieEnabled!='undefined' && navigator.cookieEnabled)?true:false;
function setCookie(name,value,time){
  if(cookieEnabled==false){ alert('您的浏览器未开通cookie，执行失败！'); return false; }
  if(!time || isNaN(time)) time=365; //此 cookie 将被保存 365 天
  var expires=new Date();    //new Date("December 31, 9998");
  expires.setTime(expires.getTime()+time*24*60*60*1000);
  var cookieStr=encodeURIComponent(value);
  if(cookieStr.length>3072){ //alert('所设置的cookie数据太大，系统将自动缩减');
    cookieStr=cookieStr.substring(0,3072).replace(/[\|\s]+$/,'');
  }
  document.cookie=name+'='+cookieStr+';expires='+expires.toGMTString()+';path=/;';
}

//是否登录
function confirmLogin(){
  if(getCookie('usercookie').toString().match(/^[^\|]+\|[^\|]+\|[\w]*$/)) //其中数字是等级分
    return true;
  return false;
}

//是否管理员
function confirmAdmin(){
  if(getCookie('usercookie').toString().match(/^[^\|]+\|[^\|]+\|manager$/)){ //其中数字是密匙
    return true;
  }
  return false;
}

function addFavor(){
  var thisTitle=document.title;
  var thisUrl=location.href;
  if(document.all)
    window.external.AddFavorite(thisUrl,thisTitle);
  else if(window.sidebar)
    window.sidebar.addPanel(thisTitle,thisUrl,"");
  return false;
}

function SetHome(obj,vrl){
  try{
    obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
  }catch(e){
    if(window.netscape){
      try{
        netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
      }catch(e){
        alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将[signed.applets.codebase_principal_support]设置为'true'"); 
      }
      var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
      prefs.setCharPref('browser.startup.homepage',vrl);
    }
  }
}

function cCss(cssNow){
  switch(cssNow){
    case 'yellow':
	case 'green':
    /*
    case 'red':
    case 'black':
	*/
      bj='_'+cssNow;
      break;
    default:
      bj='';
  }
  setCookie('162100bbsStyle',bj,365);
  location.reload();
}













