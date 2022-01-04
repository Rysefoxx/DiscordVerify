/*
 * MIT License
 *
 * Copyright (c) 2021. Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.rysefoxx.verifybot.listener;

import com.github.rysefoxx.verifybot.VerifyBot;
import com.github.rysefoxx.verifybot.util.EmbedCreator;
import com.github.rysefoxx.verifybot.util.RandomString;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Rysefoxx | Rysefoxx#6772
 * @since 1/3/2022
 */
public class MessageListener extends ListenerAdapter {

    //verify <spieler>

    private final VerifyBot plugin;

    public MessageListener(@NotNull VerifyBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        Member member = event.getMember();
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();

        if (!channel.getName().equalsIgnoreCase("verify")) return;
        if (member == null) return;
        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;
        if (!args[0].equalsIgnoreCase("!verify")) {
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
            return;
        }

        Role role = member.getRoles().stream().filter(memberRole -> memberRole.getName().equalsIgnoreCase("verified")).findAny().orElse(null);
        if (role != null) {
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
            return;
        }

        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        if (args.length != 2) {
            channel.sendMessageEmbeds(new EmbedCreator().setColor(Color.RED).setTitle(":x: | Verifizierung fehlgeschlagen!").setDescription("Um die Verifizierung zu starten, gehe wie folgt vor:").addField("1 Schritt:", "Kommando richtig aufrufen - **!verify**", true).addField("2 Schritt:", "Spielernamen angeben - **TestSpieler**", true).addField("3 Schritt:", "Schritt 1 & 2 zusammensetzen und abschicken - **!verify TestSpieler**", true).toEmbed()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            channel.sendMessageEmbeds(new EmbedCreator().setColor(Color.RED).setTitle(":x: | Verifizierung fehlgeschlagen!").setDescription("Der Spieler muss für die Verifizierung auf dem Server sein.").toEmbed()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if (this.plugin.getVerifyDataManger().inVerifyProcedure(member)) {
            channel.sendMessageEmbeds(new EmbedCreator().setColor(Color.RED).setTitle(":x: | Verifizierung ausstehend!").setDescription("Du kannst keinen Account verifizieren, da du eine ausstehende Verifizierung hast.").toEmbed()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if (this.plugin.getVerifyDataManger().inVerifyProcedure(target.getUniqueId())) {
            channel.sendMessageEmbeds(new EmbedCreator().setColor(Color.RED).setTitle(":x: | Verifizierung ausstehend!").setDescription("Der Minecraft Account wird gerade von jemanden verifiziert.").toEmbed()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        String key = new RandomString(8, ThreadLocalRandom.current()).nextString();
        this.plugin.getVerifyDataManger().addKey(target.getUniqueId(), key);
        this.plugin.getVerifyDataManger().addVerifyMember(member);
        this.plugin.getVerifyDataManger().addDiscordData(target.getUniqueId(), member);
        author.openPrivateChannel().complete().sendMessageEmbeds(new EmbedCreator().setColor(Color.GREEN).setTitle(":vertical_traffic_light: | Dein Key wurde generiert!").setDescription("Gebe im Minecraft Chat ||**/verify " + key + "**|| ein.").addField("Discord User:", author.getAsTag(), true).addField("Minecraft Account:", target.getName(), true).addField("Key:", "||" + key + "||", true).toEmbed()).queue();
    }
}
