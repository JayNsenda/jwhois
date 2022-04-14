package tech.myic.jwhois.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import tech.myic.jwhois.util.IpAddressReader;
import tech.myic.jwhois.util.ResultPrinter;

public class EmailFileProcessor
        implements Processor
{
    @Override
    public void process(String path)
            throws InterruptedException, IOException
    {
        if (!Files.probeContentType(Paths.get(path)).equals("message/rfc822")){
            throw new RuntimeException("File is not an email");
        }

        StringBuilder sb;
        try {
            try (BufferedReader fr = new BufferedReader(new FileReader(path))) {
                String line;
                sb = new StringBuilder();
                while ((line = fr.readLine()) != null){
                    sb.append(line).append("\n");
                }
            }
        }catch (IOException ex){
            throw new RuntimeException("Error reading file: " + path, ex);
        }

        String ipAddress = IpAddressReader.getIpAddressFromEmailSource(sb.toString())
                .orElseThrow(RuntimeException::new);

        ResultPrinter.print(ipAddress);

    }
}
