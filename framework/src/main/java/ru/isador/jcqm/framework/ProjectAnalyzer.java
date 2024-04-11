package ru.isador.jcqm.framework;

import java.nio.file.Path;
import java.util.Map;

import ru.isador.jcqm.model.DependencyModel;

public interface ProjectAnalyzer {

   String EXCLUDE_PACKAGES = "asm.packageFilter";

   DependencyModel analyze(Path path) throws AnalyzerException;

   void configure(Map<String, Object> properties);
}