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

# Metrics

codemetrics4j is currently tracking the following metrics:
   
 - [x] **Raw Total Lines of Code (RTLOC)** - The actual number of lines of code in a
   class, using the line numbers of the file itself.  Comments, whitespace, and
   everything else is counted. _(class)_
 - [x] **Total Lines of Code (TLOC)** - The total number of lines of code, ignoring
   comments, whitespace, and formatting differences _(project, package, class, method)_
 - [x] **Number of Attributes (NF)** - The number of fields/attributes _(class)_
 - [x] **Number of Static Attributes (NSF)** - The number of static attributes _(class)_
 - [x] **Number of Public Attributes (NPF)** - The number of public attributes _(class)_
 - [x] **Number of Methods (NM)** - The number of methods _(class)_
 - [x] **Number of Static Methods (NSM)** - The number of static methods _(class)_
 - [x] **Number of Public Methods (NPM)** - The number of public methods _(class)_
 - [x] **Number of Classes (NOC)** - The number of classes within a package _(package)_
 - [x] **Number of Parameters (NOP)** - The number of parameters a method takes _(method)_ 
 - [x] **Depth of Inheritance Tree (DIT)** - The maximum depth of the inheritance
     hierarchy for a class.  _(class)_
 - [x] **Number of Overridden Methods (NORM)** - The number of methods a class overrides
     or implements from a parent class _(class)_
 - [x] **Number of Inherited Methods (NMI)** - The number of methods a class inherits
   from parent classes _(class)_
 - [x] **Number of Methods Added to Inheritance (NMA)** - The number of methods a
   class inherits adds to the inheritance hierarchy; methods defined on the class
   that it did not override or inherit _(class)_
 - [x] **Specialization Index (SIX)** - How specialized a class is, defined as (DIT * NORM) / NOM _(class)_
 - [x] **Number of Methods Inherited Total (Mit)** - Methods inherited overall _(class)_
 - [x] **Number of Methods Inherited (Mi)** - Methods inherited but not overridden _(class)_
 - [x] **Number of Methods Defined (Md)** - Methods defined within class (same as NMA) _(class)_
 - [x] **Number of Methods Overidden (Mo)** - Methods in class that override an otherwise-inherited method _(class)_
 - [x] **Number of Methods (All) (Ma)** - Methods that can be invoked on a class (inherited, overridden, defined). Ma = Md + Mi Same as NORM. _(class)_
 - [x] **Method Inheritance Factor (MIF)** - Mi / Ma _(class)_
 - [x] **Number of Public Methods Defined (PMd)** - Number of defined methods that are public _(class)_ 
 - [x] **Number of Public Methods Inherited (PMi)** - Number of inherited (but not overridden) methods that are public _(class)_
 - [x] **Public Methods Ratio (PMR)** (PMd+PMi)/(Md+Mi) _(class)_
 - [x] **Number of Hidden Methods Defined (HMd)** - Number of defined methods that are non-public _(class)_
 - [x] **Number of Hidden Methods Inherited (HMi)** - Number of inherited (but not overridden) methods that are non-public _(class)_
 - [x] **Method Hiding Factor (MHF)** PMd / Md _(class)_
 - [x] **Number of Methods Inherited Ratio (NMIR)** (Mi / Mit) * 100 (from Lorenz and Kidd, renamed here for distinction from NMI) _(class)_
 - [x] **Coupling Factor (CF)** - see http://www.cs.kent.edu/~jmaletic/cs63901/lectures/SoftwareMetrics.pdf (note: defining this differntly than the text. text divides by TC^2-TC for the total number of relationships. that is a system level metric, but this metric is as the class level, which means the total number of relationships is 2(TC-1). if a class uses every other class and is used by every other class, that is its value for a CF of 1)_(class)_
 - [x] **Polymorphism Factor (PF)** - Mo / (Md * NOD) _(class)_
 - [x] **Number of Attributes Inherited Total (Ait)** - Attrobites inherited overall _(class)_
 - [x] **Number of Attributes Inherited (Ai)** - Attributes inherited but not overridden _(class)_
 - [x] **Number of Attributes Defined (Ad)** - Attributes defined within class (same as NMA) _(class)_
 - [x] **Number of Attributes Overidden (Ao)** - Attributes in class that override an otherwise-inherited attributes _(class)_
 - [x] **Number of Attributes (All) (Aa)** - Attributes that can be referenced in a class (inherited, overridden, defined). Aa = Ad + Ai _(class)_
 - [x] **Attribute Inheritance Factor (AIF)** - Ai / Aa _(class)_
 - [x] **Number of Public Attributes Defined (Av)** - Number of defined attributes that are public _(class)_ 
 - [x] **Number of Hidden Attributes Defined (Ah)** - Number of defined attributes that are non-public _(class)_
 - [x] **Method Hiding Factor (AHF)** Ah / Ad _(class)_
 - [x] **McCabe Cyclomatic Complexity (VG)** - The number of unique possible paths
     through code _(method)_
 - [x] **Weighed Methods per Class (WMC)** - The summation of all of the cyclomatic
       complexities of all methods on a class _(class)_
 - [x] **Lack of Cohesion Methods (LCOM)** - A measure for the Cohesiveness of a class.
       Calculated with the Henderson-Sellers method, based on the number of disjoint sets
       formed by comparing methods with the attributes they use _(class)_
 - [x] **Number of Interfaces (NOI)** - The number of abstract classes (and interfaces) in a package _(package)_
 - [x] **Afferent Coupling (Ca)** - Number of classes outside a package that depend on it _(package)_
 - [x] **Efferent Coupling (Ca)** - Number of classes inside a package that depend on classes outside of it _(package)_
 - [x] **Instability (I)** - Effectively the riskiness of a package, how often it has a reason to change, Ce/(Ce+Ca) _(package)_
 - [x] **Abstractness (A)** - The number of abstract classes (and interfaces) divided by the total number of types in a package, NOI / NOC _(package)_
 - [x] **Normalized Distance from Main Sequence (DMS)** - Robert Martin's metric for a packages distance from ideal,  | A + I - 1 | _(package)_
 - [x] **Nested Block Depth (NBD)** - The maximum depth of the deepest level of nesting within a method _(method)_
 - [x] **Number of Children (NOCh)** - Number of classes that directly extend this class _(class)_
 - [x] **Number of Parents (NOPa)** - Number of classes that this class directly extends _(class)_
 - [x] **Number of Descendants (NOD)** - Total number of classes that have this class as an ancestor _(class)_
 - [x] **Number of Ancestors (NOA)** - Total number of classes that have this class as a descendant _(class)_
 - [x] **Number of Links (NOL)** - Number of links (associations, generalizations, use links) between a class and all others _(class)_
 - [x] **Number of Dependants (NODa)** - Total number of classes that depend on this class  _(class)_
 - [x] **Number of Dependencies (NODe)** - Total number of classes that a class depends on (same as NOL) _(class)_
 - [x] **Class Category Relational Cohesion (CCRC)** - The rate of cohesion between a package's classes. Sum(NOL) / NOC _(package)_
 - [x] **Number of Comparisons (NCOMP)** - Number of comparisons in a method _(method)_
 - [x] **Number of Control Variables (NVAR)** - Number of control variables referenced in a method _(method)_
 - [x] **McClure’s Complexity Metric (MCLC)** - NCOP + NVAR _(method)_
 - [x] **Fan-out (Fout)** - The number of methods immediately subordinate to a method _(method)_
 - [x] **Fan-in (Fin)** - The number of methods that invoke a method _(method)_
 - [x] **Structural Complexity (Si)** - Fout^2 _(method)_
 - [x] **Input/Output Variables (IOVars)** - NOP + 1 (0 if void return type) _(method)_"
 - [x] **Data Complexity (Di)** - (IOVars)/(Fout+1) _(method)_
 - [x] **System Complexity (Ci)** - Si + Di _(method)_
 - [x] **Class Total System Complexity (ClTCi)** - sum(Ci) over all methods in class _(class)_
 - [x] **Class Relative System Complexity (ClRCi)** - avg(Ci) over all methods in class _(class)_
 - [x] **Package Total System Complexity (PkgTCi)** - sum(Ci) over all methods in all classes in a package _(package)_
 - [x] **Package Relative System Complexity (PkgRCi)** - avg(Ci) over all methods in all classes in a package _(package)_
