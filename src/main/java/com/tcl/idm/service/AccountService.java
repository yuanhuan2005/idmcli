package com.tcl.idm.service;

import javax.servlet.http.HttpServletResponse;

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
 * Account CLI
 * 
 * @author yuanhuan
 * 2014年3月31日 下午1:02:26
 */
public class AccountService
{
	public static CustomHttpResponse createAccount(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " createAccount [-h/--help] [-f/--format-json] [accountName (accountName) password (password)]\n"
		        + "Samples:\n" + "    1. " + IdmCliMain.getIDMCLIName()
		        + " createAccount accountName TCL password 123456\n        Create a account.";
		String accountName = "";
		String password = "";

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
			if ("accountName".equals(args[0]) && "password".equals(args[2]))
			{
				accountName = args[1];
				password = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "createAccount";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&accountName=" + accountName + "&password="
		        + password;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse deleteAccount(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " deleteAccount [-h/--help] [-f/--format-json] accountId (accountId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " deleteAccount accountId 111111-2222-3333333333SAMPLE";
		String accountId = "";

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
			if (!"accountId".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				accountId = args[1];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "deleteAccount";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&accountId=" + accountId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}
}
