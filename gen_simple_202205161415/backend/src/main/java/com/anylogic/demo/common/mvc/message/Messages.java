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

/**
 * 컨트롤러 클래스의 메서드에서 사용되는 메시지 인터페이스.
 * 해당 인터페이스를 통하여 UI에 넘길 메시지를 추가한다.
 * 여러건의 메시지 추가가 가능하다.
 *
 * @author jeado
 *
 */
public interface Messages {

    void addMessage(String code, String msg);
    void setMessage(String code, String msg);

}

