/**
* �첽����ͳ�Ʋ���ͳ�����ݡ�
* ��Ҫ�����첽����������ķ�װʵ�ֵ�JS
* ��http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* ��http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regstepclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_step.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
/**
* �첽����ͳ�Ʋ���ͳ�����ݡ�
* ��Ҫ�����첽����������ķ�װʵ�ֵ�JS
* ��http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* ��http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regfieldclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_field.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
/**
* �첽����ͳ�Ʋ���ͳ�����ݡ�
* ��Ҫ�����첽����������ķ�װʵ�ֵ�JS
* ��http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* ��http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regsubmitclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_submit.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
