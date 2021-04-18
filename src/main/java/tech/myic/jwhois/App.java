package tech.myic.jwhois;

import tech.myic.jwhois.util.Helper;
import tech.myic.jwhois.util.OsChecker;
import tech.myic.jwhois.util.Processor;

/**
 * @author jules
 */
public class App
{

    public static void main(String[] args)
    {
        boolean isLinux = OsChecker.isLinux();
        if (!isLinux){
            throw new RuntimeException("The code is not supported on: [" + System.getProperty("os.name") + "] operating system yet.");
        }

        String in = args[0];
        Helper.printHelp(args, in);
        Processor.process(in);
    }

}
