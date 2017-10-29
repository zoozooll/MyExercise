	{$loop value}
		<div>
			<div class="fLeft{if value.loop.cpid==0} fb{/if}" id="classes">
			<a>{$value.loop._name}</a>
			</div>
			<div id="sortid" class="fLeft">
			<a title="修改此类别" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}" target="adminMain">修改</a>
            <a title="移动此类别" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}&mv=1" target="adminMain">移动</a> 
			{if value.loop.ccidnum==0}
			<a onClick="return delYesOrNo();" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}&act=del" target="adminMain">删除</a>{else}<font color="#CCCCCC">删除</font>
			{/if}
			</div>
			<div>
				{if value.loop.cpid>'0'}
				{if value.loop.up=='1'}<a href="#" onClick="sort_sub_class('{$value.loop.id}','up')" title="同级上移">↑</a>{/if}
				{if value.loop.down=='1'}<a href="#" onClick="sort_sub_class('{$value.loop.id}','down')" title="同级下移">↓</a>{/if}
				{else}
				<input name="sort_id[]" type="hidden" value="{$value.loop.id}">
				<input name="sort_class[]" type="text" size="1" maxlength="3" class="txInput" value="{$value.loop.sort}">
				{/if}
			</div>
		{if value.next.cpid==0}
		{/if}
		</div>
		<div class="cls"></div>
	{/loop}
<div id="classes" class="fLeft">&nbsp;</div><div id="sortid" class="fLeft">&nbsp;</div>
<div class="fLeft"><input onClick="sort_class()" name="sortclass" type="button" class="txInput" value="排序"></div>
<div class="cls"></div>