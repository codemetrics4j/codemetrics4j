package org.codemetrics4j.executive;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.codemetrics4j.metrics.MetricName;

public class ProcessorConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ProcessorConfiguration.class.getName());

    private final Set<MetricName> disabledMetrics;

    public ProcessorConfiguration(List<MetricName> disabledMetrics) {
        this.disabledMetrics = new TreeSet<>(disabledMetrics);
        validate();
    }

    /**
     * Validates the configuration by disabling all metrics in a group if any of them is disabled.
     * It also handles dependencies by disabling one group if the required metrics are not enabled.
     */
    private void validate() {
        validateGroup(List.of(
                MetricName.NF,
                MetricName.NSF,
                MetricName.NPF,
                MetricName.NM,
                MetricName.NSM,
                MetricName.NPM,
                MetricName.DIT,
                MetricName.NORM,
                MetricName.NMI,
                MetricName.NMA,
                MetricName.SIX)); // NumberOfFieldsCalculator and SpecializationIndexCalculator
        validateGroup(List.of(
                MetricName.Ca,
                MetricName.Ce,
                MetricName.I,
                MetricName.NOI,
                MetricName.A,
                MetricName.DMS)); // RobertMartinCouplingCalculator
        validateGroup(List.of(
                MetricName.NOPa, MetricName.NOCh, MetricName.NOD, MetricName.NOA)); // ClassInheritanceCalculator
        validateGroup(List.of(
                MetricName.Mit,
                MetricName.Mi,
                MetricName.Md,
                MetricName.Mo,
                MetricName.Ma,
                MetricName.PMi,
                MetricName.PMd,
                MetricName.HMi,
                MetricName.HMd,
                MetricName.NMIR,
                MetricName.MIF,
                MetricName.PMR,
                MetricName.MHF,
                MetricName.Ait,
                MetricName.Ai,
                MetricName.Ad,
                MetricName.Ao,
                MetricName.Av,
                MetricName.AIF,
                MetricName.AHF)); // MethodAndAttributeInheritanceCalculator
        validateGroup(List.of(
                MetricName.Fout,
                MetricName.Fin,
                MetricName.Si,
                MetricName.IOVars,
                MetricName.Di,
                MetricName.Ci)); // FanCalculator
        validateGroup(List.of(MetricName.NCOMP, MetricName.NVAR, MetricName.MCLC)); // McclureCalculator
        validateGroup(List.of(MetricName.ClTCi, MetricName.ClRCi, MetricName.PF)); // TypeAggregatorCalculator
        validateGroup(List.of(MetricName.PkgTCi, MetricName.PkgRCi, MetricName.CCRC)); // PackageAggregatorCalculator
        validateGroup(List.of(MetricName.NODa, MetricName.NODe, MetricName.CF)); // CouplingFactorCalculator

        validateDependencies();
    }

    /**
     * Validates a group of metrics by disabling all of them if any one is disabled.
     *
     * @param metrics The list of metrics in the group.
     */
    private void validateGroup(List<MetricName> metrics) {
        for (MetricName metric : metrics) {
            if (disabledMetrics.contains(metric)) {
                disableMetrics(metrics);
                break;
            }
        }
    }

    /**
     * Validates dependencies between metric groups, disabling groups if the required metrics are not enabled.
     */
    private void validateDependencies() {
        validateDependencyGroup(
                List.of(MetricName.ClTCi, MetricName.ClRCi, MetricName.PF),
                List.of(MetricName.Ci, MetricName.NOD, MetricName.Mo, MetricName.Md));
        validateDependencyGroup(
                List.of(MetricName.PkgTCi, MetricName.PkgRCi, MetricName.CCRC), List.of(MetricName.Ci, MetricName.NOL));
    }

    /**
     * Validates a dependent group of metrics, disabling the group if the required metrics are not enabled.
     *
     * @param dependentMetrics The list of dependent metrics.
     * @param requiredMetrics  The list of required metrics.
     */
    private void validateDependencyGroup(List<MetricName> dependentMetrics, List<MetricName> requiredMetrics) {
        for (MetricName requiredMetric : requiredMetrics) {
            if (disabledMetrics.contains(requiredMetric)) {
                disableMetrics(dependentMetrics);
                break;
            }
        }
    }

    /**
     * Disables all metrics in a group and logs the action.
     *
     * @param metrics The list of metrics to be disabled.
     */
    private void disableMetrics(List<MetricName> metrics) {
        String groupName = String.join(", ", metrics.toString());
        disabledMetrics.addAll(metrics);
        LOGGER.info("Disabling metrics group: " + groupName);
    }

    /**
     * Returns the list of disabled metrics.
     *
     * @return The list of disabled metrics.
     */
    public Set<MetricName> getDisabledMetrics() {
        return new TreeSet<>(disabledMetrics); // a defensive copy
    }
}
