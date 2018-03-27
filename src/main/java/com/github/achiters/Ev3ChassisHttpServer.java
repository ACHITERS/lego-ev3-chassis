package com.github.achiters;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Ev3ChassisHttpServer extends NanoHTTPD {

    private static final int HTTP_PORT = 8080;

    private static final String LEFT_CMD_URL = "/left";
    private static final String RIGHT_CMD_URL = "/right";
    private static final String FORWARD_CMD_URL = "/forward";
    private static final String BACK_CMD_URL = "/back";
    private static final String DONE = "Done";

    private final ChassisCtrl chassisCtrl;

    private Ev3ChassisHttpServer(String host, int port, ChassisCtrl chassisCtrl) {
        super(host, port);
        this.chassisCtrl = chassisCtrl;
    }

    public static void main(String[] args) {
        ChassisCtrl chassisCtrl = new ChassisCtrl();

        try {

            String ipAddress = null;

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                if (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ipAddress = addr.getHostAddress();
                    break;
                }
            }

            if (ipAddress == null) {
                throw new RuntimeException("Non localhost IP Address not found");
            }


            Ev3ChassisHttpServer server = new Ev3ChassisHttpServer(ipAddress, HTTP_PORT, chassisCtrl);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            throw new RuntimeException("Server not started", e);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() != Method.GET) {
            return notFound();
        }

        switch (session.getUri()) {
            case LEFT_CMD_URL:
                chassisCtrl.turnLeft();
                break;
            case RIGHT_CMD_URL:
                chassisCtrl.turnRight();
                break;
            case FORWARD_CMD_URL:
                chassisCtrl.moveForward();
                break;
            case BACK_CMD_URL:
                chassisCtrl.moveBack();
                break;
            default:
                return notFound();
        }

        return newFixedLengthResponse(DONE);
    }

    private Response notFound() {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "");
    }
}
