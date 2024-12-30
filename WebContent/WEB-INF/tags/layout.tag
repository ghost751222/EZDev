
<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="pageTitle" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
    request.setAttribute("time", System.currentTimeMillis());
%>
  <html>

  <head>


    <title>${pageTitle}</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />



    <link href="${pageContext.request.contextPath}/resources/css/main.css?${time}" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/resources/css/jquery/jquery-ui.css?3" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/resources/css/semantic.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/resources/css/vue-multiselect.min.css" rel="stylesheet" />



    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.5.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.13.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery/jquery.tmpl.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/vue.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/vuetable.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/vue-multiselect.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js?${time}"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/diff/diff.min.js"></script>
    <jsp:invoke fragment="head"/>

    <script type="text/x-template" id="select-template">
      <select @change="$emit('change', $event.target)" class="form-control" @input="$emit('input', $event.target.value)">
          <option v-for="d in data" :value="d.value">{{d.text}}</option>
      </select>
    </script>

   <style type="text/css">
     body{
       font-family:none;
     }
   </style>

    <script type="text/x-template" id="datePicker">



          <div class="input-group input-group-sm mb-3">
            <span class="input-group-text">{{title}}</span>
            <input type="date" class="form-control" v-model="time.startTime"/>
            <span class="input-group-text">至</span>
            <input type="date" class="form-control" v-model="time.endTime"  />
            <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false"></button>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item" @click="click($event.target,1)">今日</a></li>
              <li><a class="dropdown-item" @click="click($event.target,2)" >昨日</a></li>
              <li><a class="dropdown-item" @click="click($event.target,3)" >一週內</a></li>
              <li><a class="dropdown-item" @click="click($event.target,4)" >一個月內</a></li>
              <li><a class="dropdown-item" @click="click($event.target,5)" >這個月</a></li>
              <li><a class="dropdown-item" @click="click($event.target,6)" >上個月</a></li>
              <li><a class="dropdown-item" @click="click($event.target,7)" >今年</a></li>
              <li><a class="dropdown-item" @click="click($event.target,8)" >去年</a></li>
            </ul>
          </div>


    </script>

    <script>

      var unit1996 = "1996"
      var unit1996Data = { parent: { name: unit1996 }, name: unit1996, superUnit: 'ZZZ', unitCode: 'ZZZ' }
      $(document).ready(function(){
        var bootstrapButton = $.fn.button.noConflict()
         $.fn.bootstrapBtn = bootstrapButton;
         var searchParams = new URLSearchParams(location.search);
         var userId = searchParams.get("userId")
         $.ajaxSetup({ cache: false,beforeSend:function(xhr,settings) { settings.url = settings.url + "?userId=" + userId  }   })
      })
      const authorization = ${ authorization }

      var ctx = "${pageContext.request.contextPath}"
      var sectionJson = getSection();
      var units = sectionJson["units"];
      var sections = sectionJson["sections"];
      var users = getAllUser();
      var caseStatusType = getAppCode('R04')
      var serviceCategory = getAppCode('G03')
      var callCategory = getAppCode('G04')
      var serviceType = getServiceType()
      var serviceItem = getServiceItem()



      Vue.config.productionTip = false
      Vue.config.devtools = false
      Vue.prototype.$units= units
      Vue.prototype.$sections= sections
      Vue.use(Vuetable)
      Vue.component('vuetable-pagination', Vuetable.VuetablePagination)
      Vue.component("vuetable-pagination-dropdown", Vuetable.VueTablePaginationDropDown);
      Vue.component("vuetable-pagination-info", Vuetable.VueTablePaginationInfo);
      Vue.component('vue-multiselect', window.VueMultiselect.default)
      var time_type = [
        { value: "Y", text: "依年" },
        { value: "M", text: "依月" },
        { value: "W", text: "依週" },
        { value: "D", text: "依日" },
        { value: "H", text: "依時" },
        { value: "A", text: "依總數" },
      ];
      var accept_type = [
        { value: "A", text: "全部" },
        { value: "TL", text: "電話+LINE" },
        { value: "T", text: "電話" },
        { value: "L", text: "LINE" },
        { value: "I", text: "留言" },
      ];
      var date_type = [
        { value: "All", text: "全部" },
        { value: "W", text: "週間/工作日" },
        { value: "H", text: "周末/假日" },
      ];
      var case_status_type = [];
      var service_category = [];
      var call_category = [];
      case_status_type  = [{value:'All',text:'全部'}].concat(caseStatusType.map(function(item) { return { value:item.codeCode,text:item.codeDisp }  }  )      )
      service_category  = [{value:'All',text:'全部'}].concat(serviceCategory.map(function(item) { return { value:item.codeCode,text:item.codeDisp }  }  )      )
      call_category  = [{value:'All',text:'全部'}].concat(callCategory.map(function(item) { return { value:item.codeCode,text:item.codeDisp }  }  )      )




      var return_type = [
        { value: "未回報\結報", text: "未回報\結報" },
        { value: "已回報\結報", text: "已回報\結報" },
      ];


      Vue.component("time-type-select", {
        template: "#select-template",
        props: {
          data: {
            type: Array,
            default() {
              return time_type;
            },
          },
        },
      });
      Vue.component("date-type-select", {
        template: "#select-template",
        props: {
          data: {
            type: Array,
            default() {
              return date_type;
            },
          },

        },
      });
      Vue.component("accept-type-select", {
        template: "#select-template",
        props: {
          data: {
            type: Array,
            default() {
              return accept_type;
            },
          },

        },
      });
      Vue.component("case-status-type-select", {
        template: "#select-template",
        props: {
          data: {
            type: Array,
            default() {
              return case_status_type;
            },
          },
        },
      });
       Vue.component("service-category-select", {
              template: "#select-template",
              props: {
                data: {
                  type: Array,
                  default() {
                    return service_category;
                  },
                },
              },
      });
       Vue.component("call-category-select", {
              template: "#select-template",
              props: {
                data: {
                  type: Array,
                  default() {
                    return call_category;
                  },
                },
              },
      });
      Vue.component("return-type-select", {
        template: "#select-template",
        props: {
          data: {
            type: Array,
            default() {
              return return_type;
            },
          },
        },
      });
      Vue.component("units-select", {
        template: "#select-template",
        props: {
          value:{
            type:String,
            default:''
          },
          data: {
            type: Array,
            default() {
              return units;
            },
          },
        },
      });
      Vue.component("sections-select", {
        template: "#select-template",
        props: {
            value:{
                      type:String,
                      default:''
                    },
          data: {
            type: Array,
            default() {
              return sections;
            },
          },
        },
      });

    Vue.component("users-select", {
        template: "#select-template",
        props: {
            value:{
                      type:String,
                      default:''
                    },
          data: {
            type: Array,
            default() {
              return [].concat(users.map(function(u) { return  {'value':u.userID,'text':u.userID + " " +u.userName } }));
            },
          },
        },mounted(){


        }
      });

      Vue.component('date-picker', {
          template: '#datePicker',
          props: {
            time:{
              type:Object,
               default(rawProps){
                  return{
                    startTime:'',
                     endTime:''
                  }
               }
            },

            title:{
              default:'查詢時間'
            }
          },
          methods: {
            change:function(e){
              this.$emit('input',this.time)
            },
            click: function (target, type) {
              var dateOption = { year: 'numeric', month: '2-digit', day: '2-digit' }
              switch (type) {
                case 1:
                  //var today = (new Date()).toLocaleDateString('en-CA')
                  var today = (new Date()).toISOString().split("T")[0]
                  this.time.startTime = today
                  this.time.endTime = today
                  break;
                case 2:
                  var today = new Date();
                  var yesterday = new Date();
                  yesterday.setDate(today.getDate() - 1)
                  this.time.startTime = yesterday.toISOString().split("T")[0]
                  this.time.endTime = yesterday.toISOString().split("T")[0]
                  break;
                case 3:
                  var date = new Date();
                  var today = new Date();
                  var yesterday = new Date();
                  yesterday.setDate(today.getDate() - 7)
                  this.time.startTime = yesterday.toISOString().split("T")[0]
                  this.time.endTime= today.toISOString().split("T")[0]
                  break;
                case 4:
                  var date = new Date();
                  var today = new Date();
                  var yesterday = new Date();
                  yesterday.setDate(today.getDate() - 30)
                  this.time.startTime = yesterday.toISOString().split("T")[0]
                  this.time.endTime= today.toISOString().split("T")[0]

                  break;
                case 5:
                  var date = new Date();
                  var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
                  var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

                  this.time.startTime = firstDay.toLocaleDateString('fr-CA',dateOption)
                  this.time.endTime= lastDay.toLocaleDateString('fr-CA',dateOption)
                  break;
                case 6:
                  var date = new Date();
                  var firstDay = new Date(date.getFullYear(), date.getMonth() - 1, 1);
                  var lastDay = new Date(date.getFullYear(), date.getMonth(), 0);
                  this.time.startTime = firstDay.toLocaleDateString('fr-CA',dateOption)
                  this.time.endTime= lastDay.toLocaleDateString('fr-CA',dateOption)

                  break;
                case 7:
                  var year = new Date().getFullYear()

                  this.time.startTime = `\${year}-01-01`
                  this.time.endTime= `\${year}-12-31`
                  break;
                case 8:
                  var year = new Date().getFullYear() - 1
                  this.time.startTime = `\${year}-01-01`
                  this.time.endTime= `\${year}-12-31`
                  break;
              }
              this.change()
            }
          },
          mounted() {
              var s = this.time.startTime
              var e = this.time.endTime
              this.click(1, 1)
              if(s)  this.time.startTime = s
              if(e)  this.time.endTime = e
                //this.time.startTime ='2021-06-01'

          }

        })
    </script>
  </head>

  <body>
        <jsp:doBody />
  </body>

  </html>