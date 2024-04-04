package org.codemetrics4j.functional

import spock.lang.Specification
import spock.lang.Tag

@Tag("functional")
@Tag("external")
class JUnitSpec extends Specification {
	def "junit analysis works"() {
		given:
		def repositoryDirectory = new File("build/junit5")
		if (repositoryDirectory.exists()) {
			repositoryDirectory.deleteDir()
		}
		GitRepository.clone("https://github.com/junit-team/junit5.git", "releases/5.9.x", new File("build/junit5"))

		def processBuilder = new ProcessBuilder()
		processBuilder.command(GradleHelper.gradleWrapperPath, "run", "--args", "build/junit5/junit-platform-engine --regexp .*src.main.java.org.junit.*")
				.inheritIO()

		when:
		def process = processBuilder.start()
		def result = process.waitFor()

		then:
		result == 0
	}
}
