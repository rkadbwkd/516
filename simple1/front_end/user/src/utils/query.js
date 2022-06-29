/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



const makeQueryString = jsonObject => {
  var queryString = "?";
  for (var key in jsonObject) {
    if (jsonObject[key] != undefined)
      queryString += `${key}=${jsonObject[key]}&`;
  }
  queryString = queryString.slice(0, -1);
  return queryString;
};

export { makeQueryString };


