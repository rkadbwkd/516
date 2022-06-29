/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("anyx")
public final class AnyXProperties {
    private final ServerPath serverPath;

    @Getter
    @RequiredArgsConstructor
    public static final class ServerPath {
        private final Integer servertype;
        private final String devPath;
        private final String deployPath;
        private final String anyXFilePath;
        private final String editorFilePath;
        private final String tempExcelPath;
    }
}
