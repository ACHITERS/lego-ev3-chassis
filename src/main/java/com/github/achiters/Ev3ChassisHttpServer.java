package com.github.achiters;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class Ev3ChassisHttpServer extends NanoHTTPD {

    private static final int HTTP_PORT = 8080;
    private static final String HTTP_HOST = "localhost";

    private static final String LEFT_CMD_URL = "/left";
    private static final String RIGHT_CMD_URL = "/right";
    private static final String FORWARD_CMD_URL = "/forward";
    private static final String BACK_CMD_URL = "/back";
    private static final String DONE = "Done";

    private final ChassisCtrl chassisCtrl;

    private Ev3ChassisHttpServer(ChassisCtrl chassisCtrl) {
        super(HTTP_HOST,HTTP_PORT);
        this.chassisCtrl = chassisCtrl;
    }

    public static void main(String[] args) {
//        ChassisCtrl chassisCtrl = new ChassisCtrl();

        try {
//            Ev3ChassisHttpServer server = new Ev3ChassisHttpServer(chassisCtrl);
            Ev3ChassisHttpServer server = new Ev3ChassisHttpServer(null);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            System.err.println("Couldn't start server:\n" + e);
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
