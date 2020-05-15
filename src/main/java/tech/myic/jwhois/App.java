/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.myic.jwhois;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jules
 */
public class App {

    public static void main(String[] args) {

        try {

            boolean isLinux = isLinux();
            if (!isLinux) {
                throw new RuntimeException("The code is not supported on: [" + System.getProperty("os.name") + "] operating system yet.");
            }

            String pathToEmlFile = "/home/jules/Downloads/RE,.eml";

            String emailSource = getSourceFromEmailFile(pathToEmlFile);

            String ip = getIpAddressFromEmailSource(emailSource);

            System.out.println("IP: " + ip);

            ProcessBuilder pb = new  ProcessBuilder("whois", ip);

            Process process;
            try {
                process = pb.start();
            } catch (IOException ex) {
                throw new RuntimeException("Unable to process command", ex);
            }

            StringBuilder sb = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error reading results from command", ex);
            }

            if (process.waitFor() != 0) {
                throw new RuntimeException("Unable to get result from command");
            }

            System.out.println("Results of whois: " + sb.toString());

        } catch (InterruptedException ex) {
            throw new RuntimeException("Error checking origin of email", ex);
        }
    }

    private static String getSourceFromEmailFile(String pathToEmlFile) {
        if (!pathToEmlFile.toLowerCase().endsWith(".eml")) {
            throw new RuntimeException("File is not an email");
        }

        StringBuilder sb;
        try {
            try (BufferedReader fr = new BufferedReader(new FileReader(new File(pathToEmlFile)))) {
                String line;
                sb = new StringBuilder();
                while ((line = fr.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file: " + pathToEmlFile, ex);
        }

        System.out.println("Email: \n" + sb.toString());

        return sb.toString();
    }

    private static String getIpAddressFromEmailSource(String text) {

        String regex = "(Received:).*(\\s{1}\\[\\d{0,}.\\d{0,}.\\d{0,}.\\d{0,}\\]\\))";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        if (!m.find()) {
            return null;
        }

        String found = m.group();

        found = found.replaceAll("[A-Za-z]", "");
        int openBracket = found.indexOf('[');
        int closeBracket = found.lastIndexOf("])");

        found = found.substring(openBracket + 1, closeBracket);

        return found;

    }

    private static boolean isLinux() {
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        return os.equals("linux");
    }
}
