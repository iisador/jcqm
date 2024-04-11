package ru.isador.jcqm.framework.asm.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ru.isador.jcqm.model.DependencyModel;

public class SimpleDependencyModel implements DependencyModel {

    private final Map<String, AsmPackageInfo> packages;
    private final Collection<String> excludePackages;

    public SimpleDependencyModel(Collection<String> excludePackages) {
        packages = Collections.synchronizedMap(new HashMap<>());
        this.excludePackages = new HashSet<>(excludePackages);
    }

    public synchronized void addDependency(AsmClassInfo clazz, Dependency dependency) {
        clazz.addEfferent(dependency);
        dependency.clazz().addAfferent(new Dependency(dependency.type(), clazz));
    }

    public AsmPackageInfo getPackage(String name) {
        boolean isExcluded = excludePackages.stream()
                                 .anyMatch(name::matches);
        return isExcluded ? null : packages.computeIfAbsent(name, AsmPackageInfo::new);
    }

    @Override
    public Collection<AsmPackageInfo> getPackages() {
        return Collections.unmodifiableCollection(packages.values());
    }
}
