<body class="frame">
<div class="margin-left"">
    <h3 class="frame_title">明细</h3>
    <table class="pretty_table">
        <thead>
        <tr>
            <th>产品名称(中)</th>
           
            <th>产品编号</th>
            <th>添加时间</th>
            <th>激光/光学测量仪</th>
            <th>固件/半导体激光器</th>
            <th>光纤激光器</th>
            <th>电光器件</th>
            <th>法拉第隔离器</th>
            <th>光子晶体光纤/激光玻璃</th>
            <th>NESLAB ThermoFLEX	冷却水循环器</th>
            <th>光机械及光学元器件</th>
						<th>修改</th>
						<th>删除</th>
        </tr>
        </thead>
		 <tbody>
	<?php foreach($datas as $data) { ?>
        <tr>
						<td><?=$data->subject_chi?></td>
						
						<td><?=$data->number?></td>
						<td><?=date('Y-m-d H:i', $data->time)?></td>
						<td><?=$type['client'][$data->corporate]?></td>
						<td><?=$type['client'][$data->tv]?></td>
						<td><?=$type['client'][$data->print]?></td>
						<td><?=$type['client'][$data->advertising]?></td>
						<td><?=$type['client'][$data->promotion]?></td>
						<td><?=$type['client'][$data->event]?></td>
						<td><?=$type['client'][$data->premeium]?></td>
						<td><?=$type['client'][$data->video]?></td>
						<td><?=anchor('backend/product/edit_view/'.$data->id, '修改')?></td>
						<td><?=anchor('backend/product/del/'.$data->id, '删除')?></td>
		</tr>
	<?php } ?>
		</tbody>
    </table>
    <div class="pagesNavi">
	<?=$page?>
    </div>
</div>
</body>



