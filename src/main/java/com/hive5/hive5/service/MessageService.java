package com.hive5.hive5.service;

import com.hive5.hive5.dto.Message.ChatDTO;
import com.hive5.hive5.dto.Message.CreateMessageRequest;
import com.hive5.hive5.dto.Message.MessageDTO;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.Message;
import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.MessageStatus;
import com.hive5.hive5.repository.MessageRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getAllMessages(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        List<User> participants = userRepository.findAllById(messageRepository.findChatParticipants(user));

        List<ChatDTO> chatsDTO = new ArrayList<>();

        for (User participant : participants) {
            UserDTO participantDTO = new UserDTO(participant);
            List<Message> messages = messageRepository.findMessagesBetweenUsers(user, participant);
            List<MessageDTO> messagesDTO = messages.stream().map(MessageDTO::new).toList();

            chatsDTO.add(new ChatDTO(participantDTO, messagesDTO));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("message", chatsDTO);

        return data;
    }

    public Map<String, Object> sendMessage(@RequestBody CreateMessageRequest createMessageRequest, Principal principal) {
        String username = principal.getName();

        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        if(sender.getId().equals(createMessageRequest.getReceiver()))
            throw new CustomException("Bad Request!", "You cant send message to yourself.", 404);

        User receiver = userRepository.findById(createMessageRequest.getReceiver())
                .orElseThrow(() -> new CustomException("Bad Request!", "There is no user with that ID.", 404));

        Message message = new Message();

        message.setContent(createMessageRequest.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(MessageStatus.SENT);

        messageRepository.save(message);

        MessageDTO messageDTO = new MessageDTO(message);

        Map<String, Object> data = new HashMap<>();
        data.put("message", messageDTO);

        return data;
    }

    public Map<String, Object> seenMessage(@PathVariable long messageId, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException("Bad Request", "No message with that ID.", 404));

        if(!message.getReceiver().getId().equals(user.getId())) {
            throw new CustomException("Unauthorized", "You are not allowed to update this message.", 401);
        }

        message.setStatus(MessageStatus.SEEN);

        messageRepository.save(message);

        MessageDTO messageDTO = new MessageDTO(message);

        Map<String, Object> data = new HashMap<>();
        data.put("data", messageDTO);

        return data;
    }

}
