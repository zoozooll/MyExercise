<body class="frame">
<div>
<?=form_open_multipart('backend/user/edit', '', array('id' => $data[0]->id));?>
        <fieldset class="w450">
            <legend>Edit User</legend>
                 <div class="lable_input_set">
                <label class="input_label w10">Username:</label>
                <input class="text_input w15" type="text" name="user" value="<?=$data[0]->user?>"/>
            </div>
            <div class="lable_input_set">
                <label class="input_label w10">Password:</label>
                <input class="text_input w15" type="text" name="pw" value="<?=$data[0]->pw?>"/>
            </div>
				</fieldset>
        <input type="submit" value="Submit"  class="green button b90"/>
    </form>
</div>
</body>
</html>