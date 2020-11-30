package com.kacstudios.game.sounds;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GameSounds {
    public static class SoundWrapper {
        private Sound sound;
        private ArrayList<Long> playIds = new ArrayList<>();
        private float volumeAdjustment;

        public SoundWrapper(Sound sound, float volumeAdjustment) {
            this.sound = sound;
            this.volumeAdjustment = volumeAdjustment;
        }

        public SoundWrapper(Sound sound) {
            this(sound, 1);
        }

        public void setVolume(float volume) {
            playIds.forEach(i -> sound.setVolume(i, volume * volumeAdjustment));
        }

        public long play(boolean looping) {
            long id = sound.play();
            sound.setLooping(id, looping);
            playIds.add(id);

            sound.setVolume(id, sfxVolume * volumeAdjustment);
            return id;
        }

        public void resume(long id) {
            sound.resume(id);
        }

        public void pause(long id) {
            sound.pause(id);
        }

        public void stop(long id) {
            sound.stop(id);
            playIds.remove(id);
        }

        public Long[] getIds() {
            Long[] ids = new Long[playIds.size()];
            playIds.toArray(ids);
            return ids;
        }

        public void stopAll() {
            sound.stop();
        }
    }

    private static float musicVolume = .2f;
    private static float sfxVolume = .5f;

    public static Music birdsChirping = Gdx.audio.newMusic(Gdx.files.internal("sounds-music/gamenoise.ogg"));
    public static Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds-music/Music.ogg"));

    public static SoundWrapper tractorSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/tractor-engine.ogg")), .6f);
    public static SoundWrapper plantingSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/planting-sounds.ogg")));
    public static SoundWrapper harvestSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/harvest-sounds.ogg")));
    public static SoundWrapper walkingSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/walking.ogg")), .3f);
    public static SoundWrapper wateringSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/watering_effect.ogg")));
    public static SoundWrapper fireSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/fire.ogg")));
    public static SoundWrapper waterExtinguishSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/water-extinguish.ogg")));
    public static SoundWrapper insectSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/insects.ogg")));
    public static SoundWrapper fillBucketSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/fill-bucket.ogg")), .7f);
    public static SoundWrapper chaChingSound = new SoundWrapper(
            Gdx.audio.newSound(Gdx.files.internal("sounds-music/cha-ching.ogg")));

    private static SoundWrapper[] sfx = new SoundWrapper[] {
            tractorSound,
            plantingSound,
            harvestSound,
            walkingSound,
            wateringSound,
            fireSound,
            waterExtinguishSound,
            insectSound,
            fillBucketSound,
            chaChingSound
    };

    /**
     * Volume between 0 and 1
     */
    public static void startMusic() {
        birdsChirping.setLooping(true);
        birdsChirping.play();

        gameMusic.setLooping(true);
        gameMusic.play();
    }

    public static void setMusicVolume(float musicVolume) {
        GameSounds.musicVolume = musicVolume;

        birdsChirping.setVolume(musicVolume * 3);
        gameMusic.setVolume(musicVolume);
    }

    public static void setSfxVolume(float sfxVolume) {
        GameSounds.sfxVolume = sfxVolume;

        for (SoundWrapper sfx : sfx) {
            sfx.setVolume(sfxVolume * .6f);
        }
    }

    /**
     * Overrides default static global variables for settings only if an options file is found
     */
    public static void loadGlobalSettingsFromFile() {
        FileHandle settingsFile = Gdx.files.getFileHandle("saves/options.mcconnell", Files.FileType.External);
        if (settingsFile.exists()) {
            String fileLine;
            String[] splitFileLine;

            try (Scanner fileScanner = new Scanner(settingsFile.reader())) {

                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                setSfxVolume(Float.parseFloat(splitFileLine[1]));

                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                setMusicVolume(Float.parseFloat(splitFileLine[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveGlobalSettingsToFile() {
        FileHandle settingsFile = Gdx.files.getFileHandle("saves/options.mcconnell", Files.FileType.External);
        try (Writer fileWriter = settingsFile.writer(false)){
            fileWriter.write("GameVolume,"+sfxVolume);
            fileWriter.write("\nMusicVolume,"+musicVolume);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setDefaults() {
        setMusicVolume(.2f);
        setSfxVolume(.5f);
        saveGlobalSettingsToFile();
    }

    public static void stopGameSounds() {
        for (SoundWrapper soundWrapper : sfx) {
            soundWrapper.stopAll();
        }
    }
}
