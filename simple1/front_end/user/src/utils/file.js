/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



import XLSX from "xlsx";
import { saveAs } from "file-saver";

const makeUrlForBlob = data => {
  var blob = new Blob([data], { type: "application/binary" });

  var fileURL = URL.createObjectURL(blob);

  return fileURL;
};

const SaveToDisk = (fileURL, fileName) => {
  // for non-IE
  if (!(window.ActiveXObject || "ActiveXObject" in window)) {
    var save = document.createElement("a");
    save.href = fileURL;
    save.target = "_blank";
    save.download = fileName || fileURL;
    var evt = document.createEvent("MouseEvents");
    evt.initMouseEvent(
      "click",
      true,
      true,
      window,
      1,
      0,
      0,
      0,
      0,
      false,
      false,
      false,
      false,
      0,
      null
    );
    save.dispatchEvent(evt);
    (window.URL || window.webkitURL).revokeObjectURL(save.href);
  }
};

const createFile = (file, target) => {
  var reader = new FileReader();
  reader.onload = e => {
    target.file = e.target.result;
  };
  reader.readAsDataURL(file);
  target.name = file.name;
  return target;
};

const excelDownload = (data, FileName, sheetName) => {
  var wb = XLSX.utils.book_new();

  var newWorksheet = excelHandler.getWorksheet(data);
  XLSX.utils.book_append_sheet(wb, newWorksheet, sheetName);

  var wbout = XLSX.write(wb, { bookType: "xlsx", type: "binary" });
  saveAs(
    new Blob([s2ab(wbout)], { type: "application/octet-stream" }),
    `${FileName}.xlsx`
  );
};

var excelHandler = {
  getWorksheet: data => {
    return XLSX.utils.aoa_to_sheet(data);
  }
};

const s2ab = s => {
  var buf = new ArrayBuffer(s.length); //convert s to arrayBuffer
  var view = new Uint8Array(buf); //create uint8array as viewer
  for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xff; //convert to octet
  return buf;
};

const selectElemByKey = (inputList, keys) => {
  var pureUserList = [];
  for (var i in inputList) {
    var pureUser = {};
    for (var j in keys) {
      var key = keys[j];
      if (inputList[i][key] == undefined) inputList[i][key] = "";
      pureUser[key] = inputList[i][key];
    }
    pureUserList.push(pureUser);
  }

  return pureUserList;
};

const convertJsonToCsv = json_array => {
  var csv_data = [];
  var header = [];

  for (var key in json_array[0]) {
    header.push(key);
  }
  csv_data.push(header);

  for (var i in json_array) {
    var data = [];
    for (var key2 in json_array[i]) {
      data.push(json_array[i][key2]);
    }
    csv_data.push(data);
  }

  return csv_data;
};

const getFiles = (files, comments) => {
  var rtnFiles = [];

  if (files == undefined) return;

  if (comments === undefined) {
    files.forEach(file => {
      if (file.comments === undefined || file.commnets === "") {
        rtnFiles.push(file);
      }
    });
  } else {
    files.forEach(file => {
      if (file.comments !== undefined && file.comments !== "") {
        rtnFiles.push(file);
      }
    });
  }

  return rtnFiles;
};

export {
  makeUrlForBlob,
  SaveToDisk,
  createFile,
  excelDownload,
  selectElemByKey,
  convertJsonToCsv,
  getFiles
};


