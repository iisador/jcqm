import ru.isador.jcqm.framework.ProjectAnalyzer;

module ru.isador.metrics.framework {
    requires transitive ru.isador.metrics.model;

    exports ru.isador.jcqm.framework;

    uses ProjectAnalyzer;
}