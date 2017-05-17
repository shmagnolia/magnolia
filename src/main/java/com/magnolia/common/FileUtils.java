package com.magnolia.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class FileUtils {

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				if (!deleteDir(new File(dir, children[i]))) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public static void createDir(String baseDir, String subDir) throws IOException {
		createDir(new File(baseDir));
		File file = new File(baseDir + subDir);
		Files.createDirectories(file.toPath());
	}

	/**
	 * 当目录不存在时，创建目录；如果父目录不存在，则会先创建父目录
	 * @param file
	 * @throws IOException
	 */
	public static void createDir(File file) throws IOException {
		if (!file.exists()) {
			Files.createDirectories(file.toPath());
		}
	}

	
	public static String fileReaderAsString(String filePath) throws Exception{
		 File file = new File(filePath);
		 return fileReaderAsString(file);
	}
	
	public static String fileReaderAsString(File file) throws Exception{
		 FileInputStream in = new FileInputStream(file);
		 int size = in.available();
	     byte[] buffer = new byte[size];
	     in.read(buffer);
	     in.close();
	     return new String(buffer,"UTF-8");
	}	
	
	/**
	 * 把String内容写到文件
	 * @param fileName
	 * @param content
	 */
	public static void outputToFile(String path, String fileName, String content) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path + fileName);
			byte[] b = content.getBytes();
			os.write(b);
			os.flush();
			System.out.println("outputToFile:"+path + fileName);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}
	}

	public static String getFileContentWhichHasBundles(String path,ResourceBundle resourceBundle) throws Exception {
		File file = new File(path);
		return getFileContentWhichHasBundles(file,resourceBundle,'$');
	}
	
	public static String getFileContentWhichHasBundles(String path,ResourceBundle resourceBundle,char splitChar) throws Exception {
		File file = new File(path);
		return getFileContentWhichHasBundles(file,resourceBundle,splitChar);
	}
	
	public static String getFileContentWhichHasBundles(File file,ResourceBundle resourceBundle,char splitChar) throws Exception {
		String content = fileReaderAsString(file);
		String subContent = null;
		String bundleKey = null;
		char[] chars = content.toCharArray();
		
		for (int i = 0;i < chars.length; i++) {
			if(chars[i] == splitChar){
				subContent = content.substring(i+2);
//				System.out.println("subContent.indexOf(}):"+subContent.indexOf("}"));
				bundleKey = subContent.substring(0,subContent.indexOf("}"));
//				System.out.println("bundleKey:"+bundleKey);
				if(i == 0){
					content = resourceBundle.getString(bundleKey) + content.substring(bundleKey.length() + 3);					
				}else{
					content = content.substring(0, i) + resourceBundle.getString(bundleKey) + content.substring(i + bundleKey.length() + 3);
				}
				chars = content.toCharArray();
			}
		}
		return content;
	}
	
	
//	public static void main(String[] args) throws Exception {
//		String generatorPropertiesPath = Class.class.getClass().getResource("/").getPath() +"/generator.properties";
//		ResourceBundle generatorRb = new PropertyResourceBundle(new BufferedInputStream(new FileInputStream(generatorPropertiesPath)));	
//		String path = Class.class.getClass().getResource("/").getPath() +"/java/common/Application.java.demo";
//		String content = getFileContentWhichHasBundles(path,generatorRb);
//		System.out.println(content);
//	}
}
