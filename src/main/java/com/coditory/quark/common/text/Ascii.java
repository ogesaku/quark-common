package com.coditory.quark.common.text;

public final class Ascii {
    private Ascii() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }
    /**
     * Null
     */
    public static final char NUL = 0;
    /**
     * Start of Heading
     */
    public static final char SOH = 1;
    /**
     * Start of Text
     */
    public static final char STX = 2;
    /**
     * End of Text
     */
    public static final char ETX = 3;
    /**
     * End of Transmission
     */
    public static final char EOT = 4;
    /**
     * Enquiry
     */
    public static final char ENQ = 5;
    /**
     * Acknowledgement
     */
    public static final char ACK = 6;
    /**
     * Bell - {@code \a}
     */
    public static final char BEL = 7;
    /**
     * Backspace - {@code \b}
     */
    public static final char BS = 8;
    /**
     * Backspace - {@code \b}
     */
    public static final char HT = 9;
    /**
     * Line Feed - {@code \n}
     */
    public static final char LF = 10;
    /**
     * Vertical Tab - {@code \v}
     */
    public static final char VT = 11;
    /**
     * Form Feed - {@code \f}
     */
    public static final char FF = 12;
    /**
     * Carriage Return - {@code \r}
     */
    public static final char CR = 13;
    /**
     * Shift Out
     */
    public static final char SO = 14;
    /**
     * Shift In
     */
    public static final char SI = 15;
    /**
     * Data Link Escape
     */
    public static final char DLE = 16;
    /**
     * Denice Control 1
     */
    public static final char DC1 = 17;
    /**
     * Denice Control 2
     */
    public static final char DC2 = 18;
    /**
     * Denice Control 3
     */
    public static final char DC3 = 19;
    /**
     * Denice Control 4
     */
    public static final char DC4 = 20;
    /**
     * Negative Acknowledgement
     */
    public static final char NAK = 21;
    /**
     * Synchronous Idle
     */
    public static final char SYN = 22;

    /**
     * End of Transmission Block
     */
    public static final char ETB = 23;
    /**
     * Cancel
     */
    public static final char CAN = 24;
    /**
     * End of Medium
     */
    public static final char EM = 25;
    /**
     * Substitute
     */
    public static final char SUB = 26;
    /**
     * Escape - {@code \e}
     */
    public static final char ESC = 27;
    /**
     * File Separator
     */
    public static final char FS = 28;
    /**
     * Group Separator
     */
    public static final char GS = 29;
    /**
     * Record Separator
     */
    public static final char RS = 30;
    /**
     * Unit Separator
     */
    public static final char US = 31;
    /**
     * Delete
     */
    public static final char DEL = 127;

    public static boolean isAsciiPrintable(char c) {
        return c >= 32 && c < 127;
    }

    public static boolean isAsciiPrintable(int c) {
        return c >= 32 && c < 127;
    }

    public static boolean isAsciiControlCode(char c) {
        return c < 32 || c == 127;
    }

    public static boolean isAsciiControlCode(int c) {
        return c >= 0 && (c < 32 || c == 127);
    }

    public static boolean isAscii(int c) {
        return c >= 0 && c <= 127;
    }
}
