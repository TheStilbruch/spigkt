package com.stilbruch.spigkt.command

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class CommandContext(
    val sender: CommandSender,
    val args: MutableList<String>
) {
    val flags: MutableMap<String, Boolean> = mutableMapOf()

    fun isPlayer(): Boolean {
        return sender !is ConsoleCommandSender
    }

    fun asPlayer(): Player? {
        return sender as? Player
    }

    fun checkFlag(flag: String): Boolean {
        return flags[flag] ?: false
    }

    fun checkFlags(flags: Set<String>) {
        flags.forEach {
            if (args.contains(it)) {
                args.remove(it)
                this.flags[it] = true
            } else {
                this.flags[it] = false
            }
        }
    }
}