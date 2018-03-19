@file:Suppress("unused")

package com.stilbruch.spigkt.command

class KCommand(val name: String, init: KCommand.() -> Unit) {

    val subCommands: List<KCommand> = listOf()
    val permission: String? = null
    val playerOnly = false

    val onCommand: (CommandContext.() -> Unit)? = null

    init {
        this.apply(init)
    }

}