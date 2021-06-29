package mqtt_practice;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MqttPublisher_API{
	static MqttClient sampleClient;
		
    public static void main(String[] args) {
    	MqttPublisher_API obj = new MqttPublisher_API();
    	obj.run();
    }
    public void run() {
    	connectBroker();
    	while(true) {
    		try {
    			String[] traffic_data = get_traffic_data(); // 공공 API
    			String[] parking_data = get_parking_data(); // 공공 API
    			
    	       	publish_data("in_1", "{\"in_1\": "+traffic_data[0]+"}");     // in_1 데이터 발행
    	       	publish_data("out_1", "{\"out_1\": "+traffic_data[1]+"}");   // out_1 데이터 발행
    	       	publish_data("in_2", "{\"in_2\": "+traffic_data[2]+"}");     // in_2 데이터 발행
    	       	publish_data("out_2", "{\"out_2\": "+traffic_data[3]+"}");   // out_2 데이터 발행
    	       	publish_data("in_3", "{\"in_3\": "+traffic_data[4]+"}");     // in_3 데이터 발행
    	       	publish_data("out_3", "{\"out_3\": "+traffic_data[5]+"}");   // out_3 데이터 발행
    	       	
    	       	
    	       	publish_data("all_1", "{\"all_1\": "+parking_data[0]+"}");             // all_1 데이터 발행
    	       	publish_data("condition_1", "{\"condition_1\": \""+parking_data[1]+"\"}"); // condition_1 데이터 발행
    	       	publish_data("percent_1", "{\"percent_1\": \""+parking_data[2]+"\"}");     // percent_1 데이터 발행
    	       	publish_data("current_1", "{\"current_1\": "+parking_data[3]+"}");     // current_1 데이터 발행
    	       	
    	       	publish_data("all_2", "{\"all_2\": "+parking_data[4]+"}");             // all_2 데이터 발행
    	       	publish_data("condition_2", "{\"condition_2\": \""+parking_data[5]+"\"}"); // condition_2 데이터 발행
    	       	publish_data("percent_2", "{\"percent_2\": \""+parking_data[6]+"\"}");     // percent_2 데이터 발행
    	       	publish_data("current_2", "{\"current_2\": "+parking_data[7]+"}");     // current_2 데이터 발행
    	       	
    	       	publish_data("all_3", "{\"all_3\": "+parking_data[8]+"}");             // all_3 데이터 발행
    	       	publish_data("condition_3", "{\"condition_3\": \""+parking_data[9]+"\"}"); // condition_3 데이터 발행
    	       	publish_data("percent_3", "{\"percent_3\": \""+parking_data[10]+"\"}");    // percent_3 데이터 발행
    	       	publish_data("current_3", "{\"current_3\": "+parking_data[11]+"}");    // current_3 데이터 발행



    	       	Thread.sleep(60000); // 60초 단위 반복
    		}catch (Exception e) {
				// TODO: handle exception
    			try {
    				sampleClient.disconnect();
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			e.printStackTrace();
    	        System.out.println("Disconnected");
    	        System.exit(0);
			}
    	}
    }
    
    public void connectBroker() {
        String broker = "tcp://127.0.0.1:1883"; // 브로커 서버의 주소 
        String clientId = "practice"; // 클라이언트의 ID
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);// Mqtt Client 객체 초기화
            MqttConnectOptions connOpts = new MqttConnectOptions(); // 접속시 접속의 옵션을 정의하는 객체 생성
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts); // 브로커서버에 접속
            System.out.println("Connected");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    public void publish_data(String topic_input, String data) {
        String topic = topic_input;
        int qos = 0;
        try {
            System.out.println("Publishing message: "+data);
            sampleClient.publish(topic, data.getBytes(), qos, false);
            System.out.println("Message published");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    public static String[] get_traffic_data() {
    	
    	String url = "http://openapi.airport.co.kr/service/rest/AirportParking/airportparkingRT"
    			+ "?serviceKey=9dVijXyq8cRSKGTwL5foUJo%2BN4bogoBQKpoNVybJosU%2FFSqXQvgpJKIFWj8V5mAWPyn3SVIxTkwn353Wbwblig%3D%3D"
    			+ "&schAirportCode=GMP";
    	
		String in_1 = "-99";
		String out_1 = "-99";
		String in_2 = "-99";
		String out_2 = "-99";
		String in_3 = "-99";
		String out_3 = "-99";
		
    	Document doc = null;
		
    	// Jsoup으로 API 데이터 가져오기
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(doc);
		
		Elements elements = doc.select("item");
		for (Element e : elements) {
			if (e.select("parkingAirportCodeName").text().equals("국내선 제1주차장")) {
				in_1 = e.select("parkingIincnt").text();
				out_1 = e.select("parkingIoutcnt").text();
			}
			if (e.select("parkingAirportCodeName").text().equals("국내선 제2주차장")) {
				in_2 = e.select("parkingIincnt").text();
				out_2 = e.select("parkingIoutcnt").text();
				}
			if (e.select("parkingAirportCodeName").text().equals("국제선 지하주차장")) {
				in_3 = e.select("parkingIincnt").text();
				out_3 = e.select("parkingIoutcnt").text();			}
		}
		String[] out = {in_1, out_1, in_2, out_2, in_3, out_3};
    	return out;
    }    
    
    public static String[] get_parking_data() {
    	String url = "http://openapi.airport.co.kr/service/rest/AirportParkingCongestion/airportParkingCongestionRT"
    			+ "?serviceKey=9dVijXyq8cRSKGTwL5foUJo%2BN4bogoBQKpoNVybJosU%2FFSqXQvgpJKIFWj8V5mAWPyn3SVIxTkwn353Wbwblig%3D%3D"
    			+ "&pageNo=1"
    			+ "&numOfRows=10"
    			+ "&schAirportCode=GMP";
    
		String all_1 = "-99";
		String condition_1 = "null";
		String percent_1 = "null";
		String current_1 = "-99";
		String all_2 = "-99";
		String condition_2 = "";
		String percent_2 = "null";
		String current_2 = "-99";
		String all_3 = "-99";
		String current_3 = "-99";
		String condition_3 = "";
		String percent_3 = "null";
		
    	Document doc = null;
		
    	// Jsoup으로 API 데이터 가져오기
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(doc);
		Elements elements = doc.select("item");
		for (Element e : elements) {
			if (e.select("parkingAirportCodeName").text().equals("국내선 제1주차장")) {
				all_1 = e.select("parkingTotalSpace").text();
				condition_1 = eng(e.select("parkingCongestion").text());
				percent_1 = e.select("parkingCongestionDegree").text();
				current_1 = e.select("parkingOccupiedSpace").text();
			}
			if (e.select("parkingAirportCodeName").text().equals("국내선 제2주차장")) {
				all_2 = e.select("parkingTotalSpace").text();
				condition_2 = eng(e.select("parkingCongestion").text());
				percent_2 = e.select("parkingCongestionDegree").text();
				current_2 = e.select("parkingOccupiedSpace").text();
			}
			if (e.select("parkingAirportCodeName").text().equals("국제선 지하주차장")) {
				all_3 = e.select("parkingTotalSpace").text();
				condition_3 = eng(e.select("parkingCongestion").text());
				percent_3 = e.select("parkingCongestionDegree").text();
				current_3 = e.select("parkingOccupiedSpace").text();
			}
		}
		String[] out = {all_1, condition_1, percent_1, current_1, all_2, condition_2, percent_2, current_2, all_3, condition_3, percent_3, current_3};
		return out;
    }
    public static String eng(String condition){
		if(condition.equals("원활")){
			condition = "good";
		}else if(condition.equals("만차")) {
			condition = "full";
		}else if(condition.equals("혼잡")) {
			condition = "congested";
		}
		return condition;
	}
}