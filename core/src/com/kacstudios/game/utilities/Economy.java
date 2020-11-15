package com.kacstudios.game.utilities;

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

    /**
     * Adds a given amount to the economy.
     */
    public static void addMoney(int amount) {
        setMoney(getMoney() + amount);
    }

    /**
     * Reduces the account balance by the given amount
     * @param amount
     * @return A boolean indicating if there was sufficient balance for the operation
     */
    public static boolean removeMoney(int amount) {
        if(getMoney() < amount) return false;
        setMoney(getMoney() - amount);
        return true;
    }

    private static void setMoney(int amount) {
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
