package ru.isador.jcqm.console;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;

@Parameters(resourceBundle = "i18n")
public class ArgumentsConfig {

    private Path projectPath;
    private Path outputPath;
    private boolean help;
    private List<String> excludedPackages;

    public ArgumentsConfig() {
        outputPath = Paths.get(".");
        excludedPackages = List.of("java.*", "javax.*", "jakarta.*");
    }

    public Path getProjectPath() {
        return projectPath;
    }

    @Parameter(descriptionKey = "params.projectPath.description", required = true, converter = PathConverter.class)
    public void setProjectPath(Path projectPath) {
        this.projectPath = projectPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    @Parameter( names = {"-o", "--output"}, descriptionKey = "params.outputPath.description", converter = PathConverter.class)
    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isHelp() {
        return help;
    }

    @Parameter(names = "--help", descriptionKey = "params.help.description", help = true)
    public void setHelp(boolean help) {
        this.help = help;
    }

    public List<String> getExcludedPackages() {
        return excludedPackages;
    }

    @Parameter(names = {"-e", "--exclude"}, descriptionKey = "params.exclude.description")
    public void setExcludedPackages(List<String> excludedPackages) {
        this.excludedPackages = excludedPackages;
    }
}
