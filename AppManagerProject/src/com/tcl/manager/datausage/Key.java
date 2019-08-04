package com.tcl.manager.datausage;

/** 
 * @Description: 
 * @author zuokang.li 
 * @date 2014年12月1日 下午7:23:57 
 * @copyright TCL-MIE
 */
public class Key implements Comparable<Key> {
    public final NetworkIdentitySet ident;
    public final int uid;
    public final int set;
    public final int tag;

    private final int hashCode;

    public Key(NetworkIdentitySet ident, int uid, int set, int tag) {
        this.ident = ident;
        this.uid = uid;
        this.set = set;
        this.tag = tag;
        hashCode = hashCode();
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (uid != other.uid)
			return false;
		return true;
	}

	@Override
    public int compareTo(Key another) {
        return another.uid - uid;
    }
}