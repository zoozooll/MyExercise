package com.dvr.android.dvr.bean;

//import com.lidroid.xutils.db.annotation.Column;
//import com.lidroid.xutils.db.annotation.Id;
//import com.lidroid.xutils.db.annotation.Table;

/**
 * 文件 [文件的bean]
 * 
 * @author tony
 */
//@Table(name = "VideoFile")
public class VideoFile {

    //@Id
    private long id;

    //@Column(column = "name")
    // 建议加上注解， 混淆后列名不受影响
    private String name;

    //@Column(column = "path")
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "VideoFile [id=" + id + ", name=" + name + ", path=" + path + "]";
    }

}
