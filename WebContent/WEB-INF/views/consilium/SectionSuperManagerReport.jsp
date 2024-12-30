<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 <%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
  <div class="container" id="app">
    <button class="btn btn-primary" @click="query">下載報表</button>
  </div>
  <script>
       var app = new Vue({
         el: '#app',
         data: {
           sections: "",
           units: "",
           serviceName:'ConsiliumSectionSuperManagerReport'
         }, methods: {
           query: function () {
             var unitValue = this.units
             var sectionValue = this.sections
             if (unitValue == '') unitValue = null
             if (sectionValue == '') sectionValue = null
             var json = { unitId: unitValue, sectionId: sectionValue };
             var url = ctx + '/Excel/' +this.serviceName + '/' +  this.serviceName  +'?'
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
