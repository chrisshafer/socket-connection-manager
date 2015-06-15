package com.cgs.enhancedsocket;

/**
 * Created by Chris on 2/5/14.
 */
public class LogHandler {

    public static void log(String output){
        System.out.println("INFO: "+output);
    }
    public static void logWarning(String output){
        System.out.println("|WARNING|: "+output);
    }
    public static void logError(String output){
        System.err.println("!ERROR!: "+output);
    }
    public static void logTemporaryDisplay(String output){
        System.out.print("\r"+output+"\r");
    }
    public static void singleLine(String output){
        System.out.print("\r"+output+"");
    }
}
