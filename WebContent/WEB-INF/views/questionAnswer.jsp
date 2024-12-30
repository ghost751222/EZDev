<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

        <t:layout pageTitle="FAQ管理">
            <t:menu>

                <link href="${pageContext.request.contextPath}/resources/css/faq.css?${time}" rel="stylesheet" />
                <script type="text/javascript"
                    src="${pageContext.request.contextPath}/resources/js/faq.js?${time}"></script>

                <div id="contentTab">
                    <div id="page" class="d-none">
                        <button class="main-button" @click="openPage(1)">下架審核</button>
                        <button class="main-button" @click="openPage(2)">建議審核</button>
                        <button class="main-button" @click="openPage(3)">展期列表</button>
                        <button class="main-button" @click="openPage(4)">修改列表</button>
                    </div>

                    <div style="display: flex;">

                        <div style="width:100%">
                            <div class="main-title">{{sectionName}}-FAQ列表</div>
                            <div style="display: flex; flex-direction: row">
                                <div style="flex: 50%;">
                                    <button class="main-button" @click="queryDialog">查詢</button>
                                </div>
                                <div style="display:flex; flex-direction: row-reverse;flex: 50%;">
                                    <button class="main-button" id="add">新增FAQ</button>
                                    <button class="main-button" @click="exportExcel">匯出資料</button>
                                    <button class="main-button" @click="openPage(5)">下架刪除列表</button>
                                </div>
                            </div>

                            <hr />

                            <div class="pagination ui basic segment grid">
                                <vuetable-pagination ref="pagination2" @vuetable-pagination:change-page="onChangePage">
                                </vuetable-pagination>
                            </div>
                            <div id="dataTable">
                                <table class="main-table" width="100%"
                                    style="border-spacing:10px;border-collapse: separate;table-layout:fixed;word-break:break-all;">
                                    <thead>
                                        <tr bgcolor="#f5f5dc">
                                            <th>各科室提供常見問答</th>
                                            <th>1996口語話術說明</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tb"></tbody>
                                </table>
                            </div>
                            <div class="pagination ui basic segment grid">
                                <vuetable-pagination ref="pagination" @vuetable-pagination:change-page="onChangePage">
                                </vuetable-pagination>
                            </div>
                        </div>
                    </div>
                    <div id="queryTab" style="display:none;background-color:#ccdae7;">
                        <table style="width:100%">
                            <thead>
                                <tr style="background-color:#f5f5f5;">
                                    <th>
                                        <input type="checkbox" id="sId" v-model="sId">
                                        <label for="sId">查詢編號</label><br>
                                        <input type="checkbox" id="and" v-model="and">
                                        <label for="and">邏輯查詢-AND(且，條件皆要符合R)</label><br>
                                        <input type="checkbox" id="or" v-model="or">
                                        <label for="or">邏輯查詢-OR(或，其中之一符合)</label><br>
                                    </th>
                                </tr>
                                <tr>
                                    <td></td>
                                </tr>
                                <tr style="background-color:#f5f5f5;">
                                    <th>
                                        <button id="queryConditionAddBtn" style="color:blue;">&#x2b;</button>新增文字條件
                                    </th>
                                </tr>
                                </<thead>
                            <tbody>
                                <tr style="background-color:#dedfee;text-align:center">
                                    <td>文字條件</td>
                                </tr>

                                <tr>
                                    <table style="width:100%">
                                        <tbody id="queryBody">
                                        </tbody>
                                    </table>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <script type="text/javascript">

                    var userData = { unitId: '', sectionId: '', sectionName: null, role: authorization.role, page: 'questionAnswer' }
                    var add, elements, $dialog

                    window.onload = function () {
                        add = document.querySelector("#add")
                        elements = document.querySelectorAll('input[id],select[id]:not(#qaEditHistorySel),textarea[id]')
                        add.onclick = addClick
                        setUserData(authorization.superUnit, authorization.unitCode)
                        setQuestionAnswer(userData.unitId, userData.sectionId)
                        app.records = findByUnitIdAndSectionId(userData.unitId, userData.sectionId)

                        $('#queryConditionAddBtn').click(function () {
                            $("#queryBody").append("<tr><td><input type=\"text\" style=\"width:100%\"></td></tr>")
                        })
                        if (authorization.role == 'D' || authorization.role == 'B') {
                            $("#page").removeClass("d-none")
                        }

                    }

                    function setQuestionAnswer(unitId, sectionId) {
                        QuestionAnswer.unitId = unitId
                        QuestionAnswer.sectionId = sectionId
                    }

                    function setUserData(unitId, sectionId) {
                        userData.unitId = unitId
                        userData.sectionId = sectionId
                        userData.sectionName = getSectionName(userData.sectionId)
                        app.sectionName = userData.sectionName

                    }
                    function call(e, d) {
                        try {
                            $('#tb').html('')
                            $dialog.dialog('close')
                        } catch (e) {
                        }
                        setUserData(d.superUnit, d.unitCode)
                        setQuestionAnswer(d.superUnit, d.unitCode)
                        app.pagination.current_page=1
                        app.records = findByUnitIdAndSectionId(d.superUnit, d.unitCode)

                    }

                    var app = new Vue({
                        el: '#contentTab',
                        data: {

                            sId: false,
                            and: false,
                            or: false,
                            condition: { sId: [], and: [], or: [] },
                            sectionName: '',
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
                            queryDialog() {
                                $("#queryTab").dialog({
                                    title: "FAQ查詢",
                                    width: 500,
                                    height: 400,
                                    buttons: {
                                        "查詢": function () {
                                            var condition = { sId: [], and: [], or: [] };
                                            $('#queryBody input').each(function () {
                                                if (app.sId && this.value)
                                                    condition.sId.push(this.value)

                                                if (app.and && this.value)
                                                    condition.and.push(this.value)
                                                if (app.or && this.value)
                                                    condition.or.push(this.value)

                                            })

                                            app.condition = condition

                                            app.dataTableFresh()
                                        },
                                        "關閉視窗": closeDialog
                                    }
                                })
                            },
                            query(){
                                 this.records = findByUnitIdAndSectionId(userData.unitId, userData.sectionId)
                            },
                            openPage(value) {
                                var searchParams = new URLSearchParams(location.search);
                                var params = "?userId=" + searchParams.get("userId")
                                var url = ctx
                                switch (value) {
                                    case 1:
                                        url += "/QuestionAnswer/questionAnswerVerify"
                                        break;
                                    case 2:
                                        url += "/QuestionAnswer/questionAnswerOpinionVerify"
                                        break;
                                    case 3:
                                        url += "/QuestionAnswer/questionAnswerDeadline"
                                        break;
                                    case 4:
                                        url += "/QuestionAnswer/questionAnswerEdit"
                                        break;
                                    case 5:
                                        url += "/QuestionAnswer/questionAnswerDeleteList"
                                        break;
                                }
                                url += params
                                window.open(url)
                            },
                            exportExcel() {
                                var json = { sectionId: userData.sectionId,flag:false }
                                var params = new URLSearchParams("?params=" + JSON.stringify(json));
                                var url = ctx + '/Excel/FAQDataList/FAQDataList?'
                                var aLink = document.createElement('a');
                                aLink.href = url + params.toString();
                                aLink.target = "_blank";
                                aLink.click();
                            },
                            dataTableFresh() {
                               if(this.pagination.current_page ==1){
                                  this.pagination.from = 1
                                  this.pagination.to = 0
                                  this.pagination.total = 0
                                  this.pagination.current_page = 1
                                  this.pagination.last_page = 0
                               }
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
                                this.$refs.pagination2.setPaginationData(paginationData)
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
                            }, dataManager(sortOrder, pagination) {


                                if (this.records.length < 1) return this.records;
                                let local = this.records.concat();

                                this.condition.sId.forEach(s => {
                                    local = local.filter(l => l.sId == s)
                                })


                                this.condition.and.forEach(s => {
                                    local = local.filter(l => l.question.includes(s) || l.answer.includes(s))
                                })

                                if (this.condition.or.length > 0) {
                                    var _local = []
                                    var or = this.condition.or
                                    local.forEach((l, index) => {

                                        for (var i in or) {
                                            if (l.question.includes(or[i]) || l.answer.includes(or[i])) {
                                                _local.push(l)
                                                break
                                            }
                                        }
                                    })
                                    local = _local
                                }

                                pagination = this.makePagination(
                                    local.length,
                                    this.perPage
                                );

                                let from = pagination.from - 1;
                                let to = from + this.perPage;

                                this.onPaginationData(pagination)
                                var jsonData = local.slice(from, to)
                                jsonData.forEach(r => {
                                    r["page"] = userData.page
                                    r["role"] = userData.role
                                    r["questionAnswerRelations"].forEach(rr => {
                                        rr["page"] = userData.page
                                        rr["role"] = userData.role
                                    })
                                })

                                $('#tb').html('')
                                createData(jsonData)
                            }
                        }
                    })
                </script>
            </t:menu>
        </t:layout>