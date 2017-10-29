<body class="frame">
<div>
<?=form_open_multipart('backend/head/edit', '', array('id' => $data[0]->Id))?>
        <fieldset class="w450">
            <legend>Update</legend>
            <div class="lable_input_set">
                <label class="input_label w10">Img name&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="picname" value="<?=$data[0]->picname?>"/>
            </div>
             <div class="lable_input_set">
                <label class="input_label w10">Link&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="link" value="<?=$data[0]->link?>"/>
            </div>
             <div class="lable_input_set">
                <label class="input_label w10">Sort&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="sort" value="<?=$data[0]->sort?>"/>
            </div>


       		<div class="lable_input_set">
                <label class="input_label w10">Upload Img&nbsp;&nbsp;:</label>
                <?php if ($data[0]->picpath != NULL)?>
                 <a href="system/images/backend/uploads/<?=$data[0]->picpath?>" class="test"><img src="system/images/backend/uploads/<?=$data[0]->picpath?>" height="50" wight="5 0"></img></a>
      			<input class="text_input w15" type="file" name="img[]"/>
            </div>

            </div>
		</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>

