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

package com.github.rysefoxx.verifybot.util;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.temporal.TemporalAccessor;

public class EmbedCreator {

    private final EmbedBuilder embedBuilder;

    public EmbedCreator() {
        this.embedBuilder = new EmbedBuilder();
    }

    public EmbedCreator(String title) {
        this.embedBuilder = new EmbedBuilder();
        this.embedBuilder.setTitle(title);
    }

    public EmbedCreator setTitle(String title) {
        this.embedBuilder.setTitle(title);
        return this;
    }

    public EmbedCreator setTitle(String title, String url) {
        this.embedBuilder.setTitle(title, url);
        return this;
    }

    public EmbedCreator setColor(Color color) {
        this.embedBuilder.setColor(color);
        return this;
    }

    public EmbedCreator addField(String name, String value, boolean inline) {
        this.embedBuilder.addField(name, value, inline);
        return this;
    }

    public EmbedCreator addField(MessageEmbed.Field field) {
        this.embedBuilder.addField(field);
        return this;
    }

    public EmbedCreator addBlankField(boolean inline) {
        this.embedBuilder.addBlankField(inline);
        return this;
    }

    public EmbedCreator addFields(MessageEmbed.Field... fields) {
        for (MessageEmbed.Field field : fields) {
            this.embedBuilder.addField(field);
        }
        return this;
    }

    public EmbedCreator setImage(String url) {
        this.embedBuilder.setImage(url);
        return this;
    }

    public EmbedCreator setFooter(String footer) {
        this.embedBuilder.setFooter(footer);
        return this;
    }

    public EmbedCreator setFooter(String footer, String iconUrl) {
        this.embedBuilder.setFooter(footer, iconUrl);
        return this;
    }

    public EmbedCreator setAuthor(String author) {
        this.embedBuilder.setAuthor(author);
        return this;
    }

    public EmbedCreator setDescription(String description) {
        this.embedBuilder.setDescription(description);
        return this;
    }

    public EmbedCreator setDescription(CharSequence description) {
        this.embedBuilder.setDescription(description);
        return this;
    }

    public EmbedCreator setThumbnail(String url) {
        this.embedBuilder.setThumbnail(url);
        return this;
    }

    public EmbedCreator setTimeStamp(TemporalAccessor accessor) {
        this.embedBuilder.setTimestamp(accessor);
        return this;
    }

    public MessageEmbed toEmbed() {
        this.embedBuilder.setFooter("Bot erstellt von Rysefoxx");
        return this.embedBuilder.build();
    }

}
