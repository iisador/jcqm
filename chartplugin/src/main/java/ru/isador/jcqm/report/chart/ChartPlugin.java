package ru.isador.jcqm.report.chart;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.stream.Collectors;

import ru.isador.jcqm.model.DependencyModel;
import ru.isador.jcqm.report.Report;
import ru.isador.jcqm.report.ReportConfig;
import ru.isador.jcqm.report.ReportPlugin;

public class ChartPlugin implements ReportPlugin {

    private static final String BODY = "    <div id=\"graph\" style=\"height: 800px;\">\n" +
                                       "        <canvas id=\"myChart\"></canvas>\n" +
                                       "        <script>\n" +
                                       "            window.onload = function () {\n" +
                                       "                const ctx = document.getElementById('myChart');\n" +
                                       "\n" +
                                       "                new Chart(ctx, {\n" +
                                       "                        type: 'scatter',\n" +
                                       "                        data: {\n" +
                                       "                            datasets:\n" +
                                       "                                [{\n" +
                                       "                                    label: 'Пакеты',\n" +
                                       "                                    data: packageData,\n" +
                                       "                                    backgroundColor: 'rgb(255, 99, 132)'\n" +
                                       "                                },\n" +
                                       "                                    {\n" +
                                       "                                        label: 'Главная последовательность',\n" +
                                       "                                        data: [{x: 0, y: 1}, {x: 1, y: 0}],\n" +
                                       "                                        borderColor: 'rgb(211,211,211)',\n" +
                                       "                                        backgroundColor: 'rgb(211,211,211)',\n" +
                                       "                                        type: 'line',\n" +
                                       "                                        order: 0\n" +
                                       "                                    },\n" +
                                       "                                    {\n" +
                                       "                                        data: [{x: 0, y: 0.8}, {x: 0.8, y: 0}],\n" +
                                       "                                        borderColor: 'rgb(211,211,211)',\n" +
                                       "                                        backgroundColor: 'rgb(211,211,211)',\n" +
                                       "                                        type: 'line',\n" +
                                       "                                        borderDash: [6, 6],\n" +
                                       "                                        order: 0\n" +
                                       "                                    },\n" +
                                       "                                    {\n" +
                                       "                                        data: [{x: 0.2, y: 1}, {x: 1, y: 0.2}],\n" +
                                       "                                        borderColor: 'rgb(211,211,211)',\n" +
                                       "                                        backgroundColor: 'rgb(211,211,211)',\n" +
                                       "                                        type: 'line',\n" +
                                       "                                        borderDash: [6, 6],\n" +
                                       "                                        order: 0\n" +
                                       "                                    }]\n" +
                                       "                        },\n" +
                                       "                        options:\n" +
                                       "                            {\n" +
                                       "                                scales: {\n" +
                                       "                                    x: {\n" +
                                       "                                        type: 'linear',\n" +
                                       "                                        position: 'bottom',\n" +
                                       "                                        title: {\n" +
                                       "                                            text: 'Устойчивость',\n" +
                                       "                                            display: true\n" +
                                       "                                        }\n" +
                                       "                                    },\n" +
                                       "                                    y: {\n" +
                                       "                                        type: 'linear',\n" +
                                       "                                        position: 'bottom',\n" +
                                       "                                        title: {\n" +
                                       "                                            text: 'Абстрактность',\n" +
                                       "                                            display: true\n" +
                                       "                                        }\n" +
                                       "                                    }\n" +
                                       "                                },\n" +
                                       "                                plugins: {\n" +
                                       "                                    tooltip: {\n" +
                                       "                                        callbacks: {\n" +
                                       "                                            label: function (context) {\n" +
                                       "                                                if (context.raw.label) {\n" +
                                       "                                                    return context.raw.label + ' (' + context.raw.x + ';' + context.raw.y + ')';\n" +
                                       "                                                }\n" +
                                       "                                                return null;\n" +
                                       "                                            }\n" +
                                       "                                        }\n" +
                                       "                                    },\n" +
                                       "                                    legend: {\n" +
                                       "                                        labels: {\n" +
                                       "                                            filter: function(item, chart) {\n" +
                                       "                                                return item.text;\n" +
                                       "                                            }\n" +
                                       "                                        }\n" +
                                       "                                    }\n" +
                                       "                                }\n" +
                                       "                            }\n" +
                                       "                    }\n" +
                                       "                );\n" +
                                       "            }\n" +
                                       "        </script>\n" +
                                       "    </div>\n";

    @Override
    public void generateContent(DependencyModel dependencyModel, Report report) {
        try {
            addChartJs(report);
            addDataJs(dependencyModel, report);
            report.addBody(BODY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(ReportConfig config) {

    }

    private void addChartJs(Report report) throws IOException {
        try (InputStream chartJs = getClass().getResourceAsStream("chart.js")) {
            Files.copy(chartJs, report.getJsPath().resolve("chart.js"), StandardCopyOption.REPLACE_EXISTING);
        }

        report.addHead("<script src=\"" + report.getJsPath().getFileName().toString() + "/chart.js\"></script>");
    }

    private void addDataJs(DependencyModel dependencyModel, Report report) throws IOException {
        Path dataJsPath = report.getJsPath().resolve("data.js");
        try (PrintWriter pw = new PrintWriter(Files.newOutputStream(dataJsPath))) {
            pw.println("let packageData = [");
            String data = dependencyModel.getPackages().stream()
                                            .map(
                                  p -> String.format(Locale.US, "{x: %.3f, y: %.3f, label: \"%s\"}", p.getInstability(), p.getAbstractness(),
                                      p.getName()))
                              .collect(Collectors.joining(",\n"));
            pw.print(data);
            pw.println("]");
            pw.flush();
        }

        report.addHead("<script src=\"" + report.getJsPath().getFileName().toString() + "/data.js\"></script>");
    }
}
