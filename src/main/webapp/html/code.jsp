<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%String path = request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html>

<head>
    <title>字典编辑</title>
    <meta name="content-type" content="text/html; charset=UTF-8">

    <script src="<%=path %>js/jquery.min.js"></script>
</head>

<body>
<div>
    <p>当前节点：
        id:<input type="text" id="nowId" readonly/>
        code:<input type="text" id="nowCode"/>
        value:<input type="text" id="nowValue"/>
        分组:<input type="text" id="nowGroup"/>
        排序:<input type="text" id="nowSqeuence"/>
        启用:<select id="nowEnable">
            <option value="1">是</option>
            <option value="0">否</option>
        </select>
        <button id="updateBut" onclick="update()">修改</button>
        <button id="delBut" onclick="del()">删除</button>
    </p>
    <p>兄弟节点：
        id:<input type="text" id="nextId"/>
        code:<input type="text" id="nextCode"/>
        value:<input type="text" id="nextValue"/>
        分组:<input type="text" id="nextGroup"/>
        排序:<input type="text" id="nextSqeuence"/>
        启用:<select id="nextEnable">
            <option value="1">是</option>
            <option value="0">否</option>
        </select>
        <button id="newNext" onclick="newNext()">新增</button>
    </p>
    <p>&nbsp;子节点：
        id:<input type="text" id="childId"/>
        code:<input type="text" id="childCode"/>
        value:<input type="text" id="childValue"/>
        分组:<input type="text" id="childGroup"/>
        排序:<input type="text" id="childSqeuence"/>
        启用:<select id="childEnable">
            <option value="1">是</option>
            <option value="0">否</option>
        </select>
        <button id="newChild" onclick="newChild()">新增</button>
    </p>
</div>
<p id="codeList"></p>

<script type="text/javascript">

    $(function () {
        showChild("", 0);
    });

    function showChild(obj, parentId, clickDomId) {
        $.ajax({
            type: "get",
            dataType: "json",
            contentType: "application/json",
            url: "<%=path %>maSysCode/" + parentId,
            success: function (now) {
                if (now.status != "success") {
                    alert(now.data);
                    return;
                }

                var $parentDom = $(obj).closest("div");
                if (obj == "" || typeof(obj) == "undefined") {
                    $("#codeList").children().remove();
                } else {
                    $parentDom.nextAll().remove();
                }

                if (now.data != "") {
                    $("#nowId").val(now.data.id);
                    $("#nowCode").val(now.data.code);
                    $("#nowValue").val(now.data.value);
                    $("#nowGroup").val(now.data.groupId);
                    $("#nowSqeuence").val(now.data.sqeuence);
                    $("#nowEnable").find("option[text='" + now.data.enable + "']").attr("selected", true);
                }

                var nowMaxId = 0;
                var nowMaxSqeuence = 0;
                var $aDom = $parentDom.find("a");
                for (var i = 0; i < $aDom.length; i++) {
                    var nowId = Number($($aDom[i]).attr("id"));
                    if (nowMaxId < nowId) {
                        nowMaxId = nowId;
                    }
                    var nowSqeuence = Number($($aDom[i]).attr("sqeuence"));
                    if (nowMaxSqeuence < nowSqeuence) {
                        nowMaxSqeuence = nowSqeuence;
                    }
                }
                $("#nextId").val(nowMaxId + 1);
                $("#nextGroup").val(now.data.groupId);
                $("#nextSqeuence").val(nowMaxSqeuence + 1);

                $.ajax({
                    type: "get",
                    dataType: "json",
                    contentType: "application/json",
                    url: "<%=path %>maSysCode?parentId=" + parentId,
                    success: function (childData) {
                        if (now.status != "success") {
                            alert(now.data);
                            return;
                        }

                        var code = eval(childData.data);

                        var childMaxId = 0;
                        var childMaxSqeuence = 0;
                        if (code.length > 0) {
                            $("#childGroup").val(code[0]["groupId"]);

                            //导航条
                            var parentBar = $("#" + $("#" + parentId).attr("parentid") + "_bar").text();	//父级导航
                            if (parentBar == ">") {
                                parentBar = "";
                            }
                            var barDom = "<font id='" + parentId + "_bar'>" + parentBar + ">" + $("#" + parentId).text() + "</font>";

                            var dom = "<div>" + barDom + "<ul>";
                            for (var i = 0; i < code.length; i++) {
                                var childNowId = Number(code[i]["id"]);
                                if (childMaxId < childNowId) {
                                    childMaxId = childNowId;
                                }
                                var childNowSqeuence = Number(code[i]["sqeuence"]);
                                if (childMaxSqeuence < childNowSqeuence) {
                                    childMaxSqeuence = childNowSqeuence;
                                }

                                dom += "<li><a href='#' onclick='showChild(this,\"" + code[i]["id"] + "\")' id='" + code[i]["id"] + "' parentid='" + parentId + "' sqeuence='" + code[i]["sqeuence"] + "'>" + code[i]["value"] + "</a></li>";
                            }
                            dom += "</ul></div>"

                            $("#codeList").append(dom);
                        } else {
                            $("#childGroup").val(parentId);
                        }

                        if (childMaxId == 0) {
                            childMaxId = Number(String(parentId) + "01");
                        } else {
                            childMaxId += 1;
                        }
                        $("#childId").val(childMaxId);
                        $("#childSqeuence").val(childMaxSqeuence + 1);

                        if (typeof(clickDomId) != "undefined" && clickDomId != "") {
                            $("#" + clickDomId)[0].onclick();
                        } else if (parentId == 0) {
                            var codeItem = $("#codeList").find("a");
                            if (codeItem.length > 0) {
                                codeItem[0].onclick();
                            }
                        }
                    },
                    error: function (data) {
                        alert(data.responseJSON.data);
                    }
                });

            },
            error: function (data) {
                alert(data.responseJSON.data);
            }
        });
    }

    //更新当前节点
    function update() {
        if ($("#nowId").val() == "") {
            alert("请选择节点！");
            return;
        }
        if ($("#nowValue").val() == "") {
            alert("请填写value！");
            return;
        }
        if ($("#nowSqeuence").val() == "") {
            alert("请填写排序！");
            return;
        }

        var params = {
            "id": $("#nowId").val(),
            "code": $("#nowCode").val(),
            "value": $("#nowValue").val(),
            "groupId": $("#nowGroup").val(),
            "sqeuence": $("#nowSqeuence").val(),
            "enable": $("#nowEnable").val()
        }

        //更新
        $.ajax({
            type: "put",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(params),
            url: "<%=path %>maSysCode",
            success: function (data) {
                if (data.status == "success") {
                    alert("修改成功！");

                    //重新查询当前列表
                    var $nowDom = $("#" + $("#nowId").val());
                    showChild($("#" + $nowDom.attr("parentid"))[0], $nowDom.attr("parentid"), $("#nowId").val());
                } else {
                    alert(data.data);
                }
            },
            error: function (data) {
                alert(data.responseJSON.data);
            }
        });
    }

    //删除当前结点
    function del() {
        if ($("#nowId").val() == "") {
            alert("请选择节点！");
            return;
        }

        //删除
        $.ajax({
            type: "delete",
            dataType: "json",
            contentType: "application/json",
            url: "<%=path %>maSysCode/" + $("#nowId").val(),
            success: function (data) {
                if (data.status == "success") {
                    alert("删除成功！");

                    //重新查询当前列表
                    var $nowDom = $("#" + $("#nowId").val());
                    showChild($("#" + $nowDom.attr("parentid"))[0], $nowDom.attr("parentid"));
                } else {
                    alert(data.data);
                }
            },
            error: function (data) {
                alert(data.responseJSON.data);
            }
        });
    }

    //新增当前兄弟节点
    function newNext() {
        if ($("#nextId").val() == "") {
            alert("请填写id！");
            return;
        }
        if ($("#nextValue").val() == "") {
            alert("请填写value！");
            return;
        }
        if ($("#nextSqeuence").val() == "") {
            alert("请填写排序！");
            return;
        }

        var params = {
            "id": $("#nextId").val(),
            "parentId": $("#" + $("#nowId").val()).attr("parentid"),
            "code": $("#nextCode").val(),
            "value": $("#nextValue").val(),
            "groupId": $("#nextGroup").val(),
            "sqeuence": $("#nextSqeuence").val(),
            "enable": $("#nextEnable").val()
        }

        //新增
        $.ajax({
            type: "post",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(params),
            url: "<%=path %>maSysCode",
            success: function (data) {
                if (data.status == "success") {
                    alert("添加成功！");

                    //重新查询当前列表
                    var $nowDom = $("#" + $("#nowId").val());
                    showChild($("#" + $nowDom.attr("parentid"))[0], $nowDom.attr("parentid"));
                } else {
                    alert(data.data);
                }
            },
            error: function (data) {
                alert(data.responseJSON.data);
            }
        });
    }

    //新增子节点
    function newChild() {
        if ($("#childId").val() == "") {
            alert("请填写id！");
            return;
        }
        if ($("#childValue").val() == "") {
            alert("请填写value！");
            return;
        }
        if ($("#childSqeuence").val() == "") {
            alert("请填写排序！");
            return;
        }

        var params = {
            "id": $("#childId").val(),
            "parentId": $("#nowId").val(),
            "code": $("#childCode").val(),
            "value": $("#childValue").val(),
            "groupId": $("#childGroup").val(),
            "sqeuence": $("#childSqeuence").val(),
            "enable": $("#childEnable").val()
        }

        //新增
        $.ajax({
            type: "post",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(params),
            url: "<%=path %>maSysCode",
            success: function (data) {
                if (data.status == "success") {
                    alert("添加成功！");

                    //重新查询子节点列表
                    showChild($("#" + $("#nowId").val())[0], $("#nowId").val());
                } else {
                    alert(data.data);
                }
            },
            error: function (data) {
                alert(data.responseJSON.data);
            }
        });
    }
</script>
</body>
</html>
