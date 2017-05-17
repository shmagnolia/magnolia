package com.magnolia.common;

public class FormatUtils {

	/**
	 * 处理字符串，去掉下划线“_”，并且把下划线的下一个字符变大写，flag为true，表示首字母要大写
	 * 
	 * @param name
	 * @param flag
	 * @return
	 */
	public static String getFormatString(String name, boolean flag) {
		name = name.toLowerCase();
		if (name.startsWith("t_")) { // 表名以t_开头
			name = name.substring(2);
		}
		String[] nameTemp = name.split("_");
		StringBuilder buffer = new StringBuilder();
		for (String str : nameTemp) {
			String head = str.substring(0, 1).toUpperCase();
			String tail = str.substring(1);
			buffer.append(head + tail);
		}
		StringBuilder result = null;
		if (!flag) {
			result = new StringBuilder();
			String head = buffer.substring(0, 1).toLowerCase();
			String tail = buffer.substring(1);
			result.append(head + tail);
			return result.toString();
		}
		return buffer.toString();
	}
	
	
	public static String firstLetterCaps ( String data ){
      String firstLetter = data.substring(0,1).toUpperCase();
      String restLetters = data.substring(1).toLowerCase();
      return firstLetter + restLetters;
   }
	
	 /** 
     * 处理字符串，去掉下划线“_”，并且把下划线的下一个字符变大写，flag为true，表示首字母要大写 
     * @param name 
     * @param flag 
     * @return 
     */  
	public static String getCollumnFormatString(String name, boolean flag) {  
    	name = name.toLowerCase();  
        String[] nameTemp = name.split("_");  
        StringBuilder buffer = new StringBuilder();  
        for(String str : nameTemp) {  
            String head = str.substring(0, 1).toUpperCase();  
            String tail = str.substring(1);  
            buffer.append(head+tail);  
        }  
        StringBuilder result = null;  
        if(!flag) {  
            result = new StringBuilder();  
            String head = buffer.substring(0, 1).toLowerCase();  
            String tail = buffer.substring(1);  
            result.append(head+tail);  
            return result.toString();  
        }  
        return buffer.toString();  
    }  
 
	public static String getClassFormatString(String name, boolean flag) {  
    	name = name.toLowerCase();
    	if(name.startsWith("t_")){  //表名以t_开头
    		name = name.substring(2);
    	}
    	String[] nameTemp = name.split("_");  
        StringBuilder buffer = new StringBuilder();  
        for(String str : nameTemp) {  
            String head = str.substring(0, 1).toUpperCase();  
            String tail = str.substring(1);  
            buffer.append(head+tail);  
        }  
        StringBuilder result = null;  
        result = new StringBuilder();  
        String head = buffer.substring(0, 1).toUpperCase();  
        String tail = buffer.substring(1);  
        result.append(head+tail);  
        return result.toString();  
    }  

}
