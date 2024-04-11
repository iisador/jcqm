package ru.isador.jcqm.model;

import java.util.Collection;

public interface DependencyModel {

    Collection<? extends PackageInfo> getPackages();
}
