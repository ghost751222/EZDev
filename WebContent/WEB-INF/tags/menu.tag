<%@tag description="Menu Tag" pageEncoding="UTF-8" %>
    <link href="${pageContext.request.contextPath}/resources/css/tree.css?${time}" rel="stylesheet" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/tree.js?${time}"></script>


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
            height: 50px;
            width: 50px;
            display: block;
            background: rgba(255, 255, 255, .0);
            z-index: 1;
        }
    </style>
    <script type="text/javascript">

        var ctx = "${pageContext.request.contextPath}"
        var unit1996 = "1996"
        var unit1996Data = { parent: { name: unit1996 }, name: unit1996, superUnit: 'ZZZ', unitCode: 'ZZZ' }
        findTreeData();
        function findTreeData() {
            //var _treeData = localStorage.getItem('treeData')
            //if (_treeData) {
            //  window.treeData = JSON.parse(_treeData)
            // return
            // }

            $.ajax({
                method: 'get',
                url: ctx + '/Units/findAll',
                contentType: 'application/json',
                async: false,
                success: function (res) {
                    var treeData = {}
                    var _treeData = {}
                    var key = "總窗口管理"
                    var isExist = false

                    if (authorization != null) {

                        if (location.href.includes("questionAnswer")) {
                            treeData[unit1996] = [unit1996Data]
                        }


                        res.forEach(r => {
                            r["name"] = r["unitName"]
                            if ((r.superUnit == null || r.superUnit == '') && r.unitType == 'M' && (r.policeOfficeLevel == "1.0" || r.policeOfficeLevel == "1")) {
                                treeData[r["unitName"]] = res.filter(rr => r.unitCode == rr.superUnit).map((rr) => { rr["parent"] = r; return rr })
                            }
                            if (location.href.includes("sectionData")) {


                                if (!treeData[key]) treeData[key] = []
                                if ( (r.superUnit == null || r.superUnit == '') && r.unitType == 'M' && (r.policeOfficeLevel == "1.0" || r.policeOfficeLevel == "1")) {
                                    if (r.unitName == "地政機關") {
                                        treeData[key].push({ superUnit: null, unitCode: "E2001", unitName: "土地重劃工程處",name:"土地重劃工程處", spUnitCode: "E2001" })
                                        treeData[key].push({ superUnit: null, unitCode: "E2014", unitName: "國土測繪中心",name:"國土測繪中心", spUnitCode: "E2014" })
                                    } else {
                                        treeData[key].push(r)
                                    }

                                }


                            }
                        })

                        if (authorization.role == 'C') {
                            for (var x in treeData) {
								if(x==key) continue;
                                for (var y in treeData[x]) {
                                    if (treeData[x][y].unitCode == authorization.unitCode) {
                                        isExist = true
                                        _treeData[x] = []
                                        _treeData[x].push(JSON.parse(JSON.stringify(treeData[x][y])))
                                        break;
                                    }
                                    if (isExist) break;
                                }
                            }
                            treeData = _treeData

                        } else if (authorization.role == 'E') {
						    var authorization_unitCode = authorization.unitCode
                            for (var x in treeData) {
                                for (var y in treeData[x]) {

                                    // if (authorization_unitCode == 'E2001' || authorization_unitCode == 'E2014' || authorization_unitCode == 'E2015') {

                                    //     if(authorization_unitCode =='E2015') authorization_unitCode ="E2001"
                                    //     if (treeData[x][y].spUnitCode == authorization_unitCode) {
                                    //          if(!_treeData[x]) _treeData[x]= []
                                    //         _treeData[x].push(JSON.parse(JSON.stringify(treeData[x][y])))
                                    //     }


                                    // } else {

                                        if (treeData[x][y].unitCode == authorization.unitCode) {
                                            _treeData[x] = []
                                            _treeData[x] = JSON.parse(JSON.stringify(treeData[x]))
                                            break;
                                        }

                                        if (treeData[x][y].unitCode == authorization.superUnit) {
                                            _treeData[x] = []
                                            _treeData[x].push(JSON.parse(JSON.stringify(treeData[x][y])))
                                            break
                                        }
                                    //}


                                }
                            }
                            treeData = _treeData
                        }




                        if (treeData[key]) {
                            var cloneData = JSON.stringify(treeData[key])
                            delete treeData[key]
                            treeData[key] = JSON.parse(cloneData)
                        }



                    }

                    window.treeData = treeData
                    //localStorage.setItem('treeData', JSON.stringify(treeData))
                }, error: function (err) {
                    console.log(err)
                }
            });
        }




        function treeClick(e, d) {
            if (d.name == unit1996) {
                d = d.children[0]
                d.type = null
            }
            if (!d.type) {
                call(e, d);
                //$('#iframe')[0].contentWindow.call(e, d);
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
        $(document).ready(function () {

            var treeJson = []
            tree = new Tree(document.getElementById('tree'), { navigate: true });
            tree.on('click', treeClick);
            Object.entries(window.treeData).forEach(([key, val]) => {
                if (key == unit1996) {
                    treeJson.push({ 'name': key, children: val, type: Tree.FILE })
                } else {
                    treeJson.push({ 'name': key, children: val, type: Tree.FOLDER })
                }

            })
            tree.json(treeJson);
            $('.openTree').click(openTreeClick)
            $('.closeTree').click(closeTreeClick)
            $('#drawer').click(drawerClick)


        })

    </script>

    <div class="container-fluid h-100">
        <div class="row flex-nowrap h-100 overflow-auto">
            <div class="col-auto px-0">
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

                            <span style="color:#237bd3;font-size:13px;cursor:pointer" class="openTree">全部展開</span>
                            <span>|</span>
                            <span style="color:#237bd3;font-size:13px;cursor:pointer" class="closeTree">全部關閉</span>
                        </div>
                        <br />
                        <div id="tree" class=""></div>
                    </div>


                </div>
            </div>
            <main class="col ps-md-2 pt-2">
                <label for="drawer-toggle" id="drawer-toggle-label" data-bs-target="#sidebar"
                    data-bs-toggle="collapse"></label>
                <jsp:doBody />
            </main>
        </div>