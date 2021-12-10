<#-- @ftlvariable name="input" type="no.kanters.playlistmgr.controllers.PlaylistSongController.NewPayload" -->
<#-- @ftlvariable name="backLink" type="String" -->
<#include "_base.ftl">

<#macro title>New Song in '${input.playlistName}'</#macro>

<#macro script>
    <script type="text/javascript">
        ~(function ($) {
            $(() => {
                const $form = $("#form_song_new");
                const $queryElements = $("#queryfields");
                const testLink = $("#testlink");
                const $shuffle = $("#shuffle");
                const $urlField = $("#url");
                const $radioButtons = $("#form_song_new input[type='radio']");
                const $queryField = $("#query");

                const validationRules = {
                    query: {
                        identifier: "query",
                        rules: [
                            {
                                type: 'empty', prompt: 'You must provide a search'
                            },
                            {
                                type: 'minLength[3]', prompt: 'Your search must be at least 3 characters'
                            },
                        ],
                    }
                }

                function addQueryValidation() {
                    $form
                        .form({
                            fields: validationRules,
                            inline: true,
                            on: 'blur'
                        });
                }

                addQueryValidation();
                $radioButtons.change(e => {
                    $("#querylabel").text($(e.target).data("prettytype"));
                    $queryElements.stop().show("slow");
                    addQueryValidation();
                });
                $shuffle.change(() => {
                    $queryElements.stop().hide("slow");
                    $form.form("remove fields", ["query"]);
                })
                $radioButtons.change(updateTestLink);
                $queryField.keyup(updateTestLink);

                function updateTestLink() {
                    const link = $queryField.val().length === 0 ? null :
                        $urlField.prop("checked") && /^https?:\/\//.test($queryField.val()) ? $queryField.val()
                            : $("#ytsearch").prop("checked") ? "https://www.youtube.com/results?search_query=" + encodeURIComponent($queryField.val())
                            : null;
                    testLink.attr('href', link);
                    if (!link) testLink.addClass("disabled");
                    else testLink.removeClass("disabled");
                }

                updateTestLink();
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
        <a href="${backLink}" class="ui mini red labeled icon button">
            <i class="x icon"></i>
            abort
        </a>
        <div>
            <form method="post" id="form_song_new" class="ui form">
                <div class="field">
                    <div class="ui radio checkbox">
                        <input data-prettytype="URL" type="radio" name="type" value="url" id="url" checked/>
                        <label for="url">URL</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input data-prettytype="Query" type="radio" name="type" value="ytsearch" id="ytsearch"/>
                        <label for="ytsearch">YouTube Query</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input type="radio" name="type" value="shuffle" id="shuffle"/>
                        <label for="shuffle">Shuffle Playlist</label>
                    </div>
                </div>
                <div class="field" id="queryfields">
                    <label for="query" id="querylabel">Query</label>
                    <div class="ui right labeled input">
                        <input type="text" name="query" id="query" class="ui input"/>
                        <a href="#" class="ui label green icon right labeled button" id="testlink" target="_blank">
                            Test
                            <i class="rocket icon"></i>
                        </a>
                    </div>
                </div>
                <div class="field">
                    <label for="comment">Comment (optional)</label>
                    <div class="ui labeled input">
                        <input class="mandatory" type="text" name="comment" id="comment"/>
                    </div>
                </div>
                <div class="field">
                    <input type="submit" value="create" class="ui button primary"/>
                </div>
            </form>
        </div>
    </div>
</#macro>
