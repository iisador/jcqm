package ru.isador.jcqm.framework.asm;

import java.util.Collection;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import ru.isador.jcqm.framework.asm.model.AsmClassInfo;
import ru.isador.jcqm.framework.asm.model.AsmMethodInfo;
import ru.isador.jcqm.framework.asm.model.Dependency;
import ru.isador.jcqm.framework.asm.model.SimpleDependencyModel;
import ru.isador.jcqm.framework.asm.model.AsmPackageInfo;

class MethodAnalyzer extends MethodVisitor {

    private final SimpleDependencyModel simpleDependencyModel;
    private final DependencyResolver dependencyResolver;
    private final AsmPackageInfo currentPackageInfo;
    private final AsmClassInfo currentClassInfo;
    private final AsmMethodInfo methodInfo;

    protected MethodAnalyzer(SimpleDependencyModel simpleDependencyModel, DependencyResolver dependencyResolver, AsmPackageInfo currentPackageInfo,
        AsmClassInfo currentClassInfo, AsmMethodInfo methodInfo) {
        super(Opcodes.ASM9);
        this.simpleDependencyModel = simpleDependencyModel;
        this.dependencyResolver = dependencyResolver;
        this.currentPackageInfo = currentPackageInfo;
        this.currentClassInfo = currentClassInfo;
        this.methodInfo = methodInfo;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (visible) { // учитываем только runtime зависимости
            Collection<Dependency> dependencies = dependencyResolver.getDependencies(currentPackageInfo, descriptor);
            dependencies.forEach(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
        }
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        if (visible) { // учитываем только runtime зависимости
            Collection<Dependency> dependencies = dependencyResolver.getDependencies(currentPackageInfo, descriptor);
            dependencies.forEach(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
        }
        return null;
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        methodInfo.incCcn();
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        Collection<Dependency> dependencies = dependencyResolver.getDependencies(currentPackageInfo, descriptor);
        dependencies.stream()
            .filter(d -> !d.clazz().equals(currentClassInfo))
            .forEach(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
    }
}