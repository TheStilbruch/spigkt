@file:Suppress("unused")

package com.stilbruch.spigkt

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID
import java.util.logging.Logger

var SERVER: Server
    get() = Bukkit.getServer()
    set(server) = Bukkit.setServer(server)

val LOGGER: Logger
    get() = SERVER.logger

val PLUGIN: KPlugin
    get() = KPlugin.instance!!

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