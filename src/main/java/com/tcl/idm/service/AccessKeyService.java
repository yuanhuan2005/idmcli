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
 * AccessKey CLI
 * 
 * @author yuanhuan
 * 2014年3月31日 下午1:02:26
 */
public class AccessKeyService
{

	public static CustomHttpResponse createAccessKey(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " createAccessKey [-h/--help] [-f/--format-json] [userId (userId)]\n" + "Samples:\n" + "    1. "
		        + IdmCliMain.getIDMCLIName()
		        + " createAccessKey userId 111111-2222-3333333333SAMPLE\n        create a access key for this user.\n"
		        + "    2. " + IdmCliMain.getIDMCLIName()
		        + " createAccessKey\n        create a access key for current account";
		String userId = "";

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
				// 此时为当前账户下的某一个用户创建AccessKey
				userId = args[1];
			}
		}
		else if (argsLen == 0)
		{
			// 此时为当前账户自己创建AccessKey
			userId = "";
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "createAccessKey";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime();
		if (StringUtils.isNotEmpty(userId))
		{
			params += "&userId=" + userId;
		}
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse deleteAccessKey(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " deleteAccessKey [-h/--help] [-f/--format-json] accessKeyId (accessKeyId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " deleteAccessKey accessKeyId 111111-2222-3333333333SAMPLE";
		String accessKeyId = "";

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
			if (!"accessKeyId".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				accessKeyId = args[1];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "deleteAccessKey";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&accessKeyId=" + accessKeyId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse updateAccessKey(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " updateAccessKey [-h/--help] [-f/--format-json] accessKeyId (accessKeyId) status (status)\n"
		        + "Samples:\n" + "    " + IdmCliMain.getIDMCLIName()
		        + " updateAccessKey accessKeyId 111111-2222-3333333333SAMPLE status inactive";
		String accessKeyId = "";
		String status = "";

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
			if (!"accessKeyId".equals(args[0]) || !"status".equals(args[2]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				accessKeyId = args[1];
				status = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "updateAccessKey";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&accessKeyId=" + accessKeyId + "&status="
		        + status;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse listAccessKeys(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listAccessKeys [-h/--help] [-f/--format-json] [userId (userId)]\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " listAccessKeys -f\n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listAccessKeys -f userId 111111-2222-333333333333333SAMPLE";
		String userId = "";

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
				// 此时，列出该用户的所有AccessKey
				userId = args[1];
			}
		}
		else if (argsLen == 0)
		{
			// 此时，列出当前账户的AccessKey
			userId = "";
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "listAccessKeys";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime();
		if (StringUtils.isNotEmpty(userId))
		{
			params += "&userId=" + userId;
		}
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}
}
