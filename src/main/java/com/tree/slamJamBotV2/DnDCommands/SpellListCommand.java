package com.tree.slamJamBotV2.DnDCommands;

import com.google.common.collect.Lists;
import com.tree.slamJamBotV2.SlamUtils;
import com.vdurmont.emoji.EmojiManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellListCommand {

	public long getSpellListMessageID() {
		return spellListMessageID;
	}

	public void setSpellListMessageID(long spellListMessageID) {
		this.spellListMessageID = spellListMessageID;
	}

	public int getSpellListMessagePage() {
		return spellListMessagePage;
	}

	public void setSpellListMessagePage(int spellListMessagePage) {
		this.spellListMessagePage = spellListMessagePage;
	}

	public ArrayList<EmbedObject> getSpellEmbeds() {
		return spellEmbeds;
	}

	public void setSpellEmbeds(ArrayList<EmbedObject> spellEmbeds) {
		this.spellEmbeds = spellEmbeds;
	}

	private long spellListMessageID;
	private int spellListMessagePage;
	List<CSVRecord> dnd5eSpells;
	ArrayList<EmbedObject> spellEmbeds;
	public SpellListCommand() {
		try {
			FileReader fr = new FileReader("5eSpells.csv");
			dnd5eSpells = new ArrayList<>(CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fr).getRecords());
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		buildSpellList();
	}


	private void buildSpellList() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		spellEmbeds = new ArrayList<>();
		List<List<CSVRecord>> spellNameList = Lists.partition(dnd5eSpells,25);

		StringBuilder stringBuilder = new StringBuilder();




		for (int i = 0; i < spellNameList.size(); i++) {
			embedBuilder.withTitle("Page"+ (i+1) + "/" + spellNameList.size());
			for (int j = 0; j < spellNameList.get(i).size(); j++) {

				stringBuilder.append(spellNameList.get(i).get(j).get("name"));
				stringBuilder.append("\n");
			}
			embedBuilder.appendField("Spells",stringBuilder.toString(),false);

			embedBuilder.withFooterText("To learn more about a spell type \"dnd spell name\"");

			stringBuilder.delete(0,stringBuilder.length());

			spellEmbeds.add(embedBuilder.build());

			embedBuilder.clearFields();
		}

	}


	public void sendSpellList(IChannel channel) {
		ArrayList<String> emojiList = new ArrayList<>();
		emojiList.add("arrow_left");
		emojiList.add("arrow_right:");
		emojiList.add("x");

		if (getSpellListMessageID() != 0) {
			channel.getMessageByID(getSpellListMessageID()).delete();
		}

		setSpellListMessageID(SlamUtils.sendEmbed(channel, spellEmbeds.get(0)));
		setSpellListMessagePage(0);
		System.err.println(channel.getMessageByID(getSpellListMessageID()).getContent());

		try {
			RequestBuffer.request(() -> channel.getMessageByID(getSpellListMessageID()).addReaction(EmojiManager.getForAlias(emojiList.get(0)))).get();
			RequestBuffer.request(() -> channel.getMessageByID(getSpellListMessageID()).addReaction(EmojiManager.getForAlias(emojiList.get(1)))).get();
			RequestBuffer.request(() -> channel.getMessageByID(getSpellListMessageID()).addReaction(EmojiManager.getForAlias(emojiList.get(2)))).get();
		}catch (NullPointerException e){

			System.err.println(e.getCause().fillInStackTrace().toString());
		}
	}

	public void getSpell(IChannel channel,String[] spellName) {
		StringBuilder stringBuilder = new StringBuilder();

		System.err.println(spellName.length);
		System.err.println(Arrays.toString(spellName));
		for (int i = 0; i < spellName.length - 1; i++) {
			stringBuilder.append(spellName[i].toLowerCase());

			stringBuilder.append(" ");

		}
		stringBuilder.append(spellName[spellName.length - 1].toLowerCase());
		System.err.println(stringBuilder.toString());
		for (CSVRecord spells : dnd5eSpells) {

			//spells.get("name").toLowerCase().contains(stringBuilder.toString())
			if (spells.get("name").toLowerCase().matches(stringBuilder.toString())) {
				EmbedBuilder embedBuilder = new EmbedBuilder();
				embedBuilder.withTitle(spells.get("name"));
				embedBuilder.withDesc(spells.get("description"));

				embedBuilder.appendField("Level",spells.get("level"),true);
				embedBuilder.appendField("Casting time",spells.get("casting_time"),true);
				embedBuilder.appendField("Duration",spells.get("duration"),true);
				embedBuilder.appendField("Range",spells.get("range"),true);
				embedBuilder.appendField("components", spells.get("components/raw"),true);
				embedBuilder.appendField("School",spells.get("school"),true);

				SlamUtils.sendEmbed(channel, embedBuilder.build());
				break;

			}
		}
	}

	public void decrementPage(IReaction reaction) {
		spellListMessagePage--;
		if (spellListMessagePage < 0){
			spellListMessagePage = spellEmbeds.size()-1;
			RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));

		}else {

			RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
		}
	}

	public void incrementPage(IReaction reaction) {
		spellListMessagePage++;
		if(spellListMessagePage > spellEmbeds.size()-1){
			spellListMessagePage = 0;
			RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
		}else {
			RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
		}

	}
}
