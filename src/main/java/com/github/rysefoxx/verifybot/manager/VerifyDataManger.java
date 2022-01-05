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

package com.github.rysefoxx.verifybot.manager;

import com.github.rysefoxx.potpvp.document.Document;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Rysefoxx | Rysefoxx#6772
 * @since 1/3/2022
 */
public class VerifyDataManger extends Document {

    private final Map<UUID, String> verifyKey;
    private final Map<UUID, Member> discordData;
    private final List<Member> verifyMembers;

    private final Map<UUID, String> verifiedPlayers;

    public VerifyDataManger() {
        super("verify", "discord-verify");
        this.discordData = new HashMap<>();
        this.verifyKey = new HashMap<>();
        this.verifiedPlayers = new HashMap<>();
        this.verifyMembers = new ArrayList<>();
    }

    public @Nullable String getDiscordId(@NotNull UUID uuid) {
        if (!this.verifiedPlayers.containsKey(uuid)) return null;
        return this.verifiedPlayers.get(uuid);
    }

    public void addVerifiedPlayers(@NotNull UUID uuid, @NotNull String name) {
        this.verifiedPlayers.put(uuid, name);
    }

    public Member getDiscordData(@NotNull UUID uuid) {
        return this.discordData.get(uuid);
    }

    public void addDiscordData(@NotNull UUID uuid, @NotNull Member member) {
        this.discordData.put(uuid, member);
    }

    public void removeKey(@NotNull UUID uuid) {
        this.verifyKey.remove(uuid);
    }

    public void removeDiscordData(@NotNull UUID uuid) {
        this.discordData.remove(uuid);
    }

    public void removeVerifyMembers(@NotNull Member member) {
        this.verifyMembers.remove(member);
    }

    public void addVerifyMember(@NotNull Member member) {
        this.verifyMembers.add(member);
    }

    public boolean inVerifyProcedure(@NotNull Member member) {
        return this.verifyMembers.contains(member);
    }

    public boolean inVerifyProcedure(@NotNull UUID uuid) {
        return this.verifyKey.containsKey(uuid);
    }

    public void addKey(@NotNull UUID uuid, @NotNull String key) {
        this.verifyKey.put(uuid, key);
    }

    public boolean hasKey(@NotNull UUID uuid) {
        return this.verifyKey.containsKey(uuid);
    }

    public String key(@NotNull UUID uuid) {
        return this.verifyKey.get(uuid);
    }

    @Override
    public void loadDocument() {
        if (getConfig().getKeys(false).isEmpty())
            return;
        for (String key : getConfig().getKeys(false)) {
            this.verifiedPlayers.put(UUID.fromString(key), getConfig().getString(key));
        }
    }

    @Override
    public void saveDocument() {
        for (Map.Entry<UUID, String> entry : this.verifiedPlayers.entrySet()) {
            getConfig().set(entry.getKey().toString(), entry.getValue());
        }
        saveFile();
    }
}
