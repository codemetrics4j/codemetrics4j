package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.codemetrics4j.input.Method;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.metrics.value.NumericValue;
import org.codemetrics4j.metrics.value.NumericValueSummaryStatistics;

public class TypeAggregatorCalculator implements Calculator<Type> {
    @Override
    public Set<Metric> calculate(Type type) {

        NumericValueSummaryStatistics ciStats =
                methodMetrics(type.getMethods(), MetricName.Ci).collect(NumericValue.summarizingCollector());

        ImmutableSet.Builder<Metric> metricBuilder = ImmutableSet.builder();

        if (ciStats.getCount().isGreaterThan(NumericValue.ZERO)) {

            metricBuilder
                    .add(Metric.of(MetricName.ClTCi, "Class Total System Complexity", ciStats.getSum()))
                    .add(Metric.of(MetricName.ClRCi, "Class Relative System Complexity", ciStats.getAverage()));
        }

        Optional<Metric> nodOpt = type.getMetric(MetricName.NOD);
        Optional<Metric> moOpt = type.getMetric(MetricName.Mo);
        Optional<Metric> mdOpt = type.getMetric(MetricName.Md);

        // NOD * Md
        NumericValue polyFactorDenom = mdOpt.map(Metric::getValue)
                .flatMap(md -> nodOpt.map(Metric::getValue).map(nod -> nod.times(md)))
                .orElse(NumericValue.ZERO);

        if (polyFactorDenom.isGreaterThan(NumericValue.ZERO)) {
            // Mo / (Md * NOD)
            moOpt.ifPresent(mo -> metricBuilder.add(Metric.of(
                    MetricName.PF, "Polymorphism Factor", mo.getValue().divide(polyFactorDenom))));
        }

        return metricBuilder.build();
    }

    private Stream<NumericValue> methodMetrics(Set<Method> methods, MetricName metricName) {
        return methods.stream().flatMap(method -> {
            Optional<Metric> metric = method.getMetric(metricName);
            return metric.<Stream<? extends NumericValue>>map(value -> Stream.of(value.getValue()))
                    .orElseGet(Stream::empty);
        });
    }
}
