package com.movie.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class SystemParamsUtils {
	private final static String filePath = "systemParamsConfig.properties";
	private static Map configMap = new HashMap();
	private static Properties prop = new Properties();

	public static void initSysconfig() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream in = null;
		in = cl.getResourceAsStream(filePath);
		try {
			prop.load(in);

			Iterator it = prop.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();

				configMap.put(key, prop.get(key));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map getSysConfig() {
		initSysconfig();
		return configMap;
	}

}
