/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.mvc.context;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 *
 * Message 처리를 위해 사용되는 임시 저장소에 접근하기 위한 편의 클래스
 *
 * @author jeado
 *
 */
public class BaseRequestContextHolder {
    private static final String BASE_REQUEST_CONTEXT_NAME = "3MP IoT API Request Context";

    private static final ThreadLocal<BaseRequestContext> INHERIABLE_MCI_CONTEXT_HOLDER = new NamedInheritableThreadLocal<BaseRequestContext>(
            BASE_REQUEST_CONTEXT_NAME);

    public BaseRequestContextHolder() {}

    /**
     * <pre>
     * 현재 쓰레드에 {@link BaseRequestContext}를 바인딩.
     * 만약 현재 쓰레드에 이미 바인딩 되어 있는 객체가 있는 경우에는 덮어쓰게 되니 주의 요망.
     * </pre>
     *
     * @param context
     */
    public static void set(BaseRequestContext context) {
        INHERIABLE_MCI_CONTEXT_HOLDER.set(context);
    }

    /**
     * <pre>
     * 현재 쓰레드에 바인딩 되어 있는 {@link BaseRequestContext} 객체 반환.
     * 만약 없을 경우에는 내부적으로 {@link BaseRequestContext}을 생성하여 쓰레드에 바인딩 하고 객체를 반환 함
     * </pre>
     *
     * @return 현재 쓰레드에 바인딩 되어 있는 {@link BaseRequestContext} 객체
     */
    public static BaseRequestContext get() {
        BaseRequestContext BaseRequestContext = INHERIABLE_MCI_CONTEXT_HOLDER.get();

        if (BaseRequestContext == null) {
            BaseRequestContext = new BaseRequestContext();
            INHERIABLE_MCI_CONTEXT_HOLDER.set(BaseRequestContext);
        }

        return BaseRequestContext;
    }

    /**
     * ThreadLocal 리소스를 반환
     */
    public static void clear() {
        if (INHERIABLE_MCI_CONTEXT_HOLDER.get() != null) {
            INHERIABLE_MCI_CONTEXT_HOLDER.remove();
        }
    }
}

