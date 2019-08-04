package com.tcl.manager.score;

public class PackageChangeEvent {

    public static final int ADD =0;
    public static final int REMOVE =1;
    
    public String packageName;

    public int stats;
    
    public PackageChangeEvent(String packageName, int status) {
        super();
        this.packageName = packageName;
        stats = status;
    }
   
}
