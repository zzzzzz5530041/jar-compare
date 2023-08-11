package com.citi.jpp.jar.compare;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ClassRef {
    // class name
    private final String name;
    // super class name
    private final String superClass;
    // interfaces name
    private final List<String> interfaces;
    // is interface
    private final boolean isInterface;
    // class members
    private final List<Member> members;
    private final Set<String> annotations;
    // jar name
    private final String jar;
    public Handle getHandle() {
        return new Handle(name);
    }
    public ClassRef(String name, String superClass, List<String> interfaces,
                          boolean isInterface, List<Member> members, Set<String> annotations, String jar) {
        this.name = name;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.isInterface = isInterface;
        this.members = members;
        this.annotations = annotations;
        this.jar = jar;
    }
    @Data
    public static class Member {
        // member name
        private final String name;
        // member modifiers
        private final int modifiers;
        // member type
        private final Handle type;

        public Member(String name, int modifiers, Handle type) {
            this.name = name;
            this.modifiers = modifiers;
            this.type = type;
        }

        public Handle getType() {
            return type;
        }
    }

    @Data
    public static class Handle{
        private final String name;

        public Handle(String name) {
            this.name = name;
        }
    }
}
