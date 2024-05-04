import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.chart.ChartPlugin;

module ru.isador.metrics.report.plugins.chart {

    requires jcqm.model;
    requires jcqm.report;

    provides ReportPlugin with ChartPlugin;
}
