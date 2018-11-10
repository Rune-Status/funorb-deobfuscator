package com.hexrealm.betterasm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class AsmUtils {

    public static List<ClassNode> loadJarClasses(Path pathToJar) throws IOException {
        return loadJarClasses(pathToJar, ClassReader.SKIP_DEBUG);
    }

    public static List<ClassNode> loadJarClasses(Path pathToJar, int parsingOptions) throws IOException {
        ArrayList<ClassNode> classNodes = new ArrayList<>();

        try (JarFile jarFile = new JarFile(pathToJar.toString())) {
            Enumeration<?> enums = jarFile.entries();

            while (enums.hasMoreElements()) {
                JarEntry entry = (JarEntry) enums.nextElement();

                if (entry.getName().endsWith(".class")) {
                    ClassReader classReader = new ClassReader(jarFile.getInputStream(entry));
                    ClassNode classNode = new ClassNode();

                    classReader.accept(classNode, parsingOptions);
                    classNodes.add(classNode);
                }
            }
        }
        return classNodes;
    }

    public static void writeClassesToJar(Collection<ClassNode> nodes, Path path) throws IOException {

        try (JarOutputStream output = new JarOutputStream(new FileOutputStream(path.toFile()))) {

            for (org.objectweb.asm.tree.ClassNode node : nodes) {

                JarEntry entry = new JarEntry(node.name + ".class");
                output.putNextEntry(entry);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                output.write(writer.toByteArray());
                output.closeEntry();
            }
        }
    }
}
