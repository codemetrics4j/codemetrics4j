package org.codemetrics4j.metrics.calculators

import static org.codemetrics4j.util.Matchers.containsMetric
import static org.codemetrics4j.util.Matchers.doesNotContainMetric
import static org.codemetrics4j.util.TestUtil.projectFromSnippet
import static spock.util.matcher.HamcrestSupport.expect

import org.codemetrics4j.input.Type
import org.codemetrics4j.metrics.MetricName
import org.codemetrics4j.metrics.value.NumericValue
import spock.lang.Specification

class MethodAndAttributeInheritanceCalculatorTest extends Specification {

	def "calculates inheritance factor"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {
            public int doStuff() { return 5; }
            protected void moreStuff() { }
            public void forFree() { }
        }
        
        interface I {
            int getThing();   
        }
        
        class B implements I {
            public void doBStuff() {}
            public int doStuff() { return 4; }
            public int getThing() { return 666; }
        }
        
        class C extends B implements Q {
            //Does not override doStuff()
            //gets forFree free
            //has moreStuff but it's not public
            //inherits getQ but does not override it
        }
        
        class D extends C implements Q {
            public int doStuff() { return 3; }
            public int getQ() { return 123; }
            //Has the same stuff as C but does override doStuff
            public D getAnotherD() { return new D(); }
        }
        
        interface Q {
            default int getQ() {
                return 5;
            }
        }
        '''

		Type typeA = project.locateType("A")
		Type typeB = project.locateType("B")
		Type typeC = project.locateType("C")
		Type typeD = project.locateType("D")
		Type interfaceI = project.locateType("I")
		Type interfaceQ = project.locateType("Q")

		when:
		def resultA = new MethodAndAttributeInheritanceCalculator().calculate(typeA)
		def resultB = new MethodAndAttributeInheritanceCalculator().calculate(typeB)
		def resultC = new MethodAndAttributeInheritanceCalculator().calculate(typeC)
		def resultD = new MethodAndAttributeInheritanceCalculator().calculate(typeD)
		def resultI = new MethodAndAttributeInheritanceCalculator().calculate(interfaceI)
		def resultQ = new MethodAndAttributeInheritanceCalculator().calculate(interfaceQ)

		then:
		expect resultA, containsMetric(MetricName.Mit, 0)
		expect resultB, containsMetric(MetricName.Mit, 0)
		expect resultC, containsMetric(MetricName.Mit, 4)
		expect resultD, containsMetric(MetricName.Mit, 4)
		expect resultI, containsMetric(MetricName.Mit, 0)
		expect resultQ, containsMetric(MetricName.Mit, 0)

		expect resultA, containsMetric(MetricName.Mi, 0)
		expect resultB, containsMetric(MetricName.Mi, 0) //"Inheriting" a method from an interface doesn't count unless the impl is defined on the interface
		expect resultC, containsMetric(MetricName.Mi, 4)
		expect resultD, containsMetric(MetricName.Mi, 2)
		expect resultI, containsMetric(MetricName.Mi, 0)
		expect resultQ, containsMetric(MetricName.Mi, 0)

		expect resultA, containsMetric(MetricName.Md, 3)
		expect resultB, containsMetric(MetricName.Md, 3)
		expect resultC, containsMetric(MetricName.Md, 0)
		expect resultD, containsMetric(MetricName.Md, 3)
		expect resultI, containsMetric(MetricName.Md, 0)
		expect resultQ, containsMetric(MetricName.Md, 1)

		expect resultA, containsMetric(MetricName.Mo, 0)
		expect resultB, containsMetric(MetricName.Mo, 0)
		expect resultC, containsMetric(MetricName.Mo, 0)
		expect resultD, containsMetric(MetricName.Mo, 2)
		expect resultI, containsMetric(MetricName.Mo, 0)
		expect resultQ, containsMetric(MetricName.Mo, 0)

		expect resultA, containsMetric(MetricName.Ma, 3)
		expect resultB, containsMetric(MetricName.Ma, 3)
		expect resultC, containsMetric(MetricName.Ma, 4)
		expect resultD, containsMetric(MetricName.Ma, 5)
		expect resultI, containsMetric(MetricName.Ma, 0)
		expect resultQ, containsMetric(MetricName.Ma, 1)

		expect resultA, containsMetric(MetricName.MIF, 0)
		expect resultB, containsMetric(MetricName.MIF, 0)
		expect resultC, containsMetric(MetricName.MIF, 1)
		expect resultD, containsMetric(MetricName.MIF, NumericValue.of(2).divide(NumericValue.of(5)))
		expect resultI, doesNotContainMetric(MetricName.MIF)
		expect resultQ, containsMetric(MetricName.MIF, 0)

		expect resultA, doesNotContainMetric(MetricName.NMIR)
		expect resultB, doesNotContainMetric(MetricName.NMIR)
		expect resultC, containsMetric(MetricName.NMIR, 100)
		expect resultD, containsMetric(MetricName.NMIR, NumericValue.ofRational(1,2).times(NumericValue.of(100)))
		expect resultI, doesNotContainMetric(MetricName.NMIR)
		expect resultQ, doesNotContainMetric(MetricName.NMIR)

		expect resultA, doesNotContainMetric(MetricName.PF)
		expect resultB, doesNotContainMetric(MetricName.PF)
		expect resultC, doesNotContainMetric(MetricName.PF)
		expect resultD, doesNotContainMetric(MetricName.PF)
		expect resultI, doesNotContainMetric(MetricName.PF)
		expect resultQ, doesNotContainMetric(MetricName.PF)
	}

	def "calculates method hiding factor and public methods ratio"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {
            public int doStuff() { return 5; }
            protected void moreStuff() { }
            public void forFree() { }
        }
        
        interface I {
            int getThing();   
        }
        
        class B implements I {
            public void doBStuff() {}
            public int doStuff() { return 4; }
            public int getThing() { return 666; }
            private void invisible() {}
        }
        
        class C extends B implements Q {
            //Does not override doStuff()
            //gets forFree free
            //has moreStuff but it's not public
            //inherits getQ but does not override it
            void packageScope() { }
        }
        
        class D extends C implements Q {
            public int doStuff() { return 3; }
            public int getQ() { return 123; }
            //Has the same stuff as C but does override doStuff
            public D getAnotherD() { return new D(); }
        }
        
        interface Q {
            default int getQ() {
                return 5;
            }
        }
        '''

		Type typeA = project.locateType("A")
		Type typeB = project.locateType("B")
		Type typeC = project.locateType("C")
		Type typeD = project.locateType("D")
		Type interfaceI = project.locateType("I")
		Type interfaceQ = project.locateType("Q")

		when:
		def resultA = new MethodAndAttributeInheritanceCalculator().calculate(typeA)
		def resultB = new MethodAndAttributeInheritanceCalculator().calculate(typeB)
		def resultC = new MethodAndAttributeInheritanceCalculator().calculate(typeC)
		def resultD = new MethodAndAttributeInheritanceCalculator().calculate(typeD)
		def resultI = new MethodAndAttributeInheritanceCalculator().calculate(interfaceI)
		def resultQ = new MethodAndAttributeInheritanceCalculator().calculate(interfaceQ)

		then:
		expect resultA, containsMetric(MetricName.PMd, 2)
		expect resultB, containsMetric(MetricName.PMd, 3)
		expect resultC, containsMetric(MetricName.PMd, 0)
		expect resultD, containsMetric(MetricName.PMd, 3)
		expect resultI, containsMetric(MetricName.PMd, 0) //The method isn't "defined" on the interface, only its contract
		expect resultQ, containsMetric(MetricName.PMd, 1)

		expect resultA, containsMetric(MetricName.PMi, 0)
		expect resultB, containsMetric(MetricName.PMi, 0)
		expect resultC, containsMetric(MetricName.PMi, 4)
		expect resultD, containsMetric(MetricName.PMi, 2)
		expect resultI, containsMetric(MetricName.PMi, 0)
		expect resultQ, containsMetric(MetricName.PMi, 0)

		expect resultA, containsMetric(MetricName.PMR, NumericValue.ofRational(2, 3))
		expect resultB, containsMetric(MetricName.PMR, NumericValue.ofRational(3, 4))
		expect resultC, containsMetric(MetricName.PMR, NumericValue.ofRational(4, 5))
		expect resultD, containsMetric(MetricName.PMR, NumericValue.ofRational(5, 6))
		expect resultI, doesNotContainMetric(MetricName.PMR)
		expect resultQ, containsMetric(MetricName.PMR, NumericValue.of(1))

		expect resultA, containsMetric(MetricName.HMd, 1)
		expect resultB, containsMetric(MetricName.HMd, 1)
		expect resultC, containsMetric(MetricName.HMd, 1)
		expect resultD, containsMetric(MetricName.HMd, 0)
		expect resultI, containsMetric(MetricName.HMd, 0)
		expect resultQ, containsMetric(MetricName.HMd, 0)

		expect resultA, containsMetric(MetricName.HMi, 0)
		expect resultB, containsMetric(MetricName.HMi, 0)
		expect resultC, containsMetric(MetricName.HMi, 0)
		expect resultD, containsMetric(MetricName.HMi, 0)
		expect resultI, containsMetric(MetricName.HMi, 0)
		expect resultQ, containsMetric(MetricName.HMi, 0)

		expect resultA, containsMetric(MetricName.MHF, NumericValue.ofRational(2,3))
		expect resultB, containsMetric(MetricName.MHF, NumericValue.ofRational(3,4))
		expect resultC, containsMetric(MetricName.MHF, 0)
		expect resultD, containsMetric(MetricName.MHF, 1)
		expect resultI, doesNotContainMetric(MetricName.MHF)
		expect resultQ, containsMetric(MetricName.MHF, 1)
	}

	def "calculates attribute factors"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {
            public int doStuff = 5;
            protected String moreStuff = "more stuff";
            public String forFree = "for free";
        }
        
        interface I {
            int getThing = 15;   
        }
        
        class B implements I {
            public String doBStuff = "doBStuff";
            public int doStuff = 4;
            public int getThing = 666;
        }
        
        class C extends B {
            //Does not override doStuff
            //gets forFree free
            //has moreStuff but it's not public
        }
        
        class D extends C {
            public int doStuff = 19;
            //Has the same stuff as C but does override doStuff
        }
        
        interface Q {
            int q=5;
        }
        '''

		Type typeA = project.locateType("A")
		Type typeB = project.locateType("B")
		Type typeC = project.locateType("C")
		Type typeD = project.locateType("D")
		Type interfaceI = project.locateType("I")
		Type interfaceQ = project.locateType("Q")

		when:
		def resultA = new MethodAndAttributeInheritanceCalculator().calculate(typeA)
		def resultB = new MethodAndAttributeInheritanceCalculator().calculate(typeB)
		def resultC = new MethodAndAttributeInheritanceCalculator().calculate(typeC)
		def resultD = new MethodAndAttributeInheritanceCalculator().calculate(typeD)
		def resultI = new MethodAndAttributeInheritanceCalculator().calculate(interfaceI)
		def resultQ = new MethodAndAttributeInheritanceCalculator().calculate(interfaceQ)

		then:
		expect resultA, containsMetric(MetricName.Ait, 0)
		expect resultB, containsMetric(MetricName.Ait, 1)
		expect resultC, containsMetric(MetricName.Ait, 3)
		expect resultD, containsMetric(MetricName.Ait, 3)
		expect resultI, containsMetric(MetricName.Ait, 0)
		expect resultQ, containsMetric(MetricName.Ait, 0)

		expect resultA, containsMetric(MetricName.Ai, 0)
		expect resultB, containsMetric(MetricName.Ai, 0)
		expect resultC, containsMetric(MetricName.Ai, 3)
		expect resultD, containsMetric(MetricName.Ai, 2)
		expect resultI, containsMetric(MetricName.Ai, 0)
		expect resultQ, containsMetric(MetricName.Ai, 0)

		expect resultA, containsMetric(MetricName.Ad, 3)
		expect resultB, containsMetric(MetricName.Ad, 3)
		expect resultC, containsMetric(MetricName.Ad, 0)
		expect resultD, containsMetric(MetricName.Ad, 1)
		expect resultI, containsMetric(MetricName.Ad, 1)
		expect resultQ, containsMetric(MetricName.Ad, 1)

		expect resultA, containsMetric(MetricName.Ao, 0)
		expect resultB, containsMetric(MetricName.Ao, 1)
		expect resultC, containsMetric(MetricName.Ao, 0)
		expect resultD, containsMetric(MetricName.Ao, 1)
		expect resultI, containsMetric(MetricName.Ao, 0)
		expect resultQ, containsMetric(MetricName.Ao, 0)

		expect resultA, containsMetric(MetricName.Aa, 3)
		expect resultB, containsMetric(MetricName.Aa, 3)
		expect resultC, containsMetric(MetricName.Aa, 3)
		expect resultD, containsMetric(MetricName.Aa, 3)
		expect resultI, containsMetric(MetricName.Aa, 1)
		expect resultQ, containsMetric(MetricName.Aa, 1)

		expect resultA, containsMetric(MetricName.AIF, 0)
		expect resultB, containsMetric(MetricName.AIF, 0)
		expect resultC, containsMetric(MetricName.AIF, 1)
		expect resultD, containsMetric(MetricName.AIF, NumericValue.ofRational(2, 3))
		expect resultI, containsMetric(MetricName.AIF, 0)
		expect resultQ, containsMetric(MetricName.AIF, 0)

		expect resultA, containsMetric(MetricName.Av, 2)
		expect resultB, containsMetric(MetricName.Av, 3)
		expect resultC, containsMetric(MetricName.Av, 0)
		expect resultD, containsMetric(MetricName.Av, 1)
		expect resultI, containsMetric(MetricName.Av, 1)
		expect resultQ, containsMetric(MetricName.Av, 1)

		expect resultA, containsMetric(MetricName.AHF, NumericValue.ofRational(2, 3))
		expect resultB, containsMetric(MetricName.AHF, 1)
		expect resultC, doesNotContainMetric(MetricName.AHF)
		expect resultD, containsMetric(MetricName.AHF, 1)
		expect resultI, containsMetric(MetricName.AHF, 1)
		expect resultQ, containsMetric(MetricName.AHF, 1)
	}
}
