package com.ai;

import com.ai.helper.Helper;
import com.ai.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAi01ApplicationTests {

    @Autowired
    private ChatService chatService;
    @Test
    void saveDataToVectorDatabase(){
        System.out.println("saving data to database");
        this.chatService.saveData(Helper.getData());
        System.out.println("data is saved successfully");
    }

}
