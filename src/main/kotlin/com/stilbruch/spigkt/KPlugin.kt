@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.command.KCommand
import com.stilbruch.spigkt.gui.GUIListenerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

abstract class KPlugin(val displayName: String) : JavaPlugin() {

    abstract val commands: List<KCommand>

    override fun onEnable() {
        registerListener(GUIListenerManager(this))
    }

    private fun registerListener(listener: Listener) {
        SERVER.pluginManager.registerEvents(listener, this)
    }

    //override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
    //    val command: KCommand? = commands.find { it.name == label }
    //    if (command == null) {
    //        sender.sendMessage(f)
    //    }
    //}

}
