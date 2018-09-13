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

				if(a.get("Arctic").equals("x")){
					ArcticCreatures.add(a);
				}
				if(a.get("Coastal").equals("x")){
					CoastalCreatures.add(a);
				}
				if(a.get("Desert").equals("x")){
					DesertCreatures.add(a);
				}
				if(a.get("Forest").equals("x")){
					ForestCreatures.add(a);
				}
				if(a.get("Grassland").equals("x")){
					GrasslandCreatures.add(a);
				}
				if(a.get("Hill").equals("x")){
					HillCreatures.add(a);
				}
				if(a.get("Mountain").equals("x")){
					MountainCreatures.add(a);
				}
				if(a.get("Swamp").equals("x")){
					SwampCreatures.add(a);
				}
				if(a.get("Underdark").equals("x")){
					UnderdarkCreatures.add(a);
				}
				if(a.get("Underwater").equals("x")){
					UnderwaterCreatures.add(a);
				}
				if(a.get("Urban").equals("x")){
					urbanCreatures.add(a);
				}
				if(a.get("Other Plane").equals("x")){
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
