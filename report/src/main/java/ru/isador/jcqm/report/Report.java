package ru.isador.jcqm.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Report {

    private static final String HTML_TEMPLATE = """
        <html lang="ru">
        <head>
            <meta charset="UTF-8">
            %s
            <title>Анализ проекта</title>
        </head>
        <body>
        %s
        </body>
        </html>
        """;
    private final List<String> headRows;
    private final List<String> bodyRows;
    private final Path reportDir;
    private final Path jsPath;

    public Report(Path reportDir) throws IOException {
        this.reportDir = reportDir;
        jsPath = reportDir.resolve("js");
        Files.createDirectories(jsPath);
        headRows = new ArrayList<>();
        bodyRows = new ArrayList<>();
    }

    public void addHead(String headString) {
        headRows.add(headString);
    }

    public void addBody(String bodyString) {
        bodyRows.add(bodyString);
    }

    String getContent() {
        String head = String.join("\n", headRows);
        String body = String.join("\n", bodyRows);
        return String.format(HTML_TEMPLATE, head, body);
    }

    public Path getJsPath() {
        return jsPath;
    }

    public Path getReportDir() {
        return reportDir;
    }
}
