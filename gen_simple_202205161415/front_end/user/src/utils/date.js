/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



function addDate(value, index) {
  const d = new Date();
  let returnValue;
  if (index === 'day') {
    returnValue = new Date(d.setMonth(d.getDay()+value))
  } else if (index === 'month') {
    returnValue = new Date(d.setMonth(d.getMonth()+value))
  } else if (index === 'year') {
    returnValue = new Date(d.setMonth(d.getYear()+value))
  }
  return returnValue
}

function formatDate(date) {
  var d = new Date(date);
  var month = "" + (d.getMonth() + 1).toString().padStart(2,'0');
  var day = "" + d.getDate().toString().padStart(2,'0');
  var year = d.getFullYear();

  // if (month.length < 2) month = "0" + month;
  // if (day.length < 2) day = "0" + day;

  return [year, month, day].join("-");
}

export { formatDate, addDate };


