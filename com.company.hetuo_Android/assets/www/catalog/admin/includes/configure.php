<?php
  define('HTTP_SERVER', 'http://localhost:8081');
  define('HTTP_CATALOG_SERVER', 'http://localhost:8081');
  define('HTTPS_CATALOG_SERVER', 'http://localhost:8081');
  define('ENABLE_SSL_CATALOG', 'false');
  define('DIR_FS_DOCUMENT_ROOT', 'D:/Apache Group/Apache2/htdocs/catalog/');
  define('DIR_WS_ADMIN', '/catalog/admin/');
  define('DIR_FS_ADMIN', 'D:/Apache Group/Apache2/htdocs/catalog/admin/');
  define('DIR_WS_CATALOG', '/catalog/');
  define('DIR_FS_CATALOG', 'D:/Apache Group/Apache2/htdocs/catalog/');
  define('DIR_WS_IMAGES', 'images/');
  define('DIR_WS_ICONS', DIR_WS_IMAGES . 'icons/');
  define('DIR_WS_CATALOG_IMAGES', DIR_WS_CATALOG . 'images/');
  define('DIR_WS_INCLUDES', 'includes/');
  define('DIR_WS_BOXES', DIR_WS_INCLUDES . 'boxes/');
  define('DIR_WS_FUNCTIONS', DIR_WS_INCLUDES . 'functions/');
  define('DIR_WS_CLASSES', DIR_WS_INCLUDES . 'classes/');
  define('DIR_WS_MODULES', DIR_WS_INCLUDES . 'modules/');
  define('DIR_WS_LANGUAGES', DIR_WS_INCLUDES . 'languages/');
  define('DIR_WS_CATALOG_LANGUAGES', DIR_WS_CATALOG . 'includes/languages/');
  define('DIR_FS_CATALOG_LANGUAGES', DIR_FS_CATALOG . 'includes/languages/');
  define('DIR_FS_CATALOG_IMAGES', DIR_FS_CATALOG . 'images/');
  define('DIR_FS_CATALOG_MODULES', DIR_FS_CATALOG . 'includes/modules/');
  define('DIR_FS_BACKUP', DIR_FS_ADMIN . 'backups/');

  define('DB_SERVER', 'localhost');
  define('DB_SERVER_USERNAME', 'root');
  define('DB_SERVER_PASSWORD', '123456');
  define('DB_DATABASE', 'osc');
  define('USE_PCONNECT', 'false');
  define('STORE_SESSIONS', 'mysql');
?>