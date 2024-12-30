<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout pageTitle="業務職掌查詢">

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
               <div class="col-md-3">
                                <div class="form-group">
                                            <label for="sections">關鍵字</label>
                                            <input type="text" v-model="keyword" class="form-control"/>
                                </div>
                        </div>
        </div>


        <br/>
        <div class="row w-25">
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
                >``
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
        keyword:"",
        records:[],
        sections: "",
        units: "",
        sectionsArray: [],
        perPage: 10,
        fields: [
                 {name:'sId',title:'項次'},
                 //{name:'unitName',title:'單位名稱'},
                 {name:'sectionName',title:'科室名稱'},
                 {name:'businessName',title:'業務名稱'},
                 {name:'businessContent',title:'業務內容'},
                 {name:'connector',title:'業務聯絡人姓名'},
                 {name:'tel',title:'業務聯絡人電話'},
                 {name:'extension',title:'業務聯絡人分機'},
               ]
      },
      methods: {
        query: function () {

          var unitValue = this.units;
          var sectionValue = this.sections;
          var keyword = this.keyword
          if (unitValue == "" || unitValue == "All") unitValue = null;
          if (sectionValue == "") sectionValue = null;
          if (keyword == "") keyword = null;

          var url = ctx + "/SectionBusiness/find?";
          var params = new URLSearchParams("?unitId=" + unitValue + "&sectionId=" + sectionValue + "&keyword=" + keyword);
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
          if(this.keyword != ''){
                local = local.filter(l => (l.businessContent && l.businessContent.includes(this.keyword)) || (l.businessName && l.businessName.includes(this.keyword)))
          }
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
      },watch:{
        keyword(){
           this.$refs.vuetable.refresh();
        }
      },mounted() {
           var _this = this;
            document.onkeypress = function(e){
                 if(e.keyCode == 13) _this.query()
           }
       }
    });

    </script>
</t:layout>
