package com.stanford_nlp.SentimentAnalyzer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.stanford_nlp.model.SentimentResult;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MainApp {

	public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/analyze", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
		@Override
		public void handle(com.sun.net.httpserver.HttpExchange t) throws IOException {
			Map<String, String> params = queryToMap(t.getRequestURI().getQuery()); 
			System.out.println("Requested: " + params.get("sent"));
            
			String response = Thing("" + params.get("sent"));
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
		}
    }
	
    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
    
	public static String Thing(String arg) throws IOException {
		
		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.initialize();
		SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(arg);

		System.out.println("Sentiment Returned: " + sentimentResult.getSentimentScore());
//		System.out.println("Sentiment Type: " + sentimentResult.getSentimentType());
//		System.out.println("Very positive: " + sentimentResult.getSentimentClass().getVeryPositive()+"%");
//		System.out.println("Positive: " + sentimentResult.getSentimentClass().getPositive()+"%");
//		System.out.println("Neutral: " + sentimentResult.getSentimentClass().getNeutral()+"%");
//		System.out.println("Negative: " + sentimentResult.getSentimentClass().getNegative()+"%");
//		System.out.println("Very negative: " + sentimentResult.getSentimentClass().getVeryNegative()+"%");
		
		return  "" + sentimentResult.getSentimentScore();
	}
	
	

}
