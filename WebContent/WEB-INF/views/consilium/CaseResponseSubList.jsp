<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

        <t:layout>
            <div class="container" id="app">


                <div class="input-group input-group-sm mb-3">
                    <span class="input-group-text">單位</span>

                    <vue-multiselect v-model="unitsArray" :options="data" :multiple="true" :close-on-select="false"
                        class="form-control" :clear-on-select="true" :preserve-search="true" label="text" @input="input"
                        track-by="value">
                    </vue-multiselect>
                </div>
                <date-picker :time="condition.time"></date-picker>


                <button class="btn btn-primary" @click="query">下載報表</button>
            </div>
            <script>
                var app = new Vue({
                    el: '#app',
                    data: {
                        data: [{ "value": "All", "text": "全部" }, { "value": "cancel", "text": "全部取消" }],
                        units: [],
                        unitsArray: [],
                        condition: {
                            time: {
                                startTime: '2022-12-03',
                                endTime: '',
                            },
                            unitCode: []
                        },
                        serviceName: 'ConsiliumCaseResponseSubList'
                    }, methods: {

                        input: function (value, id) {
                            var cancel = value.filter(v => v.value == "cancel")
                            var all = value.filter(v => v.value == "All")
                            if (all.length > 0) this.unitsArray = JSON.parse(JSON.stringify(this.units))
                            if (cancel.length > 0) this.unitsArray = []
                        },
                        query: function () {
                            if (this.condition.time.startTime == '' || this.condition.time.endTime == '') {
                                alert('請選擇時間')
                                return
                            }

                            if (this.unitsArray.length > 0) {
                                this.condition.unitCode = this.unitsArray.map(v => v.value).toString()
                            } else {
                                alert('請選擇單位')
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
                        this.units = units.filter(u => u.value != "")
                        this.data = this.data.concat(this.units)

                    }
                })
            </script>
        </t:layout>