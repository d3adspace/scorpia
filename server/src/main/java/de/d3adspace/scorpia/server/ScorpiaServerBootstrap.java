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

package de.d3adspace.scorpia.server;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import de.d3adspace.caladrius.Caladrius;
import de.d3adspace.caladrius.CaladriusImpl;
import de.d3adspace.scorpia.server.config.ScorpiaServerConfig;
import de.d3adspace.scorpia.server.mode.ServerMode;
import de.d3adspace.scorpia.server.tcp.ScorpiaTCPServer;
import de.d3adspace.scorpia.server.udp.ScorpiaUDPServer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ScorpiaServerBootstrap {

    private static final Path CONFIG_PATH = Paths.get("scorpia-config.yml");

    public static void main(String[] args) {

        ScorpiaServerConfig serverConfig;

        if (Files.exists(CONFIG_PATH)) {
            Caladrius caladrius = new CaladriusImpl();
            serverConfig = caladrius.readConfig(ScorpiaServerConfig.class, CONFIG_PATH);
        } else {
            serverConfig = ScorpiaServerConfig.fromEnvironment();
        }

        ServerMode mode = serverConfig.getMode();
        String serverHost = serverConfig.getServerHost();
        int serverPort = serverConfig.getServerPort();

        ScorpiaServer scorpiaServer;

        if (mode == ServerMode.TCP) {
            scorpiaServer = ScorpiaTCPServer.create(serverHost, serverPort);
        } else if (mode == ServerMode.UDP){
            scorpiaServer = ScorpiaUDPServer.create(serverHost, serverPort);
        } else {
            System.out.println("Unsupported server mode");
            System.exit(1);
            return;
        }

        ListenableFuture<Boolean> start = scorpiaServer.start();
        Futures.addCallback(start, new FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable Boolean aBoolean) {
                System.out.println("Server started");
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, MoreExecutors.directExecutor());

        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
    }
}
