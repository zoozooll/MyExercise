<body class="frame">
<div>
<?=form_open_multipart('backend/product/add', array ("name" => "example"))?>
        <fieldset class="w450">
            <legend>增加</legend>
            <div class="lable_input_set">
                <label class="input_label w10">产品名称(英):&nbsp;&nbsp;</label>
                <input class="text_input w15" type="text" name="subject_eng"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">产品名称(中):&nbsp;&nbsp;</label>
                <input class="text_input w15" type="text" name="subject_chi"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">产品编号:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                <input class="text_input w15" type="text" name="number"/>
            </div>
          
       			<div class="lable_input_set">
                <label class="input_label w10">产品图片:</label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>
            <!--<div class="lable_input_set">
                <label class="input_label w10">Picture 2:</label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">Picture 3:</label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>-->
             <div class="lable_input_set">
							激光 / 光学测量仪 : <select name="corporate">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							固件 / 半导体激光器 :<select name="tv">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光纤激光器 :<select name="print">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							电光器件 : <select name="advertising">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							法拉第隔离器 :<select name="promotion">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光子晶体光纤/激光玻璃 : <select name="event">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							NESLAB ThermoFLEX	冷却水循环器 : <select name="premeium">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
							光机械及光学元器件 :<select name="video">
									<option value="0">No</option>
									<option value="1">Yes</option>
						  </select>
            </div>
             <div class="lable_input_set">
            <label class="input_label w10">详细介绍(中):&nbsp;&nbsp;</label>
			<textarea id="content_chi" name="content_chi" style="width:700px;height:300px;visibility:hidden;">

</p>

</p>

			</textarea>
			<br />
            </div>
            <div class="lable_input_set">
            <label class="input_label w10">详细介绍(英):&nbsp;&nbsp;</label>
			<textarea id="content_eng" name="content_eng" style="width:700px;height:300px;visibility:hidden;">

</p>

</p>

			</textarea>
			<br />
            </div>
            
		</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>

