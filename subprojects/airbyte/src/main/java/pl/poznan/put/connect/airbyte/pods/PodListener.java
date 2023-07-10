package pl.poznan.put.connect.airbyte.pods;

import org.slf4j.Logger;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

public class PodListener {
    private static final Logger LOGGER = getLogger(PodListener.class);

    private InputStream stdOut = InputStream.nullInputStream();
    private InputStream stdErr = InputStream.nullInputStream();

    private ServerSocket stdOutServerSocket;
    private ServerSocket stdErrServerSocket;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    PodListener(Integer stdOutPort, Integer stdErrPort) {
        setupStdOutSocket(stdOutPort);
        setupStdErrSocket(stdErrPort);

    }

    private void setupStdOutSocket(Integer port) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        executorService.submit(() -> {
            MDC.setContextMap(context);
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                stdOutServerSocket = serverSocket;
                LOGGER.info("Creating STDOUT socket server on port {}", port);
                Socket socket = serverSocket.accept();
                LOGGER.info("STDOUT port {} accepted", port);
                this.stdOut = socket.getInputStream();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupStdErrSocket(Integer port) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        executorService.submit(() -> {
            MDC.setContextMap(context);
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                stdErrServerSocket = serverSocket;
                LOGGER.info("Creating STDERR socket server on port {}", port);
                Socket socket = serverSocket.accept();
                LOGGER.info("STDERR port {} accepted", port);
                this.stdErr = socket.getInputStream();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void closeSockets(){
        try {
            stdOutServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            stdErrServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getStdOut() {
        return stdOut;
    }

    public InputStream getStdErr() {
        return stdErr;
    }
}
