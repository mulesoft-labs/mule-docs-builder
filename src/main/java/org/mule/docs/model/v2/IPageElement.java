package org.mule.docs.model.v2;

/**
 * Created by Mulesoft.
 */
public interface IPageElement {

    void accept(IPageElementVisitor visitor);
}
