package org.codemetrics4j.output;

import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.codemetrics4j.input.*;
import org.codemetrics4j.input.Package;
import org.codemetrics4j.metrics.Metric;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLOutputter implements Outputter<Document> {

    @Override
    public Document output(Project project) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element projectElement = doc.createElement("Project");
            doc.appendChild(projectElement);

            addAttributes(project, projectElement);
            addMetricsForNode(doc, projectElement, project);

            Element packagesElement = doc.createElement("Packages");
            projectElement.appendChild(packagesElement);

            for (Package packageNode : sortChildren(project.getPackages())) {
                Element packageElement = doc.createElement("Package");
                packageElement.setAttribute("name", packageNode.getName());
                packagesElement.appendChild(packageElement);

                addAttributes(packageNode, packageElement);
                addMetricsForNode(doc, packageElement, packageNode);

                Element classesElement = doc.createElement("Classes");
                packageElement.appendChild(classesElement);

                for (Type classNode : sortChildren(packageNode.getTypes())) {
                    Element classElement = doc.createElement("Class");
                    classElement.setAttribute("name", classNode.getName());
                    classesElement.appendChild(classElement);

                    addAttributes(classNode, classElement);
                    addMetricsForNode(doc, classElement, classNode);

                    Element methodsElement = doc.createElement("Methods");
                    classElement.appendChild(methodsElement);

                    for (Method methodNode : sortChildren(classNode.getMethods())) {
                        Element methodElement = doc.createElement("Method");
                        methodElement.setAttribute("name", methodNode.getName());
                        methodsElement.appendChild(methodElement);

                        addAttributes(methodNode, methodElement);
                        addMetricsForNode(doc, methodElement, methodNode);
                    }
                }
            }

            return doc;

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Code> List<T> sortChildren(Collection<T> children) {
        return children.stream().sorted(Comparator.comparing(Code::getName)).collect(Collectors.toList());
    }

    private void addAttributes(Code classNode, Element classElement) {
        for (Map.Entry<String, String> attribute : classNode.getAttributes().entrySet()) {
            classElement.setAttribute(attribute.getKey(), attribute.getValue());
        }
    }

    private void addMetricsForNode(Document doc, Node parentElement, Code node) {
        Element metricsContainer = doc.createElement("Metrics");

        Set<Metric> metrics = node.getMetrics();
        List<Metric> sortedMetrics =
                metrics.stream().sorted(Comparator.comparing(Metric::getName)).collect(Collectors.toList());
        for (Metric metric : sortedMetrics) {
            Element metricsElement = doc.createElement("Metric");

            metricsElement.setAttribute("name", metric.getName());
            metricsElement.setAttribute("description", metric.getDescription());
            metricsElement.setAttribute("value", metric.getFormattedValue());

            metricsContainer.appendChild(metricsElement);
        }
        parentElement.appendChild(metricsContainer);
    }
}
