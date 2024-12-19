package com.github.coderjingtao.agent;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 修改类字节码的类
 * @author Joseph.Liu
 */
public class Transformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if(className == null || !className.startsWith("com/github/coderjingtao")){
            return null;
        }
        try{
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_FRAMES);
            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    return new MethodVisitor(Opcodes.ASM5, mv) {
                        @Override
                        public void visitCode() {
                            //插入方法开始时的逻辑
                            super.visitCode();
                            super.visitLdcInsn(className + "." + name);
                            super.visitMethodInsn(Opcodes.INVOKESTATIC,"com/github/coderjingtao/agent/MethodTimer","start","(Ljava/lang/String;)V",false);
                        }

                        @Override
                        public void visitInsn(int opcode) {
                            //在方法返回之前插入逻辑
                            if((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW){
                                super.visitLdcInsn(className + "." + name);
                                super.visitMethodInsn(Opcodes.INVOKESTATIC,"com/github/coderjingtao/agent/MethodTimer","end","(Ljava/lang/String;)V",false);
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
            };
            reader.accept(visitor, ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
