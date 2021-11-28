package socket;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class serverTCP {

    public final static int SERVER_PORT = 6543;

    //danh sách các tiến trình đang được socket kết nối tới
    private static List<TCPThread> listThread;

    private static TextArea showUp;

    public static void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);//Khoi tao socket phia server
            listThread = new ArrayList<>();
            while (true) {

                //chờ phía client kết nối
                Socket socket = serverSocket.accept();

                //khởi tạo thread cho phía server
                TCPThread tempThread = new TCPThread(socket, showUp);

                //phân loại cho thread
                tempThread.setType(1);
                listThread.add(tempThread);
                tempThread.start();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static List<TCPThread> getListThread() {
        return listThread;
    }

    public static void setListThread(List<TCPThread> listThread) {
        serverTCP.listThread = listThread;
    }

}