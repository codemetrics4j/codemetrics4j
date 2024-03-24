package org.codemetrics4j.metrics.calculators

import static org.codemetrics4j.util.Matchers.containsMetric
import static org.codemetrics4j.util.TestUtil.projectFromSnippet
import static spock.util.matcher.HamcrestSupport.expect

import org.codemetrics4j.input.Method
import org.codemetrics4j.input.Type
import spock.lang.Specification

class McclureCalculatorSpec extends Specification {

	def "properly calculates mcclure complexity"() {

		given:
		def project = projectFromSnippet '''
        package org.whatever.stuff;

        class SomeClass {
        
            public void doWork(int stop) {
                double rand = Math.random();
                int i = 0;
                boolean shouldRun = rand < 0.5;
                if(shouldRun) {
                    do {
                        i++;                   
                    } while(i < stop);   
                } else if(rand < 0.9) {
                    System.out.println("rare!");
                }   
                
                
                int q = 0;
                while(q < stop) {
                    System.out.println(factorial(q++));
                }         
            }
            
            public static int factorial(int n) {
                int result = 1;
                for(int i = 2; i <= n; i++)
                    result *= i;
                return result;
            }

        }      
        '''

		org.codemetrics4j.input.Package aPackage = (project.getPackages() as List<Package>)[0]

		Type classA = project.locateType("SomeClass");

		Method doWork = classA.lookupMethodBySignature("doWork(int)").get()
		Method factorial = classA.lookupMethodBySignature("factorial(int)").get()

		when:
		def resultDoWork = new McclureCalculator().calculate(doWork);
		def resultFactorial = new McclureCalculator().calculate(factorial);

		then:

		expect resultDoWork, containsMetric("NCOMP", 4)
		expect resultFactorial, containsMetric("NCOMP", 1)

		expect resultDoWork, containsMetric("NVAR", 5)
		expect resultFactorial, containsMetric("NVAR", 2)

		expect resultDoWork, containsMetric("MCLC", 9)
		expect resultFactorial, containsMetric("MCLC", 3)
	}
}
