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
 * ����������
 * @author TCL
 *
 */
public class CommonService
{
	/**
	 * ��ȡidm������
	 * 
	 * @param confKey
	 *            ����������
	 * @return confValue ������ֵ
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
	 * ��ȡJDBC������
	 * 
	 * @param confKey
	 *            ����������
	 * @return confValue ������ֵ
	 */
	public static String getJDBCConfValue(String confKey)
	{
		return CommonService.getBaseConfValue("jdbc.properties", confKey);
	}

	/**
	 * �������������ƻ�ȡ��Ӧ��ֵ
	 * 
	 * @param propertitesFile
	 *            property file
	 * @param confKey
	 *            ����������
	 * @return confValue ������ֵ
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
	 * ��ͨJAVA��ȡ WEB��Ŀ�µ�WEB-INFĿ¼·��
	 * 
	 * @return WEB-INFĿ¼·��
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
			// ��class�ļ���war��ʱ����ʱ����zip:D:/...������·��
			path = path.substring(4);
		}
		else if (path.startsWith("file"))
		{
			// ��class�ļ���class�ļ���ʱ����ʱ����file:/D:/...������·��
			path = path.substring(6);
		}
		else if (path.startsWith("jar"))
		{
			// ��class�ļ���jar�ļ�����ʱ����ʱ����jar:file:/D:/...������·��
			path = path.substring(10);
		}

		path = CommonService.getURLDecodeString(path);

		return path;
	}

	/**
	 * ����������
	 * 
	 * @param seconds
	 *            ����
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
	 * ���ɳɹ�����ʱ���JSON�ַ���
	 * 
	 * @return
	 */
	public static String genSuccesResultJsonString()
	{
		return "{\"reuslt\":\"" + CustomErrorCode.Success.getCode() + "\"}";
	}

	/**
	 * ���ַ�������URL����
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
	 * ���ַ�������URL����
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
	 * ���ɰ������ֺʹ�Сд��ĸ������ַ���
	 * 
	 * @param length �ַ�������
	 * @return ������ɵ��ַ���
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
