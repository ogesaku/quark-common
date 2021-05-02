package com.coditory.quark.common.uri;

import com.coditory.quark.common.util.Objects;
import com.coditory.quark.common.util.Strings;
import com.coditory.quark.common.check.Args;
import com.coditory.quark.common.net.Ports;
import org.jetbrains.annotations.Nullable;

import static com.coditory.quark.common.check.Args.checkNoBlanks;
import static com.coditory.quark.common.net.Ports.SCHEME_DEFAULT_PORT_NUMBER;
import static com.coditory.quark.common.net.Ports.checkPortNumberOrSchemeDefault;

public final class UriAuthority {
    private static final UriAuthority EMPTY = new UriAuthority(null, null, SCHEME_DEFAULT_PORT_NUMBER);

    public static UriAuthority empty() {
        return EMPTY;
    }

    public static UriAuthority of(@Nullable String userInfo, @Nullable String hostname, int port) {
        if (hostname != null) {
            checkNoBlanks(hostname, "hostname");
        }
        if (userInfo != null) {
            Args.checkNotEmpty(userInfo, "userInfo");
        }
        checkPortNumberOrSchemeDefault(port);
        if (userInfo == null && hostname == null && Ports.isSchemeDefault(port)) {
            return EMPTY;
        }
        return new UriAuthority(
                userInfo,
                Objects.mapNotNull(hostname, Strings::lowerCase),
                port
        );
    }

    private final String userInfo;
    private final String hostname;
    private final int port;

    private UriAuthority(@Nullable String userInfo, @Nullable String hostname, int port) {
        this.userInfo = userInfo;
        this.hostname = hostname;
        this.port = port;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public String toString() {
        return "UriAuthority{" +
                "userInfo='" + userInfo + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UriAuthority that = (UriAuthority) o;
        return port == that.port && Objects.equals(userInfo, that.userInfo) && Objects.equals(hostname, that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfo, hostname, port);
    }
}
