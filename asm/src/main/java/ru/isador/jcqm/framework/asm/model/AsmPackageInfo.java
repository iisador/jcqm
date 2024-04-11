package ru.isador.jcqm.framework.asm.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.isador.jcqm.model.DependencyType;
import ru.isador.jcqm.model.PackageInfo;

public class AsmPackageInfo implements PackageInfo {

    private final String name;
    private final Map<String, AsmClassInfo> classes;

    public AsmPackageInfo(String name) {
        this.name = name;
        classes = Collections.synchronizedMap(new HashMap<>());
    }

    public AsmClassInfo getClass(String name) {
        return classes.computeIfAbsent(name, (n) -> new AsmClassInfo(this, name));
    }

    public double getAbstractness() {
        long abstractElements = classes.values().stream()
                                    .filter(AsmClassInfo::isAbstract)
                                    .count();
        return (double) abstractElements / classes.size();
    }

    public Collection<AsmPackageInfo> getAfferent() {
        return classes.values().stream()
                   .flatMap(c -> c.getAfferent(DependencyType.ACROSS_PACKAGE).stream())
                   .map(AsmClassInfo::getPackage)
                   .collect(Collectors.toSet());
    }

    public double getCcn() {
        return classes.values().stream()
                   .mapToDouble(AsmClassInfo::getCcn).average().orElse(Double.NaN);
    }

    public Collection<AsmClassInfo> getClasses() {
        return classes.values();
    }

    public double getCohesion() {
        int innerDependenciesCount = classes.values().stream()
                                         .flatMap(c -> c.getEfferent(DependencyType.INNER_PACKAGE).stream())
                                         .collect(Collectors.toSet()).size();
        return (double) innerDependenciesCount / classes.size();
    }

    public double getDistance() {
        return Math.abs(getAbstractness() + getInstability() - 1);
    }

    public Collection<AsmPackageInfo> getEfferent() {
        return classes.values().stream()
                   .flatMap(c -> c.getEfferent(DependencyType.ACROSS_PACKAGE).stream())
                   .map(AsmClassInfo::getPackage)
                   .collect(Collectors.toSet());
    }

    public double getInstability() {
        if (getEfferent().isEmpty()) {
            return 0.0;
        }

        return (double) getEfferent().size() / (getAfferent().size() + getEfferent().size());
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AsmPackageInfo aAsmPackageInfo = (AsmPackageInfo) o;
        return Objects.equals(name, aAsmPackageInfo.name);
    }
}
