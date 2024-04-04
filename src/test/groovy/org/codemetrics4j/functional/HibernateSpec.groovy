package org.codemetrics4j.functional

import spock.lang.Specification
import spock.lang.Tag

@Tag("functional")
@Tag("external")
class HibernateSpec extends Specification {
	def "hibernate analysis works"() {
		given:
		def repositoryDirectory = new File("build/hibernate")
		if (repositoryDirectory.exists()) {
			repositoryDirectory.deleteDir()
		}
		GitRepository.clone("https://github.com/hibernate/hibernate-orm.git", "6.4", new File("build/hibernate"))

		def processBuilder = new ProcessBuilder()
		processBuilder.command(GradleHelper.gradleWrapperPath, "run", "--args", "build/hibernate/hibernate-core --regexp .*src.main.java.org.hibernate.*")
				.inheritIO()

		when:
		def process = processBuilder.start()
		def result = process.waitFor()

		then:
		result == 0
	}
}