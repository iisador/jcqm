import ru.isador.jcqm.report.ReportPlugin;
import ru.isador.jcqm.report.puml.DiagramPlugin;

module ru.isador.metrics.report.plugins.puml {

    requires jcqm.model;
    requires jcqm.report;
    requires net.sourceforge.plantuml;

    provides ReportPlugin with DiagramPlugin;
}
