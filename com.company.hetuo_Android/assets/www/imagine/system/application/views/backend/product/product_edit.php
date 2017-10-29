<body class="frame">
<div>
<?=form_open_multipart('backend/product/edit', '', array('id' => $data[0]->id));?>
        <fieldset class="w450">
            <legend>修改产品</legend>
             <div class="lable_input_set">
                <label class="input_label w10">产品名称(英):</label>
                <input class="text_input w15" type="text" name="subject_eng" value="<?=$data[0]->subject_eng?>"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">产品名称(中):</label>
                <input class="text_input w15" type="text" name="subject_chi" value="<?=$data[0]->subject_chi?>"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">产品编号:</label>
                <input class="text_input w15" type="text" name="number" value="<?=$data[0]->number?>"/>
            </div>

 			<div class="lable_input_set">
                <label class="input_label w10">产品图片:
                <img src="<?=base_url();?>system/images/backend/uploads/<?=$data[0]->img1?>" width="80" heigth = "80"/>
                </label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>

             <div class="lable_input_set">
							激光/光学测量仪 : <select name="corporate">
									<option value="0" <?php echo $data[0]->corporate == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->corporate == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							固件/半导体激光器 :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select name="tv">
									<option value="0" <?php echo $data[0]->tv == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->tv == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光纤激光器 :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select name="print">
									<option value="0" <?php echo $data[0]->print == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->print == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							电光器件 : <select name="advertising">
									<option value="0" <?php echo $data[0]->advertising == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->advertising == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							法拉第隔离器 :<select name="promotion">
									<option value="0" <?php echo $data[0]->promotion == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->promotion == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光子晶体光纤/激光玻璃 :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select name="event">
									<option value="0" <?php echo $data[0]->event == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->event == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							NESLAB ThermoFLEX	冷却水循环器 : <select name="premeium">
									<option value="0" <?php echo $data[0]->premeium == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->premeium == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光机械及光学元器件 :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select name="video">
									<option value="0" <?php echo $data[0]->video == '0' ? "selected = selected" : "";?>>No</option>
									<option value="1" <?php echo $data[0]->video == '1' ? "selected = selected" : "";?>>Yes</option>
						  </select>
            </div>
                  <div class="lable_input_set">
            <label class="input_label w10">详细介绍(中):&nbsp;&nbsp;</label>
			<textarea id="content_chi" name="content_chi" style="width:700px;height:300px;visibility:hidden;">

</p>
<?=$data[0]->content_chi?>  
</p>

			</textarea>
			<br />
            </div>
            <div class="lable_input_set">
            <label class="input_label w10">详细介绍(英):&nbsp;&nbsp;</label>
			<textarea id="content_eng" name="content_eng" style="width:700px;height:300px;visibility:hidden;">

</p>
<?=$data[0]->content_eng?> 
</p>

			</textarea>
			<br />
				</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>