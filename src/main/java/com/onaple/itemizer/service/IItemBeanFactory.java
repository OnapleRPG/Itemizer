package com.onaple.itemizer.service;

import com.onaple.itemizer.data.beans.IItemBeanConfiguration;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public interface IItemBeanFactory {

    String getKeyId();

    IItemBeanConfiguration build(ConfigurationNode node);
}
