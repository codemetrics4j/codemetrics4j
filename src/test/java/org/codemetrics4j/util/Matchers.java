package org.codemetrics4j.util;

import java.util.Optional;
import java.util.Set;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.metrics.value.NumericValue;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Matchers {

    public static Matcher<Set<Metric>> containsMetric(MetricName name, NumericValue value) {
        return new BaseMatcher<Set<Metric>>() {
            @Override
            @SuppressWarnings("unchecked")
            public boolean matches(final Object item) {
                final Set<Metric> metrics = (Set<Metric>) item;
                if (metrics == null || metrics.isEmpty()) return false;
                Optional<Metric> namedMetric =
                        metrics.stream().filter((m) -> m.getName().equals(name)).findFirst();
                return namedMetric.isPresent()
                        && Math.abs(value.doubleValue()
                                        - namedMetric.get().getValue().doubleValue())
                                < 0.000000000001;
            }

            @Override
            public void describeTo(final Description description) {
                description
                        .appendText("expected metrics to contain")
                        .appendValue(name.toString())
                        .appendValue(value);
            }
        };
    }

    public static Matcher<Set<Metric>> containsMetric(MetricName name, double value) {
        return containsMetric(name, NumericValue.of(value));
    }

    public static Matcher<Set<Metric>> containsMetric(MetricName name, long value) {
        return containsMetric(name, NumericValue.of(value));
    }

    public static Matcher<Set<Metric>> doesNotContainMetric(MetricName name) {
        return new BaseMatcher<Set<Metric>>() {
            @Override
            @SuppressWarnings("unchecked")
            public boolean matches(final Object item) {
                final Set<Metric> metrics = (Set<Metric>) item;
                Optional<Metric> namedMetric =
                        metrics.stream().filter((m) -> m.getName().equals(name)).findFirst();
                return !namedMetric.isPresent();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("expected metrics to not contain").appendValue(name.toString());
            }
        };
    }
}
