package tech.myic.jwhois;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jules
 */
public class App {

    public static void main(String[] args) {
        boolean isLinux = isLinux();
        if (!isLinux) {
            throw new RuntimeException("The code is not supported on: [" + System.getProperty("os.name") + "] operating system yet.");
        }

        String in = args[0];

        try {
            if (in.contains("ef=")) {
                in = in.substring(in.indexOf("ef=") + 3);
                try {
                    usingEmailFile(in);
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Unable to get email details using email file", ex);
                }
            } else if (in.contains("ip=")) {
                in = in.substring(in.indexOf("ip=") + 3);
                try {
                    usingIPAdress(in);
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Unable to get email details using IP address", ex);
                }
            } else if (in.contains("-te")) {
                try {
                    usingSourceEmail();
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException("Unable to get email details using email source", ex);
                }
            } else if ("?".equals(in) || in.equals("--help")) {
                System.out.println("Welcome to jwhois.\n"
                        + "==========================================================\n"
                        + "This program allows you to see source details of an email\n"
                        + "you have received either in your inbox or spam.\n"
                        + "You can pass in:\n"
                        + "\t a. an email(.eml) file from your local computer: \n"
                        + "\t    This would be the actual email file\n"
                        + "\t ** the argument will be: ef=<path_to_the_eml_file>\n"
                        + "\t b. an IP address: This is the IP address (IPv4) of \n"
                        + "\t    the email\n"
                        + "\t ** the argument will be: ip=<the_ip_address>\n"
                        + "\t c. the source email or orignail email. You can google\n"
                        + "\t    to know how to get this :)\n"
                        + "\t ** the arcgument will be: -te, press enter then paste\n"
                        + "\t    the<email_source> and preess Ctrl+D on your keyboard\n"
                        + "==========================================================\n"
                        + "PRE-REQUESITE\n"
                        + "In order to use this program, please make sure you are:\n"
                        + "\t 1. Running a Linus machine,\n"
                        + "\t 2. Have installed the whois command.\n"
                        + "==========================================================\n"
                        + "EXAMPLE ON RUNNING THE PROGRAM\n"
                        + "Eg.1: Running the program with email file:\n"
                        + "\t java -jar jwhois ef=/home/john/Download/example.eml\n"
                        + "Eg.2: Running the program with IP address:\n"
                        + "\t java -jar jwhois ip=193.0.0.1\n"
                        + "Eg.3: Running the program with source email:\n"
                        + "\t java -jar jwhois ef=/home/john/Download/example.eml\n"
                        + "Enjoy!");
                System.exit(1);
            } else if (args.length > 1) {
                System.err.println("Enter only 1 params in order to proceed \n"
                        + "Please type: ? or --help to get help");
                System.exit(1);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Incorrect parameters entered", e);
        }
    }

    private static void displayEmailDetailUsingIp(String ip) throws InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("whois", ip);

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
        System.out.println("==========================================================================");
        System.out.println(sb.toString());
        System.out.println("==========================================================================");
    }

    private static void usingEmailFile(String firstArg) throws InterruptedException {
        displayEmailDetailUsingIp(getIpAddressFromEmailSource(getSourceFromEmailFile(firstArg)));
    }

    private static void usingIPAdress(String firstArg) throws InterruptedException {
        displayEmailDetailUsingIp(firstArg);
    }

    private static void usingSourceEmail() throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String line;

            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        String inText = sb.toString();

        String ip = getIpAddressFromEmailSource(inText);

        displayEmailDetailUsingIp(ip);

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
