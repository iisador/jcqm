package ru.isador.jcqm.model;

import java.util.Collection;

public interface ClassInfo {

    Collection<? extends ClassInfo> getEfferent(DependencyType dependencyType);

    Collection<? extends ClassInfo> getAfferent(DependencyType dependencyType);

    /** Это где класс описан. */
    Collection<? extends ClassInfo> getAfferent();

    double getCcn();

    /* Исходящая, "зависит от" или импорты описанные в этом класса. */
    Collection<? extends ClassInfo> getEfferent();

    double getInstability();

    Collection<? extends MethodInfo> getMethods();

    String getName();

    PackageInfo getPackage();

    boolean isAbstract();
}
