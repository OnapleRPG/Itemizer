package com.onaple.itemizer;

import static org.checkerframework.checker.units.UnitsTools.s;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;

import java.nio.file.Path;

public class ConfigUtils {

    public static <T> T load(Class<T> clazz, Path path) {
        try {
            ObjectMapper mapper = ObjectMapper.forClass(clazz);
            HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(path).build();
            return (T) mapper.bind(clazz.getConstructor().newInstance()).populate(hcl.load());
        } catch (Exception e) {
            throw new RuntimeException("Could not load file " + s, e);
        }
    }
}
