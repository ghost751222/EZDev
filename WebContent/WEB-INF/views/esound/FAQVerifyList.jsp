

    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
     <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

    <t:layout >
      <div class="container" id="app">
        <form>
          <div class="form-group">
            <label for="units" >單位</label>
            <units-select v-model="units"/>
          </div>
          <div class="form-group">
            <label for="sections">科室</label>
            <sections-select v-model="sections" />
          </div>
        </form>
        <button class="btn btn-primary" @click="query">下載報表</button>
      </div>
      <script>
           var app = new Vue({
             el: '#app',
             data: {
               sections: "",
               units: "",
             }, methods: {
               query: function () {
                 var unitValue = this.units
                 var sectionValue = this.sections
                 if (unitValue == '') unitValue = null
                 if (sectionValue == '') sectionValue = null
                 var json = { unitId: unitValue, sectionId: sectionValue };
                 var url = ctx + '/Excel/FAQVerifyList/FAQVerifyList?'
                 var params = new URLSearchParams("?params=" + JSON.stringify(json));
                 var aLink = document.createElement('a');
                 aLink.href = url + params.toString();
                 aLink.target = "_blank";
                 aLink.click();
               }
             }
           })
      </script>
    </t:layout>
