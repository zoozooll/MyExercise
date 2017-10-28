function aliclick(u, param) {
    d = new Date();
    if(document.images) {
        (new Image()).src="http://stat.china.alibaba.com/tracelog/click.html" + param + "&time=" + d.getTime();
    }
    return true;
}
function etcclick(u, param) {
    d = new Date();
    if(document.images) {
        (new Image()).src="http://stat.china.alibaba.com/etclistquery.html" + param + "&time=" + d.getTime();
    }
    return true;
}


