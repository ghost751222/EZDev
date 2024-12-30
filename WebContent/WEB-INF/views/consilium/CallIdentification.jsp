<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

    <t:layout>
      <div class="container" id="app">


        <div class="input-group input-group-sm mb-3">
          <span class="input-group-text">展開方式</span>
          <time-type-select v-model="condition.timeType" :data="timeType"></time-type-select>
        </div>

        <div class="input-group input-group-sm mb-3">
          <span class="input-group-text">業務類別</span>
          <vue-multiselect v-model="condition.serviceType" :options="serviceTypeData" :multiple="true"
            :close-on-select="false" class="form-control" :clear-on-select="true" :preserve-search="true" label="text"
            group-values="data" group-label="label" :group-select="true"
            @input="input" track-by="value">
          </vue-multiselect>
        </div>

        <div class="input-group input-group-sm mb-3">
          <span class="input-group-text">次類別</span>
          <vue-multiselect v-model="condition.serviceItem" :options="serviceItemData" :multiple="true"
            :close-on-select="false" class="form-control" :clear-on-select="true" :preserve-search="true" label="text"
            group-values="data" group-label="label" :group-select="true"
            track-by="value">
          </vue-multiselect>
        </div>

        <div class="input-group input-group-sm mb-3">
          <span class="input-group-text">來電類別</span>
          <call-category-select v-model="condition.callCategory"></call-category-select>
        </div>

        <div class="input-group input-group-sm mb-3">
          <span class="input-group-text">服務類別</span>
          <service-category-select v-model="condition.serviceCategory"></service-category-select>
        </div>


        <date-picker :time="condition.time"></date-picker>


        <button class="btn btn-primary" @click="query">下載報表</button>
      </div>
      <script>
        var app = new Vue({
          el: '#app',
          data: {
            serviceTypeData: [],
            serviceItemData: [],
            timeType:[],
            condition: {
              time: {
                startTime: '2022-12-03',
                endTime: '',
              },
              timeType: 'Y',
              serviceType: [],
              serviceItem: [],
              callCategory: 'All',
              serviceCategory: 'All'
            },
            serviceName: 'ConsiliumCallIdentification'
          }, methods: {
            input: function (value, id) {
              var _defaultData = {label:"全部",data:[]}
              var _data =[]
              for(v of value){
                var subItem = serviceItem.filter(ele=> ele.typeId == v.value && ele.state ==1).map(ele=>({'value': ele.itemId, 'text': ele.itemName  }))
                _data = _data.concat(subItem)
              }
              this.serviceItemData = [{label:"全部",data:_data}];

            },
            query: function () {
              if (this.condition.time.startTime == '' || this.condition.time.endTime == '') {
                alert('請選擇時間')
                return
              }

              if(this.condition.serviceType.length ==0){
                 alert('請選擇業務類別')
                 return
              }
              var condition = JSON.parse(JSON.stringify(this.condition));
              condition.serviceType = this.condition.serviceType.map(ele => ele.value).toString()
              condition.serviceItem = this.condition.serviceItem.map(ele => ele.value).toString()
              var url = ctx + '/Excel/' + this.serviceName + '/' + this.serviceName + '?'
              var params = new URLSearchParams("?params=" + JSON.stringify(condition));
              var aLink = document.createElement('a');
              aLink.href = url + params.toString();
              aLink.target = "_blank";
              aLink.click();
            }
          }, mounted() {
            this.serviceTypeData = [{label:"全部",data:serviceType.filter((u)=>u.state ==1).map(function (u) { return { 'value': u.typeId, 'text': u.typeName } })}]
            this.serviceItemData = [{label:"全部",data:[]}]
            this.timeType = time_type.filter(ele=> ele.value != 'W').filter(ele=> ele.value != 'A')

          }
        })
      </script>
    </t:layout>