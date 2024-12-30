<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

  <t:layout>
    <div class="container" id="app">
       <date-picker  :time="time"></date-picker>
      <button class="btn btn-primary" @click="query">下載報表</button>
    </div>
    <script>
         var app = new Vue({
           el: '#app',
           data: {
              time:{
                          startTime: '2022-12-03',
                          endTime: '',
               }
           }, methods: {
             query: function () {
               if(this.time.startTime =='' || this.time.endTime =='') {
                    alert('請選擇時間')
                    return
               }



               var json = this.time;
               var url = ctx + '/Excel/ConsiliumLineService/ConsiliumLineService?'
               var params = new URLSearchParams("?params=" + JSON.stringify(json));

               var aLink = document.createElement('a');
               aLink.href = url + params.toString();
               aLink.target = "_blank";
               aLink.click();
             }
           }
         })
    </script>
  </t:layout>

