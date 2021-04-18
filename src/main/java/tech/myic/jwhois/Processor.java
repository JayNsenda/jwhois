package tech.myic.jwhois;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor
{
    private Processor()
    {
    }

    public static void process(String in)
    {
        if (in.contains("ef=")){
            in = in.substring(in.indexOf("ef=") + 3);
            try {
                Processor.usingEmailFile(in);
            }catch (InterruptedException ex){
                throw new RuntimeException("Unable to get email details using email file", ex);
            }
        }else if (in.contains("ip=")){
            in = in.substring(in.indexOf("ip=") + 3);
            try {
                Processor.usingIPAddress(in);
            }catch (InterruptedException ex){
                throw new RuntimeException("Unable to get email details using IP address", ex);
            }
        }else if (in.contains("-te")){
            try {
                Processor.usingSourceEmail();
            }catch (IOException | InterruptedException ex){
                throw new RuntimeException("Unable to get email details using email source", ex);
            }
        }
    }

    private static void displayEmailDetailUsingIp(String ip)
            throws InterruptedException
    {
        ProcessBuilder pb = new ProcessBuilder("whois", ip);

        Process process;
        try {
            process = pb.start();
        }catch (IOException ex){
            throw new RuntimeException("Unable to process command", ex);
        }

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
        }catch (IOException ex){
            throw new RuntimeException("Error reading results from command", ex);
        }

        if (process.waitFor() != 0){
            throw new RuntimeException("Unable to get result from command");
        }
        System.out.println("==========================================================================");
        System.out.println(sb);
        System.out.println("==========================================================================");
    }

    public static void usingEmailFile(String firstArg)
            throws InterruptedException
    {
        displayEmailDetailUsingIp(getIpAddressFromEmailSource(getSourceFromEmailFile(firstArg)));
    }

    public static void usingIPAddress(String firstArg)
            throws InterruptedException
    {
        displayEmailDetailUsingIp(firstArg);
    }

    public static void usingSourceEmail()
            throws IOException, InterruptedException
    {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String line;

            while ((line = r.readLine()) != null){
                sb.append(line).append("\n");
            }
        }

        String inText = sb.toString();

        String ip = getIpAddressFromEmailSource(inText);

        displayEmailDetailUsingIp(ip);

    }

    private static String getSourceFromEmailFile(String pathToEmlFile)
    {
        if (!pathToEmlFile.toLowerCase().endsWith(".eml")){
            throw new RuntimeException("File is not an email");
        }

        StringBuilder sb;
        try {
            try (BufferedReader fr = new BufferedReader(new FileReader(pathToEmlFile))) {
                String line;
                sb = new StringBuilder();
                while ((line = fr.readLine()) != null){
                    sb.append(line).append("\n");
                }
            }
        }catch (IOException ex){
            throw new RuntimeException("Error reading file: " + pathToEmlFile, ex);
        }

        return sb.toString();
    }

    private static String getIpAddressFromEmailSource(String text)
    {
        String regex = "(Received:).*(\\s\\[\\d*.\\d*.\\d*.\\d*]\\))";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        if (!m.find()){
            return null;
        }

        String found = m.group();

        found = found.replaceAll("[A-Za-z]", "");
        int openBracket = found.indexOf('[');
        int closeBracket = found.lastIndexOf("])");

        found = found.substring(openBracket + 1, closeBracket);

        return found;

    }
}
