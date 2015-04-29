package com.tcl.idm.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��21�� ����3:44:29
 */
public class PolicyResourceUtils
{
	Map<String, String[]> validResourceMap = new HashMap<String, String[]>();

	private static class SingletonHolder
	{
		private static final PolicyResourceUtils INSTANCE = new PolicyResourceUtils();
	}

	private PolicyResourceUtils()
	{
	}

	public static final PolicyResourceUtils getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	public Map<String, String[]> getValidResourceMap()
	{
		// add service: transcoding
		String transcodingKey = "transcode";
		String[] transcodingValue = { "transcoder", "logo", "snapshots", "clipper", "insertion" };
		validResourceMap.put(transcodingKey, transcodingValue);

		// add service: cameratake
		String cameratakeKey = "cameratake";
		String[] cameratakeValue = { "cameratake" };
		validResourceMap.put(cameratakeKey, cameratakeValue);

		return validResourceMap;
	}

}
