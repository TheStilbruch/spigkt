package com.stilbruch.spigkt


/**
 * Represents a class that needs to hook into the onEnable, onLoad, and onDisable functions of the plugin.
 */
abstract class KModule {

    init {
        PLUGIN.modules.add(this)
    }

    open fun onEnable() {}
    open fun onDisable() {}
    open fun onLoad() {}

}