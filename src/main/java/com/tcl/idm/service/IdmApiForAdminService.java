package com.tcl.idm.service;

import com.tcl.idm.model.CustomHttpResponse;

/**
 * Admin��������Ա�û������ࡣAdmin�û����Բ������нӿ�
 * 
 * @author yuanhuan
 * 2014��4��22�� ����3:07:45
 */
public class IdmApiForAdminService extends IdmApiForAccountService
{
	public CustomHttpResponse createAccount(String[] args)
	{
		return AccountService.createAccount(args);
	}

	public CustomHttpResponse deleteAccount(String[] args)
	{
		return AccountService.deleteAccount(args);
	}
}
