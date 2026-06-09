from playwright.sync_api import Page, expect


class BasePage:
    BASE_URL = "https://www.demoblaze.com/index.html"

    def __init__(self, page: Page):
        self.page = page

    def open_store(self):
        self.page.goto(self.BASE_URL, wait_until="domcontentloaded")
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        expect(self.page.locator("a.hrefch").first).to_be_visible(timeout=15000)

    def click_product_store(self):
        self.page.locator("#nava").click()
        self.page.wait_for_load_state("domcontentloaded")
        self.page.wait_for_selector("#tbodyid", state="attached", timeout=15000)
        expect(self.page.locator("a.hrefch").first).to_be_visible(timeout=15000)

    def accept_dialog_after_action(self, action):
        with self.page.expect_event("dialog") as dialog_info:
            action()

        dialog = dialog_info.value
        dialog.accept()
        self.page.wait_for_timeout(1000)

    def wait_until_product_visible(self, product_name: str):
        expect(
            self.page.locator("a.hrefch", has_text=product_name)
        ).to_be_visible(timeout=15000)