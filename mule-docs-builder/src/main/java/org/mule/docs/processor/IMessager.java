package org.mule.docs.processor;

/**
 * Created by Mulesoft.
 */
public interface IMessager {

    void error(String message);
    void error(String message,Throwable e);

    void warning(String message);
    void warning(String message,Throwable e);

    void info(String message);
    void info(String message,Throwable e);
}
