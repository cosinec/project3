import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;


public class FX extends Application {
    // 重写开始方法
    final static int MSGLEN = 128;// 数据报的长度
    final static int SERVER_PORTNUM = 2020;// 服务器的端口号
    static int pid = -1;
    static String licenseStr = "";
    static String netAddress = "255.255.255.255";// 广播地址

    public void start(Stage primaryStage) throws Exception {
        Label l = new Label(" 客户端许可证输入界面");           // 创建标签
        l.setLayoutX(140); l.setLayoutY(5);
        l.setFont(Font.font("SimHei",20));

        Label lb =new Label("请输入许可证序号：");
        lb.setLayoutY(45);lb.setLayoutX(20);
        lb.setFont(Font.font("SimHei",20));
        TextField entraytxt  = new TextField();
        entraytxt.setLayoutX(200);entraytxt.setLayoutY(43);
        entraytxt.setPrefWidth(280);entraytxt.setPrefHeight(28);

        Button CancelBtn = new Button("清除");
        CancelBtn.setLayoutX(30); CancelBtn.setLayoutY(88);
        CancelBtn.setPrefWidth(85);CancelBtn.setPrefHeight(30);
        CancelBtn.setFont(Font.font("SimHei",15));
        CancelBtn.setOnAction(new EventHandler<ActionEvent>() {   // 给按钮添加动作事件
            public void handle(ActionEvent event) {             // 触发方法
                entraytxt.setText("");             // 控制台输出文字
            }
        });


        Button ConfirmBtn = new Button("登录");
        ConfirmBtn.setDisable(true);
        ConfirmBtn.setLayoutX(200); ConfirmBtn.setLayoutY(88);
        ConfirmBtn.setPrefWidth(85);ConfirmBtn.setPrefHeight(30);
        ConfirmBtn.setFont(Font.font("SimHei",15));
        ConfirmBtn.setOnAction(new EventHandler<ActionEvent>() {   // 给按钮添加动作事件
            public void handle(ActionEvent event) {             // 触发方法
                if(entraytxt.getText().length()!=10){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("不规范的许可证格式  invalid form of license");
                    alert.setHeaderText("许可证不规范错误");
                    alert.setContentText("许可证要求字符串长度为10，请重输！");

                    alert.showAndWait();
                }
                else {
                    licenseStr = entraytxt.getText();
                    keep(primaryStage);
                }
            }
        });
        entraytxt.setOnKeyReleased(event->
        {
            if(event.getCode()== KeyCode.ENTER){
                if(entraytxt.getText().length()!=10){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("不规范的许可证格式  invalid form of license");
                    alert.setHeaderText("许可证不规范错误");
                    alert.setContentText("许可证要求字符串长度为10，请重输！");
                    alert.showAndWait();
                }
                else {
                    licenseStr = entraytxt.getText();
                    keep(primaryStage);
                }
            }
        });


        Button ExitBtn = new Button("退出");
        ExitBtn.setLayoutX(370); ExitBtn.setLayoutY(88);
        ExitBtn.setPrefWidth(85);ExitBtn.setPrefHeight(30);
        ExitBtn.setFont(Font.font("SimHei",15));
        ExitBtn.setOnAction(new EventHandler<ActionEvent>() {   // 给按钮添加动作事件
            public void handle(ActionEvent event) {             // 触发方法
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("A Exit Dialog");
                alert.setContentText("Do you want to exit?\n确认退出?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    Platform.exit();//exit the program
                }
                else {
                    event.consume();
                }
            }
        });

        AnchorPane rootNode = new AnchorPane();               // 创建流布局节点作为根节点
        // 获取根节点监视的列表
        ObservableList<Node> children = rootNode.getChildren();
        children.add(l);
        children.add(lb);// 将标签添加到列表中
        children.add(entraytxt);
        children.add(CancelBtn);
        children.add(ConfirmBtn);
        children.add(ExitBtn);
        entraytxt.textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov,
                                        String newValue, String oldValue) {
                        if(entraytxt.getText().length()==10){
                            ConfirmBtn.setDisable(false);
                        }
                        else {
                            ConfirmBtn.setDisable(true);
                        }
                    }

                }

        );
        // 创建场景，使用根节点，设置场景宽和高
        Scene mySecne = new Scene(rootNode, 500, 130);
        // 舞台启用场景
        primaryStage.setScene(mySecne);
        primaryStage.setTitle("客户端许可证");       // 设置窗体标题
        primaryStage.show();                      // 展示舞台
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("A Exit Dialog");
                alert.setContentText("Do you want to exit?\n确认退出?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    Platform.exit();//exit the program

                }
                else {
                    event.consume();
                }
            }
        });
    }

        //------------------get_ticket()--------------------------------
        public void keep(Stage stage){



        stage.hide();
        C1 open = new C1();
        open.start(new Stage());

    }
    //获取进程pid


    public static void main(String[] args) {
        launch(args);                                                // 启动
    }

}
class C1 extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label l = new Label("  恭喜，登录成功！");           // 创建标签
        l.setLayoutX(155); l.setLayoutY(10);
        l.setFont(Font.font("SimHei",20));
        Label n = new Label("Project 3\n版本 2020 (20 内部版本 00000)\n" +
                "2020 ComputerNetWork Corporation。保留所有权利。\n\n用户界面受中国和其他国家/地区的商标法\n和其他待颁布或已颁布的知识产权法保护。\n根据 ComputerNetwork 软件许可条款，许可如下用户使用本产品:\n" +
                "\n\n" + "admin\n" + "组织名称");
        n.setLayoutX(10);n.setLayoutY(40);
        n.setFont(Font.font("SimHei",15));
        AnchorPane root = new AnchorPane();
        ObservableList<Node> children = root.getChildren();

        children.add(l);
        children.add(n);
        Scene scene = new Scene(root, 500, 250);

        primaryStage.setTitle("Succeed in logging in!");

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("A Exit Dialog");
                alert.setContentText("Do you want to exit?\n确认退出?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    System.out.println("OK");
                    // ... user chose OK

                    Platform.exit();//exit the program
                }
                else {
                    event.consume();
                }
            }
        });
    }

}

