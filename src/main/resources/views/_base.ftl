<html>
    <head>
        <title>
            <#-- TODO fix this, probably with @nested -->
            <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@title/>
        </title>
        <script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script type="text/javascript"
                src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script>
        <script type="text/javascript"
                src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.js"></script>
        <link rel="stylesheet"
              type="text/css"
              href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"/>
        <link rel="stylesheet" type="text/css" href="/styles.css"/>
        <#if stylesheet??>
        <#-- TODO fix this, probably with @nested -->
        <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@stylesheet/>
        </#if>
    </head>
    <body>
        <#-- TODO fix this, probably with @nested -->
        <div id="content">
            <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@content/>
        </div>
        <#include "_footer.ftl">
        <#if script??>
        <#-- TODO fix this, probably with @nested -->
        <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@script/>
        </#if>
    </body>
</html>
