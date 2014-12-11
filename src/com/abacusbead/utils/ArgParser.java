package com.abacusbead.utils;

import java.util.Map;
import java.util.HashMap;

/**
 * @author: mizhang
 * @since: Dec 11, 2014 3:46:59 PM
 */
public class ArgParser {
    protected Map<String, String> argValue;

    public ArgParser(String[] args) {
        argValue = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    argValue.put(args[i], args[i + 1]);
                    i++;
                } else {
                    argValue.put(args[i], "");
                }
            }
        }
    }

    public boolean hasArg(String arg) {
        return argValue.containsKey("--" + arg);
    }

    public String getValue(String arg) {
        if (hasArg(arg)) {
            return argValue.get("--" + arg);
        }

        return null;
    }

    public static void main(String[] args) {
        ArgParser argParser = new ArgParser(new String[]{"--hello", "world", "--this", "--is", "min"});
        System.out.println(argParser.hasArg("hello"));
        System.out.println(argParser.hasArg("world"));
        System.out.println(argParser.getValue("is"));
    }
}
