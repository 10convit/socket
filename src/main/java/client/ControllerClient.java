package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import socket.clientTCP;

public class ControllerClient implements Initializable {

    @FXML
    AnchorPane pane;
    @FXML
    ComboBox<String> option;
    @FXML
    TextField input;
    @FXML
    TextArea showUp;
    @FXML
    Text name;

    //màu sắc của giao diện
    private String colorshowUp1 = "#d5f6ff";
    private String colorshowUp2 = "#ffffff";
    private String colorTextshowUp = "black";
    private String colorinput = "black";

    //tên người gửi(client)
    private String senderName = "123";

    String[] opt = {"Chọn chức năng", "Đổi tên", "Đổi màu chữ", "Đổi giao diện"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showUp.setWrapText(true);
        name.setText(senderName);
        ImageInput img = new ImageInput();
        img.setSource(new Image("/option.png"));
        option.setEffect(img);
        option.setItems(FXCollections.observableArrayList(opt));
        option.setOnAction(actionEvent -> optionButton());
        clientTCP.setShowUp(showUp);
        System.out.println("da set xong showUp");
        new Thread(this::runClient).start();
    }

    //Lấy dữ liệu từ ô chat gửi cho server
    public void getInputData() {
        String data = input.getText();
        input.clear();
        String temp = "Tôi: " + data + "\n";
        showUp.setText(showUp.getText() + temp);
        clientTCP.getTCPThread().sendData(senderName + ": " + data);
    }

    public void runClient() {
        try {
            clientTCP.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Thay đổi giao diện
    public void changeButton(int type) {
        Dialog<Integer> dialog = new Dialog<>();
        if (type == 1) {
            dialog.setHeaderText("Nhập tên");
        } else if (type == 2) {
            dialog.setHeaderText("Chọn màu chữ");
        } else if (type == 3) {
            dialog.setHeaderText("Chọn giao diện");
        }
        ButtonType checkContinueButton = new ButtonType("OK!", ButtonBar.ButtonData.YES);
        dialog.getDialogPane().getButtonTypes().addAll(checkContinueButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));
        if (type == 1) {
            TextField temp = new TextField();
            grid.add(temp, 0, 0);
            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(button -> {
                String tempName = temp.getText();
                if(!tempName.equals("")) {

                    //gửi gói tin để báo cho client còn lại biết đã thay đổi tên
                    clientTCP.getTCPThread().sendData("-changeName: " + senderName + " " + tempName);
                    senderName = tempName;
                    name.setText(senderName);
                }
                return 1;
            });
        } else if (type == 2) {

            //thay đổi màu của chữ
            ChoiceBox<String> temp = new ChoiceBox<>();
            temp.getItems().addAll("Cam", "Vàng", "Lục", "Lam", "Đen");
            temp.setValue("Đen");
            grid.add(temp, 0, 0);
            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(button -> {
                switch (temp.getValue()) {
                    case "Cam" -> {
                        colorTextshowUp = "#ffbb00";
                        colorinput = "#ffbb00";
                        changeColor();
                    }
                    case "Vàng" -> {
                        colorTextshowUp = "#e3db00";
                        colorinput = "#e3db00";
                        changeColor();
                    }
                    case "Lục" -> {
                        colorTextshowUp = "#00db07";
                        colorinput = "#00db07";
                        changeColor();
                    }
                    case "Lam" -> {
                        colorTextshowUp = "#0088ff";
                        colorinput = "#0088ff";
                        changeColor();
                    }
                    case "Đen" -> {
                        colorTextshowUp = "#000000";
                        colorinput = "#000000";
                        changeColor();
                    }
                }
                return 1;
            });
        } else if (type == 3) {

            //thay đổi giao diện(sáng hoặc tối)
            ChoiceBox<String> temp = new ChoiceBox<>();
            temp.getItems().addAll("Sáng", "Tối");
            temp.setValue("Sáng");
            grid.add(temp, 0, 0);
            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(button -> {
                if(temp.getValue().equals("Sáng")) {
                    name.setFill(Color.BLACK);
                    pane.setStyle("-fx-background-color: linear-gradient(from 0px 0px to 0px 52px, #a3e8fa, #ffffff)");
                    colorshowUp1 = "#d5f6ff";
                    colorshowUp2 = "#ffffff";
                    changeColor();
                } else if (temp.getValue().equals("Tối")) {
                    name.setFill(Color.WHITE);
                    pane.setStyle("-fx-background-color: linear-gradient(from 0px 0px to 0px 52px, #f519ff, #404040)");
                    colorshowUp1 = "#e667eb";
                    colorshowUp2 = "#3b3b42";
                    colorTextshowUp = "WHITE";
                    changeColor();
                }
                return 1;
            });
        }
        dialog.showAndWait();
    }

    //hàm xử lý combobox
    public void optionButton() {
        switch (option.getValue()) {
            case "Đổi tên" -> changeButton(1);
            case "Đổi màu chữ" -> changeButton(2);
            case "Đổi giao diện" -> changeButton(3);
        }
    }

    //hàm thay đổi giao diện
    public void changeColor() {
        showUp.setStyle("text-area-background: linear-gradient(from 0px 141px to 0px 282px, reflect, "
                + colorshowUp1 +
                ", " + colorshowUp2 +
                ");-fx-text-fill: " +
                colorTextshowUp);
        input.setStyle("-fx-text-inner-color: " + colorinput);
        option.setValue("Chọn chức năng");
    }


}
