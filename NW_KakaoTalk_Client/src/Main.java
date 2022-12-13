import Object.ServerInfo;
import Utilization.Util;

public class Main {
    public static void main(String[] args) {
        ServerInfo info = Util.getServerInfo();
        // 서버 정보 받아오기
        BeforeLogin beforeLogin = new BeforeLogin(info);
        // 카카오톡 실행
    }
}