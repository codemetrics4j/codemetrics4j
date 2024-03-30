package org.codemetrics4j.input;

import java.io.File;
import java.util.regex.Pattern;
import org.apache.commons.io.filefilter.IOFileFilter;

public class RegexpFilter implements IOFileFilter {
    private final Pattern pattern;

    public RegexpFilter(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    @Override
    public boolean accept(File file) {
        return pattern.matcher(file.getAbsolutePath()).matches();
    }

    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(new File(dir, name).getAbsolutePath()).matches();
    }
}
