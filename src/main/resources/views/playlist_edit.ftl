<#-- @ftlvariable name="playlist" type="no.kanters.playlistmgr.models.Playlist" -->
<#-- @ftlvariable name="getEntryType" type="kotlin.jvm.functions.Function1<no.kanters.playlistmgr.models.PlaylistEntry, String>" -->
<#-- @ftlvariable name="urlencode" type="kotlin.jvm.functions.Function1<String, String>" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#-- @ftlvariable name="newLink" type="String" -->
<#include "_base.ftl">

<#macro title>Editing playlist '${playlist.name}'</#macro>

<#macro content>
    <h2>Editing playlist '${playlist.name}'</h2>
    <div>
        <a href="${backLink}">&lt; back</a> | <a href="${newLink}">add song</a>
        <ul class="playlist-entries">
            <#list playlist.entries as entry>
                <li>
                    <#switch getEntryType.invoke(entry)>
                        <#case 'Shuffle'>
                            &lt;&lt; Shuffle Playlist &gt;&gt;
                            <#break/>
                        <#case 'LineComment'>
                            <span class="comment">${entry.comment}</span>
                            <#break/>
                        <#case 'UriLiteral'>
                        <#--noinspection FtlReferencesInspection-->
                            ${entry.url}
                            <#break/>
                        <#case 'YoutubeSearch'>
                        <#-- @ftlvariable name="entry" type="no.kanters.playlistmgr.models.PlaylistEntry.YoutubeSearch" -->
                            YouTube Search:
                        <#--noinspection FtlReferencesInspection-->
                            "<a href="https://www.youtube.com/results?search_query=${urlencode.invoke(entry.query)}"
                                target="_blank">
                            <#--noinspection FtlReferencesInspection-->
                            ${entry.query}
                        </a>"
                            <#break/>
                    </#switch>
                </li>
            </#list>
        </ul>
    </div>
</#macro>
