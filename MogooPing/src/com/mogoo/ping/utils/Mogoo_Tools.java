package com.mogoo.ping.utils;

import java.util.HashMap;
import java.util.Map;

public class Mogoo_Tools {

	
	/**
	 * 	分割intent
	 * @author 梁师松
	 * @param str
	 * @return
	 */
		
	public static String castrateIntent(String str){

				Map<String, String> values = new HashMap<String, String> ();
				StringBuffer sb = new StringBuffer();
				String [] string = str.split(";");

				for (int i = 0; i < string.length; i++) {
					if (string[i].contains("=")){
					String[] s = string[i].split("=");
					values.put(s[0], s[1]);
					}
				}
				String componentValue = values.get("component");
				String[] s1 = componentValue.split("/");
				sb.append(s1[0]).append(s1[1]);
				
				return sb.toString();
			}
}
