package br.edu.ifpb.dac.groupd.tests.utils;

public class TestUtils {
	
	private static final String FRONTEND_PREFIX = "http://localhost:3000";
	
	public static final boolean os(String system) {
		String os = System.getProperty("os.name").toUpperCase();
		
		return os.startsWith(system.toUpperCase());
	}
	
	public static String buildFrontendUrl(String endpoint) {
		return String.format("%s/%s", FRONTEND_PREFIX, endpoint);
	}
}
