<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

        <t:layout pageTitle="FAQ查詢">
            <style>
                .nowrap {
                    white-space: nowrap;
                }
                .pre-wrap{
                    white-space: pre-wrap;
                }
            </style>
            <div class="container-fluid" id="app">

                <div class="row">
                    <div class="col col-md-1">
                        <div class="form-group ">
                            <label for="units">編號</label>
                            <input v-model="id" class="form-control" type="text" />
                        </div>
                    </div>

                    <div class="col col-md-3">
                        <div class="form-group ">
                            <label for="units">關鍵字</label>
                            <input v-model="keyword" class="form-control" type="text"/>
                        </div>
                    </div>

                </div>


                <br />
                <div class="row w-25">
                    <div>
                        <button class="btn btn-primary" @click="query">查詢</button>
                        <button class="btn btn-secondary" @click="open">網頁開啟</button>
                    </div>
                </div>
                <br />
                <div class="row">
                    <div class="pagination ui basic segment grid">
                        <vuetable-pagination ref="pagination" @vuetable-pagination:change-page="onChangePage">
                        </vuetable-pagination>
                    </div>
                    <vuetable ref="vuetable" :api-mode="false" pagination-path="pagination" :data-manager="dataManager" track-by="sId"
                        :per-page="perPage" :fields="fields" @vuetable:pagination-data="onPaginationData">
                         <template slot="action"  scope="props">
                                    <button class="btn btn-info btn-sm"  @click="hit(props.rowData)">
                                        <span class="glyphicon glyphicon-trash">參閱點擊</span>
                                    </button>
                         </template>
                    </vuetable>
                </div>
            </div>
            <script>
                var app = new Vue({
                    el: "#app",
                    data: {
                        records: [],
                        keyword: "",
                        id: "",
                        sectionsArray: [],
                        perPage: 10,
                        fields: [
                            { name: 'sId', title: '系統編號', width: '3%' },
                            {
                                name: 'unitId', title: '單位', width: '8%', formatter(value) {
                                    return getUnitName(value);
                                }
                            },
                            {
                                name: 'sectionId', title: '科室', width: '10%', formatter(value) {
                                    return getSectionName(value);
                                }
                            },
                            {
                                name: 'question', title: '問題', width: '10%', formatter(value) {
                                    //return "<textarea readonly rows='5' cols='50' style='width:100%'>" + value + "</textarea>"
                                     app.keyword.split(",").forEach(k=>{
                                            value = value.replace(k, "<h5 style='color:red'>" + k + "</h5>")
                                     })
                                     return value
                                }
                            },
                            {
                                name: 'answer', title: '回答', width: '40%',dataClass:'pre-wrap', formatter(value) {
                                     app.keyword.split(",").forEach(k=>{
                                        value = value.replace(k, "<h5 style='color:red'>" + k + "</h5>")
                                     })
                                     return value
                                }
                            },
                            {
                                name: 'keyword', title: '關鍵字', width: '10%', formatter(value) {

                                    app.keyword.split(",").forEach(k=>{
                                       value = value.replace(k, "<h5 style='color:red'>" + k + "</h5>")
                                    })
                                    return value

                                }
                            },
                            {
                                name: 'connector', title: '聯絡人', titleClass: 'nowrap', width: '1%'
                            },
                            {
                                name: 'tel', title: '聯絡人電話', titleClass: 'nowrap', width: '1%',
                            },
                            {
                                name: 'extension', title: '聯絡人分機', titleClass: 'nowrap', width: '1%'

                            },
                            { name: 'lastUpdateTime', title: '最後修改時間', titleClass: 'nowrap', dataClass: 'nowrap', width: '5%' },
                            { name: 'hit', title: '點擊率', titleClass: 'nowrap', dataClass: 'nowrap', width: '5%' },
                            { name: 'action', title: '', titleClass: 'nowrap', dataClass: 'nowrap', width: '5%' },

                        ]
                    },
                    watch:{
                        records(){
                            this.$nextTick(function () {
                                 this.$refs.vuetable.refresh()
                            });
                        }
                    },
                    methods: {
                        hit:function(data){
                            var sId = data.sId
                            var url = ctx + "/QuestionAnswerRelation/hit/" + sId
                            var res = this.callAjax('put',url,null);
                            if(res){
                               data.hit = res.hit

                            }


                        },
                        query: function () {

                            var keyword = this.keyword;
                            var id = this.id;
                            var keysForDel = [];
                            var url = ctx + "/QuestionAnswerRelation/find?";
                            var params = new URLSearchParams();
                            params.append("keyword", keyword)
                            params.append("id", id)

                            params.forEach((value, key) => {
                                if (value == '') {
                                    keysForDel.push(key);
                                }
                            })

                            keysForDel.forEach(key => {
                                params.delete(key);
                            });

                            var res;
                            //var res = sessionStorage.getItem('data');
                            //if (res != null)
                                //res = JSON.parse(res);
                            //else
                                res = this.callAjax("get", url + params.toString());

                            this.records  =res;
                            sessionStorage.setItem('data', JSON.stringify(res))
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
                            var keyword = this.keyword
                            //if(keyword){
                                //local = local.filter(l => {
                                  //  try{
                                    //  return  l.answer.includes(keyword) || l.question.includes(keyword) || l.keyword.includes(keyword)
                                    //}catch(e){
                                     // return true;
                                    //}
                                //})
                            //}



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
                            var $ajax = $.ajax({
                                method: method,
                                url: url,
                                async: false,
                                data: requestData,
                                success: function (res) {
                                   response =res
                                },
                                error: function (err) {
                                    console.log(err);
                                    alert("發生異常錯誤");
                                },
                            });
                            return response;
                        },
                        open: function () {
                            window.open(location.href)
                        }
                    },
                    mounted() {
                        var _this = this;
                        this.query()
                        document.onkeypress = function(e){
                            if(e.keyCode == 13) _this.query()

                        }
                    }
                });

            </script>
        </t:layout>