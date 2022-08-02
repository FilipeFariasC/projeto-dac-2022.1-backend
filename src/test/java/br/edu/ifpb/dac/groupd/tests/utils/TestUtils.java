package br.edu.ifpb.dac.groupd.tests.utils;

public class TestUtils {
	public static final boolean os(String system) {
		String os = System.getProperty("os.name").toUpperCase();
		
		return os.startsWith(system.toUpperCase());
	}
}
