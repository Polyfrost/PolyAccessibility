package cc.polyfrost.polyaccessibility.pauliefill;

public @interface PolyInject {
    //TODO: transfer this to pauliefill, make it work
    public Class<?>[] value() default { };
}
