/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



function makeTableHeader(header, _name, _required, _label, _align, _field, _sortable) {
  header.push(
    {
      name: _name,
      required: _required,
      label: _label,
      align: _align,
      field: row => row[_field],
      sortable: _sortable
    }
  );
  return header;
}

export { makeTableHeader };

