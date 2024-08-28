package com.bajaj.Hash;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

@SpringBootApplication
public class HashApplication {

	public static void main(String[] args) {
		SpringApplication.run(HashApplication.class, args);
	}

	@Component
	public static class JsonProcessor implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			if (args.length != 2) {
				System.out.println("Usage: java -jar <jarfile> <PRN Number> <Path to JSON file>");
				return;
			}

			String prnNumber = args[0];
			String jsonFilePath = args[1];

			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(new File(jsonFilePath));

				String destinationValue = findDestinationValue(rootNode);
				if (destinationValue == null) {
					System.out.println("Key 'destination' not found in the JSON file.");
					return;
				}

				String randomString = generateRandomString(8);

				String combined = prnNumber + destinationValue;
				System.out.println("Combined PNR and destination:"+combined);

				String md5Hash = generateMD5Hash(combined);

				System.out.println("MD5 with random String: "+md5Hash+ ";" + randomString);
			} catch (IOException e) {
				System.err.println("Error reading the JSON file: " + e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				System.err.println("Error generating MD5 hash: " + e.getMessage());
			}
		}

		private String findDestinationValue(JsonNode node) {
			if (node.isObject()) {
                for (Iterator<String> it = ((ObjectNode) node).fieldNames(); it.hasNext(); ) {
                    String fieldName = it.next();
                    if ("destination".equals(fieldName)) {
                        return node.get(fieldName).asText();
                    }
                    JsonNode childNode = node.get(fieldName);
                    String result = findDestinationValue(childNode);
                    if (result != null) {
                        return result;
                    }
                }
			} else if (node.isArray()) {
				for (JsonNode arrayNode : node) {
					String result = findDestinationValue(arrayNode);
					if (result != null) {
						return result;
					}
				}
			}
			return null;
		}

		private String generateRandomString(int length) {
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			Random random = new Random();
			StringBuilder sb = new StringBuilder(length);
			for (int i = 0; i < length; i++) {
				sb.append(characters.charAt(random.nextInt(characters.length())));
			}
			return sb.toString();
		}

		private String generateMD5Hash(String input) throws NoSuchAlgorithmException {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashBytes = md.digest(input.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		}
	}
}
