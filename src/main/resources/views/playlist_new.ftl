<#-- @ftlvariable name="error" type="String" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#-- @ftlvariable name="createLink" type="String" -->
<#include "_base.ftl">

<#macro title>New playlist</#macro>

<#macro content>
    <div>
        <h2>Create a playlist</h2>
        <#if error??>
            <span class="error">${error}</span>
        </#if>
        <form action="${createLink}" method="post" class="ui form">
            <div class="field">
                <label for="name">Name:</label>
                <input type="text" name="name" id="name"/>
            </div>
            <a href="${backLink}" class="ui labeled icon button">
                <i class="left arrow icon"></i>
                cancel
            </a>
            <button type="submit" class="ui right labeled icon primary button">
                <i class="icon attached arrow right"></i>
                create
            </button>
        </form>
    </div>
</#macro>
