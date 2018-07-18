package com.msemu.world.client.utils;

import com.msemu.world.enums.SerializedFunctionType;

public abstract class ISerializedFunction<T> {

    public abstract SerializedFunctionType getFunctionType();

    public abstract void  call(T object);
}
