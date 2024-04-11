package ru.isador.jcqm.framework.asm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.isador.jcqm.framework.asm.model.AsmClassInfo;
import ru.isador.jcqm.framework.asm.model.AsmPackageInfo;
import ru.isador.jcqm.framework.asm.model.Dependency;
import ru.isador.jcqm.framework.asm.model.SimpleDependencyModel;
import ru.isador.jcqm.model.DependencyType;

public class DependencyResolver {

    private final SimpleDependencyModel simpleDependencyModel;

    public DependencyResolver(SimpleDependencyModel simpleDependencyModel) {
        this.simpleDependencyModel = simpleDependencyModel;
    }

    Collection<Dependency> getDependencies(AsmPackageInfo currentPackageInfo, String descriptor) {
        Pattern p = Pattern.compile("(?<type>L[\\w/$]+[;|<])");
        Matcher m = p.matcher(descriptor);

        Collection<Dependency> dependencies = new HashSet<>();
        while (m.find()) {
            String fullClassName = m.group("type");
            fullClassName = fullClassName.substring(1, fullClassName.length() - 1);
            getDependency(currentPackageInfo, fullClassName)
                .ifPresent(dependencies::add);
        }

        return dependencies;
    }

    Optional<Dependency> getDependency(AsmPackageInfo currentPackageInfo, String fullClassName) {
        String packageName = getPackageName(fullClassName);
        AsmPackageInfo _packageInfo = simpleDependencyModel.getPackage(packageName);
        if (_packageInfo != null) {
            String className = getClassName(fullClassName);
            AsmClassInfo clazz = _packageInfo.getClass(className);
            if (fullClassName.contains("$")) {
                return Optional.of(new Dependency(DependencyType.INNER_CLASS, clazz));
            } else if (_packageInfo.equals(currentPackageInfo)) {
                return Optional.of(new Dependency(DependencyType.INNER_PACKAGE, clazz));
            }
            return Optional.of(new Dependency(DependencyType.ACROSS_PACKAGE, clazz));
        }
        return Optional.empty();
    }

    String getClassName(String fullName) {
        int i = fullName.lastIndexOf('/');
        if (i != -1) {
            fullName = fullName.substring(i + 1);
        }

        return fullName;
    }

    String getPackageName(String fullName) {
        int i = fullName.lastIndexOf('/');
        if (i != -1) {
            return fullName.substring(0, i).replace('/', '.');
        }
        return "";
    }
}
