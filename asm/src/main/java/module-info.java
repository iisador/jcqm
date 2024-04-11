import ru.isador.jcqm.framework.ProjectAnalyzer;
import ru.isador.jcqm.framework.asm.AsmProjectAnalyzer;

module ru.isador.metrics.framework.asm {
    requires transitive ru.isador.metrics.model;
    requires transitive ru.isador.metrics.framework;

    requires org.objectweb.asm;

    exports ru.isador.jcqm.framework.asm;
    exports ru.isador.jcqm.framework.asm.model;

    provides ProjectAnalyzer with AsmProjectAnalyzer;
}