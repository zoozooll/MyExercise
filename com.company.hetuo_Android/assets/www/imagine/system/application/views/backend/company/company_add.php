<body class="frame">
<div>
<?=form_open_multipart('backend/company/add')?>
        <fieldset class="w450">
            <legend>增加</legend>
            <div class="lable_input_set">
                <label class="input_label w10">标题(英):</label>
                <input class="text_input w15" type="text" name="subject_eng"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">标题(中):</label>
                <input class="text_input w15" type="text" name="subject_chi"/>
            </div>
			 			<div class="lable_input_set">
                <label class="input_label w10">内容(英):</label>
                <textarea rows="5	" cols="20" class="text_input w15" name="content_eng"></textarea>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">内容(中):</label>
                <textarea rows="5	" cols="20" class="text_input w15" name="content_chi"></textarea>
            </div>
       			<div class="lable_input_set">
                <label class="input_label w10">图片:</label>
                <input class="text_input w15" type="file" name="img[]"/>
            </div>
		</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>

