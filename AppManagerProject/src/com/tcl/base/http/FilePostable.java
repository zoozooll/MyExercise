package com.tcl.base.http;

import java.io.File;
import java.util.Map;

/**
 * @Description: 可上传文件接口
 * @author rexzou
 * @date 2014年10月8日 下午5:48:48
 * @copyright TCL-MIE
 */
public interface FilePostable {

    /**
     * 获取上传文件的接口
     * @return
     */
    Map<String,File> getPostFiles();

}
