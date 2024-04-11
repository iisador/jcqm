import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.plugins.stat.TablePlugin;

module ru.isador.metrics.report.plugins.stat {

    requires ru.isador.metrics.model;
    requires ru.isador.mitrics.report;

    provides ReportPlugin with TablePlugin;
}
