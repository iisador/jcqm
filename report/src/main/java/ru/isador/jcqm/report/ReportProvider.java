package ru.isador.jcqm.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;

import ru.isador.jcqm.model.DependencyModel;

public class ReportProvider {

    public void createReport(DependencyModel dependencyModel, Path reportDir) throws IOException {
        Files.createDirectories(reportDir);
        Report report = new Report(reportDir);
        ReportConfig config = new ReportConfig();

        ServiceLoader<ReportPlugin> pluginsLoader = ServiceLoader.load(ReportPlugin.class);
        pluginsLoader.stream()
            .map(ServiceLoader.Provider::get)
            .peek(p -> p.configure(config))
            .forEach(p -> p.generateContent(dependencyModel, report));

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportDir.resolve("index.html"), StandardCharsets.UTF_8))) {
            writer.println(report.getContent());
            writer.flush();
        }
    }
}
