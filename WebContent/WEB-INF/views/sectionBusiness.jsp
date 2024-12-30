<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
        <t:layout pageTitle="科室資料維護">
            <t:menu>
                <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/xlsx.min.js?${time}"></script>
                <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/excel.js?${time}"></script>
                <script type="text/x-jquery-tmpl" id="businessItem">

            <tr>
                 <td class="center">\${index}</td>
                 <td >\${businessName}  </td>
                 <td >\${businessContent}</td>
                 <td class="center">
                    <button class="button" onclick="editClick('view',  \${JSON.stringify(this)} )">檢視</button>
                    <button class="button" onclick="editClick('edit',  \${JSON.stringify(this)} )">編輯</button>
                    <button class="button" onclick="deleteClick( \${JSON.stringify(this)} )">刪除</button>
                 </td>
            </tr>
        </script>

                <div id="contentTab">
                    <div>
                        <div class="main-title">{{sectionName}}-業務執掌列表</div>
                        <div style="display:flex; flex-direction: row-reverse;">
                            <button id="add" class="main-button">新增業務</button>
                            <button id="excel" class="main-button" @click="excel">匯出</button>
                        </div>
                        <hr />

                        <table width="100%" class="main-table">
                            <thead>
                                <tr bgcolor="#dedeef">
                                    <th>項次</th>
                                    <th>業務名稱</th>
                                    <th>業務內容</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="tb"></tbody>
                        </table>


                        <div class="pagination ui basic segment grid">
                            <vuetable-pagination ref="pagination" @vuetable-pagination:change-page="onChangePage">
                            </vuetable-pagination>

                        </div>
                    </div>
                </div>

                <div id="editTab" style="display:none;">
                    <div style="width:100%">
                        <div class="main-title"> 業務執掌資料</div>
                        <hr />
                        <table class="main-table" width="100%">
                            <tr>
                                <td class="center">單位名稱</td>
                                <td>
                                    <span id="unitName">unitName</span>
                                    <span id="unitId" style="display:none">unitId</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="center">科室名稱</td>
                                <td colspan="2">
                                    <span id="sectionName">sectionName</span>
                                    <span id="sectionId" style="display:none">sectionId</span>
                                </td>
                            </tr>



                            <tr>
                                <td class="center">業務名稱</td>
                                <td>
                                    <textarea id="businessName" value="businessName" rows="3" cols="50"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="center">業務內容</td>
                                <td>
                                    <textarea id="businessContent" value="businessContent" rows="3"
                                        cols="50"></textarea>
                                </td>
                            </tr>



                            <tr>
                                <td class="center">相關查尋關鍵字</td>
                                <td>
                                    <textarea id="keyword" value="keyword" rows="3" cols="50"></textarea>
                                </td>
                            </tr>

                            <tr>
                                <td class="center">業務聯絡人姓名</td>
                                <td><input id="connector"></td>
                            </tr>


                            <tr>
                                <td class="center">業務聯絡人電話</td>
                                <td>
                                    <input id="tel">
                                    分機
                                    <input id="extension" size="10">
                                </td>

                            </tr>


                            <tr>
                                <td class="center">1966補充內容</td>
                                <td>
                                    <textarea id="editContent" value="editContent" rows="3" cols="50"></textarea>
                                </td>
                            </tr>

                            <tr>
                                <td class="center">1966補充關鍵字</td>
                                <td>
                                    <textarea id="addKeyword" value="addKeyword" rows="3" cols="50"></textarea>
                                </td>
                            </tr>

                            <tr>
                                <td class="center">附檔</td>
                                <td>
                                    <input type="file">
                                </td>
                            </tr>

                        </table>


                    </div>
                </div>


                <script type="text/javascript">

                    Vue.component('vuetable-pagination', Vuetable.VuetablePagination)
                    var app = new Vue({
                        el: '#contentTab',
                        data: {
                            sectionName:'',
                            records: [],
                            perPage: 10,
                            pagination: {
                                current_page: 1,
                                from: 1,
                                last_page: 0,
                                next_page_url: "",
                                per_page: 10,
                                prev_page_url: "",
                                to: 0,
                                total: 0
                            }
                        }, watch: {
                            records(newVal, oldVal) {
                                this.dataTableFresh()
                            }
                        },
                        methods: {
                            excel(){
                                 const aoa =[];
                                 var _data = null
                                 this.records.forEach(r=>{
                                    _data = {}
                                    _data["單位名稱"] = sectionData.unitName
                                    _data["科室名稱"] = sectionData.sectionName
                                    _data["業務名稱"] = r.businessName
                                    _data["業務內容"] = r.businessContent
                                    _data["相關查尋關鍵字"] = r.keyword
                                    _data["業務聯絡人姓名"] = r.connector
                                    _data["業務聯絡人電話"] = r.tel
                                    _data["分機"] = r.extension
                                    _data["1966補充內容"] = r.editContent
                                    _data["1966補充關鍵字"] = r.addKeyword
                                    aoa.push(_data)
                                 })

                                 var sheet = XLSX.utils.json_to_sheet(aoa);
                                 openDownloadDialog(sheet2blob(sheet), '業務執掌列表.xlsx');
                            },
                            dataTableFresh() {
                                this.pagination.from = 1
                                this.pagination.to = 0
                                this.pagination.total = 0
                                this.pagination.current_page = 1
                                this.pagination.last_page = 0
                                this.dataManager(null, this.pagination)
                            },
                            makePagination(length, pageSize) {
                                this.pagination.last_page = Math.ceil(length / pageSize)
                                this.pagination.per_page = pageSize
                                this.pagination.total = length
                                this.pagination.from = ((this.pagination.current_page - 1) * pageSize) + 1
                                this.pagination.to = this.pagination.current_page * pageSize
                                return this.pagination
                            },
                            onPaginationData(paginationData) {
                                this.$refs.pagination.setPaginationData(paginationData)
                            },
                            onChangePage(page) {
                                if (page == 'next') {
                                    this.pagination.current_page++
                                } else if (page == 'prev') {
                                    this.pagination.current_page--
                                } else {
                                    this.pagination.current_page = page
                                }

                                this.dataManager(null, this.pagination)
                                this.dataManager(null, this.pagination)
                            }, dataManager(sortOrder, pagination) {


                                if (this.records.length < 1) return this.records;

                                let local = this.records;
                                pagination = this.makePagination(
                                    local.length,
                                    this.perPage
                                );
                                let from = pagination.from - 1;
                                let to = from + this.perPage;
                                this.onPaginationData(pagination)
                                var jsonData = local.slice(from, to)
                                $('#tb').html('')
                                $("#businessItem").tmpl(jsonData).appendTo("#tb");
                            }
                        }
                    })


                    var ctx = "${pageContext.request.contextPath}"
                    var userData = { unitId: 'A1000', sectionId: 'A1001',sectionName: null }
                    userData.unitId  = authorization.superUnit
                    userData.sectionId  = authorization.unitCode

                    var units = null, sectionDataArray = null, sectionData
                    var reqData = {
                        sId: null,
                        pId: null,
                        businessName: null,
                        businessContent: null,
                        keyword: null,
                        connector: null,
                        tel: null,
                        editContent: null,
                        addKeyword: null,
                        extension: null,
                        creator: null,
                        createTime: null,
                        lastModifier: null,
                        lastUpdateTime: null,
                        score: null,
                        hitCount: null,
                        email: null

                    }

                    findUnits(); findAllSectionData();
                    function findUnits() {
                        if (sessionStorage.getItem('units')) {
                            units = JSON.parse(sessionStorage.getItem('units'))
                            return
                        }
                        var method = 'Get'
                        var url = ctx + '/Units/findAll'
                        var response = callAjax(method, url, null)
                        units = response
                        sessionStorage.setItem('units', JSON.stringify(response))

                    }


                    function findAllSectionData() {
                        if (sessionStorage.getItem('sectionDataArray')) {
                            sectionDataArray = JSON.parse(sessionStorage.getItem('sectionDataArray'))
                            return
                        }
                        var method = 'Get'
                        var url = ctx + '/SectionData/findAll'
                        var response = callAjax(method, url, null)
                        sectionDataArray = response
                        sessionStorage.setItem('sectionDataArray', JSON.stringify(response))
                    }

                    function findSectionData(unitId, sectionId) {
                        var sectionData = sectionDataArray.find(s => { return s.unitId == unitId && s.sectionId == sectionId })
                        if (sectionData != null) {
                            sectionData["unitName"] = units.find(u => u.unitCode == sectionData.unitId).unitName
                            sectionData["sectionName"] = units.find(u => u.unitCode == sectionData.sectionId).unitName
                        }
                        return sectionData;
                    }


                    function findAllSectionBusiness(sectionData) {
                        var method = 'get';
                        var url = ctx + '/SectionBusiness/' + (sectionData == null ? 0 : sectionData["sId"])
                        var response = callAjax(method, url, null)
                        response.forEach((r, i) => { r["index"] = i + 1 })

                        createData(response)
                    }

                    function loadPageData(unitId, sectionId) {
                        sectionData = findSectionData(unitId, sectionId)
                        app.sectionName = sectionData["sectionName"]
                        findAllSectionBusiness(sectionData)
                    }


                    function call(e, d) {
                        $('#tb').html('')
                        try {
                            closeDialog()
                        } catch (e) {
                        }

                        loadPageData(d.superUnit, d.unitCode)
                    }

                    function saveClick() {
                        elements.forEach(e => {
                            if (reqData.hasOwnProperty(e.id))
                                reqData[e.id] = e.value
                        })

                        var method = 'put'
                        if (reqData.sId == null) {
                            method = 'post'
                        }
                        var response = callAjax(method, ctx + '/SectionBusiness/', JSON.stringify(reqData))
                        if (response) {
                            alert('儲存資料成功')
                            location.reload()
                        }

                    }

                    function addClick() {
                        if (sectionData == null) {
                            alert('請先新增科室資料')
                            return
                        }
                        resetClick()
                        var buttons = { "重設": resetClick, "儲存": saveClick, "關閉": closeDialog }
                        openDialog('新增業務職掌', buttons)
                        reqData.sId = null
                        reqData.pId = sectionData.sId
                        showSectionBusiness(JSON.parse(JSON.stringify(reqData)))
                    }
                    function editClick(type, data) {
                        var buttons = { "重設": resetClick, "儲存": saveClick, "關閉": closeDialog }
                        resetClick()

                        reqData.sId = data.data.sId
                        showSectionBusiness(data.data)
                        if (type == "view") {
                            delete buttons["重設"]
                            delete buttons["儲存"]
                            elements.forEach(e => { e.disabled = true })
                        }
                        openDialog('編輯業務職掌', buttons)
                    }

                    function showSectionBusiness(sectionBusiness) {
                        elements.forEach(e => {
                            if (e.tagName == 'INPUT' || e.tagName == 'TEXTAREA') e.value = sectionBusiness[e.id]
                            if (e.tagName == 'SPAN') e.innerHTML = sectionBusiness[e.id]
                        })
                        document.getElementById('unitName').innerHTML = sectionData.unitName
                        document.getElementById('unitId').innerHTML = sectionData.unitId
                        document.getElementById('sectionName').innerHTML = sectionData.sectionName
                        document.getElementById('sectionId').innerHTML = sectionData.sectionId

                    }

                    function deleteClick(data) {
                        if (confirm('是否刪除資料')) {
                            var _data = data.data
                            var method = 'delete'
                            var url = ctx + '/SectionBusiness/' + _data.sId
                            var response = callAjax(method, url)
                            if (response) {
                                alert('刪除完成')
                                location.reload()
                            }
                        }

                    }
                    function resetClick() {
                        elements.forEach(e => {
                            e.disabled = false
                            if (e.tagName != 'SPAN') {
                                e.value = ''
                            }
                        })
                    }

                    function callAjax(method, url, requestData) {
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
                    }

                    function openDialog(title, buttons) {
                        $("#editTab").dialog({
                            title: title,
                            modal: true,
                            height: 700,
                            width: 800,
                            buttons: buttons
                        });
                    }

                    function closeDialog() {
                        $('#editTab').dialog('close')
                    }




                    function createData(jsonData) {
                        app.records = jsonData;
                    }

                    window.onload = function () {

                        add = document.querySelector("#add")
                        elements = document.querySelectorAll('[id]:not(button,div)')
                        add.onclick = addClick
                        loadPageData(userData.unitId, userData.sectionId)
                    }
                </script>
            </t:menu>
        </t:layout>