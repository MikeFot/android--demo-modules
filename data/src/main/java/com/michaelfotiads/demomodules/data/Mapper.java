package com.michaelfotiads.demomodules.data;

import java.util.List;

abstract class Mapper<T, D> {

    public abstract List<T> map(List<D> apiItems);

    public abstract T map(D apiItems);
}
