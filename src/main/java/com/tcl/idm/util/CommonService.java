package com.tcl.idm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.tcl.idm.model.CustomErrorCode;

/**
 * 公共服务类
 * @author TCL
 *
 */
public class CommonService
{
	/**
	 * 获取idm配置项
	 * 
	 * @param confKey
	 *            配置项名称
	 * @return confValue 配置项值
	 */
	public static String getIdmCliConfValue(String confKey)
	{
		String confValue = "";
		String propertiesFileName = "idmcli.properties";
		String propertiesFilePath = "/resources/" + propertiesFileName;
		InputStream is = CommonService.class.getResourceAsStream(propertiesFilePath);
		confValue = CommonService.getPropertiesConfValue(is, confKey);
		if (StringUtils.isEmpty(confValue))
		{
			confValue = CommonService.getBaseConfValue(propertiesFileName, confKey);
		}

		return confValue;
	}

	/**
	 * 获取JDBC配置项
	 * 
	 * @param confKey
	 *            配置项名称
	 * @return confValue 配置项值
	 */
	public static String getJDBCConfValue(String confKey)
	{
		return CommonService.getBaseConfValue("jdbc.properties", confKey);
	}

	/**
	 * 根据配置项名称获取对应的值
	 * 
	 * @param propertitesFile
	 *            property file
	 * @param confKey
	 *            配置项名称
	 * @return confValue 配置项值
	 */
	public static String getBaseConfValue(String propertiesFile, String confKey)
	{
		InputStream inputStream = new CommonService().getClass().getClassLoader().getResourceAsStream(propertiesFile);
		return CommonService.getPropertiesConfValue(inputStream, confKey);
	}

	public static String getPropertiesConfValue(String propertiesFile, String confKey)
	{
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(new File(propertiesFile));
			return CommonService.getPropertiesConfValue(inputStream, confKey);
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.toString());
		}

		return "";
	}

	public static String getPropertiesConfValue(InputStream inputStream, String confKey)
	{
		String confValue = "";

		Properties p = new Properties();

		try
		{
			p.load(inputStream);
			confValue = p.getProperty(confKey);
		}
		catch (Exception e)
		{
			confValue = "";
		}
		finally
		{
			try
			{
				if (null != inputStream)
				{
					inputStream.close();
				}
			}
			catch (IOException e)
			{
				confValue = "";
			}
		}

		return confValue;
	}

	/**
	 * 普通JAVA获取 WEB项目下的WEB-INF目录路径
	 * 
	 * @return WEB-INF目录路径
	 */
	public static String getWebInfPath()
	{
		URL url = new CommonService().getClass().getProtectionDomain().getCodeSource().getLocation();
		String path = url.toString();
		int index = path.indexOf("WEB-INF");

		if (index == -1)
		{
			index = path.indexOf("classes");
		}

		if (index == -1)
		{
			index = path.indexOf("bin");
		}

		path = path.substring(0, index);

		if (path.startsWith("zip"))
		{
			// 当class文件在war中时，此时返回zip:D:/...这样的路径
			path = path.substring(4);
		}
		else if (path.startsWith("file"))
		{
			// 当class文件在class文件中时，此时返回file:/D:/...这样的路径
			path = path.substring(6);
		}
		else if (path.startsWith("jar"))
		{
			// 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
			path = path.substring(10);
		}

		path = CommonService.getURLDecodeString(path);

		return path;
	}

	/**
	 * 休眠若干秒
	 * 
	 * @param seconds
	 *            秒数
	 */
	public static void sleep(long seconds)
	{
		try
		{
			Thread.sleep(seconds * 1000);
		}
		catch (InterruptedException e)
		{
			// ignore
		}
	}

	/**
	 * 生成成功操作时候的JSON字符串
	 * 
	 * @return
	 */
	public static String genSuccesResultJsonString()
	{
		return "{\"reuslt\":\"" + CustomErrorCode.Success.getCode() + "\"}";
	}

	/**
	 * 将字符串进行URL解码
	 * 
	 * @param str
	 * @return
	 */
	public static String getURLDecodeString(String str)
	{
		try
		{
			return URLDecoder.decode(str, "UTF-8");
		}
		catch (Exception e)
		{
		}

		return str;
	}

	/**
	 * 将字符串进行URL编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getURLEncodeString(String str)
	{
		try
		{
			return URLEncoder.encode(str, "UTF-8");
		}
		catch (Exception e)
		{
		}

		return str;
	}

	/**
	 * 生成包含数字和大小写字母的随机字符串
	 * 
	 * @param length 字符串长度
	 * @return 随机生成的字符串
	 */
	public static String randomString(int length)
	{
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int strLen = str.length();
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int num = random.nextInt(strLen);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
}
