package com.smarthealth.controller.User;

import com.smarthealth.common.result.Result;
import com.smarthealth.domain.Chat.ChatDTO;
import com.smarthealth.service.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyH524
 * @since 2025/4/6 下午12:45
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    //聊天
    @PostMapping
    public Result chat(@RequestBody ChatDTO chatDTO) {
        try {
            System.out.println(chatDTO.getMessage());
            String response = chatService.chat(chatDTO.getMessage());
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
