package ru.isador.jcqm.framework.asm.model;

import ru.isador.jcqm.model.DependencyType;

public record Dependency(DependencyType type, AsmClassInfo clazz) {
}
