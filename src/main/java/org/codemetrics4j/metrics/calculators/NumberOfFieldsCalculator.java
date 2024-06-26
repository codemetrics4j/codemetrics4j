package org.codemetrics4j.metrics.calculators;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.codemetrics4j.input.Type;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.util.CalculationUtils;

/**
 * Counts the number of fields and methods in a class.
 *
 * <ul>
 *     <li>NF - Number of Attributes (fields)</li>
 *     <li>NSF - Number of Static Attributes (fields)</li>
 *     <li>NPF - Number of Public Attributes (fields)</li>
 *     <li>NM - Number of Methods</li>
 *     <li>NSM - Number of Static Methods</li>
 *     <li>NPM - Number of Public Methods</li>
 * </ul>
 *
 * @author Rod Hilton
 * @since 0.2
 */
public class NumberOfFieldsCalculator implements Calculator<Type> {
    @Override
    public Set<Metric> calculate(Type type) {
        ClassOrInterfaceDeclaration declaration = type.getSource();

        long numAttributes = declaration.getFields().size();
        long numStaticAttributes = declaration.getFields().stream()
                .filter(f -> CalculationUtils.getModifierKeywords(f).contains(Modifier.Keyword.STATIC))
                .count();
        long numPublicAttributes = declaration.getFields().stream()
                .filter(f -> CalculationUtils.getModifierKeywords(f).contains(Modifier.Keyword.PUBLIC))
                .count();

        long numMethods = declaration.getMethods().size();
        long numStaticMethods = declaration.getMethods().stream()
                .filter(f -> CalculationUtils.getModifierKeywords(f).contains(Modifier.Keyword.STATIC))
                .count();
        long numPublicMethods = declaration.getMethods().stream()
                .filter(f -> CalculationUtils.getModifierKeywords(f).contains(Modifier.Keyword.PUBLIC))
                .count();

        return ImmutableSet.<Metric>builder()
                .add(Metric.of(MetricName.NF, "Number of Attributes", numAttributes))
                .add(Metric.of(MetricName.NSF, "Number of Static Attributes", numStaticAttributes))
                .add(Metric.of(MetricName.NPF, "Number of Public Attributes", numPublicAttributes))
                .add(Metric.of(MetricName.NM, "Number of Methods", numMethods))
                .add(Metric.of(MetricName.NSM, "Number of Static Methods", numStaticMethods))
                .add(Metric.of(MetricName.NPM, "Number of Public Methods", numPublicMethods))
                .build();
    }
}
