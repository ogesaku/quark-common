package com.coditory.quark.common.net;

import com.coditory.quark.common.util.Strings;
import com.coditory.quark.common.util.Integers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InetAddressValidator {
    private InetAddressValidator() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    private static final int MAX_BYTE = 128;
    private static final int IPV4_MAX_OCTET_VALUE = 255;
    private static final int MAX_UNSIGNED_SHORT = 0xffff;
    private static final int BASE_16 = 16;
    private static final Pattern IPV4_REGEX = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
    private static final int IPV6_MAX_HEX_GROUPS = 8;
    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;

    public static boolean isValidInetAddress(String inetAddress) {
        return isValidInet4Address(inetAddress) || isValidInet6Address(inetAddress);
    }

    public static boolean isValidInet4Address(String inet4Address) {
        Matcher matcher = IPV4_REGEX.matcher(inet4Address);
        if (!matcher.matches()) {
            return false;
        }
        for (int j = 0; j < matcher.groupCount(); j++) {
            String ipSegment = matcher.group(j + 1);
            if (Strings.isNullOrBlank(ipSegment)) {
                return false;
            }
            if (ipSegment.startsWith("0")) {
                return false;
            }
            Integer iIpSegment = Integers.parseIntegerOrNull(ipSegment);
            if (iIpSegment == null || iIpSegment > IPV4_MAX_OCTET_VALUE) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidInet6Address(String inet6Address) {
        String[] parts;
        parts = inet6Address.split("/", -1);
        if (parts.length > 2) {
            return false;
        }
        if (parts.length == 2) {
            if (parts[1].matches("\\d{1,3}")) {
                int bits = Integer.parseInt(parts[1]);
                if (bits < 0 || bits > MAX_BYTE) {
                    return false;
                }
            } else {
                return false;
            }
        }
        parts = parts[0].split("%", -1);
        if (parts.length > 2) {
            return false;
        } else if (parts.length == 2){
            if (!parts[1].matches("[^\\s/%]+")) {
                return false;
            }
        }
        inet6Address = parts[0];
        boolean containsCompressedZeroes = inet6Address.contains("::");
        if (containsCompressedZeroes && (inet6Address.indexOf("::") != inet6Address.lastIndexOf("::"))) {
            return false;
        }
        if ((inet6Address.startsWith(":") && !inet6Address.startsWith("::"))
                || (inet6Address.endsWith(":") && !inet6Address.endsWith("::"))) {
            return false;
        }
        String[] octets = inet6Address.split(":");
        if (containsCompressedZeroes) {
            List<String> octetList = new ArrayList<>(Arrays.asList(octets));
            if (inet6Address.endsWith("::")) {
                octetList.add("");
            } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
                octetList.remove(0);
            }
            octets = octetList.toArray(new String[octetList.size()]);
        }
        if (octets.length > IPV6_MAX_HEX_GROUPS) {
            return false;
        }
        int validOctets = 0;
        int emptyOctets = 0; // consecutive empty chunks
        for (int index = 0; index < octets.length; index++) {
            String octet = octets[index];
            if (octet.length() == 0) {
                emptyOctets++;
                if (emptyOctets > 1) {
                    return false;
                }
            } else {
                emptyOctets = 0;
                if (index == octets.length - 1 && octet.contains(".")) {
                    if (!isValidInet4Address(octet)) {
                        return false;
                    }
                    validOctets += 2;
                    continue;
                }
                if (octet.length() > IPV6_MAX_HEX_DIGITS_PER_GROUP) {
                    return false;
                }
                int octetInt = 0;
                try {
                    octetInt = Integer.parseInt(octet, BASE_16);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (octetInt < 0 || octetInt > MAX_UNSIGNED_SHORT) {
                    return false;
                }
            }
            validOctets++;
        }
        return validOctets <= IPV6_MAX_HEX_GROUPS
                && (validOctets >= IPV6_MAX_HEX_GROUPS || containsCompressedZeroes);
    }
}
