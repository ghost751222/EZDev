const QuestionAnswerWindows = [];
const QuestionAnswer = {
  unitId: null,
  sectionId: null,
  question: null,
  answer: null,
  keyword: null,
  creator: null,
  createTime: null,
  lastModifier: null,
  lastUpdateTime: null,
  status: null,
  efficientTime: null,
  score: null,
  hit: null,
  isEdit: null,
  applier: null,
  applyTime: null,
  closeData: null,
  closeDataTime: null,
  oldQuestion: null,
  oldAnswer: null,
  disableReason: null,
  disableTime: null,
  orderTime: null,
  isPublic: null,
};

loadTemplate();
loadDivTab();
function loadTemplate() {
  var template = [
    "tmplItem",
    "questionAnswerRelationDisplayItem",
    "questionAnswerDisplayItem",
    "questionAnswerWindowDisplayItem",
    "questionAnswerWindowEditItem",
    "questionAnswerOpinionItem",
    "qaEditHistoryItem",
    "questionAnswerAttachEditItem",
    "questionAnswerAttachDisplayItem",
  ];
  var url = ctx + "/resources/js/component/";
  for (var i in template) {
    var id = template[i];
    $.ajax({
      url: url + id + ".html?" + new Date().getTime(),
      async: false,
      success: function (d) {
        var s = document.createElement("script");
        s.type = "text/x-jquery-tmpl";
        s.id = id;
        s.appendChild(document.createTextNode(d));
        document.body.appendChild(s);
      },
    });
  }
}

function loadDivTab() {
  var url = ctx + "/resources/js/component/";
  var template = [
    "editTab",
    "qaEditHistoryTab",
    "suggestListTab",
    "suggestTab",
    "disableReasonTab",
    "qaReasonTab",
  ];
  var url = ctx + "/resources/js/component/";
  for (var i in template) {
    var id = template[i];
    $.ajax({
      url: url + id + ".html?" + new Date().getTime(),
      async: false,
      success: function (d) {
        $(document.body).append(d);
      },
    });
  }
}

function findByUnitIdAndSectionId(unitId, sectionId) {
  var method = "get";
  var url = ctx + "/QuestionAnswer/find?";
  //var url = ctx + '/QuestionAnswer/findAll?'
  let params = new URLSearchParams();
  params.append("unitId", unitId);
  params.append("sectionId", sectionId);
  url = url + params.toString();
  var res = callAjax(method, url);
  return res;
}

function openDialog(targetID, title, width, height, button) {
  $dialog = $("#" + targetID).dialog({
    title: title,
    modal: false,
    height: height,
    width: width,
    buttons: button,
  });
}
function closeDialog() {
  $(this).dialog("close");
}

function getQuestionAnswerWindows() {
  var questionAnswerWindows = [];
  $("#questionAnswerWindowTable tr").each(function (index, row) {
    var tel = null,
      extension = null,
      connector = null;
    $(row)
      .find("input")
      .each(function (i, input) {
        if (input.id == "connector") connector = input.value;
        if (input.id == "extension") extension = input.value;
        if (input.id == "tel") tel = input.value;
      });
    questionAnswerWindows.push({
      connector: connector,
      tel: tel,
      extension,
      extension,
    });
  });

  return questionAnswerWindows;
}

function uploadQuestionAnswerAttaches(pId, callback) {

  if($("#questionWindowAttachTable tr").length ==0 && callback){
    callback(null)
  }
  var url = ctx + "/QuestionAnswerAttachment/";
  $("#questionWindowAttachTable tr input ").each(async function (index, input) {
    var file = null
    var sId = null;
    var fileName = null;
    var fileSize = null;
    var fileType = null;
    var fileContent = null;
    var IsAdd = true;
    var res;
    if(input.type == 'file'){
         file = input.files[0];
         fileName = file.name;
         fileSize = file.size;
         fileType = file.type;
      var bytes = await readFileAsync(file);
          bytes = Array.from(new Uint8Array(bytes));
          fileContent = btoa(bytes.map((item) => String.fromCharCode(item)).join(""));

    }else{
         file = JSON.parse(input.getAttribute('data-attachment')).data
         fileName = file.fileName;
         fileSize = file.fileSize;
         fileType = file.fileType;
         fileContent = file.fileContent;
         if(file.pId == pId) IsAdd = false;
    }
            var json = {
              sId: sId,
              pId: pId,
              fileName: fileName,
              fileSize: fileSize,
              fileContent: fileContent,
              fileType: fileType,
            };

           if(IsAdd) res = callAjax("post", url, JSON.stringify(json));
           if (callback) callback(res);


  });
}
function readFileAsync(file) {
  return new Promise((resolve, reject) => {
    var reader = new FileReader();

    reader.onload = () => {
      resolve(reader.result);
    };

    reader.readAsArrayBuffer(file);
  });
}

function addClick() {
  Object.entries(QuestionAnswer).forEach(([key, value]) => {
    QuestionAnswer[key] = null;
  });
  QuestionAnswer.unitId = userData.unitId;
  QuestionAnswer.sectionId = userData.sectionId;

  elements.forEach((e) => (e.value = ""));
  $(
    "#efficientTimeQuote,#keywordQuote,#answerQuote,#questionQuote,#isPublicQuote,#questionWindowQuote,#questionWindowAttachQuote"
  ).hide();

  $("#questionAnswerWindowTable tr").remove();
  $("#questionWindowAttachTable tr").remove();

  var buttons = {
    確定建立: function () {
      saveClick(null, QuestionAnswer);
    },
    關閉視窗: closeDialog,
  };
  openDialog("editTab", "新增FAQ", 800, 500, buttons);
}

function suggestSaveClick(questionAnswerRelation) {
  var content = document.querySelector("#content").value;
  var QuestionAnswerOpinion = {
    sId: null,
    pId: questionAnswerRelation.sId,
    content: content,
  };
  var method = "post";
  var url = ctx + "/QuestionAnswerOpinion/";
  var requestData = JSON.stringify(QuestionAnswerOpinion);
  var response = callAjax(method, url, requestData);
  if (response) {
    alert("儲存建議成功");
    location.reload();
  }
}
function saveClick(event, questionAnswer) {
  elements.forEach((e) => {
    questionAnswer[e.id] = e.value;
  });

  if (questionAnswer.sId == null || questionAnswer.sId == "") {
    method = "post";
  } else {
    method = "put";
  }

  questionAnswer["questionAnswerWindows"] = getQuestionAnswerWindows();

  if (questionAnswer.efficientTime == "") questionAnswer.efficientTime = null;

  var url = ctx + "/QuestionAnswer/";
  var requestData = JSON.stringify(questionAnswer);
  var response = callAjax(method, url, requestData);
  if (response) {
    alert("儲存資料成功");
    uploadQuestionAnswerAttaches(response.sId, function (data) {
      if(Array.isArray(questionAnswer.questionAnswerAttaches))
            questionAnswer.questionAnswerAttaches =questionAnswer.questionAnswerAttaches.concat(data);
      else{
         questionAnswer.questionAnswerAttaches = []
      }
      refreshQuestionAnswerView(event, questionAnswer);
    });
  }
}
function editClick(event, data) {
  var _data = data.data;
  addClick();
  //QuestionAnswer.sId = _data.sId
  elements.forEach((e) => {
    if (_data.hasOwnProperty(e.id)) {
      e.value = _data[e.id];
    }
  });
  createQuestionAnswerWindowData(_data.questionAnswerWindows);
  createQuestionAnswerAttachData(_data.questionAnswerAttaches);

  var buttons = {
    確定建立: function () {
      saveClick(event, _data);
    },
    關閉視窗: closeDialog,
  };
  openDialog("editTab", "編輯FAQ", 800, 700, buttons);
}

function offClick(event, data) {


  document.querySelector("#qaReason").value = data.data.qaReasons.length > 0 ? data.data.qaReasons[0].reason : '';

  var buttons = {
    確定建立: function () {
      var questionAnswer = data.data;
      questionAnswer.reason = document.querySelector("#qaReason").value;
      var method = "post";
      var url = ctx + "/QuestionAnswer/off";
      var requestData = JSON.stringify(questionAnswer);
      var response = callAjax(method, url, requestData);
      if (response) {
        alert("下架成功");
        jQuery.extend(questionAnswer, response);
        refreshQuestionAnswerView(event, questionAnswer);
      }
    },
    關閉視窗: closeDialog,
  };

  openDialog("qaReasonTab", "下架原因", 600, 300, buttons);
}

function applyOffClick(event, data) {

  document.querySelector("#disableReason").value = data.data.disableReason;
  var buttons = {
    確定建立: function () {
      var questionAnswer = data.data;
      questionAnswer.disableReason =
        document.querySelector("#disableReason").value;
      var questionAnswer = data.data;
      var method = "post";
      var url = ctx + "/QuestionAnswer/applyOff";
      var requestData = JSON.stringify(questionAnswer);
      var response = callAjax(method, url, requestData);
      if (response) {
        jQuery.extend(questionAnswer, response);
        refreshQuestionAnswerView(event, questionAnswer);
      }
    },
    關閉視窗: closeDialog,
  };

  openDialog("disableReasonTab", "你確定要申請下架嗎？", 600, 300, buttons);
}

function cancelOffClick(event, data) {

  var questionAnswer = data.data;
  var method = "post";
  var url = ctx + "/QuestionAnswer/cancelOff";
  var requestData = JSON.stringify(questionAnswer);
  var response = callAjax(method, url, requestData);
  if (response) {

    questionAnswer.status = response.status;
    if(event.nodeName == 'SPAN')
        refreshQuestionAnswerView(event, questionAnswer);
    else{
          alert('資料已經上架')
          refreshQuestionAnswerView(null, questionAnswer);
    }

  }
}
function deleteClick(data) {
  var _data = data.data;
  if (
    _data.questionAnswerRelation == null ||
    _data.questionAnswerRelation.status == "S"
  ) {
    if (confirm("是否刪除")) {
      var method = "delete";
      var url = ctx + "/QuestionAnswer/" + _data.sId;
      var response = callAjax(method, url);
      if (response) {
        alert("刪除成功");
        location.reload();
      }
    }
  } else {
    alert("請先下架或刪除補充說明");
  }
}
function moreClick(obj) {
  var $table = $(obj).parent().siblings("div.ellipsis").children();
  var $neighborTable = $(obj)
    .parent()
    .parent()
    .siblings("td")
    .children("div.ellipsis")
    .children();

  var $isVisible = $table.css("visibility");
  var $neighborVisible = $neighborTable.css("visibility");

  if ($isVisible == "collapse" || $isVisible == "hidden") {
    $table.css("visibility", "visible");
    //if ($neighborVisible == "collapse") $neighborTable.css('visibility', 'hidden')
    //var height = $table.height()
    //var neighborHeight = $neighborTable.height()
    //var max = Math.max(height, neighborHeight)
    //$table.height(max)
    //$neighborTable.height(max)
    $(obj).text("返回");
  } else {
    //if ($neighborVisible == "hidden") {
    $table.css("visibility", "collapse");
    //$neighborTable.css('visibility', 'collapse')
    //} else if ($neighborVisible == "visible") {
    //  $table.css('visibility', 'hidden')
    //}
    $(obj).text("更多...");
  }
}
function questionAnswerRelationMoreClick(obj) {
  moreClick(obj);
}

function relationClick(event,data) {
  addClick();
  var _data = data.data;
  $(
    "#efficientTimeQuote,#keywordQuote,#answerQuote,#questionQuote,#isPublicQuote,#questionWindowQuote,#questionWindowAttachQuote"
  ).show();
  $("#efficientTimeQuote")
    .unbind("click")
    .click(function () {
      $("#efficientTime").val(_data.efficientTime);
    });
  $("#keywordQuote")
    .unbind("click")
    .click(function () {
      $("#keyword").val(_data.keyword);
    });
  $("#answerQuote")
    .unbind("click")
    .click(function () {
      $("#answer").val(_data.answer);
    });
  $("#questionQuote")
    .unbind("click")
    .click(function () {
      $("#question").val(_data.question);
    });

  $("#isPublicQuote")
    .unbind("click")
    .click(function () {
      $("#isPublic").val(_data.isPublic);
    });
  $("#questionWindowQuote")
    .unbind("click")
    .click(function () {
      createQuestionAnswerWindowData(_data.questionAnswerWindows);
    });
  $("#questionWindowAttachQuote")
    .unbind("click")
    .click(function () {
      createQuestionAnswerAttachData(_data.questionAnswerAttaches);
    });

  var buttons = {
    確定建立: function () {
      _data["pId"] = _data.sId;
      _data.questionAnswerOpinions = [];
      questionAnswerRelationSaveClick(event, _data);
    },
    關閉視窗: closeDialog,
  };
  openDialog("editTab", "新增補充說明", 900, 700, buttons);
}

function historyClick(data) {
  var questionAnswer = data.data;

  var method = "get";
  var url = ctx + "/QAEditHistory/find/" + questionAnswer.sId;
  var res = callAjax(method, url);
  if (res) {
    var buttons = { 關閉視窗: closeDialog };
    openDialog("qaEditHistoryTab", "版本差異", 800, 600, buttons);

    $("#qaEditHistorySel")
      .unbind("change")
      .change(function (e) {
        $("#historyTb").html("");
        var jsonData = {};
        var value = this.value;

        var obj = res.find((r) => r.sId.toString() === value);

        jsonData["question"] = highLight3(obj.question, questionAnswer.question,true,false);
        jsonData["answer"] = highLight3(obj.answer, questionAnswer.answer,true,false)
        jsonData["keyword"] = highLight3(obj.keyword, questionAnswer.keyword,true,false);

        jsonData["editQuestion"] = highLight3(obj.question,questionAnswer.question,false,true);
        jsonData["editAnswer"] = highLight3(obj.answer, questionAnswer.answer,false,true);
        jsonData["editKeyword"] = highLight3(obj.keyword,questionAnswer.keyword,false,true);

//        highLight3(obj.question,questionAnswer.question,false,true)
//		jsonData["question"] = questionAnswer.question
//		jsonData["answer"] = questionAnswer.answer
//		jsonData["keyword"] = questionAnswer.keyword
//
//        jsonData["editQuestion"] = obj.question;
//        jsonData["editAnswer"] = obj.answer;
//        jsonData["editKeyword"] = obj.keyword;

        var html = templateRender("#qaEditHistoryItem", jsonData);

        $("#historyTb").append(
          html.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
        );
      })
      .empty();

    res.forEach((r) => {
      $("#qaEditHistorySel").append(new Option(r.editDate, r.sId));
    });
    $("#qaEditHistorySel").change();
  }
}

function questionAnswerRelationSaveClick(event, questionAnswerRelation) {

  var method = ''
  elements.forEach((e) => {
    if (questionAnswerRelation.hasOwnProperty(e.id)) {
      questionAnswerRelation[e.id] = e.value;
    }
  });



  questionAnswerRelation["questionAnswerWindows"] = getQuestionAnswerWindows();
  if (questionAnswerRelation.efficientTime == "") questionAnswerRelation.efficientTime = null;
  if (questionAnswerRelation.sId == "") questionAnswerRelation.sId = null
  if(questionAnswerRelation.sId != null)
    method ='put'
  else
    method = 'post'

  var url = ctx + "/QuestionAnswerRelation/";
  var requestData = JSON.stringify(questionAnswerRelation);
  var res = callAjax(method, url, requestData);
  if (res) {
    alert("儲存資料成功");
    uploadQuestionAnswerAttaches(res.sId,function(data){
        //location.reload();
         jQuery.extend(questionAnswerRelation, res);
         if(Array.isArray(questionAnswerRelation.questionAnswerAttaches))
                    questionAnswerRelation.questionAnswerAttaches =questionAnswerRelation.questionAnswerAttaches.concat(data);
          else{
                 questionAnswerRelation.questionAnswerAttaches = []
        }

        refreshQuestionAnswerRelationView(event, questionAnswerRelation);
    });

  }
}

function questionAnswerRelationEditClick(event,questionAnswerRelation) {
  var questionAnswerWindows = questionAnswerRelation.questionAnswerWindows;
  addClick();
   createQuestionAnswerWindowData(questionAnswerWindows);
   createQuestionAnswerAttachData(questionAnswerRelation.questionAnswerAttaches);
  elements.forEach((e) => {
    if (questionAnswerRelation.hasOwnProperty(e.id))
      e.value = questionAnswerRelation[e.id];
  });
  var buttons = {
    確定建立: function () {
      questionAnswerRelationSaveClick(event, questionAnswerRelation);
    },
    關閉視窗: closeDialog,
  };
  openDialog("editTab", "編輯1996口語話術說明", 800, 700, buttons);
}
function questionAnswerRelationDeleteClick(data) {
  var method = "delete";
  var url = ctx + "/QuestionAnswerRelation/" + data.sId;
  var res = callAjax(method, url, null);
  if (res) {
    alert("刪除成功");
    location.reload();
  }
}

function questionAnswerRelationSuggestClick(questionAnswerRelation) {
  document.querySelector("#content").value = "";
  var buttons = {
    確定建立: function () {
      suggestSaveClick(questionAnswerRelation);
    },
    關閉視窗: closeDialog,
  };
  openDialog("suggestTab", "新增建議", 600, 500, buttons);
}

function questionAnswerRelationOnClick(event, questionAnswerRelation) {
  var method = "put";
  var url = ctx + "/QuestionAnswerRelation/on";
  var requestData = JSON.stringify(questionAnswerRelation);
  var res = callAjax(method, url, requestData);
  if (res) {
    alert("上架成功");
    jQuery.extend(questionAnswerRelation, res);
    // location.reload();
    refreshQuestionAnswerRelationView(event, questionAnswerRelation);
  }
}

function questionAnswerRelationOffClick(event, questionAnswerRelation) {
  document.querySelector("#qaReason").value = "";

  var buttons = {
    確定建立: function () {
      var method = "put";
      var url = ctx + "/QuestionAnswerRelation/off";
      questionAnswerRelation.reason = document.querySelector("#qaReason").value;
      var requestData = JSON.stringify(questionAnswerRelation);
      var res = callAjax(method, url, requestData);
      if (res) {
        alert("下架成功");
        //location.reload();
        jQuery.extend(questionAnswerRelation, res);
        refreshQuestionAnswerRelationView(event, questionAnswerRelation);
      }
    },
    關閉視窗: closeDialog,
  };

  openDialog("qaReasonTab", "下架原因", 600, 300, buttons);
}

function visibleClick(e, questionAnswerOpinion) {
  if (confirm("是否將其設置不可見")) {
    questionAnswerOpinion.visible = "N";
    $(e).parent().remove();
    var method = "put";
    var url = ctx + "/QuestionAnswerOpinion/";
    var requestData = JSON.stringify(questionAnswerOpinion);
    var response = callAjax(method, url, requestData);
    if (response) {
    }
  }
}
function viewClick(e, data) {
  var questionAnswerRelation = data.data;
  var method = "get";
  var url = ctx + "/QuestionAnswerOpinion/find/" + questionAnswerRelation.sId;
  var response = callAjax(method, url, null);
  $("#suggestTb").html("");
  $("#questionAnswerOpinionItem").tmpl(response).appendTo("#suggestTb");
  var buttons = { 關閉視窗: closeDialog };
  openDialog("suggestListTab", "所有建立列表", 600, 500, buttons);
}
function createData(jsonData) {
  $("#tmplItem").tmpl(jsonData).appendTo("#tb");
}

function createQuestionAnswerWindowData(jsonData) {
  $("#questionAnswerWindowEditItem")
    .tmpl(jsonData)
    .appendTo("#questionAnswerWindowTable");
}

function createQuestionAnswerAttachData(jsonData) {
  $("#questionAnswerAttachEditItem")
    .tmpl(jsonData)
    .appendTo("#questionWindowAttachTable");
}

function questionAnswerWindowItemDeleteClick(e, data) {
  $(e).closest("tr").remove();
}

function questionAnswerAttachItemDeleteClick(e, data) {
  if(data.data.sId){
        callAjax('delete', ctx + "/QuestionAnswerAttachment/" + data.data.sId,null)
  }
  $(e).closest("tr").remove();
}

function questionAnswerWindowBtnClick(e) {
  var data = [];
  data.push({
    sId: null,
    pId: null,
    connector: null,
    tel: null,
    extension: null,
  });
  createQuestionAnswerWindowData(data);
}

function questionWindowAttachBtnClick(e) {
  var data = [];
  data.push({
    sId: null,
    pId: null,
    fileName: null,
    fileSize: null,
    fileContent: null,
    fileType: null,
  });

  createQuestionAnswerAttachData(data);
}

function templateRender(id, data) {
  var html = "";
  try {
    $(id)
      .tmpl(data)
      .each(function (i, q) {
        if (typeof this.outerHTML == "string") html += this.outerHTML;
      });
  } catch (e) {
    console.log(id, data, e);
  }

  return html;
}

function words(s) {
  //var q = s.toLowerCase().match(/\S/g);
  var q = s.match(/\S/g);
  return q;
}

function highLight(source, reference) {
  let a = words(source) || [];
  let b = words(reference) || [];
  let res1 = b.filter((i) => !a.includes(i));
  let res2 = new Set(a.filter((i) => !b.includes(i)));

  // Loop over the words in res2 not present in res1.

  res2.forEach((word) => {
    // Replace the word with the word wrapped in an element.
    // source = source.replace(word, `<span style="color:red">${word}</span>`);
    source = source.replaceAll(word, `<∬>${word}</∮>`);
  });

  source = source.replaceAll("∬", 'span style="color:green"');
  source = source.replaceAll("∮", "span");

  return source;
}

function highLight2(source, reference) {
  let a = words(source) || [];
  let b = words(reference) || [];
  let res1 = b.filter((i) => !a.includes(i));
  let res2 = new Set(a.filter((i) => !b.includes(i)));

  // Loop over the words in res2 not present in res1.

  res2.forEach((word) => {
    // Replace the word with the word wrapped in an element.
    // source = source.replace(word, `<span style="color:red">${word}</span>`);
    source = source.replaceAll(word, `<∬>${word}</∮>`);
  });

  source = source.replaceAll("∬", 'span style="color:red"');
  source = source.replaceAll("∮", "span");

  return source;
}


function highLight3(source, reference,added,removed) {
  var pre = document.createElement('pre')
  var fragment = document.createDocumentFragment();
  var diff = Diff.diffChars(source, reference);

   diff.forEach((part) => {
        // green for additions, red for deletions
        // grey for common parts
        const color = part.added ? 'green' :
            part.removed ? 'red' : '';

        span = document.createElement('span');
        span.style.color = color;
        span.appendChild(document.createTextNode(part.value));



        if(part.added ==added) fragment.appendChild(span);

        if(part.removed == removed) fragment.appendChild(span);

        if(!part.added && !part.removed) fragment.appendChild(span);


    });

    pre.appendChild(fragment);
  return pre.innerHTML;
}

function callAjax(method, url, requestData) {
  var response;
  var $ajax = $.ajax({
    method: method,
    url: url,
    contentType: "application/json",
    async: false,
    data: requestData,
    success: function (res) {
      response = res;
    },
    error: function (err) {
      console.log(err);
      alert("發生異常錯誤");
    },
  });
  return response;
}

function refreshQuestionAnswerView(event, questionAnswer) {

  if ($dialog) $dialog.dialog("close");

  if(app && app.query){
     app.query()
  }

//  var $td = $(event).closest("td");
//  if($td.length > 0){
//    var html = templateRender("#questionAnswerDisplayItem", questionAnswer);
//    html = html.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
//    $td.html("").append(html)
//  }
}

function refreshQuestionAnswerRelationView(event, questionAnswerRelation) {



  if ($dialog) $dialog.dialog("close");
    if(app && app.query){
       app.query()
    }
//    var $div = $(event).closest(".questionAnswerRelation");
//  if($div[0]){
//    var html = templateRender("#questionAnswerRelationDisplayItem",questionAnswerRelation);
//    var $td = $(event).closest("td");
//        html = html.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
//        $(html).insertBefore($div);
//        $div[0].outerHTML = "";
//  }

}

function checkExpired(efficientTime,sectionId){
var returnValue = ""
if(efficientTime){
    var sectionName = getSectionName(sectionId)
    var interValDay = 30
    var today = new Date()
    var d = new Date(efficientTime);

    var diffTime = d.getTime() - today.getTime();
    var diffDay = diffTime / (1000 * 3600 * 24);

    if(d < today){
      returnValue = `&#9888;${sectionName} 資料已於${efficientTime}過期`
    }else if(diffDay <= interValDay) {
      returnValue = `&#9888;${sectionName} 資料將於${efficientTime}過期`
    }
}


return returnValue

}

function showEdit(lastUpdateTime,lastModifier){
    var _users = users.filter(u=>u.userID ==lastModifier)
    var sectionName = null
    var _userAccount= _users[0].account
    if (_users.length > 0){
          sectionName = getUnitName(_users[0].unitCode)  || getSectionName(_users[0].unitCode)
    }
    if(!sectionName) sectionName = ''
    //if(_users.length > 0) _userAccount = users[0].account
    return `${lastUpdateTime}內容被[${sectionName} ${_userAccount}]修改過`
}


function closeDataUser(closeDataTime,closeData){
    
    var _users = users.filter(u=>u.userID ==closeData)
    var sectionName = null
    if (users.length > 0){
             sectionName = getUnitName(_users[0].unitCode)  || getSectionName(_users[0].unitCode)
    }
    if(!sectionName) sectionName = ''

    return `${closeDataTime}已被[${sectionName} ${_users[0].account}]下架`
}

function applierUser(applyTime,applier){
   // var users = getUser(applier)
   var _users = users.filter(u=>u.userID ==applier)
    var sectionName = null
    if (_users.length > 0){
             sectionName = getUnitName(_users[0].unitCode)  || getSectionName(_users[0].unitCode)
    }
    if(!sectionName) sectionName = ''

    return `${applyTime}已被[${sectionName} ${_users[0].account}]申請下架`
}