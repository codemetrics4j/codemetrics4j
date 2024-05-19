package org.codemetrics4j.metrics.calculators

import static org.codemetrics4j.util.Matchers.containsMetric
import static org.codemetrics4j.util.TestUtil.packageFromSnippet
import static spock.util.matcher.HamcrestSupport.expect

import org.codemetrics4j.metrics.MetricName
import spock.lang.Specification

class NumberOfClassesCalculatorSpec extends Specification {
	def "calculate simple metric"() {

		given:
		def aPackage = packageFromSnippet '''
        package org.whatever.stuff;

        public class Doubler {
            public int doDouble(int x) {
                return x*2;
            }
        }

        class Tripler {
            public int doTriple(int x) {
                return x*3;
            }
        }
        '''

		when:
		def result = new NumberOfClassesCalculator().calculate(aPackage)

		then:
		expect result, containsMetric(MetricName.NOC, 2)
	}

	def "calculate number of classes for empty package"() {

		given:
		def aPackage = packageFromSnippet '''
        package org.whatever.stuff;
        '''

		when:
		def result = new NumberOfClassesCalculator().calculate(aPackage)

		then:
		expect result, containsMetric(MetricName.NOC, 0)
	}

	def "counts interfaces when counting classes"() {

		given:
		def aPackage = packageFromSnippet '''
        package org.whatever.stuff;

        interface Doubler {
            int doDouble(int x);
        }

        abstract class AbstractDoubler implements Doubler {
            abstract int getFactor();

            public int doDouble(int x) {
                return x * getFactor();
            }
        }

        class MyDoubler extends AbstractDoubler {
            public int getFactor() {
                return 2;
            }
        }
        '''

		when:
		def result = new NumberOfClassesCalculator().calculate(aPackage)

		then:
		expect result, containsMetric(MetricName.NOC, 3)
	}

	//TODO: does not count inner or anonymous classes
}
