package com.github.coderjingtao.agent;

import java.lang.instrument.Instrumentation;

/**
 * Java Agent Entrance
 * @author Joseph.Liu
 */
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Java Play Agent Start...");
        inst.addTransformer(new Transformer(),false);
    }
}
