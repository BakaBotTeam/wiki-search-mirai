package ltd.guimc.wikisearch.fetcher

import ltd.guimc.wikisearch.PluginMain
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import java.time.Duration

object SogouFetcher {
    val heightLimit = 9000

    val driver
        get() = PluginMain.driver

    fun fetch(word: String, full: Boolean = false): ByteArray? {
        driver.get("https://baike.sogou.com/")

        driver.findElement(By.xpath("//*[@id=\"searchText\"]")).clear()
        driver.findElement(By.xpath("//*[@id=\"searchText\"]")).sendKeys(word)
        driver.findElement(By.xpath("//*[@id=\"enterLemma\"]")).click()

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        val element = driver.findElement(By.className("lemma_container"))

        if (!full) {
            driver.manage().window().size = Dimension(1920, 1080)
        } else {
            driver.manage().window().size = Dimension(1920, minOf(heightLimit, element.rect.height))
        }

        return element.getScreenshotAs(OutputType.BYTES)
    }
}