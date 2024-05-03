package ru.isador.jcqm.model;

import java.util.Collection;

/** Аггрегированная информация о пакетах проекта. */
public interface DependencyModel {

    Collection<? extends PackageInfo> getPackages();
}
