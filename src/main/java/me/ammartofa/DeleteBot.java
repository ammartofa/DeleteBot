package me.ammartofa;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import javax.security.auth.login.LoginException;

public class DeleteBot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException{

        JDA bot = JDABuilder.createDefault("") // Insert bot token
                .setActivity(Activity.playing("with your server's messages"))
                .addEventListeners(new DeleteBot())
                .build();

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return; // Ignore reactions from other bots
        }

        // Check if the reaction is on a message and the reaction is the desired emoji ("👍" in this case)
        if (event.isFromType(ChannelType.TEXT) &&
                event.getReaction().getEmoji().equals("👎")) {
            int desiredReactionCount = 1; // Adjust as needed

            // Fetch the reacted message
            Message reactedMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();

            // Check if the message has reached the desired number of reactions
            if (reactedMessage.getReactions().stream()
                    .anyMatch(reaction -> reaction.getEmoji().equals("👎")) &&
                    reactedMessage.getReactions().stream()
                            .mapToInt(MessageReaction::getCount)
                            .sum() >= desiredReactionCount) {
                // Delete the message
                reactedMessage.delete().queue();

                // Send a "Deleted!" message
                event.getChannel().sendMessage("Deleted!").queue(message -> {
                    // Delete the "Deleted!" message after a delay (e.g., 5 seconds)
                    message.delete().queueAfter(5, java.util.concurrent.TimeUnit.SECONDS);
                });
            }
        }
    }

//    public void onMessageReactionAdd(MessageReactionAddEvent event) {
//        // Check if the reaction is on a message and the reaction is the desired emoji ("👎" in this case)
//        if (event.isFromType(ChannelType.TEXT) &&
//                event.getReaction().getEmoji().equals("👎")) {
//            int desiredReactionCount = 2; // Adjust as needed
//
//            // Fetch the reacted message
//            Message reactedMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
//
//            // Check if the message has reached the desired number of reactions
//            if (reactedMessage.getReactions().stream()
//                    .filter(reaction -> reaction.getEmoji().equals("👎"))
//                    .mapToInt(MessageReaction::getCount)
//                    .sum() >= desiredReactionCount) {
//                // Delete the message
//                reactedMessage.delete().queue();
//
//                // Send a "Deleted!" message
//                event.getChannel().sendMessage("Deleted!").queue(message -> {
//                    // Delete the "Deleted!" message after a delay (e.g., 5 seconds)
//                    message.delete().queueAfter(5, java.util.concurrent.TimeUnit.SECONDS);
//                });
//            }
//        }
//    }
}
