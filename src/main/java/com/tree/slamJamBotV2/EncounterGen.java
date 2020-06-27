package com.tree.slamJamBotV2;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by Keith on 11/23/2017.
 */
public class EncounterGen {

	private ArrayList<CSVRecord> ArcticCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> CoastalCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> DesertCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> ForestCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> GrasslandCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> HillCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> MountainCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> SwampCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> UnderdarkCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> UnderwaterCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> urbanCreatures = new ArrayList<>();
	private ArrayList<CSVRecord> OtherPlaneCreatures = new ArrayList<>();

	public EncounterGen() throws IOException {


		Iterable<CSVRecord> records = null;
		try {
			Reader in = new FileReader("Monsters.csv");
			records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);


		} catch (IOException e) {
			e.printStackTrace();
		}

		if(records != null){
			BufferedWriter br = new BufferedWriter(new FileWriter("a.csv"));
			br.write(String.valueOf(records.iterator()));

			records.forEach(a->{

				if(a.get("Arctic").equals("placeInActivites")){
					ArcticCreatures.add(a);
				}
				if(a.get("Coastal").equals("placeInActivites")){
					CoastalCreatures.add(a);
				}
				if(a.get("Desert").equals("placeInActivites")){
					DesertCreatures.add(a);
				}
				if(a.get("Forest").equals("placeInActivites")){
					ForestCreatures.add(a);
				}
				if(a.get("Grassland").equals("placeInActivites")){
					GrasslandCreatures.add(a);
				}
				if(a.get("Hill").equals("placeInActivites")){
					HillCreatures.add(a);
				}
				if(a.get("Mountain").equals("placeInActivites")){
					MountainCreatures.add(a);
				}
				if(a.get("Swamp").equals("placeInActivites")){
					SwampCreatures.add(a);
				}
				if(a.get("Underdark").equals("placeInActivites")){
					UnderdarkCreatures.add(a);
				}
				if(a.get("Underwater").equals("placeInActivites")){
					UnderwaterCreatures.add(a);
				}
				if(a.get("Urban").equals("placeInActivites")){
					urbanCreatures.add(a);
				}
				if(a.get("Other Plane").equals("placeInActivites")){
					OtherPlaneCreatures.add(a);
				}
			});
		}
		records = null;

	}

	public CSVRecord[] getUrbanMonster(int amount){
		CSVRecord[] x = new CSVRecord[amount];
		if(amount > urbanCreatures.size()-1){
			return null;
		}
		for (int i = 0; i < amount; i++) {
			int r = ThreadLocalRandom.current().nextInt(0,urbanCreatures.size()-1);
			x[i] = urbanCreatures.get(r);
		}


		return x;
	}


}
