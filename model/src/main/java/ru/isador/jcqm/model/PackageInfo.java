package ru.isador.jcqm.model;

import java.util.Collection;

public interface PackageInfo {

    ClassInfo getClass(String name);

    double getAbstractness();

    Collection<? extends PackageInfo> getAfferent();

    double getCcn();

    Collection<? extends ClassInfo> getClasses();

    double getCohesion();

    double getDistance();

    Collection<? extends PackageInfo> getEfferent();

    double getInstability();

    String getName();
}
