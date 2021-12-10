<#-- @ftlvariable name="playlist" type="no.kanters.playlistmgr.models.Playlist" -->
<#-- @ftlvariable name="getEntryType" type="kotlin.jvm.functions.Function1<no.kanters.playlistmgr.models.PlaylistEntry, String>" -->
<#-- @ftlvariable name="yturl" type="kotlin.jvm.functions.Function1<String, String>" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#-- @ftlvariable name="newLink" type="String" -->
<#include "_base.ftl">

<#macro title>Editing playlist '${playlist.name}'</#macro>

<#macro content>
    <h2>Editing playlist '${playlist.name}'</h2>
    <div>
        <a href="${backLink}" class="ui labeled icon button">
            <i class="left arrow icon"></i>
            back
        </a>
        <a href="${newLink}" class="ui primary right labeled icon button">
            <i class="plus icon"></i>
            add song
        </a>
        <ul class="playlist-entries ui bulleted list">
            <#list playlist.entries as entry>
                <li>
                    <#if entry.comment?? && entry.comment?length &gt; 0 && getEntryType.invoke(entry) != 'LineComment'>
                        <span class="comment">${entry.comment}</span>
                    </#if>
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
                            YouTube Search:
                        <#--noinspection FtlReferencesInspection-->
                            "<a href="${yturl.invoke(entry.query)}" target="_blank">${entry.query}</a>"
                            <#break/>
                    </#switch>
                </li>
            </#list>
        </ul>
    </div>
</#macro>
