from playwright.sync_api import expect, TimeoutError as PlaywrightTimeoutError

from pages.base_page import BasePage


class ProductPage(BasePage):
    PRODUCT_CATEGORY = {
        "Samsung galaxy s6": "Phones",
        "Iphone 6 32gb": "Phones",
        "Sony vaio i5": "Laptops",
    }

    def login(self, username: str, password: str):
        self.page.locator("#login2").click()
        self.page.locator("#loginusername").fill(username)
        self.page.locator("#loginpassword").fill(password)

        self.page.get_by_role("button", name="Log in").click()

        # Si aparece alerta, se captura como error de login.
        try:
            dialog = self.page.wait_for_event("dialog", timeout=3000)
            message = dialog.message
            dialog.accept()
            raise AssertionError(f"No fue posible iniciar sesión en Demoblaze. Mensaje: {message}")
        except PlaywrightTimeoutError:
            pass

        expect(self.page.locator("#nameofuser")).to_be_visible(timeout=15000)

    def add_product_to_cart(self, product_name: str):
        self.click_product_store()
        self._select_category(product_name)
        self._open_product_detail(product_name)

        self.accept_dialog_after_action(
            lambda: self.page.get_by_role("link", name="Add to cart").click()
        )

    def go_to_cart(self):
        self.page.locator("#cartur").click()
        self.page.wait_for_load_state("domcontentloaded")
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        self.page.wait_for_timeout(1200)

    def _select_category(self, product_name: str):
        category = self.PRODUCT_CATEGORY.get(product_name)
        if category:
            self.page.get_by_role("link", name=category).click()
            self.wait_until_product_visible(product_name)

    def _open_product_detail(self, product_name: str):
        self.page.locator("a.hrefch", has_text=product_name).click()
        expect(self.page.locator("h2.name")).to_have_text(product_name, timeout=15000)
