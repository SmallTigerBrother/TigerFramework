package com.mn.tiger.request.sync.method;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 信任所有主机-对于任何证书都不做检查
 * 
 * 
 * @since 2014年1月9日
 */
public class TGTmArray implements X509TrustManager {
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}
}
