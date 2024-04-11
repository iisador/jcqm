import ru.isador.jcqm.report.ReportPlugin;

module ru.isador.mitrics.report {
    uses ReportPlugin;

    requires ru.isador.metrics.model;

    exports ru.isador.jcqm.report;
}