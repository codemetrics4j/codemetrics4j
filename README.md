# codemetrics4j
[![Build Status](https://github.com/kdunee/codemetrics4j/actions/workflows/gradle.yml/badge.svg)](https://github.com/kdunee/codemetrics4j/actions/workflows/gradle.yml)
[![GitHub issues](https://img.shields.io/github/issues/kdunee/codemetrics4j.svg)](https://github.com/kdunee/codemetrics4j/issues)
[![Latest release](https://img.shields.io/github/release/kdunee/codemetrics4j.svg?colorB=dfb430)](https://github.com/kdunee/codemetrics4j/releases/latest)
[![Github All Releases](https://img.shields.io/github/downloads/kdunee/codemetrics4j/total.svg?colorB=0083c3)](https://github.com/kdunee/codemetrics4j/releases)

**codemetrics4j** (formerly JaSoMe) is a Java source code analyzer that calculates insightful quality metrics without requiring project compilation.  This means you can analyze code even when dependencies are missing or code generation steps are incomplete.

Key Features:

- Compilation-free analysis: Get metrics directly from your .java files, streamlining the analysis process.
- File-level metrics: Provides granular metrics for classes and methods within each file.
- Package-level insights: Receive aggregated metrics at the package level.
- XML Output: Results are delivered in a structured XML format for easy integration.

> [!NOTE]  
> This project was originally forked from https://github.com/rodhilton/jasome.  Due to the original repository being unmaintained, I've published my fork under the name `codemetrics4j`. This ensures easy access to the version containing the latest features and bug fixes.  The previous version can still be found at https://github.com/kdunee/jasome. 

# Getting Started

Download the latest distribution and unzip, change into directory, then run:

  ```
  bin/codemetrics4j <directory to analyze>
  ```
  
codemetrics4j will gather metrics and output them to the console.  You can save the XML
to a file using the `--output <file>` option.

# Java compatibility

This project requires Java 17 or later to function correctly. Please ensure you have a compatible Java Runtime Environment (JRE) or Java Development Kit (JDK) installed.

# Metrics

codemetrics4j is currently tracking the following metrics:

| Metric                                           | Description                                  | Method                  | Class                   | Package                 | Project                 |
|--------------------------------------------------|----------------------------------------------|-------------------------|-------------------------|-------------------------|-------------------------|
| **Raw Total Lines of Code (RTLOC)**              | Actual lines of code, including comments     |                         | :ballot_box_with_check: |                         |                         |
| **Total Lines of Code (TLOC)**                   | Lines of code, excluding comments/whitespace | :ballot_box_with_check: | :ballot_box_with_check: | :ballot_box_with_check: | :ballot_box_with_check: |
| **Number of Attributes (NF)**                    | Count of fields/attributes                   |                         | :ballot_box_with_check: |                         |                         |
| **Number of Static Attributes (NSF)**            | Count of static attributes                   |                         | :ballot_box_with_check: |                         |                         |
| **Number of Public Attributes (NPF)**            | Count of public attributes                   |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods (NM)**                       | Count of methods                             |                         | :ballot_box_with_check: |                         |                         |
| **Number of Static Methods (NSM)**               | Count of static methods                      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Public Methods (NPM)**               | Count of public methods                      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Classes (NOC)**                      | Count of classes in a package                |                         |                         | :ballot_box_with_check: |                         |
| **Number of Parameters (NOP)**                   | Count of method parameters                   | :ballot_box_with_check: |                         |                         |                         |
| **Depth of Inheritance Tree (DIT)**              | Maximum depth of a class's inheritance       |                         | :ballot_box_with_check: |                         |                         |
| **Number of Overridden Methods (NORM)**          | Methods a class overrides/implements         |                         | :ballot_box_with_check: |                         |                         |
| **Number of Inherited Methods (NMI)**            | Methods inherited from parent classes        |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Added to Inheritance (NMA)** | New methods defined in the class             |                         | :ballot_box_with_check: |                         |                         |
| **Specialization Index (SIX)**                   | Measure of class specialization              |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Inherited Total (Mit)**      | Total methods inherited                      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Inherited (Mi)**             | Methods inherited but not overridden         |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Defined (Md)**               | Methods defined within class                 |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Overidden (Mo)**             | Methods overriding inherited ones            |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods (All) (Ma)**                 | All methods accessible on a class            |                         | :ballot_box_with_check: |                         |                         |
| **Method Inheritance Factor (MIF)**              | Ratio of inherited vs. total methods         |                         | :ballot_box_with_check: |                         |                         |
| **Number of Public Methods Defined (PMd)**       | Count of public defined methods              |                         | :ballot_box_with_check: |                         |                         |
| **Number of Public Methods Inherited (PMi)**     | Count of public inherited methods            |                         | :ballot_box_with_check: |                         |                         |
| **Public Methods Ratio (PMR)**                   | Ratio of public methods                      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Hidden Methods Defined (HMd)**       | Count of non-public defined methods          |                         | :ballot_box_with_check: |                         |                         |
| **Number of Hidden Methods Inherited (HMi)**     | Count of non-public inherited methods        |                         | :ballot_box_with_check: |                         |                         |
| **Method Hiding Factor (MHF)**                   | Ratio of hidden vs. defined methods          |                         | :ballot_box_with_check: |                         |                         |
| **Number of Methods Inherited Ratio (NMIR)**     | Ratio of inherited methods from total        |                         | :ballot_box_with_check: |                         |                         |
| **Coupling Factor (CF)**                         | Measure of class's external dependencies     |                         | :ballot_box_with_check: |                         |                         |
| **Polymorphism Factor (PF)**                     | Measure of method overriding                 |                         | :ballot_box_with_check: |                         |                         |
| **Number of Attributes Inherited Total (Ait)**   | Total attributes inherited                   |                         | :ballot_box_with_check: |                         |                         |
| **Number of Attributes Inherited (Ai)**          | Attributes inherited but not overridden      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Attributes Defined (Ad)**            | Attributes defined in class                  |                         | :ballot_box_with_check: |                         |                         |
| **Number of Attributes Overidden (Ao)**          | Attributes overriding inherited ones         |                         | :ballot_box_with_check: |                         |                         |
| **Number of Attributes (All) (Aa)**              | All attributes accessible on a class         |                         | :ballot_box_with_check: |                         |                         |
| **Attribute Inheritance Factor (AIF)**           | Ratio of inherited vs. total attributes      |                         | :ballot_box_with_check: |                         |                         |
| **Number of Public Attributes Defined (Av)**     | Count of public defined attributes           |                         | :ballot_box_with_check: |                         |                         |
| **Number of Hidden Attributes Defined (Ah)**     | Count of non-public defined attributes       |                         | :ballot_box_with_check: |                         |                         |
| **Method Hiding Factor (AHF)**                   | Ratio of hidden vs. defined attributes       |                         | :ballot_box_with_check: |                         |                         |
| **McCabe Cyclomatic Complexity (VG)**            | Number of unique paths through code          | :ballot_box_with_check: |                         |                         |                         |
| **Weighted Methods per Class (WMC)**             | Sum of method complexities                   |                         | :ballot_box_with_check: |                         |                         |
| **Lack of Cohesion Methods (LCOM)**              | Measure of class cohesiveness                |                         | :ballot_box_with_check: |                         |                         |
| **Number of Interfaces (NOI)**                   | Count of interfaces/abstract classes         |                         |                         | :ballot_box_with_check: |                         |
| **Afferent Coupling (Ca)**                       | Number of external classes depending on it   |                         |                         | :ballot_box_with_check: |                         |
| **Efferent Coupling (Ce)**                       | Number of external classes it depends on     |                         |                         | :ballot_box_with_check: |                         |
| **Instability (I)**                              | How likely a package is to change            |                         |                         | :ballot_box_with_check: |                         |
| **Abstractness (A)**                             | Ratio of abstract classes in a package       |                         |                         | :ballot_box_with_check: |                         |
| **Normalized Distance from Main Sequence (DMS)** | How far a package is from ideal              |                         |                         | :ballot_box_with_check: |                         |
| **Nested Block Depth (NBD)**                     | Maximum nesting depth within a method        | :ballot_box_with_check: |                         |                         |                         | 
| **Number of Children (NOCh)**                    | Count of direct subclasses                   |                         | :ballot_box_with_check: |                         |                         |
| **Number of Parents (NOPa)**                     | Count of classes this class extends          |                         | :ballot_box_with_check: |                         |                         |
| **Number of Descendants (NOD)**                  | Count of classes deriving from this class    |                         | :ballot_box_with_check: |                         |                         |
| **Number of Ancestors (NOA)**                    | Count of classes this class derives from     |                         | :ballot_box_with_check: |                         |                         |
| **Number of Links (NOL)**                        | Number of relations to other classes         |                         | :ballot_box_with_check: |                         |                         |
| **Number of Dependants (NODa)**                  | Number of classes depending on this class    |                         | :ballot_box_with_check: |                         |                         |
| **Number of Dependencies (NODe)**                | Number of classes this class depends on      |                         | :ballot_box_with_check: |                         |                         |
| **Class Category Relational Cohesion (CCRC)**    | Measure of intra-package class cohesion      |                         |                         | :ballot_box_with_check: |                         |
| **Number of Comparisons (NCOMP)**                | Count of comparisons in a method             | :ballot_box_with_check: |                         |                         |                         |
| **Number of Control Variables (NVAR)**           | Count of control variables in a method       | :ballot_box_with_check: |                         |                         |                         |
| **McClure’s Complexity Metric (MCLC)**           | NCOMP + NVAR                                 | :ballot_box_with_check: |                         |                         |                         |
| **Fan-out (Fout)**                               | Count of immediately subordinate methods     | :ballot_box_with_check: |                         |                         |                         |
| **Fan-in (Fin)**                                 | Count of methods invoking this method        | :ballot_box_with_check: |                         |                         |                         |
| **Structural Complexity (Si)**                   | Fout^2                                       | :ballot_box_with_check: |                         |                         |                         |
| **Input/Output Variables (IOVars)**              | NOP + 1 (0 for void return type)             | :ballot_box_with_check: |                         |                         |                         |
| **Data Complexity (Di)**                         | IOVars / (Fout + 1)                          | :ballot_box_with_check: |                         |                         |                         |
| **System Complexity (Ci)**                       | Si + Di                                      | :ballot_box_with_check: |                         |                         |                         |
| **Class Total System Complexity (ClTCi)**        | Sum of Ci for all methods in class           |                         | :ballot_box_with_check: |                         |                         |
| **Class Relative System Complexity (ClRCi)**     | Average Ci for all methods in class          |                         | :ballot_box_with_check: |                         |                         |
| **Package Total System Complexity (PkgTCi)**     | Sum of Ci across all classes in package      |                         |                         | :ballot_box_with_check: |                         |
| **Package Relative System Complexity (PkgRCi)**  | Average Ci across all classes in package     |                         |                         | :ballot_box_with_check: |                         |