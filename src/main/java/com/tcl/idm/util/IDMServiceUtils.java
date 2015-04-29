package com.tcl.idm.util;

import java.io.File;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.IDMCLIConfig;
import com.tcl.idm.model.PolicyDocument;

/**
 * IDM工具类
 * 
 * @author yuanhuan
 * 2014年4月4日 上午9:38:08
 */
public class IDMServiceUtils
{
	public static String genAccessKey()
	{
		return CommonService.randomString(32);
	}

	public static String genSecretAccessKey()
	{
		return CommonService.randomString(48);
	}

	public static String genOpenIDMObjectId()
	{
		return CommonService.randomString(32);
	}

	public static PolicyDocument[] convertToPolicyDocumentArray(String policyDocumentJsonString)
	{
		PolicyDocument[] policyDocumentArr = null;

		try
		{
			JSONArray policyDocumentJSONArray = JSONArray.fromObject(policyDocumentJsonString);
			if (null != policyDocumentJSONArray && policyDocumentJSONArray.size() > 0)
			{
				policyDocumentArr = new PolicyDocument[policyDocumentJSONArray.size()];
				PolicyDocument policyDocument = null;
				for (int i = 0; i < policyDocumentJSONArray.size(); i++)
				{
					policyDocument = new PolicyDocument();
					policyDocument.setEffect(((JSONObject) policyDocumentJSONArray.get(i)).getString("effect"));
					policyDocument.setResource(((JSONObject) policyDocumentJSONArray.get(i)).getString("resource"));
					policyDocumentArr[i] = policyDocument;
				}
			}
		}
		catch (Exception e)
		{
		}

		return policyDocumentArr;
	}

	/**
	 * 获取JSON格式的响应消息，如果输入参数不是Json格式，则转换成Json格式。
	 * 
	 * @param jsonMessage
	 * @return
	 */
	public static String getJsonMessage(String jsonMessage)
	{
		try
		{
			JSONObject.fromObject(jsonMessage);
		}
		catch (Exception e)
		{
			try
			{
				JSONArray.fromObject(jsonMessage);
			}
			catch (Exception e1)
			{
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.IDMServiceUnreachable.getCode(),
				        CustomErrorCode.IDMServiceUnreachable.getMessage());
			}
		}

		return jsonMessage;
	}

	public static String getIdmCliConfigFilePath()
	{
		return System.getProperty("user.home") + File.separator + ".idmcli";
	}

	public static IDMCLIConfig getIDMCLIConfig()
	{
		IDMCLIConfig idmCLIConfig = new IDMCLIConfig();
		String idmCliConfigFilePath = IDMServiceUtils.getIdmCliConfigFilePath();

		idmCLIConfig.setIdmEndPoint(CommonService.getPropertiesConfValue(idmCliConfigFilePath, "IDMEndPoint"));
		idmCLIConfig.setAccessKeyId(CommonService.getPropertiesConfValue(idmCliConfigFilePath, "AccessKeyId"));
		idmCLIConfig.setSecretAccessKey(AESUtils.decrypt(CommonService.getPropertiesConfValue(idmCliConfigFilePath,
		        "SecretAccessKey")));
		idmCLIConfig.setSignatureMethod(CommonService.getPropertiesConfValue(idmCliConfigFilePath, "SignatureMethod"));
		idmCLIConfig
		        .setSignatureVersion(CommonService.getPropertiesConfValue(idmCliConfigFilePath, "SignatureVersion"));

		return idmCLIConfig;
	}
}
