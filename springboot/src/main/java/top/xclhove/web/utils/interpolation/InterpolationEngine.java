package top.xclhove.web.utils.interpolation;

public interface InterpolationEngine {
    public interface Bindings {
        Object get(String name);
    }

    String combine(String template, Bindings bindings);
}
