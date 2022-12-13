import API.Weather;
import ImageResizer.ImageResizer;
import Object.ServerInfo;
import Utilization.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainFrame extends JFrame {
    public static int totalUserNumber;
    private String user;

    private JPanel mainFrame;
    public JPanel mainPanel;
    private JPanel APIPanel;

    private JButton btnFriend;
    private JButton btnSearch;
    private JLabel lblAPI;
    private JButton btnChat;
    private JButton btnMore;
    private JPanel leftFrame;
    private JPanel fieldPanel;
    private JLabel lblField;
    private JButton btnAdd;
    private JButton btnLogout;


    private static JPanel target;
    public static JPanel getTarget() {
        return target;
    }
    public void setTarget(JPanel target) {
        this.target = target;
    }

    private ServerInfo info;
    private static Socket socket;
    public static Socket getSocket() {
        return socket;
    }
    public static void setSocket(Socket sck) {
        socket = sck;
    }
    private Scanner clientInput;
    private PrintWriter clientOutput;

    private static boolean isChange = false;
    public static boolean getIsChange() {
        return isChange;
    }
    public static void setIsChange(boolean isChange) {
        MainFrame.isChange = isChange;
    }
    private static boolean isLogout = false;
    public boolean isLogout() {
        return isLogout;
    }
    public static void setLogout(boolean logout) {
        isLogout = logout;
    }

    MainFrame(ServerInfo serverInfo, String nickName) {
        this.info = serverInfo;
        this.user = new String(nickName);

        try {
            socket = new Socket(info.serverIP, info.serverPort);
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isLogout == true) {
                    isLogout = false;
                    dispose();
                } // 로그아웃
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isLogout == true) {
                    isLogout = false;
                    dispose();
                } // 로그아웃
            }
        }); // 마우스 움직임

        btnFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblField.setText("친구");

                mainPanel.removeAll();
                mainPanel.add(new FriendBoard(user, socket).getMainPanel());
                // 메인 패널 재배치

                mainPanel.revalidate();
                mainPanel.repaint();
                target = mainPanel;
            }
        }); // 친구 패널 불러오기

        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }); // 채팅 패널 불러오기

        btnMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }); // 더보기 버튼

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    clientInput = new Scanner(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    clientOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                    // 인풋 아웃풋 연결

                    String logoutJSON = Util.createSingleJSON(3003, "logout", user);
                    clientOutput.println(logoutJSON);

                    if (clientInput.hasNextLine()) {
                        String serverOutput = new String();

                        try {
                            serverOutput = clientInput.nextLine();
                            if (serverOutput.isEmpty()) serverOutput = clientInput.nextLine();
                            // 서버로부터 수신

                            JSONParser parser = new JSONParser();
                            JSONObject object = (JSONObject) parser.parse(serverOutput);
                            // JSON 파싱

                            int response = Integer.parseInt(String.valueOf(object.get("code")));
                            // 응답 코드 확인

                            if (response == 200) {
                                String logoutResult = String.valueOf(object.get("logout"));
                                System.out.println(logoutResult);
                                // 로그아웃 요청 결과

                                if (logoutResult.equals("true")) {
                                    JOptionPane.showMessageDialog(mainPanel, "Logout Success.", "Notice", JOptionPane.INFORMATION_MESSAGE);

                                    clientInput.close();
                                    clientOutput.close();
                                    socket.close();
                                    // 소켓 연결 해제

                                    dispose();
                                } // 로그아웃이 된 경우
                                else {
                                    JOptionPane.showMessageDialog(mainPanel, "Logout Failed.", "Warning", JOptionPane.WARNING_MESSAGE);
                                } // 로그아웃이 되지 않은 경우
                            } // 서버가 요청을 정상적으로 처리한 경우
                            else {
                                System.out.println("Logout Error");
                            } // 비정상적으로 처리된 경우
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }); // 로그아웃

        JButton[] btnGroup2 = new JButton[2];
        btnGroup2[0] = btnSearch;
        btnGroup2[1] = btnAdd;
        // 버튼 그룹지정
        ImageResizer.FriendBoardImage(btnGroup2);

        JButton[] btnGroup = new JButton[3];
        btnGroup[0] = btnFriend;
        btnGroup[1] = btnChat;
        btnGroup[2] = btnMore;
        // 버튼 그룹 지정
        ImageResizer.InterfaceImage(btnGroup);

        setContentPane(mainFrame);

        lblField.setText("친구");

        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(new FriendBoard(user, socket).getMainPanel());
        target = mainPanel;

        Weather weatherAPI = new Weather();
        String sky;
        if(weatherAPI.skyState==1){
            sky = "맑음";
        }
        else if(weatherAPI.skyState==3){
            sky = "구름 많음";
        }
        else{
            sky = "흐림";
        }

        lblAPI.setText(
                "<html>" +
                "<h1>" + "현재 기상정보" + "</h1>" +
                "하늘 상태: " + sky + "<br>" +
                "기온: " + weatherAPI.temperature + "<br>" +
                "강수 확률: " + weatherAPI.POP + "<br>" +
                "</html>"
        );

        // 처음 패널 불러오기

        setSize(450, 600);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KakaoTalk - " + user);
        setVisible(true);
    }
}
