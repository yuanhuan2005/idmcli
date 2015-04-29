package com.tcl.idm.model;

/**
 * ������Կ
 * 
 * @author yuanhuan
 * 2014��3��27�� ����4:09:36
 */
public class AccessKey
{
	/**
	 * ������Կ
	 */
	private String accessKeyId;

	/**
	 * ���ܷ�����Կ
	 */
	private String secretAccessKey;

	/**
	 * ״̬��active/inactive
	 */
	private String status;

	/**
	 * ����ID
	 */
	private String userId;

	/**
	 * ����ʱ��
	 */
	private String createDate;

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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	@Override
	public String toString()
	{
		return "AccessKey [accessKeyId=" + accessKeyId + ", secretAccessKey=" + secretAccessKey + ", status=" + status
		        + ", userId=" + userId + ", createDate=" + createDate + "]";
	}

}
