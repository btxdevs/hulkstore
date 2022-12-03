$(document).ready(function() {

    var productosContainer = $("#productos-container");
    var productoCardBase = $(".producto-card-base");
    var productosValorTotal = $("#productos-valor-total");
    var productoCartRemove = $(".producto-cart-remove");
    var cartTotal = $("#cart-total");
    var checkoutForm = $("#checkout-form");

    function loadProductos(){
            $.ajax({
                type: "GET",
                url: "/api/cart",
                dataType: 'json',
                success: function(data) {
                    populateProductos(data);
                },
                error: function(jqXhr, textStatus, errorMessage) {
                    alert(jqXhr.responseJSON.message);
                }
            });
    }

    function populateProductos(data){

                    productosContainer.children().not(':first').remove();
                    data.productos.forEach(function(item, index) {
                        var newProductoCard = productoCardBase.clone();
                        newProductoCard.removeAttr("style");
                        newProductoCard.find(".producto-nombre").html(item.nombre);
                        newProductoCard.find(".producto-precio").html("$"+item.valorTotal);
                        newProductoCard.find(".producto-cantidad").html(item.cantidad)
                        newProductoCard.find(".producto-cart-remove").attr("id-producto",item.id);

                        productosContainer.append(newProductoCard);
                    });

                    cartTotal.html(data.cantidadTotal);
                    productosValorTotal.html("$"+data.valorTotal);
    }

    function removeFromCart(id, cantidad){
            var json = JSON.stringify({"id":id, "cantidad":cantidad});

            $.ajax({
                type: "DELETE",
                url: "/api/cart",
                contentType: 'application/json; charset=utf-8',
                data: json,
                success: function(data) {

                    populateProductos(data);
                },
                error: function(jqXhr, textStatus, errorMessage) {
                    alert(jqXhr.responseJSON.message);
                }
            })
        };

    productosContainer.on("click", ".producto-cart-remove", function(){
        var id = $(this).attr("id-producto");
        removeFromCart(id, 1);
    });

    checkoutForm.on("submit", function(){

                    $.ajax({
                        type: "POST",
                        url: "/api/cart/checkout",
                        success: function(data) {
                            checkoutForm.find("input").val("");
                            loadProductos();
                            alert("Compra exitosa")
                        },
                        error: function(jqXhr, textStatus, errorMessage) {
                            alert(jqXhr.responseJSON.message);
                        }
                    });

          return false;
    });

    loadProductos();
});