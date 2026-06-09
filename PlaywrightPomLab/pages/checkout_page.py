from playwright.sync_api import expect

from pages.base_page import BasePage


class CheckoutPage(BasePage):
    def place_order(self):
        self.page.get_by_role("button", name="Place Order").click()
        expect(self.page.locator("#orderModal")).to_be_visible(timeout=15000)

    def fill_purchase_form(
        self,
        name: str,
        country: str,
        city: str,
        credit_card: str,
        month: str,
        year: str,
    ):
        self.page.locator("#name").fill(name)
        self.page.locator("#country").fill(country)
        self.page.locator("#city").fill(city)
        self.page.locator("#card").fill(credit_card)
        self.page.locator("#month").fill(month)
        self.page.locator("#year").fill(year)

    def confirm_purchase(self):
        self.page.get_by_role("button", name="Purchase").click()

    def should_show_successful_purchase(self):
        expect(self.page.locator(".sweet-alert h2")).to_have_text(
            "Thank you for your purchase!",
            timeout=15000,
        )
        expect(self.page.locator(".sweet-alert")).to_contain_text("Amount")
        self.page.get_by_role("button", name="OK").click()
