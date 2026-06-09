from pytest_bdd import given, parsers, scenarios, then, when

scenarios("../features/agregar_articulo_carrito.feature")


@given("que ingreso a Demoblaze e inicio sesión")
def abrir_demoblaze_e_iniciar_sesion(product_page, cart_page, demoblaze_credentials):
    product_page.open_store()
    product_page.login(
        demoblaze_credentials["username"],
        demoblaze_credentials["password"],
    )

    # Limpieza preventiva para evitar productos de ejecuciones anteriores.
    cart_page.delete_all_products()
    product_page.click_product_store()


@given(parsers.parse('que tengo los productos "{first_product}" y "{second_product}" en el carrito'))
def tener_productos_en_carrito(product_page, first_product, second_product):
    product_page.add_product_to_cart(first_product)
    product_page.add_product_to_cart(second_product)
    product_page.go_to_cart()


@when(parsers.parse('agrego los productos "{first_product}" y "{second_product}" al carrito'))
def agregar_dos_productos(product_page, first_product, second_product):
    product_page.add_product_to_cart(first_product)
    product_page.add_product_to_cart(second_product)


@when("voy al carrito")
def voy_al_carrito(product_page):
    product_page.go_to_cart()


@when("realizo el proceso de compra")
def realizo_el_proceso_de_compra(checkout_page):
    checkout_page.place_order()
    checkout_page.fill_purchase_form(
        name="Katherinne QA",
        country="Colombia",
        city="Bogotá",
        credit_card="4111111111111111",
        month="12",
        year="2028",
    )
    checkout_page.confirm_purchase()


@then(parsers.parse('debo ver los productos "{first_product}" y "{second_product}" en el carrito'))
def validar_productos_en_carrito(cart_page, first_product, second_product):
    cart_page.should_contain_products(first_product, second_product)


@then("debo ver la confirmación de compra exitosa")
def validar_compra_exitosa(checkout_page):
    checkout_page.should_show_successful_purchase()
