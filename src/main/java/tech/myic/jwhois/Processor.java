package tech.myic.jwhois;

import java.io.IOException;

public interface Processor
{
    void process(String path)
            throws InterruptedException, IOException;
}
