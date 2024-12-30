
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

      <t:layout pageTitle="1996進線量及秒數">
        <div class="container" id="app">
            <date-picker  :time="condition.time"></date-picker>

           <button class="btn btn-primary" @click="query">下載報表</button>
        </div>
        <script>
             var app = new Vue({
               el: '#app',
               data: {
                  condition:{
                    time:{
                          startTime: '2022-12-03',
                          endTime: '',
                    },

                  },
                  serviceName:'ConsiliumIncomeSec'
               }, methods: {
                 query: function () {
                   if(this.condition.time.startTime =='' || this.condition.time.endTime =='') {
                        alert('請選擇時間')
                        return
                   }
                   var json = this.condition;
                   var url = ctx + '/Excel/' +this.serviceName + '/' +  this.serviceName  +'?'
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

