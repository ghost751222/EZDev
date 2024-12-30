<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout pageTitle="FAQ 展期作業">

        <link href="${pageContext.request.contextPath}/resources/css/faq.css?${time}" rel="stylesheet" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/faq.js?${time}"></script>





<ul class="nav nav-tabs" role="tablist">
        <li class="nav-item">
            <button class="nav-link active"  data-bs-toggle="tab" data-bs-target="#expiration" type="button"
                role="tab" aria-controls="home" aria-selected="true">即將到期(30天)</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link"  data-bs-toggle="tab" data-bs-target="#overtime" type="button"
                role="tab" aria-controls="profile" aria-selected="false">已過期</button>
        </li>

    </ul>
    <div class="tab-content">
        <div class="tab-pane fade show active" id="expiration" role="tabpanel" aria-labelledby="home-tab">
            <div class="container">
                <div class="row">
                    <div class="form-group col-md-2">
                        <label for="startTime2">起始時間</label>
                        <input type="date" id="startTime2" class="form-control" />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="endTime2">結束時間</label>
                        <input type="date" id="endTime2" class="form-control" />
                    </div>


                    <div class="form-group col-md-2">
                        <label>&nbsp;</label>
                        <button class="btn btn-primary form-control" id="query2">查詢</button>
                    </div>

                </div>
            </div>

            <br /><br /><br />
            <div id="contentTab2">
                <div style="display: flex;">
                    <div id="tree" class=""></div>
                    <div style="width:100%">
                        <div class="main-title">即將到期-FAQ列表</div>
                        <hr />
                        <div class="pagination ui basic segment grid">
                            <vuetable-pagination ref="pagination3" @vuetable-pagination:change-page="onChangePage">
                            </vuetable-pagination>
                        </div>
                        <div id="dataTable">
                            <table class="main-table" width="100%"
                                style="border-spacing:10px;border-collapse: separate;">
                                <thead>
                                    <tr bgcolor="#f5f5dc">
                                        <th>各科室提供常見問答</th>
                                        <th>1996口語話術說明</th>
                                    </tr>
                                </thead>
                                <tbody id="tb2"></tbody>
                            </table>
                        </div>

                        <div class="pagination ui basic segment grid">
                            <vuetable-pagination ref="pagination4" @vuetable-pagination:change-page="onChangePage">
                            </vuetable-pagination>
                        </div>

                    </div>
                </div>
            </div>



        </div>
        <div class="tab-pane fade" id="overtime" role="tabpanel" aria-labelledby="profile-tab">

            <div class="container">
                <div class="row">
                    <div class="form-group col-md-2">
                        <label for="startTime">起始時間</label>
                        <input type="date" id="startTime" class="form-control" />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="endTime">結束時間</label>
                        <input type="date" id="endTime" class="form-control" />
                    </div>


                    <div class="form-group col-md-2">
                        <label>&nbsp;</label>
                        <button class="btn btn-primary form-control" id="query">查詢</button>
                    </div>

                </div>
            </div>


            <br /><br /><br />
            <div id="contentTab">
                <div style="display: flex;">
                    <div id="tree" class=""></div>
                    <div style="width:100%">
                        <div class="main-title">已過期-FAQ列表</div>
                        <hr />
                        <div class="pagination ui basic segment grid">
                            <vuetable-pagination ref="pagination2" @vuetable-pagination:change-page="onChangePage">
                            </vuetable-pagination>
                        </div>
                        <div id="dataTable">
                            <table class="main-table" width="100%"
                                style="border-spacing:10px;border-collapse: separate;">
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
            </div>

        </div>

    </div>




 <script type="text/javascript">

        var userData = { unitId: 'A1000', sectionId: 'A1001', sectionName: null, authorization: 1 }


        var add, suggestSave, suggestBack, elements, $dialog
        $(document).ready(function () {
            $("#query").click(function () {
                findOverTime()
            })

            $("#query2").click(function () {
                findExpiration()
            })
        })


        window.onload = function () {

            elements = document.querySelectorAll("#editTab input[id],select[id],textarea[id]")
            setUserData('A1000', 'A1001')
            setQuestionAnswer(userData.unitId, userData.sectionId)

            var dt = new Date();
            var year = dt.getFullYear();
            var month = (dt.getMonth() + 1).toString().padStart(2, '0')
            var date = dt.getDate().toString().padStart(2, '0')

            var startTime = `\${year}-\${month}-01`
            var endTime = `\${year}-\${month}-\${date}`

            $("#startTime").val(startTime)
            $("#endTime").val(endTime)
            $("#startTime2").val(startTime)
            $("#endTime2").val(endTime)
        }

        function setQuestionAnswer(unitId, sectionId) {
            QuestionAnswer.unitId = unitId
            QuestionAnswer.sectionId = sectionId
        }

        function setUserData(unitId, sectionId) {
            userData.unitId = unitId
            userData.sectionId = sectionId
            userData.sectionName = getSectionName(userData.sectionId)
            userData.page = "questionAnswerVerify"

        }
        function findExpiration() {


            var startTime = $("#startTime2").val()
            var endTime = $("#endTime2").val()

            var method = "get";
            var url = ctx + "/QuestionAnswer/findExpiration?";
            let params = new URLSearchParams();
            params.append("startTime", startTime);
            params.append("endTime", endTime);

            url = url + params.toString();
            var res = callAjax(method, url);
            if (res){

                   expiration.records = res;
            }

        }
        function findOverTime() {


            var startTime = $("#startTime").val()
            var endTime = $("#endTime").val()

            var method = "get";
            var url = ctx + "/QuestionAnswer/findOverTime?";
            let params = new URLSearchParams();
            params.append("startTime", startTime);
            params.append("endTime", endTime);

            url = url + params.toString();
            var res = callAjax(method, url);
            if (res) {

                overtime.records = res;
            }
        }


        var overtime = new Vue({
            el: '#contentTab',
            data: {
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
                    this.$refs.pagination2.setPaginationData(paginationData)
                },
                onChangePage(page) {
                    this.pagination.current_page = page
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
                    jsonData.forEach(r => {
                        r["page"] = userData.page
                        r["questionAnswerRelations"].forEach(rr => {
                            rr["page"] = userData.page
                        })
                    })

                    $('#tb').html('')
                    createData(jsonData)
                }
            }
        })


        var expiration = new Vue({
            el: '#contentTab2',
            data: {
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

                    this.$refs.pagination3.setPaginationData(paginationData)
                    this.$refs.pagination4.setPaginationData(paginationData)
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
                    let local = this.records;
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
                        r["questionAnswerRelations"].forEach(rr => {
                            rr["page"] = userData.page
                        })
                    })

                    $('#tb2').html('').append($("#tmplItem").tmpl(jsonData))
                }
            }
        })
    </script>







</t:layout>