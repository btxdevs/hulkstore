$(document).ready(function() {

    var baseUrl = "/api/kardex";
    var kardexTable = $("#kardexTable");
    var filter = "producto.id%3A%27"+kardexTable.attr("id-producto")+"%27"

    function loadTablePage(table, page, size, baseUrl) {

        var tablePagination = table.parent().parent().parent().find(".table-pagination");

        $.ajax(baseUrl + '?page=' + page + '&size=' + size + '&filter=' + filter, {
            type: 'GET',
            success: function(data, status, xhr) {

                table.find('tr').not(":nth-child(1)").not(":nth-child(2)").remove();
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

                    var fechaHora = new Date(item.fechaHora).toLocaleString();
                    var detalle = item.detalle;
                    var productoNombre = item.producto.nombre;
                    var productoSku = item.producto.sku;
                    var entradaCantidad = "";
                    var entradaValorUnitario = "";
                    var entradaValorTotal = "";
                    var salidaCantidad = "";
                    var salidaValorUnitario = "";
                    var salidaValorTotal = "";
                    if(item.entrada!=null){
                        var entradaCantidad = item.entrada.cantidad;
                        var entradaValorUnitario = item.entrada.valorUnitario;
                        var entradaValorTotal = item.entrada.valorTotal;
                    }
                    if(item.salida!=null){
                        var salidaCantidad = item.salida.cantidad;
                        var salidaValorUnitario = item.salida.valorUnitario;
                        var salidaValorTotal = item.salida.valorTotal;
                    }
                    var saldoCantidad = item.saldo.cantidad;
                    var saldoValorUnitario = item.saldo.valorUnitario;
                    var saldoValorTotal = item.saldo.valorTotal;

                    var rowAppend = "<tr><td>"+fechaHora+"</td><td>"+detalle+"</td><td>"+productoNombre+"</td><td>"
                    +productoSku+"</td><td>"+entradaCantidad+"</td><td>"+entradaValorUnitario
                    +"</td><td>"+entradaValorTotal+"</td><td>"+salidaCantidad+"</td><td>"
                    +salidaValorUnitario+"</td><td>"+salidaValorTotal+"</td><td>"+saldoCantidad
                    +"</td><td>"+saldoValorUnitario+"</td><td>"+saldoValorTotal+"</td></tr>";

                    $(table).append(rowAppend);
                });
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        });
    }

    function setupTable(table, size) {

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

    setupTable(kardexTable, 10);
});