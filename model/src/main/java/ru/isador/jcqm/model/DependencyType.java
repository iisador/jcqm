package ru.isador.jcqm.model;

public enum DependencyType {

    // Связь между классами внутри одного пакета.
    INNER_PACKAGE,

    // Связь класса с внутренним классом.
    INNER_CLASS,

    // Связь между классами в разных пакетах.
    ACROSS_PACKAGE
}
