package tech.myic.jwhois.util;

public class OsChecker
{
    private OsChecker()
    {
    }

    public static boolean isLinux()
    {
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        return os.equals("linux");
    }
}
