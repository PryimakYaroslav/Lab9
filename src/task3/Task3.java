
package task3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Task3 {
    public static void main(String[] args) {
        System.out.println("=== Завдання 3: Object Pool ===");

        ArenaPool pool = new ArenaPool(3);

        pool.requestMatch(new Match("Вовки", "Ворони"));
        pool.requestMatch(new Match("Лиси", "Орли"));
        pool.requestMatch(new Match("Маги", "Лицарі"));
        pool.requestMatch(new Match("Дракони", "Титани"));
        pool.requestMatch(new Match("Соколи", "Ведмеді"));

        System.out.println("\nАктивних матчів: " + pool.busyCount());
        System.out.println("У черзі: " + pool.waitingCount());

        // Імітуємо завершення матчів
        pool.finishMatch("A-1");
        pool.finishMatch("A-2");
        pool.finishMatch("A-3");
        pool.finishMatch("A-1");
        pool.finishMatch("A-2");

        System.out.println("\nСтворено арен: " + pool.createdCount());
        System.out.println("Матчів у черзі: " + pool.waitingCount());
    }
}

// Дані матчу
class Match {
    String teamA;
    String teamB;

    Match(String teamA, String teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
    }

    @Override
    public String toString() {
        return teamA + " проти " + teamB;
    }
}

// PvP-арена
class Arena {
    final String id;

    Match currentMatch;
    int scoreA;
    int scoreB;
    int timerSeconds;

    List<String> items = new ArrayList<>();

    Arena(String id) {
        this.id = id;
        System.out.println("Створено арену " + id);
    }

    // Початок матчу
    void startMatch(Match match) {
        if (currentMatch != null) {
            throw new IllegalStateException("Арена зайнята!");
        }

        currentMatch = match;
        System.out.println("Арена " + id + ": " + match);
    }

    // Скидання стану після завершення
    void reset() {
        currentMatch = null;
        scoreA = 0;
        scoreB = 0;
        timerSeconds = 0;
        items.clear();

        System.out.println("Арена " + id + " очищена.");
    }

    boolean isFree() {
        return currentMatch == null;
    }
}

// Пул із обмеженою кількістю арен
class ArenaPool {
    private final List<Arena> arenas = new ArrayList<>();
    private final Deque<Match> waitingQueue = new ArrayDeque<>();

    ArenaPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Некоректна кількість арен");
        }

        // Створюємо арени тільки один раз
        for (int i = 1; i <= capacity; i++) {
            arenas.add(new Arena("A-" + i));
        }
    }

    // Запит на проведення матчу
    void requestMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Матч відсутній");
        }

        Arena free = findFreeArena();

        if (free == null || !waitingQueue.isEmpty()) {
            waitingQueue.addLast(match);
            System.out.println("Матч " + match + " додано в чергу.");
            dispatch();
        } else {
            free.startMatch(match);
        }
    }

    // Завершення матчу
    void finishMatch(String arenaId) {
        Arena arena = getArena(arenaId);

        if (arena.isFree()) {
            throw new IllegalStateException("Арена вже вільна");
        }

        System.out.println("Завершено: " + arena.currentMatch);
        arena.reset();

        // Запускаємо наступний матч із черги
        dispatch();
    }

    // Видача вільної арени першому матчу в черзі
    private void dispatch() {
        Arena free;

        while (!waitingQueue.isEmpty()
                && (free = findFreeArena()) != null) {

            free.startMatch(waitingQueue.removeFirst());
        }
    }

    private Arena findFreeArena() {
        for (Arena arena : arenas) {
            if (arena.isFree()) {
                return arena;
            }
        }
        return null;
    }

    private Arena getArena(String id) {
        for (Arena arena : arenas) {
            if (arena.id.equals(id)) {
                return arena;
            }
        }

        throw new IllegalArgumentException("Арена не знайдена: " + id);
    }

    int createdCount() {
        return arenas.size();
    }

    int waitingCount() {
        return waitingQueue.size();
    }

    int busyCount() {
        int count = 0;

        for (Arena arena : arenas) {
            if (!arena.isFree()) {
                count++;
            }
        }

        return count;
    }
}
