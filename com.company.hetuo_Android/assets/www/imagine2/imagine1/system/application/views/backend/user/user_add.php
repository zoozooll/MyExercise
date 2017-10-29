<body class="frame">
<div>
<?=form_open_multipart('backend/user/add')?>
        <fieldset class="w450">
            <legend>New User</legend>
            <div class="lable_input_set">
                <label class="input_label w10">Username:&nbsp;&nbsp;</label>
                <input class="text_input w15" type="text" name="user"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">Password:&nbsp;&nbsp;</label>
                <input class="text_input w15" type="text" name="pw"/>
            </div>
		</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>

