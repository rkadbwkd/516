/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.crud.user.controllerDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.time.LocalDate;
import com.anylogic.demo.common.model.AnyXCommonDTO;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel
public class GetUserDTO extends AnyXCommonDTO implements Serializable {
    @ApiModelProperty(value = "사용자 seq")
    @NotEmpty
    private String userNo;                       // 사용자 seq

}


