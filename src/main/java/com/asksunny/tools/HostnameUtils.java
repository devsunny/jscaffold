package com.asksunny.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostnameUtils {

	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			return "localwebserver";
		}
	}

}
