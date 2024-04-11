package ru.isador.jcqm.report;

import ru.isador.jcqm.model.DependencyModel;

public interface ReportPlugin {

    void generateContent(DependencyModel dependencyModel, Report report);

    void configure(ReportConfig config);
}
