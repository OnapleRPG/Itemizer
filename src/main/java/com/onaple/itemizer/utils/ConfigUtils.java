package com.onaple.itemizer.utils;

import cz.neumimto.config.blackjack.and.hookers.NotSoStupidObjectMapper;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.onaple.itemizer.Itemizer;

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
                Itemizer.getLogger().error("Could not load file " + path.getFileName(), e);
            }
        });
        return loadedFiles;
    }

    /**
    * Recursion limit to avoid unexpected long loops
    **/
    private static int READ_RECURSION_LIMIT = 3;
    /**
    /**
    * Get all config Files or subfolders until recursion is reached
    **/
    private static List<Path> getFilesFromPath(Path path) {
        List<Path> filesFound = new ArrayList<>();
        if (Files.exists(path)) {
            try {
                filesFound.addAll(Files.walk(path, READ_RECURSION_LIMIT)
                    .filter(Files::isRegularFile)
                    .distinct()
                    .collect(Collectors.toList()));
            } catch (IOException e) {
                Itemizer.getLogger().error("Error when reading configuration files from folder", e);
            }
        }
        return filesFound;
    }
}

