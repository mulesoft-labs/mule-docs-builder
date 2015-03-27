package org.mule.docs.model;

/**
 * Created by Mulesoft.
 */
public interface IPageElement {

    void accept(IPageElementVisitor visitor);
}
