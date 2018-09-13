package com.tree.slamJamBotV2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands {
	Gson gson = new Gson();

	@EventSubscriber
	public void onUserJoin(UserJoinEvent event){
		if (event.getUser().getLongID() == 73669669172555776L && event.getGuild().getLongID() == 284976055922458624L){
			event.getGuild().getUserByID(73669669172555776L).addRole(event.getGuild().getRoleByID(348225438973296653L));
			event.getGuild().getUserByID(73669669172555776L).addRole(event.getGuild().getRoleByID(433149215258968065L));
		}
	}
	ArrayList<String> messages = new ArrayList<>();


	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {

    	IChannel channel = event.getChannel();
		String message = event.getMessage().getContent().toLowerCase();

		if(message.contains("f to c")||message.contains("c to f")) {
			SlamUtils.sendMessage(channel,String.valueOf(convertTemp(message)));
		}
		if(message.contains("miles to km")||message.contains("km to miles")||
				message.contains("m to ft") || message.contains("ft to m")
				){
			SlamUtils.sendMessage(channel,String.valueOf(convertDistance(message)));
		}

		if (message.contains("cad to usd")|| message.contains("usd to cad")){
			SlamUtils.sendMessage(channel, String.valueOf(convertCurrancy(message)));
		}


    }


    private double convertCurrancy(String message){
		String[] s = SlamUtils.spiltMessage(message);
		double x = 0;
		double numberOnly= Double.parseDouble(s[0].replaceAll("[^0-9./-]", ""));
		String json;
		if(s[0].contains("cad")){
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=CAD_USD&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("CAD_USD")
					.getAsJsonObject().get("val").getAsDouble();
			double a = numberOnly * x;
			double roundOff = (double) Math.round(a * 100) / 100;
					 return roundOff;
		}
		if(s[0].contains("usd")){
			 json = SlamUtils.readUrl("http://free.currencyconverterapi.com/api/v5/convert?q=USD_CAD&compact=y.json");
			x = gson.fromJson(json,JsonObject.class).get("results")
					.getAsJsonObject().get("USD_CAD")
					.getAsJsonObject().get("val").getAsDouble();
			double a = numberOnly * x;

			double roundOff = (double) Math.round(a * 100) / 100;
			return roundOff;
					 
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
