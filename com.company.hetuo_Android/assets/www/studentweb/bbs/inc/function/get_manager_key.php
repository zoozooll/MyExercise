
  <script language="JavaScript" type="text/javascript">
  <!--
  function get_checkbox(){var allCheckBox=document.getElementsByName("id[]");var article='';if(allCheckBox!=null && allCheckBox.length>0){for(var i=0;i<allCheckBox.length;i++){if(allCheckBox[i].checked==true && allCheckBox[i].disabled==false){article=allCheckBox[i].value;break;}}}return article;}
  function chk(obj,manageType){
  if(get_checkbox()==''){
  alert('数据不能空！请点选');return false;}
  if(confirm('确定'+(manageType=='chanto'?'转移':obj.value)+'吗？')){
  if(manageType=='edit'){
    document.manageform.action='user_list_edit.php';
	document.manageform.submit();
	return;
  }
  document.manageform.action='run.php?run=admin_del&dataname=yzsoulistdata&limit='+manageType+'';document.manageform.submit();}return false;}
  -->
  </script>
  <input name="act" type="button" class="mm" style="float:right" value="退出" onclick="location.href='?area_id=<?php echo $_REQUEST['area_id']; ?>&list_type=<?php echo $_REQUEST['list_type']; ?>'" />
  <select name="to_id" class="mm" style="float:right;width:180px;" onchange="if(this.value!='')chk(this,'chanto')">
<?php
  echo '
    <option value="">转移</option>';
  foreach((array)$web['area'] as $i=>$area){
    echo '
      <optgroup label="'.$area[0].'">';
    foreach((array)$area as $j=>$class){
      if($j!=0){
        echo '
	    <option value="'.$i.'_'.$j.'">'.$class.'</option>';
      }
    }
    echo '
      </optgroup>';
  }
?>
  </select>
  <input name="act" type="button" class="mm" style="float:right" value="删除" onclick="chk(this,'del')" />
  <input name="act2" type="button" class="mm" style="float:right" value="撤精" onclick="chk(this,'cess')" />
  <input name="act2" type="button" class="mm" style="float:right" value="加精" onclick="chk(this,'ess')" />
  <input name="act2" type="button" class="mm" style="float:right" value="撤顶" onclick="chk(this,'ctop')" />
  <input name="act2" type="button" class="mm" style="float:right" value="置顶" onclick="chk(this,'top')" />
  <input name="act2" type="button" class="mm" style="float:right" value="编辑" onclick="chk(this,'edit')" />