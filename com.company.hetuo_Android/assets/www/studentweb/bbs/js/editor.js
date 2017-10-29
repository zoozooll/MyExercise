var maxWordCount=(typeof(liMaxCount)!='undefined' && !isNaN(liMaxCount))?parseInt(liMaxCount):50000; //最多字数限制

function getText(){
  if(typeof(contT)!='undefined' && contT!='' && contT!='false'){
    editor.document.body.innerHTML=document.mainform.content.value=contT;
  }
}
if(document.all){
  window.attachEvent('onload',getText);//对于IE
}else{
  window.addEventListener('load',getText,false);//对于FireFox
} 

document.writeln('<!-- 162100编辑器-改动型 -->');
document.writeln('<div id="editarea">');
document.writeln('<form name="mainform" id="mainform" method="post" onsubmit="return writeChk();" action="'+((typeof(formU)=='undefined' || !formU)?'':formU)+'">');
document.writeln(''+((typeof(formT)=='undefined' || !formT)?'<u><b id="a_subject"><span style="color:#FF6600">*</span> 标题（名称）：</b></u><br /><br /><input name="subject" id="subject" type="text" value="'+((typeof(subjT)=='undefined' || !subjT)?'':subjT)+'" size="50" maxlength="180" />':formT)+'');
document.writeln(''+((typeof(formF)=='undefined')?'':formF)+'');
document.writeln('<br /><br />');
document.writeln('<u><b id="a_content"><span style="color:#FF6600">*</span> 内容（介绍）：</b></u><br /><br />');
document.writeln('<span id="content_tool">代码状态下，请填写常用、准确、安全的html代码，否则系统自动进行过滤。如果您对此项不熟，请切换到设计状态填写。</span>');
document.writeln('<span id="f_content_tool" style="display:none">'+addTools()+'</span>');
document.writeln('<span id="max_wordcount"></span>');
document.writeln('<textarea name="content" id="content"></textarea>');
document.writeln('<input type="submit" name="a" id="a" value="提交"></form>');

var editor,iframeStartSet,nowModeId,colors,toolbarTitle;
    
//判断浏览器版本级别
if(parseFloat(navigator.appVersion)>=4){
  document.getElementById('a').style.display='none';
  document.writeln('<span id="add_toolbar"></span>');
  document.writeln('<iframe name="f_content" id="f_content" marginwidth="3" marginheight="3" src="about:blank" allowTransparency="true"></iframe>');
  document.writeln('<div><input id="f_content_mode" type="button" value="设计" class="tool_img" onclick="toMode(\'f_content\',\'content\')"><input id="content_mode" type="button" value="代码" class="tool_img" onclick="toMode(\'content\',\'f_content\')"><input type="button" value="↓" title="纵向增高编辑区" class="tool_img" onclick="changeEditorSizeY(150)"><input type="button" value="↑" title="纵向减小编辑区" class="tool_img" onclick="changeEditorSizeY(-150)"><input type="button" value="→" title="横向增减编辑区" class="tool_img" onclick="changeEditorSizeX(this)"> <input type="button" value="提交" onclick="runSubmit()"> <input type="button" value="重置" onclick="runReset()"> 提示Shift+Enter小换行</div>');
  //设iframe为可编辑
  editor=document.getElementById('f_content').contentWindow;
  editor.document.designMode='on';
  editor.document.contentEditable=true;
  editor.document.open();
  editor.document.writeln('<html><head><style type="text/css"><!-- body,th,td{font-size:12px;} //--></style></head><body></body></html>');
  editor.document.close();
  editor.document.body.onblur=function(){
    document.mainform.content.value=editor.document.body.innerHTML;
  }
  iframeStartSet=setTimeout("startF()",0);
  getNowMode('f_content','content');
}

document.writeln('</div>');
runGetWordCount();

//缓存
function startF(){
  if(document.mainform.content.value!='')
    editor.document.body.innerHTML=document.mainform.content.value;
  if(iframeStartSet)
    window.clearTimeout(iframeStartSet);
}

//工具图标
function addTools(){
  var tools='';
  var toolbar=new Array("RemoveFormat","Bold","Italic","Underline","StrikeThrough","insertorderedlist","insertunorderedlist","Indent","Outdent","Subscript","Superscript","JustifyCenter","JustifyFull","JustifyLeft","JustifyRight","15","16","17","18","19","20","21","InsertHorizontalRule","InsertMarquee","CreateLink","UnLink","Copy","Cut","Paste","Delete");
  toolbarTitle=new Array("删除格式","粗体字","斜体字","下划线","中划线","编号","项目符号","增加缩进量","减小缩进量","下标","上标","中对齐","两端对齐","左对齐","右对齐","插入图片","插入动画","插入影音文件","插入其它文件","插入表情","插入特殊符号","插入表格","插入水平线","插入滚动字幕","给所选内容添加链接","取消链接","复制所选","剪切所选","粘帖","删除");
  for(var i=0;i<toolbar.length;i++){
    tools+='<img src="'+path+'images/tools/'+toolbar[i].toLowerCase()+'.gif" title="'+toolbarTitle[i]+'" class="tool_img" align="absmiddle" onmousedown="this.className=\'tool_img_onmousedown\'" onmouseup="this.className=\'tool_img\'" onclick="toolClick(\''+toolbar[i]+'\')">';
  }
  var font=new Array("宋体","黑体","仿宋_GB2312","楷体_GB2312","Arial","Comic Sans MS","Courier New","Tahoma","Times New Roman","Verdana");
  tools+='<select style="width:49px" onchange="toolClick(\'FontName\',this.value);"><option selected="selected">字体</option>';
  for(var j=0;j<font.length;j++){
    tools+='<option style="font-family:'+font[j]+';" value="'+font[j]+'">'+font[j]+'</option>';
  }
  tools+='</select>';

  tools+='<select style="width:49px" onchange="toolClick(\'FontSize\',this.value);"><option selected="selected">字号</option>';
  for(var k=1;k<=7;k++){
    tools+='<option value="'+k+'">'+k+'</option>';
  }
  tools+='</select>';
  colors='';
  var color=new Array("Black","Silver","Gray","Yellow","Maroon","Red","Purple","Fuchsia","Green","Lime","Olive","Yellow","Navy","Blue","Teal","Aqua");
  for(var l=0;l<color.length;l++){
    colors+='<option style="background-color:'+color[l]+'" value="'+color[l]+'">'+color[l]+'</option>';
  }
  tools+='<select style="width:73px" onchange="toolClick(\'ForeColor\',this.value);"><option selected="selected">字体颜色</option>'+colors+'</select>';
  return tools;
}

//处理模式
function getNowMode(showModeId,hideModeId){
  document.getElementById(showModeId+'_tool').style.display=document.getElementById(showModeId).style.display='';
  document.getElementById(hideModeId+'_tool').style.display=document.getElementById(hideModeId).style.display='none';
  document.getElementById(showModeId+'_mode').disabled=true;
  document.getElementById(hideModeId+'_mode').disabled=false;
  nowModeId=showModeId;
}

//转变编辑状态
function toMode(showModeId,hideModeId){
  if(showModeId=='f_content'){
    editor.document.body.innerHTML=getNowValue();
    document.getElementById('add_toolbar').style.display='';
  }else{
    document.mainform.content.value=getNowValue();
    document.getElementById('add_toolbar').style.display='none';
  }
  getNowMode(showModeId,hideModeId);
}

//取值
function getNowValue(){
  if(nowModeId=='f_content'){
    return editor.document.body.innerHTML;
  }else{
    return document.mainform.content.value;
  }
}

//表单提交时将iframe值传给textarea
function runSubmit(){
  document.mainform.content.value=getNowValue();
  if(writeChk()!=false){
    document.mainform.submit();
  }
}

//重置
function runReset(){
  document.mainform.content.value='';
  editor.document.body.innerHTML='';
}

//max_wordcount
function runGetWordCount(){
  var calcCountTimer;
  document.getElementById('max_wordcount').innerHTML='字数'+getNowValue().length+'限'+maxWordCount+'';
  if(calcCountTimer){
    window.clearTimeout(calcCountTimer);
  }
  calcCountTimer=setTimeout('runGetWordCount()',1000);
}

//改变垂直编辑区尺寸
function changeEditorSizeY(size){
  var nowModeObj=document.getElementById(nowModeId);
  var areaHeight=parseInt(nowModeObj.offsetHeight);
  if(areaHeight+size>=150){
    nowModeObj.style.height=(areaHeight+size)+'px';
  }
}

//改变水平编辑区尺寸
function changeEditorSizeX(obj){
  var nowModeObj=document.getElementById('editarea');
  try{
    if(nowModeObj.style.width!='100%'){
      nowModeObj.style.width='100%';
      nowModeObj.style.clear='both';
      obj.value='←';
    }else{
      nowModeObj.style.width='444px';
      nowModeObj.style.clear='none';
      obj.value='→';
    }
  }catch(err){
  }
}



//命令处理
function toolClick(inId,val){
  editor.focus();
  if(inId=='CreateLink'){
    if(Url=prompt('为选择的文本添加链接——URL地址：','http://'))
      editor.document.execCommand('CreateLink',false,encodeURI(Url));
  //15：图片、16：动画、17：视频、18：其它文件、19：表情、20：符号、21：表格
  }else if(inId=="InsertMarquee"){
    if(txt=prompt('添加滚动字幕——文本：',''))
      insertFile('<marquee style="border:1px #808080 solid;">'+txt+'</marquee>',21);
  }else if(inId>=15 && inId<=21){
    var addToolbar=document.getElementById('add_toolbar');
    var theToolbar=document.getElementById('tool_'+inId);
    var theToolbarText='';
    var resetObjSize='';

    if(inId==15 || inId==16 || inId==17 || inId==21)
      resetObjSize='<font color=gray>（控件大小可在编辑器中更改）</font>';
    if(inId==21){
      theToolbarText='表格宽度：<select name="thetable_width" id="thetable_width"><option value="">默认</option><option value=" width=100%">100%</option><option value=" width=90%">90%</option><option value=" width=80%">80%</option><option value=" width=70%">70%</option><option value=" width=60%">60%</option><option value=" width=50%">50%</option></select> ';
      theToolbarText+='表格行数：<input name="thetable_rows" id="thetable_rows" type="text" value="2" size="5" maxlength="2" onKeyUp="isDigit(this)"> ';
      theToolbarText+='表格列数：<input name="thetable_cols" id="thetable_cols" type="text" value="2" size="5" maxlength="2" onKeyUp="isDigit(this)"><br /> ';
      theToolbarText+='对齐方式：<select name="thetable_align" id="thetable_align"><option value="">默认</option><option value=" align=left">居左</option><option value=" align=center">居中</option><option value=" align=right">居右</option></select> ';
      theToolbarText+='单元边距：<input name="thetable_spacing" id="thetable_spacing" type="text" value="0" size="5" maxlength="2" onKeyUp="isDigit(this)"> ';
      theToolbarText+='单元间距：<input name="thetable_padding" id="thetable_padding" type="text" value="3" size="5" maxlength="2" onKeyUp="isDigit(this)"><br /> ';
      theToolbarText+='背影颜色：<select name="thetable_bgcolor" id="thetable_bgcolor"><option style="background-color:white" value="White">White</option>'+colors+'</select> ';
      theToolbarText+='边框颜色：<select name="thetable_bordercolor" id="thetable_bordercolor">'+colors+'</select> 边框宽度：默认1px ';
      theToolbarText+='<input type="button" value="确定" onclick="insertFile(insertTableVal(),'+inId+')">';
    }else if(inId==20){
      var marks=new Array("※","§","〃","№","〓","○","●","△","▲","◎","☆","★","◇","◆","□","■","▽","▼","㊣","♀","♂","⊕","⊙","↑","↓","←","→","↖","↗","↙","↘","【","】","『","』","≈","≠","＝","≤","≥","＜","＞","≮","≯","∷","±","＋","－","×","÷","／","∫","∮","∝","∞","∧","∨","∑","∏","∪","∩","∈","∵","∴","⊥","∥","∠","⌒","⊙","≌","∽","√","≦","≧","≒","≡","～","∟","⊿","㏒","㏑","°","′","″","＄","￥","〒","￠","￡","％","＠","℃","℉","﹩","﹪","‰","﹫","㏕","㎜","㎝","㎞","㏎","㎡","㎎","㎏","㏄","°","○","¤","Ⅰ","Ⅱ","Ⅲ","Ⅳ","Ⅴ","Ⅵ","Ⅶ","Ⅷ","Ⅸ","Ⅹ","€","￥","￡","?","?","?","…");
      for(var i=0;i<marks.length;i++)
        theToolbarText+='<input type="button" onclick="insertFile(\''+marks[i]+'\','+inId+')" value="'+marks[i]+'">';
    }else if(inId==19){
      for(var i=1;i<=50;i++)
        theToolbarText+='<img src="'+path+'images/smiley/'+i+'.gif" onclick="toolClick(\'InsertImage\',this.src)">';
    }else{
      theToolbarText='<form name="uploadform'+inId+'" enctype="multipart/form-data" action="'+path+'run.php?run=upload&in_id='+inId+'&pathUrl='+path+'" method="post" target="lastFrame" onsubmit="if(this.uploadfile.value==\'\'){alert(\'上传文件不能为空！\');return false;}">';
      theToolbarText+='链接：<input name="linkfile" type="text" size="40"> <input type="button" value="确定" onclick="insertFile(this.form.linkfile.value,'+inId+')"><br />';
      theToolbarText+='上传：<input name="uploadfile" type="file"> <input type="submit" value="确定">';
      theToolbarText+='</form>';
    }

    if(!theToolbar){
      addToolbar.innerHTML+='<fieldset id="tool_'+inId+'"><legend><b>'+toolbarTitle[inId]+'</b>/插入之前请用光标定好位置'+resetObjSize+'</legend><span class="close" onclick="toolClick('+inId+')" title="关闭">×</span>'+theToolbarText+'</fieldset>';
    }else{
      addToolbar.removeChild(theToolbar);
    }
  }else{
    editor.document.execCommand(inId,false,val);
  }
  editor.focus();
}

//插入文件
function insertFile(val,inId){
  editor.focus();
  var newVal,theObj,reg;

  if(val=='')
    return;

  var now=new Date(); 
  var regId=now.getYear()+''+now.getMonth()+''+now.getDate()+''+now.getHours()+''+now.getMinutes()+''+now.getSeconds()+''+now.getMilliseconds();

  switch(inId){
    case 15:
      //editor.document.execCommand('InsertImage',false,encodeURI(val));
      //return;
      theObj='<a href="'+encodeURI(val)+'" target="_blank"><img src="'+encodeURI(val)+'" border="0" /></a>';
      newVal='insertImg:'+regId;
      break;
    case 16:
    case 17:
	  theObj='<div style="width:400px;height:auto !important;height:64px;font-size:12px;background-color:black;color:white;">浏览器不兼容会导致播放器不显示？可<a href="'+encodeURI(val)+'" target="_blank">用客户端播放</a><br>'+getVideo(val)+' &nbsp;</div><br>\n\n';
      newVal='insertFilm:'+regId;
      break;
    case 18:
      theObj='<img src="'+path+'images/picenc.gif" title="附件"><a href="'+encodeURI(val)+'" target="_blank">'+val+'</a><br />';
      newVal='insertOtherFile:'+regId;
      break;
    case 20:
      theObj=val;
      newVal='insertMark:'+regId;
      break;
    case 21:
      theObj=val;
      newVal='insertOther:'+regId;
      break;
    default:
      return;
  }
  editor.document.execCommand('InsertImage',false,newVal);
  reg=new RegExp('<img src="'+newVal+'">','ig');
  editor.document.body.innerHTML=editor.document.body.innerHTML.replace(reg,theObj);

/*
  if(document.all){
    var r=editor.document.body.document.selection.createRange();
    r.moveStart('character',editor.document.body.innerHTML.length);
    r.moveEnd('character',0);
    r.select();
  }
*/
}

//插入表格值
function insertTableVal(){
  var tableVal;
  var tableWidth=document.getElementById('thetable_width').value;
  var tableAlign=document.getElementById('thetable_align').value;
  var tableRows=document.getElementById('thetable_rows').value;
  var tableCols=document.getElementById('thetable_cols').value;
  var tableCellspacing=document.getElementById('thetable_spacing').value;
  var tableCellpadding=document.getElementById('thetable_padding').value;
  var tableBgcolor=document.getElementById('thetable_bgcolor').value;
  var tableBordercolor=document.getElementById('thetable_bordercolor').value;

  if(isNaN(tableRows || tableCols) || tableRows<1 || tableCols<1){
    alert("无效的行数或列数！至少填数字1");
    return false;
  }

  tableVal='<table'+tableWidth+tableAlign+' border="1" cellspacing="'+tableCellspacing+'" cellpadding="'+tableCellpadding+'" bgcolor="'+tableBgcolor+'" bordercolor="'+tableBordercolor+'">';
  for(var i=1;i<=tableRows;i++){
    tableVal+='<tr>';
    for(j=1;j<=tableCols;j++)
      tableVal+='<td>&nbsp;</td>';
    tableVal+='</tr>';
  }
  tableVal+='</table>';
  return tableVal;
}

// 只允许输入数字
function isDigit(obj){
  if(!/^([1-9]|[0-9]{2,})$/.test(obj.value)){
    alert("你输入的值不对，应填写数字！");
    obj.value="";
  }
}

/* 表单检查 */
function writeChk(){
  //主题检查
  var s=document.mainform.subject;
  if(s!=null){
    if(s.value==''){
      document.getElementById('a_subject').innerHTML='<span style="color:#FF6600">请填写标题！！！</span>';
      s.focus();
      return false;
    }
  }
  //内容检查
  var c=document.mainform.content;
  if(c!=null){
    if(c.value==''){
      document.getElementById('a_content').innerHTML='<span style="color:#FF6600">请填写内容！！！</span>';
      try{
        c.focus();
      }catch(err){
      }
      try{
        editor.focus();//编辑器定位
      }catch(err){
      }
      return false;
    }
    //最多允许字符数检查
    if(c.value.length>maxWordCount){
      alert('所写内容已超出允许字符数'+maxWordCount+'！请删减');
      return false;
    }
  }
  return true;
}

//取视频播放器代码
function getVideo(val){
  var sufFix=val.substring(val.lastIndexOf(".")+1,val.length).toLowerCase();
  if(sufFix.indexOf("#")) sufFix=sufFix.split("#")[0];
  if(sufFix.indexOf("?")) sufFix=sufFix.split("?")[0];
  switch(sufFix){
    case 'wma': case 'wmv': case 'wav': case 'mid': case 'midi': case 'mp3': case 'asf': case 'asx': case 'mov': case 'mpg': case 'mpeg': case 'avi':
    return '<object id="mediaplayer" width="480" height="'+((sufFix=='mp3'||sufFix=='wma'||sufFix=='mid'||sufFix=='midi')?64:424)+'" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6" align="center" border="0" type="application/x-oleobject" standby="Loading Windows Media Player components..." style="background-color:black">\
<param name="url" value="'+encodeURI(val)+'">\
<param name="AutoStart" value="0">\
<param name="Balance" value="0">\
<param name="enabled" value="-1">\
<param name="EnableContextMenu" value="0">\
<param name="PlayCount" value="1">\
<param name="rate" value="1">\
<param name="currentPosition" value="0">\
<param name="currentMarker" value="0">\
<param name="defaultFrame" value="">\
<param name="invokeURLs" value="-1">\
<param name="baseURL" value="">\
<param name="stretchToFit" value="0">\
<param name="volume" value="100">\
<param name="mute" value="0">\
<param name="uiMode" value="full">\
<param name="windowlessVideo" value="0">\
<param name="fullScreen" value="0">\
<param name="enableErrorDialogs" value="0">\
<param name="SAMIStyle" value="">\
<param name="SAMILang" value="">\
<param name="SAMIFilename" value="">\
<param name="captioningID" value="">\
</object>';
    break;
    case 'rm': case 'rmvb': case 'ram': case 'ra':
      return '<object classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" width="400" height="300"  id="amourReal" style="background-color:black">\
<param name="src" value="'+encodeURI(val)+'">\
<param name="autostart" value="false">\
<param name="controls" value="imagewindow">\
<param name="console" value="clip1">\
<embed src="'+encodeURI(val)+'" width="400" height="300" type="audio/x-pn-realaudio-plugin" autostart="false" controls="imagewindow" console="video">\
</embed>\
</object>\
<br>\
<object classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" width="400" height="50" id="amourReal">\
<param name="src" value="'+encodeURI(val)+'">\
<param name="autostart" value="false">\
<param name="controls" value="all">\
<param name="console" value="clip1">\
<embed type="audio/x-pn-realaudio-plugin" src="'+encodeURI(val)+'" width="400" height="50" autostart="true" controls="all" console="video">\
</embed>\
</object>';
    break;
    default :
      return '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="600" height="480" style="background-color:black">\
<param name="movie" value="'+encodeURI(val)+'">\
<param name="quality" value="high">\
<embed src="'+encodeURI(val)+'" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="480" height="330"></embed>\
</object>';

  }
}


























