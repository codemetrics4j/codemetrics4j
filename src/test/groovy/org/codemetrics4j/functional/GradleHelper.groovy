package org.codemetrics4j.functional

class GradleHelper {
	static String getGradleWrapperPath() {
		return System.getProperty("os.name").toLowerCase().contains("win") ? "./gradlew.bat" : "./gradlew"
	}
}
