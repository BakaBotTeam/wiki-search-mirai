package ltd.guimc.wikisearch

import ltd.guimc.wikisearch.handler.MoegirlHandler
import ltd.guimc.wikisearch.handler.SogouHandler
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin
import java.time.Duration

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "ltd.guimc.wikisearch",
        name = "Wiki Search",
        version = "0.1.2"
    ) {
        author("BakaBotTeam")
        dependsOn("xyz.cssxsh.mirai.plugin.mirai-selenium-plugin")
    }
) {
    lateinit var blockedPermission: Permission
    lateinit var adminPermission: Permission
    val driver by lazy {
        MiraiSeleniumPlugin.driver()
    }

    var isUsing = false

    override fun onEnable() {
        logger.info("Wiki Search正在加载的路上了喵~")
        registerPerms()
        registerEvents()
        MiraiSeleniumPlugin.setup()
        driver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        logger.info("Wiki Search加载好啦喵~")
    }

    override fun onDisable() {
        logger.info("Wiki Search正在保存东西呢...")
        driver.quit()
        logger.info("Wiki Search已关闭了喵")
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        blockedPermission = register(PermissionId("ltd.guimc.wikisearch", "block"), "屏蔽某人执行百科搜索")
        adminPermission = register(PermissionId("ltd.guimc.wikisearch", "admin"), "屏蔽某人执行百科搜索")
    }

    private fun registerEvents() = this.globalEventChannel().run {
        subscribeAlways<MessageEvent> { event ->
            SogouHandler.onMessage(event)
            MoegirlHandler.onMessage(event)
        }
    }
}
