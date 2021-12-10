<#-- @ftlvariable name="playlist" type="String" -->
<#-- @ftlvariable name="addmoreurl" type="String" -->
<#-- @ftlvariable name="playlisturl" type="String" -->
<#include "_base.ftl">

<#macro title>New Song Added!</#macro>

<#macro content>
    <div>
        <h2>New song added to ${playlist}!</h2>
        <a href="${addmoreurl}" class="ui labeled icon button">
            <i class="plus icon"></i> add more
        </a>
        <br/>
        <br/>
        <a href="${playlisturl}" class="ui right labeled icon button">
            view playlist
            <i class="right arrow icon"></i>
        </a>
    </div>
</#macro>
