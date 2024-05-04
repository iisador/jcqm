import ru.isador.jcqm.framework.ProjectAnalyzer;
import ru.isador.jcqm.framework.asm.AsmProjectAnalyzer;

module jcqm.framework.asm {

    requires transitive jcqm.model;
    requires transitive jcqm.framework;

    requires org.objectweb.asm;

    exports ru.isador.jcqm.framework.asm;
    exports ru.isador.jcqm.framework.asm.model;

    provides ProjectAnalyzer with AsmProjectAnalyzer;
}
