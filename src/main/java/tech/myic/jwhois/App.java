package tech.myic.jwhois;

import java.io.IOException;
import tech.myic.jwhois.util.Helper;
import tech.myic.jwhois.util.OsChecker;

public class App
{
    public static void main(String[] args)
    {
        boolean isLinux = OsChecker.isLinux();
        if (!isLinux){
            throw new RuntimeException("The code is not supported on: [" + System.getProperty("os.name") + "] operating system yet.");
        }

        String in = args[0];

        Helper.print(args, in);

        if (in.contains("ef=")){
            in = in.substring(in.indexOf("ef=") + 3);
            try {
                new EmailFileProcessor().process(in);
            }catch (InterruptedException | IOException ex){
                throw new RuntimeException("Unable to get email details using email file", ex);
            }
        }else if (in.contains("ip=")){
            in = in.substring(in.indexOf("ip=") + 3);
            try {
                new IpAddressProcess().process(in);
            }catch (InterruptedException | IOException ex){
                throw new RuntimeException("Unable to get email details using IP address", ex);
            }
        }else if (in.contains("-te")){
            try {
                new EmailSourceProcessor().process(null);
            }catch (IOException | InterruptedException ex){
                throw new RuntimeException("Unable to get email details using email source", ex);
            }
        }

    }

}
