package com.donzelitos.multtenancy.configuration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Instance {

    private String name;
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(name, instance.name) && Objects.equals(url, instance.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
