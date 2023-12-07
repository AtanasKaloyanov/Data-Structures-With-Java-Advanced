package core;

import models.Message;

import java.util.*;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {
    private Map<String, Message> messagesById = new LinkedHashMap<>();

    @Override
    public void sendMessage(Message message) {
        this.messagesById.put(message.getId(), message);
    }

    @Override
    public boolean contains(Message message) {
        return this.messagesById.containsKey(message.getId());
    }

    @Override
    public int size() {
        return this.messagesById.size();
    }

    @Override
    public Message getMessage(String messageId) {
        Message message = this.messagesById.get(messageId);
        if (message == null) {
            throw new IllegalArgumentException();
        }
        return message;
    }

    @Override
    public void deleteMessage(String messageId) {
        Message message = this.messagesById.remove(messageId);
        if (message == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void reactToMessage(String messageId, String reaction) {
        Message message = this.messagesById.get(messageId);
        if (message == null) {
            throw new IllegalArgumentException();
        }
        message.getReactions().add(reaction);
    }

    @Override
    public Iterable<Message> getChannelMessages(String channel) {
        List<Message> list = this.messagesById.values()
                .stream()
                .filter((message) -> message.getChannel().equals(channel))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return list;
    }

    @Override
    public Iterable<Message> getMessagesByReactions(List<String> reactions) {
       return this.messagesById.values()
                .stream()
                .filter( (message) -> {
                    List<String> reactS = message.getReactions();
                    return new HashSet<>(reactS).containsAll(reactions);
                })
               .sorted(Comparator.comparing( (Message m) -> m.getReactions().size(), Comparator.reverseOrder())
                       .thenComparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {
        return this.messagesById.values()
                .stream()
                .filter((message) -> message.getTimestamp() >= lowerBound && message.getTimestamp() <= upperBound)
                .sorted(Comparator.comparing( (Message m) -> m.getChannel().length()).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return this.messagesById.values()
                .stream()
                .sorted(Comparator.comparing((Message m) -> m.getReactions().size()).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        return this.messagesById.values()
                .stream()
                .sorted(Comparator.comparing( (Message m) -> m.getReactions().size(), Comparator.reverseOrder())
                        .thenComparing(Message::getTimestamp)
                        .thenComparing( (message) -> message.getContent().length()))
                .collect(Collectors.toList());
    }
}
