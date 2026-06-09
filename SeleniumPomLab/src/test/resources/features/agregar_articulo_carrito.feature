Feature: Carrito de compras en Demoblaze

  @selenium @carrito @dos_productos
  Scenario: Agregar dos productos al carrito y validarlos
    Given que ingreso a Demoblaze e inicio sesión
    When agrego los productos "Samsung galaxy s6" y "Sony vaio i5" al carrito
    And voy al carrito
    Then debo ver los productos "Samsung galaxy s6" y "Sony vaio i5" en el carrito

  @selenium @carrito_vacio
  Scenario: Eliminar productos del carrito y validar carrito vacío
    Given que ingreso a Demoblaze e inicio sesión
    And que tengo los productos "Samsung galaxy s6" y "Sony vaio i5" en el carrito
    When elimino los productos del carrito
    Then debo ver el carrito vacío
