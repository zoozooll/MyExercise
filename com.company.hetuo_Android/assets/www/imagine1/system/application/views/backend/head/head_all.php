<body class="frame">
<div class="margin-left"">
    <h3 class="frame_title">List</h3>
    <table class="pretty_table">
        <thead>
        <tr>

            <th>Img name</th>
            <th>Img</th>
            <th>Sort</th>

            <th>Upload Time</th>
            <th>Modify</th>
			<th>Del</th>
        </tr>
        
        </thead>
		 <tbody>
	<?php foreach($datas as $data) { ?>
        <tr>
						
						<td><?=$data->picname?></td>
                        <td><a href="system/images/backend/uploads/<?=$data->picpath?>" class="test"><img src="system/images/backend/uploads/<?=$data->picpath?>" height="50" wight="5 0"></img></a></td>
						<td><?=$data->sort?></td>
                       
                        <td><?=date('Y-m-d H:i:s', $data->time)?></td>
						<td><?=anchor('backend/head/edit_view/'.$data->Id, 'Update')?></td>
						<td><?=anchor('backend/head/del/'.$data->Id, 'Del')?></td>
		</tr>
	<?php } ?>
		</tbody>
    </table>
    <div class="pagesNavi">
	<?=$page?>
    </div>
</div>
</body>



