package Utilization;

import Object.ServerInfo;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

public class Util {
    public static ServerInfo getServerInfo() {
        ServerInfo info = new ServerInfo("localhost", 20814);

        try {
            File serverDataFile = new File("config.dat");
            FileReader fileReader = new FileReader(serverDataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // 파일에서 접속 정보 불러오기

            info.serverIP = bufferedReader.readLine();
            info.serverPort = Integer.parseInt(bufferedReader.readLine());
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + "\nUsing default infomation.");
        }

        return info;
    } // 서버 정보 가져오기

    public static String encryptSHA512(String originalString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] stringBytes = originalString.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(stringBytes);

            return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    } // SHA512로 비밀번호 암호화

    public static String createJSON(int code, HashMap<String, Object> elements) {
        JSONObject json = new JSONObject();
        json.put("code", code);

        Iterator<String> keyIterator = elements.keySet().iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = elements.get(key);
            json.put(key, String.valueOf(value));
        }

        return json.toString();
    } // JSON 생성

    public static String createSingleJSON(int code, String key, String value) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put(key, value);

        return json.toString();
    } // 싱글 키 JSON 생성
}
