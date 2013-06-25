<!--

    Copyright (C) 2012 eXo Platform SAS.

    This is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 2.1 of
    the License, or (at your option) any later version.

    This software is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this software; if not, write to the Free
    Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
    02110-1301 USA, or see the FSF site: http://www.fsf.org.

-->
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>IDE</title>

     <%!
      public String genIdeStaticResourceUrl(HttpServletRequest request, String name) {
        return request.getContextPath() + "/" + request.getAttribute("ws") + "/_app/" + name;
      }
     %>

    <script type="text/javascript" language="javascript">
        var appConfig = {
            "context": "/ide/rest/",
            "websocketContext": "/ide/websocket/"
        }
        var hiddenFiles = ".*";
        var ws = "<%= request.getAttribute("ws")%>";
        var project = <%= request.getAttribute("project") != null ? "\"" + request.getAttribute("project")  + "\"" : null%>;
        var path = <%= request.getAttribute("path") != null ? "\"" + request.getAttribute("path")  + "\"" : null%>;
        var authorizationContext = "/rest";
        var authorizationErrorPageURL = "/ide/ide/error_oauth.html";
        var securityCheckURL = "/ide/j_security_check";
    </script>

    <link rel="shortcut icon" href='<%= genIdeStaticResourceUrl(request, "favicon.ico")%>'/>
    <script type="text/javascript" language="javascript" src='<%= genIdeStaticResourceUrl(request, "_app.nocache.js")%>'></script>
    <link type="text/css" rel="stylesheet" href='<%= genIdeStaticResourceUrl(request, "top-menu.css")%>' media="all"/>
    <link href='<%= genIdeStaticResourceUrl(request, "css/ide.css")%>' media="screen" rel="stylesheet" type="text/css"/>
</head>

<body>

<script type="text/javascript" language="javascript" src='<%= genIdeStaticResourceUrl(request, "browserNotSupported.js")%>'></script>

<div id="ide-menu-additions" align="right" class="ideMenuAdditions">
    <table cellspacing="0" cellpadding="0" border="0"
           class="ideMenuAdditionsTable">
        <tr id="ide-menu-additions-rows">
        </tr>
    </table>
</div>

<script type="text/javascript" language="javascript" src='<%= genIdeStaticResourceUrl(request, "cloud_menu.js")%>'></script>


<script type="text/javascript">
    var uvOptions = {};
    (function () {
        var uv = document.createElement('script');
        uv.type = 'text/javascript';
        uv.async = true;
        uv.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'widget.uservoice.com/jWE2fqGrmh1pa5tszJtZQA.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(uv, s);
    })();
</script>
</body>

</html>
