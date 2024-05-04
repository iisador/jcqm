package ru.isador.jcqm.console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import ru.isador.jcqm.framework.AnalyzerException;
import ru.isador.jcqm.framework.ProjectAnalyzer;
import ru.isador.jcqm.report.ReportProvider;

public class Main {

    public static void main(String[] args) throws IOException, AnalyzerException {
        ProjectAnalyzer analyzer = ServiceLoader.load(ProjectAnalyzer.class)
                                       .findFirst().orElseThrow();
        analyzer.configure(getAnalyzerProperties());

        Path jpdrNewPath = Paths.get(System.getenv("SRC"));

        Path jpdrNewReportPath = Paths.get(System.getenv("TRG"));

        new ReportProvider().createReport(analyzer.analyze(jpdrNewPath), jpdrNewReportPath);
    }

    private static Map<String, Object> getAnalyzerProperties() {
        Map<String, Object> analyzerProperties = new HashMap<>();
        analyzerProperties.put(ProjectAnalyzer.EXCLUDE_PACKAGES, List.of(System.getenv("EXCLUDE").split(File.pathSeparator)));

        return analyzerProperties;
    }
}
