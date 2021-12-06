<#-- @ftlvariable name="input" type="no.kanters.playlistmgr.controllers.PlaylistSongController.NewPayload" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#include "_base.ftl">

<#macro title>New Song in '${input.playlistName}'</#macro>

<#macro script>
    <script type="text/javascript">
        ~(function ($) {
            $(() => {
                const queryElements = $("#queryfields");
                const $shuffle = $("#shuffle");
                $("#form_song_new").validate({
                    errorClass: "error fail-alert",
                    validClass: "valid success-alert",
                    rules: {
                        query: {
                            minlength: 3,
                            required: {
                                depends: () => !$shuffle.is(':checked')
                            }
                        }
                    }
                });
                $("#form_song_new input[type='radio']").change(e => {
                    $("#querylabel").text($(e.target).data("prettytype"));
                    queryElements.stop().show('slow');
                });
                $shuffle.change(() => {
                    queryElements.stop().hide('slow');
                })
            })
        })(jQuery);
    </script>
</#macro>

<#macro stylesheet>
    <style type="text/css">
        <#--noinspection CssUnusedSymbol-->
        label.error.fail-alert {
            border: 2px solid red;
            border-radius: 4px;
            line-height: 1;
            padding: 2px 0 6px 6px;
            background: #ffe6eb;
        }

        <#--noinspection CssUnusedSymbol-->
        input.valid.success-alert {
            border: 2px solid #4CAF50;
            color: green;
        }

        form {
            margin: 15px 0;
        }
    </style>
</#macro>

<#macro content>
    <div>
        <h2>New song in '${input.playlistName}'</h2>
        <a href="${backLink}">&lt; back</a>
        <div>
            <form method="post" id="form_song_new">
                <div style="display: block">
                    <input data-prettytype="URL" type="radio" name="type" value="url" id="url" checked/>
                    <label for="url">URL</label>
                    <br/>
                    <input data-prettytype="Query" type="radio" name="type" value="ytsearch" id="ytsearch"/>
                    <label for="ytsearch">YouTube Query</label>
                    <br/>
                    <input type="radio" name="type" value="shuffle" id="shuffle"/>
                    <label for="shuffle">Shuffle Playlist</label>
                </div>
                <div style="display: block" id="queryfields">
                    <label for="query" id="querylabel">Query</label>
                    <br/>
                    <input type="text" name="query" id="query"/>
                </div>
                <div style="display: block">
                    <label for="comment">Comment (optional)</label>
                    <br/>
                    <input class="mandatory" type="text" name="comment" id="comment"/>
                </div>
                <input type="submit" value="Create!"/>
            </form>
        </div>
    </div>
</#macro>
