package tech.myic.jwhois.processor;

import java.io.IOException;

public interface Processor
{
    void process(String path)
            throws InterruptedException, IOException;
}
