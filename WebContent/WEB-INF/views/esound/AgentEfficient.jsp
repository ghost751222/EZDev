

    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

      <t:layout>
        <div class="container" id="app">


                     <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text">展開方式</span>
                         <time-type-select v-model="condition.timeType"></time-type-select>
                      </div>


            <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text">人員</span>
                       <users-select v-model="condition.user" :data="userData"></users-select>
                      </div>



            <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text">日期別</span>
                       <date-type-select v-model="condition.dateType"></date-type-select>
                      </div>
                <date-picker  :time="condition.time"></date-picker>


           <button class="btn btn-primary" @click="query">下載報表</button>
        </div>
        <script>
             var app = new Vue({
               el: '#app',
               data: {
                  userData:[{"value":"","text":""}],
                  condition:{
                    time:{
                        startTime: '2022-01-01',
                        endTime: '2022-12-02',
                    },
                    timeType:'Y',
                    dateType:'All',
                    user:''
                  },
                  serviceName:'AgentEfficient'
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
               },mounted(){
                       this.userData = this.userData.concat(users.filter(u=>u.unitType == "H").map(function(u) { return  {'value':u.userID,'text':u.userID + " " +u.userName } }))
               }
             })
        </script>
      </t:layout>

