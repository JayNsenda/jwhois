package tech.myic.jwhois.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import tech.myic.jwhois.util.IpAddressReader;
import tech.myic.jwhois.util.ResultPrinter;

public class EmailSourceProcessor
        implements Processor
{
    @Override
    public void process(String path)
            throws InterruptedException, IOException
    {
        StringBuilder sb = new StringBuilder();

        try (InputStreamReader streamReader = new InputStreamReader(System.in, StandardCharsets.UTF_8)) {
            try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line).append("\n");
                }
            }

            String inText = sb.toString();

            String ip = IpAddressReader.getIpAddressFromEmailSource(inText)
                    .orElseThrow(RuntimeException::new);

            ResultPrinter.print(ip);
        }
    }
}
