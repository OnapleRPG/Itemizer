package com.onaple.itemizer.utils;

import cz.neumimto.config.blackjack.and.hookers.NotSoStupidObjectMapper;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

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
}
