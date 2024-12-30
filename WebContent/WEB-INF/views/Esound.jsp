<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>

    <head>
        <title>報表</title>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
       <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" />

       <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js"></script>

            <style>
              li
              {
                 white-space: nowrap;
              }
            </style>

    </head>

    <body>
        <div class="col-md-12" style="height:100%">


            <ul class="nav nav-tabs nav-pills" id="pills-tab" role="tablist">

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">案件分析表</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">


                        <li data-src="/Excel/Esound/CallAmountCompareAll">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">服務概況表</button>
                        </li>


                        <li data-src="/Excel/Esound/CallAmountType">
                            <button class="nav-link" id="CallAmountType" data-bs-toggle="pill"
                                aria-selected="false">業務類型分析</button>
                        </li>


                        <li data-src="/Excel/Esound/CallFlowDetailReport">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">派案明細表</button>
                        </li>

                        <li data-src="/Excel/Esound/ProcessTypeReport">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">業務服務類別統計表</button>
                        </li>

                        <li data-src="/Excel/Esound/LineService">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">Line互動客服統計表</button>
                        </li>
                    </ul>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">轄下案件分析表</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">
                        <li data-src="/Excel/Esound/CaseResponseSubList">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">案件處理狀態清單</button>
                        </li>

                        <li data-src="/Excel/Esound/CaseProcTimeAvgSub">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">案件處理時間統計</button>
                        </li>

                        <li data-src="/Excel/Esound/ContentInfoForm">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">系統更正/新增/確認通知單報表</button>
                        </li>

                    </ul>
                </li>


                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">話務管理報表</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">

                        <li data-src="/Excel/Esound/TalkFlow">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">分時話務服務水準表</button>
                        </li>

                        <li data-src="/Excel/Esound/AgentEfficient">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">話務績效統計表</button>
                        </li>


                        <li data-src="/Excel/Esound/BlackList">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">來電號碼比對過濾統計表</button>
                        </li>

                        <li data-src="/Excel/Esound/IncomeSec">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">1996進線量及秒數</button>
                        </li>


                        <li data-src="/Excel/Esound/WaitSec">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">1996等待秒數列表</button>
                        </li>


                        <li data-src="/Excel/Esound/RISReport">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">轉接統計表</button>
                        </li>

                    </ul>
                </li>


                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">FAQ統計表</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">
                        <li data-src="/Excel/Esound/FAQStatistic">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">FAQ統計表</button>
                        </li>


                        <li data-src="/Excel/Esound/FAQSectionStatistic">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">FAQ科室統計表</button>
                        </li>

                    </ul>
                </li>


                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">FAQ清單</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">

                        <li data-src="/Excel/Esound/FAQDataList">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">FAQ 對照列表</button>
                        </li>


                    </ul>
                </li>



                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">窗口清單</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">

                        <li data-src="/Excel/Esound/SectionSuperManagerReport">
                            <button class="nav-link" id="sectionData" data-bs-toggle="pill">總連絡窗口清單</button>
                        </li>

                        <li data-src="/Excel/Esound/SectionDataReport">
                            <button class="nav-link" id="sectionData" data-bs-toggle="pill">連絡窗口清單</button>
                        </li>


                    </ul>
                </li>


                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">滿意度調查分析</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">

                        <li data-src="/Excel/Esound/SatisfactionStatistics">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">滿意度調查統計表</button>
                        </li>
                        <li data-src="/Excel/Esound/SatisfactionList">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">滿意度調查清單</button>
                        </li>


                    </ul>
                </li>


                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                        aria-expanded="true">評核作業報表</a>
                    <ul class="dropdown-menu"
                        style="position: absolute; inset: 0px auto auto 0px; margin: 0px; transform: translate(0px, 42px);"
                        data-popper-placement="bottom-start">

                        <li data-src="/Excel/Esound/SectionForecastSampleNumber">
                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                aria-selected="false">業務職掌預估抽測通數表</button>
                        </li>


                    </ul>
                </li>


            </ul>

            <br /><br />
            <div class="tab-content h-100">
                <div class="tab-pane active show">
                    <iframe id="iframe" width="100%" height="100%" src="" scrolling="no"></iframe>
                </div>
            </div>
        </div>

    </body>
    <script>
        window.onload = () => {
            $('button').click(function () {
                var contextPath = "${pageContext.request.contextPath}"
                var src = location.origin + contextPath + $(this).parent().attr('data-src')
                var searchParams = new URLSearchParams(location.search);
                src += "?userId=" + searchParams.get("userId")
                $('#iframe').attr('src', src)
            })

            $('#CallAmountType').trigger('click')

        }

    </script>

    </html>