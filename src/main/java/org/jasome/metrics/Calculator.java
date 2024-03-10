package org.jasome.metrics;

import java.util.Set;
import org.jasome.input.Code;

public interface Calculator<T extends Code> {

    Set<Metric> calculate(T t);
}
