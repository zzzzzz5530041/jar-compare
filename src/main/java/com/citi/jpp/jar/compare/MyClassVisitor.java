package com.citi.jpp.jar.compare;

import org.objectweb.asm.*;

import java.util.*;

public class MyClassVisitor extends ClassVisitor {
    private String name;
    private String superName;
    private String[] interfaces;
    private boolean isInterface;
    private List<ClassRef.Member> members;
    private ClassRef.Handle classHandle;
    private Set<String> annotations;
    private final Set<ClassRef> classRefs;
    private final Set<MethodRef> methodRefs;
    private final String jar;



    public MyClassVisitor(Set<ClassRef> classRefs,
                          Set<MethodRef> methodRefs,
                          String jarName) {
        super(Opcodes.ASM9);
        this.classRefs = classRefs;
        this.methodRefs = methodRefs;
        this.jar = jarName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.name = name;
        this.superName = superName;
        this.interfaces = interfaces;
        this.isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        this.members = new ArrayList<>();
        this.classHandle = new ClassRef.Handle(name);
        annotations = new HashSet<>();
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        annotations.add(descriptor);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if ((access & Opcodes.ACC_STATIC) == 0) {
            Type type = Type.getType(descriptor);
            String typeName;
            if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                typeName = type.getInternalName();
            } else {
                typeName = type.getDescriptor();
            }
            members.add(new ClassRef.Member(name, access, new ClassRef.Handle(typeName)));
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        Set<String> mAnno = new HashSet<>();
        methodRefs.add(new MethodRef(
                classHandle,
                name,
                descriptor,
                isStatic,
                mAnno, access));
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
//        return new DiscoveryMethodAdapter(Opcodes.ASM9, mv, mAnno);
        return mv;
    }

    @Override
    public void visitEnd() {
        ClassRef classReference = new ClassRef(
                name,
                superName,
                Arrays.asList(interfaces),
                isInterface,
                members,
                annotations,
                jar);
        classRefs.add(classReference);
        super.visitEnd();
    }
}
