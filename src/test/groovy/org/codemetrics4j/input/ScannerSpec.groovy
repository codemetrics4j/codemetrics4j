package org.codemetrics4j.input

import static org.codemetrics4j.util.TestUtil.projectFromResources
import static org.codemetrics4j.util.TestUtil.projectFromSnippet

import spock.lang.Specification

class ScannerSpec extends Specification {

	def "gracefully handles unparseable files"() {
		given:
		def project = projectFromResources("org/codemetrics4j/unparseable")

		when:
		Type stuffType = project.locateType("Stuff")
		Type fineType = project.locateType("Fine")

		then:
		stuffType == null
		fineType != null
	}
}
