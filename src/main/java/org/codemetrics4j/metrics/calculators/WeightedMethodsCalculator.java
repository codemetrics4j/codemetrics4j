package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.value.NumericValue;

public class WeightedMethodsCalculator implements Calculator<Type> {
    @Override
    public Set<Metric> calculate(Type method) {
        NumericValue total = method.getMethods().parallelStream()
                .map(m -> m.getMetric("VG").get().getValue())
                .reduce(NumericValue.ZERO, NumericValue::plus);

        return ImmutableSet.of(Metric.of("WMC", "Weighted methods per Class", total));
    }
}
