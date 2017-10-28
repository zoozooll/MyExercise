/**
* 异步发出统计参数统计数据。
* 需要包含异步跨域请求类的封装实现的JS
* 如http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* 或http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regstepclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_step.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
/**
* 异步发出统计参数统计数据。
* 需要包含异步跨域请求类的封装实现的JS
* 如http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* 或http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regfieldclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_field.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
/**
* 异步发出统计参数统计数据。
* 需要包含异步跨域请求类的封装实现的JS
* 如http://style.china.alibaba.com/js/chinaindex/chinajs_v2.js
* 或http://style.china.alibaba.com/js/p4p/getAdvance.js
*/
function regsubmitclick(param) {
d = new Date();
var clickUrl = "http://stat.china.alibaba.com/reg_submit.htm" + param + "&time=" + d.getTime();
AsyncScript.script(clickUrl,function(){},window);
return true;
}
