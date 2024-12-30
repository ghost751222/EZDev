<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>

    <head>
        <title>Units</title>

        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" />
        <link href="../resources/css/jquery/jquery-ui.css?3" rel="stylesheet">
        <link href="../resources/css/semantic.min.css" rel="stylesheet">

        <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.5.1.js"></script>
        <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.13.1.js"></script>

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


                <div class="col col-md-3">

                    <div class="form-group ">
                        <label for="units">單位代碼</label>
                        <input type="text" v-model="queryUnitCode" class="form-control" />
                    </div>

                    <div class="form-group ">
                        <label for="units">單位名稱</label>
                        <input type="text" v-model="queryUnitName" class="form-control" />
                    </div>
                </div>



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
                    <div class="form-group row ">
                        <label for="spUnitCode" class="col-sm-2 col-form-label">上層單位代碼</label>
                        <div class="col-sm-9">
                            <select class="form-control" v-model="units.superUnit">
                                <option v-for="s in spUnitCode" :value="s.value">{{s.value}}-{{s.text}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="unitCode" class="col-sm-2 col-form-label">單位代碼</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="units.unitCode" required>
                        </div>
                    </div>

                    <div class="form-group row ">
                        <label for="unitType" class="col-sm-2 col-form-label">單位類別</label>
                        <div class="col-sm-9">

                            <select class="form-control" v-model="units.unitType">
                                <option value=""></option>

                                <option value="M">M</option>
                                <option value="H">H</option>
                                <option value="Z">Z</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="unitName" class="col-sm-2 col-form-label">單位名稱</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="units.unitName" required>
                        </div>
                    </div>

                    <div class="form-group row ">
                        <label for="policeOfficeLevel" class="col-sm-2 col-form-label">警察單位層級</label>
                        <div class="col-sm-9">
                            <select class="form-control" v-model="units.policeOfficeLevel">
                                <option value=""></option>
                                <option value="1">1-局</option>
                                <option value="2">2-分局</option>
                                <option value="3">3-派出所</option>
                                <option value="10">10-內政部司</option>
                                <option value="11">11-內政部科</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="sname" class="col-sm-2 col-form-label">單位簡稱</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="units.sname" required>
                        </div>
                    </div>

                    <div class="form-group row ">
                        <label for="spUnitCode" class="col-sm-2 col-form-label">所屬上層(權限)</label>
                        <div class="col-sm-9">
                            <select class="form-control" v-model="units.spUnitCode">
                                <option v-for="s in spUnitCode" :value="s.value">{{s.value}}-{{s.text}}</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group row ">
                        <label for="spServiceType" class="col-sm-2 col-form-label">可查詢案件類別</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" v-model="units.spServiceType" requiredd>
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
                queryUnitCode: '',
                queryUnitName: '',
                records: [],
                spUnitCode: [{ value: '', text: '' }],
                fields: [
                    { name: 'superUnit', title: '上層單位代碼' },
                    { name: 'unitCode', title: '單位代碼' },
                    { name: 'unitType', title: '單位類別' },
                    { name: 'unitName', title: '單位名稱' },
                    { name: 'policeOfficeLevel', title: '警察單位層級' },
                    { name: 'sname', title: '單位簡稱' },
                    { name: 'spUnitCode', title: '所屬上層(權限)' },
                    { name: 'spServiceType', title: '可查詢案件類別' },
                    { name: 'actions', title: '功能' }
                ],
                perPage: 10,
                units: {
                    cityCode: "0",
                    isDVPCPoliceSuperUnit: null,
                    orderNumber: null,
                    policeOfficeLevel: null,
                    sname: null,
                    spServiceType: null,
                    spUnitCode: null,
                    superUnit: null,
                    unitAddress: null,
                    unitCode: null,
                    unitContact: null,
                    unitFax: null,
                    unitName: null,
                    unitTel: null,
                    unitType: null
                },
                method: ''
            }, watch: {
                records(newVal, oldVal) {
                    this.$refs.vuetable.refresh();
                },
                queryUnitName() {
                    this.$refs.vuetable.refresh();
                },
                queryUnitCode() {
                    this.$refs.vuetable.refresh();
                }
            }, methods: {
                findAll() {
                    var method = 'get'
                    var url = ctx + '/Units/findAll'
                    var response = this.callAjax(method, url)
                    this.records = response;

                    this.spUnitCode = this.spUnitCode.concat(response.map(r => {
                        return { value: r.unitCode, text: r.unitName }
                    }))
                },
                addClick() {
                    var object = JSON.parse(JSON.stringify(this.units))

                    Object.entries(object).forEach(([key, value]) => {
                        object[key] = null
                    })
                    this.units = object
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
                    this.units.cityCode = 0
                    this.units.unitType = 'M'
                    //this.units.policeOfficeLevel ='1'
                    if (this.units.superUnit == '') this.units.superUnit = null

                    this.units.spUnitCode = this.units.superUnit
                    var requestData = JSON.stringify(this.units)


                    var method = this.method
                    var url = ctx + '/Units/'
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
                    if (this.queryUnitName != '') {
                        local = local.filter(l => l.unitName.includes(this.queryUnitName))
                    }

                    if (this.queryUnitCode != '') {
                        local = local.filter(l => l.unitCode.includes(this.queryUnitCode))
                    }

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
                    this.units = data

                }, deleteRow(data) {
                    var requestData = JSON.stringify(data)


                    var method = 'delete'
                    var url = ctx + '/Units/'
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

    </html