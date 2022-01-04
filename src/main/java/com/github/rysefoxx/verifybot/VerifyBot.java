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

package com.github.rysefoxx.verifybot;

import com.github.rysefoxx.verifybot.command.VerifyCommand;
import com.github.rysefoxx.verifybot.config.Config;
import com.github.rysefoxx.verifybot.listener.MessageListener;
import com.github.rysefoxx.verifybot.manager.VerifyDataManger;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

@Getter
public final class VerifyBot extends JavaPlugin {

    private VerifyDataManger verifyDataManger;
    private JDA jda;

    @Override
    public void onEnable() {
        startBot();
        init();
    }

    @Override
    public void onDisable() {
        this.jda.shutdownNow();
        this.verifyDataManger.saveDocument();
    }

    private void startBot(){
        JDABuilder builder = JDABuilder.createDefault(Config.get("TOKEN"));
        builder.setActivity(Activity.playing("auf localhost :("));
        builder.setStatus(OnlineStatus.ONLINE);

        try {
            this.jda = builder.build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }

        this.jda.addEventListener(new MessageListener(this));
    }

    private void init(){
        registerCommands();
        registerManagers();
    }

    private void registerCommands(){
        getCommand("verify").setExecutor(new VerifyCommand(this));
    }

    private void registerManagers(){
        this.verifyDataManger = new VerifyDataManger();
        enableManagers();
    }

    private void enableManagers(){
        this.verifyDataManger.loadDocument();

    }
}
