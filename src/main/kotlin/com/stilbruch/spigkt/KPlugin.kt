@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.command.CommandContext
import com.stilbruch.spigkt.command.KCommand
import com.stilbruch.spigkt.gui.GuiListener
import org.bukkit.ChatColor.GRAY
import org.bukkit.ChatColor.RED
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

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
        registerListener(GuiListener(this))
    }

    fun registerListener(listener: Listener) {
        SERVER.pluginManager.registerEvents(listener, this)
    }

    fun <E : Event> listen(type : Class<out E>, priority : EventPriority = EventPriority.NORMAL, ignoreCancelled : Boolean = true, handler : (E.() -> Unit)) {
        val listener = object : Listener, EventExecutor {
            override fun execute(listener : Listener?, event : Event) {
                if (listener != this) return
                (event as E).apply(handler)
            }
        }
        SERVER.pluginManager.registerEvent(type, listener, priority, listener, PLUGIN, ignoreCancelled)
    }

    inline fun <reified E : Event> listen(priority : EventPriority = EventPriority.NORMAL,
                                          ignoreCancelled : Boolean = true,
                                          noinline handler : E.() -> Unit) =
            listen(E::class.java, priority, ignoreCancelled, handler)

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
