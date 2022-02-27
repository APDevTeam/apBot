package io.github.apdevteam.listener;

import io.github.apdevteam.config.Settings;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(!event.isFromGuild())
            return;
        if(!event.getGuild().getId().equals(Settings.GUILD))
            return;
        if(!event.getChannel().getId().equals(Settings.CHANNEL))
            return;

        handlePromotion(event);
        handleDeletion(event);
    }

    private void handlePromotion(@NotNull MessageReactionAddEvent event) {
        if(!event.getReaction().getReactionEmote().getAsCodepoints().equals("U+2705"))
            return;

        // TODO: things
    }

    private void handleDeletion(@NotNull MessageReactionAddEvent event) {
        if(!event.getReaction().getReactionEmote().getAsCodepoints().equals("U+274c"))
            return;

        // TODO: things
    }
}
