package org.codemetrics4j.output;

import org.codemetrics4j.input.Project;

public interface Outputter<T> {
    T output(Project project);
}
