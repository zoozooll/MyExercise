/**
 * 
 */
package com.butterfly.vv.beans;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hongbo ke
 */
public class CalendarTree {
	private List<YearTree> tree;

	/**
	 * @param dateStr Input date string ,formatted like this:2015-04-22
	 */
	public void addDate(String dateStr) {
		if (tree == null) {
			tree = new LinkedList<CalendarTree.YearTree>();
		}
	}

	public static class YearTree {
		public String year;
		public List<MonthTree> months;
	}

	public static class MonthTree {
		public String month;
		public List<DayNode> days;
	}

	public static class DayNode {
		public String day;
	}
}
