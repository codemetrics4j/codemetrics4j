package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import java.util.Set;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.value.NumericValue;

public class CouplingFactorCalculator implements Calculator<Type> {
    @Override
    public Set<Metric> calculate(Type type) {
        Graph<Type> unchecked =
                type.getParentPackage().getParentProject().getMetadata().getClientGraph();

        Set<Type> clientRelationships = unchecked.successors(type);
        Set<Type> serverRelationships = unchecked.predecessors(type);

        NumericValue totalClasses = NumericValue.of(type.getParentPackage().getParentProject().getPackages().stream()
                .mapToLong(p -> p.getTypes().size())
                .sum());
        NumericValue totalPossibleRelationships = totalClasses
                .minus(NumericValue.ONE)
                .times(NumericValue.of(2)); // Can have a server or client relationship with every class other than self

        ImmutableSet.Builder<Metric> metricBuilder = ImmutableSet.<Metric>builder()
                .add(Metric.of("NODa", "Number of Dependants", NumericValue.of(serverRelationships.size())))
                .add(Metric.of("NODe", "Number of Dependencies", NumericValue.of(clientRelationships.size())));

        if (totalPossibleRelationships.isGreaterThan(NumericValue.ZERO)) {
            metricBuilder.add(Metric.of(
                    "CF",
                    "Coupling Factor",
                    NumericValue.of(clientRelationships.size() + serverRelationships.size())
                            .divide(totalPossibleRelationships)));
        }

        return metricBuilder.build();
    }
}
