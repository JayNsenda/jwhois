package tech.myic.jwhois.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressReader
{
    private IpAddressReader()
    {
    }

    public static Optional<String> getIpAddressFromEmailSource(String text)
    {
        if (text == null || text.isEmpty()){
            throw new RuntimeException("No text provided");
        }

        String regex = "(Received:).*(\\s*\\[\\d*.\\d*.\\d*.\\d*]\\))";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        if (!m.find()){
            return Optional.empty();
        }

        String found = m.group();

        found = found.replaceAll("[A-Za-z]", "");

        int openBracket = found.indexOf('[');
        int closeBracket = found.lastIndexOf("])");

        found = found.substring(openBracket + 1, closeBracket);

        return Optional.of(found);

    }
}
