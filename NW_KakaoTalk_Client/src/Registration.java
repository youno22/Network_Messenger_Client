import Object.ServerInfo;
import Utilization.Util;
import com.mysql.cj.xdevapi.JsonParser;
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
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends JFrame {
    private JTextField txtName;
    private JTextField txtId;
    private JButton btnId;
    private JPasswordField pfPwd;
    private JPasswordField pfRePwd;
    private JTextField txtNickname;
    private JTextField txtBirth;
    private JTextField txtPhone;
    private JRadioButton rdMale;
    private JRadioButton rdFemale;
    private JPanel mainPanel;
    private JCheckBox ckAgree;
    private JButton btnRegist;
    private JLabel lblId;
    private JLabel lblRe;
    private JButton btnNickname;
    private JLabel lblNickname;
    private JTextField txtEmail;
    private JButton btnEmail;
    private JLabel lblEmail;

    private Socket socket;
    private Scanner clientInput;
    private PrintWriter clientOutput;

    private boolean isIdPossible = false;
    private boolean isNicknamePossible = false;
    private boolean isEmailPossible = false;
    private boolean rightFormat = false;
    private boolean isRegistSuccess = false;

    private ServerInfo info;

    private void ClearForm() {
        txtName.setText("");
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        txtId.setText("");
        lblId.setText("");
        pfPwd.setText("");
        pfRePwd.setText("");
        txtNickname.setText("");
        txtBirth.setText("");
        txtPhone.setText("");
        ckAgree.setSelected(false);
    }

    Registration(ServerInfo serverInfo) {
        this.info = serverInfo;

        try {
            socket = new Socket(info.serverIP, info.serverPort);
            clientInput = new Scanner(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            clientOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch(Exception e) {
            e.printStackTrace();
        } // 소켓 연결

        btnId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern idPattern = Pattern.compile("^[a-zA-Z0-9]{1,15}$");
                Matcher matcher = idPattern.matcher(txtId.getText());
                System.out.println("Check Duplicated Id...");
                // ID 가능 패턴

                if (matcher.find()) {
                    String idCheck = Util.createSingleJSON(1001, "id", txtId.getText());
                    clientOutput.println(idCheck);
                    // 서버에게 id 중복 체크 요청

                    if (clientInput.hasNextLine()) {
                        String line = new String();

                        try {
                            line = clientInput.nextLine();
                            if (line.isEmpty()) line = clientInput.nextLine();
                            // 서버로부터 결과 수신

                            JSONParser parser = new JSONParser();
                            JSONObject object = (JSONObject) parser.parse(line);
                            // JSON 파싱

                            int response = Integer.parseInt(String.valueOf(object.get("code")));
                            // 응답 코드 확인

                            if (response == 200) {
                                String check = String.valueOf(object.get("id"));
                                System.out.println(check);
                                // 서버의 ID 중복 체크 결과 확인

                                if (check.equals("true")) {
                                    lblId.setForeground(Color.GREEN);
                                    lblId.setText("Possible");
                                    isIdPossible = true;
                                } // ID가 사용 가능한 경우
                                else {
                                    lblId.setForeground(Color.RED);
                                    lblId.setText("Impossible");
                                    isIdPossible = false;
                                } // ID가 사용 불가능한 경우
                            } // 서버가 요청을 정상적으로 처리한 경우
                            else {
                                System.out.println("Error");
                            } // 비정상적으로 처리된 경우
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }

                    } // 서버로부터 결과를 수신한 경우
                } // ID 사용 가능 패턴에 맞게 작성한 경우
            }
        }); // ID 중복 체크

        btnNickname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern nickNamePattern = Pattern.compile("^[a-zA-Z0-9]{1,15}$");
                Matcher matcher = nickNamePattern.matcher(txtNickname.getText());
                System.out.println("Check Duplicated Nickname...");
                // 닉네임 사용 가능한 패턴

                if (matcher.find()) {
                    String nickNameCheck = Util.createSingleJSON(1002, "nickname", txtNickname.getText());
                    clientOutput.println(nickNameCheck);
                    // 서버에게 닉네임 중복 체크 요청

                    if (clientInput.hasNextLine()) {
                        String line = new String();

                        try {
                            line = clientInput.nextLine();
                            if (line.isEmpty()) line = clientInput.nextLine();
                            // 서버로부터 결과 수신

                            JSONParser parser = new JSONParser();
                            JSONObject object = (JSONObject) parser.parse(line);
                            // JSON 파싱

                            int response = Integer.parseInt(String.valueOf(object.get("code")));
                            // 응답 코드 확인

                            if (response == 200) {
                                String check = String.valueOf(object.get("nickname"));
                                System.out.println(check);
                                // 서버의 닉네임 중복 체크 결과 확인

                                if (check.equals("true")) {
                                    lblNickname.setForeground(Color.GREEN);
                                    lblNickname.setText("Possible");
                                    isNicknamePossible = true;
                                } // 닉네임이 사용 가능한경우
                                else {
                                    lblNickname.setForeground(Color.RED);
                                    lblNickname.setText("Impossible");
                                    isNicknamePossible = false;
                                } // 닉네임이 사용 불가능한 경우
                            } // 서버가 요청을 정상적으로 처리한 경우
                            else {
                                System.out.println("Error");
                            } // 비정상적으로 처리된 경우
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }

                    } // 서버로부터 결과를 수신한 경우
                } // 닉네임 사용 가능 패턴에 맞게 작성한 경우
            }
        }); // 닉네임 중복 체크

        txtBirth.addFocusListener(new FocusListener() {
            String hint  = new String("ex) yyyy-mm-dd");
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBirth.getText().equals(hint)) txtBirth.setText("");
            } // 포커스를 얻은 경우

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBirth.getText().length() == 0) txtBirth.setText("ex) yyyy-mm-dd");
            } // 포커스를 잃은 경우
        }); // 생년월일 힌트

        txtPhone.addFocusListener(new FocusListener() {
            String hint = new String("ex) xxx-xxxx-xxxx");
            @Override
            public void focusGained(FocusEvent e) {
                if (txtPhone.getText().equals(hint)) txtPhone.setText("");
            } // 포커스를 얻은 경우

            @Override
            public void focusLost(FocusEvent e) {
                if (txtPhone.getText().length() == 0) txtPhone.setText(hint);
            } // 포커스를 잃은 경우
        }); // 전화번호 힌트

        txtEmail.addFocusListener(new FocusListener() {
            String hint = new String("ex) xxx@xxx");
            @Override
            public void focusGained(FocusEvent e) {
                if (txtEmail.getText().equals(hint)) txtEmail.setText("");
            } // 포커스를 얻은 경우

            @Override
            public void focusLost(FocusEvent e) {
                if (txtEmail.getText().length() == 0) txtEmail.setText("");
            } // 포커스를 잃은 경우
        }); // 이메일 힌트

        btnEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern emailPattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
                Matcher matcher = emailPattern.matcher(txtEmail.getText());
                System.out.println("Check Duplicated Email...");
                // 이메일 사용 가능한 패턴

                if (matcher.find()) {
                    String emailCheck = Util.createSingleJSON(1003, "email", txtEmail.getText());
                    clientOutput.println(emailCheck);
                    // 서버에게 이메일 중복 체크 요청

                    if (clientInput.hasNextLine()) {
                        String line = new String();

                        try {
                            line = clientInput.nextLine();
                            if (line.isEmpty()) line = clientInput.nextLine();
                            // 서버로부터 결과 수신

                            JSONParser parser = new JSONParser();
                            JSONObject object = (JSONObject) parser.parse(line);
                            // JSON 파싱

                            int response = Integer.parseInt(String.valueOf(object.get("code")));
                            // 응답 코드 확인
                            
                            if (response == 200) {
                                String check = String.valueOf(object.get("email"));
                                System.out.println(check);
                                // 서버로부터 이메일 중복 체크 결과 확인

                                if (check.equals("true")) {
                                    lblEmail.setForeground(Color.GREEN);
                                    lblEmail.setText("Possible");
                                    isEmailPossible = true;
                                } // 이메일이 사용 가능한 경우
                                else {
                                    lblEmail.setForeground(Color.RED);
                                    lblEmail.setText("Impossible");
                                    isEmailPossible = false;
                                } // 이메일이 사용 불가능한 경우
                            } // 서버가 요청을 정상적으로 처리한 경우
                            else {
                                System.out.println("Error");
                            } // 비정상적으로 처리된 경우
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }

                    } // 서버로부터 결과를 수신한 경우
                } // 이메일 사용 가능 패턴에 맞게 작성한 경우
            }
        }); // 이메일 중복 체크

        btnRegist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Registration...");

                if (isIdPossible == true && isNicknamePossible == true && isEmailPossible == true) {
                    if (ckAgree.isSelected() && String.valueOf(pfPwd.getPassword()).length() >= 8) {
                        if (rdMale.isSelected() && String.valueOf(pfPwd.getPassword()).equals(String.valueOf(pfRePwd.getPassword()))) {
                            HashMap<String, Object> registHashMap = new HashMap<String, Object>();
                            registHashMap.put("id", txtId.getText());
                            registHashMap.put("pwd", Util.encryptSHA512(String.valueOf(pfPwd.getPassword())));
                            registHashMap.put("name", txtName.getText());
                            registHashMap.put("gender", rdMale.getText());
                            registHashMap.put("nickname", txtNickname.getText());
                            registHashMap.put("birth", txtBirth.getText());
                            registHashMap.put("phone", txtPhone.getText());
                            registHashMap.put("email", txtEmail.getText());
                            // 서버에게 전송할 HashMap 작성

                            String registJSON = Util.createJSON(1004, registHashMap);
                            clientOutput.println(registJSON);
                            // 서버에게 회원가입 요청

                            if (clientInput.hasNextLine()) {
                                String line = new String();

                                try {
                                    line = clientInput.nextLine();
                                    if (line.isEmpty()) line = clientInput.nextLine();
                                    // 서버로부터 결과 수신

                                    JSONParser parser = new JSONParser();
                                    JSONObject object = (JSONObject) parser.parse(line);
                                    // JSON 파싱

                                    int response = Integer.parseInt(String.valueOf(object.get("code")));
                                    // 응답 코드 확인

                                    if (response == 200) {
                                        String registResult = String.valueOf(object.get("registration"));
                                        System.out.println(registResult);
                                        // 회원가입 결과 확인

                                        if (registResult.equals("true")) {
                                            JOptionPane.showMessageDialog(mainPanel, "Registration Success.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                                            Login login = new Login(serverInfo);
                                            // 로그인 창 실행

                                            dispose();
                                            socket.close();
                                        } // 회원가입에 성공한 경우
                                        else {
                                            JOptionPane.showMessageDialog(mainPanel, "Registration Failed.", "Warning", JOptionPane.WARNING_MESSAGE);
                                        } // 회원가입에 실패한 경우
                                    } // 서버가 요청을 정상적으로 처리한 경우
                                    else {
                                        System.out.println("Error");
                                    } // 비정상적으로 처리된 경우
                                } catch (ParseException ex) {
                                    throw new RuntimeException(ex);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } // 서버로부터 결과를 수신한 경우
                        }// 남자가 체크된 경우
                        else if (rdFemale.isSelected() && String.valueOf(pfPwd.getPassword()).equals(String.valueOf(pfRePwd.getPassword()))) {
                            HashMap<String, Object> registHashMap = new HashMap<String, Object>();
                            registHashMap.put("id", txtId.getText());
                            registHashMap.put("pwd", Util.encryptSHA512(String.valueOf(pfPwd.getPassword())));
                            registHashMap.put("name", txtName.getText());
                            registHashMap.put("gender", rdFemale.getText());
                            registHashMap.put("nickname", txtNickname.getText());
                            registHashMap.put("birth", txtBirth.getText());
                            registHashMap.put("phone", txtPhone.getText());
                            registHashMap.put("email", txtEmail.getText());
                            // 서버에게 전송할 HashMap 작성

                            String registJSON = Util.createJSON(1004, registHashMap);
                            clientOutput.println(registJSON);
                            // 서버에게 회원가입 요청

                            if (clientInput.hasNextLine()) {
                                String line = new String();

                                try {
                                    line = clientInput.nextLine();
                                    if (line.isEmpty()) line = clientInput.nextLine();
                                    // 서버로부터 결과 수신

                                    JSONParser parser = new JSONParser();
                                    JSONObject object = (JSONObject) parser.parse(line);
                                    // JSON 파싱

                                    int response = Integer.parseInt(String.valueOf(object.get("code")));
                                    // 응답 코드 확인

                                    if (response == 200) {
                                        String registResult = String.valueOf(object.get("registration"));
                                        System.out.println(registResult);
                                        // 서버로부터 회원가입 결과 확인

                                        if (registResult.equals("true")) {
                                            JOptionPane.showMessageDialog(mainPanel, "Registration Success.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                                            Login login = new Login(serverInfo);
                                            // 로그인 창 실행

                                            dispose();
                                            socket.close();
                                        } // 성공적으로 회원가입된 경우
                                        else {
                                            JOptionPane.showMessageDialog(mainPanel, "Registration Failed.", "Warning", JOptionPane.WARNING_MESSAGE);
                                        } // 회원가입에 실패한 경우
                                    } // 서버가 요청을 정상적으로 처리한 경우
                                    else {
                                        System.out.println("Registration Error");
                                    } // 비정상적으로 처리된 경우
                                } catch (ParseException ex) {
                                    throw new RuntimeException(ex);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } // 서버로부터 결과를 수신한 경우
                        } // 여자가 체크된 경우
                    } // 동의에 체크하고, 비밀번호가 8자 이상인 경우
                } // 모든 중복 체크에서 가능함을 받은 경우
                else if (!String.valueOf(pfPwd.getPassword()).equals(String.valueOf(pfRePwd.getPassword()))) {
                    System.out.println("Not Equal");
                } // 비밀번호 재입력이 같지 않은 경우
            }
        }); // 회원가입

        setContentPane(mainPanel);

        setSize(600, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Instagram Registration Form");
        setVisible(true);
    }
}
