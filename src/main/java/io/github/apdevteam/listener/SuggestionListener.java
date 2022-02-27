package io.github.apdevteam.listener;

import io.github.apdevteam.config.Settings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SuggestionListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.isFromGuild())
            return;
        if(!event.getGuild().getId().equals(Settings.GUILD))
            return;
        handleCommand(event);

        if(!event.getChannel().getId().equals(Settings.CHANNEL))
            return;

        handleSuggestion(event);
    }

    private void handleCommand(@NotNull MessageReceivedEvent event) {
        Member m = event.getMember();
        if(m == null || !m.hasPermission(Permission.ADMINISTRATOR))
            return;
        if(!event.getMessage().getContentDisplay().startsWith("'checksuggestions"))
            return;

        // TODO: process suggestions
    }

    private void handleSuggestion(@NotNull MessageReceivedEvent event) {
        Message m = event.getMessage();
        m.addReaction("U+2B06").queue(null, (error) -> {
            System.err.println("Failed to add upvote to " + m.getContentDisplay());
        });
        m.addReaction("U+2B07").queue(null, (error) -> {
            System.err.println("Failed to add downvote to " + m.getContentDisplay());
        });

        // TODO: create thread
    }
}
