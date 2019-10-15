package com.onaple.itemizer;

import cz.neumimto.config.blackjack.and.hookers.NotSoStupidObjectMapper;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;

import java.nio.file.Path;

import static org.checkerframework.checker.units.UnitsTools.s;

public class ConfigUtils {

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, Path path) {
        try {
            ObjectMapper mapper = NotSoStupidObjectMapper.forClass(clazz);
            HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
            return (T) mapper.bind(clazz.getConstructor().newInstance()).populate(hcl.load());
        } catch (Exception e) {
            throw new RuntimeException("Could not load file " + s, e);
        }
    }
}
