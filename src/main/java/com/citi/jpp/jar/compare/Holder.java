package com.citi.jpp.jar.compare;

import java.util.*;

public class Holder {
    public  Set<ClassFileEntity> classFileList = new HashSet<>();
    public  Set<ClassRef> classRefs = new HashSet<>();
    public  Set<MethodRef> methodRefs = new HashSet<>();
    public  Map<ClassRef.Handle, ClassRef> classMap = new HashMap<>();
    public  Map<MethodRef.Handle, MethodRef> methodMap = new HashMap<>();
}