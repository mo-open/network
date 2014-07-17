
package org.mds.net.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A basic implementation of the builder pattern.  Mutable factory for
 * some immutable object which is composed of multiple objects of the same type.
 *
 * @author
 */
public abstract class AbstractBuilder<ElementType, CreateType> implements Builder<ElementType, CreateType, AbstractBuilder<ElementType, CreateType>> {
    protected final List<ElementType> elements = new LinkedList<>();
    protected AbstractBuilder() {

    }

    @Override
    public AbstractBuilder<ElementType, CreateType> add (ElementType element) {
        Checks.notNull("element", element);
        onBeforeAdd(element);
        elements.add (element);
        onAfterAdd(element);
        return this;
    }
    
    protected void onAfterAdd (ElementType element) {
        
    }

    @Override
    public final AbstractBuilder<ElementType, CreateType> add (String string) {
        Checks.notNull("string", string);
        return add (createElement(string));
    }

    protected List<ElementType> elements() {
        return new ArrayList<>(elements);
    }

    protected AbstractBuilder<ElementType, CreateType> addElement(ElementType element) {
        Checks.notNull("element", element);
        onBeforeAdd(element);
        elements.add(element);
        return this;
    }

    protected int size() {
        return elements.size();
    }
    protected abstract ElementType createElement(String string);
    /**
     * Hook which can be overridden when an element is added.
     * @param toAdd
     */
    protected void onBeforeAdd (ElementType toAdd) {
        //do nothing
    }
}
