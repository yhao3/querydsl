/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.Iterator;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryModifiers;

/**
 * LimitingIterator provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class LimitingIterator<E> implements Iterator<E> {

    public static <T> Iterator<T> create(Iterator<T> iterator, QueryModifiers modifiers) {
        if (modifiers.isRestricting()) {
            if (modifiers.getOffset() != null) {
                int counter = 0;
                while (iterator.hasNext() && counter < modifiers.getOffset()) {
                    counter++;
                    iterator.next();
                }
            }
            if (modifiers.getLimit() != null) {
                iterator = new LimitingIterator<T>(iterator, modifiers.getLimit());
            }
        }
        return iterator;
    }

    private long counter;

    private final long limit;

    private final Iterator<E> original;

    LimitingIterator(Iterator<E> iterator, long limit) {
        this.original = Assert.notNull(iterator);
        this.limit = limit;
    }

    @Override
    public boolean hasNext() {
        return original.hasNext() && counter < limit;
    }

    @Override
    public E next() {
        counter++;
        return original.next();
    }

    @Override
    public void remove() {

    }

}
