package com.travel.tracker.SocketClient;

import com.travel.tracker.gloabls.Global;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by mTech on 18-Apr-2017.IO.
 */
public class SC_IOApplication {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Global.SOCKET_URL);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}