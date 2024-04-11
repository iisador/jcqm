package ru.isador.jcqm.report.puml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import ru.isador.jcqm.model.DependencyModel;
import ru.isador.jcqm.model.PackageInfo;
import ru.isador.jcqm.report.Report;
import ru.isador.jcqm.report.ReportConfig;
import ru.isador.jcqm.report.ReportPlugin;

public class DiagramPlugin implements ReportPlugin {

    private static final String MAP_TEMPLATE = """
        map "%s" as %s {
            Абстрактность => %.3f
            Нестабильность => %.3f
            Расстояние ГП => %.3f
        }
        """;

    private static final Pattern DOT_REPLACE_PATTERN = Pattern.compile("\\.(\\w)");

    private boolean skipPackagesWithoutDependencies;

    @Override
    public void generateContent(DependencyModel dependencyModel, Report report) {
        StringBuilder packages = new StringBuilder();
        StringBuilder dependencies = new StringBuilder();
        dependencyModel.getPackages()
            .forEach(p -> {
                if (skipPackagesWithoutDependencies && p.getAfferent().isEmpty() && p.getEfferent().isEmpty()) {
                    return;
                }

                packages.append(getPackageDescr(p))
                    .append("\n");

                String str = getPackageDependencies(p);
                dependencies.append(str);
                if (!str.isBlank()) {
                    dependencies.append("\n");
                }
            });

        String diagramText = "@startuml\nskinparam svgDimensionStyle false\n" + packages + dependencies + "@enduml\n";
        //        System.out.println(diagramText);
        SourceStringReader ssr = new SourceStringReader(diagramText);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ssr.outputImage(out, new FileFormatOption(FileFormat.SVG));
            String image = out.toString(StandardCharsets.UTF_8);
            report.addBody("<div id=\"diagram\">\n" + image + "\n" + "</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(ReportConfig config) {

    }

    private String getPackageDescr(PackageInfo p) {
        return String.format(MAP_TEMPLATE, p.getName(), getPackageNameCamelCased(p), p.getAbstractness(), p.getInstability(), p.getDistance());
    }

    private String getPackageNameCamelCased(PackageInfo p) {
        StringBuilder name = new StringBuilder();
        Matcher m = DOT_REPLACE_PATTERN.matcher(p.getName());
        while (m.find()) {
            m.appendReplacement(name, m.group(1).toUpperCase());
        }
        m.appendTail(name);
        return name.toString();
    }

    private String getPackageDependencies(PackageInfo _packageInfo) {
        return _packageInfo.getEfferent().stream()
                   .map(p -> getPackageDependencyString(_packageInfo, p))
                   .collect(Collectors.joining("\n"));
    }

    private String getPackageDependencyString(PackageInfo source, PackageInfo target) {
        if (source.getInstability() >= target.getInstability()) {
            return getPackageNameCamelCased(source) + " --> " + getPackageNameCamelCased(target);
        }

        return getPackageNameCamelCased(source) + " -[#red]-> " + getPackageNameCamelCased(target);
    }

    public void setSkipPackagesWithoutDependencies(boolean skipPackagesWithoutDependencies) {
        this.skipPackagesWithoutDependencies = skipPackagesWithoutDependencies;
    }
}
