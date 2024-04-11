import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.puml.DiagramPlugin;

module ru.isador.metrics.report.plugins.puml {

    requires ru.isador.metrics.model;
    requires ru.isador.mitrics.report;
    requires net.sourceforge.plantuml;

    provides ReportPlugin with DiagramPlugin;
}