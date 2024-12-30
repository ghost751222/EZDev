<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <html>

    <head>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>

        <link href="../resources/css/tree.css?2" rel="stylesheet">
        <script src="../resources/js/tree.js?2"></script>
        <title>Test</title>
        <style>
        #drawer-toggle-label:before {
            content: '';
            display: block;
            position: absolute;
            height: 2px;
            width: 24px;
            background: #8d8d8d;

            box-shadow: 0 6px 0 #8d8d8d, 0 12px 0 #8d8d8d;
        }

        #drawer-toggle-label {

            user-select: none;
            left: 0px;
            height:50px;
            width: 50px;
            display: block;
            background: rgba(255,255,255,.0);
            z-index: 1;
        }
        </style>
        <script>
            var ctx = "${pageContext.request.contextPath}"
            findTreeData();
            function findTreeData() {
                var _treeData = localStorage.getItem('treeData')
                if (_treeData) {
                    window.treeData = JSON.parse(_treeData)
                    return
                }

                $.ajax({
                    method: 'get',
                    url: ctx + '/Units/findAll',
                    contentType: 'application/json',
                    async: false,
                    success: function (res) {
                        var treeData = {}
                        res.forEach(r => {
                            r["name"] = r["unitName"]
                            if (r.superUnit == null) {
                                treeData[r["unitName"]] = res.filter(rr => r.unitCode == rr.superUnit).map((rr) => { rr["parent"] = r; return rr })
                            }
                        })
                        window.treeData = treeData
                        localStorage.setItem('treeData', JSON.stringify(treeData))
                    }, error: function (err) {
                        console.log(err)
                    }
                });
            }




            function treeClick(e, d) {
                if (!d.type) {
                    $('#iframe')[0].contentWindow.call(e, d);
                }
            }
            function openTreeClick(e) {
                $('details:not(:first)').each((index, d) => {
                    $(d).attr('open', 'open')
                })
            }

            function closeTreeClick(e) {
                $('details:not(:first)').each((index, d) => {
                    $(d).removeAttr('open')
                })
            }

            function drawerClick(e) {

                var $menu = $('#menu')
                if ($menu.is(":visible")) {
                    $menu.hide()
                } else {
                    $menu.show()
                }

            }
            window.onload = () => {
                $('button').click(function () {
                    var src = location.origin + $(this).parent().attr('data-src')
                    var params = new URLSearchParams(document.location.search);
                    let userId = params.get("userId");
                    // src ='/EZDev/SectionData/page'
                    $('#iframe').attr('src', src + "?userId=" + userId)
                })

                var treeJson = []
                tree = new Tree(document.getElementById('tree'), { navigate: true });
                tree.on('click', treeClick);
                Object.entries(window.treeData).forEach(([key, val]) => {
                    treeJson.push({ 'name': key, children: val, type: Tree.FOLDER })
                })
                tree.json(treeJson);
                $('#openTree').click(openTreeClick)
                $('#closeTree').click(closeTreeClick)
                $('#drawer').click(drawerClick)
                $('#sectionData').click()

            }

        </script>
    </head>

    <body>
     ${authorization}
        <div class="container-fluid h-100">
            <div class="row flex-nowrap h-100">
                <div class="col-auto px-0 d-none">
                    <div id="sidebar" class="collapse collapse-horizontal show border-end">
                        <div id="sidebar-nav" class="list-group border-0 rounded-0 text-sm-start min-vh-100">

                            <div
                                style="background-image: linear-gradient(#d8e6f6, #6192d3);color:#14418a;font-size:20px;font-weight:bold;width:100%">
                                科室列表
                                <!-- <span style="float:right;font=size:10px;cursor:pointer" id="drawer2">&#10235;</span>
                                <svg width="10%" height="10%" version="1.1" viewBox="0 0 20 20" x="0px" y="0px" class="ScIconSVG-sc-1bgeryd-1 ifdSJl"><g><path d="M16 16V4h2v12h-2zM6 9l2.501-2.5-1.5-1.5-5 5 5 5 1.5-1.5-2.5-2.5h8V9H6z"></path></g></svg>
                                             <svg width="10%" height="10%" version="1.1" viewBox="0 0 20 20" x="0px" y="0px" class="ScIconSVG-sc-1bgeryd-1 ifdSJl"><g><path d="M4 16V4H2v12h2zM13 15l-1.5-1.5L14 11H6V9h8l-2.5-2.5L13 5l5 5-5 5z"></path></g></svg>
                                             -->
                            </div>
                            <div>

                                <span style="color:#237bd3;font-size:13px;cursor:pointer" id="openTree">全部展開</span>
                                |
                                <span style="color:#237bd3;font-size:13px;cursor:pointer" id="closeTree">全部關閉</span>
                            </div>
                            <br />
                            <div id="tree" class=""></div>
                        </div>


                    </div>
                </div>
                <main class="col ps-md-2 pt-2">

                   <label for="drawer-toggle" id="drawer-toggle-label" data-bs-target="#sidebar" data-bs-toggle="collapse" ></label>

                    <div class="row h-100">
                        <div class="col-12">
                            <ul class="nav nav-tabs nav-pills" id="pills-tab" role="tablist">


                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                                        aria-expanded="false">科室</a>
                                    <ul class="dropdown-menu">
                                        <li data-src="/EZDev/SectionData/page/sectionData">
                                            <button class="nav-link" id="sectionData"
                                                data-bs-toggle="pill">科室資料維護</button>
                                        </li>
                                        <li data-src="/EZDev/SectionData/page/SectionDataView">
                                            <button class="nav-link" id="sectionData"
                                                data-bs-toggle="pill">科室資料查詢</button>
                                        </li>
                                    </ul>
                                </li>


                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                                        aria-expanded="false">業務職掌</a>
                                    <ul class="dropdown-menu">
                                        <li class="nav-item" data-src="/EZDev/SectionBusiness/page/sectionBusiness">
                                            <button class="nav-link" id="sectionBusiness"
                                                data-bs-toggle="pill">業務職掌維護</button>
                                        </li>

                                        <li class="nav-item" data-src="/EZDev/SectionBusiness/page/SectionBusinessView">
                                            <button class="nav-link" id="sectionBusiness"
                                                data-bs-toggle="pill">業務職掌查詢</button>
                                        </li>
                                    </ul>
                                </li>


                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                                        aria-expanded="false">FAQ</a>
                                    <ul class="dropdown-menu">
                                        <li class="nav-item" data-src="/EZDev/QuestionAnswer/questionAnswer">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 編輯
                                            </button>
                                        </li>

                                        <li class="nav-item" data-src="/EZDev/QuestionAnswer/FAQView">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 查詢
                                            </button>
                                        </li>

                                        <li class="nav-item" data-src="/EZDev/QuestionAnswer/questionAnswerVerify">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 下架審核
                                            </button>
                                        </li>

                                        <li class="nav-item"
                                            data-src="/EZDev/QuestionAnswer/questionAnswerOpinionVerify">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 建議審核
                                            </button>
                                        </li>



                                        <li class="nav-item" data-src="/EZDev/QuestionAnswer/questionAnswerDeadline">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 展期作業
                                            </button>
                                        </li>

                                         <li class="nav-item" data-src="/EZDev/QuestionAnswer/questionAnswerDeleteList">
                                                                                    <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                                                        aria-selected="false">
                                                                                        FAQ下架刪除
                                                                                    </button>
                                                                                </li>

 <li class="nav-item"
                                            data-src="/EZDev/QuestionAnswer/questionAnswerEdit">
                                            <button class="nav-link" id="questionAnswer" data-bs-toggle="pill"
                                                aria-selected="false">
                                                FAQ 修改列表
                                            </button>
                                        </li>

                                    </ul>
                                </li>



                                <li class="nav-item" data-src="/EZDev/Units/page">
                                    <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                        aria-selected="false">科室基本資料</button>
                                </li>


                                <li class="nav-item" data-src="/EZDev/Announce/Announce">
                                    <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                        aria-selected="false">公告訊息維護</button>
                                </li>
                                <li class="nav-item" data-src="/EZDev/Announce/AnnounceMarquee">
                                    <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                        aria-selected="false">公告訊息展示</button>
                                </li>


                                 <li class="nav-item" data-src="/EZDev/Excel/Excel">
                                    <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                        aria-selected="false">報表</button>
                                </li>

                                <li class="nav-item dropdown d-none">
                                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button"
                                        aria-expanded="false">報表</a>
                                    <ul class="dropdown-menu">
                                        <li data-src="/EZDev/Excel/SectionDataReport">
                                            <button class="nav-link" id="sectionData"
                                                data-bs-toggle="pill">聯絡窗口清單</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/SectionSuperManagerReport">
                                            <button class="nav-link" id="sectionData"
                                                data-bs-toggle="pill">總連絡框口清單</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/FAQVerifyList">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">FAQ 對照列表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/FAQStatistic">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">FAQ統計表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/FAQSectionStatistic">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">FAQ科室統計表</button>
                                        </li>




                                        <li data-src="/EZDev/Excel/CallFlowDetailReport">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">派案明細</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/CallAmountCompareAll">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">服務概況表</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/CallAmountType">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">業務類型分析</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/ProcessTypeReport">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">業務服務類別統計表</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/LineService">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">Line互動客服統計表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/IncomeSec">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">1996進線量及秒數</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/WaitSec">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">1996等待秒數列表</button>
                                        </li>
                                        <li data-src="/EZDev/Excel/SatisfactionStatistics">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">滿意度調查統計表</button>
                                        </li>
                                        <li data-src="/EZDev/Excel/SatisfactionList">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">滿意度調查清單</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/SectionForecastSampleNumber">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">業務職掌預估抽測通數表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/RISReport">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">轉接統計表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/CaseResponseSubList">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">案件處理狀態清單</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/CaseProcTimeAvgSub">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">案件處理時間統計</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/ContentInfoForm">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">系統更正新增確認通知單報表</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/TalkFlow">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">系統更正 新增 分時話務服務水準表</button>
                                        </li>

                                        <li data-src="/EZDev/Excel/AgentEfficient">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">話務績效統計表</button>
                                        </li>


                                        <li data-src="/EZDev/Excel/BlackList">
                                            <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                                aria-selected="false">來電號碼比對過濾統計表</button>
                                        </li>


                                    </ul>
                                </li>


                            </ul>



                            <div class="tab-content h-100">
                                <div class="tab-pane active show  h-100">
                                    <iframe id="iframe" width="100%" height="100%"></iframe>
                                </div>
                            </div>

                        </div>
                    </div>
                </main>
            </div>
        </div>



    </body>

    </html>