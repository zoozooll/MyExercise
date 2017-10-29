<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Panel</title>
    <link rel="stylesheet" type="text/css" href="<?=base_url();?>system/css/backend/login.css"/>
<body>
<div id="container">
    <div class="content">
        <?=form_open('backend/user/check_login')?>
            <fieldset>
                <legend>Login</legend>
                <div class="lable_input_set">
                    <label for="username" class="input_lable">Username:</label>
                    <input type="text" class="text_input" name="user" id="user"/>
                </div>
                <div class="lable_input_set">
                    <label for="password" class="input_lable">Password:</label>
                    <input type="password" class="text_input" name="pw" id="pw"/>
                </div>
            </fieldset>
            <input class="submit_btn" type="submit" id="Login" value="Login"/>
        </form>
        <div id="state_info"></div>
    </div>
    <div id="footer">Powered by XXX</div>
</div>
</body>
</html>