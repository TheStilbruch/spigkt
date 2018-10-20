@file:Suppress("unused")

package com.stilbruch.spigkt

import com.stilbruch.spigkt.gui.Gui
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.logging.Logger

var SERVER: Server
    get() = Bukkit.getServer()
    set(server) = Bukkit.setServer(server)

val LOGGER: Logger
    get() = SERVER.logger

val CONSOLE: CommandSender
    get() = Bukkit.getConsoleSender()

val PLUGIN: KPlugin
    get() = KPlugin.instance

//Extension Functions
fun CommandSender.sendInfo(message: String) {
    this.sendMessage("${PLUGIN.displayName}${org.bukkit.ChatColor.GRAY} $message")
}

fun CommandSender.sendError(message: String) {
    this.sendMessage("${PLUGIN.displayName}${org.bukkit.ChatColor.RED} $message")
}

fun CommandSender.sendGood(message: String) {
    this.sendMessage("${PLUGIN.displayName}${org.bukkit.ChatColor.GREEN} $message")
}

fun HumanEntity.openGui(gui: Gui) {
    this.openInventory(gui.inventory)
}

//Logging functions
object Log {
    fun info(message: String) {
        CONSOLE.sendInfo(message)
    }

    fun error(message: String) {
        CONSOLE.sendError(message)
    }

    fun good(message: String) {
        CONSOLE.sendGood(message)
    }

    fun debug(message: String) {
        if (PLUGIN.verbose) CONSOLE.sendMessage("${PLUGIN.displayName}${org.bukkit.ChatColor.BLUE} $message")
    }
}

object Tasks {

    fun runTask(toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTask(PLUGIN)
    }

    fun runTaskLater(delay: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskLater(PLUGIN, delay)
    }

    fun runTaskTimer(delay: Long, repeat: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskTimer(PLUGIN, delay, repeat)
    }

    fun runTaskAsync(toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskAsynchronously(PLUGIN)
    }

    fun runTaskLaterAsync(delay: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskLaterAsynchronously(PLUGIN, delay)
    }

    fun runTaskTimerAsync(delay: Long, repeat: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskTimerAsynchronously(PLUGIN, delay, repeat)
    }

}

//Player functions
object Players {

    operator fun get(name: String) = getPlayerExact(name)

    operator fun get(uuid: UUID) = getPlayer(uuid)

    fun getPlayer(name: String): Player? = SERVER.getPlayer(name)

    fun getPlayerExact(name: String): Player? = SERVER.getPlayerExact(name)

    fun matchPlayer(name: String): List<Player> = SERVER.matchPlayer(name)

    fun getPlayer(id: UUID): Player? = SERVER.getPlayer(id)

    val online: Collection<Player> = SERVER.onlinePlayers

    val all: Array<OfflinePlayer> = SERVER.offlinePlayers

    fun savePlayers() = SERVER.savePlayers()

    fun getOfflinePlayer(id: UUID): OfflinePlayer? = SERVER.getOfflinePlayer(id)

    val operators: Set<OfflinePlayer>
        get() = SERVER.operators
}