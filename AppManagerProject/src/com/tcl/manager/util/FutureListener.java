
package com.tcl.manager.util;



public interface FutureListener<T> {
    
    public void onFutureBegin(TCLFuture<T> future);

    
    public void onFutureDone(TCLFuture<T> future);
}
