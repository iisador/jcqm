package ru.isador.jcqm.framework.asm.model;

import ru.isador.jcqm.model.MethodInfo;

public class AsmMethodInfo implements MethodInfo {

    private final String name;
    private int ccn;

    public AsmMethodInfo(String name) {
        this.name = name;
        ccn = 1;
    }

    public void incCcn() {
        ccn++;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getCcn() {
        return ccn;
    }

    public String getName() {
        return name;
    }
}
