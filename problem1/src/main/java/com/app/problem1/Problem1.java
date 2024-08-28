package com.app.problem1;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Problem1 {

	
	

	    private static final String DESTINATION_KEY = "destination";

	    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
	        if (args.length != 2) {
	            System.out.println("Usage: java -jar DestinationHashGenerator.jar <prn_number> <json_file>");
	            return;
	        }

	        String prnNumber = args[0].toLowerCase();
	        String filePath = args[1];

	        String destinationValue = findDestinationValue(filePath);
	        if (destinationValue == null) {
	            System.out.println("Key \"destination\" not found in JSON file.");
	            return;
	        }

	        String randomString = generateRandomString();
	        String concatenatedString = prnNumber + destinationValue + randomString;
	        String md5Hash = generateMD5Hash(concatenatedString);

	        System.out.println(md5Hash + ";" + randomString);
	    }

	    private static String findDestinationValue(String filePath) throws IOException {
	        ObjectMapper objectMapper = new ObjectMapper();
	        Object jsonObject = objectMapper.readValue(new File(filePath), Object.class);
	        return findDestinationRecursive(jsonObject);
	    }

	    private static String findDestinationRecursive(Object object) {
	        if (object instanceof String) {
	            return null;
	        } else if (object instanceof Map) {
	            Map<String, Object> map = (Map<String, Object>) object;
	            if (map.containsKey(DESTINATION_KEY)) {
	                return map.get(DESTINATION_KEY).toString();
	            }
	            for (Object value : map.values()) {
	                String destinationValue = findDestinationRecursive(value);
	                if (destinationValue != null) {
	                    return destinationValue;
	                }
	            }
	        } else if (object instanceof List) {
	            for (Object element : (List<?>) object) {
	                String destinationValue = findDestinationRecursive(element);
	                if (destinationValue != null) {
	                    return destinationValue;
	                }
	            }
	        }
	        return null;
	    }

	    private static String generateRandomString() {
	        int length = 8;
	        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	        StringBuilder sb = new StringBuilder();
	        Random random = new Random();
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(alphabet.length());
	            sb.append(alphabet.charAt(index));
	        }
	        return sb.toString();
	    }

	    private static String generateMD5Hash(String message) throws NoSuchAlgorithmException {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hash) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }
	
}
