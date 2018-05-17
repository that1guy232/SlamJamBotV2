package com.tree.slamJamBotV2.memeCommands;

public class MemeCommand {
	String emotes[];

	MemeCommand(String[] names,String message,  String[] emotes, Boolean exact, String[] filePaths) {
		this.names = names;
		this.message = message;
		this.emotes = emotes;
		this.filePaths = filePaths;
		this.exact = exact;
	}

	String names[];
	String message;
	String filePaths[];
	Boolean exact;


}
