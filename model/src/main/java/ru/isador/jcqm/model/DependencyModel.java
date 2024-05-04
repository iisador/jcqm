package ru.isador.jcqm.model;

import java.util.Collection;

/** Агрегированная информация о пакетах проекта. */
public interface DependencyModel {

    Collection<? extends PackageInfo> getPackages();
}
