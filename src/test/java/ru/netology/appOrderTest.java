package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class appOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSuccessfullySubmitForm() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Йцукен Фыва");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+99999999999");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldReturnWarningIfNameWrong() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Y");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+99999999999");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button")).click();
        String text = driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[1]/span/span/span[3]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldReturnWarningIfPhoneWrong() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Федор Двинятин");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+првоп95ваор5");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.className("button")).click();
        String text = driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[2]/span/span/span[3]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldReturnWarningIfCheckboxUnchecked() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Федор Двинятин");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+99999999");
        form.findElement(By.className("button")).click();
        WebElement formAfterClick = driver.findElement(By.cssSelector("form"));
        assertNotNull(formAfterClick, "Form should not be submitted when checkbox is unchecked.");
    }
}