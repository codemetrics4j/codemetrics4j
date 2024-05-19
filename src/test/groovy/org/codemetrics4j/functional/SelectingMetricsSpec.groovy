package org.codemetrics4j.functional

import spock.lang.Specification
import spock.lang.Tag

@Tag("functional")
class SelectingMetricsSpec extends Specification {
	def "selecting metrics works"() {
		given:
		def processBuilder = new ProcessBuilder()
		processBuilder.command(GradleHelper.gradleWrapperPath, "run", "--args", ". --regexp .*src.test.resources.org.codemetrics4j.* --no-katz")
				.redirectInput(ProcessBuilder.Redirect.INHERIT)
				.redirectError(ProcessBuilder.Redirect.INHERIT)
				.redirectOutput(ProcessBuilder.Redirect.PIPE)

		when:
		def process = processBuilder.start()
		def outputStream = new StringBuilder()
		def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
		String line
		while ((line = reader.readLine()) != null) {
			outputStream.append(line).append("\n")
		}
		def result = process.waitFor()
		def output = outputStream.toString()

		then:
		result == 0
		!output.contains("KATZ")
	}
}
