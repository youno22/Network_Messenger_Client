package API;

/**
 * 공공데이터 단기예보 조회
 * skyState: 날씨(맑음, 구름많음, 흐림)
 * temperature: 온도
 * POP: 강수확률
 *
 */


import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

public class Weather {
    public int skyState; //날씨
    public String temperature; //온도
    public String POP; // 강수확률
    public Weather() {
        try{
            // 현재 날짜 구하기
            LocalDate now = LocalDate.now();
            // 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 포맷 적용
            String formatedNow = now.format(formatter);

            // 현재 시간 전체
            LocalTime nowTime = LocalTime.now();
            // 시 구하기
            int hour = nowTime.getHour();


            switch(hour){
                case 0:
                case 1:
                    //case 2:
                    // 현재 0시와 1시는 전날 23시로 변경
                    int change = Integer.parseInt(formatedNow);
                    change--;
                    formatedNow = Integer.toString(change);
                    hour = 23;
                    break;
                case 3:
                case 4:
                    //case 5:
                    //2~4시는 2시값 적용 나머지도 같은 방식
                    hour = 2;
                    break;
                case 6:
                case 7:
                    //case 8:
                    hour = 5;
                    break;
                case 9:
                case 10:
                    //case 11:
                    hour = 8;
                    break;
                case 12:
                case 13:
                    //case 14:
                    hour = 11;
                    break;
                case 15:
                case 16:
                    //case 17:
                    hour = 14;
                    break;
                case 18:
                case 19:
                    //case 20:
                    hour = 17;
                    break;
                case 21:
                case 22:
                    //case 23:
                    hour = 20;
                    break;
                default:
                    break;

            }

            String bT;
            if(hour<10){
                bT = "0" + Integer.toString(hour) + "00"; //hour을 0000의 형식으로 변환
            }
            else {
                bT = Integer.toString(hour) + "00"; //hour을 0000의 형식으로 변환
            }

            //공공데이터 받아오기
            String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
            String result = "";
            // 홈페이지에서 받은 키
            String serviceKey = "P4L9nfOkwT0N1JJoOFTxbQQrKagrWukh5D34%2FIfs21q1onLWBEgccIPkbsaxQyLxla%2Fffkjqs4n6y2AnYFqk7A%3D%3D"; //인증키
            String nx = "37";//위도 37 성남시 수정구
            String ny = "127";//경도 128
            String baseDate = formatedNow;//조회하고싶은 날짜 formatedNow에 현재 날짜 자동 저장
            String baseTime = bT; //조회하고싶은 시간 02시부터 3시간 단위로, 현재 시간에 따라 자동적으로 설정
            String type = "JSON";//타입 xml, json 등등 ..
            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));/* 타입 */
            /*
             * GET방식으로 전송해서 파라미터 받아오기
             */
            URL url = new URL(urlBuilder.toString());

            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            //JSON parsing
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            //System.out.println(result.toString()); //json 파일 출력
            System.out.println(result.toString());


            JSONObject getResponse = (JSONObject)jsonObject.get("response");
            JSONObject getI = (JSONObject)getResponse.get("body");
            JSONObject getItem = (JSONObject)getI.get("items");
            JSONArray getArray = (JSONArray) getItem.get("item");

            //임시 변수 설정
            JSONObject temp = (JSONObject)getArray.get(0);
            String temp2 = (String) temp.get("category");


            //TMP(온도), SKY(하늘상태), POP(강수확률) 값만 가져오기
            for(int i=0;i<Integer.parseInt(getI.get("numOfRows").toString());i++){
                temp = (JSONObject) getArray.get(i);
                if((temp2 = (String) temp.get("category")).equals("TMP")) {
                    //TMP: 온도
                    temperature = temp.get("fcstValue").toString();
                    System.out.println(temp.get("fcstValue").toString() + " is temperature"); // 온도 출력
                }
                else if((temp2 = (String) temp.get("category")).equals("SKY")) {
                    //SKY: 하늘 상태, 1맑음, 3구름많음 4흐림
                    skyState = Integer.parseInt(temp.get("fcstValue").toString());
                    String stringSkyState;
                    if(skyState==1){
                        stringSkyState = "맑음";
                    }
                    else if(skyState==3){
                        stringSkyState = "구름많음";
                    }
                    else if(skyState==4){
                        stringSkyState = "흐림";
                    }
                    else{
                        stringSkyState = "맑음";
                    }
                    System.out.println("하늘상태 is " + stringSkyState); //하늘 상태 출력
                }
                else if((temp2 = (String) temp.get("category")).equals("POP")) {
                    //POP: 강수확률
                    POP = temp.get("fcstValue").toString();
                    System.out.println(temp.get("fcstValue").toString() + " is 강수확률"); // 강수 확률 출력
                }
                //System.out.println(getArray.get(i));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args)  throws IOException, ParseException {
        Weather wDemo = new Weather();

    }

}







