package com.tcl.idm.service;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tcl.idm.main.IdmCliMain;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.CustomHttpResponse;
import com.tcl.idm.model.IDMCLIConfig;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.DateFormatterUtils;
import com.tcl.idm.util.FileUtils;
import com.tcl.idm.util.HttpRequestUtils;
import com.tcl.idm.util.IDMServiceUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.SignatureUtils;
import com.tcl.idm.util.UrlUtils;

/*
 * Group CLI
 * 
 * @author yuanhuan
 * 2014年3月31日 下午1:02:26
 */
public class GroupService
{
	public static CustomHttpResponse createGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " createGroup [-h/--help] [-f/--format-json] groupName (groupName) [description (description)]\n"
		        + "Samples:\n" + "    1. " + IdmCliMain.getIDMCLIName()
		        + " createGroup groupName TCL description 123456\n        Create a group.";
		String groupName = "";
		String description = "";

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
			if ("groupName".equals(args[0]))
			{
				groupName = args[1];
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
			if ("groupName".equals(args[0]) && "description".equals(args[2]))
			{
				groupName = args[1];
				description = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "createGroup";
		String paramsWithoutDesc = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&groupName=" + groupName;
		String paramsWithDesc = paramsWithoutDesc;
		String paramsWithEncodedDesc = paramsWithoutDesc;
		if (StringUtils.isNotEmpty(description))
		{
			paramsWithDesc += "&description=" + description;
			paramsWithEncodedDesc += "&description=" + CommonService.getURLEncodeString(description);
		}
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), paramsWithDesc);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		paramsWithEncodedDesc += "&signature=" + signature;
		String url = idmEndPoint + "?" + paramsWithEncodedDesc;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse getGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " getGroup [-h/--help] [-f/--format-json] groupId (groupId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " getGroup groupId 111111-2222-3333333333SAMPLE";
		String groupId = "";

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
			if (!"groupId".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				groupId = args[1];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "getGroup";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&groupId=" + groupId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse listGroups(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " listGroups [-h/--help] [-f/--format-json] [userId (userId)]\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " listGroups userId 111111-2222-3333333333SAMPLE";
		String userId = "";

		// 检查参数是否合法
		if (null == args)
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
		else if (argsLen == 0)
		{
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "listGroups";
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

	public static CustomHttpResponse deleteGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " deleteGroup [-h/--help] [-f/--format-json] groupId (groupId)\n" + "Samples:\n" + "    "
		        + IdmCliMain.getIDMCLIName() + " deleteGroup groupId 111111-2222-3333333333SAMPLE";
		String groupId = "";

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
			if (!"groupId".equals(args[0]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				groupId = args[1];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "deleteGroup";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&groupId=" + groupId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse updateGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n"
		        + "    "
		        + IdmCliMain.getIDMCLIName()
		        + " updateGroup [-h/--help] [-f/--format-json] groupId (groupId) [groupName (groupName) description (description)]\n"
		        + "Samples:\n" + "    " + IdmCliMain.getIDMCLIName()
		        + " updateGroup groupId 111111-2222-3333333333SAMPLE groupName testGroup01";
		String groupId = "";
		String groupName = "";
		String description = "";

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
			if ("groupId".equals(args[0]) && ("groupName".equals(args[2]) || "description".equals(args[2])))
			{
				groupId = args[1];

				if ("groupName".equals(args[2]))
				{
					groupName = args[3];
				}

				if ("description".equals(args[2]))
				{
					description = args[3];
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
		else if (argsLen == 6)
		{
			if ("groupId".equals(args[0]) && "groupName".equals(args[2]) && "description".equals(args[4]))
			{
				groupId = args[1];
				groupName = args[3];
				description = args[5];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "updateGroup";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&groupId=" + groupId;
		if (StringUtils.isNotEmpty(groupName))
		{
			params += "&groupName=" + groupName;
		}

		if (StringUtils.isNotEmpty(description))
		{
			params += "&description=" + description;
		}

		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse addUserToGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " addUserToGroup [-h/--help] [-f/--format-json] userId (userId) groupId (groupId)\n" + "Samples:\n"
		        + "    " + IdmCliMain.getIDMCLIName()
		        + " addUserToGroup userId 111111-2222-3333333333SAMPLE groupId 222222-1152-3333333333SAMPLE";
		String userId = "";
		String groupId = "";

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
			if (!"userId".equals(args[0]) || !"groupId".equals(args[2]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				groupId = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "addUserToGroup";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&groupId=" + groupId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}

	public static CustomHttpResponse removeUserFromGroup(String[] args)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		String usage = "Usage: \n" + "    " + IdmCliMain.getIDMCLIName()
		        + " removeUserFromGroup [-h/--help] [-f/--format-json] userId (userId) groupId (groupId)\n"
		        + "Samples:\n" + "    " + IdmCliMain.getIDMCLIName()
		        + " removeUserFromGroup userId 111111-2222-3333333333SAMPLE groupId Bob";
		String userId = "";
		String groupId = "";

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
			if (!"userId".equals(args[0]) || !"groupId".equals(args[2]))
			{
				customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customHttpResponse.setResponseMessage(IdmErrorMessageUtils.genErrorMessageInJson(
				        CustomErrorCode.InvalidParameter.getCode(), CustomErrorCode.InvalidParameter.getMessage()));
				return customHttpResponse;
			}
			else
			{
				userId = args[1];
				groupId = args[3];
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
		String idmEndPoint = FileUtils.addSlashToPathIfNecessary(idmCLIConfig.getIdmEndPoint()) + "removeUserFromGroup";
		String params = "twsAccessKeyId=" + idmCLIConfig.getAccessKeyId() + "&signatureMethod="
		        + idmCLIConfig.getSignatureMethod() + "&signatureVersion=" + idmCLIConfig.getSignatureVersion()
		        + "&timestamp=" + DateFormatterUtils.getCurrentUTCTime() + "&userId=" + userId + "&groupId=" + groupId;
		String signatureString = SignatureService.genSignatureString("GET", UrlUtils.getHostByEndPoint(idmEndPoint),
		        UrlUtils.getURIByEndPoint(idmEndPoint), params);
		String signature = SignatureUtils.calculateRFC2104HMAC(signatureString, idmCLIConfig.getSecretAccessKey());
		params += "&signature=" + signature;
		String url = idmEndPoint + "?" + params;
		CustomHttpResponse result = HttpRequestUtils.sendGetRequest(url);
		return result;
	}
}
