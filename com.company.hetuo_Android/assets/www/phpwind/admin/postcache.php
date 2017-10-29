<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=postcache";

if (empty($action)) {

	$facedb = array();
	$query  = $db->query("SELECT * FROM pw_smiles WHERE type=0 ORDER BY vieworder");
	while($postcache=$db->fetch_array($query)){
		$facedb[]=$postcache;
	}
	$shownum = count($facedb);
	@extract($db->get_one("SELECT db_value AS fc_shownum FROM  pw_config WHERE db_name='fc_shownum'"));

	include PrintEot('postcache');exit;

} elseif ($_POST['action'] == 'addface') {

	InitGP(array('path','name'),'P',1);
	if (empty($path) || !is_dir("$imgdir/post/smile/$path")) {
		adminmsg('smile_path_error');
	}
	empty($name) && adminmsg('smile_name_error');
	$rs = $db->get_one("SELECT COUNT(*) AS sum FROM pw_smiles WHERE path=".pwEscape($path));
	$rs['sum']>=1 && adminmsg('smile_rename');

	InitGP(array('vieworder'),'P',2);
	$db->update("INSERT INTO pw_smiles"
		. " SET " . pwSqlSingle(array(
			'path'		=> $path,
			'name'		=> $name,
			'vieworder'	=> $vieworder
	)));
	updatecache_p();
	adminmsg('operate_success');

} elseif ($_POST['action'] == 'editsmiles') {

	InitGP(array('shownum','name'),'P');
	InitGP(array('vieworder'),'P',2);

	foreach ($vieworder as $key => $value) {
		$smilesname = $name[$key];
		$db->update("UPDATE pw_smiles"
			. " SET " . pwSqlSingle(array(
					'name'		=> $smilesname,
					'vieworder'	=> $value
				))
			. " WHERE id=".pwEscape($key)
		);
	}
	setConfig('fc_shownum', $shownum);

	updatecache_p();
	adminmsg('operate_success');

} elseif ($action == 'delete') {

	InitGP(array('id'));
	$db->update("DELETE FROM pw_smiles WHERE id=".pwEscape($id));
	$db->update("DELETE FROM pw_smiles WHERE type=".pwEscape($id));
	updatecache_p();
	adminmsg('operate_success');

} elseif ($action == 'smilemanage') {

	if (!$_POST['step']) {

		InitGP(array('id'));
		@extract($db->get_one("SELECT * FROM pw_smiles WHERE id=".pwEscape($id)));
		$rs = $db->query("SELECT * FROM pw_smiles WHERE type=".pwEscape($id)."ORDER BY vieworder");
		$smiles_new = $smiles_old = $smiles = array();
		$picext = array("gif","bmp","jpeg","jpg","png");
		while ($smiledb = $db->fetch_array($rs)) {
			$smiledb['src'] = "$imgpath/post/smile/$path/{$smiledb[path]}";
			$smiles_old[] = $smiledb['path'];
			$smiles[] = $smiledb;
		}
		$smilepath = "$imgdir/post/smile/$path";

		$fp = opendir($smilepath);
		$i = 0;
		while ($smilefile = readdir($fp)) {
			if (in_array(strtolower(end(explode(".",$smilefile))),$picext)) {
				if (!in_array($smilefile,$smiles_old)) {
					$i++;
					$smiles_new[$i]['path']=$smilefile;
					$smiles_new[$i]['src']="$imgpath/post/smile/$path/$smilefile";
				}
			}
		}
		closedir($fp);
		include PrintEot('postcache');exit;

	} else {

		InitGP(array('name','descipt','id'),'P');
		InitGP(array('vieworder'),'P',2);
		foreach ($vieworder as $key => $value) {
			$smilesname = $name[$key];
			$descipts	= $descipt[$key];
			$db->update("UPDATE pw_smiles"
				. " SET " . pwSqlSingle(array(
						'name'		=> $smilesname,
						'descipt'	=> $descipts,
						'vieworder'	=> $value
					))
				. " WHERE id=".pwEscape($key)
			);
		}
		updatecache_p();
		adminmsg('operate_success',"$basename&action=smilemanage&id=$id");
	}
} elseif ($_POST['action'] == 'addsmile') {

	InitGP(array('add','id'),'P');
	foreach ($add as $value) {
		$db->update("INSERT INTO pw_smiles SET ".pwSqlSingle(array('path'=>$value,'type'=>$id)));
	}
	updatecache_p();
	adminmsg('operate_success',"$basename&action=smilemanage&id=$id");

} elseif ($action == 'delsmile') {

	InitGP(array('smileid','typeid'));
	$db->update("DELETE FROM pw_smiles WHERE id=".pwEscape($smileid));
	updatecache_p();
	adminmsg('operate_success',"$basename&action=smilemanage&id=$typeid");
}
?>