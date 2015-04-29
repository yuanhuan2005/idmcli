package com.tcl.idm.service;

import com.tcl.idm.model.CustomHttpResponse;

/**
 * Account账户的操作类。Account账户可以操作除了createAccount/deleteAccount之外的所有接口
 * 
 * @author yuanhuan
 * 2014年4月22日 下午3:07:45
 */
public class IdmApiForAccountService
{
	public CustomHttpResponse createUser(String[] args)
	{
		return UserService.createUser(args);
	}

	public CustomHttpResponse getUser(String[] args)
	{
		return UserService.getUser(args);
	}

	public CustomHttpResponse listUsers(String[] args)
	{
		return UserService.listUsers(args);
	}

	public CustomHttpResponse deleteUser(String[] args)
	{
		return UserService.deleteUser(args);
	}

	public CustomHttpResponse updateUser(String[] args)
	{
		return UserService.updateUser(args);
	}

	public CustomHttpResponse changePassword(String[] args)
	{
		return UserService.changePassword(args);
	}

	public CustomHttpResponse createGroup(String[] args)
	{
		return GroupService.createGroup(args);
	}

	public CustomHttpResponse getGroup(String[] args)
	{
		return GroupService.getGroup(args);
	}

	public CustomHttpResponse listGroups(String[] args)
	{
		return GroupService.listGroups(args);
	}

	public CustomHttpResponse deleteGroup(String[] args)
	{
		return GroupService.deleteGroup(args);
	}

	public CustomHttpResponse updateGroup(String[] args)
	{
		return GroupService.updateGroup(args);
	}

	public CustomHttpResponse addUserToGroup(String[] args)
	{
		return GroupService.addUserToGroup(args);
	}

	public CustomHttpResponse removeUserFromGroup(String[] args)
	{
		return GroupService.removeUserFromGroup(args);
	}

	public CustomHttpResponse createAccessKey(String[] args)
	{
		return AccessKeyService.createAccessKey(args);
	}

	public CustomHttpResponse deleteAccessKey(String[] args)
	{
		return AccessKeyService.deleteAccessKey(args);
	}

	public CustomHttpResponse updateAccessKey(String[] args)
	{
		return AccessKeyService.updateAccessKey(args);
	}

	public CustomHttpResponse listAccessKeys(String[] args)
	{
		return AccessKeyService.listAccessKeys(args);
	}

	public CustomHttpResponse createUserPolicy(String[] args)
	{
		return UserPolicyService.createUserPolicy(args);
	}

	public CustomHttpResponse getUserPolicy(String[] args)
	{
		return UserPolicyService.getUserPolicy(args);
	}

	public CustomHttpResponse updateUserPolicy(String[] args)
	{
		return UserPolicyService.updateUserPolicy(args);
	}

	public CustomHttpResponse listUserPolicys(String[] args)
	{
		return UserPolicyService.listUserPolicys(args);
	}

	public CustomHttpResponse deleteUserPolicy(String[] args)
	{
		return UserPolicyService.deleteUserPolicy(args);
	}

	public CustomHttpResponse createGroupPolicy(String[] args)
	{
		return GroupPolicyService.createGroupPolicy(args);
	}

	public CustomHttpResponse getGroupPolicy(String[] args)
	{
		return GroupPolicyService.getGroupPolicy(args);
	}

	public CustomHttpResponse updateGroupPolicy(String[] args)
	{
		return GroupPolicyService.updateGroupPolicy(args);
	}

	public CustomHttpResponse listGroupPolicys(String[] args)
	{
		return GroupPolicyService.listGroupPolicys(args);
	}

	public CustomHttpResponse deleteGroupPolicy(String[] args)
	{
		return GroupPolicyService.deleteGroupPolicy(args);
	}
}
