package socket;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

public class clientTCP {

    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 6543;

    //luồng tiến trình chung
    private static TCPThread TCPThread;

    private static TextArea showUp;

    public static void run() {
        try {

            //kết nối tới server
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            //khởi tạo thread cho socket này
            TCPThread = new TCPThread(socket, showUp);
            TCPThread.setType(2);//giúp phân biêt thread này là của client
            TCPThread.start();
        } catch (IOException ie) {
            System.out.println("Can't connect to server");
        }
    }

    public static TCPThread getTCPThread() {
        return TCPThread;
    }

    public static void setTCPThread(TCPThread TCPThread) {
        clientTCP.TCPThread = TCPThread;
    }

    public static TextArea getShowUp() {
        return showUp;
    }

    public static void setShowUp(TextArea showUp) {
        clientTCP.showUp = showUp;
    }

}