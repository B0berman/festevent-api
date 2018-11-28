package com.eip.festevent.utils;

import com.eip.festevent.beans.Publication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Comparator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Utils {

	private static String UPLOAD_FILE_SERVER = "/home/ubuntu/festevent-resources/";

	public static byte[] getImage(String key) {
		BufferedImage mapmodifiable;
		try {
			mapmodifiable = ImageIO.read(new File(UPLOAD_FILE_SERVER + key));
		} catch (IOException e1) {
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(mapmodifiable, "jpg", baos);
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public static class SortPublicationByDate implements Comparator<Publication>
	{
		// Used for sorting in ascending order of
		// roll number
		public int compare(Publication a, Publication b)
		{
			return b.getDate().compareTo(a.getDate());
		}
	}


	public static boolean writeToFileServer(byte[] bytes, String fileName) {

		String qualifiedUploadFilePath = UPLOAD_FILE_SERVER + fileName;
		try {
			OutputStream outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
		}
		catch (IOException ioe) {
			System.out.print(ioe.getCause());
			ioe.printStackTrace();
			return false;
		}
		return true;
	}

	public static String sendResetPwdMail(String target, String content) {

		final String username = "fest.event.eip@gmail.com";
//		final String password = "QueLaForceSoitAvecNous117";
		final String password = "kaemhhmxmelksmik";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("fest.event.eip@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(target));
			message.setSubject("New password");
			message.setText(content);

			Transport.send(message);

			return "Successfuly sent";
		} catch (MessagingException e) {
    		return e.getMessage() + " ||| " + e.getCause();
		}
	}
	
	public static boolean checkEmail(String email) {
		if (email == null) {
			return false;
		}
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static void normalizeEmail(String email) {
		email.trim();
		email.toLowerCase();
	}
	
	public static boolean ckeckPhoneNumber(String phoneNumber) {
		if (phoneNumber == null) {
			return false;
		}
		String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}
	
	public static void normalizePhoneNumber(String phoneNumber) {
		phoneNumber.trim();
		phoneNumber.replace(" ", "");
		phoneNumber.replace(".", "");
		phoneNumber.replace("-", "");
	}
	
	public static boolean checkProperNoun(String properNoun) {
		if (properNoun == null) {
			return false;
		}
		String regex = "^[a-zA-Z -.\']{2,32}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(properNoun);
		return matcher.matches();
	}
	
	public static void normalizeProperNoun(String properNoun) {
		properNoun.trim();
		properNoun = properNoun.substring(0,1).toUpperCase() + properNoun.substring(1).toLowerCase();
	}
	
	public static boolean checkUrl(String url) {
		if (url == null) {
			return false;
		}
		String regex = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	public static void normalizeUrl(String url) {
		url.trim();
	}
	
	public static boolean checkPassword(String password) {
		if (password == null) {
			return false;
		}
		
		//(?=.*[0-9]) Au moins un chiffre
		//(?=.*[a-z]) Au moins une lettre minuscule
		//(?=.*[A-Z]) Au moins une lettre maj
		//(?=.*[@#$%^&+=]) Au moins un carac spé
		//(?=\\S+$) Pas d'espace
		//.{8,} Au moins 8 caracs
		
		String regex = "^(?=\\S+$).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return true;//matcher.matches();
	}
	
	public static class Response {
		
		protected String message;
		
		public String getMsg() {
			return message;
		}

		public Response(String message) {
			this.message = message;
		}
	}
	
}
