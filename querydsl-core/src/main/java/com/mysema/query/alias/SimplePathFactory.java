/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang.StringUtils;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PBooleanArray;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComparableArray;
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PStringArray;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadata;

/**
 * SimplePathFactory is a PathFactory implementation for the creation of Path
 * instances
 * 
 * @author tiwe
 * @version $Id$
 */
class SimplePathFactory implements PathFactory {

    private static class PathFactory<K, V> extends LazyMap<K, V> {

        private static final long serialVersionUID = -2443097467085594792L;

        protected PathFactory(Transformer<K, V> transformer) {
            super(new WeakHashMap<K, V>(), transformer);
        }

    }

    private final PString str = new PString(PathMetadata.forVariable("str"));

    private final PBoolean btrue = new PBoolean(md()), bfalse = new PBoolean(
            md());

    private long counter = 0;

    private final Map<Object, PBooleanArray> baToPath = new PathFactory<Object, PBooleanArray>(
            new Transformer<Object, PBooleanArray>() {
                public PBooleanArray transform(Object arg) {
                    return new PBooleanArray(md());
                }
            });

    private final Map<Object, PComparableArray<?>> caToPath = new PathFactory<Object, PComparableArray<?>>(
            new Transformer<Object, PComparableArray<?>>() {
                @SuppressWarnings("unchecked")
                public PComparableArray<?> transform(Object arg) {
                    return new PComparableArray(((List) arg).get(0).getClass(),
                            md());
                }
            });

    private final Map<Collection<?>, PEntityCollection<?>> ecToPath = new PathFactory<Collection<?>, PEntityCollection<?>>(
            new Transformer<Collection<?>, PEntityCollection<?>>() {
                @SuppressWarnings("unchecked")
                public PEntityCollection<?> transform(Collection<?> arg) {
                    if (!arg.isEmpty()) {
                        Class<?> cl = ((Collection) arg).iterator().next().getClass();
                        return new PEntityCollection(cl, cl.getSimpleName(), md());
                    } else {
                        return new PEntityCollection(Object.class, "Object", md());
                    }
                }
            });

    private final Map<List<?>, PEntityList<?,?>> elToPath = new PathFactory<List<?>, PEntityList<?,?>>(
            new Transformer<List<?>, PEntityList<?,?>>() {
                @SuppressWarnings({ "unchecked", "serial" })
                public PEntityList<?,?> transform(List<?> arg) {
                    final Class<?> cl = arg.isEmpty() ?  Object.class : arg.get(0).getClass();
                    return new PEntityList<Object,PEntity<Object>>(Object.class, null, md()){                        
                        @Override
                        public PEntity get(Expr<Integer> index) {
                            return new PEntity(cl, cl.getSimpleName(), 
                                    PathMetadata.forListAccess(this, index));
                        }
                        @Override
                        public PEntity get(int index) {
                            return new PEntity(cl, cl.getSimpleName(), 
                                    PathMetadata.forListAccess(this, index));
                        }
                    };
                }
            });

    private final Map<Map<?, ?>, PComponentMap<?, ?>> emToPath = new PathFactory<Map<?, ?>, PComponentMap<?, ?>>(
            new Transformer<Map<?, ?>, PComponentMap<?, ?>>() {
                @SuppressWarnings("unchecked")
                public PComponentMap<?, ?> transform(Map<?, ?> arg) {
                    if (!arg.isEmpty()) {
                        Map.Entry entry = arg.entrySet().iterator().next();
                        Class<?> keyType = entry.getKey().getClass();
                        Class<?> valueType = entry.getValue().getClass();
                        return new PComponentMap(keyType, valueType, md());
                    } else {
                        return new PComponentMap(Object.class, Object.class, md());
                    }
                }

            });

    private final Map<Object, PComparable<?>> comToPath = new PathFactory<Object, PComparable<?>>(
            new Transformer<Object, PComparable<?>>() {
                @SuppressWarnings("unchecked")
                public PComparable<?> transform(Object arg) {
                    return new PComparable(arg.getClass(), md());
                }
            });

    private final Map<Object, PNumber<?>> numToPath = new PathFactory<Object, PNumber<?>>(
            new Transformer<Object, PNumber<?>>() {
                @SuppressWarnings("unchecked")
                public PNumber<?> transform(Object arg) {
                    return new PNumber(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PDate<?>> dateToPath = new PathFactory<Object, PDate<?>>(
            new Transformer<Object, PDate<?>>() {
                @SuppressWarnings("unchecked")
                public PDate<?> transform(Object arg) {
                    return new PDate(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PDateTime<?>> dateTimeToPath = new PathFactory<Object, PDateTime<?>>(
            new Transformer<Object, PDateTime<?>>() {
                @SuppressWarnings("unchecked")
                public PDateTime<?> transform(Object arg) {
                    return new PDateTime(arg.getClass(), md());
                }
            });
    
    private final Map<Object, PTime<?>> timeToPath = new PathFactory<Object, PTime<?>>(
            new Transformer<Object, PTime<?>>() {
                @SuppressWarnings("unchecked")
                public PTime<?> transform(Object arg) {
                    return new PTime(arg.getClass(), md());
                }
            });

    private final Map<Object, PStringArray> saToPath = new PathFactory<Object, PStringArray>(
            new Transformer<Object, PStringArray>() {
                public PStringArray transform(Object arg) {
                    return new PStringArray(md());
                }
            });

    private final Map<Object, PEntity<?>> entityToPath = new PathFactory<Object, PEntity<?>>(
            new Transformer<Object, PEntity<?>>() {
                @SuppressWarnings("unchecked")
                public PEntity<?> transform(Object arg) {
                    return new PEntity(arg.getClass(), arg.getClass()
                            .getSimpleName(), md());
                }
            });

    private final Map<String, PString> strToPath = new PathFactory<String, PString>(
            new Transformer<String, PString>() {
                public PString transform(String str) {
                    return new PString(md());
                }
            });

    public <D> Expr<D> createAny(D arg) {
        throw new UnsupportedOperationException();
    }

    public PBoolean createBoolean(Boolean arg) {
        return arg.booleanValue() ? btrue : bfalse;
    }

    public PBooleanArray createBooleanArray(Boolean[] args) {
        return baToPath.get(Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    public <D extends Comparable<?>> PComparable<D> createComparable(D arg) {
        return (PComparable<D>) comToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D extends Comparable<?>> PComparableArray<D> createComparableArray(D[] args) {
        return (PComparableArray<D>) caToPath.get(Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDate<D> createDate(D arg) {
        return (PDate<D>) dateToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDateTime<D> createDateTime(D arg) {
        return (PDateTime<D>) dateTimeToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg) {
        return (PEntity<D>) entityToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D> PEntityCollection<D> createEntityCollection(Collection<D> arg) {
        return (PEntityCollection<D>) ecToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <K, V> PComponentMap<K, V> createMap(Map<K, V> arg) {
        return (PComponentMap<K, V>) emToPath.get(arg);
    }

    @SuppressWarnings("unchecked")
    public <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg) {
        return (PNumber<D>) numToPath.get(arg);
    }

    public PString createString(String arg) {
        return StringUtils.isEmpty(arg) ? str : strToPath.get(arg);
    }

    public PStringArray createStringArray(String[] args) {
        return saToPath.get(Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PTime<D> createTime(D arg) {
        return (PTime<D>) timeToPath.get(arg);
    }

    private PathMetadata<String> md() {
        return PathMetadata.forVariable("v" + String.valueOf(++counter));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D> PEntityList<D,?> createList(List<D> arg) {
        return (PEntityList<D,?>) elToPath.get(arg);
    }

}
