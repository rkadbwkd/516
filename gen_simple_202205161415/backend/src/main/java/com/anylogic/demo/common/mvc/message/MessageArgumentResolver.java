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

import com.anylogic.demo.common.mvc.context.BaseRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러에 매개변수로 등록된 메시지처리 접근자({@link Messages}) 를 생성하여 컨트롤러에 전달한다.
 * @author jeado
 *
 */
public class MessageArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Messages.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        LOGGER.debug("Try to inject MessageImpl to Controller {}",parameter.getClass().getName());

        MessagesJsonImpl messagesJsonImpl = new MessagesJsonImpl();

        BaseRequestContextHolder.get().setMessageAccessor(messagesJsonImpl);

        return messagesJsonImpl;
    }

}

