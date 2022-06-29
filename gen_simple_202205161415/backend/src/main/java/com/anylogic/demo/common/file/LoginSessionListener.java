/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.file;

import com.anylogic.demo.common.mvc.message.Messages;
import com.anylogic.demo.common.mvc.message.MessagesJsonImpl;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;


@Component
public class LoginSessionListener implements HttpSessionListener {


//    @Autowired
//    private LoginService loginService;

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        session.setMaxInactiveInterval(3600);

        try{
            System.out.println("[MySessionListener] Session created: " + session);
            session.setAttribute("foo", "bar");
        }catch (Exception e){
            System.out.println("[MySessionListener] Error setting session attribute: " + e.getMessage());
        }

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        String sessionId = session.getId();
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("session_id",sessionId);
        Messages messages = new MessagesJsonImpl();
        session.removeAttribute("userInfo");
        session.invalidate();
//        loginService.sessionTimeout(parameter);
    }
}

