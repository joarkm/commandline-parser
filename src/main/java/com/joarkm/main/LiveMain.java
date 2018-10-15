package com.joarkm.main;

import com.joarkm.commandline.input.CommandLineParser;
import com.joarkm.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LiveMain {
    public static void main(String[] args) {

        List<String> validOptions = Arrays.asList("path");
        /*List<String> validFlags = Arrays.asList("");
        CommandLineParser commandLineParser = new CommandLineParser(args, validOptions, validFlags);*/
        CommandLineParser commandLineParser = new CommandLineParser(args, validOptions);
        System.out.println("CommandLineParser.toString():");
        System.out.println(commandLineParser);

        Reader reader = new InputStreamReader(System.in);
        BufferedReader inputReader = new BufferedReader(reader);

        try {
            String line;
            String[] lineValues;
            while (!(StringUtils.isNullOrEmpty((line = inputReader.readLine()))  )) {
                lineValues = line.split("\\s");
                commandLineParser.parse(lineValues);
                System.out.println(commandLineParser);
            }
            System.out.println("Client exiting...");
            return;
        } catch (IOException e) {
            System.exit(0);
        }

    }
}
