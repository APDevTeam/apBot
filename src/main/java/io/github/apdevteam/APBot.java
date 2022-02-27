package io.github.apdevteam;

import io.github.apdevteam.config.Settings;
import io.github.apdevteam.listener.ReactionListener;
import io.github.apdevteam.listener.SuggestionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;

public class APBot {
    private static APBot instance = null;

    @NotNull
    public static APBot getInstance() {
        return instance;
    }

    public static void main(String @NotNull [] args) {
        for(String arg : args) {
            if(arg.startsWith("-t=")) {
                Settings.TOKEN = arg.split("=")[1];
            }
            else if(arg.startsWith("-g=")) {
                Settings.GUILD = arg.split("=")[1];
            }
            else if(arg.startsWith("-c=")) {
                Settings.CHANNEL = arg.split("=")[1];
            }
            else if(arg.startsWith("-d")) {
                Settings.DEBUG = true;
            }
        }

        if("".equals(Settings.TOKEN) || "".equals(Settings.GUILD) | "".equals(Settings.CHANNEL)) {
            System.err.println("Failed to load arguments, please read the code for 'help'.");
            return;
        }

        new APBot();
    }

    @Nullable
    private JDA jda = null;
    @Nullable
    private Guild guild = null;

    public APBot() {
        JDABuilder builder = JDABuilder.createDefault(Settings.TOKEN);
        builder.disableCache(CacheFlag.ACTIVITY);
        builder.disableCache(CacheFlag.CLIENT_STATUS);
        builder.disableCache(CacheFlag.EMOTE);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.disableCache(CacheFlag.ONLINE_STATUS);
        builder.disableCache(CacheFlag.ROLE_TAGS);
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setChunkingFilter(ChunkingFilter.NONE);
        builder.setMemberCachePolicy(MemberCachePolicy.NONE);
        builder.enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        );

        // Add shutdown hook for CTRL+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> APBot.getInstance().shutdown()));

        try {
            jda = builder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            System.err.println("Failed to login to Discord!");
            e.printStackTrace();
            return;
        }

        for(Guild g : jda.getGuilds()) {
            if(g.getId().equals(Settings.GUILD)) {
                guild = g;
                System.out.println("Found guild: " + g.getName());
                break;
            }
        }
        if(guild == null) {
            System.err.println("Failed to find Guild!");
            return;
        }

        jda.addEventListener(new ReactionListener());
        jda.addEventListener(new SuggestionListener());

        System.out.println("Sucessfully booted!");
        instance = this;
    }

    public void shutdown() {
        if(jda == null)
            return;

        System.out.println("Shutting down...");
        jda.shutdown();
        jda = null;
        System.out.println("Shut down.");
    }
}
