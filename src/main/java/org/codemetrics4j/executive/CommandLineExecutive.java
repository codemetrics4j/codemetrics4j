package org.codemetrics4j.executive;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.codemetrics4j.input.FileScanner;
import org.codemetrics4j.input.Project;
import org.codemetrics4j.input.RegexpFilter;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class CommandLineExecutive {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineExecutive.class);

    public static void main(String[] args) throws ParseException {

        Options options = prepareOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("codemetrics4j <source directory>", options);
            System.exit(0);
        } else if (line.hasOption("version")) {
            String v = CommandLineExecutive.class.getPackage().getImplementationVersion();
            System.out.println("codemetrics4j version: " + v);
            System.exit(0);
        } else if (line.getArgs().length != 1) {
            System.out.println("No source directory provided.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("codemetrics4j <java file or directory>", options);
            System.exit(0);
        } else {
            String fileParam = line.getArgs()[0];
            File scanDir = new File(fileParam).getAbsoluteFile();
            FileScanner scanner = new FileScanner(scanDir);

            IOFileFilter fileFilter =
                    line.hasOption("excludetests") ? new ExcludeTestsFilter(scanDir) : FileFilterUtils.trueFileFilter();

            scanner.addFilter(fileFilter);

            if (line.hasOption("regexp")) {
                scanner.addFilter(new RegexpFilter(line.getOptionValue("regexp")));
            }

            long startTime = System.currentTimeMillis();

            Project scannerOutput = scanner.scan();

            List<MetricName> disabledMetrics = new ArrayList<>();
            for (MetricName metric : MetricName.values()) {
                if (line.hasOption("no-" + metric.toString().toLowerCase())) {
                    disabledMetrics.add(metric);
                }
            }

            ProcessorConfiguration processorConfiguration = new ProcessorConfiguration(disabledMetrics);
            ProcessorFactory.getProcessor(processorConfiguration).process(scannerOutput);

            long endTime = System.currentTimeMillis();

            try {
                Document outputDocument = new XMLOutputter().output(scannerOutput);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                DOMSource source = new DOMSource(outputDocument);

                StreamResult result;
                if (line.hasOption("output")) {

                    String outputLocation = line.getOptionValue("output");
                    File tempOutputFile = new File(outputLocation + ".tmp");
                    File finalOutputFile = new File(outputLocation).getAbsoluteFile();
                    if (finalOutputFile.getParentFile() != null) {
                        finalOutputFile.getParentFile().mkdirs();
                    }

                    result = new StreamResult(tempOutputFile);
                    transformer.transform(source, result);
                    tempOutputFile.renameTo(finalOutputFile);
                    System.out.println("Operation completed in " + ((endTime - startTime) / 1000)
                            + " seconds, output written to " + finalOutputFile);
                } else {
                    result = new StreamResult(System.out);
                    transformer.transform(source, result);
                }
            } catch (TransformerException e) {
                logger.error("", e);
            }
        }
    }

    private static Options prepareOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false, "print this message");
        Option version = new Option("v", "version", false, "print the version information and exit");
        Option excludetests = new Option("xt", "excludetests", false, "exclude test files from scanning");
        Option output = new Option("o", "output", true, "where to save output (default is print to STDOUT)");
        Option regexp = new Option(
                "r", "regexp", true, "include only files for which the absolute path matches this expression");

        options.addOption(help);
        options.addOption(version);
        options.addOption(excludetests);
        options.addOption(output);
        options.addOption(regexp);

        for (MetricName metric : MetricName.values()) {
            options.addOption(new Option(
                    null, "no-" + metric.toString().toLowerCase(), false, "disable " + metric.toString() + " metric"));
        }

        return options;
    }

    private static class ExcludeTestsFilter implements IOFileFilter {
        private static final Set<String> testSuffixes =
                ImmutableSet.of("Test", "Spec", "Tests", "Specs", "Suite", "TestCase");

        private static final Set<String> testDirectories =
                ImmutableSet.of("test", "tests", "examples", "example", "samples", "sample");

        private final IOFileFilter underlyingFilter;

        public ExcludeTestsFilter(File baseDir) {
            String baseDirPath = baseDir.getPath();
            IOFileFilter doesNotHaveTestSuffix = new NotFileFilter(FileFilterUtils.asFileFilter(path -> {
                for (String testSuffix : testSuffixes) {
                    if (path.getName().endsWith(testSuffix + ".java")) {
                        return true;
                    }
                }
                return false;
            }));

            IOFileFilter isNotInTestSubDirectory = new NotFileFilter(FileFilterUtils.asFileFilter(path -> {
                String pathName = path.getPath();
                String relativePath = StringUtils.removeStart(pathName, baseDirPath);
                for (String testDirectory : testDirectories) {
                    if (relativePath.contains(File.separator + testDirectory + File.separator)) {
                        return true;
                    }
                }
                return false;
            }));

            this.underlyingFilter = FileFilterUtils.and(doesNotHaveTestSuffix, isNotInTestSubDirectory);
        }

        @Override
        public boolean accept(File file) {
            return underlyingFilter.accept(file);
        }

        @Override
        public boolean accept(File dir, String name) {
            return underlyingFilter.accept(dir, name);
        }
    }
}
