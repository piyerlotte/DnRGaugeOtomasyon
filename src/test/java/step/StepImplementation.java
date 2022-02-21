package step;

import com.thoughtworks.gauge.Step;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class StepImplementation extends BaseSteps {
    final static Logger logger = Logger.getLogger(BaseSteps.class.getName());

    /**
     * Ürünler listesi, ürün eklenmeden önceki ve sonraki ürün sayıları
     */
    public static Integer OLD_COUNT = null;
    public static Integer NEW_COUNT = null;

    @Step("<url> adresine git")
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step("<key> elementine tıklama")
    public void clickElement(String key) {
        click(key);
        logger.info(key + " elementine tıklandı");
    }

    @Step("<key> lokasyonuna fare hareketi")
    public void hoverElement(String key) {
        moveElement(key);
        logger.info(key + " elementine gitti");
    }

    @Step("<int> saniye kadar bekle")
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("<key> alanına <text> bilgisi yazdırma")
    public void sendKeys(String key, String text) {
        getElementWithKeyIfExists(key);
        sendKey(key, text);
        logger.info(key + " alanına " + text + " bilgisi yazdırıldı");
    }

    //ALTERNATİF ASSERTION METODU

    @Step("<key> alanında <info> elementi görünüyor mu?")
    public void isDisplayed(String key, String info) {
        String text = findElement(key).getText();
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                boolean display = isDisplayed(key);
                Assertions.assertTrue(display, text + " elementi görünmüyor!!!");
                logger.info(text + " elementi görünüyor");
                return;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MAX_ITERATION_COUNT);
        }
    }

    @Step("<key> alanında <s> değeri <text> değeri ile aynı mı")
    public void getAttribute(String key, String s, String text) {
        WebElement element = findElement(key);
        String txt = findElement(key).getAttribute(s);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(s) != null){
            Assertions.assertEquals(txt, text, "aynı değil");
            logger.info(text + " ile " + txt + " eşleşti");
            return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
    }

    @Step("<key >rastgele ürün seç")
    public void randomSelect(String key) {
        List<WebElement> products = findElements(key);
        Random random = new Random();
        products.get(random.nextInt(products.size())).click();
        logger.info("Rastgele ürün seçildi");
    }

    @Step("Elemente ENTER keyi yolla <key>")
    public void sendKeyToElementENTER(String key) {
        findElement(key).sendKeys(Keys.ENTER);
        logger.info(key + " elementine ENTER keyi yollandı.");
    }

    @Step("<long> milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Şuanki URL <url> değerini içeriyor mu kontrol et")
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL " + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step("<key> olarak comboboxdan bir değer seçilir")
    public void comboBoxRandom(String key) throws InterruptedException {

        WebElement comboBoxElemenet;
        comboBoxElemenet = findElementWithKey(key);
        Select comboBox = new Select(comboBoxElemenet);
        int randomIndex = new Random().nextInt(comboBox.getOptions().size());
        comboBox.selectByIndex(randomIndex);
        Thread.sleep(1500);

        logger.info(key + " comboboxından herhangi bir değer seçildi");

    }

    @Step("<key> dizisinin eleman sayısı")
    public Integer arraySize(String key) {
        List<WebElement> elements = findElements(key);
        OLD_COUNT = elements.size();
        logger.info("Dizinin eleman sayısı: " + OLD_COUNT);
        return OLD_COUNT;
    }

    @Step("<key> dizisi eleman sayısı karşılaştırma")
    public void arrayCompare(String key) {
        NEW_COUNT = arraySize(key);
        Assertions.assertEquals(OLD_COUNT, NEW_COUNT, "Sayılar Eşleşmedi!!!");
        logger.info("Adres sayıları eşleşti");
    }

    @Step("<key> li elementi bul, temizle ve rasgele  email değerini yaz")
    public void RandomMail(String key) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@sahabt.com");
    }
    @Step("<text> textini <key> elemente tek tek yaz")
    public void sendKeyOneByOne(String text, String key) throws InterruptedException {

        WebElement field = findElement(key);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                findElement(key).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yazıldı.");
        }
    }

    @Step("Element var mı kontrol et <key>")
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        assertFalse(Boolean.parseBoolean("Element: '" + key + "' doesn't exist."));
        return null;
    }
}