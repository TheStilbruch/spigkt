@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.command.CommandContext
import com.stilbruch.spigkt.command.KCommand
import com.stilbruch.spigkt.gui.GuiListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.java.JavaPlugin

abstract class KPlugin(val displayName: String) : JavaPlugin() {

    companion object {
        lateinit var instance: KPlugin
    }

    init {
        instance = this
    }

    private val commands: MutableSet<KCommand> = mutableSetOf()
    internal val modules: MutableSet<KModule> = mutableSetOf()
    var verbose = false

    final override fun onLoad() {
        onPluginLoad()
        modules.forEach(KModule::onLoad)
    }

    final override fun onEnable() {
        onPluginEnable()
        modules.forEach(KModule::onEnable)
        registerListener(GuiListener(this))
    }

    final override fun onDisable() {
        onPluginDisable()
        modules.forEach(KModule::onDisable)
    }

    fun onPluginLoad() {}
    fun onPluginEnable() {}
    fun onPluginDisable() {}

    /**
     * Registers an event listener
     */
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
