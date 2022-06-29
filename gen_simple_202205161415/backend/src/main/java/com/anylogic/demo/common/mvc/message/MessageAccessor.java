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

import java.util.List;

/**
 * Message에 접근하기 위한 접근자 인터페이스
 *
 * @author jeado
 *
 */
public interface MessageAccessor {

    List<Message> getMessageList();
}

