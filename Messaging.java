public static void sendListMessage(Target target, Type type, Player player, String configID, Object value, String... replace) {
        ArrayList<String> message = null;

        switch (type) {
            case CUSTOM:
                if(value instanceof ArrayList) {
                    message = (ArrayList<String>) value;
                } else {
                    sendError("sendListMessage(... Object value, ...) -> (type = CUSTOM) -> VALUE IS NOT ARRAY-LIST!");
                }
                break;
            case CONFIG:
                if(value instanceof String) {
                    FileConfiguration config = api.getConfig(configID, null);
                    if(configID != null && config != null) {
                        message = (ArrayList<String>) config.getStringList((String) value);
                    } else {
                        sendError("sendListMessage(... String configID, ...) -> CONFIGURATION FILE IS NULL!");
                    }
                } else {
                    sendError("sendListMessage(... Object value, ...) -> (type = CONFIG) -> VALUE IS NOT STRING!");
                }
                break;
            case PLAYER_LANGUAGE:
                if(value instanceof String) {
                    message = (ArrayList<String>) api.lang().getPlayerLang(player.getUniqueId()).getStringList((String) value);
                } else {
                    sendError("sendListMessage(... Object value, ...) -> (type = PLAYER_LANGUAGE) -> VALUE IS NOT STRING!");
                }
                break;
            case CONSOLE_LANGUAGE:
                if(value instanceof String) {
                    message = (ArrayList<String>) api.lang().getLang(api.config().getConfig().getString("language.console"), null).getStringList((String) value);
                } else {
                    sendError("sendListMessage(... Object value, ...) -> (type = CONSOLE_LANGUAGE) -> VALUE IS NOT STRING!");
                }
                break;
            case SELECTED_LANGUAGE:
                if(value instanceof String) {
                    message = (ArrayList<String>) api.lang().getLang(configID, null).getStringList((String) value);
                } else {
                    sendError("sendListMessage(... Object value, ...) -> (type = SELECTED_LANGUAGE) -> VALUE IS NOT STRING!");
                }
                break;
        }

        ArrayList<String> newList = new ArrayList<>();
        if (replace.length % 2 == 0) {
            for (int i = 0; i < replace.length; i += 2) {
                if (replace[i] != null && replace[i + 1] != null) {
                    for (String line : message) {
                        newList.add(line.replace(replace[i], replace[i + 1]));
                    }
                } else
                    break;
            }
        } else
            sendError("sendMessage(... String... replace) -> (replace.lenght = [ " + replace.length + " ] % 2 != 0) ->  REPLACE MUST HAVE AN EVEN NUMBER OF ARGUMENTS");


        if(target == Target.PLAYER) {
            if(player != null) {
                for(String line : newList) {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                    player.sendMessage(line.replace("&", "§"));
                }
            } else {
                sendError("sendListMessage(... Target target, ...) -> (target = PLAYER, player = null) -> PLAYER IS NULL!");
            }
        } else {
            for(String line : newList) {
                Bukkit.getConsoleSender().sendMessage(line.replace("&", "§"));
            }
        }
    }
