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

package com.github.rysefoxx.verifybot.command;

import com.github.rysefoxx.potpvp.PotSystemAPI;
import com.github.rysefoxx.potpvp.data.impl.UserStats;
import com.github.rysefoxx.verifybot.VerifyBot;
import com.github.rysefoxx.verifybot.util.EmbedCreator;
import com.github.rysefoxx.verifybot.util.StringDefaults;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Rysefoxx | Rysefoxx#6772
 * @since 1/3/2022
 */
public record VerifyCommand(@NotNull VerifyBot plugin) implements CommandExecutor {

    @Contract(pure = true)
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage(" §8● §7Verwende§8: §e/Verify <Key>");
            player.sendMessage(" §8● §7Verwende§8: §e/Verify reset");
            return true;
        }
        UserStats userStats = PotSystemAPI.getInstance().getUserDataManager().getUserData(player.getUniqueId()).getUserStats();

        if (args[0].equalsIgnoreCase("reset")) {
            if (!userStats.isVerified()) {
                player.sendMessage(StringDefaults.ERROR + "§cDu bist mit keinem Discord Account verbunden!");
                return true;
            }
            String id = this.plugin.getVerifyDataManger().getDiscordId(player.getUniqueId());
            if (id == null) {
                player.sendMessage(StringDefaults.ERROR + "§cFür deinen Minecraft Account konnte keinen Key gefunden werden.");
                return true;
            }
            User user = this.plugin.getJda().retrieveUserById(id).complete();
            Guild guild = this.plugin.getJda().getGuildById("747888677077647470");
            if (guild != null) {
                Role verifyRole = guild.getRolesByName("verified", true).get(0);
                guild.removeRoleFromMember(id, verifyRole).queue();
            }
            userStats.setVerified(false);
            this.plugin.getVerifyDataManger().getConfig().set(player.getUniqueId().toString(), null);
            this.plugin.getVerifyDataManger().saveFile();
            player.sendMessage(StringDefaults.VERIFY + "§7Verbindung mit dem Discord Account §6" + user.getAsTag() + " §7wurde aufgehoben!");
            user.openPrivateChannel().complete().sendMessageEmbeds((new EmbedCreator()).setColor(Color.RED).setTitle(":warning: | Verknüpfung aufgehoben!").setDescription("Dein Minecraft Account **" + player.getName() + "** ist nicht mehr mit Discord verbunden.").addField("Discord User:", user.getAsTag(), true).addField("Minecraft Account:", player.getName(), true).toEmbed()).queue();
            return true;
        }

        if (userStats.isVerified()) {
            String id = this.plugin.getVerifyDataManger().getDiscordId(player.getUniqueId());
            if (id == null) {
                player.sendMessage(StringDefaults.ERROR + "§cFür deinen Minecraft Account konnte keinen Key gefunden werden.");
                return true;
            }
            User user = this.plugin.getJda().retrieveUserById(id).complete();
            player.sendMessage(StringDefaults.ERROR + "§cDein Minecraft Account ist bereits mit §6" + user.getAsTag() + " §cverbunden!");
            return true;
        }

        if (!this.plugin.getVerifyDataManger().hasKey(player.getUniqueId())) {
            player.sendMessage(StringDefaults.ERROR + "§cFür deinen Minecraft Account konnte kein Key gefunden werden.");
            return true;
        }

        String key = args[0];
        if (!this.plugin.getVerifyDataManger().key(player.getUniqueId()).equalsIgnoreCase(key)) {
            player.sendMessage(StringDefaults.ERROR + "§cUngültiger Key! §7Versuche erneut.");
            return true;
        }
        Member member = this.plugin.getVerifyDataManger().getDiscordData(player.getUniqueId());
        Guild guild = this.plugin.getJda().getGuildById("747888677077647470");
        if (guild != null) {
            Role verifyRole = guild.getRolesByName("verified", true).get(0);
            guild.addRoleToMember(member, verifyRole).queue();
        }

        this.plugin.getVerifyDataManger().removeVerifyMembers(member);
        this.plugin.getVerifyDataManger().removeDiscordData(player.getUniqueId());
        this.plugin.getVerifyDataManger().removeKey(player.getUniqueId());
        this.plugin.getVerifyDataManger().addVerifiedPlayers(player.getUniqueId(), member.getId());
        userStats.setVerified(true);

        player.sendMessage(StringDefaults.VERIFY + "§aVerifizierung erfolgreich! §7Dein Minecraft Account ist nun mit §6" + member.getUser().getAsTag() + " §7verbunden.");
        member.getUser().openPrivateChannel().complete().sendMessageEmbeds(new EmbedCreator().setColor(Color.GREEN).setTitle(":white_check_mark: | Minecraft mit Discord verbunden!").setDescription("Dein Minecraft Account **" + player.getName() + "** ist nun mit deinem Discord Account verbunden.").addField("Discord User:", member.getUser().getAsTag(), true).addField("Minecraft Account:", player.getName(), true).toEmbed()).queue();
        return true;
    }
}
