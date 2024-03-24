package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.codemetrics4j.input.Package;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;

/**
 * Simply counts the number of classes in a package.  Only counts top-level classes, not inner or anonymous classes.
 *
 * @author Rod Hilton
 * @since 0.3
 */
public class NumberOfClassesCalculator implements Calculator<Package> {

    @Override
    public Set<Metric> calculate(Package aPackage) {
        return ImmutableSet.of(
                Metric.of("NOC", "Number of Classes", aPackage.getTypes().size()));
    }
}
