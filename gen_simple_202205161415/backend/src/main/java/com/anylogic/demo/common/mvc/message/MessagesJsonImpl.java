/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.mvc.message;

import java.util.ArrayList;
import java.util.List;

public class MessagesJsonImpl implements Messages, MessageAccessor{

    private List<Message> messages = new ArrayList<Message>();

    @Override
    public void addMessage(String code, String msg) {

        messages.add(new Message(code, msg));
    }

    @Override
    public void setMessage(String code, String msg) {
        if(messages.size() > 0) {
            messages.set(0,  new Message(code, msg));
        }else {
            messages.add(new Message(code, msg));
        }
    }
    
    @Override
    public List<Message> getMessageList() {
        return messages;
    }

}

