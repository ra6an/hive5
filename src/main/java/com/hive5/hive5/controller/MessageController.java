package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.dto.Message.CreateMessageRequest;
import com.hive5.hive5.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllMessages(Principal principal) {
        Map<String, Object> data = messageService.getAllMessages(principal);

        ApiResponse response = new ApiResponse();
        response.setStatusCode(200);
        response.setMessage("");
        response.setStatus("OK");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody CreateMessageRequest createMessageRequest, Principal principal) {
        Map<String, Object> data = messageService.sendMessage(createMessageRequest, principal);

        ApiResponse response = new ApiResponse();
        response.setStatusCode(200);
        response.setMessage("Message sent!");
        response.setStatus("OK");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{messageId}/seen")
    public ResponseEntity<ApiResponse> seenMessage(@PathVariable long messageId, Principal principal) {
        Map<String, Object> data = messageService.seenMessage(messageId, principal);

        ApiResponse response = new ApiResponse();
        response.setStatusCode(200);
        response.setMessage("");
        response.setStatus("OK");
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}
