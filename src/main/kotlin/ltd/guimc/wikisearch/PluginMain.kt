package ltd.guimc.wikisearch

import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import xyz.cssxsh.mirai.selenium.MiraiSeleniumPlugin

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "ltd.guimc.wikisearch",
        name = "Wiki Search",
        version = "0.1.1"
    ) {
        author("BakaBotTeam")
        dependsOn("xyz.cssxsh.mirai.plugin.mirai-selenium-plugin")
    }
) {
    lateinit var blockedPermission: Permission
    val driver by lazy {
        MiraiSeleniumPlugin.driver()
    }

    var isUsing = false

    override fun onEnable() {
        logger.info("Wiki Search正在加载的路上了喵~")
        registerPerms()
        registerEvents()
        driver
        logger.info("Wiki Search加载好啦喵~")
    }

    override fun onDisable() {
        logger.info("Wiki Search正在保存东西呢...")
        driver.quit()
        logger.info("Wiki Search已关闭了喵")
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        blockedPermission = register(PermissionId("ltd.guimc.wikisearch", "block"), "屏蔽某人执行百科搜索")
    }

    private fun registerEvents() = GlobalEventChannel.run {
    }
}
