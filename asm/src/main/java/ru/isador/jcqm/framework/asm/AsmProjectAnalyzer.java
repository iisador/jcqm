package ru.isador.jcqm.framework.asm;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import ru.isador.jcqm.framework.asm.model.SimpleDependencyModel;
import ru.isador.jcqm.framework.AnalyzerException;
import ru.isador.jcqm.framework.ProjectAnalyzer;
import ru.isador.jcqm.model.DependencyModel;

public class AsmProjectAnalyzer implements ProjectAnalyzer {

    private Collection<String> packageFilter;

    @Override
    public DependencyModel analyze(Path path) throws AnalyzerException {
        try {
            Visitor visitor = new Visitor(packageFilter);
            Files.walkFileTree(path, visitor);
            return visitor.getAnalysisResult();
        } catch (IOException e) {
            throw new AnalyzerException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, Object> properties) {
        packageFilter = (Collection<String>) properties.get(EXCLUDE_PACKAGES);
    }

    private static class Visitor extends SimpleFileVisitor<Path> {

        private final SimpleDependencyModel simpleDependencyModel;

        public Visitor(Collection<String> packageFilter) {
            simpleDependencyModel = new SimpleDependencyModel(packageFilter);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.getFileName().toString().endsWith(".class")) {
                ClassReader classReader = new ClassReader(Files.newInputStream(file));
                classReader.accept(new ClassAnalyzer(simpleDependencyModel), 0);
            }

            return FileVisitResult.CONTINUE;
        }

        public SimpleDependencyModel getAnalysisResult() {
            return simpleDependencyModel;
        }
    }
}
