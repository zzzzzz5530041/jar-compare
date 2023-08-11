package com.citi.jpp.jar.compare;

import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Data
public class MethodRef {
    private final ClassRef.Handle classRef;
    private final Set<String> annotations;
    private final String name;
    private final String desc;
    private final int access;
    private final boolean isStatic;
    public Handle getHandle() {
        return new Handle(classRef, name, desc);
    }
    public MethodRef(ClassRef.Handle classReference,
                           String name, String desc, boolean isStatic,
                           Set<String> annotations,
                           int access) {
        this.classRef = classReference;
        this.name = name;
        this.desc = desc;
        this.isStatic = isStatic;
        this.annotations = annotations;
        this.access = access;
    }
    public static class Handle {
        private final ClassRef.Handle classReference;
        private final String name;
        private final String desc;

        public Handle(ClassRef.Handle classReference, String name, String desc) {
            this.classReference = classReference;
            this.name = name;
            this.desc = desc;
        }

        public ClassRef.Handle getClassReference() {
            return classReference;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Handle handle = (Handle) o;
            if (!Objects.equals(classReference, handle.classReference))
                return false;
            if (!Objects.equals(name, handle.name))
                return false;
            return Objects.equals(desc, handle.desc);
        }

        @Override
        public int hashCode() {
            int result = classReference != null ? classReference.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            return result;
        }
    }
}
