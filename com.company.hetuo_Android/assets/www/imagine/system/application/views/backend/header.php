<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<?=base_url()?>" />

<link type="text/css" rel="stylesheet" href="system/css/backend/general.css"/>
<link type="text/css" rel="stylesheet" href="system/css/backend/table.css"/>
<script src="system/js/mts.js"></script>
<script src="system/js/kindeditor.js" /></script>
		<script>
			KE.show({
				id : 'content_chi',
				width : '95%',
				cssPath : 'system/css/index.css',
				afterCreate : function(id) {
					
					KE.event.ctrl(document, 13, function() {
						KE.sync(id);
						document.forms['example'].submit();
					});
					KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
						KE.sync(id);
						document.forms['example'].submit();
					});
				}
			});
			KE.show({
				id : 'content_eng',
				width : '95%',
				cssPath : 'system/css/index.css',
				afterCreate : function(id) {
					
					KE.event.ctrl(document, 13, function() {
						KE.sync(id);
						document.forms['example'].submit();
					});
					KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
						KE.sync(id);
						document.forms['example'].submit();
					});
				}
			});
		</script>
