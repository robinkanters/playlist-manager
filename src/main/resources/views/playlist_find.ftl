<#-- @ftlvariable name="playlists" type="String[]" -->
<#-- @ftlvariable name="newLink" type="String" -->
<#-- @ftlvariable name="link" type="kotlin.jvm.functions.Function1<String, String>" -->
<#include "_base.ftl">

<#macro title>Listing playlists</#macro>

<#macro content>
    <h2>Playlists</h2>
    <a href="/playlists/new" class="ui primary right labeled icon button">
        <i class="plus icon"></i>
        new
    </a>
    <table class="playlists ui celled table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <#list playlists as pl>
                <tr>
                    <td>${pl}</td>
                    <td>
                        <a href="${link.invoke(pl)}" class="ui mini button yellow">
                            edit
                        </a>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
</#macro>
