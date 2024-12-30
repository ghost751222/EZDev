<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
        <t:layout pageTitle="科室資料維護">
            <t:menu>
                <style>
                    td {
                        height: 35px
                    }
                </style>

                <script type="text/javascript">
                    var reset, save, elements, sectionArray = [], units = []
                    var ctx = "${pageContext.request.contextPath}"
                    var userData = { unitId: 'A1000', sectionId: 'A1001',sectionName:null }
                    userData.unitId  = authorization.superUnit
                    userData.sectionId  = authorization.unitCode
                    var reqData = {
                        sId: null,
                        unitId: null,
                        sectionId: null,
                        occupation1: null,
                        connector1: null,
                        tel1: null,
                        email1: null,
                        occupation2: null,
                        connector2: null,
                        tel2: null,
                        email2: null,
                        creator: null,
                        createTime: null,
                        lastModifier: null,
                        lastUpdateTime: null,
                        extension1: null,
                        extension2: null,
                        needAudit: null,
                    }

                    var sectionSuperManager = {
                        sId: null,
                        unitId: null,
                        superOccupation: null,
                        superConnector: null,
                        superTel: null,
                        superEmail: null,
                        creator: null,
                        createTime: null,
                        lastModifier: null,
                        lastUpdateTime: null,
                        superExtension: null,
                        superOccupation2: null,
                        superConnector2: null,
                        superTel2: null,
                        superEmail2: null,
                        superExtension2: null,
                    }


                    window.onload = function () {

                        reset = document.querySelector("#reset")
                        save = document.querySelector("#save")
                        elements = document.querySelectorAll('[id]:not(button,div)')
                        reset.onclick = resetClick
                        save.onclick = saveClick
                        var sectionData = findCurrentSectionData(userData.unitId, userData.sectionId)
                        if (sectionData == null) {
                            sectionData = JSON.parse(JSON.stringify(reqData));
                            sectionData.unitId = userData.unitId
                            sectionData.sectionId = userData.sectionId
                        }

                        sectionData["unitName"] = units.find(u => u.unitCode == sectionData.unitId).unitName
                        sectionData["sectionName"] = units.find(u => u.unitCode == sectionData.sectionId).unitName
                        showCurrentSectionData(sectionData)

                    }


                    findSectionData(); findUnits();
                    function findSectionData() {
                        $.ajax({
                            method: 'get',
                            url: ctx + '/SectionData/findAll',
                            contentType: 'application/json',
                            async: false,
                            success: function (res) {
                                sectionArray = res
                            }, error: function (err) {
                                console.log(err)
                            }
                        });
                    }

                    function findUnits() {

                        if (sessionStorage.getItem('units')) {
                            units = JSON.parse(sessionStorage.getItem('units'))
                            return
                        }
                        $.ajax({
                            method: 'get',
                            url: ctx + '/Units/findAll',
                            contentType: 'application/json',
                            async: false,
                            success: function (res) {
                                units = res
                                sessionStorage.setItem('units', JSON.stringify(units))
                            }, error: function (err) {
                                console.log(err)
                            }
                        });
                    }

                    function resetClick() {

                        elements.forEach(e => {
                            if (e.tagName == 'INPUT') {
                                if (e.type == "radio") {
                                    e.checked = true;
                                } else {
                                    e.value = ''
                                }
                            }
                        })
                    }
                    function saveClick() {
                        elements.forEach(e => {
                            if (reqData.hasOwnProperty(e.id))
                                reqData[e.id] = e.value || e.innerHTML
                        })

                        reqData["needAudit"] = document.querySelector('input[name="needAudit"]:checked').value;

                        var method = 'put'
                        if (reqData.sId == null) {
                            method = 'post'
                        }

                        $.ajax({
                            method: method,
                            url: ctx + '/SectionData/',
                            contentType: 'application/json',
                            data: JSON.stringify(reqData),
                            async: false,
                            success: function (res) {
                                alert('資料儲存成功')
                                reqData = JSON.parse(JSON.stringify(res))
                                var index = sectionArray.findIndex(s => { return res.unitId == s.unitId && res.sectionId == s.sectionId })
                                if (index == -1)
                                    sectionArray.push(res)
                                else
                                    sectionArray[index] = res

                                //location.reload()
                            }, error: function (err) {
                                console.log(err)
                                alert('資料儲存失敗')
                            }
                        });

                    }
                    function saveSuperClick() {
                        sectionSuperManager["unitId"]  = document.getElementById('unitId').innerHTML
                        sectionSuperManager["superOccupation"] = document.getElementById("occupation1").value
                        sectionSuperManager["superConnector"] = document.getElementById("connector1").value
                        sectionSuperManager["superTel"] = document.getElementById("tel1").value
                        sectionSuperManager["superExtension"] = document.getElementById("extension1").value
                        sectionSuperManager["superEmail"] = document.getElementById("email1").value

                        sectionSuperManager["superOccupation2"] = document.getElementById("occupation2").value
                        sectionSuperManager["superConnector2"] = document.getElementById("connector2").value
                        sectionSuperManager["superTel2"] = document.getElementById("tel2").value
                        sectionSuperManager["superExtension2"] = document.getElementById("extension2").value
                        sectionSuperManager["superEmail2"] =  document.getElementById("email2").value


                        var method = 'put'
                        if (sectionSuperManager.sId == null) {
                            method = 'post'
                        }

                        $.ajax({
                            method: method,
                            url: ctx + '/SectionSuperManager/',
                            contentType: 'application/json',
                            data: JSON.stringify(sectionSuperManager),
                            async: false,
                            success: function (res) {
                                alert('資料儲存成功')
                                sectionSuperManager.sId = res.sId
                            }, error: function (err) {
                                console.log(err)
                                alert('資料儲存失敗')
                            }
                        });

                    }

                    function findCurrentSectionData(unitId, sectionId) {
                        var sectionData = sectionArray.find(s => { return unitId == s.unitId && sectionId == s.sectionId })
                        return sectionData

                    }

                    function showCurrentSectionData(sectionData) {
                        elements.forEach(e => {
                            if (e.tagName == 'INPUT') {
                                if (e.type == "radio") {
                                    try {
                                        document.querySelector('input[name="needAudit"][value="' + sectionData["needAudit"] + '"]').checked = true;
                                    } catch (err) {
                                    }

                                } else {
                                    e.value = sectionData[e.id]
                                }

                            }
                            if (e.tagName == 'SPAN') e.innerHTML = sectionData[e.id]
                        })
                        document.getElementById('unitName').innerHTML = sectionData.unitName
                        document.getElementById('unitId').innerHTML = sectionData.unitId
                        document.getElementById('sectionName').innerHTML = sectionData.sectionName
                        document.getElementById('sectionId').innerHTML = sectionData.sectionId
                        reqData.sId = sectionData.sId
                    }


                    function showCurrentSectionSuperManager(unitId) {

                        $.ajax({
                            method: 'get',
                            url: ctx + '/SectionSuperManager/' + unitId,
                            contentType: 'application/json',
                            async: false,
                            success: function (res) {
                                if (res.length > 0) {
                                    document.getElementById("occupation1").value = res[0]["superOccupation"]
                                    document.getElementById("connector1").value = res[0]["superConnector"]
                                    document.getElementById("tel1").value = res[0]["superTel"]
                                    document.getElementById("extension1").value = res[0]["superExtension"]
                                    document.getElementById("email1").value = res[0]["superEmail"]

                                    document.getElementById("occupation2").value = res[0]["superOccupation2"]
                                    document.getElementById("connector2").value = res[0]["superConnector2"]
                                    document.getElementById("tel2").value = res[0]["superTel2"]
                                    document.getElementById("extension2").value = res[0]["superExtension2"]
                                    document.getElementById("email2").value = res[0]["superEmail2"]
                                    sectionSuperManager.sId = res[0]["sId"]
                                }
                            }, error: function (err) {
                                console.log(err)
                                alert('資料儲存失敗')
                            }
                        });
                    }



                    function call(e, d) {
                        resetClick()

                        if (!d.type) {
                            if (d.superUnit != null) {
                                save.onclick = saveClick
                                var sectionData = findCurrentSectionData(d.superUnit, d.unitCode);
                                if (!sectionData) {
                                    document.getElementById('unitName').innerHTML = d.parent.unitName
                                    document.getElementById('unitId').innerHTML = d.parent.unitCode
                                    document.getElementById('sectionName').innerHTML = d.unitName
                                    document.getElementById('sectionId').innerHTML = d.unitCode
                                    reqData.sId = null
                                } else {
                                    sectionData["unitName"] = d.parent.unitName
                                    sectionData["sectionName"] = d.unitName
                                    showCurrentSectionData(sectionData)
                                }
                            } else {
                                //總框口
                                save.onclick = saveSuperClick
                                document.getElementById('unitName').innerHTML = d.unitName
                                document.getElementById('unitId').innerHTML = d.unitCode
                                document.getElementById('sectionName').innerHTML = ""
                                document.getElementById('sectionId').innerHTML = ""
                                sectionSuperManager.sId = null
                                showCurrentSectionSuperManager(d.unitCode)
                            }
                        }
                    }


                </script>


                <div>
                    <div>

                        <table class="main-table">
                            <tr>
                                <td colspan="3" style="background-color: #6699cc" class="center">
                                    科室資料編輯
                                </td>
                            </tr>
                            <tr>
                                <td class="center">單位名稱</td>
                                <td colspan="2">
                                    <span id="unitName"></span>
                                    <span id="unitId" style="display:none"></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="center">科室名稱</td>
                                <td colspan="2">
                                    <span id="sectionName"></span>
                                    <span id="sectionId" style="display:none"></span>
                                </td>
                            </tr>

                            <tr>
                                <td rowspan="4" class="center">科室代表<br />聯絡窗口</td>
                                <td class="center">職稱</td>
                                <td><input id="occupation1"></td>
                            </tr>

                            <tr>
                                <td class="center">姓名</td>
                                <td><input id="connector1"></td>
                            </tr>


                            <tr>
                                <td class="center">電話</td>
                                <td>
                                    <input id="tel1">
                                    分機
                                    <input id="extension1">
                                </td>

                            </tr>

                            <tr>
                                <td class="center">電子郵件</td>
                                <td><input id="email1"></td>
                            </tr>


                            <tr>
                                <td rowspan="4" class="center">科室第二<br />聯絡窗口</td>
                                <td class="center">職稱</td>
                                <td><input id="occupation2"></td>
                            </tr>

                            <tr>
                                <td class="center">姓名</td>
                                <td><input id="connector2"></td>
                            </tr>


                            <tr>
                                <td class="center">電話</td>
                                <td>
                                    <input id="tel2">
                                    分機
                                    <input id="extension2">
                                </td>

                            </tr>

                            <tr>
                                <td class="center">電子郵件</td>
                                <td><input id="email2"></td>
                            </tr>

                            <tr>
                                <td class="center">科室資料是否需要審核</td>
                                <td colspan="2">
                                    <input name="needAudit" value="Y" type="radio" id="needAudit1">需要
                                    <input name="needAudit" value="N" type="radio" id="needAudit2" checked="true">無需
                                </td>
                            </tr>

                            <tr>
                                <td colspan="3" style="background-color: #cccccc" class="center">
                                    <button class="main-button" id="reset">重設</button>
                                    <button class="main-button" id="save">存檔</button>
                                </td>
                            </tr>

                        </table>


                    </div>

                </div>
            </t:menu>
        </t:layout>