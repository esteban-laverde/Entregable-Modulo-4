Feature: Carrito y compra en Demoblaze

  @playwright @carrito @dos_productos
  Scenario: Agregar dos productos al carrito y validarlos
    Given que ingreso a Demoblaze e inicio sesión
    When agrego los productos "Samsung galaxy s6" y "Iphone 6 32gb" al carrito
    And voy al carrito
    Then debo ver los productos "Samsung galaxy s6" y "Iphone 6 32gb" en el carrito

  @playwright @compra
  Scenario: Realizar el proceso de compra
    Given que ingreso a Demoblaze e inicio sesión
    And que tengo los productos "Samsung galaxy s6" y "Iphone 6 32gb" en el carrito
    When realizo el proceso de compra
    Then debo ver la confirmación de compra exitosa
