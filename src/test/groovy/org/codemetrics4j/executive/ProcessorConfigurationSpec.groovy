package org.codemetrics4j.executive

import org.codemetrics4j.metrics.MetricName
import spock.lang.Specification

class ProcessorConfigurationSpec extends Specification {

	def "Test validateGroup disables entire group if any metric is disabled"() {
		given: "A list of disabled metrics containing one metric from the group"
		def disabledMetrics = [MetricName.NF]

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "All metrics in the group are disabled"
		config.getDisabledMetrics().containsAll([
			MetricName.NF,
			MetricName.NSF,
			MetricName.NPF,
			MetricName.NM,
			MetricName.NSM,
			MetricName.NPM
		])
	}

	def "Test validateDependencies disables dependent group if required metrics are disabled"() {
		given: "A list of disabled metrics containing required metrics"
		def disabledMetrics = [MetricName.Ci]

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "The dependent group is disabled"
		config.getDisabledMetrics().containsAll([
			MetricName.ClTCi,
			MetricName.ClRCi,
			MetricName.PF
		])
	}

	def "Test no changes when all metrics are enabled"() {
		given: "An empty list of disabled metrics"
		def disabledMetrics = []

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "No metrics are disabled"
		config.getDisabledMetrics().isEmpty()
	}

	def "Test validateGroup does not disable unrelated groups"() {
		given: "A list of disabled metrics containing one metric from a group"
		def disabledMetrics = [MetricName.NF]

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "Other groups remain enabled"
		!config.getDisabledMetrics().contains(MetricName.Ca)
		!config.getDisabledMetrics().contains(MetricName.Fin)
		!config.getDisabledMetrics().contains(MetricName.NVAR)
		!config.getDisabledMetrics().contains(MetricName.PF)
	}

	def "Test validateDependencies only disables relevant dependent groups"() {
		given: "A list of disabled metrics containing required metrics for one dependent group"
		def disabledMetrics = [MetricName.NOL]

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "The correct dependent group is disabled"
		config.getDisabledMetrics().containsAll([
			MetricName.PkgTCi,
			MetricName.PkgRCi,
			MetricName.CCRC
		])
		!config.getDisabledMetrics().contains(MetricName.ClTCi)
		!config.getDisabledMetrics().contains(MetricName.ClRCi)
		!config.getDisabledMetrics().contains(MetricName.PF)
	}

	def "Test multiple dependencies and groups are disabled correctly"() {
		given: "A list of disabled metrics containing required metrics for multiple groups"
		def disabledMetrics = [MetricName.Ci, MetricName.Mo]

		when: "The configuration is created"
		def config = new ProcessorConfiguration(disabledMetrics)

		then: "The correct dependent groups are disabled"
		config.getDisabledMetrics().containsAll([
			MetricName.ClTCi,
			MetricName.ClRCi,
			MetricName.PF
		])
		config.getDisabledMetrics().containsAll([
			MetricName.PkgTCi,
			MetricName.PkgRCi,
			MetricName.CCRC
		])
	}
}