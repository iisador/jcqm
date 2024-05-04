module ru.isador.main {

    uses ru.isador.jcqm.framework.ProjectAnalyzer;

    requires jcqm.framework;
    requires jcqm.report;
    requires jcommander;

    exports ru.isador.jcqm.console to jcommander;
}
