package com.oregonscientific.meep.message.common;


public class ApplicationAccess {

	public static enum Access {

		ALLOW("allow"), 
		MEDIUM("medium"), 
		DENY("deny"),
		HIGH("high");

		private String accessString = null;

		Access(String accessString) {
			this.accessString = accessString;
		}

		public String toString() {
			return accessString;
		}
		
		public static Access fromString(String text) {
			if (text != null) {
				for (Access b : Access.values()) {
					if (text.equalsIgnoreCase(b.accessString)) {
						return b;
					}
				}
			}
			return null;
		}
	}

	private String access = null;
	private int timelimit = 0;

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public int getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = timelimit;
	}

}
