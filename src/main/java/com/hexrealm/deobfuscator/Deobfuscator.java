package com.hexrealm.deobfuscator;

import com.hexrealm.betterasm.ClassNode;

import java.util.List;

public interface Deobfuscator {

    void process(List<ClassNode> classNodes);

}
