$(document).ready(function() {

    function loadTablePage(table, page, size, baseUrl, rowBuildFunction) {

        var tablePagination = table.parent().parent().parent().find(".table-pagination");

        $.ajax(baseUrl + '?page=' + page + '&size=' + size, {
            type: 'GET',
            success: function(data, status, xhr) {

                table.find('tr:not(:first)').remove();
                tablePagination.find(".table-pagination-number").remove();
                table.attr("page", page);

                if (page < data.totalPages - 1) {
                    tablePagination.find("ul li:last-child").removeClass("disabled");
                } else {
                    tablePagination.find("ul li:last-child").addClass("disabled");
                }

                if (page == 0) {
                    tablePagination.find("ul li:first-child").addClass("disabled");
                } else {
                    tablePagination.find("ul li:first-child").removeClass("disabled");
                }

                for (var i = 0; i < data.totalPages; i++) {
                    tablePagination.find("ul li:eq(" + i + ")")
                        .after("<li class='page-item table-pagination-number'><a class='page-link' href='#'>" + (i + 1) + "</a></li>");
                }

                tablePagination.find("ul li").removeClass("active");
                tablePagination.find("ul li:eq(" + (page + 1) + ")").addClass("active");

                data.elements.forEach(function(item, index) {
                     var rowAppend = rowBuildFunction(item, index);
                     $(table).append(rowAppend);
                });
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        });
    }

    function setupTable(table, size, baseUrl, rowBuildFunction) {

        var tablePagination = table.parent().parent().parent().find(".table-pagination");

        tablePagination.on('click', '.table-pagination-number a', function() {
            loadTablePage(table, $(this).html() - 1, size, baseUrl);
        });

        tablePagination.on('click', '.page-link.prev', function() {
            var currentPage = table.attr("page");
            loadTablePage(table, parseInt(currentPage) - 1, size, baseUrl);
        });
        tablePagination.on('click', '.page-link.next', function() {
            var currentPage = table.attr("page");
            loadTablePage(table, parseInt(currentPage) + 1, size, baseUrl);
        });

        table.attr("page", 0);
        table.attr("size", size);
        table.attr("url", baseUrl);

        loadTablePage(table, 0, size, baseUrl, rowBuildFunction);
    }

    function reloadTable(table, rowBuildFunction) {
        var currentPage = table.attr("page");
        var size = table.attr("size");
        var baseUrl = table.attr("url");
        loadTablePage(table, parseInt(currentPage), parseInt(size), baseUrl, rowBuildFunction);
    }

    function tableRowSelectable(table){
        table.on("click", "tr:not(':has(th)')", function() {
            if ($(this).attr("selected") !== undefined) {
                $(this).removeAttr("selected");
                $(this).removeClass("table-dark");
            } else {
                $(table).find("tr").removeAttr("selected");
                $(table).find("tr").removeClass("table-dark");
                $(this).addClass("table-dark");
                $(this).attr("selected", true);
            }
        });
    }

    function detectInputUpdateForm(form){
        form.find("input").bind('input', function() {
            $(this).attr("updated", true);
        });

        form.find("select").bind('input', function() {
            $(this).attr("updated", true);
        });
    }

    function resetInputUpdateForm(form){
        form.find("input").removeAttr("updated");
        form.find("select").removeAttr("updated");
    }

    function rowBuildFunction(item, index){
        return "<tr><td>"+item.nombre+"</td><td>"+item.rol
        +"</td><td>"+item.email+"</td><td>"+item.telefono+"</td></tr>";
    }

    var modalUsuario = $("#modalUsuario");
    var modalEditarUsuario = $("#modalEditarUsuario");
    var usuarioTable = $("#usuarioTable");

    setupTable(usuarioTable, 10, "/api/usuario", rowBuildFunction);

    tableRowSelectable(usuarioTable);

    $("#btnNuevoUsuario").on("click", function(){
        modalUsuario.modal("toggle");
    });

    modalUsuario.find(".button-cancel").on("click", function(){
        modalUsuario.find("input").val("");
    });

    modalUsuario.find("form").on("submit", function(){

        if($("#usuarioPassword").val()!==$("#usuarioPasswordConfirmar").val()){
            alert("Las contraseñas no coinciden");
            return;
        }

        var form = modalUsuario.find("form")
        var formData = {};
        $(form).serializeArray()
        .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "POST",
            url: "/api/usuario",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                reloadTable(usuarioTable, rowBuildFunction);
                modalUsuario.modal("toggle");
                modalUsuario.find("input").val("");
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
        return false;
    });


    detectInputUpdateForm(modalEditarUsuario.find("form"));

    $("#btnEditarUsuario").on("click", function(){

        var selectedTr = usuarioTable.find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un usuario");
            return;
        }

        var tds = selectedTr.find("td");

        modalEditarUsuario.find("input[name='nombre']").val(tds[0].innerHTML);
        modalEditarUsuario.find("select[name='rol']").val(tds[1].innerHTML);
        modalEditarUsuario.find("input[name='email']").val(tds[2].innerHTML);
        modalEditarUsuario.find("input[name='telefono']").val(tds[3].innerHTML);

        modalEditarUsuario.modal("toggle");
    });

    modalEditarUsuario.find(".button-cancel").on("click", function(){
        modalEditarUsuario.find("input").val("");
        resetInputUpdateForm(modalEditarUsuario.find("form"));
    });

    modalEditarUsuario.find("form").on("submit", function(){

        if($("#usuarioEditarPassword").val()!==$("#usuarioEditarPasswordConfirmar").val()){
            alert("Las contraseñas no coinciden");
            return;
        }

        var form = modalEditarUsuario.find("form input[name='nombre'], form input[updated='true'], form select[updated='true']");
        var formData = {};
        $(form).serializeArray()
        .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "PATCH",
            url: "/api/usuario",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                reloadTable(usuarioTable, rowBuildFunction);
                modalEditarUsuario.modal("toggle");
                modalEditarUsuario.find("input").val("");
                resetInputUpdateForm(modalEditarUsuario.find("form"));
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
        return false;
    });


     $("#btnEliminarUsuario").on("click", function(){
        var selectedTr = usuarioTable.find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un usuario");
            return;
        }

        var nombre= selectedTr.children(":first").html();

        var json = JSON.stringify({
            "nombre": nombre
        });

        $.ajax({
            type: "DELETE",
            url: "/api/usuario",
            contentType: 'application/json; charset=utf-8',
            data: json,
            success: function(data) {
                reloadTable(usuarioTable, rowBuildFunction);
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
     });
});