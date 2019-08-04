package com.tcl.manager.application;

public enum AMDirType
{
	root, log, image, apps, cache, crash;

	public int value()
	{
		return ordinal() + 1;
	}
}
