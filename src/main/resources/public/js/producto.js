$(document).ready(function() {

    function loadTablePage(table, page, size, baseUrl) {

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
                    var objectData = [];
                    Object.keys(item).forEach(e => objectData.push(`${item[e]}`));
                    if (objectData.length > 0) {
                        var rowAppend = "<tr>";
                        for (var i = 0; i < objectData.length; i++) {
                            rowAppend = rowAppend.concat("<td>" + objectData[i] + "</td>");
                        }
                        rowAppend = rowAppend.concat("<tr/>");
                        $(table).append(rowAppend);
                    }
                });
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        });
    }

    function setupTable(table, size, baseUrl) {

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

        loadTablePage(table, 0, size, baseUrl);
    }

    function reloadTable(table) {
        var currentPage = table.attr("page");
        var size = table.attr("size");
        var baseUrl = table.attr("url");
        loadTablePage(table, parseInt(currentPage), parseInt(size), baseUrl);
    }

    setupTable($("#productoTable"), 10, "/api/producto");

    const modalProducto = $("#modalProducto");
    const modalEditarProducto = $("#modalEditarProducto");
    const modalEntradaProducto = $("#modalEntradaProducto");
    const modalSalidaProducto = $("#modalSalidaProducto");

    modalProducto.find(".button-cancel").on("click", function() {
        modalProducto.find("input").val("");
    });

    modalProducto.find("form").on("submit", function() {

        var form = modalProducto.find("form")
        var formData = {};
        $(form)
            .serializeArray()
            .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "POST",
            url: "/api/producto",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                modalProducto.modal('toggle');
                reloadTable($("#productoTable"));
                modalProducto.find("input").val("");
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })

        return false;
    });

    $("#productoTable").on("click", "tr:not(':has(th)')", function() {
        if ($(this).attr("selected") !== undefined) {
            $(this).removeAttr("selected");
            $(this).removeClass("table-dark");
        } else {
            $("#productoTable").find("tr").removeAttr("selected");
            $("#productoTable").find("tr").removeClass("table-dark");
            $(this).addClass("table-dark");
            $(this).attr("selected", true);
        }
    });

    $("#btnEliminarProducto").on("click", function() {
        var selectedTr = $("#productoTable").find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un producto");
            return;
        }

        var id = selectedTr.children(":first").html();

        var json = JSON.stringify({
            "id": id
        });

        $.ajax({
            type: "DELETE",
            url: "/api/producto",
            contentType: 'application/json; charset=utf-8',
            data: json,
            success: function(data) {
                reloadTable($("#productoTable"));
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })

    });

    $("#btnEditarProducto").on("click", function() {
        var selectedTr = $("#productoTable").find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un producto");
            return;
        }

        var id = selectedTr.children().get(0).innerHTML;
        var nombre = selectedTr.children().get(1).innerHTML;
        var sku = selectedTr.children().get(2).innerHTML;

        modalEditarProducto.find("input[name='id']").val(id);
        modalEditarProducto.find("input[name='nombre']").val(nombre);
        modalEditarProducto.find("input[name='sku']").val(sku);

        modalEditarProducto.modal('toggle');
    });

    modalEditarProducto.find(".button-cancel").on("click", function() {
        modalProducto.find("input").val("");
    });

    modalEditarProducto.find("form").on("submit", function() {

        var form = modalEditarProducto.find("form")
        var formData = {};
        $(form)
            .serializeArray()
            .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "PUT",
            url: "/api/producto",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                modalEditarProducto.modal('toggle');
                reloadTable($("#productoTable"));
                modalEditarProducto.find("input").val("");
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })

        return false;
    });

    $("#btnEntradaProducto").on("click", function() {
        var selectedTr = $("#productoTable").find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un producto");
            return;
        }

        var id = selectedTr.children().get(0).innerHTML;
        var nombre = selectedTr.children().get(1).innerHTML;
        var sku = selectedTr.children().get(2).innerHTML;

        modalEntradaProducto.find("input[name='id']").val(id);

        modalEntradaProducto.modal('toggle');
    });

    modalEntradaProducto.find(".button-cancel").on("click", function() {
        modalEntradaProducto.find("input").val("");
    });

    modalEntradaProducto.find("form").on("submit", function() {

        var form = modalEntradaProducto.find("form")
        var formData = {};
        $(form)
            .serializeArray()
            .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "POST",
            url: "/api/kardex/registrarEntrada",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                modalEntradaProducto.modal('toggle');
                reloadTable($("#productoTable"));
                modalEntradaProducto.find("input").val("");
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
        return false;
    });



    $("#btnSalidaProducto").on("click", function() {
        var selectedTr = $("#productoTable").find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un producto");
            return;
        }

        var id = selectedTr.children().get(0).innerHTML;
        var nombre = selectedTr.children().get(1).innerHTML;
        var sku = selectedTr.children().get(2).innerHTML;

        modalSalidaProducto.find("input[name='id']").val(id);

        modalSalidaProducto.modal('toggle');
    });

    modalSalidaProducto.find(".button-cancel").on("click", function() {
        modalSalidaProducto.find("input").val("");
    });

    modalSalidaProducto.find("form").on("submit", function() {

        var form = modalSalidaProducto.find("form")
        var formData = {};
        $(form)
            .serializeArray()
            .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "POST",
            url: "/api/kardex/registrarSalida",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                modalSalidaProducto.modal('toggle');
                reloadTable($("#productoTable"));
                modalSalidaProducto.find("input").val("");
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
        return false;
    });


    $("#btnVerKardex").on("click", function() {
        var selectedTr = $("#productoTable").find("tr[selected='selected']");

        if (selectedTr.length == 0) {
            alert("No se ha seleccionado un producto");
            return;
        }

        var id = selectedTr.children().get(0).innerHTML;

        window.location.href = '/kardex?idProducto=' + id;
    });
});