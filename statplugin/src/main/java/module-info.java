import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.plugins.stat.TablePlugin;

module ru.isador.metrics.report.plugins.stat {

    requires jcqm.model;
    requires jcqm.report;

    provides ReportPlugin with TablePlugin;
}
