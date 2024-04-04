package org.codemetrics4j.functional

import spock.lang.Specification
import spock.lang.Tag

@Tag("functional")
class SelfAnalysisSpec extends Specification {
	def "self analysis works"() {
		given:
		def processBuilder = new ProcessBuilder()
		processBuilder.command(GradleHelper.gradleWrapperPath, "run", "--args", ". --regexp .*src.main.java.org.codemetrics4j.*")
				.inheritIO()

		when:
		def process = processBuilder.start()
		def result = process.waitFor()

		then:
		result == 0
	}
}
