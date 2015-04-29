package com.tcl.idm.util;

import java.util.Map;

import net.sf.json.JSONArray;

import com.tcl.idm.model.PolicyDocument;
import com.tcl.idm.model.PolicyEffect;

/**
 * 策略工具类
 * 
 * @author yuanhuan
 * 2014年4月21日 下午3:38:24
 */
public class PolicyUtils
{
	public static boolean isPolicyDocumentValid(String policyDocumentString)
	{
		try
		{
			PolicyDocument[] policyDocumentArray = IDMServiceUtils.convertToPolicyDocumentArray(policyDocumentString);
			if (null == policyDocumentArray || policyDocumentArray.length < 1)
			{
				return false;
			}

			// 获取合法的策略资源映射
			Map<String, String[]> validResourceMap = PolicyResourceUtils.getInstance().getValidResourceMap();

			// 遍历policyDocumentString，检查是否合法
			PolicyDocument policyDocument = null;
			String serviceName = "";
			String apiName = "";
			String[] validApiNameArr = null;
			for (int policyDocumentArrayIndex = 0; policyDocumentArrayIndex < policyDocumentArray.length; policyDocumentArrayIndex++)
			{
				policyDocument = policyDocumentArray[policyDocumentArrayIndex];

				// 检查effect是否合法
				if (!PolicyEffect.isPolicyEffectValid(policyDocument.getEffect()))
				{
					return false;
				}

				// 是否是所有资源
				if ("*:*".equals(policyDocument.getResource()))
				{
					continue;
				}

				// 检查ServiceName是否合法， 格式resource = [ServiceName]:[ApiName]
				serviceName = policyDocument.getResource().split(":")[0].trim();
				if (null == validResourceMap.get(serviceName))
				{
					return false;
				}

				// 检查ApiName是否合法， 格式resource = [ServiceName]:[ApiName]
				apiName = policyDocument.getResource().split(":")[1].trim();
				if ("*".equals(apiName))
				{
					continue;
				}
				validApiNameArr = validResourceMap.get(serviceName);
				boolean validApiNameFlag = false;
				for (int validApiNameArrIndex = 0; validApiNameArrIndex < validApiNameArr.length; validApiNameArrIndex++)
				{
					if (apiName.equals(validApiNameArr[validApiNameArrIndex]))
					{
						validApiNameFlag = true;
						continue;
					}
				}
				if (!validApiNameFlag)
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	public static void main(String[] args)
	{
		PolicyDocument pd1 = new PolicyDocument();
		pd1.setEffect(PolicyEffect.ALLOW);
		pd1.setResource("*:*");
		PolicyDocument pd2 = new PolicyDocument();
		pd2.setEffect(PolicyEffect.ALLOW);
		pd2.setResource("transcode:logo");
		PolicyDocument pd3 = new PolicyDocument();
		pd3.setEffect("aaa");
		pd3.setResource("cameratake:*");

		PolicyDocument[] pdArr = new PolicyDocument[3];
		pdArr[0] = pd1;
		pdArr[1] = pd2;
		pdArr[2] = pd3;

		String policyDocumentString = JSONArray.fromObject(pdArr).toString();
		boolean result = PolicyUtils.isPolicyDocumentValid(policyDocumentString);
		System.out.println(result);
	}
}
