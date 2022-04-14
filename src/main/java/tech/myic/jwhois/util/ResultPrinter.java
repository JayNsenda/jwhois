package tech.myic.jwhois.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResultPrinter
{
    private ResultPrinter()
    {
    }

    public static void print(String text)
            throws InterruptedException
    {
        ProcessBuilder pb = new ProcessBuilder("whois", text);

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
}
