package tech.myic.jwhois.processor;

import java.io.IOException;
import tech.myic.jwhois.util.ResultPrinter;

public class IpAddressProcess
        implements Processor
{
    @Override
    public void process(String ip)
            throws InterruptedException, IOException
    {
        ResultPrinter.print(ip);
    }
}
