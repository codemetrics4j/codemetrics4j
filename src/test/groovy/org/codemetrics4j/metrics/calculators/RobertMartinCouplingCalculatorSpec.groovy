package org.codemetrics4j.metrics.calculators

import static org.codemetrics4j.util.Matchers.containsMetric
import static org.codemetrics4j.util.Matchers.doesNotContainMetric
import static org.codemetrics4j.util.TestUtil.projectFromSnippet
import static spock.util.matcher.HamcrestSupport.expect

import org.codemetrics4j.metrics.MetricName
import org.codemetrics4j.metrics.value.NumericValue
import spock.lang.Specification

class RobertMartinCouplingCalculatorSpec extends Specification {

	def "calculate simple coupling metrics"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;

        public class A {
            public void wee() {
                System.out.println(D.utilMethod(10));
            }
        }
        ''','''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;
        
        public class B {
            public <T extends C> void blah(T stuff) {
                System.out.println(stuff);
            }
        }
        
        ''','''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;
        
        public interface I {
            
        }
        
        ''','''
        package org.whatever.stuff;
              
        public class CustomException extends Exception {
        
        }        
        ''','''
        package org.whatever.stuff2;
        
        import org.whatever.stuff.*;

        public class C {
            private A field;
        
            public A returnsA() {
                return new A();
            }
        }
        ''','''
        package org.whatever.stuff2;

        import org.whatever.stuff.*;
        
        abstract public class D {
       
            static {
                System.out.println("Some stuff "+new CustomException());
            } 
       
            public void usesB(B in) throws CustomException {
                System.out.println(in);
            }
            
            public static int utilMethod(int i) {
                return i*2;
            }
        }
        ''','''
        package org.whatever.stuff2;

        import org.whatever.stuff.*;
        
        public class E {
            public void usesD() {
                D d = new D();
                System.out.println(d);
            }
        }
        '''

		org.codemetrics4j.input.Package firstPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff"}
		org.codemetrics4j.input.Package secondPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff2"}


		when:
		def firstResult = new RobertMartinCouplingCalculator().calculate(firstPackage)
		def secondResult = new RobertMartinCouplingCalculator().calculate(secondPackage)

		then:
		expect firstResult, containsMetric(MetricName.Ce, 2)
		expect firstResult, containsMetric(MetricName.Ca, 2)

		expect secondResult, containsMetric(MetricName.Ce, 2)
		expect secondResult, containsMetric(MetricName.Ca, 2)

		expect firstResult, containsMetric(MetricName.I, 0.5)
		expect firstResult, containsMetric(MetricName.A, 0.25)
		expect firstResult, containsMetric(MetricName.DMS, 0.25)
		expect firstResult, containsMetric(MetricName.NOI, NumericValue.ONE)
	}


	def "calculate assumes contradictory names are used within the package"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;
        
        public class A {
            public void helloA() {
                System.out.println("Hello A");
            }
        }
        
        ''','''
        package org.whatever.stuff2;
        
        import org.whatever.stuff.*;

        public class A {
            public void otherHelloA() {
                System.out.println("Hello A 2");
            }
        }
        ''','''
        package org.whatever.stuff2;

        import org.whatever.stuff.*;
        
        public class B {
            public static int utilMethod(A a) {
                return 100;
            }
        }
        '''

		org.codemetrics4j.input.Package firstPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff"}
		org.codemetrics4j.input.Package secondPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff2"}


		when:
		def firstResult = new RobertMartinCouplingCalculator().calculate(firstPackage)
		def secondResult = new RobertMartinCouplingCalculator().calculate(secondPackage)

		then:
		expect firstResult, containsMetric(MetricName.Ce, NumericValue.ZERO)
		expect firstResult, containsMetric(MetricName.Ca, NumericValue.ZERO)

		expect secondResult, containsMetric(MetricName.Ce, NumericValue.ZERO)
		expect secondResult, containsMetric(MetricName.Ca, NumericValue.ZERO)

		expect firstResult, doesNotContainMetric(MetricName.I)
		expect secondResult, containsMetric(MetricName.Ca, NumericValue.ZERO)
		expect firstResult, doesNotContainMetric(MetricName.DMS)
		expect firstResult, containsMetric(MetricName.NOI, NumericValue.ZERO)
	}

	def "doesn't count non-public classes"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;
        
        public class A {
            public void helloA() {
                System.out.println("Hello A");
            }
        }
        
        class B {
            public void helloB() {
                System.out.println("Hello B");
            }
        }        
        ''','''
        package org.whatever.stuff2;
        
        import org.whatever.stuff.*;

        public class C {
            public void sayHello() {
                new B().helloB();
            }
        }
        '''

		org.codemetrics4j.input.Package firstPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff"}
		org.codemetrics4j.input.Package secondPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff2"}


		when:
		def firstResult = new RobertMartinCouplingCalculator().calculate(firstPackage)
		def secondResult = new RobertMartinCouplingCalculator().calculate(secondPackage)

		then:
		expect firstResult, containsMetric(MetricName.Ce, NumericValue.ZERO)
		expect firstResult, containsMetric(MetricName.Ca, NumericValue.ZERO)

		expect secondResult, containsMetric(MetricName.Ce, NumericValue.ZERO)
		expect secondResult, containsMetric(MetricName.Ca, NumericValue.ZERO)

		expect firstResult, doesNotContainMetric(MetricName.I)
		expect secondResult, containsMetric(MetricName.Ca, NumericValue.ZERO)
		expect firstResult, doesNotContainMetric(MetricName.DMS)
		expect firstResult, containsMetric(MetricName.NOI, NumericValue.ZERO)
	}

	def "able to see public static classes"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;
        
        import org.whatever.stuff2.*;
        
        public class A {
            public void helloA() {
                System.out.println("Hello A");
            }
            
            public static class Inner {
                public static void sayHello() {
                    System.out.println("hi");
                }
            }
        }            
        ''','''
        package org.whatever.stuff2;
        
        import org.whatever.stuff.*;

        public class B {
            public void sayHello() {
                A.Inner.sayHello();
            }
        }
        '''

		org.codemetrics4j.input.Package firstPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff"}
		org.codemetrics4j.input.Package secondPackage = (project.getPackages() as List<Package>).find{ p -> p.name=="org.whatever.stuff2"}


		when:
		def firstResult = new RobertMartinCouplingCalculator().calculate(firstPackage)
		def secondResult = new RobertMartinCouplingCalculator().calculate(secondPackage)

		then:
		expect firstResult, containsMetric(MetricName.Ce, NumericValue.ZERO)
		expect firstResult, containsMetric(MetricName.Ca, NumericValue.ONE)

		expect secondResult, containsMetric(MetricName.Ce, NumericValue.ONE)
		expect secondResult, containsMetric(MetricName.Ca, NumericValue.ZERO)
	}
}
