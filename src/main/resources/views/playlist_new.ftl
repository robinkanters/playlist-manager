<#-- @ftlvariable name="error" type="String" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#-- @ftlvariable name="createLink" type="String" -->
<#include "_base.ftl">

<#macro title>New playlist</#macro>

<#macro content>
    <div>
        <h2>Create a playlist</h2>
        <a href="${backLink}">&lt; back</a>
        <#if error??>
            <span class="error">${error}</span>
        </#if>
        <form action="${createLink}" method="post"><label for="name">Name:</label><br><input type="text"
                                                                                             name="name"><br><input
                    type="submit" value="Create!"></form>
    </div>
</#macro>