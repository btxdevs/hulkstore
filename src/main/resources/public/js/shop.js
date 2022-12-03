$(document).ready(function() {

    var productosContainer = $("#productos-container");
    var productoCardBase = $(".producto-card-base");
    var cartTotal = $("#cart-total");

    function loadProductos(){
            $.ajax({
                type: "GET",
                url: "/api/producto?size=50&sort=cantidadDisponible%2Cdesc",
                dataType: 'json',
                success: function(data) {
                    productosContainer.children().not(':first').remove();
                    data.elements.forEach(function(item, index) {

                        var newProductoCard = productoCardBase.clone();
                        newProductoCard.find(".producto-nombre").html(item.nombre);

                        if(item.cantidadDisponible==0){
                            newProductoCard.find(".producto-precio").html("Sin Stock");
                            newProductoCard.find(".producto-cart").remove();
                        }else{
                            newProductoCard.find(".producto-precio").html("$"+item.valorUnitario);
                            newProductoCard.find(".producto-cart").attr("id-producto", item.id);
                            newProductoCard.find(".producto-cart").prop("disabled", false);
                        }

                        productosContainer.append(newProductoCard);
                    });
                    if(data.elements.length>0){
                        productoCardBase.css("display","none");
                    }
                },
                error: function(jqXhr, textStatus, errorMessage) {
                    alert(jqXhr.responseJSON.message);
                }
            });
    }

    function addToCart(id, cantidad){
            var json = JSON.stringify({"id":id, "cantidad":cantidad});

            $.ajax({
                type: "POST",
                url: "/api/cart",
                contentType: 'application/json; charset=utf-8',
                data: json,
                success: function(data) {
                    cartTotal.html(data.cantidadTotal);
                },
                error: function(jqXhr, textStatus, errorMessage) {
                    alert(jqXhr.responseJSON.message);
                }
            })
        }

    productosContainer.on("click", ".producto-cart", function(){
        var id = $(this).attr("id-producto");
        addToCart(id, 1);
    });

    function loadCart(){
            $.ajax({
                type: "GET",
                url: "/api/cart",
                dataType: 'json',
                success: function(data) {
                    cartTotal.html(data.cantidadTotal);
                },
                error: function(jqXhr, textStatus, errorMessage) {
                    alert(jqXhr.responseJSON.message);
                }
            });
    }

    loadProductos();
    loadCart();
});