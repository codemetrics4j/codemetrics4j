package org.codemetrics4j.metrics.calculators;

import com.github.javaparser.ast.expr.Expression;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.Network;
import java.util.Set;
import java.util.function.Function;
import org.codemetrics4j.input.Method;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.util.Distinct;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.KatzCentrality;
import org.jgrapht.graph.guava.ImmutableNetworkAdapter;

@SuppressWarnings("UnstableApiUsage")
public class KatzCentralityCalculator implements Calculator<Method> {
    private final Function<Network<Method, Distinct<Expression>>, KatzCentrality<Method, Distinct<Expression>>>
            cachingKatzCentralityCalculator;

    public KatzCentralityCalculator() {
        cachingKatzCentralityCalculator = new Function<>() {
            private KatzCentrality<Method, Distinct<Expression>> katzCentrality;

            @Override
            public KatzCentrality<Method, Distinct<Expression>> apply(
                    Network<Method, Distinct<Expression>> methodCalls) {
                synchronized (this) {
                    if (katzCentrality == null) {
                        Graph<Method, Distinct<Expression>> graph =
                                new ImmutableNetworkAdapter<>(ImmutableNetwork.copyOf(methodCalls));
                        katzCentrality = new KatzCentrality<>(graph);
                    }
                }
                return katzCentrality;
            }
        };
    }

    @Override
    public Set<Metric> calculate(Method method) {
        KatzCentrality<Method, Distinct<Expression>> katzCentrality =
                cachingKatzCentralityCalculator.apply(method.getParentType()
                        .getParentPackage()
                        .getParentProject()
                        .getMetadata()
                        .getCallNetwork());

        double centralityScore = katzCentrality.getVertexScore(method);
        return ImmutableSet.of(Metric.of("KATZ", "Katz Centrality", centralityScore));
    }
}
