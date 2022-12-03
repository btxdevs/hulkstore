$(document).ready(function() {

    $("form").on("submit", function(e) {

        e.preventDefault();

        var formData = {};
        $(this).serializeArray()
        .map(input => formData[input.name] = input.value);

        var json = JSON.stringify(formData);

        $.ajax({
            type: "POST",
            url: "/api/auth/login",
            contentType: 'application/json; charset=utf-8',
            data: json,
            success: function(data) {
                window.location.href = "/shop";
            },
            error: function(jqXhr, textStatus, errorMessage) {
                alert(jqXhr.responseJSON.message);
            }
        })
    });

});