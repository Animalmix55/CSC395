package com.kacstudios.game.grid;

import com.kacstudios.game.disasters.Disaster;
import com.kacstudios.game.grid.plants.Plant;
import com.kacstudios.game.utilities.TimeEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DisasterSpawner {

    public interface DisasterSpawnMethod {
        Disaster spawn(Plant plant);
    }
    private static class DisasterWrapper {
        private DisasterSpawnMethod disaster;
        private int secondsPerSpawn;
        /**
         * The allowable deviation from the period in which the disaster may spawn
         */
        private float variationMargin = .2f;
        private LocalDateTime nextSpawnTime = TimeEngine.getDateTime();

        public DisasterWrapper(DisasterSpawnMethod disaster, int secondsPerSpawn) {
            this.disaster = disaster;
            this.secondsPerSpawn = secondsPerSpawn;
            calculateNextSpawnTime();
        }

        /**
         * Returns if the disaster is mature enough to be spawned again
         * @return
         */
        public boolean readyToSpawn() {
            return TimeEngine.getSecondsSince(nextSpawnTime) > 0;
        }

        /**
         * Returns the disaster period
         * @return
         */
        public int getPeriod() {
            return secondsPerSpawn;
        }

        /**
         * Uses the current time and margin information to calculate the next spawn time
         */
        private void calculateNextSpawnTime() {
            float percentVariation = Disaster.generateRandom(0, 100);
            nextSpawnTime = TimeEngine.getDateTime().plusSeconds((long) (secondsPerSpawn +
                    secondsPerSpawn * variationMargin * (percentVariation/100)));
        }

        public void spawnDisaster(Plant target) {
            calculateNextSpawnTime();
            if(Disaster.generateRandom(0, 10) < 3) return; // let there be a slim chance of not spawning at all

            target.setDisaster(disaster.spawn(target));
        }
    }

    /**
     * How often the spawner checks for disasters ready to spawn
     */
    private static final int updatePeriod = 20;
    private LocalDateTime lastRun = TimeEngine.getDateTime();
    private Grid grid;
    private ArrayList<DisasterWrapper> registeredDisasters = new ArrayList<>();

    public DisasterSpawner(Grid grid) {
        this.grid = grid;
    }

    public void registerDisaster(DisasterSpawnMethod disaster, int secondsPerSpawn) {
        registeredDisasters.add(new DisasterWrapper(disaster, secondsPerSpawn));
    }

    /**
     * To be run every frame to calculate disaster spawning
     */
    public void act() {
        if(TimeEngine.getSecondsSince(lastRun) <= updatePeriod) return; // only run every so many seconds

        DisasterWrapper readyDisaster = null;

        // figure out what disaster to install, prefer disasters with greater periods
        for (DisasterWrapper disaster: registeredDisasters) {
            if(disaster.readyToSpawn() && (readyDisaster == null || readyDisaster.getPeriod() < disaster.getPeriod())) {
                readyDisaster = disaster;
            }
        }

        if(readyDisaster == null) return;

        // linearize all of the plant squares so as to chose a random one
        ArrayList<Plant> linearizedPlantSquares = new ArrayList<>();
        GridSquare[][] gridSquares = grid.getGridSquares();
        for(int x = 0; x < gridSquares.length; x++) {
            for (int y = 0; y < gridSquares[x].length; y++) {
                GridSquare square = gridSquares[x][y];
                if(square != null && Plant.class.isAssignableFrom(square.getClass()) && !((Plant) square).getDead())
                    linearizedPlantSquares.add((Plant) square);
            }
        }

        Plant target = linearizedPlantSquares.get(Disaster.generateRandom(0, linearizedPlantSquares.size() - 1));
        readyDisaster.spawnDisaster(target);
    }
}
