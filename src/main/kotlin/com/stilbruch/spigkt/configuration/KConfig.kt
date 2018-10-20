package com.stilbruch.spigkt.configuration

import com.stilbruch.spigkt.KModule
import com.stilbruch.spigkt.Log
import com.stilbruch.spigkt.PLUGIN
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

open class KConfig(val name: String) : KModule() {

    private val file: File = File(PLUGIN.dataFolder, name)
    private lateinit var yamlConfig: YamlConfiguration


    init {
        try {
            if (!PLUGIN.dataFolder.exists()) {
                PLUGIN.dataFolder.mkdirs()
            }
            if (!file.exists()) {
                Log.debug("Config file $name not found. Creating...")
                if (name == "config.yml") PLUGIN.saveDefaultConfig()
                else file.createNewFile()
            } else {
                Log.debug("Found file $name. Loading...")
            }
            yamlConfig = YamlConfiguration.loadConfiguration(file)
        } catch (e: Exception) {
            Log.error("Failed to load config file $name")
            e.printStackTrace()
        }

    }

}