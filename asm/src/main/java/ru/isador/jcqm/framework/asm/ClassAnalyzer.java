package ru.isador.jcqm.framework.asm;

import java.util.Collection;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import ru.isador.jcqm.framework.asm.model.AsmClassInfo;
import ru.isador.jcqm.framework.asm.model.AsmMethodInfo;
import ru.isador.jcqm.framework.asm.model.AsmPackageInfo;
import ru.isador.jcqm.framework.asm.model.Dependency;
import ru.isador.jcqm.framework.asm.model.SimpleDependencyModel;

class ClassAnalyzer extends ClassVisitor {

    private static final Collection<String> RESERVED_CLASS_NAMES = List.of("package-info", "module-info");

    private final SimpleDependencyModel simpleDependencyModel;
    private final DependencyResolver dependencyResolver;

    private AsmPackageInfo currentPackageInfo;
    private AsmClassInfo currentClassInfo;

    protected ClassAnalyzer(SimpleDependencyModel simpleDependencyModel) {
        super(Opcodes.ASM9);
        this.simpleDependencyModel = simpleDependencyModel;
        dependencyResolver = new DependencyResolver(simpleDependencyModel);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (currentPackageInfo == null) {
            currentPackageInfo = simpleDependencyModel.getPackage(dependencyResolver.getPackageName(name));
        }

        if (currentPackageInfo == null) {
            return;
        }

        String className = dependencyResolver.getClassName(name);
        if (RESERVED_CLASS_NAMES.contains(className)) {
            return;
        }

        currentClassInfo = currentPackageInfo.getClass(className);
        currentClassInfo.setAbstract((access & Opcodes.ACC_ABSTRACT) != 0);

        if (signature != null) {
            Collection<Dependency> dependencies = dependencyResolver.getDependencies(currentPackageInfo, signature);
            dependencies.forEach(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
        }
        dependencyResolver.getDependency(currentPackageInfo, superName)
                          .ifPresent(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
        //        System.out.println(superName);
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
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (currentClassInfo == null) {
            return null;
        }

        Collection<Dependency> dependencies;
        if (signature != null) {
            dependencies = dependencyResolver.getDependencies(currentPackageInfo, signature);
        } else {
            dependencies = dependencyResolver.getDependencies(currentPackageInfo, descriptor);
        }

        dependencies.forEach(dependency -> simpleDependencyModel.addDependency(currentClassInfo, dependency));
        AsmMethodInfo methodInfo = new AsmMethodInfo(name);
        currentClassInfo.addMethod(methodInfo);
        return new MethodAnalyzer(simpleDependencyModel, dependencyResolver, currentPackageInfo, currentClassInfo, methodInfo);
    }
}