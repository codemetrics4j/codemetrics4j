package org.jasome.executive;

import org.jasome.output.Output;

public interface Outputter<T> {
    public T output(Output output);
}
