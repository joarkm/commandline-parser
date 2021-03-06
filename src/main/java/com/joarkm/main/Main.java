package com.joarkm.main;

import com.joarkm.commandline.input.CommandLineParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        List<String> validOptions = Arrays.asList("-path", "-max-scan-depth");
        List<String> validFlags = Arrays.asList("-recursive");
        CommandLineParser commandLineParser = new CommandLineParser(args, validOptions, validFlags);

        if (args.length > 0)
        {
            if (commandLineParser.hasOptions())
            {
                Map<String, String> options = commandLineParser.getOptions();
                Set<String> opts = options.keySet();
                System.out.println("Options used:");
                for (String opt : opts)
                {
                    String message = String.format("option: %s \t argument: %s", opt, options.get(opt));
                    System.out.println(message);
                }
                String pathArgument = commandLineParser.getArgument("-path");
                if (pathArgument != null)
                {
                    String message = String.format("Run with argument \"%s\" for the path option", pathArgument);
                    System.out.println(message);
                }
            }
            if (commandLineParser.hasFlags())
            {
                Set<String> flags  = commandLineParser.getFlags();
                System.out.println("Flags used:");
                System.out.println(Arrays.toString(flags.toArray()));
                if (commandLineParser.hasFlag("-recursive"))
                {
                    System.out.println("Flag \"recursive\" used.");
                }
            }
        }
        System.out.println("CommandLineParser.toString():");
        System.out.println(commandLineParser);
    }
    /** Example output when run with arguments '-path C:\folder -recursive':
     *  --------------------------------------------------
     *  Options used:
     *  option: -path 	 argument: C:\folder
     *  Run with argument "C:\folder" for the path option
     *  Flags used:
     *  [-recursive]
     *  Flag "recursive" used.
     *  CommandLineParser: {
     * 	    _flags = [-recursive]
     * 	    _options = {-path=C:\test.txt}
     * }
     *  --------------------------------------------------
     */
}
