package com.tree.slamJamBotV2;

import com.vdurmont.emoji.EmojiManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Keith on 11/18/2017.
 */


public class SillyCommands {

    ArrayList<CSVRecord> drinks;


    public SillyCommands(){
        try {
            drinks = new ArrayList<>(CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new FileReader("drinks.csv")).getRecords());





        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        IChannel channel = event.getChannel();
        message = message.toLowerCase();
        IGuild guild = event.getGuild();

        if(message.equals("traps are gay")){
            SlamUtils.sendFileWithMessage(channel,"<@284068848183934988>",new File("traps.png"));
        }
        if(message.contains("good bot")){

            event.getMessage().addReaction(EmojiManager.getForAlias("smile"));
        }
        if(message.equals("bad bot")){
            event.getMessage().addReaction(EmojiManager.getForAlias("disappointed_relieved"));
        }
        if(message.equals("give me a drink")|| message.equals("i need a drink") || message.equals("you need a drink")){
            giveMeADrink(channel);

        }
        if(message.contains("retarded") || message.contains("retard") || message.contains("traps are not gay")){
            StringBuilder sb = new StringBuilder(message.length());
            for(char c : message.toCharArray()){
                sb.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
            }
            try {
                BufferedImage sponge = ImageIO.read(new File("spongebob.jpg"));
                byte[] b = mergeImageAndText(sponge, sb.toString(), new Point(sponge.getWidth()/2, sponge.getHeight()));

                FileOutputStream fos = new FileOutputStream("so"+2+".png");
                fos.write(b);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File pic = new File("so"+2+".png");
            SlamUtils.sendFile(channel,pic);
        }

        if(message.contains("sleepy-bye arty")){

            guild.getUserByID(284068848183934988L).moveToVoiceChannel(guild.getVoiceChannelByID(376655874543976448L));

        }

        if(message.contains("aaaa")){
            SlamUtils.sendMessage(event.getChannel(),"https://www.youtube.com/watch?v=Y4Z7Ds_yv8o");
        }
        if(message.contains("reee")){
            SlamUtils.sendMessage(event.getChannel(),"https://youtu.be/LGgWWL9lTZs");
        }
        if(message.equalsIgnoreCase("easy breezy beautiful crocodile") || message.equalsIgnoreCase("ebbc")){
            SlamUtils.sendFile(channel,new File("corc.jpg"));
        }
        if(message.contains("it's fine") || message.contains("its fine")){
            SlamUtils.sendFile(channel,new File("its_fine.jpg"));
        }
        if(message.contains("on fire")){
            SlamUtils.sendFile(channel,new File("on_fire.jpg"));
        }




    }



    private void giveMeADrink(IChannel channel) {

        int drinkNumber = ThreadLocalRandom.current().nextInt(0,drinks.size());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle(drinks.get(drinkNumber).get("Name"));
        embedBuilder.withDescription(drinks.get(drinkNumber).get("Description"));
        embedBuilder.appendField("Effect", drinks.get(drinkNumber).get("Effect"),false);
        embedBuilder.appendField("Price",drinks.get(drinkNumber).get("Price"),false);

        RequestBuffer.request(() -> channel.sendMessage(embedBuilder.build()));


    }




    public static byte[] mergeImageAndText(BufferedImage image,
                                           String text, Point textPosition) throws IOException {
        Graphics2D g2 = image.createGraphics();



        g2.setFont(new Font("KenVector Bold",Font.PLAIN,25));

        int sw =  g2.getFontMetrics().stringWidth(text);
        g2.setColor(Color.BLACK);
        ArrayList<String> stringsForImage = new ArrayList<>();
        if(image.getWidth() < sw){
            String[] words = text.split("\\s");
            int currentWidth = 0;

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                if(currentWidth > image.getWidth()-250){
                    stringsForImage.add(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    currentWidth = 0;
                }
                stringBuilder.append(words[i]);
                stringBuilder.append(" ");
                currentWidth += g2.getFontMetrics().stringWidth(words[i]);

            }
            stringsForImage.add(stringBuilder.toString());
            Collections.reverse(stringsForImage);
            int y = 15;
            for (int i = 0; i < stringsForImage.size(); i++) {
                y += 40;

                if(i == 0){

                    g2.drawString(stringsForImage.get(i),textPosition.x - (g2.getFontMetrics().stringWidth(stringsForImage.get(i))/2),textPosition.y-15);
                }else {

                    g2.drawString(stringsForImage.get(i),textPosition.x - (g2.getFontMetrics().stringWidth(stringsForImage.get(i))/2),textPosition.y+g2.getFontMetrics().getHeight()-y);
                }
            }


        }else {
            g2.drawString(text, textPosition.x - (sw/2), textPosition.y);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

}
