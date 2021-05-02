package com.coditory.quark.common.net;

import com.coditory.quark.common.check.Args;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public final class Ports {
    private Ports() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public final static int SCHEME_DEFAULT_PORT_NUMBER = -1;
    public final static int MIN_PORT_VALUE = 0;
    public final static int MAX_PORT_VALUE = 65535;

    public static boolean isValidPortNumberOrSchemeDefault(int port) {
        return port >= SCHEME_DEFAULT_PORT_NUMBER && port <= MAX_PORT_VALUE;
    }

    public static boolean isValidPortNumber(int port) {
        return port >= MIN_PORT_VALUE && port <= MAX_PORT_VALUE;
    }

    public static boolean isSchemeDefault(int port) {
        return port == SCHEME_DEFAULT_PORT_NUMBER;
    }

    public static int checkPortNumberOrSchemeDefault(int port) {
        Args.check(
                isValidPortNumberOrSchemeDefault(port),
                "Expected port number in range [%d, %d]. Got: %d",
                SCHEME_DEFAULT_PORT_NUMBER, MAX_PORT_VALUE, port
        );
        return port;
    }

    public static int checkPortNumber(int port) {
        Args.check(
                isValidPortNumber(port),
                "Expected port number in range [%d, %d]. Got: %d",
                MIN_PORT_VALUE, MAX_PORT_VALUE, port
        );
        return port;
    }

    public static int nextAvailablePort() {
        try {
            try (ServerSocket socket = new ServerSocket(0)) {
                return socket.getLocalPort();
            }
        } catch (IOException exception) {
            throw new PortNotAvailable("Could not get free port", exception);
        }
    }

    public static int nextAvailablePort(int min, int max) {
        for (int port = min; port <= max; ++port) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        throw new PortNotAvailable("Could not get free port between: " + min + " and " + max);
    }

    public static boolean isPortAvailable(int port) {
        try {
            try (ServerSocket socket = new ServerSocket(port)) {
                socket.setReuseAddress(true);
                try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
                    datagramSocket.setReuseAddress(true);
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static class PortNotAvailable extends RuntimeException {
        public PortNotAvailable(String message) {
            super(message);
        }

        public PortNotAvailable(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
