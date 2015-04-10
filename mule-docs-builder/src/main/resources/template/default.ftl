<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="/docs/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/docs/css/bootstrap.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300' rel='stylesheet' type='text/css'>
    <link href="/docs/css/asciidoctor.css" rel="stylesheet">
    <link href="/docs/css/mule-docs.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.min.css">
    <script src="/docs/js/jquery-1.11.2.min.js"></script>
    <script src="/docs/js/bootstrap.js"></script>
    <title>MuleSoft Documentation - ${title}</title>
</head>
<body>
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<div class="navbar navbar-inverse navbar-static-top">
    <div class="navbar-inner">
        <div class="container-fluid" id="top">
            <img src="/docs/img/mule-docs-logo.png">
        </div>
    </div>
</div>
<div class="container" id="main">
    <div class="row">
        <div class="col-md-3">
            <ul class="toc">

                ${toc}

            </ul>

        </div>
        <!--/span-->
        <div class="col-md-9">
            <div id="content">

                ${breadcrumb}

                <div id="header">
                    <h1>${ title }</h1>
                </div>
                <div id="doc-metadata">
                    <!--<span class="updated-date" style="color:#B1B1B1;">Updated: January 15, 2015</span>
                   <span class="updated-date" style="color:#DDD; padding: 0 0 0 7px;">/</span>-->
                    <span class="updated-date" style="color:#B1B1B1; padding: 0 0 0 0px;">Version: </span>
       <span class="dropdown" style="padding: 0 7px 0 0px;"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Current <span class="caret"></span></a>
        <ul class="dropdown-menu" role="menu">
            <li><a href="#">January 2015</a></li>
            <li><a href="#">October 2014</a></li>
            <li><a href="#">July 2014</a></li>
        </ul> </span>
                    <span class="updated-date" style="color:#DDD; padding: 0 0 0 0px;">/</span>
       <span class="dropdown" style="padding: 0 7px 0 7px;"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Export <span class="caret"></span></a>
        <ul class="dropdown-menu" role="menu">
            <li><a href="#">PDF - Only this page</a></li>
            <li><a href="#">PDF - CloudHub section</a></li>
            <li><a href="#">EPub - MuleSoft Docs</a></li>
        </ul> </span>
                    <span class="updated-date" style="color:#DDD; padding: 0 0 0 0px;">/</span>
       <span class="dropdown" style="padding: 0 7px 0 7px;"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Feedback <span class="caret"></span></a>
       <ul class="dropdown-menu" role="menu">
           <li><a href="#">Rate this Topic</a></li>
           <li><a href="#">Edit on GitHub</a></li>
       </ul> </span>
                </div>

                ${content}

            </div>
        </div>
    </div>
</div>
</body>
</html>
