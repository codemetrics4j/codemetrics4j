package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.codemetrics4j.input.Method;
import org.codemetrics4j.input.Package;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.metrics.value.NumericValue;
import org.codemetrics4j.metrics.value.NumericValueSummaryStatistics;

public class PackageAggregatorCalculator implements Calculator<Package> {
    @Override
    public Set<Metric> calculate(Package aPackage) {

        Stream<Method> allMethods = aPackage.getTypes().stream().flatMap(t -> t.getMethods().stream());
        NumericValueSummaryStatistics stats =
                methodMetrics(allMethods, MetricName.Ci).collect(NumericValue.summarizingCollector());

        Stream<Type> allTypes = aPackage.getTypes().stream();
        NumericValueSummaryStatistics numberOfLinksSummary =
                typeMetrics(allTypes, MetricName.NOL).collect(NumericValue.summarizingCollector());

        NumericValue classCategoricalRelationalCohesion = NumericValue.of(100).times(numberOfLinksSummary.getAverage());

        return ImmutableSet.of(
                Metric.of(MetricName.PkgTCi, "Package Total System Complexity", stats.getSum()),
                Metric.of(MetricName.PkgRCi, "Package Relative System Complexity", stats.getAverage()),
                Metric.of(
                        MetricName.CCRC, "Class Categorical Relational Cohesion", classCategoricalRelationalCohesion));
    }

    private Stream<NumericValue> typeMetrics(Stream<Type> types, MetricName metricName) {
        return types.flatMap(type -> {
            Optional<Metric> metric = type.getMetric(metricName);
            return metric.<Stream<? extends NumericValue>>map(value -> Stream.of(value.getValue()))
                    .orElseGet(Stream::empty);
        });
    }

    private Stream<NumericValue> methodMetrics(Stream<Method> methods, MetricName metricName) {
        return methods.flatMap(type -> {
            Optional<Metric> metric = type.getMetric(metricName);
            return metric.<Stream<? extends NumericValue>>map(value -> Stream.of(value.getValue()))
                    .orElseGet(Stream::empty);
        });
    }
}
