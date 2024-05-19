package org.codemetrics4j.metrics.calculators;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.VoidType;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.Network;
import java.util.Set;
import org.codemetrics4j.input.Method;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.metrics.value.NumericValue;
import org.codemetrics4j.util.Distinct;

public class FanCalculator implements Calculator<Method> {

    @Override
    public synchronized Set<Metric> calculate(Method method) {

        Network<Method, Distinct<Expression>> methodCalls = method.getParentType()
                .getParentPackage()
                .getParentProject()
                .getMetadata()
                .getCallNetwork();

        Set<Method> methodsCalled = methodCalls.successors(method);

        int fanOut = 0;

        for (Method methodCalled : methodsCalled) {
            Set<Distinct<Expression>> calls = methodCalls.edgesConnecting(method, methodCalled);
            fanOut += calls.size();
        }

        Set<Method> methodsCalling = methodCalls.predecessors(method);

        int fanIn = 0;

        for (Method methodCalling : methodsCalling) {
            Set<Distinct<Expression>> calls = methodCalls.edgesConnecting(methodCalling, method);
            fanIn += calls.size();
        }

        int returns = method.getSource().getType() instanceof VoidType ? 0 : 1;
        int parameters = method.getSource().getParameters().size();
        int iovars = parameters + returns;

        NumericValue dataComplexity = NumericValue.of(iovars).divide(NumericValue.ONE.plus(NumericValue.of(fanOut)));
        NumericValue structuralComplexity = NumericValue.of(fanOut).pow(2);
        NumericValue systemComplexity = dataComplexity.plus(structuralComplexity.divide(NumericValue.ONE));

        return ImmutableSet.of(
                Metric.of(MetricName.Fout, "Fan-out", fanOut),
                Metric.of(MetricName.Fin, "Fan-in", fanIn),
                Metric.of(MetricName.Si, "Structural Complexity", structuralComplexity),
                Metric.of(MetricName.IOVars, "Input/Output Variables", iovars),
                Metric.of(MetricName.Di, "Data Complexity", dataComplexity),
                Metric.of(MetricName.Ci, "System Complexity", systemComplexity));
    }
}
