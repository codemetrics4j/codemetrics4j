package org.codemetrics4j.metrics.calculators;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Graph;
import java.util.Set;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;

public class LinkCalculator implements Calculator<Type> {

    @Override
    public Set<Metric> calculate(Type type) {

        Graph<Type> uses =
                type.getParentPackage().getParentProject().getMetadata().getClientGraph();

        Set<Type> links = uses.successors(type);

        return ImmutableSet.of(Metric.of("NOL", "Number of Links", links.size()));
    }
}
