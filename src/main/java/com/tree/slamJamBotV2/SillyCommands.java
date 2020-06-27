package com.tree.slamJamBotV2;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Keith on 11/18/2017.
 */


public class SillyCommands implements MessageCreateListener {

    private ArrayList<CSVRecord> drinks;





    public SillyCommands(){

        try {
            FileReader fr = new FileReader("drinks.csv");
            drinks = new ArrayList<>(CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fr).getRecords());
            fr.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String message = event.getMessage().getContent();


        
        
        Channel channel = event.getChannel();
        message = message.toLowerCase();
        
		String[] splitMessage = SlamUtils.spiltMessage(message);


//
        if(event.getChannel().getId() == 669108141354385418L){
            event.getMessage().delete("Only memes in the meme channel");
        }

//        if(message.startsWith("insult")){
//            System.err.println("test");
//	        try {
//		        HttpResponse<String> response = Unirest.get("https://evilinsult.com/generate_insult.php?lang=en&type=json")
//				        .header("X-Mashape-Key", "0mnOHhNeC8mshrGhzHzG8kqCDcG3p1i9khajsnL2q0cWjyLHok")
//				        .header("Accept", "text/plain")
//				        .asString();
//
//                Gson gson = new GsonBuilder().create();
//                JsonObject jsonObject = new JsonObject();
//                jsonObject = gson.fromJson(response.getBody(),jsonObject.getClass());
//
//                System.out.println("HERE HERE HERE");
//               System.out.println(jsonObject.get("insult"));
//		      //  System.err.println(response.getBody());
//		        if(splitMessage.length > 1){
//		            if(splitMessage[1].equals("me")) {
//		                event.getChannel().sendMessage(event.getMessageAuthor().getDisplayName() + " " + jsonObject.get("insult"));
//
//                    }else {
//                        event.getChannel().sendMessage(splitMessage[1] + " " + jsonObject.get("insult"));
//                    }
//		        }else {
//					event.getChannel().sendMessage(String.valueOf(jsonObject.get("insult")));
//		        }
//
//	        } catch (UnirestException e) {
//		        e.printStackTrace();
//	        }
//
//
//        }
        String unoString = "\\n\n" +
                "<:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:blank:562491174498205696>  <:Uno:595136091581841418> <:blank:562491174498205696> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418> <:blank:562491174498205696> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418>\n" +
                "<:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:blank:562491174498205696>  <:Uno:595136091581841418><:blank:562491174498205696> <:Uno:595136091581841418><:blank:562491174498205696> <:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:blank:562491174498205696> <:Uno:595136091581841418>\n" +
                "<:blank:562491174498205696> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418><:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:blank:562491174498205696> <:Uno:595136091581841418> <:blank:562491174498205696> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418> <:Uno:595136091581841418>";

        if(message.contains("uno") && !event.getMessageAuthor().isBotUser()){
            channel.asTextChannel().get().sendMessage(unoString);
        }

        if (!message.contains("no meme")) {

            if(message.contains("moon man")){
                channel.asTextChannel().get().sendMessage(test);
            }

            if (message.equals("give me a drink") || message.equals("i need a drink") || message.equals("you need a drink")) {
                giveMeADrink(channel);

            }
            /*


            if (message.contains("retarded") || message.contains("retard") || message.contains("traps are not gay")) {
                StringBuilder sb = new StringBuilder(message.length());
                for (char c : message.toCharArraNy()) {
                    sb.append(ThreadLocalRandom.current().nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
                }
                try {
                    BufferedImage sponge = ImageIO.read(new File("spongebob.jpg"));
                    byte[] b = mergeImageAndText(sponge, sb.toString(), new Point(sponge.getWidth() / 2, sponge.getHeight()));

                    FileOutputStream fos = new FileOutputStream("so" + 2 + ".png");
                    fos.write(b);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File pic = new File("so" + 2 + ".png");
                SlamUtils.sendFile(channel, pic);
            }
 */
            if (message.contains("sleepy-bye arty")) {
                event.getServer().get().getMemberById(284068848183934988L).get().move(event.getServer().get().getAfkChannel().get());
                
            }



            if (message.startsWith("come here") &&
                    event.getMessageAuthor().asUser().get().getConnectedVoiceChannel(event.getServer().get()).get() != event.getServer().get().getAfkChannel().get()) {

                String[] words = message.split("\\s");
                words[2] = words[2].replaceAll("(?:[<@!>])", "");
                event.getServer().get().getMembers().iterator().forEachRemaining(user -> {


                    if(words[2].equals(user.getIdAsString())){
                        user.move(event.getMessageAuthor().asUser().get().getConnectedVoiceChannel(event.getServer().get()).get());
                    }
                });

            }
            if (message.startsWith("sleepy-bye")) {
                String[] words = message.split("\\s");
                words[1] = words[1].replaceAll("(?:[<@!>])", "");

                event.getServer().get().getMembers().iterator().forEachRemaining(user -> {
                    if(words[1].equals(user.getIdAsString())){
                        if(words[1].equals("145676409078022144")){
                            event.getMessageAuthor().asUser().get().move(event.getServer().get().getAfkChannel().get());
                        }else{
                          user.move(event.getServer().get().getAfkChannel().get());

                        }
                    }
                });


            }
        }

    }


    private void giveMeADrink(Channel channel) {
        System.err.println(drinks.size());

        if(ThreadLocalRandom.current().nextBoolean()){
            int drinkNumber = ThreadLocalRandom.current().nextInt(0,drinks.size());

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(drinks.get(drinkNumber).get("Name"));
            embedBuilder.setDescription(drinks.get(drinkNumber).get("Description"));
            embedBuilder.addField("Effect", drinks.get(drinkNumber).get("Effect"),false);
            embedBuilder.addField("Price",drinks.get(drinkNumber).get("Price"),false);

            channel.asTextChannel().get().sendMessage(embedBuilder);
        }else {
            channel.asTextChannel().get().sendMessage(rDrinks[ThreadLocalRandom.current().nextInt(0,rDrinks.length)]);
        }




    }





    public static byte[] mergeImageAndText(BufferedImage image,
                                           String text, Point textPosition) throws IOException {
        Graphics2D g2 = image.createGraphics();



        Font f = new Font("KenVector Bold",Font.PLAIN,25);

        g2.setFont(f);

        int sw =  g2.getFontMetrics().stringWidth(text);
        g2.setColor(Color.black);
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


    String test = "\n:full_moon::full_moon::full_moon::full_moon::full_moon::full_moon::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::full_moon::full_moon::full_moon::tophat::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::full_moon::full_moon::waning_crescent_moon::new_moon::waxing_crescent_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::full_moon::waning_crescent_moon::new_moon::new_moon::new_moon::first_quarter_moon:\n" +
            "\n" +
            ":full_moon::full_moon::waning_gibbous_moon::new_moon::eye::new_moon::eye::first_quarter_moon:\n" +
            "\n" +
            ":full_moon::full_moon::last_quarter_moon::new_moon::new_moon::lips::new_moon::waxing_gibbous_moon:\n" +
            "\n" +
            ":full_moon::full_moon::waning_crescent_moon::new_moon::new_moon::new_moon::waxing_crescent_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::waning_crescent_moon::new_moon::new_moon::ribbon::first_quarter_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::waning_crescent_moon::new_moon::eggplant::new_moon::waxing_gibbous_moon::full_moon:\n" +
            "\n" +
            ":full_moon::full_moon::waning_crescent_moon::waxing_gibbous_moon::last_quarter_moon::new_moon::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::waning_gibbous_moon::waxing_crescent_moon::full_moon::last_quarter_moon::waxing_crescent_moon::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::last_quarter_moon::first_quarter_moon::full_moon::last_quarter_moon::first_quarter_moon::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::waning_crescent_moon::waxing_gibbous_moon::full_moon::last_quarter_moon::first_quarter_moon::full_moon::full_moon:\n" +
            "\n" +
            ":full_moon::high_heel::full_moon::full_moon::full_moon::high_heel::full_moon::full_moon:";

    String[] rDrinks = {
            "The Flaming Dragon - A spicy beer that causes the user to breathe fire when they burp.",
            "The Banshee's Breath - white, swirling liquor made from a special translucent wild berry. Tastes sweet, has an effect similar to mint gum in that it always feels cold.",
            "The Mountain's Bounty - A fine liquor made using water from a glacial stream. Always refreshing, and always makes you feel cold no mater the weather.",
            "Mawxie - A drink all the locals cite as a local treasure. Tastes disgusting.",
            "Bog Grog - A mix of Rum, Orange Juice and fermented herbs that, when drunk, causes the user to gain advantage on saving throws against being poisoned for 30 minutes.",
            "Frost Mead - Honey and the tear of an Ice Giant make this shot. The crackling blue sparkle and the jet of icy breath you have for rest of the day is worth the expense.",
            "Weatherbee’s Whirler - Invented by the perhaps too inventive Filbus Weatherbee, this drink is testimony as to why it is a bad idea to point a gnome’s sharp mind towards the creation of a new brew. After spending many years living among the dwarves, Weatherbee made his way home with a drink even the stout folk couldn’t handle. This monstrosity is laced with latent magic designed to lessen the chance of drinkers dying from its ungodly alcohol content, and reportedly tastes like \"A kick in the face from a horse.\" After one shot of this drink, the drinker is shunted into a chaotic haze of blurry awareness, bolstered confidence, and overpowering drunkenness. In addition, the latent magic in the brew causes minor, uncontrolled magical effects to occur around the drinker at random times during the haze. The nature of these effects is up to either the DM or the player, so long as the effects are sufficiently insignificant. After 1d4 hours, the haze drops away and the drinker immediately and almost violently falls into a deep sleep so that they may recover.",
            "Dragonborn Bloodwine - When you drink it, you are able to use a breath attack once within the next 10 minutes.",
            "The Quieker - nasty rum that gives a high pitched voice for 1d4 hours.",
            "Faerie Fireball - a delicious cinnamon whiskey made with a touch of Fey magic. Causes uncontrollable hiccups for 1d4 hours. With each hiccup a small cloud of shimmering breath is released.",
            "Lily in a Well - a tall mug of ale, half full with an edible flower garnish.",
            "Hammer Beer - One glass will make you feel like you just hit yourself with a hammer. Minus 5 HP.",
            "Dragon's Piss - A beer that tastes exactly like one would suspect by its name. For sure not a drink you need a second one of.",
            "Shamrock Shake - Instant dc15 con save. On fail the patron is incapacitated. No save required if patron has Irish (sounding) accent.",
            "The Sun's Glory - A citrusy cider that makes your eyes glow like an Aasimar.",
            "Black Midnight - A drink created by necromancers to honor fallen necromancers, Bitter with a touch of rum. Those who drink it have nightmares of dying and spending an eternity rotting away inside a coffin. This helps enforce necromancers to contemplate their own moralities.",
            "The Phoenix - A peppery drink that burns on the way down, and then again on the way out. It is often used as a prank on drunk companions, who have a nasty surprise waiting for them the next time they go to relieve themselves.",
            "Hair of the Bloodhound - Once you have become intoxicated on this brew, you gain the usual drawbacks of drunkenness but gain advantage on survival checks.",
            "Seer's Solution - A mildly viscous green liquid. The first two shots have no effect. The third gives you truesight up to 60ft for 1d4 minutes. The fourth and subsequent shots give the drinker horrible audio/visual hallucinations for 1d4+2 hours. Counter resets at dawn.",
            "Spider's Bite - Take 1d8 poison damage on a failed CON saving throw. Packs a mean punch.",
            "The Fortnight - Very strong alcohol. If you actually drink enough to get drunk, you stay hammered for days.",
            "Tinkerer's Tincture - dark and smooth, and when you drink it all the clicking, whistling, and scraping noises are more apparent to your ear.",
            "2 Couples in a Shared Household - (much easier if teabags exist) Two different types of hot tea, 2 of each, into one mug.",
            "Sucker Punching a Rabbit - A single teabag steeped in the biggest mug you have.",
            "Buried Treasure - A single, very sweet, rather expensive hard candy is stuck to the bottom of a mug of very hard liqour. Once you've drunk it all, you get a spoon to pull it off with.",
            "Ouch - Two full shots worth of lemon juice put into a glass of very high proof alcohol.",
            "What was I Saying? - An unassuming shot of very strong alcohol, with a cherry in it, usually taken in the middle of a conversation, which is promptly ended.",
            "Actual Torture - 2 Teaspoons of salt which are to be eaten all at once. Then washed down with a citrus based liquor. If anyone else offers any drinks, their hands are free game for attack.",
            "The Green Kobold - The first drink to ever be served in a piece of ham, with the skin. 1 shot of herbal liquor wrapped in ham. To be eater all at once. Probably fixed in place with at least 1 pin, make sure you pick it out before you eat it.",
            "End of the Line - Very high quality, rather expensive alcohol. A coin is flipped. If heads, your drink is free. If tails, you are forced to drink until you either die or pass out. If you regain consciousness, you must continue drinking.",
            "Traffic Stop - Invented by a Diviner. Whenever a fight seems to be brewing, everyone orders a traffic stop. It's a mug of 3 separate liquors that stay separated in their mug, all very strong.The goal is to drink it all before the local police forces arrive.",
            "Dragon Milk - not really milk, or related to dragons. It's an expensive white drink, resembling milk (duh) that removes any alcohol in your body. As a result you exale fire in the form of a single burp, resembling a dragon.",
            "The Necromancer - a drink for those who fall unconcious from alcohol. It's a green glowing liquid. Also known as the 'Corpse Reviver'; when poured into the unconcious persons mouth he/she gets up and walks in a way similair to a zombie.",
            "Beholder - a delicious drink decorated with an eye or multiple smaller ones. Feels like normal alcohol but gives the person a (false of course) feeling he/she has multiple eyes after drinking enough of it.",
            "Elysium - a nonalcoholic drink that smells and looks as bad as it tastes. Some compared it to trash, vomit or even excrement but only because they couldn't find the adequate foul words. Most refuse to look at it, let alone allow it to come close to their nose. Only those with the strongest will manage to gulp it down. Once drunk, the person experiences true bliss, which seems to last for decades. In reality it's a few seconds.",
            "Nine Steps - commonly known as 'The Niner' or by it's full name 'Nine Steps to Hell'. It is a liquid that when left to settle separates into 9 parts, the bottom one being pure black and the top a beautiful red with a gradient in between. After drinking it the person seems frozen for a few seconds, but to the person who gulpes it down it feels like days, weeks, maybe even months of 9 different experiences, all basically a form of torture. Often used as a torture method but sometimes drunk to prove ones mental strenght, as those that can't endure it go insane. The niner is a rare drink because it's extremely hard to make. An amateur making it, if the ingridients aren't correct to the milligram, makes a drink that causes instant death.",
            "Honey Pine Dew - An imported halfling mead, served in small cups. Very pleasant taste, cheap in halfling towns, but expensive elsewhere.",
            "Cubed Spirit - This drink is served as a hollow ice cube with liquid spirit within. As the ice melts in your mouth the drink will come out. How exactly this novelty drink is produced, is a well kept secret.",
            "Milky Way Whisky - A light blue drink that tastes like very watered down, sweetened milk with a lot of alcohol. Besides giving a quick buzz, it also gives bone, and thus teeth, a blue fluorescent glow for 1d4 hours.",
            "The house special - This drink doesn't have a particular name, but it's cheap. It doesn't really have an effect. In fact, you're pretty sure the bartender is simply casting prestidigitation on dishwater to make it raste like like it has alcohol in it.",
            "True Dwarven Stout - A strong drink, not recommended if you cannot handle your alcohol. Traditionally served on the rocks, literally. There are pieces of stone lying on the bottom of your drink. Said to give the true mining flavor. This drink will make any dwarve feel very nostalgic.",
            "Petralias Wine - A very expensive wine that is served as a single droplet. It does nothing for thirst or getting drunk, bit the flavor is said to be very concentrated and the lack of drink quantity should make the experience richer. Typically ordered by very pretentious people.",
            "Golden Goat - Fermented goat milk and honey.",
            "Star Liquid - A really black drink resembling the night sky. If you drink it you experience a wonderful journey trough the stars for 1 min.",
            "Polymorphic Brew - Commonly used in drinking games, this brew will turn the user into an animal when they burp. They turn back about a minute later.",
            "Insom's Ale - has same effects as a long rest, calming.",
            "Lilphina’s Lusty Lover Liquid Liquor - The bottle comes in two parts, with each part having a different hue of color depending on the flavor. When two persons consume the drink within 5 minutes of one another, their minds are swapped for 1 hour.",
            "Piña Colossus - a rare colossal pineapple hollowed out and filled with rum, coconut cream, and pineapple juice. Usually a shared drink. The pineapple shrinks in size when the liquid is drank or spilled. It will also continue to expand and grow to colossal proportions the more liquid that is added.",
            "The Coup de Grace - At the end of the night, the bartop is wiped down with a rag, the contents are rung into a shot glass.",
            "Drippzt - The drops in the bottoms of emptied kegs mixed together and dyed black. Anyone who can drink a pint of it without vomiting doesn’t have to pay.",
            "Firebreath Ale - If someone drinks it and then breathes into a flame, a 3rd level Fireball is cast centered on the flame.",
            "Belching rum - After trunking this make a con save. On a failed save the effect takes place immediately. On a success you choose when to have the effect take place (After ten minutes the effect takes place no matter what.) Effect: You belch thunderously. Everyone in a 100 foot radius is defened for one round.",
            "The Titans brew - A regular tasting ale served in an enormous cup, and after drinking it the receiver grows a few inches.",
            "Paladin’s Bane - sweet enough to tempt the righteous and you hardly taste the alcohol, but it’ll give you a decent hangover and diabetes to boot.",
            "Goodberry Gin - if you drink enough of it, it works as a mild healing potion which may or may not compensate for the damage done to your liver or you falling down the stairs while drunk. 1d6 hp healing, 1d4 drunk damage.",
            "Hagraven Brandy - though it has a taste as ugly as its namegiver, the appearance of everyone around you will exponentially improve with every sip you take. -3 on Charisma saving throws for 1d6 hours.",
            "Will-o-the-Whiskey - whisky with minor hallucinatory effects, starts with a tiny tingling light in the corner of your eyes, ends with a shining orb of light dancing a few yards away from you, moving away as you try to catch it.",
            "Mandrake Mocha - a hot creamy beverage with a narcotic effect. Dulls the pain, leads you into a deep slumber.",
            "Madman’s Mead - downing a mug causes a fleeting bout of insanity, roll a d100 for effects of Short-Term Madness.",
            "Ochre Stout - a cheap drink so thick you can taste chunks in it. Best to swallow and not be curious.",
            "The Drunken Dwarf - a pint of dwarven stout with a teabag in it.",
            "For (local deity)’s Sake - a local sake or rice wine, popular with priests.",
            "Ciderella - a sweet apple cider, considered \"a girls drink\". Even the toughest bloke will begin giggling like a little girl after a few drinks.",
            "Jalapálinka - a fruit brandy spiced with hot peppers. Burns the throat, downing a pitcher leads to steam escaping ears and nostrils.",
            "Ginger Ale - Wait, this doesn’t taste like ginger at all... An ale that turns your hair ginger, effect lasts for 1d6 days.",
            "The Maiden’s Ass - a quadrupel beer, served in a bottle with a donkey and a pretty girl on the label. Sweet but strong.",
            "Bock Bear - a bock beer that gives you +1 Str and extra body hair for 1d6 hours.",
            "Polypilsener - turns you into a canary for 1d4 minutes. Drinking half a mug will turn you halfway into a canary.",
            "Our Thoughts and Prayers - a brandy that works as a reverse Detect Thoughts spell; surrounding people learn your surface thoughts, although you’re unaware of it. Range increases with 2ft each glass, though your thoughts don’t exactly get more coherent.",
            "Smirgnome - a vodka that fills your brain with the weirdest ideas, although the morning after you’ll likely have no memory of inventing a sunlight-storing clockwork pigeon to hunt vampires with.",
            "Abbathor’s Gold - a clear golden mead that does nothing to quench your thirst - instead, you crave more of it. Roll a Charisma saving throw (DC 10) after each glass; if you fail, you keep drinking. After 8 glasses you pass out for 1d10 hours.",
            "Coala - a Dwarven invention, this black bubbly drink tastes like grinded coal with sugar, but also makes you feel reinvigorated and less tired.",
            "Cinder - a spiced cider served hot. You can control non-magical flame that fits within a 1ft cube for 1d10 minutes.",
            "The Umber Hulk - a pint of whisky, brandy and tequila in equal measures. Good luck.",
            "Bottomless pint - the bartender pours beer into a ceramic mug. When the patron pulls the glass up to their mouths, they realize that the mug really doesn’t have a bottom. The mug is empty, and the beer has been pour through the mug into a hole in the bar with a pitcher underneath.",
            "Copperhead - the bartender pours a beer, mixes in a shot of whiskey, and then a couple drop of snake venom from a vial. Normally drinkers will feel numb in their extremities. If a drinker fails their saving throw, they will be paralyzed from the neck down for 1D20 minutes.",
            "Brazenbrew - Served in a special mug laced with bronze, the drinker is more apt to make outrageous claims of ability, but also gains the relevant luck to succeed while still under the influence.",
            "Yam's Choice - A delightful mead, high class, has a fair chance of being extremely addictive. Withdrawal is applying two effects from the long-term madness chart, and one from the short-term chart.",
            "Witchwood Absinthe - A potent spirit the color of a moss-covered tombstone. It has mild hallucinogenic properties, and local folklore holds you can hear the voices of those you've lost if you drink enough. Not too much, though. You might join them.",
            "Salty Dog Ale - A dark, rich brew that reminds you of the sea. Plopping in the shell of a sea snail for good luck is customary, and adds a fitting salinity to the drink.",
            "Hymvaren's Luck - A bright, golden-colored beer named after a local drunk who woke up on the beach after a night of carousing with a chest full of pirate's treasure. He claims to have no memory of that night.",
            "Bourbon of Dwarfkind - makes the user start to grow a beard. If they can normally, you see accelerated growth. If they can’t, a few hairs will sprout on their chin.",
            "Old Mill Rye - tastes alright, has a strange aftertaste in the back of your throat of an old sock. It's cheap and gets you drink",
            "[Insert local diety's name] brew - Has characteristics that reminds you of said god. Gold for sun worship for example. Hint of cinnamon for a hot and fiery one. A hint of licorice root perhaps",
            "Ochre Jelly Ale - Ale with safe-to-drink ochre jelly mixed in it.",
            "A regular glass of water - That’s it. It’s just a normal glass with regular, boring water inside.",
            "Mimic Drink - Usually sold by tricksters to play pranks on people. Looks like a regular glass of water, but a tiny water elemental/water weird is disguised as normal water.",
            "Liquid Nitrogen - Drank by frost giants and other beings that can tolerate extreme cold.",
            "Mead of invulnerability - Once drunk user makes a DC 15 Wisdom check. On a fail believes they are immune to all damage and if damage is dealt to them believe they did not take the damage. Effects for ten minutes. User still take all damage as normal.",
            "Inverted rum - when you drink it, every one around you in a 15 foot cube becomes drunk. This dose not include yourself.",
            "Goblin Spit - whiskey and gin mixed with the barkeep's home-made mints. It tastes surprisingly good despite its name. As is tavern tradition, a long-distance spitting competition occurs after every round.",
            "The Hook and Slider - a cooked goat (?) intestine tied and filled with a heavy beer. After finishing the beer chow on the intensive",
            "Lucky Leprechaun - A sickly green drink that gives you advantage on Charisma checks for one hour.",
            "The Tiamat - 5 different shots, one for each color of the different heads. One is black and syrupy, one blue and gives tingly feeling, one is on fire, one is green and tastes a bit minty, the last is white and frosts the closest things. They are mixed together and separate in the cup making a very nice presentation.",
            "The Sweet Roll - flavors of cinnamon and sugar blend with the strong scent of rum. The drinker gains an additional 1d4 to any pickpocket attempts for the next hour.",
            "The Sailor's Spirit - There once was a cap' and a crew, Who made the most wonderful brew, From rations of lime, They would in their spare time, Make fine drinks no man could outdo.",
            "Good Hearth's Brew - A hot spiced rum which is popular during long winter nights for the immediate feeling of warmth and calm that follows.",
            "Tarnation - A strong spiced cider served warm and traditionally drank as quickly as posible after a boisterous cheer or a lewd drinking song. Enchantmented with the effect of the consumer belching a small flame right after ingesting.",
            "Gnome Rum - Makes your voice high, squeaky, and annoying.",
            "Ethereal Ale - The more intoxicated you get, the more you fade into the ethereal plane. First you become slightly transparent, than objects start to phase through you from tame to time and if you manage to keep drinking you entirely enter the ethereal plane.",
    };

}
