@file:Suppress("unused")

package com.stilbruch.spigkt.command

import com.stilbruch.spigkt.sendError

class KCommand(
        val names: Array<String>,
        val help: String = "",
        val permission: String? = null,
        val maxArgs: Int = 0,
        val minArgs: Int = 0,
        val onCommand: ((CommandContext) -> Unit) = {},
        val subCommands: Set<KCommand> = setOf()
) {

    fun handleContext(commandContext: CommandContext) {
        if (commandContext.args.size > maxArgs) {
            commandContext.sender.sendError("Too many args!")
        } else if (commandContext.args.size < minArgs) {
            commandContext.sender.sendError("Not enough args!")
        } else if (permission != null && !commandContext.sender.hasPermission(permission)) {
            commandContext.sender.sendError("You don't have permission for this command!")
        } else {
            onCommand(commandContext)
        }
    }

    fun matches(input: String?): Boolean {
        return names.contains(input ?: return false)
    }

}