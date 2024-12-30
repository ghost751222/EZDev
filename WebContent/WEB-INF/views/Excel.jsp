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


            <li class="nav-item" data-src="/Excel/Esound">
                                    <button class="nav-link" id="pills-contact-tab" data-bs-toggle="pill"
                                        aria-selected="false">采聲報表</button>
            </li>
            <li class="nav-item" data-src="/Excel/Consilium">
                                    <button class="nav-link" id="pills-contact-tab2" data-bs-toggle="pill"
                                        aria-selected="false">雲端報表</button>
            </li>




            </ul>

            <br /><br />
            <div class="tab-content h-100">
                <div class="tab-pane active show">
                    <iframe id="iframe" width="100%" height="100%" src=""></iframe>
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

            $('#pills-contact-tab2').trigger('click')

        }

    </script>

    </html>