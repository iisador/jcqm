import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.chart.ChartPlugin;

module ru.isador.metrics.report.plugins.chart {

    requires ru.isador.metrics.model;
    requires ru.isador.mitrics.report;

    provides ReportPlugin with ChartPlugin;
}
