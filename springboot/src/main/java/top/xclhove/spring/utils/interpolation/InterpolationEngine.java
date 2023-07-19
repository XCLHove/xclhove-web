package top.xclhove.spring.utils.interpolation;

public interface InterpolationEngine {
    public interface Bindings {
        Object get(String name);
    }

    String combine(String template, Bindings bindings);
}
