from playwright.sync_api import expect

from pages.base_page import BasePage


class CartPage(BasePage):
    def go_to_cart(self):
        self.page.locator("#cartur").click()
        self.page.wait_for_load_state("domcontentloaded")
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        self.page.wait_for_timeout(1200)

    def get_product_names(self):
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        self.page.wait_for_timeout(1200)

        names = self.page.locator("#tbodyid tr td:nth-child(2)").all_inner_texts()
        return [name.strip() for name in names if name.strip()]

    def should_contain_products(self, first_product: str, second_product: str):
        expect(self.page.locator("#tbodyid")).to_contain_text(first_product, timeout=15000)
        expect(self.page.locator("#tbodyid")).to_contain_text(second_product, timeout=15000)

    def delete_all_products(self):
        self.go_to_cart()

        while self.page.locator("a", has_text="Delete").count() > 0:
            rows_before = self.page.locator("#tbodyid tr").count()
            self.page.locator("a", has_text="Delete").first.click()

            self.page.wait_for_function(
                "(rowsBefore) => document.querySelectorAll('#tbodyid tr').length < rowsBefore",
                arg=rows_before,
                timeout=15000,
            )

        self.page.wait_for_timeout(1000)

    def should_be_empty(self):
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        expect(self.page.locator("#tbodyid tr")).to_have_count(0, timeout=15000)