
package task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1 {
    public static void main(String[] args) {
        System.out.println("=== Завдання 1: Prototype ===");

        // Створюємо одного еталонного вовка
        Monster prototype = Monster.createPrototype();

        // Копіюємо еталон 5 разів
        List<Monster> wolves = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            wolves.add(prototype.copy());
        }

        // Змінюємо лише першого вовка
        Monster leader = wolves.get(0);
        leader.level = 10;
        leader.equipment.weapon = "Крижаний меч";
        leader.skills.add("Виття ватажка");
        leader.loot.add("Амулет ватажка");

        System.out.println("\nЕталон: " + prototype);

        for (int i = 0; i < wolves.size(); i++) {
            System.out.println("Вовк " + (i + 1)
                    + ": " + wolves.get(i));
        }

        // Перевірка незалежності копій
        if (prototype.skills.contains("Виття ватажка")
                || wolves.get(1).skills.contains("Виття ватажка")) {
            throw new IllegalStateException("Помилка копіювання!");
        }

        System.out.println("\nPrototype працює правильно!");
    }
}

// Екіпірування монстра
class Equipment {
    String weapon;

    Equipment(String weapon) {
        this.weapon = weapon;
    }

    Equipment copy() {
        return new Equipment(weapon);
    }
}

// Клас монстра
class Monster {
    String species;
    int level;
    String model;
    Equipment equipment;
    List<String> skills;
    List<String> loot;

    Monster(String species, int level, String model,
            Equipment equipment, List<String> skills,
            List<String> loot) {

        this.species = species;
        this.level = level;
        this.model = model;
        this.equipment = equipment;
        this.skills = skills;
        this.loot = loot;
    }

    // Створення еталонного монстра
    static Monster createPrototype() {
        System.out.println("Завантаження моделі вовка...");

        return new Monster(
                "Вовк",
                3,
                "wolf-model",
                new Equipment("Кігті"),
                new ArrayList<>(Arrays.asList("Укус", "Ривок")),
                new ArrayList<>(Arrays.asList("Хутро"))
        );
    }

    // Глибоке копіювання змінних даних
    Monster copy() {
        return new Monster(
                species,
                level,
                model,
                equipment.copy(),
                new ArrayList<>(skills),
                new ArrayList<>(loot)
        );
    }

    @Override
    public String toString() {
        return species
                + ", рівень=" + level
                + ", зброя=" + equipment.weapon
                + ", уміння=" + skills
                + ", лут=" + loot;
    }
}
