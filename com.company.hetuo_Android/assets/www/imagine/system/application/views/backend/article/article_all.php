<body class="frame">
<div class="margin-left"">
    <h3 class="frame_title">明细</h3>
    <table class="pretty_table">
        <thead>
        <tr>
            <th>标题(中)</th>
            <th>内容(中)</th>
            <th>图片</th>
            <th>时间</th>
						<th>修改</th>
						<th>删除</th>
        </tr>
        </thead>
		 <tbody>
	<?php foreach($datas as $data) { ?>
        <tr>
						<td><?=$data->subject_chi?></td>
						<td><?=$data->content_chi?></td>
						<td><img src="<?=base_url()?>system/images/backend/uploads/<?=$data->img?>" width="40" heigth = "40"/></td>
						<td><?=date('Y-m-d H:i', $data->time)?></td>
						<td><?=anchor('backend/article/edit_view/'.$data->id, '修改')?></td>
						<td><?=anchor('backend/article/del/'.$data->id, '删除')?></td>
		</tr>
	<?php } ?>
</form>
		</tbody>
    </table>
    <div class="pagesNavi">
	<?=$page?>
    </div>
</div>
</body>