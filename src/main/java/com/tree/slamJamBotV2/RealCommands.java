package com.tree.slamJamBotV2;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands {

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
			File f = new File("Commands.json");
			SlamUtils.sendFile(event.getChannel(), f);
		}

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
