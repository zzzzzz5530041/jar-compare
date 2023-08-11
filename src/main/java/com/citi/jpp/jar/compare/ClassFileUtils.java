package com.citi.jpp.jar.compare;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.sun.media.jfxmediaimpl.HostUtils.isWindows;

@Slf4j
public class ClassFileUtils {

    public static Set<ClassFileEntity> getClassesFromJars(String version, String jarFile) throws Exception {

        List<String>  jarFiles = new ArrayList<>();
        jarFiles.add(jarFile);
        Set<ClassFileEntity> classEntities = new HashSet<>();
        for (String temp : jarFiles) {
            Path tmpDir = Paths.get(version+"/class-temp");
            try {
                Files.delete(tmpDir);
                Files.createDirectory(tmpDir);
            } catch (Exception ignored) {
            }

            Path jarPath = Paths.get(temp);
            InputStream is = Files.newInputStream(jarPath);
            JarInputStream jarInputStream = new JarInputStream(is);
            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                String name = jarEntry.getName();
                Path fullPath = tmpDir.resolve(jarEntry.getName());
                if(!jarEntry.isDirectory()){
                    // not class
                    if (!name.endsWith(".class")) {
                        log.error("not class file : {}" + name);
                    continue;
                    }else {
                        Path dirName = fullPath.getParent();
                        if (!Files.exists(dirName)) {
                            Files.createDirectories(dirName);
                        }
                        //write class file to tmp folder
                        OutputStream outputStream = Files.newOutputStream(fullPath);
                        copy(jarInputStream, outputStream);
                        outputStream.close();
                        ClassFileEntity classFileEntity =  ClassFileEntity.builder().className(name).jarName(temp).path(fullPath).pathStr(fullPath.toAbsolutePath().toString())
                                .build();
                        String splitStr;
                        if (isWindows()) {
                            splitStr = "\\\\";
                        } else {
                            splitStr = "/";
                        }
                        String[] splits = temp.split(splitStr);
                        classFileEntity.setJarName(splits[splits.length - 1]);

                        classEntities.add(classFileEntity);
                    }
                }
            }
        }
        return classEntities;
    }

    static void copy(InputStream inputStream, OutputStream outputStream) {
        try {
            final byte[] buffer = new byte[4096];
            int n;
            while ((n = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}