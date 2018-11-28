package com.onaple.itemizer.service;

import com.onaple.itemizer.data.beans.IItemBeanConfiguration;
import ninja.leaping.configurate.ConfigurationNode;

public interface IItemBeanFactory {

    String getKeyId();

    IItemBeanConfiguration build(ConfigurationNode node);
}
