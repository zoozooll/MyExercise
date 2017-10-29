<?php if (!defined('IN_PHPBB')) exit; ?>Subject: phpBB 安装成功

恭喜！

您已经成功地安装了phpBB。

这封信包含了重要的安装信息，请妥善保管。注意数据库中的密码是被加密过的，无法直接读取，若您不幸遗失此密码，就只能再申请一个新的密码了。

----------------------------
用户名：<?php echo (isset($this->_rootref['USERNAME'])) ? $this->_rootref['USERNAME'] : ''; ?>


论坛地址：<?php echo (isset($this->_rootref['U_BOARD'])) ? $this->_rootref['U_BOARD'] : ''; ?>

----------------------------

您可以在安装目录的“docs”文件夹，以及phpBB.com的支持页面“http://www.phpbb.com/support/”找到一些有用的信息。

为了保障您的论坛安全稳定，我们强烈建议您订阅邮件列表以便随时获得最新的发布消息，地址如下：

<?php echo (isset($this->_rootref['EMAIL_SIG'])) ? $this->_rootref['EMAIL_SIG'] : ''; ?>