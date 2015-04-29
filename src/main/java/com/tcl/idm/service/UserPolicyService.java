package com.tcl.idm.service;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tcl.idm.main.IdmCliMain;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.CustomHttpResponse;
import com.tcl.idm.model.IDMCLIConfig;
import com.tcl.idm.util.DateFormatterUtils;
import com.tcl.idm.util.FileUtils;
import com.tcl.idm.util.HttpRequestUtils;
import com.tcl.idm.util.IDMServiceUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.SignatureUtils;
import com.tcl.idm.util.UrlUtils;

/**
 * UserPolicy CLI
 * 
 * @author yuanhuan
 * 2014年3月31日 下午1:02:26
 */
public class UserPolicyService
{
	public static CustomHttpResponse createUserPolicy(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " createUserPolicy [-h/--help] [-f/--format-json] userId (userId)"
		        + " policyName (policyName) policyDocument (policyDocument)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName()
		        + " createUserPolicy userId 111111-2222-3333333333SAMPLE policyName testPolicy01"
		        + " policyDocument [{\"effect\":\"allow\", \"resource\":\"*:*\"}]";
		String userId = "";
		String policyName = "";
		String policyDocument = "";

		int argsLen = args.length;
		if (argsLen == 1)
		{
			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 6)
		{
			if (!"userId".equals(args[0]) || !"policyName".equals(args[2]) || !"policyDocument".equals(args[4]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				policyName = args[3];
				policyDocument = args[5];
			}
		}
		else
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "createUserPolicy";
		String params = "{\"twsAccessKeyId\":\"" + idmCLIConfig.getAccessKeyId() + "\",\"signatureMethod\":\""
		        + idmCLIConfig.getSignatureMethod() + "\",\"signatureVersion\":\"" + idmCLIConfig.getSignatureVersion()
		        + "\",\"timestamp\":\"" + DateFormatterUtils.getCurrentUTCTime() + "\",\"userId\":\"" + userId
		        + "\",\"policyName\":\"" + policyName + "\",\"policyDocument\":" + policyDocument + "}";
		String signatureString = SignatureService.genSignatureString("POST", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params = params.subSequence(0, params.length() - 1) + ",\"signature\":\"" + signature + "\"}";
		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(idmEndPoint, params);
		return result;
	}

	public static CustomHttpResponse getUserPolicy(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " getUserPolicy [-h/--help] [-f/--format-json] userId (userId) policyId (policyId)\n" + "Samples:\n"
		        + "    " + IdmCliMain.getIDMCLIName()
		        + " getUserPolicy userId 111111-2222-3333333333SAMPLE policyId 222222-2222-3333333333SAMPLE";
		String userId = "";
		String policyId = "";

		int argsLen = args.length;
		if (argsLen == 1)
		{
			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 4)
		{
			if (!"userId".equals(args[0]) || !"policyId".equals(args[2]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				policyId = args[3];
			}
		}
		else
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "getUserPolicy";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&policyId="
		        + policyId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse updateUserPolicy(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " updateUserPolicy [-h/--help] [-f/--format-json] userId (userId)"
		        + " policyId (policyId) [policyName (policyName) policyDocument (policyDocument)]\n" + "Samples:\n"
		        + "    " + IdmCliMain.getIDMCLIName()
		        + " updateUserPolicy userId 111111-2222-3333333333SAMPLE policyId p01 policyName p23";
		String userId = "";
		String policyId = "";
		String policyName = "";
		String policyDocument = "";

		int argsLen = args.length;
		if (argsLen == 1)
		{
			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 6)
		{
			if ("userId".equals(args[0]) && "policyId".equals(args[2])
			        && ("policyName".equals(args[4]) || "policyDocument".equals(args[4])))
			{
				userId = args[1];
				policyId = args[3];

				if ("policyName".equals(args[4]))
				{
					policyName = args[5];
				}

				if ("policyDocument".equals(args[4]))
				{
					policyDocument = args[5];
				}
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 8)
		{
			if ("userId".equals(args[0]) && "policyId".equals(args[2]) && "policyName".equals(args[4])
			        && "policyDocument".equals(args[6]))
			{
				userId = args[1];
				policyId = args[3];
				policyName = args[5];
				policyDocument = args[7];
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "updateUserPolicy";
		String params = "{\"twsAccessKeyId\":\"" + idmCLIConfig.getAccessKeyId() + "\",\"signatureMethod\":\""
		        + idmCLIConfig.getSignatureMethod() + "\",\"signatureVersion\":\"" + idmCLIConfig.getSignatureVersion()
		        + "\",\"timestamp\":\"" + DateFormatterUtils.getCurrentUTCTime() + "\",\"userId\":\"" + userId
		        + "\",\"policyId\":\"" + policyId + "\"";
		if (StringUtils.isNotEmpty(policyName))
		{
			params += ",\"policyName\":\"" + policyName + "\"";
		}
		if (StringUtils.isNotEmpty(policyDocument))
		{
			params += ",\"policyDocument\":" + policyDocument + "";
		}
		params += "}";
		String signatureString = SignatureService.genSignatureString("POST", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params = params.subSequence(0, params.length() - 1) + ",\"signature\":\"" + signature + "\"}";
		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(idmEndPoint, params);
		return result;
	}

	public static CustomHttpResponse listUserPolicys(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listUserPolicys [-h/--help] [-f/--format-json] userId (userId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " listUserPolicys userId 111111-2222-3333333333SAMPLE";
		String userId = "";

		// 检查参数是否合法
		if (null == args || args.length == 0)
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		int argsLen = args.length;
		if (argsLen == 1)
		{
			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 2)
		{
			if (!"userId".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
			}
		}
		else
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "listUserPolicys";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse deleteUserPolicy(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " deleteUserPolicy [-h/--help] [-f/--format-json] userId (userId) policyId (policyId)\n"
		        + "Samples:\n" + "    1. " + IdmCliMain.getIDMCLIName()
		        + " deleteUserPolicy userId 111111-2222-3333333333SAMPLE policyId 555555-2222-3333333333SAMPLE";
		String userId = "";
		String policyId = "";

		int argsLen = args.length;
		if (argsLen == 1)
		{
			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
		}
		else if (argsLen == 4)
		{
			if ("userId".equals(args[0]) && "policyId".equals(args[2]))
			{
				userId = args[1];
				policyId = args[3];
			}
			else
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;

			}
		}
		else
		{
			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "deleteUserPolicy";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&policyId="
		        + policyId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}
}
