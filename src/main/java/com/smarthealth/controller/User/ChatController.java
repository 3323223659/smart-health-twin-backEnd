package com.smarthealth.controller.User;

import com.smarthealth.common.context.BaseContext;
import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Chat.ChatDTO;
import com.smarthealth.service.ChatService;
import lombok.RequiredArgsConstructor;

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
        try {
            System.out.println(chatDTO.getMessage());
            String response = chatService.chat(chatDTO.getMessage(),id);
            return Result.ok(response);
        } catch (Exception e) {
            return Result.error("错误:"+e.getMessage());
        }
    }


    //清楚会话记录
    @PostMapping("/clear")
    public Result clearHistory() {
        chatService.clearHistory();
        return Result.ok();
    }

}
