package com.darkona.adventurebackpack.config;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public class Keybindings {

    private static final String KEYS_CATEGORY = "keys.adventureBackpack.category";
    private static final String OPEN_INVENTORY = "keys.adventureBackpack.openInventory";
    private static final String TOGGLE_ACTIONS = "keys.adventureBackpack.toggleActions";
    private static final String TOGGLE_ACTIVATE = "keys.adventureBackpack.toggleActivate";

    public static KeyBinding openInventory = new KeyBinding(OPEN_INVENTORY, Keyboard.KEY_NONE, KEYS_CATEGORY);
    public static KeyBinding toggleActions = new KeyBinding(TOGGLE_ACTIONS, Keyboard.KEY_NONE, KEYS_CATEGORY);
    public static KeyBinding toggleActivate = new KeyBinding(TOGGLE_ACTIVATE, Keyboard.KEY_NONE, KEYS_CATEGORY);

    public static String getInventoryKeyName() {
        return GameSettings.getKeyDisplayString(openInventory.getKeyCode());
    }

    public static String getActionKeyName() {
        return GameSettings.getKeyDisplayString(toggleActions.getKeyCode());
    }
}
