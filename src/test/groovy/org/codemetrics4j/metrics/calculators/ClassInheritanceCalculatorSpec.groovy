package org.codemetrics4j.metrics.calculators

import static org.codemetrics4j.util.Matchers.containsMetric
import static org.codemetrics4j.util.TestUtil.projectFromSnippet
import static spock.util.matcher.HamcrestSupport.expect

import org.codemetrics4j.input.Type
import org.codemetrics4j.metrics.MetricName
import spock.lang.Specification

class ClassInheritanceCalculatorSpec extends Specification {

	def "correctly calculates number of children and parents"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {

        }

        interface I {

        }

        interface J extends I {

        }

        interface K extends J {

        }

        class ClassX extends A implements K {

        }

        class ClassY extends A {

        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>)[0]

		Type classX = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassX" }
		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassY" }
		Type classA = (aPackage.getTypes() as List<Type>).find { type -> type.name == "A" }
		Type classK = (aPackage.getTypes() as List<Type>).find { type -> type.name == "K" }

		when:
		def resultX = new ClassInheritanceCalculator().calculate(classX)
		def resultY = new ClassInheritanceCalculator().calculate(classY)
		def resultA = new ClassInheritanceCalculator().calculate(classA)
		def resultK = new ClassInheritanceCalculator().calculate(classK)

		then:
		expect resultX, containsMetric(MetricName.NOPa, 2)
		expect resultY, containsMetric(MetricName.NOPa, 1)
		expect resultA, containsMetric(MetricName.NOCh, 2)
		expect resultK, containsMetric(MetricName.NOCh, 1)
	}

	def "correctly calculates number of descendants"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {

        }

        interface I {

        }

        interface J extends I {

        }

        interface K extends J {

        }

        class ClassX extends A implements K {

        }

        class ClassY extends A {

        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>)[0]

		Type classA = (aPackage.getTypes() as List<Type>).find { type -> type.name == "A" }
		Type classK = (aPackage.getTypes() as List<Type>).find { type -> type.name == "K" }
		Type classI = (aPackage.getTypes() as List<Type>).find { type -> type.name == "I" }

		when:
		def resultA = new ClassInheritanceCalculator().calculate(classA)
		def resultK = new ClassInheritanceCalculator().calculate(classK)
		def resultI = new ClassInheritanceCalculator().calculate(classI)

		then:
		expect resultA, containsMetric(MetricName.NOD, 2)
		expect resultK, containsMetric(MetricName.NOD, 1)

		expect resultI, containsMetric(MetricName.NOD, 3)
	}

	def "correctly calculates number of ancestors"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class A {

        }

        interface I {

        }

        interface J extends I {

        }

        interface K extends J {

        }

        class ClassX extends A implements K {

        }

        class ClassY extends A {

        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>)[0]

		Type classX = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassX" }
		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassY" }

		when:
		def resultX = new ClassInheritanceCalculator().calculate(classX)
		def resultY = new ClassInheritanceCalculator().calculate(classY)

		then:

		expect resultX, containsMetric(MetricName.NOA, 4)
		expect resultY, containsMetric(MetricName.NOA, 1)
	}

	def "properly resolves class name conflicts"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        import org.whatever.stuff.far.away.*;

        class ClassY implements I2 {

        }
        ''', '''
        package org.whatever.stuff.far.away;
        
           
        interface I2 {
        
        }
        ''', '''
        package org.whatever.stuff.close;
        
        interface I1 {
        
        }
        
        interface I2 extends I1 {
        
        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>).find { type -> type.name == "org.whatever.stuff" }

		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassY" }

		when:
		def resultY = new ClassInheritanceCalculator().calculate(classY)

		then:

		expect resultY, containsMetric(MetricName.NOA, 1)
		expect resultY, containsMetric(MetricName.NOPa, 1)
	}

	def "completely ignores classes it doesn't know about"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class ClassY implements Serializable {

        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>).find { type -> type.name == "org.whatever.stuff" }

		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassY" }

		when:
		def resultY = new ClassInheritanceCalculator().calculate(classY)

		then:

		expect resultY, containsMetric(MetricName.NOA, 0)
		expect resultY, containsMetric(MetricName.NOPa, 0)
	}


	def "properly handles inner classes"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class Outer {

            public static class Inner {
                public void sayHello() {
                    System.out.println("hi!");
                }
            }     
        }        
        ''', '''
        package org.whatever.stuff2;
        
        import org.whatever.stuff.Outer;
           
        class ClassX extends Outer.Inner {
        
        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>).find { type -> type.name == "org.whatever.stuff2" }

		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassX" }

		when:
		def resultY = new ClassInheritanceCalculator().calculate(classY)

		then:

		expect resultY, containsMetric(MetricName.NOA, 1)
		expect resultY, containsMetric(MetricName.NOPa, 1)
	}

	def "properly handles inner classes even when statically imported"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        import org.whatever.stuff.far.away.*; //ignore the import

        class Outer {

            public static class Inner {
                public void sayHello() {
                    System.out.println("hi!");
                }
            }     
        }        
        ''', '''
        package org.whatever.stuff2;
        
        import static org.whatever.stuff.Outer.*;
           
        class ClassX extends Inner {
        
        }
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>).find { type -> type.name == "org.whatever.stuff2" }

		Type classY = (aPackage.getTypes() as List<Type>).find { type -> type.name == "ClassX" }

		when:
		def resultY = new ClassInheritanceCalculator().calculate(classY)

		then:

		expect resultY, containsMetric(MetricName.NOA, 1)
		expect resultY, containsMetric(MetricName.NOPa, 1)
	}
}
