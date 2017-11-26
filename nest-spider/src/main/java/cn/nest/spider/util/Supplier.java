package cn.nest.spider.util;


@FunctionalInterface
public interface Supplier<T> {

	/**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;
}
