<body class="frame">
<div class="margin-left"">
    <h3 class="frame_title">User List</h3>
    <table class="pretty_table">
        <thead>
        <tr>
            <th>Id</th>
            <th>User</th>
            <th>Password</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        </thead>
		 <tbody>
	<?php foreach($datas as $data) { ?>
        <tr>
            <td><?=$data->id?></td>
						<td><?=$data->user?></td>
						<td><?=$data->pw?></td>
						<td><?=anchor('backend/user/edit_view/'.$data->id, 'Edit')?></td>
						<td><?=anchor('backend/user/del/'.$data->id, 'Delete')?></td>
		</tr>
	<?php } ?>
		</tbody>
    </table>
    <div class="pagesNavi">
	<?=$page?>
    </div>
</div>
</body>



