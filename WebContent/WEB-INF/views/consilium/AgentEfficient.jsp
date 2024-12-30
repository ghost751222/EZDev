<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

        <t:layout>

            <div class="container" id="app">

                <div class="input-group input-group-sm mb-3">
                    <span class="input-group-text">人員</span>
                    <vue-multiselect v-model="value" :options="data" :multiple="true" :close-on-select="false" class="form-control"
                        :clear-on-select="true" :preserve-search="true"  label="text" @input="input"
                        track-by="value" >
                    </vue-multiselect>

                </div>

                <div class="input-group input-group-sm mb-3">
                    <span class="input-group-text">展開方式</span>
                    <time-type-select v-model="condition.timeType"></time-type-select>
                </div>


                <!--<div class="input-group input-group-sm mb-3">
                    <span class="input-group-text">人員</span>
                    <users-select v-model="condition.user" :data="userData"></users-select>
                </div> -->



                <!--<div class="input-group input-group-sm mb-3">
                    <span class="input-group-text">日期別</span>
                    <date-type-select v-model="condition.dateType"></date-type-select>
                </div>-->
                <date-picker :time="condition.time"></date-picker>


                <button class="btn btn-primary" @click="query">下載報表</button>
            </div>
            <script>
                var app = new Vue({
                    el: '#app',
                    data: {
                        value: [],
                        userData: [],
                        data:[{ "value": "All", "text": "全部" },{ "value": "cancel", "text": "全部取消" }],
                        condition: {
                            time: {
                                startTime: '2022-12-03',
                                endTime: '',
                            },
                            timeType: 'Y',
                            dateType: 'All',
                            user:null
                        },
                        serviceName: 'ConsiliumAgentEfficient'
                    }, methods: {
                        input:function(value, id){
                            var cancel = value.filter(v=> v.value =="cancel")
                            var all = value.filter(v=> v.value =="All")
                            if(cancel.length >0) this.value =[]
                            if(all.length >0) this.value =JSON.parse(JSON.stringify(this.userData))
                        },
                        query: function () {
                            if(this.value.length >0){
                                this.condition.user = this.value.map(v=>  v.value).toString()
                            }else{
                                alert('請選擇人員')
                                return
                            }

                            if (this.condition.time.startTime == '' || this.condition.time.endTime == '') {
                                alert('請選擇時間')
                                return
                            }
                            var json = this.condition;
                            var url = ctx + '/Excel/' + this.serviceName + '/' + this.serviceName + '?'
                            var params = new URLSearchParams("?params=" + JSON.stringify(json));
                            var aLink = document.createElement('a');
                            aLink.href = url + params.toString();
                            aLink.target = "_blank";
                            aLink.click();
                        }
                    }, mounted() {

                        this.userData = users.filter(u => u.account.includes('fet') && u.account.substring(3) <=15 ).map(function (u) { return { 'value': u.userID, 'text': u.userName } });
                        this.data = this.data.concat(this.userData)
                    }
                })
            </script>
        </t:layout>