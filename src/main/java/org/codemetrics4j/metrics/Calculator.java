package org.codemetrics4j.metrics;

import java.util.Set;
import org.codemetrics4j.input.Code;

public interface Calculator<T extends Code> {

    Set<Metric> calculate(T t);
}
