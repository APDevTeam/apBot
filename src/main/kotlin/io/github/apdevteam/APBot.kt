package io.github.apdevteam

import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.obj.ReactionEmoji
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuilder

internal val logger = LoggerFactory.getLogger("cse-bot")

fun main(args: Array<String>) {
    if(args.isEmpty()){
        logger.error("No token supplied")
        return
    }
    val client  = createClient(args[0],true)
    if(client == null){
        logger.error("Client initialization failed")
        return
    }
    val dispatcher = client.dispatcher
    dispatcher.registerListener(Reactor)
    logger.info("Loading successful")
}
fun createClient(token: String, login: Boolean): IDiscordClient? { // Returns a new instance of the Discord client
    val clientBuilder = ClientBuilder() // Creates the ClientBuilder instance
    clientBuilder.withToken(token) // Adds the login info to the builder
    try {
        return if (login) {
            clientBuilder.login() // Creates the client instance and logs the client in
        } else {
            clientBuilder.build() // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
        }
    } catch (e: DiscordException) { // This is thrown if there was a problem building the client
        logger.error("Critical error: ", e)
        return null
    }
}
object Reactor {
    @EventSubscriber
    @SuppressWarnings("Unused")
    fun onMessageReceived(event: MessageReceivedEvent){
        if(!event.channel.name.equals("suggestions"))
            return
        val builder = RequestBuilder(event.client)
        builder.shouldBufferRequests(true)
        builder.doAction({
            event.message.addReaction(ReactionEmoji.of("⬆"))
            return@doAction true
        }).andThen({
            event.message.addReaction(ReactionEmoji.of("⬇"))
            return@andThen true
        }).execute()

    }
}
