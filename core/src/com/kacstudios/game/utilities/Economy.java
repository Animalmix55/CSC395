package com.kacstudios.game.utilities;

import com.kacstudios.game.sounds.GameSounds;

import java.util.ArrayList;

public class Economy {
    private static int Money = 4000;
    private static ArrayList<Runnable> updateSubscribers = new ArrayList<>();

    public static void subscribeToUpdate(Runnable onUpdate) {
        Economy.updateSubscribers.add(onUpdate);
    }

    public static int getMoney() {
        return Money;
    }

    public static void Init() {
        setMoney(4000);
    }

    /**
     * Adds a given amount to the economy.
     */
    public static void addMoney(int amount) {
        GameSounds.chaChingSound.play(false);
        setMoney(getMoney() + amount);
    }

    /**
     * Reduces the account balance by the given amount
     * @param amount
     * @return A boolean indicating if there was sufficient balance for the operation
     */
    public static boolean removeMoney(int amount) {
        if(getMoney() < amount) return false;
        GameSounds.chaChingSound.play(false);
        setMoney(getMoney() - amount);
        return true;
    }

    public static void setMoney(int amount) {
        Money = amount;
        informSubscribers();
    }

    private static void informSubscribers() {
        updateSubscribers.forEach(u -> {
            try {
                u.run();
            } catch (Exception e) {
                updateSubscribers.remove(u);
            }
        });
    }
}
