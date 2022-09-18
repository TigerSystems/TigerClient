#!/usr/bin/lua

require 'de.MarkusTieger.tigerclient.lua.api.LoggingAPI'
require 'de.MarkusTieger.tigerclient.lua.api.DataAPI'
require 'de.MarkusTieger.tigerclient.lua.api.ConstantsAPI'
require 'de.MarkusTieger.tigerclient.lua.api.EventAPI'
require 'de.MarkusTieger.tigerclient.lua.api.ModuleAPI'

function cfg_default_bool(name, def)

    if (!config.data.isBoolean(name)) {
        config.data.remove(name)
        config.data.add(name, def)
    }

end

function initConfig()
    
    if (config.exists())
        config.loadConfig()
    end

    cfg_default_bool("tick", false)
    cfg_default_bool("high-tick", false)
    cfg_default_bool("key-listener", false)
    cfg_default_bool("mouse-listener", false)
    cfg_default_bool("draggable", false)
    cfg_default_bool("default_state", false)

    if (!config.data.isString("searchname")) {
        config.data.remove("searchname")
        config.data.add("searchname", "example")
    }
end

function buildModule()
    local b = modules.newModuleBuilder()

    b.baseInformations("lua_example", "Lua Example Module", "A Example Module") --required

    if (config.data.asBoolean("tick"))
        b.makeTickable(function() logger.info("Tickable called in Example Module") end) --optional
    end

    if (config.data.asBoolean("high-tick"))
        b.makeHighTickable(function() logger.info("Hightickable called in Example Module") end) -- optional
    end
    
    if (config.data.asBoolean("key-listener"))    
        b.makeKeyable(key_pressed) -- optional
    end

    b.customSearchname(config.data.asString("searchname")) -- optional

    b.customEnableHandler(config.data.asBoolean("default_state")) -- optional (can also be a setter with boolean and a getter with a result of a boolean)

    if (config.data.asBoolean("mouse-listener"))
        b.makeMousable(function(action) logger.info("Mouse-Left Event. Action: " .. tostring(action)) end, function(action) logger.info("Mouse-Right Event. Action: " .. tostring(action)) end, function(action) logger.info("Mouse-Middle Event. Action: " .. tostring(action)) end) -- optional
    end

    if (config.data.asBoolean("draggable"))
        b.addDraggable(
            data.createStaticScreenLocationFromRelative(0.5, 0.5),
            data.createStaticBounds(50, 50),
            function(stack, pos) logger.info("Rendering Draggable...") end,
            function(stack, pos) logger.info("Rendering Dummy Draggable...") end
        )
    end

    return b.build()

end

initConfig()

mod = buildModule()

function onEnable()

    modules.register(mod)

    logger.info("Example Plugin is enabled.")
end

function onDisable()

    modules.unregister(mod)

    logger.info("Example Plugin is disabled.")
end

function key_pressed(keyCode, flag)
    if (keyCode == constants.glfw.GLFW_KEY_F && flag == constants.glfw.GLFW_PRESS)
        logger.info("Key \"F\" pressed!")
    end
end
