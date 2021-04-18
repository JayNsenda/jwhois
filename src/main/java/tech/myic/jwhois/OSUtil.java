package tech.myic.jwhois;

public class OSUtil
{
    private OSUtil()
    {
    }

    public static boolean isLinux()
    {
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        return os.equals("linux");
    }
}
