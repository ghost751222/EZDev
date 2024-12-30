<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout pageTitle="科室資料查詢">
    <div class="container-fluid" id="app">

        <div class="row">
            <div class="col col-md-3">
                <div class="form-group ">
                    <label for="units">單位</label>
                    <units-select v-model="units" @change="change"/>
                </div>
            </div>
            <div class="col-md-3">
                <div class="form-group">
                    <label for="sections">科室</label>
                    <sections-select v-model="sections" :data="sectionsArray" @change="sectionChange"/>
                </div>
            </div>


        </div>


        <br/>
        <div class="row w-25 ">
            <div>
                <button class="btn btn-primary" @click="query">查詢</button>
                <button class="btn btn-secondary" @click="open">網頁開啟</button>
            </div>
        </div>
        <br/>
        <div class="row">
            <div class="pagination ui basic segment grid">
                <vuetable-pagination
                        ref="pagination"
                        @vuetable-pagination:change-page="onChangePage"
                >
                </vuetable-pagination>
            </div>
            <vuetable
                    ref="vuetable"
                    :api-mode="false"
                    pagination-path="pagination"
                    :data-manager="dataManager"
                    :per-page="perPage"
                    :fields="fields"
                    @vuetable:pagination-data="onPaginationData"
            >
            </vuetable>
        </div>
    </div>
    <script>
    var app = new Vue({
      el: "#app",
      data: {
        records:[],
        sections: "",
        units: "",
        sectionsArray: [],
        perPage: 10,
        fields: [
                 {name:'unitName',title:'單位名稱'},
                 {name:'sectionName',title:'科室名稱'},
                 {name:'occupation1',title:'第一聯絡窗口人職稱'},
                 {name:'connector1',title:'第一聯絡窗口人姓名'},
                 {name:'tel1',title:'第一聯絡窗口人電話'},
                 {name:'extension1',title:'第一聯絡窗口人分機'},
                 {name:'email1',title:'第一聯絡窗口人EMAIL'},
                 {name:'occupation2',title:'第二聯絡窗口人職稱'},
                 {name:'connector2',title:'第二聯絡窗口人姓名'},
                 {name:'tel2',title:'第二聯絡窗口人電話'},
                 {name:'extension2',title:'第二聯絡窗口人分機'},
                 {name:'email2',title:'第二聯絡窗口人EMAIL'}
                ]
      },
      methods: {
        query: function () {

          var unitValue = this.units;
          var sectionValue = this.sections;

          if (unitValue == "") unitValue = null;
          if (sectionValue == "") sectionValue = null;

          var url = ctx + "/SectionData/find?";
          var params = new URLSearchParams("?unitId=" + unitValue + "&sectionId=" + sectionValue);
          this.callAjax("get", url + params.toString());
        },
        change: function (target) {
          this.sections = ''
          this.sectionsArray = [{ value: "", text: "" }].concat(
            this.$sections.filter((s) => s.superUnit == target.value)
          );
        },
        sectionChange(){
                     this.query()
        },
        onPaginationData(paginationData) {
          this.$refs.pagination.setPaginationData(paginationData);
        },
        onChangePage(page) {
          this.$refs.vuetable.changePage(page);
        },
        dataManager(sortOrder, pagination) {
          if (this.records.length < 1) return this.records;

          let local = this.records;
          pagination = this.$refs.vuetable.makePagination(
            local.length,
            this.perPage
          );

          let from = pagination.from - 1;
          let to = from + this.perPage;

          return {
            pagination: pagination,
            data: local.slice(from, to),
          };
        },
        callAjax(method, url, requestData) {
          var response;
          var _this = this
          var $ajax = $.ajax({
            method: method,
            url: url,
            async: false,
            data: requestData,
            success: function (res) {
               _this.records =res;
               _this.$nextTick(function () {
                 _this.$refs.vuetable.refresh()
               });

            },
            error: function (err) {
              console.log(err);
              alert("發生異常錯誤");
            },
          });
          return response;
        },
        open:function(){
             window.open(location.href)
        }
      }

    });

    </script>
</t:layout>
