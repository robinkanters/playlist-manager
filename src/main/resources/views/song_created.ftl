<#-- @ftlvariable name="playlist" type="String" -->
<#-- @ftlvariable name="addmoreurl" type="String" -->
<#-- @ftlvariable name="playlisturl" type="String" -->
<#include "_base.ftl">

<#macro title>New Song Added!</#macro>

<#macro content>
    <div>
        <h2>New song added to ${playlist}!</h2>
        <a href="${addmoreurl}">&lt; add more</a>
        <br/>
        <a href="${playlisturl}">&gt; view playlist</a>
    </div>
</#macro>
