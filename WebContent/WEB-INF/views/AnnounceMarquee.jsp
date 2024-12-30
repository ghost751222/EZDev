<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>

    <head>
        <title>公告訊息跑馬燈</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.1.3/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" ></script>

        <script src="../resources/js/vue.js"></script>
        <style>
            .form-group {
                margin-bottom: 1.5em;
            }
        </style>
    </head>

    <body>
      <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
        <!--<div class="carousel-indicators">
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
          <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>-->
        <div class="carousel-inner">
            <div class="carousel-item" v-for="(r,index) in records" :class="[index==0 ? 'active':'']">
                <div class="p-3 mb-3 text-white" :class="[r.importance == '1' ? 'bg-danger' : 'bg-info']" style="text-align: center;">
                    [重要公告訊息]<br/>
                    {{r.subject}}
                </div>
            </div>



        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Next</span>
        </button>
      </div>

    </body>
    <script type="text/javascript">
        var ctx = "${pageContext.request.contextPath}"
var myCarousel = document.querySelector('#carouselExampleIndicators')
var carousel = new bootstrap.Carousel(myCarousel, {
  interval: 2000
})


        var app = new Vue({
            el: '#carouselExampleIndicators',
            data: {
                records:[]
            },
            methods: {
                findAllEfficient() {
                    var method = 'get'
                    var url = ctx + '/Announce/findAllEfficient'
                    var response = this.callAjax(method, url)
                    this.records = response;
                    console.log(response)

                },
                callAjax(method, url, requestData) {
                    var response;
                    var $ajax = $.ajax({
                        method: method,
                        url: url,
                        contentType: 'application/json',
                        async: false,
                        data: requestData,
                        success: function (res) {
                            response = res;
                        }, error: function (err) {
                            console.log(err)
                            alert('發生異常錯誤')
                        }

                    });
                    return response
                },

            }, mounted: function () {
                this.findAllEfficient()
                var timeoutID = window.setInterval(this.findAllEfficient, 600*1000);
            }
        })
    </script>

    </html>