package com.tcl.idm.service;


public class SignatureService
{
	public static String genSignatureString(String httpMethod, String host, String uri, String params)
	{
		StringBuffer signatureStringBuffer = new StringBuffer();

		signatureStringBuffer.append(httpMethod);
		signatureStringBuffer.append("\n");
		signatureStringBuffer.append(host);
		signatureStringBuffer.append("\n");
		signatureStringBuffer.append(uri);
		signatureStringBuffer.append("\n");
		signatureStringBuffer.append(params);

		return signatureStringBuffer.toString();
	}
}
