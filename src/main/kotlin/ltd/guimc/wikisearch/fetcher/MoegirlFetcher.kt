package ltd.guimc.wikisearch.fetcher

import ltd.guimc.wikisearch.PluginMain
import org.json.JSONArray
import org.openqa.selenium.*
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.TimeUnit

object MoegirlFetcher {
    private const val UserAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/109.0"
    private const val MoegirlApi =
        "https://zh.moegirl.org.cn/api.php?action=opensearch&format=json&formatversion=2&namespace=0&limit=10&search="
    private val RemoveableElements = arrayListOf(
        "moe-global-header", "moe-global-footer", "moe-global-toolbar",
        "moe-open-in-app", "moe-float-toc-container", "moe-global-background",
        "moe-a11y-navigations", "moe-global-siderail", "moe-topbanner-container",
        "MOE_DRAW_LOTS_ROOT", "bottomRightCorner", "moe-page-tools-container",
        "moe-after-content"
    )
    private const val heightLimit = 9000

    val driver
        get() = PluginMain.driver

    fun fetch(word: String, full: Boolean = false): ByteArray? {
        driver.manage().timeouts().pageLoadTimeout(if (full) 14 else 7, TimeUnit.SECONDS)

        try {
            driver.get(getURL(word) ?: return null)
        } catch (_: TimeoutException) {
        }
        driver.findElement(By.className("n-modal-container"))

        try {
            (driver as JavascriptExecutor).executeScript(getRemoveRemoveableElementCommand())
        } catch (_: Throwable) {
        }

        val element = driver.findElement(By.id("mw-body"))

        if (!full) {
            driver.manage().window().size = Dimension(1920, 1080)
        } else {
            driver.manage().window().size = Dimension(1920, minOf(heightLimit, element.rect.height))
        }

        return element.getScreenshotAs(OutputType.BYTES)
    }

    private fun getRemoveRemoveableElementCommand(): String {
        var _str =
            ";document.getElementsByClassName(\"n-modal-container\")[0].remove();var childs = document.getElementById(\"moe-main-container\").children; for (i=0; i<childs.length; i++) {if (childs[i].classList[0] != \"moe-flexible-container\") { childs[i].remove(); }};"
        RemoveableElements.forEach {
            _str += "document.getElementById(\"$it\").remove();"
        }
        return _str
    }

    fun getURL(word: String): String? {
        // TencentWAF f**k u
        val httpClient = HttpClient.newHttpClient()
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(MoegirlApi + URLEncoder.encode(word, "UTF-8")))
            .header("User-Agent", UserAgent)
            .header("Referer", "https://zh.moegirl.org.cn/")
            .header("Origin", "https://zh.moegirl.org.cn")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Accept", "application/json, text/javascript, */*; q=0.01")
            .build()
        val responce = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        val body = responce.body()
        val bodyJson = JSONArray(body)
        return bodyJson.getJSONArray(3).getString(0)
    }
}