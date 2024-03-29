package org.codemetrics4j.input;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.*;
import org.codemetrics4j.metrics.Metric;

public abstract class Code {
    private final String name;
    protected final Set<Code> children;
    private Code parent = null;
    private final Map<String, Metric> metrics;
    private final Map<String, String> attributes;

    protected Code(String name) {
        this.name = name;
        this.children = new HashSet<>();
        this.metrics = new HashMap<>();
        this.attributes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Set<Metric> getMetrics() {
        return ImmutableSet.copyOf(metrics.values());
    }

    public Optional<Metric> getMetric(String name) {
        return Optional.ofNullable(this.metrics.get(name));
    }

    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return Objects.equal(getName(), code.getName()) && Objects.equal(getParent(), code.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getParent());
    }

    // * * * Package-level, only the scanner can add metrics and attributes, not calculators

    synchronized void addMetric(Metric metric) {
        metrics.put(metric.getName(), metric);
    }

    synchronized void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    void addAttribute(Map.Entry<String, String> attribute) {
        addAttribute(attribute.getKey(), attribute.getValue());
    }

    void addMetrics(Set<Metric> metrics) {
        for (Metric metric : metrics) {
            this.addMetric(metric);
        }
    }

    // * * * Protected, only subclasses should be able to directly add/access parent and children, other callers should
    // use addMethod, addPackage, etc

    protected Set<Code> getChildren() {
        return children;
    }

    protected Code getParent() {
        return parent;
    }

    protected void addChild(Code child) {
        child.parent = this;
        this.children.add(child);
    }
}
