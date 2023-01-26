/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.wikisearch.utils

import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText

object MessageUtils {
    fun MessageChain.getPlainText(): String {
        var pt = ""
        for (i in listIterator()) if (i is PlainText) pt += i.content
        return pt
    }
}