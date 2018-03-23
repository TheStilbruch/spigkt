@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.command.CommandContext
import com.stilbruch.spigkt.command.KCommand
import com.stilbruch.spigkt.gui.GUIListenerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.ChatColor.*
import org.bukkit.event.Event
import org.bukkit.event.EventHandler

abstract class KPlugin(name: String) : JavaPlugin() {

    companion object {
        var instance: KPlugin? = null
    }

    init {
        instance = this
    }

    val displayName: String = "$GRAY[$name$GRAY]"
    val commands: MutableSet<KCommand> = mutableSetOf()

    override fun onEnable() {
        registerListener(GUIListenerManager(this))
    }

    fun registerListener(listener: Listener) {
        SERVER.pluginManager.registerEvents(listener, this)
    }

    fun <T : Event> listener(f: T.() -> Unit) {
        SERVER.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun handle(e: T) {
                e.apply(f)
            }
        }, this)
    }

    fun formatMessage(message: String): String {
        return "$displayName $message"
    }

    fun formatError(message: String): String {
        return "$displayName$RED $message"
    }

    final override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val command: KCommand = commands.find { it.matches(label) } ?: return false

        return handleCommand(command, sender, args.toList())
    }

    //This function checks for sub-commands
    private fun handleCommand(command: KCommand, sender: CommandSender, args: List<String>): Boolean {
        val subCommand: KCommand? = command.subCommands.find { it.matches(args.getOrNull(0)) }

        return if (subCommand == null) {
            command.onCommand?.invoke(CommandContext(sender, args))
            true
        } else {
            handleCommand(subCommand, sender, args.drop(1))
        }
    }

}
