package ru.isador.jcqm.framework.asm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.isador.jcqm.model.ClassInfo;
import ru.isador.jcqm.model.DependencyType;

public class AsmClassInfo implements ClassInfo {

    private final AsmPackageInfo _package;
    private final List<AsmMethodInfo> methods;
    private final Collection<Dependency> efferent;
    private final Collection<Dependency> afferent;
    private final String name;
    private boolean _abstract;

    public AsmClassInfo(AsmPackageInfo _package, String name) {
        methods = new ArrayList<>();
        efferent = new HashSet<>();
        afferent = new HashSet<>();
        _abstract = false;
        this._package = _package;
        this.name = name;
    }

    public void addMethod(AsmMethodInfo method) {
        methods.add(method);
    }

    public void addEfferent(Dependency dependency) {
        efferent.add(dependency);
    }

    public void addAfferent(Dependency dependency) {
        afferent.add(dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_package, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AsmClassInfo aClass = (AsmClassInfo) o;
        return Objects.equals(_package, aClass._package) && Objects.equals(name, aClass.name);
    }

    @Override
    public String toString() {
        return _package.getName() + "." + name;
    }

    public Collection<AsmClassInfo> getEfferent(DependencyType dependencyType) {
        return efferent.stream()
                   .filter(d -> d.type().equals(dependencyType))
                   .map(Dependency::clazz)
                   .collect(Collectors.toUnmodifiableSet());
    }

    /* Исходящая, "зависит от" или импорты описанные в этом класса. */
    public Collection<AsmClassInfo> getAfferent(DependencyType dependencyType) {
        return afferent.stream()
                   .filter(d -> d.type().equals(dependencyType))
                   .map(Dependency::clazz)
                   .collect(Collectors.toUnmodifiableSet());
    }

    /** Это где класс описан. */
    public Collection<AsmClassInfo> getAfferent() {
        return afferent.stream()
                   .map(Dependency::clazz)
                   .collect(Collectors.toUnmodifiableSet());
    }

    public double getCcn() {
        return methods.stream()
                   .mapToInt(AsmMethodInfo::getCcn).average().orElse(1.0);
    }

    public Collection<AsmClassInfo> getEfferent() {
        return efferent.stream()
                   .map(Dependency::clazz)
                   .collect(Collectors.toUnmodifiableSet());
    }

    public double getInstability() {
        if (getEfferent().isEmpty() && getAfferent().isEmpty()) {
            return 0.0;
        }
        return (double) getEfferent().size() / (getAfferent().size() + getEfferent().size());
    }

    public Collection<AsmMethodInfo> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public String getName() {
        return name;
    }

    public AsmPackageInfo getPackage() {
        return _package;
    }

    public boolean isAbstract() {
        return _abstract;
    }

    public void setAbstract(boolean _abstract) {
        this._abstract = _abstract;
    }
}
