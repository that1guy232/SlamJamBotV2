package com.tree.slamJamBotV2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands {
	Gson gson = new Gson();
	@EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
    	IChannel channel = event.getChannel();
		String message = event.getMessage().getContent().toLowerCase();

		if(message.contains("f to c")||message.contains("c to f")) {
			SlamUtils.sendMessage(channel,String.valueOf(convertTemp(message)));
		}
		if(message.contains("miles to km")||message.contains("km to miles")){
			SlamUtils.sendMessage(channel,String.valueOf(convertDistance(message)));
		}

		if(message.equals("get commands")) {
			File f = new File("commands/"+event.getGuild().getLongID()+"Commands.json");
			SlamUtils.sendFile(event.getChannel(), f);
		}
		if (message.contains("cad to usd")|| message.contains("usd to cad")){
			SlamUtils.sendMessage(channel, String.valueOf(convertCurrancy(message)));
		}

    }


    private double convertCurrancy(String message){
		String[] s = SlamUtils.spiltMessage(message);
		double x;
		double numberOnly= Double.parseDouble(s[0].replaceAll("[^0-9/-]", ""));
		String json = null;
		if(s[0].contains("cad")){
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=CAD_USD&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("CAD_USD")
					.getAsJsonObject().get("val").getAsDouble();
			return numberOnly * x;
		}
		if(s[0].contains("usd")){
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=USD_CAD&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("USD_CAD")
					.getAsJsonObject().get("val").getAsDouble();
			return numberOnly * x;
		}





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

		}
		return 0;
	}

	private double convertTemp(String message) {
		String[] s = SlamUtils.spiltMessage(message);

		double numberOnly= Double.parseDouble(s[0].replaceAll("[^0-9/-]", ""));

		if(s[0].contains("-")){

			numberOnly -= 0;
		}
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
