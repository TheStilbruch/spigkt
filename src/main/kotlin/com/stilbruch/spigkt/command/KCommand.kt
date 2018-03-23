@file:Suppress("unused")

package com.stilbruch.spigkt.command

class KCommand(val names: Array<String>, init: KCommand.() -> Unit) {

    constructor(name: String, init: KCommand.() -> Unit) : this(arrayOf(name), init)

    val subCommands: List<KCommand> = listOf()
    val permission: String? = null
    val playerOnly = false

    val onCommand: (CommandContext.() -> Unit)? = null

    init {
        this.apply(init)
    }

    fun matches(input: String?): Boolean {
        return names.contains(input ?: return false)
    }

}