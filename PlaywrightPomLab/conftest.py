import os
from pathlib import Path

import allure
import pytest
from playwright.sync_api import sync_playwright


@pytest.fixture(scope="session")
def browser():
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=True)
        yield browser
        browser.close()


@pytest.fixture
def page(browser):
    context = browser.new_context(viewport={"width": 1920, "height": 1080})
    page = context.new_page()
    yield page
    context.close()


@pytest.fixture
def demoblaze_credentials():
    return {
        "username": os.getenv("DEMOBLAZE_USERNAME", "estebanlav@gmail.com"),
        "password": os.getenv("DEMOBLAZE_PASSWORD", "Test1234"),
    }


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    report = outcome.get_result()

    if report.when == "call" and report.failed and "page" in item.funcargs:
        page = item.funcargs["page"]

        screenshots_dir = Path("reports") / "screenshots"
        screenshots_dir.mkdir(parents=True, exist_ok=True)

        screenshot_path = screenshots_dir / f"{item.name}.png"
        screenshot_bytes = page.screenshot(path=str(screenshot_path), full_page=True)

        allure.attach(
            screenshot_bytes,
            name=f"screenshot_{item.name}",
            attachment_type=allure.attachment_type.PNG,
        )
