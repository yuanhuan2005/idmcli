package com.tcl.idm.service;

import com.tcl.idm.model.CustomHttpResponse;

/**
 * Admin超级管理员用户操作类。Admin用户可以操作所有接口
 * 
 * @author yuanhuan
 * 2014年4月22日 下午3:07:45
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
