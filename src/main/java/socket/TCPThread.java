package socket;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

public class TCPThread extends Thread {

    public DataInputStream inputStream = null;
    public DataOutputStream outputStream = null;
    private Socket socket;
    private TextArea showUp;

    /*
    phan biet server hay client
    1 la server
    2 la client
     */
    private int type = 0;

    public TCPThread(Socket socket, TextArea showUp) {
        this.socket = socket;
        this.showUp = showUp;
    }

    public void run() {
        try {

            //khởi tạo tiến trình gửi và nhận
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            //tạo 1 thread để luôn luôn nhận dữ liệu từ server/client
            new Thread(this::receiveData).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        try {
            String temp;
            while ((temp = inputStream.readUTF()) != null) {
                System.out.println("da nhan du lieu");

                //xét xem là server hay client, 1 là sv, 2 là cl
                if(type == 1) {
                    int count = serverTCP.getListThread().size();

                    //gửi toàn bộ dữ liệu vừa nhận được từ client đến client khác
                    for(int i=0;i<count;i++) {
                        if(this.socket != serverTCP.getListThread().get(i).getSocket()) {
                            serverTCP.getListThread().get(i).sendData(temp);
                        }
                    }
                } else if(type==2) {

                    //xét xem có phải câu lệnh thay đổi tên hay ko
                    if(temp.contains("-changeName:")) {
                        String[] str = temp.split(" ");
                        showUp.setText(showUp.getText().replaceAll(str[1]+":",str[2]+":"));
                    }
                    else {
                        showUp.setText(showUp.getText() + temp + "\n");//đưa dữ liệu vừa nhận lên lịch sử chat
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //gửi dữ liệu đi
    public void sendData(String data) {
        try {
            outputStream.writeUTF(data);
            System.out.println("da gui du lieu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
