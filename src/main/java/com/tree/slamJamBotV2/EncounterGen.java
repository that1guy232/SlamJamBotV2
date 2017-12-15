package com.tree.slamJamBotV2;

import de.btobastian.sdcf4j.Command;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by Keith on 11/23/2017.
 */
public class EncounterGen {


    private ArrayList<CSVRecord> records;
    private ArrayList<CSVRecord> pickedCreature;
    public EncounterGen(){
        records = new ArrayList<>();
        try {
            Reader in = new FileReader("Monsters.csv");
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Command(aliases = "!encounter",async = true)
    public void encounterGen(String[] args){

    }


}
