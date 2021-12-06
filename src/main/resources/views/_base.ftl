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
        <link rel="stylesheet" href="/styles.css"/>
        <#if stylesheet??>
            <#-- TODO fix this, probably with @nested -->
            <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@stylesheet/>
        </#if>
    </head>
    <body>
        <#-- TODO fix this, probably with @nested -->
        <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
        <@content/>
        <#if script??>
            <#-- TODO fix this, probably with @nested -->
            <#--noinspection FtlImportCallInspection,FtlReferencesInspection-->
            <@script/>
        </#if>
    </body>
</html>