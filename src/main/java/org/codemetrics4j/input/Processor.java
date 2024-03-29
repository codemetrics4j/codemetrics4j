package org.codemetrics4j.input;

import java.util.HashSet;
import java.util.Set;
import org.codemetrics4j.metrics.Calculator;
import org.codemetrics4j.metrics.Metric;
import org.codemetrics4j.util.ProjectMetadata;

// TODO: try to figure out a way to move this to org.codemetrics4j.metrics - it needs to access a package-level method
// on Code
// which prevents this
public class Processor {
    private final Set<Calculator<Project>> projectCalculators;
    private final Set<Calculator<Package>> packageCalculators;
    private final Set<Calculator<Type>> typeCalculators;
    private final Set<Calculator<Method>> methodCalculators;

    public Processor() {
        projectCalculators = new HashSet<>();
        packageCalculators = new HashSet<>();
        typeCalculators = new HashSet<>();
        methodCalculators = new HashSet<>();
    }

    public void registerProjectCalculator(Calculator<Project> calculator) {
        projectCalculators.add(calculator);
    }

    public void registerPackageCalculator(Calculator<Package> calculator) {
        packageCalculators.add(calculator);
    }

    public void registerTypeCalculator(Calculator<Type> calculator) {
        typeCalculators.add(calculator);
    }

    public void registerMethodCalculator(Calculator<Method> calculator) {
        methodCalculators.add(calculator);
    }

    public void process(Project project) {

        ProjectMetadata metadata = new ProjectMetadata(project);

        project.getPackages().parallelStream().forEach(aPackage -> {
            aPackage.getTypes().parallelStream().forEach(type -> {
                type.getMethods().parallelStream()
                        .forEach(method -> methodCalculators.parallelStream().forEach(methodMetricCalculator -> {
                            Set<Metric> methodMetrics = methodMetricCalculator.calculate(method);
                            method.addMetrics(methodMetrics);
                        }));

                typeCalculators.parallelStream().forEach(typeMetricCalculator -> {
                    Set<Metric> classMetrics = typeMetricCalculator.calculate(type);
                    type.addMetrics(classMetrics);
                });
            });

            packageCalculators.parallelStream().forEach(packageMetricCalculator -> {
                Set<Metric> packageMetrics = packageMetricCalculator.calculate(aPackage);
                aPackage.addMetrics(packageMetrics);
            });
        });

        projectCalculators.parallelStream().forEach(projectMetricCalculator -> {
            Set<Metric> projectMetrics = projectMetricCalculator.calculate(project);
            project.addMetrics(projectMetrics);
        });
    }
}
