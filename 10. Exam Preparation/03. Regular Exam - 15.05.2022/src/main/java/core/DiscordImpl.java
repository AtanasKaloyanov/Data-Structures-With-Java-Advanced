package core;

import models.Message;

import java.util.*;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {
    private Map<String, Message> messagesById = new LinkedHashMap<>();

    @Override
    public void sendMessage(Message message) {
        messagesById.put(message.getId(), message);
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
        if (!this.messagesById.containsKey(messageId)) {
            throw new IllegalArgumentException();
        }
        this.messagesById.get(messageId).getReactions().add(reaction);
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
        return new ArrayList<>();
    }

    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {
        return this.messagesById.values()
                .stream()
                .filter((message) -> message.getTimestamp() >= lowerBound && message.getTimestamp() <= upperBound)
                .sorted((first, second) -> {
                    int firstResult = first.getReactions().size();
                    int secondResult = second.getReactions().size();
                    return Integer.compare(secondResult, firstResult);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return this.messagesById.values()
                .stream()
                .sorted((first, second) -> {
                    int firstResult = first.getReactions().size();
                    int secondResult = second.getReactions().size();
                    return Integer.compare(secondResult, firstResult);
                }).limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        return this.messagesById.values()
                .stream()
                .sorted((first, second) -> {
                    int firstResult = first.getReactions().size();
                    int secondResult = second.getReactions().size();
                    int result = Integer.compare(secondResult, firstResult);

                    if (result == 0) {
                        result = Integer.compare(first.getTimestamp(),
                                second.getTimestamp());
                    }

                    if (result == 0) {
                      result = Integer.compare(first.getContent().length(),
                              second.getContent().length());
                    }
                    return result;
                })

                .collect(Collectors.toList());
    }
}
