/*
 * MIT License
 *
 * Copyright (c) 2019 d3adspace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.d3adspace.scorpia.server.config;

import com.google.common.base.Preconditions;
import de.d3adspace.caladrius.annotation.Config;
import de.d3adspace.caladrius.annotation.Key;
import de.d3adspace.caladrius.config.ConfigType;
import de.d3adspace.scorpia.server.mode.ServerMode;

@Config(name = "scorpia-config", type = ConfigType.YAML)
public class ScorpiaServerConfig {

    private static final String ENV_SERVER_MODE = "REVERSE_PROXY_TYPE";
    private static final String ENV_SERVER_HOST = "REVERSE_PROXY_HOST";
    private static final String ENV_SERVER_PORT = "REVERSE_PROXY_PORT";

    @Key("mode")
    private ServerMode mode;

    @Key("server.host")
    private String serverHost;

    @Key("server.port")
    private int serverPort;

    public ScorpiaServerConfig() {
    }

    public ScorpiaServerConfig(ServerMode mode, String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        Preconditions.checkNotNull(mode, "Mode may not be null.");

        this.mode = mode;
    }

    public static ScorpiaServerConfig fromEnvironment() {
        ServerMode serverMode = ServerMode.valueOf(System.getenv(ENV_SERVER_MODE));
        String serverHost = System.getenv(ENV_SERVER_HOST);
        int serverPort = Integer.parseInt(System.getenv(ENV_SERVER_PORT));

        return ScorpiaServerConfig.of(serverMode, serverHost, serverPort);
    }

    private static ScorpiaServerConfig of(ServerMode serverMode, String serverHost, int serverPort) {
        Preconditions.checkNotNull(serverMode);
        Preconditions.checkNotNull(serverHost);

        return new ScorpiaServerConfig(serverMode, serverHost, serverPort);
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public ServerMode getMode() {
        return mode;
    }
}
