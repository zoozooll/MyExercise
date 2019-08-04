package com.mogoo.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

// [类名]---[时间][日志级别][行号][日志内容][附件信息][异常信息][输出日志消耗时间]
public class LogUtil
{

	private LogUtil()
	{

	}

	/**
	 * TIMESTAMP�ĸ�ʽΪ��"Mmm dd hh:mm:ss"
	 * Mmm��ȡֵ���£�Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec 
	 * dd,hh,mm,ss�ֱ����ա�Сʱ���֡���	 * 
	 * @return String
	 */
	public static String getTimestamp(java.util.Date date)
	{
		SimpleDateFormat sm = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.US);
		return sm.format(date);
	}

	/**
	 * ��ȡ����ʱ��[yyyy-MM-dd HH:mm:ss]
	 * 
	 * @param date Date
	 * @return String
	 */
	public static String getDateTime(java.util.Date date)
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sm.format(date);

	}

	/**
	 * ��ȡ����ʱ��[MM-dd HH:mm:ss]
	 * 
	 * @param date Date
	 * @return String
	 */
	public static String getDateTime2(java.util.Date date)
	{
		SimpleDateFormat sm = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sm.format(date);

	}
	
	
	/**
	 * ת����־������Ϣ
	 * 
	 * @param additionInfo String ������Ϣ
	 * @return String
	 */
	public static String convertAddInfo(String... additionInfo)
	{
		String addinfo="";
		for(String s:additionInfo)
		{
			addinfo = addinfo  + s +",";
		}
		if(addinfo.lastIndexOf(",")>-1)
		{
			addinfo = addinfo.substring(0, addinfo.lastIndexOf(","));
		}
		
		return addinfo;
	}

	
	
//	public static String[] getInvokeClassInfo()
//	{
//		
//		String[] str =null;
//		StackTraceElement stack[] = (new Throwable()).getStackTrace();
//		if(stack!=null)
//		{
//				str = new String[4];
//				//包名
//				str[0] = stack[3].getClassName();
//				//类名
//				str[1] = stack[3].getFileName();
//				//方法名
//				str[2] = stack[3].getMethodName();
//				//行号
//				str[3] =  String.valueOf(stack[3].getLineNumber());		
//
//		}
//		
//		return str;		
//	}
	
	
	public static String[] getInvokeClassInfo(String classname)
	{
		
		String[] str =null;
		int index=-1;
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		if(stack!=null)
		{
			for(int i=0;i<stack.length;i++)
			{
				if(stack[i].getClassName().contains(classname))
				{
					index =i;
					break;
				}
			}
			
			if(index>-1)
			{
				str = new String[4];
				//包名
				str[0] = stack[index].getClassName();
				//类名
				str[1] = stack[index].getFileName();
				//方法名
				str[2] = stack[index].getMethodName();
				//行号
				str[3] =  String.valueOf(stack[index].getLineNumber());		
			}				
		}
		
		return str;
		
	}
	
	public static String[] getInvokeClassInfo(Throwable t)
	{
		
		String[] str =null;		
		StackTraceElement stack[] = t.getStackTrace();
		if(stack!=null)
		{
			str = new String[4];
			//包名
			str[0] = stack[2].getClassName();
			//类名
			str[1] = stack[2].getFileName();
			//方法名
			str[2] = stack[2].getMethodName();
			//行号
			str[3] =  String.valueOf(stack[2].getLineNumber());			
		}
		
		return str;
		
	}
	
	// [类名]---[日志级别][时间][模块][包名][方法名][行号][日志内容][附件信息][异常信息]
	public static String formatLog(String module,String[] classinfo,String message)
	{
		String content ="[" + getDateTime2(new Date()) +"]"		   				
		   				+ "[" + module +"]"
		   				+ "[" + classinfo[0] +"]"
		   				+ "[" + classinfo[2] +"]"
		   				+ "[" + classinfo[3] +"]"
		   				+ "-[" + message +"]";
		   				//+ "[" + LogUtil.convertAddInfo(additionalinfo) +"]";
		
		
		
	    return content;
	}

	
	// [类名]---[日志级别][时间][模块][包名][方法名][行号][日志内容][附件信息][异常信息]
	public static String formatLog(String module,String[] classinfo,String message,Throwable t)
	{
		String content ="[" + getDateTime2(new Date()) +"]"		   				
		   				+ "[" + module +"]"
		   				+ "[" + classinfo[0] +"]"
		   				+ "[" + classinfo[2] +"]"
		   				+ "[" + classinfo[3] +"]"
		   				+ "-[" + message +"]"
		                + "-[" + getStackTraceString(t) +"]";
		
		
		
		
	    return content;
	}
	
	/**
	 * ��ָ���ַ��ֳ��ַ�����
	 * 
	 * @param text String �ı�
	 * @param regex String ����ַ�
	 * @return String[]
	 */
	private static String[] split(String text, String regex)
	{
		return text.split(regex);
	}

	
	
	
	public static void main(String[] args)
	{
		
	}
	
	
    public static String getStackTraceString(Throwable tr) 
    {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
}
