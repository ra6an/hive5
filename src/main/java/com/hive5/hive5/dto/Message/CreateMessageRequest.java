package com.hive5.hive5.dto.Message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateMessageRequest {
    private String content;
    private UUID receiver;
}