package ru.isador.jcqm.report.plugins.stat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Collectors;

import ru.isador.jcqm.model.DependencyModel;
import ru.isador.jcqm.model.PackageInfo;
import ru.isador.jcqm.report.Report;
import ru.isador.jcqm.report.ReportConfig;
import ru.isador.jcqm.report.ReportPlugin;

public class TablePlugin implements ReportPlugin {

    private static final String TABLE_TEMPLATE = "<div id=\"packageTable\">\n" +
                                                 "    <table style=\"border: 1px solid black;\" class=\"sortable\">\n" +
                                                 "        <tr>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Пакет</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">CCN</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Абстрактность</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Нестабильность</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Связность</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Афферентные зависимости</th>\n" +
                                                 "            <th style=\"border: 1px solid black;\">Эфферентные зависимости</th>\n" +
                                                 "        </tr>\n" +
                                                 "        %s\n" +
                                                 "    </table>\n" +
                                                 "</div>\n";
    private static final String ROW_TEMPLATE = "        <tr>\n" +
                                               "            <td style=\"border: 1px solid black;\">%s</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%.3f</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%.3f</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%.3f</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%.3f</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%s</td>\n" +
                                               "            <td style=\"border: 1px solid black;\">%s</td>\n" +
                                               "        </tr>\n";

    @Override
    public void generateContent(DependencyModel dependencyModel, Report report) {
        try {
            addSortTableJs(report);

            StringBuilder sb = new StringBuilder();
            dependencyModel.getPackages().stream()
                .sorted(Comparator.comparing(PackageInfo::getName))
                .forEach(p -> {
                    String afferent = p.getAfferent().stream()
                                          .sorted(Comparator.comparing(PackageInfo::getName))
                                          .map(PackageInfo::getName)
                                          .collect(Collectors.joining("</br>\n"));
                    String efferent = p.getEfferent().stream()
                                          .sorted(Comparator.comparing(PackageInfo::getName))
                                          .map(PackageInfo::getName)
                                          .collect(Collectors.joining("</br>\n"));
                    sb.append(String.format(ROW_TEMPLATE, p.getName(), p.getCcn(), p.getAbstractness(), p.getInstability(), p.getCohesion(), afferent,
                        efferent));
                });
            report.addBody(String.format(TABLE_TEMPLATE, sb));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(ReportConfig config) {

    }


    private void addSortTableJs(Report report) throws IOException {
        try (InputStream sorttable = getClass().getResourceAsStream("sorttable.js")) {
            Files.copy(sorttable, report.getJsPath().resolve("sorttable.js"), StandardCopyOption.REPLACE_EXISTING);
        }

        report.addHead("<script src=\"" + report.getJsPath().getFileName().toString() + "/sorttable.js\"></script>");
    }
}
