<body class="frame">
<div>
<?=form_open_multipart('backend/head/add')?>
        <fieldset class="w450">
            <legend>Add Img</legend>
            <div class="lable_input_set">
                <label class="input_label w10">Img name&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="picname"/>
            </div>
             <div class="lable_input_set">
                <label class="input_label w10">Link&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="link"/>
            </div>
             <div class="lable_input_set">
                <label class="input_label w10">Sort&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</label>
                <input class="text_input w15" type="text" name="sort" value="0"/>
            </div>

       		<div class="lable_input_set">
                <label class="input_label w10">Upload Img&nbsp;:</label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>

            </div>
		</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>

