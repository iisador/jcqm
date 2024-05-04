import ru.isador.jcqm.framework.ProjectAnalyzer;

module jcqm.framework {

    requires transitive jcqm.model;

    exports ru.isador.jcqm.framework;

    uses ProjectAnalyzer;
}
