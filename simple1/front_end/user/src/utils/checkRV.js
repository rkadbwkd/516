/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



const checkRV2Message = {
  checkRV: (value, isRequired, checkType, errorMsgRequired, errorMsgValidation, limitLen) => {
    if (isRequired) {
      if (value == undefined || value == "") {
        alert(errorMsgRequired);
        return false;
      }
    } else {
      if (value == undefined || value == "") return true;
    }

    if (limitLen != undefined && limitLen != "") {
      if (value.length > limitLen) {
        alert(errorMsgValidation);
        return false;
      }
    }

    if (!checkUtil[checkType](value, limitLen)) {
      alert(errorMsgValidation);
      return false;
    }
    return true;
  }
};

const checkRV = {
  checkRV: (value, isRequired, checkType, errorMsg, limitLen) => {
    if (isRequired) {
      if (value == undefined || value == "") {
        alert(errorMsg);
        return false;
      }
    } else {
      if (value == undefined || value == "") return true;
    }

    if (limitLen != undefined && limitLen != "") {
      if (value.length > limitLen) {
        alert(errorMsg);
        return false;
      }
    }

    if (!checkUtil[checkType](value, limitLen)) {
      alert(errorMsg);
      return false;
    }
    return true;
  }
};

const checkUtil = {
  checkObj: obj => {
    if (obj == undefined || obj == null) return false;
    return true;
  },

  // undefined인지 여부
  checkNullValue: obj => {
    if (obj === undefined || obj === null) return "";
    return obj;
  },

  // 값이 존재하는지 여부
  checkValObj: obj => {
    if (obj == undefined || obj == null || obj.toString().trim() == "")
      return false;
    return true;
  },

  // 버전 형식 여부 ( 숫자, . )
  checkVersion: str => {
    var number = "/^[0-9.]*$/";
    if (!number.test(str)) {
      alert("숫자와 '.'만 입력 가능합니다.");
      str = "";
    }
    return str;
  },
  checkByte: (value, length) => {
    var str = value;
    var l = 0,
      r = 0;
    if (typeof str == "undefined") return "";
    for (var i = 0; i < str.length; i++) {
      l += str.charCodeAt(i) > 128 ? 2 : 1;
      if (l <= length) r = i + 1;
    }
    if (l > length) {
      alert(" 길이를 초과하였습니다.");
      return str.substring(0, r);
    }
    return str;
  },
  checkCodeId: (value, single) => {
    if (single !== undefined && single) return;
    single = true;
    var rc = /^[a-zA-Z0-9]+$/.test(value);
    if (!rc) {
      alert("영문 및 숫자만 입력 가능합니다.");
    }
    single = false;
    return rc;
  },

  // 패스워드 체크 : 영문, 숫자, 특수문자 포함 (8~12자) 체크 .... 세안 사이트에서만 유효
  checkSeianPwd: (id, password) => {
    if (
      "!/^[a-zA-Z0-9{}[]/?.,;:|)*~`!^-_+<>@#$%&\\=('\"]{8,12}$/".test(password)
    ) {
      alert("숫자와 영문자, 특수문자 조합으로 6~15자리를 사용해야 합니다.");
      return false;
    }

    var checkNumber = password.search(/[0-9]/g);
    var checkEnglish = password.search(/[a-z]/gi);
    var checkChar = password.search("/[{}[]/?.,;:|)*~`!^-_+<>@#$%&\\=('\"]/gi");
    if (checkNumber < 0 || checkEnglish < 0 || checkChar < 0) {
      // 개별적으로 영문, 숫자, 특수 문자 반드시 포함
      alert("숫자와 영문자, 특수문자를 혼용하여야 합니다.");
      return false;
    }

    if (/(\w)\1\1\1/.test(password)) {
      // 같은 문자 3번 반복 금지
      alert("같은 문자를 3번 이상 사용하실 수 없습니다.");
      return false;
    }

    if (password.search(id) > -1) {
      // 패스워드에 id 포함 금지
      alert("비밀번호에 아이디가 포함되었습니다.");
      return false;
    }

    return true;
  },

  // 패스워드 체크 : 영문, 숫자, 특수문자 포함 (6~12자) 체크
  checkPW: (str = "") => {
    // 번호
    var regExp = "/^[a-zA-Z0-9{}[]/?.,;:|)*~`!^-_+<>@#$%&\\=('\"]{6,12}$/";
    if (!regExp.test(str)) {
      return false;
    }
    return true;
  },

  // 휴대폰 번호 체크 : (위는 '-' 있는것), 아래는 '-'없는 것
  checkHpNo: (str = "") => {
    var testNumber = String(str); // 문자로 변환
    testNumber = testNumber.replace(/-/gi, "");

    // var regExp =
    // /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
    var regExp = /^01([016789]{1})([0-9]{7,8})$/;
    if (!regExp.test(testNumber)) {
      return false;
    }
    return true;
  },

  // 번호 체크 : (위는 '-' 있는것), 아래는 '-'없는 것
  checkTelNo: (str = "") => {
    var testNumber = String(str); // 문자로 변환
    testNumber = testNumber.replace(/-/gi, "");

    var regExp = "/^0[2-9]{1,2}-[0-9]{3,4}-[0-9]{4}$/";
    if (!regExp.test(testNumber)) {
      return false;
    }
    return true;
  },

  // 정수 숫자 체크 ( 0-9 )
  checkNumberOnly: (str = 0) => {
    var number = /^[0-9]*$/;
    if (!number.test(str)) {
      return false;
    }
    return true;
  },

  // 소수 숫자 체크 ( +, -, ., 0-9 )
  checkReal: (str = 0) => {
    var number = /^[+-]?[0-9]*(\.?[0-9]*)$/;
    if (!number.test(str)) {
      return false;
    }
    return true;
  },

  // 양수 숫자
  checkPositiveNumber: (str = 0) => {
    if (str === undefined || str.isNaN == true || str == null) {
      return false;
    }
    var number = /^[0-9]*(.[0-9]*)$/;
    if (!number.test(str)) {
      return false;
    }
    if (str <= 0) {
      return false;
    }
    return true;
  },

  // 문자 길이
  checkStrLen: (str = "", len) => {
    if (str.length <= len) {
      return true;
    } else {
      return false;
    }
  },

  // 모든문자
  checkAll: () => {
    // console.log(str);
    return true;
  },

  // 주민 번호 체크
  checkJumin: (num = "") => {
    const isValidDate = (yyyy, MM, dd) => {
      var oDate = new Date();
      oDate.setFullYear(yyyy);
      oDate.setMonth(MM - 1);
      oDate.setDate(dd);

      if (
        oDate.getFullYear() != yyyy ||
        oDate.getMonth() + 1 != MM ||
        oDate.getDate() != dd
      ) {
        return false;
      }
      return true;
    };

    const isSSN = (jumin1, jumin2) => {
      var n = 2;
      var sum = 0;
      for (var i = 0; i < jumin1.length; i++) {
        sum += parseInt(jumin1.substr(i, 1)) * n++;
      }
      for (var j = 0; j < jumin2.length - 1; j++) {
        sum += parseInt(jumin2.substr(j, 1)) * n++;
        if (n == 10) {
          n = 2;
        }
      }

      var checkSum = 11 - (sum % 11);
      if (checkSum == 11) {
        checkSum = 1;
      }
      if (checkSum == 10) {
        checkSum = 0;
      }
      if (checkSum != parseInt(jumin2.substr(6, 1))) {
        return false;
      }
      return true;
    };

    var socialRegNumber = String(num); // 문자로 변환
    socialRegNumber = socialRegNumber.replace(/-/gi, "");

    if (!socialRegNumber || socialRegNumber.length != 13) {
      return false;
    }

    // 숫자가 아닌 것을 입력한 경우
    if (isNaN(socialRegNumber)) {
      return false;
    }

    var jumin1 = socialRegNumber.substr(0, 6),
      jumin2 = socialRegNumber.substr(6, 7),
      genda = jumin2.substr(0, 1), // 성별 1~4
      formalYear = genda < 3 ? "19" : "20", // 연도 계산 - 1 또는 2:
      // 1900년대, 3 또는 4:
      // 2000년대
      yyyy = formalYear + jumin1.substr(0, 2),
      MM = jumin1.substr(2, 2),
      dd = jumin1.substr(4, 2);
    // 성별부분이 1 ~ 4 가 아닌 경우
    if (genda < 1 || genda > 4) {
      return false;
    }
    // 날짜 유효성 검사
    if (isValidDate(yyyy, MM, dd) == false) {
      return false;
    }
    // Check Digit 검사
    if (!isSSN(jumin1, jumin2)) {
      return false;
    }
    return true;
  },

  // 외국인 번호 체크
  checkForign: (num = "") => {
    var sum = 0;
    var odd = 0;
    var buf = new Array(13);
    for (var i = 0; i < 13; i++) {
      buf[i] = parseInt(num.charAt(i));
    }
    odd = buf[7] * 10 + buf[8];
    if (odd % 2 != 0) {
      return false;
    }
    if (buf[11] != 6 && buf[11] != 7 && buf[11] != 8 && buf[11] != 9) {
      return false;
    }
    const multipliers = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];
    for (i = 0, sum = 0; i < 12; i++) {
      sum += buf[i] *= multipliers[i];
    }
    sum = 11 - (sum % 11);
    if (sum >= 10) {
      sum -= 10;
    }
    sum += 2;
    if (sum >= 10) {
      sum -= 10;
    }
    if (sum != buf[12]) {
      return false;
    }
    return true;
  },

  // 법인 번호 체크
  checkRegNo: (str = "") => {
    // 법인번호 오류검증 공식
    // 법인번호에서 마지막 자리를 제외한
    // 앞에 모든 자리수를 1과 2를 순차적으로 곱한다.
    // 예) 1234567890123
    //     ************
    //     121212121212
    //     각각 곱한 수를 모든 더하고 10으로 나눈 나머지 수를 구한다.
    //     (각각더한수 % 10)
    //     나눈 나머지 수와 법인번호 마지막 번호가 일치하면 검증일치
    var regNumber = String(str); // 문자로 변환
    regNumber = regNumber.replace(/-/gi, "");

    var arr_regno = regNumber.split("");
    var arr_wt = new Array(1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2);
    var iSum_regno = 0;
    var iCheck_digit = 0;

    for (var i = 0; i < 12; i++) {
      iSum_regno += eval(arr_regno[i]) * eval(arr_wt[i]);
    }

    iCheck_digit = 10 - (iSum_regno % 10);
    iCheck_digit = iCheck_digit % 10;
    if (iCheck_digit != arr_regno[12]) {
      return false;
    }
    return true;
  },

  // 사업자 번호 체크
  checkBizNo: (num = "") => {
    var testNumber = String(num); // 문자로 변환
    testNumber = testNumber.replace(/-/gi, "");

    var sumMod = 0;
    sumMod += parseInt(testNumber.substring(0, 1));
    sumMod += (parseInt(testNumber.substring(1, 2)) * 3) % 10;
    sumMod += (parseInt(testNumber.substring(2, 3)) * 7) % 10;
    sumMod += (parseInt(testNumber.substring(3, 4)) * 1) % 10;
    sumMod += (parseInt(testNumber.substring(4, 5)) * 3) % 10;
    sumMod += (parseInt(testNumber.substring(5, 6)) * 7) % 10;
    sumMod += (parseInt(testNumber.substring(6, 7)) * 1) % 10;
    sumMod += (parseInt(testNumber.substring(7, 8)) * 3) % 10;
    sumMod += Math.floor((parseInt(testNumber.substring(8, 9)) * 5) / 10);
    sumMod += (parseInt(testNumber.substring(8, 9)) * 5) % 10;
    sumMod += parseInt(testNumber.substring(9, 10));

    if (sumMod % 10 != 0) {
      return false;
    }
    return true;
  },

  // 첫글자는 영문자 그리고 숫자 포함(5~12)자
  checkId: (str = "") => {
    var idRegx = /^[a-zA-Z]+[a-zA-Z0-9]{5,12}$/;
    return idRegx.test(str);
  },

  // 이메일 주소 검증
  checkEmail: (str = "") => {
    var emailRegx = /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/;
    return emailRegx.test(str);
  },

  // IP 주소 검증
  checkIpAddr: (str = "") => {
    var number = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    if (!number.test(str)) {
      return false;
    }
    return true;
  },

  // 한국어
  checkKor: (str = "") => {
    /* var kor =/^[0-9a-zA-Z]*$/; */
    var kor = "/[a-z0-9]|[ []{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g";
    if (kor.test(str)) {
      return false;
    }
    return true;
    // return str;
  },

  // Null
  checkNull: str => {
    if (str == undefined || str == null || str == "") {
      return false;
    }
    return true;
  },

  /*
   * $scope.numberWithCommas = function(number) { return
   * number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","); }
   */

  numberWithCommas: val => {
    if (val < 1000) {
      return val;
    } else if (val >= 1000) {
      return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    } else {
      return val;
    }
  }
};

export { checkRV, checkRV2Message };


