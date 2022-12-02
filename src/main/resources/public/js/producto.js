$(document).ready(function() {
    setupTable($("#productoTable"), 10, "/api/producto");

    const modalProducto = $("#modalProducto");
    const modalEditarProducto = $("#modalEditarProducto");

    modalProducto.find(".button-cancel").on("click", function() {
        modalProducto.find("input").val("");
    });

    modalProducto.find(".button-ok").on("click", function() {

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
    });

    $("#productoTable").on("click", "tr", function() {
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

    modalEditarProducto.find(".button-ok").on("click", function() {

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
    });
});