package com.truecontext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.net.URL;

public class PocTest {
    private WebDriver driver = null;
    private WebDriverWait wait;
    protected static final Duration DEFAULT_TIMEOUT_IN_SEC = Duration.ofSeconds(10L);
    protected static final Duration POLLING_INTERVAL = Duration.ofMillis(10L);

    @Test
    public void openDuckDuckGoTest() {
        driver.get("https://duckduckgo.com");
        WebElement searchInput = waitToBeVisible(By.id("searchbox_input"));  
        searchInput.sendKeys("Selenium");
        searchInput.submit();
    }

    protected WebDriver initDriver() throws MalformedURLException {
		WebDriver driver;
        driver = new RemoteWebDriver(new URL("http://localhost:4444"), getFirefoxOptions());

		driver.manage()
				.timeouts()
				.pageLoadTimeout(Duration.ofSeconds(65))
				.scriptTimeout(Duration.ofSeconds(30));
		return driver;
	}

    private FirefoxOptions getFirefoxOptions() {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--width=1920");
		options.addArguments("--height=1080");
		options.addPreference("privacy.popups.policy", 2);
		options.addPreference("browser.download.folderList", 2);
		options.addPreference("browser.download.dir", "/tmp/downloads");
		options.addPreference("pdfjs.disabled", true);
		options.addPreference(
				"browser.helperApps.neverAsk.saveToDisk",
				"application/octet-stream,application/pdf,text/plain,text/xml,application/xml,application/csv,application/json,"
				+ "image/png,image/jpg,image/jpeg,text/csv,application/x-msexcel,application/excel,application/x-excel");
		options.setCapability("se:name", getVideoFileName());

		return options;
	}

    public String getVideoFileName() {
		return this.getClass().getSimpleName() + "_" + System.currentTimeMillis();
	}

    public WebDriver getDriver() {
		return this.driver;
	}

    public WebElement waitToBeVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	@AfterMethod
	public void tearDown() {
        if (getDriver() != null) {
			getDriver().quit();
		}
	}

	@BeforeMethod
	public void setUp() throws MalformedURLException {
		this.driver = initDriver();
        this.wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT_IN_SEC);
        wait.pollingEvery(POLLING_INTERVAL);
	}


}
