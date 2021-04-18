package tech.myic.jwhois;

/**
 * @author jules
 */
public class App
{

    public static void main(String[] args)
    {
        boolean isLinux = OSUtil.isLinux();
        if (!isLinux){
            throw new RuntimeException("The code is not supported on: [" + System.getProperty("os.name") + "] operating system yet.");
        }

        String in = args[0];
        Helper.printHelp(args, in);
        Processor.process(in);
    }

}
