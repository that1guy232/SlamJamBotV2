package com.tree.slamJamBotV2;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Keith on 11/18/2017.
 */
public class SillyCommands {


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();
        message = message.toLowerCase();




        if(message.contains("retarded") || message.contains("retard")){
                        StringBuilder sb = new StringBuilder(message.length());
            for(char c : message.toCharArray()){
                sb.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
            }
            try {
                BufferedImage sponge = ImageIO.read(new File("spongebob.jpg"));
                byte[] b = mergeImageAndText(sponge, sb.toString(), new Point(sponge.getWidth()/2, sponge.getHeight()-30));
                FileOutputStream fos = new FileOutputStream("so2.png");
                fos.write(b);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SlamUtils.sendFile(channel,new File("so2.png"));
            //SlamUtils.sendMessage(channel,sb.toString());
        }


        if(message.equalsIgnoreCase("meme me")){
            switch (ThreadLocalRandom.current().nextInt(1, 3)){
                case 1:
                    SlamUtils.sendMessage(event.getChannel(),"https://www.youtube.com/watch?v=Y4Z7Ds_yv8o");
                    break;
                case 2:
                    SlamUtils.sendMessage(event.getChannel(),"https://youtu.be/LGgWWL9lTZs");
                    break;
                case 3:
                    SlamUtils.sendFile(channel,new File("corc.jpg"));
                break;


            }


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


    }

    public static byte[] mergeImageAndText(BufferedImage image,
                                           String text, Point textPosition) throws IOException {
       // BufferedImage im = ImageIO.read(new File(imageFilePath));
        Graphics2D g2 = image.createGraphics();



        g2.setFont(new Font("KenVector Bold",Font.PLAIN,25));

        int sw =  g2.getFontMetrics().stringWidth(text);
        g2.setColor(Color.BLACK);
        //ArrayList<String> strings = new ArrayList<>();
        if(image.getWidth() < sw){
            String[] words = text.split("\\s");
            int currentWidth = 0;
            StringBuilder s1 = new StringBuilder();
            StringBuilder s2 = new StringBuilder();
            int i = 0;
            while (currentWidth < image.getWidth()){
                s1.append(words[i]);
                s1.append(" ");
                currentWidth += g2.getFontMetrics().stringWidth(s1.toString());
                i++;
            }
            for (int j = i; j < words.length; j++) {
                s2.append(words[j]);
                s2.append(" ");

            }
            g2.drawString(s2.toString(), textPosition.x - (g2.getFontMetrics().stringWidth(s2.toString())/2), textPosition.y+g2.getFontMetrics().getHeight()-25);
            g2.drawString(s1.toString(), textPosition.x - (g2.getFontMetrics().stringWidth(s1.toString())/2), textPosition.y-25);
        }else {
            g2.drawString(text, textPosition.x - (sw/2), textPosition.y);
        }











        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

}
