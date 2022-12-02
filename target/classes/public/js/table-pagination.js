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