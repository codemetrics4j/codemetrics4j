package org.codemetrics4j.executive;

import java.util.Set;
import org.codemetrics4j.input.Processor;
import org.codemetrics4j.metrics.MetricName;
import org.codemetrics4j.metrics.calculators.*;

class ProcessorFactory {
    static Processor getProcessor(ProcessorConfiguration configuration) {
        Processor processor = new Processor();
        Set<MetricName> disabledMetrics = configuration.getDisabledMetrics();

        if (!disabledMetrics.contains(MetricName.RTLOC)) {
            processor.registerTypeCalculator(new RawTotalLinesOfCodeCalculator());
        }

        if (!disabledMetrics.contains(MetricName.NF)) {
            processor.registerTypeCalculator(new NumberOfFieldsCalculator());
        }

        if (!disabledMetrics.contains(MetricName.TLOC)) {
            processor.registerProjectCalculator(new TotalLinesOfCodeCalculator.ProjectCalculator());
            processor.registerPackageCalculator(new TotalLinesOfCodeCalculator.PackageCalculator());
            processor.registerTypeCalculator(new TotalLinesOfCodeCalculator.TypeCalculator());
            processor.registerMethodCalculator(new TotalLinesOfCodeCalculator.MethodCalculator());
        }

        if (!disabledMetrics.contains(MetricName.VG)) {
            processor.registerMethodCalculator(new CyclomaticComplexityCalculator());
        }
        if (!disabledMetrics.contains(MetricName.WMC)) {
            processor.registerTypeCalculator(new WeightedMethodsCalculator());
        }

        if (!disabledMetrics.contains(MetricName.NOP)) {
            processor.registerMethodCalculator(new NumberOfParametersCalculator());
        }
        if (!disabledMetrics.contains(MetricName.NOC)) {
            processor.registerPackageCalculator(new NumberOfClassesCalculator());
        }

        if (!disabledMetrics.contains(MetricName.DIT)) {
            processor.registerTypeCalculator(new SpecializationIndexCalculator());
        }

        if (!disabledMetrics.contains(MetricName.Ca)) {
            processor.registerPackageCalculator(new RobertMartinCouplingCalculator());
        }

        if (!disabledMetrics.contains(MetricName.NBD)) {
            processor.registerMethodCalculator(new NestedBlockDepthCalculator());
        }
        if (!disabledMetrics.contains(MetricName.LCOM)) {
            processor.registerTypeCalculator(new LackOfCohesionMethodsCalculator());
        }

        if (!disabledMetrics.contains(MetricName.NOPa)) {
            processor.registerTypeCalculator(new ClassInheritanceCalculator());
        }

        if (!disabledMetrics.contains(MetricName.Mit)) {
            processor.registerTypeCalculator(new MethodAndAttributeInheritanceCalculator());
        }

        if (!disabledMetrics.contains(MetricName.Fout)) {
            processor.registerMethodCalculator(new FanCalculator());
        }
        if (!disabledMetrics.contains(MetricName.NOL)) {
            processor.registerTypeCalculator(new LinkCalculator());
        }
        if (!disabledMetrics.contains(MetricName.NCOMP)) {
            processor.registerMethodCalculator(new McclureCalculator());
        }

        if (!disabledMetrics.contains(MetricName.ClTCi)) {
            processor.registerTypeCalculator(new TypeAggregatorCalculator());
        }
        if (!disabledMetrics.contains(MetricName.PkgTCi)) {
            processor.registerPackageCalculator(new PackageAggregatorCalculator());
        }

        if (!disabledMetrics.contains(MetricName.KATZ)) {
            processor.registerMethodCalculator(new KatzCentralityCalculator());
        }

        if (!disabledMetrics.contains(MetricName.CF)) {
            processor.registerTypeCalculator(new CouplingFactorCalculator());
        }

        return processor;
    }
}
