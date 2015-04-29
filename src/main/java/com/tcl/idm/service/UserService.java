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

/*
 * User CLI
 * 
 * @author yuanhuan
 * 2014年3月31日 下午1:02:26
 */
public class UserService
{

	public static CustomHttpResponse createUser(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " createUser [-h/--help] [-f/--format-json] [userName (userName) password (password)]\n"
		        + "Samples:\n" + "    1. " + IdmCliMain.getIDMCLIName()
		        + " createUser userName TCL password 123456\n        Create a user.";
		String userName = "";
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
			if ("userName".equals(args[0]) && "password".equals(args[2]))
			{
				userName = args[1];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "createUser";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userName=" + userName + "&password="
		        + password;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse getUser(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " getUser [-h/--help] [-f/--format-json] userId (userId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " getUser userId 111111-2222-3333333333SAMPLE";
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "getUser";
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

	public static CustomHttpResponse listUsers(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listUsers [-h/--help] [-f/--format-json] \n" + "Samples:\n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listUsers -f";

		// 检查参数是否合法
		if (null != args && args.length > 0)
		{
			int argsLen = args.length;
			if (argsLen > 1)
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}

			if ("-h".equals(args[0]) || "--help".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_OK);
				customHttpResponse.setResponseMessage(usage);
				return customHttpResponse;
			}

			customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
			        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
			return customHttpResponse;
		}

		// 构造IDM接口请求参数
		IDMCLIConfig idmCLIConfig = IDMServiceUtils.getIDMCLIConfig();
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "listUsers";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime();
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse deleteUser(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " deleteUser [-h/--help] [-f/--format-json] userId (userId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " deleteUser userId 111111-2222-3333333333SAMPLE";
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "deleteUser";
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

	public static CustomHttpResponse updateUser(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " updateUser [-h/--help] [-f/--format-json] userId (userId) userName (userName)\n" + "Samples:\n"
		        + "    " + IdmCliMain.getIDMCLIName() + " updateUser userId 111111-2222-3333333333SAMPLE userName Bob";
		String userId = "";
		String userName = "";

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
			if (!"userId".equals(args[0]) || !"userName".equals(args[2]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				userName = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "updateUser";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&userName="
		        + userName;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse changePassword(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " changePassword [-h/--help] [-f/--format-json] userId (userId)"
		        + " oldPassword (oldPassword) newPassword (newPassword)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName()
		        + " changePassword userId 111111-2222-3333333333SAMPLE oldPassword 123456 newPassword China.2013";
		String userId = "";
		String oldPassword = "";
		String newPassword = "";

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
			if (!"userId".equals(args[0]) || !"oldPassword".equals(args[2]) || !"newPassword".equals(args[4]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				oldPassword = args[3];
				newPassword = args[5];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "changePassword";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&oldPassword="
		        + oldPassword + "&newPassword=" + newPassword;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}
}
