package com.stilbruch.spigkt.command

import com.stilbruch.spigkt.sendError
import org.bukkit.command.CommandSender

abstract class SCommand(
    val names: Set<String>,
    private val helpMessage: String? = null,
    private val permission: String? = null,
    private val maxArgs: Int? = null,
    private val minArgs: Int? = null,
    private val mustBePlayer: Boolean = false,
    private val subCommands: Set<SCommand> = setOf(),
    private val flags: Set<String> = setOf(),
    // Errors
    private val errorTooManyArgs: String = "Too many arguments for this command!",
    private val errorTooFewArgs: String = "Not enough arguments for this command!",
    private val errorNoPermission: String = "No permission to run this command!",
    private val errorMustBePlayer: String = "You must be a player to run this command!"
) {
    abstract fun onCommand(context: CommandContext)

    private fun onTabComplete(context: CommandContext): MutableList<String> {
        context.checkFlags(flags)
        val completes = mutableListOf<String>()
        if (context.args.size > 0) {
            completes += context.flags.filter { !it.value }.keys.filter { it.startsWith(context.args.last()) }
            completes += subCommands.flatMap { it.names }.filter { it.startsWith(context.args.last()) }
        } else {
            completes += context.flags.filter { !it.value }.keys
            completes += subCommands.flatMap { it.names }
        }
        return completes
    }

    protected fun showHelp(sender: CommandSender) {
        if (helpMessage != null) sender.sendError(helpMessage)
    }

    fun handleContext(context: CommandContext) {
        if (maxArgs != null && context.args.size > maxArgs) {
            context.sender.sendError(errorTooManyArgs)
            showHelp(context.sender);
        } else if (minArgs != null && context.args.size < minArgs) {
            context.sender.sendError(errorTooFewArgs)
            showHelp(context.sender);
        } else if (permission != null && !context.sender.hasPermission(permission)) {
            context.sender.sendError(errorNoPermission)
            showHelp(context.sender);
        } else if (mustBePlayer && !context.isPlayer()) {
            context.sender.sendError(errorMustBePlayer)
            showHelp(context.sender);
        } else {
            val subCommand: SCommand? = subCommands.find {
                context.args.isNotEmpty() && it.names.contains(context.args[0].toLowerCase())
            }

            // If a subcommand matches execute it, else handle the command
            if (subCommand != null) subCommand.handleContext(CommandContext(context.sender, context.args.drop(1).toMutableList()))
            else {
                // Change flags
                context.checkFlags(flags)
                // Call command
                onCommand(context)
            }
        }
    }

    fun handleTabComplete(context: CommandContext): MutableList<String> {
        return if (maxArgs != null && context.args.size > maxArgs) {
            mutableListOf()
        } else if (minArgs != null && context.args.size < minArgs) {
            mutableListOf()
        } else if (permission != null && !context.sender.hasPermission(permission)) {
            mutableListOf()
        } else if (mustBePlayer && !context.isPlayer()) {
            mutableListOf()
        } else {
            val subCommand = subCommands.find {
                context.args.isNotEmpty() && it.names.contains(context.args[0].toLowerCase())
            }

            subCommand?.handleTabComplete(CommandContext(context.sender, context.args.drop(1).toMutableList()))
                ?: onTabComplete(context)
        }
    }
}