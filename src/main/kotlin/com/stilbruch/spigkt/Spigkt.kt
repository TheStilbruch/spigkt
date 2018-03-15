@file:Suppress("unused")

package com.stilbruch.spigkt

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.logging.Logger

var SERVER: Server
    get() = Bukkit.getServer()
    set(server) = Bukkit.setServer(server)

val LOGGER: Logger
    get() = SERVER.logger

object Tasks {

    fun runTask(plugin: JavaPlugin, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTask(plugin)
    }

    fun runTaskLater(plugin: JavaPlugin, delay: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskLater(plugin, delay)
    }

    fun runTaskTimer(plugin: JavaPlugin, delay: Long, repeat: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskTimer(plugin, delay, repeat)
    }

    fun runTaskAsync(plugin: JavaPlugin, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskAsynchronously(plugin)
    }

    fun runTaskLaterAsync(plugin: JavaPlugin, delay: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskLaterAsynchronously(plugin, delay)
    }

    fun runTaskTimerAsync(plugin: JavaPlugin, delay: Long, repeat: Long, toRun: BukkitRunnable.() -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                this.toRun()
            }
        }.runTaskTimerAsynchronously(plugin, delay, repeat)
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