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

    private static final String BODY = """
            <div id="graph" style="height: 800px;">
                <canvas id="myChart"></canvas>
                <script>
                    window.onload = function () {
                        const ctx = document.getElementById('myChart');

                        new Chart(ctx, {
                                type: 'scatter',
                                data: {
                                    datasets:
                                        [{
                                            label: 'Пакеты',
                                            data: packageData,
                                            backgroundColor: 'rgb(255, 99, 132)'
                                        },
                                            {
                                                label: 'Главная последовательность',
                                                data: [{x: 0, y: 1}, {x: 1, y: 0}],
                                                borderColor: 'rgb(211,211,211)',
                                                backgroundColor: 'rgb(211,211,211)',
                                                type: 'line',
                                                order: 0
                                            },
                                            {
                                                data: [{x: 0, y: 0.8}, {x: 0.8, y: 0}],
                                                borderColor: 'rgb(211,211,211)',
                                                backgroundColor: 'rgb(211,211,211)',
                                                type: 'line',
                                                borderDash: [6, 6],
                                                order: 0
                                            },
                                            {
                                                data: [{x: 0.2, y: 1}, {x: 1, y: 0.2}],
                                                borderColor: 'rgb(211,211,211)',
                                                backgroundColor: 'rgb(211,211,211)',
                                                type: 'line',
                                                borderDash: [6, 6],
                                                order: 0
                                            }]
                                },
                                options:
                                    {
                                        scales: {
                                            x: {
                                                type: 'linear',
                                                position: 'bottom',
                                                title: {
                                                    text: 'Устойчивость',
                                                    display: true
                                                }
                                            },
                                            y: {
                                                type: 'linear',
                                                position: 'bottom',
                                                title: {
                                                    text: 'Абстрактность',
                                                    display: true
                                                }
                                            }
                                        },
                                        plugins: {
                                            tooltip: {
                                                callbacks: {
                                                    label: function (context) {
                                                        if (context.raw.label) {
                                                            return context.raw.label + ' (' + context.raw.x + ';' + context.raw.y + ')';
                                                        }
                                                        return null;
                                                    }
                                                }
                                            },
                                            legend: {
                                                labels: {
                                                    filter: function(item, chart) {
                                                        return item.text;
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                        );
                    }
                </script>
            </div>
        """;

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
