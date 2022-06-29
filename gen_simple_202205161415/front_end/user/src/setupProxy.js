/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



const createProxyMiddleware = require('http-proxy-middleware')
module.exports = app => {
  app.use(
    '/curd',
    createProxyMiddleware(
      // ['/crud', '/socket.io'],
      {
        target: 'http://localhost:8580',
        changeOrigin: true,
      })
  );
  app.use(
    '/api',
    createProxyMiddleware(
      // ['/crud', '/socket.io'],
      {
        target: 'http://localhost:8580',
        changeOrigin: true,
        ws: true,
        router: {
          '/socket.io': 'ws://nginx:80'
        }
      }
    )
  )
}

