package ru.isador.jcqm.console;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.isador.jcqm.framework.AnalyzerException;
import ru.isador.jcqm.framework.ProjectAnalyzer;
import ru.isador.jcqm.report.ReportProvider;

public class Main {

    public static void main(String[] args) throws IOException, AnalyzerException {
        ArgumentsConfig argumentsConfig = new ArgumentsConfig();
        JCommander jCommander = JCommander.newBuilder()
                                          .addObject(argumentsConfig)
                                          .build();
        jCommander.setProgramName("java -jar jcqm.jar");

        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            e.printStackTrace();
            jCommander.usage();
            System.exit(1);
        }

        if (argumentsConfig.isHelp()) {
            jCommander.usage();
            System.exit(0);
        }

        ProjectAnalyzer analyzer = ServiceLoader.load(ProjectAnalyzer.class)
                                       .findFirst().orElseThrow();
        analyzer.configure(getAnalyzerProperties(argumentsConfig.getExcludedPackages()));

        new ReportProvider().createReport(analyzer.analyze(argumentsConfig.getProjectPath()), argumentsConfig.getOutputPath());
    }

    private static Map<String, Object> getAnalyzerProperties(Collection<String> excludedPackages) {
        Map<String, Object> analyzerProperties = new HashMap<>();
        analyzerProperties.put(ProjectAnalyzer.EXCLUDE_PACKAGES, excludedPackages);

        return analyzerProperties;
    }
}
