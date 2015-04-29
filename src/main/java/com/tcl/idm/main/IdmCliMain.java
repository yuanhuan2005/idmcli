package com.tcl.idm.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tcl.idm.model.CLIExitValue;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.CustomHttpResponse;
import com.tcl.idm.model.UserType;
import com.tcl.idm.service.AccessKeyService;
import com.tcl.idm.service.IdmApiForAccountService;
import com.tcl.idm.service.IdmApiForAdminService;
import com.tcl.idm.util.AESUtils;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.FileUtils;
import com.tcl.idm.util.IDMServiceUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.JsonFormatUtils;

/**
 * IDM CLI Main function
 * 
 * @author yuanhuan
 * 2014年4月22日 上午10:49:45
 */
public class IdmCliMain
{

	/**
	 * 生成IDM CLI帮助信息
	 * @return
	 */
	private static String getIDMCLIUsageString()
	{
		StringBuilder usageStringBuilder = new StringBuilder();
		usageStringBuilder.append("Usage:\n");
		usageStringBuilder.append("    1. " + IdmCliMain.getIDMCLIName() + " [OPTIONS]...\n");
		usageStringBuilder.append("        OPTIONS: Options for IDM CLI.\n");
		usageStringBuilder.append("            -i/--install: Install IDM CLI.\n");
		usageStringBuilder.append("            -u/--uninstall: Uninstall IDM CLI.\n");
		usageStringBuilder.append("            -l/--list-all-apis: List all IDM interfaces.\n");
		usageStringBuilder.append("            -h/--help: Display this help and exit.\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("    2. " + IdmCliMain.getIDMCLIName()
		        + " (IDM_API_NAME) [API_OPTIONS/API_PARAMETERS]...\n");
		usageStringBuilder.append("        IDM_API_NAME: The API Name of IDM. Case Sensitive.\n");
		usageStringBuilder.append("        API_OPTIONS: Options for this IDM API.\n");
		usageStringBuilder.append("            -f/--format-json: Convert json result to human readable format.\n");
		usageStringBuilder.append("            -h/--help: Display IDM API help and exit.\n");
		usageStringBuilder.append("        API_PARAMETERS: The API parameter(s) of this IDM interface.\n");
		usageStringBuilder.append("            See API help infomation.\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("Description: \n");
		usageStringBuilder.append("    Call IDM interface by specifying the API name and parameters.\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("Samples: \n");
		usageStringBuilder.append("    1. " + IdmCliMain.getIDMCLIName() + " -l\n");
		usageStringBuilder.append("       List all IDM interfaces.\n");
		usageStringBuilder.append("    2. " + IdmCliMain.getIDMCLIName() + " -h\n");
		usageStringBuilder.append("       Display this help and exit.\n");
		usageStringBuilder.append("    3. " + IdmCliMain.getIDMCLIName() + " createUser -h\n");
		usageStringBuilder.append("       How to create a user.\n");
		usageStringBuilder
		        .append("    4. " + IdmCliMain.getIDMCLIName() + " createUser userName Bob password 123456\n");
		usageStringBuilder.append("       Create a user with name Bob and password 123456\n");
		usageStringBuilder.append("    5. " + IdmCliMain.getIDMCLIName() + " listUsers\n");
		usageStringBuilder.append("       List all users of this account.\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("Exit status: \n");
		usageStringBuilder.append("    0  if OK,\n");
		usageStringBuilder.append("    1  if minor problems (e.g., cannot access subdirectory),\n");
		usageStringBuilder.append("    2  if serious trouble (e.g., cannot access command-line argument).\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("Author: \n");
		usageStringBuilder.append("    Yuan Huan\n");
		usageStringBuilder.append("\n");
		usageStringBuilder.append("Bug report: \n");
		usageStringBuilder.append("    yuanhuan@tcl.com");

		return usageStringBuilder.toString();
	}

	/**
	 * 利用反射机制根据对象、方法名和参数调用方法
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object invokeMethod(Object owner, String methodName, Object[] args) throws NoSuchMethodException,
	        SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Class ownerClass = owner.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++)
		{
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(owner, args);
	}

	/**
	 * 安装IDM CLI
	 * @param idmCliConfigFilePath
	 * @return
	 */
	private static void installIdmCli(String idmCliConfigFilePath)
	{
		StringBuilder idmCliConfigContentBuilder = new StringBuilder();
		String str = "";
		InputStreamReader inputStreamReader = null;

		// 是否已经安装过，避免重复安装
		if (FileUtils.isFileExisted(idmCliConfigFilePath))
		{
			System.out.println("Error: IDM CLI has been already installed.");
			System.exit(CLIExitValue.MINOR_PROBLEM);
		}

		try
		{
			inputStreamReader = new InputStreamReader(System.in);
			System.out.print("Please input IDM end point(Sample: http://www.idm.com/idm): ");
			str = new BufferedReader(inputStreamReader).readLine();
			while (StringUtils.isEmpty(str))
			{
				System.out.print("Please input IDM end point(Sample: http://www.idm.com/idm): ");
				str = new BufferedReader(inputStreamReader).readLine();
			}
			idmCliConfigContentBuilder.append("IDMEndPoint=" + str + "\n");

			System.out.print("Please input your Access Key ID: ");
			str = new BufferedReader(inputStreamReader).readLine();
			while (StringUtils.isEmpty(str))
			{
				System.out.print("Please input your Access Key ID: ");
				str = new BufferedReader(inputStreamReader).readLine();
			}
			idmCliConfigContentBuilder.append("AccessKeyId=" + str + "\n");

			System.out.print("Please input your Secret Access Key: ");
			str = new BufferedReader(inputStreamReader).readLine();
			while (StringUtils.isEmpty(str))
			{
				System.out.print("Please input your Secret Access Key: ");
				str = new BufferedReader(inputStreamReader).readLine();
			}

			// 加密存储SecretAccessKey
			idmCliConfigContentBuilder.append("SecretAccessKey=" + AESUtils.encrypt(str) + "\n");

			System.out.print("Please input signature method [HmacSHA256]: ");
			str = new BufferedReader(inputStreamReader).readLine();
			if (StringUtils.isEmpty(str))
			{
				str = "HmacSHA256";
			}
			idmCliConfigContentBuilder.append("SignatureMethod=" + str + "\n");

			System.out.print("Please input signature version [1]: ");
			str = new BufferedReader(inputStreamReader).readLine();
			if (StringUtils.isEmpty(str))
			{
				str = "1";
			}
			idmCliConfigContentBuilder.append("SignatureVersion=" + str);

			// 写入文件
			boolean writeResult = false;
			writeResult = FileUtils.writeFile(idmCliConfigFilePath, idmCliConfigContentBuilder.toString());
			if (!writeResult)
			{
				System.out.println("Error: failed to write IDM CLI configration file " + idmCliConfigFilePath);
				System.exit(CLIExitValue.SERIOUS_PROBLEM);
			}

			// 验证用户配置信息
			System.out.println("\nPlease wait for verifying your configration...");
			String[] args = {};
			CustomHttpResponse verifyResult = AccessKeyService.listAccessKeys(args);
			if (HttpServletResponse.SC_OK == verifyResult.getHttpStatusCode())
			{
				System.out.println("Congratulations, your configrations passed the validation test.");
				System.out.println("IDM CLI has been installed successfully.");
				System.exit(CLIExitValue.SUCCESS);
			}
			else
			{
				FileUtils.deleteFile(idmCliConfigFilePath);
				System.out
				        .println("Sorry, your configrations didn't pass the validation test. Please try again after checking.");
				System.exit(CLIExitValue.SERIOUS_PROBLEM);
			}
		}
		catch (Exception e)
		{
			FileUtils.deleteFile(idmCliConfigFilePath);
			System.out.println(e.toString());
			System.exit(CLIExitValue.SERIOUS_PROBLEM);

		}
	}

	private static void uninstallIdmCli()
	{
		String str = "";
		InputStreamReader inputStreamReader = null;
		String idmCliConfigFilePath = IDMServiceUtils.getIdmCliConfigFilePath();

		try
		{
			System.out.print("Do you really want to uninstall IDM CLI? [y/n Default no]:");
			inputStreamReader = new InputStreamReader(System.in);
			str = new BufferedReader(inputStreamReader).readLine();
			if (StringUtils.isEmpty(str))
			{
				// 默认是no
				str = "n";
			}

			if ("y".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str))
			{
				// 卸载CLI
				if (!FileUtils.deleteFile(idmCliConfigFilePath))
				{
					System.out.println("Error: failed to uninstall IDM CLI.");
					System.exit(CLIExitValue.SERIOUS_PROBLEM);
				}
				else
				{
					System.out.println("IDM CLI had been uninstalled successfully.");
					System.exit(CLIExitValue.SUCCESS);
				}
			}
			else
			{
				System.out.println("Abort.");
				System.exit(CLIExitValue.SUCCESS);
			}
		}
		catch (Exception e)
		{
			System.out.println("Error: failed to install IDM CLI.");
			System.exit(CLIExitValue.SERIOUS_PROBLEM);
		}
	}

	private static void checkIdmCliConfigFile()
	{
		// 检查IDM CLI的配置文件存不存在，如果不存在则需要执行安装步骤来创建该文件
		String idmCliConfigFilePath = IDMServiceUtils.getIdmCliConfigFilePath();
		if (!FileUtils.isFileExisted(idmCliConfigFilePath))
		{
			System.out.println("Error: IDM CLI is not installed.");
			System.out.println("Would you like to install it now, or you can execute \"" + IdmCliMain.getIDMCLIName()
			        + " -i\" to install later.");

			try
			{
				System.out.print("Install now? [y/n Default yes]:");
				InputStreamReader inputStreamReader = new InputStreamReader(System.in);
				String str = new BufferedReader(inputStreamReader).readLine();
				if (StringUtils.isEmpty(str))
				{
					// 默认是yes
					str = "y";
				}
				if ("y".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str))
				{
					// 安装CLI
					IdmCliMain.installIdmCli(idmCliConfigFilePath);
				}
				else
				{
					System.out.println("Abort.");
					System.exit(CLIExitValue.SUCCESS);
				}
			}
			catch (Exception e)
			{
				System.out.println("Error: failed to install IDM CLI.");
				System.exit(CLIExitValue.SERIOUS_PROBLEM);
			}

			System.exit(CLIExitValue.SERIOUS_PROBLEM);
		}
	}

	/**
	 * 分发IDM接口请求
	 * 
	 * @param apiName
	 * @param apiParams
	 */
	private static void dispatchIdmApiRequest(String[] args)
	{
		String resultJsonString = "";
		int resultExitValue = CLIExitValue.SERIOUS_PROBLEM;
		String jsonFormatString = "    ";

		// 判断是否需要格式化Json结果
		boolean needToFormatResult = false;
		for (int i = 0; i < args.length; i++)
		{
			if ("-f".equals(args[i]) || "--format-json".equals(args[i]))
			{
				needToFormatResult = true;
				break;
			}
		}

		// 将格式化参数去掉，例如：{ "-f", "list", "list", "-h" } 去掉格式化参数之后就变成{ "list", "list", "-h" }
		String[] argsWithoutFormatFlag = args;
		if (needToFormatResult)
		{
			argsWithoutFormatFlag = new String[args.length - 1];
			for (int i = 0, j = 0; i < (args.length - 1) && j < args.length; i++, j++)
			{
				if ("-f".equals(args[j]) || "--format-json".equals(args[j]))
				{
					needToFormatResult = true;
					i--;
					continue;
				}

				argsWithoutFormatFlag[i] = args[j];
			}
		}

		// 构造接口参数数组
		String apiName = argsWithoutFormatFlag[0];
		int argsLen = argsWithoutFormatFlag.length;
		String[] apiParams = new String[argsLen - 1];
		for (int i = 1; i < argsLen; i++)
		{
			apiParams[i - 1] = argsWithoutFormatFlag[i];
		}

		// 分发接口请求
		Object[] apiArgs = { apiParams };
		try
		{
			CustomHttpResponse result = null;
			String userType = CommonService.getIdmCliConfValue("userType");
			if (UserType.ADMIN.equalsIgnoreCase(userType))
			{
				result = (CustomHttpResponse) IdmCliMain.invokeMethod(new IdmApiForAdminService(), apiName, apiArgs);
			}
			else
			{
				result = (CustomHttpResponse) IdmCliMain.invokeMethod(new IdmApiForAccountService(), apiName, apiArgs);
			}
			if (HttpServletResponse.SC_OK == result.getHttpStatusCode())
			{
				resultJsonString = result.getResponseMessage();
				resultExitValue = CLIExitValue.SUCCESS;
			}
			else
			{
				resultJsonString = IDMServiceUtils.getJsonMessage(result.getResponseMessage());
				resultExitValue = CLIExitValue.SERIOUS_PROBLEM;
			}
		}
		catch (NoSuchMethodException e)
		{
			resultJsonString = IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.NoSuchEntity.getCode(),
			        apiName + " not found in IDM APIs.");
			resultExitValue = CLIExitValue.SERIOUS_PROBLEM;
		}
		catch (Exception e)
		{
			resultJsonString = IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
			        e.toString());
			resultExitValue = CLIExitValue.SERIOUS_PROBLEM;
		}

		if (needToFormatResult)
		{
			System.out.println(JsonFormatUtils.formatJson(resultJsonString, jsonFormatString));
		}
		else
		{
			System.out.println(resultJsonString);
		}
		System.exit(resultExitValue);
	}

	/**
	 * 列出所有IDM的接口名
	 */
	private static void listAllIdmApiNames()
	{
		Method[] methods = null;
		String userType = CommonService.getIdmCliConfValue("userType");
		if (UserType.ADMIN.equalsIgnoreCase(userType))
		{
			methods = IdmApiForAdminService.class.getDeclaredMethods();
		}
		else if (UserType.ACCOUNT.equalsIgnoreCase(userType))
		{
			methods = IdmApiForAccountService.class.getDeclaredMethods();
		}
		else
		{
			System.out.println("Error: IDM CLI configuration is not correct.\n       Unknown user type: " + userType);
			System.exit(CLIExitValue.SUCCESS);
		}

		System.out.println("Supported IDM APIs:");
		for (int i = 0; i < methods.length; i++)
		{
			System.out.println("    " + methods[i].getName());
		}
		System.exit(CLIExitValue.SUCCESS);
	}

	/**
	 * 获取IDM CLI名字，避免后期可能修改名引起不一致
	 * @return
	 */
	public static String getIDMCLIName()
	{
		return "idm";
	}

	/**
	 * 主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// 参数至少有一个
		if (null == args || args.length == 0)
		{
			System.out.println("Error: missing parameters.");
			System.out.println(IdmCliMain.getIDMCLIUsageString());
			return;
		}

		if (args.length == 1 && ("-f".equals(args[0]) || "--format-json".equals(args[0])))
		{
			System.out.println(IdmCliMain.getIDMCLIUsageString());
			System.exit(CLIExitValue.SERIOUS_PROBLEM);
		}

		// 显示帮助信息
		if ("-h".equals(args[0]) || "--help".equals(args[0]))
		{
			System.out.println(IdmCliMain.getIDMCLIUsageString());
			System.exit(CLIExitValue.SUCCESS);
		}

		// 安装IDM CLI
		String idmCliConfigFilePath = IDMServiceUtils.getIdmCliConfigFilePath();
		if ("-i".equals(args[0]) || "--install".equals(args[0]))
		{
			// 安装CLI
			IdmCliMain.installIdmCli(idmCliConfigFilePath);
		}

		// 检查IDM CLI的配置文件存不存在，如果不存在则需要执行安装步骤来创建该文件
		IdmCliMain.checkIdmCliConfigFile();

		// 卸载IDM CLI
		if ("-u".equals(args[0]) || "--uninstall".equals(args[0]))
		{
			IdmCliMain.uninstallIdmCli();
		}

		// 显示所有接口列表
		if ("-l".equals(args[0]) || "--list-all-apis".equals(args[0]))
		{
			IdmCliMain.listAllIdmApiNames();
		}

		// 分发接口请求
		IdmCliMain.dispatchIdmApiRequest(args);

		System.out.println(IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.UnknownError.getCode(),
		        CustomErrorCode.UnknownError.getMessage()));
		System.exit(CLIExitValue.SERIOUS_PROBLEM);
	}
}
