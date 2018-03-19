package com.stilbruch.spigkt.command

import org.bukkit.command.CommandSender

class CommandContext(val sender: CommandSender, val args: List<String>) {

    fun get(i: Int): Any {
        return args[i]
    }

}