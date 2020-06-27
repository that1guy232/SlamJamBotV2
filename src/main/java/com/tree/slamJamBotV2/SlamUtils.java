package com.tree.slamJamBotV2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamUtils {




	public static boolean containsWord(String sentence, String word){
		String regex = ".*\\b" + Pattern.quote(word) + "\\b.*";
		return sentence.matches(regex);
	}

	public static String[] spiltMessage(String message){
		String regex = "\\s";
		return message.split(regex);
	}

	public static String makeSentence(ArrayList<String> strings){
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			stringBuilder.append(strings.get(i));
			if(strings.size()-1 != i){

				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}

	public static String readUrl(String urlString) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	public static <T> T[] push(T[] arr, T item) {
		T[] tmp = Arrays.copyOf(arr, arr.length + 1);
		tmp[tmp.length - 1] = item;
		return tmp;
	}

	public static <T> T[] pop(T[] arr) {
		T[] tmp = Arrays.copyOf(arr, arr.length - 1);
		return tmp;
	}
}
