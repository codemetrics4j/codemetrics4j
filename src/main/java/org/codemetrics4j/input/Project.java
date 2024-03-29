package org.codemetrics4j.input;

import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.codemetrics4j.util.ProjectMetadata;

public class Project extends Code {

    private JavaSymbolSolver symbolSolver;
    private final Map<String, Package> packageLookup;
    private final ProjectMetadata metadata;

    public Project(String name) {
        super(name);
        packageLookup = new HashMap<>();
        metadata = new ProjectMetadata(this);
    }

    @SuppressWarnings("unchecked")
    public Set<Package> getPackages() {
        return (Set<Package>) (Set<?>) getChildren();
    }

    public ProjectMetadata getMetadata() {
        return metadata;
    }

    public void addPackage(Package aPackage) {
        packageLookup.put(aPackage.getName(), aPackage);
        addChild(aPackage);
    }

    @Override
    public String toString() {
        return "Project(" + this.getName() + ")";
    }

    // Normally equals is just, a matching name and parent
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setSymbolSolver(JavaSymbolSolver symbolSolver) {
        this.symbolSolver = symbolSolver;
    }

    public JavaSymbolSolver getSymbolSolver() {
        return symbolSolver;
    }

    public Optional<Package> lookupPackageByName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            packageName = "default";
        }

        if (packageLookup.containsKey(packageName)) {
            return Optional.of(packageLookup.get(packageName));
        } else {
            return Optional.empty();
        }
    }
}
