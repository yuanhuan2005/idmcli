package com.tcl.idm.model;

/**
 *
 * 
 * @author yuanhuan
 * 2014年4月22日 下午1:32:45
 */
public class IDMCLIConfig
{
	/**
	 * IDM服务地址，例如：http://www.idm.com/ 或者 http://127.0.0.1:8080/idm/
	 */
	private String idmEndPoint;

	private String accessKeyId;

	private String secretAccessKey;

	private String signatureMethod;

	private String signatureVersion;

	public String getIdmEndPoint()
	{
		return idmEndPoint;
	}

	public void setIdmEndPoint(String idmEndPoint)
	{
		this.idmEndPoint = idmEndPoint;
	}

	public String getAccessKeyId()
	{
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId)
	{
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey()
	{
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey)
	{
		this.secretAccessKey = secretAccessKey;
	}

	public String getSignatureMethod()
	{
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod)
	{
		this.signatureMethod = signatureMethod;
	}

	public String getSignatureVersion()
	{
		return signatureVersion;
	}

	public void setSignatureVersion(String signatureVersion)
	{
		this.signatureVersion = signatureVersion;
	}

	@Override
	public String toString()
	{
		return "IDMCLIConfig [idmEndPoint=" + idmEndPoint + ", accessKeyId=" + accessKeyId + ", secretAccessKey="
		        + secretAccessKey + ", signatureMethod=" + signatureMethod + ", signatureVersion=" + signatureVersion
		        + "]";
	}

}
