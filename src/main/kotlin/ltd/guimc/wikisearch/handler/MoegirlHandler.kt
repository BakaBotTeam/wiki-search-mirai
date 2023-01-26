package ltd.guimc.wikisearch.handler

import ltd.guimc.wikisearch.PluginMain
import ltd.guimc.wikisearch.fetcher.MoegirlFetcher
import ltd.guimc.wikisearch.utils.MessageUtils.getPlainText
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.ExternalResource

object MoegirlHandler {
    suspend fun onMessage(event: MessageEvent) {
        val subject = event.subject
        if (!event.sender.permitteeId.hasPermission(PluginMain.blockedPermission)) {
            try {
                val messageText = event.message.getPlainText()
                if (messageText.isEmpty()) return

                if (messageText.startsWith("萌娘百科 ")) {
                    subject.sendMessage("客官稍等一下喵~ 正在查找你想要的东西...")

                    if (PluginMain.isUsing) {
                        subject.sendMessage("Selenium正忙着呢... 等一下吧~")
                        return
                    }

                    PluginMain.isUsing = true
                    val imageByteArray =
                        MoegirlFetcher.fetch(messageText.substring(5)) ?: subject.sendMessage("查询失败了喵...")
                    PluginMain.isUsing = false

                    subject.sendImage(imageByteArray as ExternalResource)
                } else if (messageText.startsWith("萌娘百科详细 ")) {
                    subject.sendMessage("客官稍等一下喵~ 正在查找你想要的东西...")

                    if (PluginMain.isUsing) {
                        subject.sendMessage("Selenium正忙着呢... 等一下吧~")
                        return
                    }

                    PluginMain.isUsing = true
                    val imageByteArray =
                        MoegirlFetcher.fetch(messageText.substring(7), true) ?: subject.sendMessage("查询失败了喵...")
                    PluginMain.isUsing = false

                    subject.sendImage(imageByteArray as ExternalResource)
                }
            } catch (e: Throwable) {
                subject.sendMessage("出了一点小故障... $e")
                e.printStackTrace()
            }
        }
    }
}