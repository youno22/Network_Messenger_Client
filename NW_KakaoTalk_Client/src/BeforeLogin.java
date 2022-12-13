import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Object.ServerInfo;

public class BeforeLogin extends JFrame {
    private JLabel lblLogo;
    private JButton btnLogin;
    private JButton btnRegist;
    private JLabel lblLogin;
    private JLabel lblRegist;
    private JPanel mainPanel;

    private ServerInfo info;

    BeforeLogin(ServerInfo serverInfo) {
        this.info = serverInfo;

        btnLogin.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               Login login = new Login(info);
           }
        }); // 로그인 버튼 -> 로그인 창

        btnRegist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registration registration = new Registration(info);
            }
        }); // 회원가입 버튼 -> 회원가입 창

        setContentPane(mainPanel);

        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("KakaoTalk Main Screen");
        setVisible(true);
    }
}
