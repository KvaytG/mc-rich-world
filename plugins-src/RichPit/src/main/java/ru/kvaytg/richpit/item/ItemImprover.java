package ru.kvaytg.richpit.item;

import org.bukkit.Material;

public class ItemImprover {

    public int getImprovementCost(Material material) {
        return switch (material) {
            // Мечи
            case WOODEN_SWORD -> 200;
            case STONE_SWORD -> 400;
            case IRON_SWORD -> 800;
            case DIAMOND_SWORD -> 1000;
            // Броня — шлемы
            case LEATHER_HELMET -> 200;
            case CHAINMAIL_HELMET -> 400;
            case IRON_HELMET -> 800;
            case DIAMOND_HELMET -> 1000;
            // Броня — нагрудники
            case LEATHER_CHESTPLATE -> 200;
            case CHAINMAIL_CHESTPLATE -> 400;
            case IRON_CHESTPLATE -> 800;
            case DIAMOND_CHESTPLATE -> 1000;
            // Броня — штаны
            case LEATHER_LEGGINGS -> 200;
            case CHAINMAIL_LEGGINGS -> 400;
            case IRON_LEGGINGS -> 800;
            case DIAMOND_LEGGINGS -> 1000;
            // Броня — ботинки
            case LEATHER_BOOTS -> 200;
            case CHAINMAIL_BOOTS -> 400;
            case IRON_BOOTS -> 800;
            case DIAMOND_BOOTS -> 1000;
            // не улучшаемые предметы или незерит уже
            default -> 0;
        };
    }

    public Material improveItem(Material material) {
        return switch (material) {
            // Мечи
            case WOODEN_SWORD -> Material.STONE_SWORD;
            case STONE_SWORD -> Material.IRON_SWORD;
            case IRON_SWORD -> Material.DIAMOND_SWORD;
            case DIAMOND_SWORD -> Material.NETHERITE_SWORD;
            // Шлемы
            case LEATHER_HELMET -> Material.CHAINMAIL_HELMET;
            case CHAINMAIL_HELMET -> Material.IRON_HELMET;
            case IRON_HELMET -> Material.DIAMOND_HELMET;
            case DIAMOND_HELMET -> Material.NETHERITE_HELMET;
            // Нагрудники
            case LEATHER_CHESTPLATE -> Material.CHAINMAIL_CHESTPLATE;
            case CHAINMAIL_CHESTPLATE -> Material.IRON_CHESTPLATE;
            case IRON_CHESTPLATE -> Material.DIAMOND_CHESTPLATE;
            case DIAMOND_CHESTPLATE -> Material.NETHERITE_CHESTPLATE;
            // Поножи
            case LEATHER_LEGGINGS -> Material.CHAINMAIL_LEGGINGS;
            case CHAINMAIL_LEGGINGS -> Material.IRON_LEGGINGS;
            case IRON_LEGGINGS -> Material.DIAMOND_LEGGINGS;
            case DIAMOND_LEGGINGS -> Material.NETHERITE_LEGGINGS;
            // Ботинки
            case LEATHER_BOOTS -> Material.CHAINMAIL_BOOTS;
            case CHAINMAIL_BOOTS -> Material.IRON_BOOTS;
            case IRON_BOOTS -> Material.DIAMOND_BOOTS;
            case DIAMOND_BOOTS -> Material.NETHERITE_BOOTS;
            // По умолчанию — без изменений
            default -> material;
        };
    }

}