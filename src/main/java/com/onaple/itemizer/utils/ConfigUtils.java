package com.onaple.itemizer.utils;

import cz.neumimto.config.blackjack.and.hookers.NotSoStupidObjectMapper;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.checkerframework.checker.units.UnitsTools.s;

public class ConfigUtils {

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, Path path) throws IOException, ObjectMappingException {
        try {
            ObjectMapper mapper = NotSoStupidObjectMapper.forClass(clazz);
            HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
            return (T) mapper.bind(clazz.getConstructor().newInstance()).populate(hcl.load());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Could not load file " + s, e);
        }
    }

    /**
    * Replicates "load" method but allowing folders
    **/
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadMultiple(Class<T> clazz, Path initialPath) throws IOException, ObjectMappingException {
        List<T> loadedFiles = new ArrayList<>();
        List<Path> paths = getFilesFromPath(initialPath);
        paths.forEach(path -> {
            try {
                ObjectMapper mapper = NotSoStupidObjectMapper.forClass(clazz);
                HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
                loadedFiles.add((T) mapper.bind(clazz.getConstructor().newInstance()).populate(hcl.load()));
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ObjectMappingException | IOException e) {
                throw new RuntimeException("Could not load file " + s, e);
            }
        });
        return loadedFiles;
    }

    /**
    * Recursion limit to avoid unexpected long loops
    **/
    private static int READ_RECURSION_LIMIT = 3;
    /**
    * Get all config paths
    **/
    private static List<Path> getFilesFromPath(Path path) {
        return getFilesFromPath(path, 0);
    }
    /**
    * Get all config Files or subfolders until recursion is reached
    **/
    private static List<Path> getFilesFromPath(Path path, int recursion) {
        // Initializing list and stopping if exceeding recursion imit
        List<Path> filesFound = new ArrayList<>();
        if (recursion <= READ_RECURSION_LIMIT) {
            return filesFound;
        }
        // Checking configuration file or folder matching the given name
        if (Files.exists(Paths.get(path + ".conf"))) {
            filesFound.add(Paths.get(path + ".conf"));
        }
        if (Files.exists(path)) {
            File file = path.toFile();
            if (file.isDirectory()) {
                for (String fileName: file.list()) {
                    filesFound.addAll(getFilesFromPath(Path.of(fileName), recursion + 1));
                }
            }
        }
        return filesFound;
    }
}

