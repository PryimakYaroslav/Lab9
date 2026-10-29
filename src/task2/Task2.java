
package task2;

public class Task2 {
    public static void main(String[] args) {
        System.out.println("=== Завдання 2: Singleton ===");

        WeatherSystem weather = new WeatherSystem();
        MonsterSpawnSystem spawner = new MonsterSpawnSystem();
        TradingSystem trading = new TradingSystem();
        QuestSystem quests = new QuestSystem();

        // Погода змінює спільний час
        weather.setNightTime();

        // Інші системи бачать той самий час
        spawner.spawnMonsters();
        trading.showTime();
        quests.showTime();

        // Перевіряємо, що об'єкт один
        boolean sameInstance =
                weather.getClock() == spawner.getClock()
                        && spawner.getClock() == trading.getClock()
                        && trading.getClock() == quests.getClock();

        System.out.println("\nОдин об'єкт часу: " + sameInstance);
    }
}

// Singleton — єдиний ігровий годинник
final class WorldClock {

    // Створюємо єдиний екземпляр
    private static final WorldClock INSTANCE = new WorldClock();

    private int hour = 12;

    // Забороняємо створювати нові об'єкти ззовні
    private WorldClock() {
        System.out.println("Ігровий годинник створено.");
    }

    // Повертаємо завжди один об'єкт
    public static WorldClock getInstance() {
        return INSTANCE;
    }

    public synchronized void setHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Некоректна година");
        }

        this.hour = hour;
    }

    public synchronized String getTime() {
        return String.format("%02d:00", hour);
    }

    public synchronized boolean isNight() {
        return hour >= 20 || hour < 6;
    }
}

// Підсистема погоди
class WeatherSystem {
    private final WorldClock clock = WorldClock.getInstance();

    public void setNightTime() {
        clock.setHour(22);
        System.out.println("Погода: настала ніч, час " + clock.getTime());
    }

    public WorldClock getClock() {
        return clock;
    }
}

// Підсистема спавну монстрів
class MonsterSpawnSystem {
    private final WorldClock clock = WorldClock.getInstance();

    public void spawnMonsters() {
        if (clock.isNight()) {
            System.out.println("Спавн нічних монстрів.");
        } else {
            System.out.println("Спавн денних монстрів.");
        }
    }

    public WorldClock getClock() {
        return clock;
    }
}

// Підсистема торгівлі
class TradingSystem {
    private final WorldClock clock = WorldClock.getInstance();

    public void showTime() {
        System.out.println("Торгівля: час " + clock.getTime());
    }

    public WorldClock getClock() {
        return clock;
    }
}

// Підсистема квестів
class QuestSystem {
    private final WorldClock clock = WorldClock.getInstance();

    public void showTime() {
        System.out.println("Квести: час " + clock.getTime());
    }

    public WorldClock getClock() {
        return clock;
    }
}
