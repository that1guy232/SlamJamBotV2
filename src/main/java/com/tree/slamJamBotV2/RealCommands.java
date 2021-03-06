package com.tree.slamJamBotV2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.ArrayList;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands implements MessageCreateListener {
	Gson gson = new Gson();


	ArrayList<String> messages = new ArrayList<>();


        //TODO ONLY F TO C WORKS

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        System.err.println(event.getMessage().getCreationTimestamp());
    	//Channel channel = event.getChannel();
		String message = event.getMessage().getContent().toLowerCase();

		if(message.contains("f to c")||message.contains("c to f")) {
		    event.getChannel().sendMessage(String.valueOf(convertTemp(message)));

		}


		if(message.contains("miles to km")||message.contains("km to miles")||
				message.contains("m to ft") || message.contains("ft to m")
				){
			event.getChannel().sendMessage(String.valueOf(convertDistance(message)));
		}

		if (message.contains("cad to usd")|| message.contains("usd to cad")){
			event.getChannel().sendMessage(String.valueOf(convertCurrancy(message)));
		}


    }


    private double convertCurrancy(String message){
		String[] s = SlamUtils.spiltMessage(message);
		double x = 0;
		double numberOnly= Double.parseDouble(s[0].replaceAll("[^0-9./-]", ""));
		String json = null;


		System.out.println(s[0]); //todo this is fucked up fix rplace with get first kinda thing in the whole string instead of slpiting?
		if(s[0].contains("cad")){
			System.out.println("t");
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=CAD_USD&apiKey=5dad0dc5806422010fd1&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("CAD_USD")
					.getAsJsonObject().get("val").getAsDouble();
			System.out.println(json);
			double a = numberOnly * x;
			double roundOff = (double) Math.round(a * 100) / 100;
					 return roundOff;
		}
		if(s[0].contains("usd")){
			System.out.println("z");
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=USD_CAD&apiKey=5dad0dc5806422010fd1&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("USD_CAD")
					.getAsJsonObject().get("val").getAsDouble();
			double a = numberOnly * x;

			double roundOff = (double) Math.round(a * 100) / 100;
			return roundOff;

		}

	    System.out.println(json);




		return 0;
	}

	private double convertDistance(String message) {
		String[] s = SlamUtils.spiltMessage(message);

		double numberOnly= Double.parseDouble(s[0].replaceAll("[^0-9/-]", ""));

		if(s[0].contains("miles")){
			double m = numberOnly*1.60934;
			return Math.round(m*100.0)/100.0;


		}
		else if(s[0].contains("km")){
			double km = numberOnly/1.60934;
			return Math.round(km*100.0)/100.0;

		}else if(s[0].contains("ft")){
			double m = numberOnly*0.3048;
			return m;


		}
		else if(s[0].contains("m")){
			double ft = numberOnly/0.3048;
			return Math.round(ft*100.0)/100.0;


		}

		return 0;
	}

	private double convertTemp(String message) {
		String[] s = SlamUtils.spiltMessage(message);

		double numberOnly= Double.parseDouble(s[0].replaceAll("[f | to | c]", ""));


		System.err.println(numberOnly);



		if(s[0].contains("f")){
			double f = (numberOnly - 32)*.5556;
			return Math.round(f*100.0)/100.0;

		}
		else if(s[0].contains("c")){
			double c = numberOnly*1.8+32;
			return Math.round(c*100.0)/100.0;

		}
		return 0;

	}



}
