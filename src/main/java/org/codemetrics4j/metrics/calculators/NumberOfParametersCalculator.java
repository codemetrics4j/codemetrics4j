package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.codemetrics4j.input.Method;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;

/**
 * Simply counts the number of parameters on a method
 *
 * @author Rod Hilton
 * @since 0.3
 */
public class NumberOfParametersCalculator implements Calculator<Method> {
    @Override
    public Set<Metric> calculate(Method method) {
        return ImmutableSet.of(Metric.of(
                "NOP",
                "Number of Parameters",
                method.getSource().getParameters().size()));
    }
}
