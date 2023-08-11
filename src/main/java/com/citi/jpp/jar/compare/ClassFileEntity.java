package com.citi.jpp.jar.compare;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Path;

@Data@Builder
public class ClassFileEntity {
private Long id;
private String className;
    private Path path;
private String pathStr;
private String jarName;
}
