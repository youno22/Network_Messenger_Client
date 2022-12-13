
import ImageResizer.ImageResizer;
import Object.ServerInfo;
import Utilization.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField txtId;
    private JPasswordField pfPwd;
    private JLabel lblLogo;
    private JButton btnLogin;
    private JButton btnRegist;

    private ServerInfo info;
    private Socket socket;
    private Scanner clientInput;
    private PrintWriter clientOutput;


    private boolean isLogin = false;

    private void ClearForm() {
        txtId.setText("");
        pfPwd.setText("");
    }

    Login(ServerInfo serverInfo) {
        this.info = serverInfo;

        try {
            socket = new Socket(info.serverIP, info.serverPort);
            clientInput = new Scanner(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            clientOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch(Exception e) {
            e.printStackTrace();
        } // 소켓 연결

        txtId.addFocusListener(new FocusListener() {
            String hint = new String("kakao ID");
            @Override
            public void focusGained(FocusEvent e) {
                if (txtId.getText().equals(hint)) txtId.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtId.getText().length() == 0) txtId.setText("");
            }
        }); // 로그인 힌트

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, Object> loginHashMap = new HashMap<String, Object>();
                loginHashMap.put("id", txtId.getText());
                loginHashMap.put("pwd", Util.encryptSHA512(String.valueOf(pfPwd.getPassword())));
                // 서버에 전송할 HashMap 생성

                String loginJSON = Util.createJSON(2001, loginHashMap);
                clientOutput.println(loginJSON);
                // JSON으로 전송

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
                            String loginResult = String.valueOf(object.get("login"));
                            System.out.println(loginResult);
                            // 로그인 요청 결과

                            if (loginResult.equals("true")) {
                                JOptionPane.showMessageDialog(mainPanel, "Login Success.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                                info.serverPort = Integer.parseInt(String.valueOf(object.get("port")));
                                // 메인 서버 포트 번호 업데이트

                                clientInput.close();
                                clientOutput.close();
                                socket.close();
                                // 소켓 연결 해제

                                MainFrame mainFrame = new MainFrame(serverInfo, String.valueOf(object.get("nickname")));
                                dispose();
                                // 메인 창 실행
                            } // 로그인 성공
                            else {
                                JOptionPane.showMessageDialog(mainPanel, "Login Failed.", "Warning", JOptionPane.WARNING_MESSAGE);
                            } // 로그인 실패
                        } // 로그인 요청이 정상적으로 처리된 경우
                        else {
                            System.out.println("Login Error");
                        } // 비정상적으로 처리된 경우
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }); // 로그인 버튼

        btnRegist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    socket.close();
                    clientInput.close();
                    clientOutput.close();
                    // 소켓 연결 해제
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Registration loginToRegist = new Registration(serverInfo);
                dispose();
            }
        }); // 회원가입 버튼 -> 회원가입 창 실행

        setContentPane(mainPanel);

        setSize(350, 600);
        ImageResizer.LoginImage(lblLogo);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kakaotalk Login");
        setVisible(true);
    }
}
