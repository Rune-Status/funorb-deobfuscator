package com.hexrealm.deobfuscator.impl;

import com.hexrealm.betterasm.ClassNode;
import com.hexrealm.deobfuscator.Deobfuscator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Objects;

public class TryCatchDeobfuscator implements Deobfuscator {

    @Override
    public void process(List<ClassNode> classNodes) {
        classNodes.forEach(classNode -> {
            List<MethodNode> methods = classNode.methods;

            for (MethodNode method : methods) {

                if ((method.access & Opcodes.ACC_ABSTRACT) != 0 || (method.access & Opcodes.ACC_NATIVE) != 0) {
                    continue;
                }
                method.tryCatchBlocks.removeIf(tc -> (Objects.equals(tc.type, "java/lang/RuntimeException") && tc.handler.getNext().getNext().getOpcode() == Opcodes.ATHROW));
            }
        });
    }
}
