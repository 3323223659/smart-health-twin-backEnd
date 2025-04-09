package com.smarthealth.controller.User;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.smarthealth.common.context.BaseContext;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Chat.ChatDTO;
import com.smarthealth.service.ChatService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:45
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

//    @PostMapping
//    public Result chat(@RequestBody ChatDTO chatDTO, @RequestParam String userId) {
//        try {
//            String response = chatService.chat(userId, chatDTO.getMessage());
//            return Result.ok(response);
//        } catch (Exception e) {
//            return Result.error("错误:" + e.getMessage());
//        }
//    }


    //聊天
    @PostMapping
    public Result chat(@RequestBody ChatDTO chatDTO) {
        String id = BaseContext.getCurrentId().toString();
        log.info("userid:"+id);
        try {
            String response = chatService.chat(chatDTO.getMessage(),id);
            return Result.ok(response);
        } catch (Exception e) {
            return Result.error("错误:"+e.getMessage());
        }
    }


    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> chatStream(@RequestBody ChatDTO chatDTO) {
        String userId = BaseContext.getCurrentId().toString();
        System.out.println(chatDTO);
        try {
            System.out.println("User ID: " + userId);
            InputStream stream = chatService.chatStream(userId, chatDTO.getMessage());
            System.out.println("Stream: " + stream);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            System.err.println("Error in chatStream (Controller): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //保存流式输出的结果
    @PostMapping("/saveResponse")
    public Result saveStreamResponse(@RequestBody String aiResponse) {
        String userId = BaseContext.getCurrentId().toString();
        chatService.saveStreamResponse(userId, aiResponse);
        return Result.ok();
    }


    //清楚会话记录
    @PostMapping("/clear")
    public Result clearHistory() {
        String userid = BaseContext.getCurrentId().toString();
        log.info("userid:"+userid);
        chatService.clearHistory(userid);
        return Result.ok();
    }

}
