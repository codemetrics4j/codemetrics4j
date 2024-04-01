package org.codemetrics4j.functional;

import spock.lang.Specification
import spock.lang.Tag

@Tag("functional")
@Tag("external")
class SpringSpec extends Specification {
	def "spring analysis works"() {
		given:
		def repositoryDirectory = new File("build/spring")
		if (repositoryDirectory.exists()) {
			repositoryDirectory.deleteDir()
		}
		GitRepository.clone("https://github.com/spring-projects/spring-framework.git", "6.1.x", new File("build/spring"))

		def processBuilder = new ProcessBuilder()
		processBuilder.command(GradleHelper.gradleWrapperPath, "run", "--args", "build/spring/spring-core --regexp .*src.main.java.org.springframework.*")
				.inheritIO()

		when:
		def process = processBuilder.start()
		def result = process.waitFor()

		then:
		result == 0
	}
}