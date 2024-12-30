<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>

    <head>
        <title>公告訊息維護</title>

        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.1.3/css/bootstrap.min.css">
        <link href="../resources/css/jquery/jquery-ui.css?3" rel="stylesheet">
        <link href="../resources/css/semantic.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>

        <script src="../resources/js/vue.js"></script>
        <script src="../resources/js/vuetable.js"></script>
        <style>
            .form-group {
                margin-bottom: 1.5em;
            }
        </style>
    </head>

    <body>
        <div class="container" id="app">

            <div class="row">


                <div class="pagination ui basic segment grid">
                    <vuetable-pagination ref="pagination" @vuetable-pagination:change-page="onChangePage">
                    </vuetable-pagination>
                    <div style="float:right;">
                        <button class="btn btn-primary" @click="addClick">新增</button>
                    </div>

                </div>
                <vuetable ref="vuetable" :api-mode="false" pagination-path="pagination" :data-manager="dataManager"
                    :per-page="perPage" :fields="fields" @vuetable:pagination-data="onPaginationData">
                    <template slot="actions" scope="props">
                        <div class="table-button-container">
                            <button class="btn btn-warning btn-sm" @click="editRow(props.rowData)">
                                <span class="glyphicon glyphicon-pencil"></span> Edit
                            </button>&nbsp;&nbsp;
                            <button class="btn btn-danger btn-sm" @click="deleteRow(props.rowData)">
                                <span class="glyphicon glyphicon-trash"></span> Delete
                            </button>&nbsp;&nbsp;
                            <slot name="customized" :rowData="props.rowData">
                            </slot>
                        </div>
                    </template>
                </vuetable>


            </div>
            <div id="editTab" style="display:none">
                <form @submit.prevent="submit">
                    <input type="submit" ref="form" style='display:none' />

                    <div class="form-group row">
                        <label for="id" class="col-sm-1 col-form-label">編號</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="announce.id" id="id" disabled="true">
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="subject" class="col-sm-1 col-form-label">主旨</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="announce.subject" required id="subject">
                        </div>
                    </div>



                    <div class="form-group row ">
                        <label for="importance" class="col-sm-1 col-form-label">重要性</label>
                        <div class="col-sm-9">
                            <select class="form-control" v-model="announce.importance" required id="importance">
                                <option v-for="s in importanceArray" :value="s.value">{{s.text}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group row ">
                        <label for="category" class="col-sm-1 col-form-label">公告類別</label>
                        <div class="col-sm-9">
                            <select class="form-control" v-model="announce.category" required id="category">
                                <option v-for="s in categoryArray" :value="s.value">{{s.text}}</option>
                            </select>
                        </div>
                    </div>




                    <div class="form-group row">
                        <label for="startTime" class="col-sm-1 col-form-label">開始時間</label>
                        <div class="col-sm-9">
                            <input type="date" class="form-control" v-model="announce.startTime" required
                                id="startTime">
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="endTime" class="col-sm-1 col-form-label">結束時間</label>
                        <div class="col-sm-9">
                            <input type="date" class="form-control" v-model="announce.endTime" id="endTime">
                        </div>
                    </div>


                </form>
                <div>

                </div>

            </div>
        </div>
    </body>
    <script type="text/javascript">
        var ctx = "${pageContext.request.contextPath}"
        function closeDialog() {
            $(this).dialog('close')
        }

        Vue.use(Vuetable)
        Vue.component('vuetable-pagination', Vuetable.VuetablePagination)
        Vue.component("vuetable-pagination-dropdown", Vuetable.VueTablePaginationDropDown);
        Vue.component("vuetable-pagination-info", Vuetable.VueTablePaginationInfo);
        var app = new Vue({
            el: '#app',
            data: {
                records: [],
                fields: [
                    { name: 'id', title: '編號' },
                    {
                        name: 'importance', title: '公告類別', formatter: function (value) {
                            var d = app.importanceArray.find(c => c.value == value)
                            var text = value
                            if (d) text = d.text
                            return text


                        }
                    },
                    { name: 'subject', title: '主旨' },
                    {
                        name: 'category', title: '公告類別', formatter: function (value) {
                            var d = app.categoryArray.find(c => c.value == value)
                            var text = value
                            if (d) text = d.text
                            return text


                        }
                    },
                    { name: 'startTime', title: '開始時間' },
                    { name: 'endTime', title: '結束時間' },
                    { name: 'createTime', title: '建立時間' },
                    { name: 'creator', title: '建立者' },
                    { name: 'actions', title: '功能' }

                ],
                perPage: 10,
                announce: {
                    id: null,
                    subject: null,
                    category: null,
                    importance: null,
                    startTime: null,
                    endTime: null
                }, categoryArray: [
                    { value: "", text: "" },
                    { value: "1", text: "活動通知" },
                    { value: "2", text: "作業流程" }
                ], importanceArray: [
                    { value: "", text: "" },
                    { value: "1", text: "重要" },
                    { value: "2", text: "一般" }
                ],
                method: ''
            }, watch: {
                records(newVal, oldVal) {
                    this.$refs.vuetable.refresh();
                }
            }, methods: {
                findAll() {
                    var method = 'get'
                    var url = ctx + '/Announce/findAll'
                    var response = this.callAjax(method, url)
                    this.records = response;

                },
                addClick() {
                    var object = JSON.parse(JSON.stringify(this.announce))

                    Object.entries(object).forEach(([key, value]) => {
                        object[key] = null
                    })
                    console.log(object)

                    this.announce = object
                    this.method = 'post'
                    this.openDialog('新增')
                },
                openDialog(title) {
                    $("#editTab").dialog({
                        title: title,
                        modal: true,
                        height: 500,
                        width: 1000,
                        buttons: {
                            "確定建立": this.saveClick,
                            "關閉視窗": closeDialog
                        }
                    });
                },
                submit() {

                    if (this.announce.endTime == '') this.announce.endTime = null
                    var requestData = JSON.stringify(this.announce)
                    var method = this.method
                    var url = ctx + '/Announce/'
                    var response = this.callAjax(method, url, requestData)
                    if (response) {
                        alert('儲存成功')
                        localStorage.removeItem('treeData');
                        location.reload()
                    }
                },
                saveClick() {
                    this.$refs.form.click()
                },
                callAjax(method, url, requestData) {
                    var response;
                    var $ajax = $.ajax({
                        method: method,
                        url: url,
                        contentType: 'application/json',
                        async: false,
                        data: requestData,
                        success: function (res) {
                            response = res;
                        }, error: function (err) {
                            console.log(err)
                            alert('發生異常錯誤')
                        }

                    });
                    return response
                },

                onPaginationData(paginationData) {
                    this.$refs.pagination.setPaginationData(paginationData)
                },
                onChangePage(page) {
                    this.$refs.vuetable.changePage(page)
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
                        data: local.slice(from, to)
                    };
                }, editRow(data) {
                    this.openDialog('編輯')
                    this.method = 'put'
                    this.announce = JSON.parse(JSON.stringify(data))

                }, deleteRow(data) {
                    var requestData = JSON.stringify(data)


                    var method = 'delete'
                    var url = ctx + '/Announce/'
                    var response = this.callAjax(method, url, requestData)
                    if (response) {
                        alert('刪除成功')
                        localStorage.removeItem('treeData');
                        location.reload()
                    }
                }
            }, mounted: function () {
                this.findAll()
            }
        })
    </script>

    </html>