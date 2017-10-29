function showTabs(id,select,element,css){
	var o = getObj(id);
	var t = o.getElementsByTagName(element);
	for (var i=0;i<t.length;i++) {
		if (t[i].id) {
			var oo = getObj(t[i].id);
			if (t[i].id == select) {
				getObj(t[i].id).className = css;
				getObj('info_'+t[i].id).style.display = '';
			} else {
				getObj(t[i].id).className = '';
				getObj('info_'+t[i].id).style.display = 'none';
			}
		}
	}
	if (getObj('info_type')) {
		getObj('info_type').value=select;
	}
	return false;
}