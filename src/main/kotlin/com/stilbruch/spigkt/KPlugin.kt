@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.command.CommandContext
import com.stilbruch.spigkt.command.SCommand
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

    protected open val commands: MutableSet<SCommand> = mutableSetOf()
    internal val modules: MutableSet<KModule> = mutableSetOf()
    var verbose = false

    final override fun onLoad() {
        onPluginLoad()
        modules.forEach(KModule::onLoad)
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

    open fun onPluginLoad() {}
    open fun onPluginEnable() {}
    open fun onPluginDisable() {}

    /**
     * Registers an event listener
     */
    fun registerListener(listener: Listener) {
        SERVER.pluginManager.registerEvents(listener, this)
    }

    fun <E : Event> listen(type : Class<out E>, priority : EventPriority = EventPriority.NORMAL, ignoreCancelled : Boolean = true, handler : (E.() -> Unit)) {
        val listener = object : Listener, EventExecutor {
            override fun execute(listener : Listener, event : Event) {
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

    // Handle commands
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        commands
            .find { it.names.contains(command.name) }
            ?.handleContext(CommandContext(sender, args.toMutableList()))
        // Always return true because this will always succeed
        return true
    }

    // Handle tab completes
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return commands
            .find { it.names.contains(command.name) }
            ?.handleTabComplete(CommandContext(sender, args.toMutableList())) ?: mutableListOf()
    }

}
