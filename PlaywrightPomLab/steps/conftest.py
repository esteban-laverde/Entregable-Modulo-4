import pytest

from pages.product_page import ProductPage
from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage


@pytest.fixture
def product_page(page):
    return ProductPage(page)


@pytest.fixture
def cart_page(page):
    return CartPage(page)


@pytest.fixture
def checkout_page(page):
    return CheckoutPage(page)
