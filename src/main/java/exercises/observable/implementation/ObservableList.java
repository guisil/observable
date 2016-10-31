package exercises.observable.implementation;

import exercises.observable.model.*;
import exercises.observable.model.Observable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Implementation of ArrayList with Observable capabilities.
 *
 * Created by guisil on 09/08/2016.
 */
public class ObservableList<T> extends ArrayList<T> implements Observable<T, ListListener<T>> {

    private final String name;

    private final Lock readLock;
    private final Lock writeLock;
    private Set<ListListener<T>> listeners;

    public ObservableList(String name) {
        super();
        this.name = name;
        this.listeners = new CopyOnWriteArraySet<>();

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    private ObservableList(ObservableList<T> list) {
        super(list);
        this.name = list.name;
        this.listeners = new CopyOnWriteArraySet<>();

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T element) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            if (!super.add(element)) {
                return false;
            }
            notification = ListNotification.newListAddNotification(
                    Collections.singletonList(element), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, T element) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            super.add(index, element);
            notification = ListNotification.newListAddNotification(
                    Collections.singletonList(element), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            if (!super.addAll(collection)) {
                return false;
            }
            notification = ListNotification.newListAddNotification(
                    new ArrayList<>(collection), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            if (!super.addAll(index, collection)) {
                return false;
            }
            notification = ListNotification.newListAddNotification(
                    new ArrayList<>(collection), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        if (this.isEmpty()) {
            return;
        }
        List<T> listBeforeClearing;
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            listBeforeClearing = new ArrayList<>(this);
            super.clear();
            notification = ListNotification.newListRemoveNotification(
                    listBeforeClearing, new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T remove(int index) {
        T removed;
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            removed = super.remove(index);
            notification = ListNotification.newListRemoveNotification(
                    Collections.singletonList(removed), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            if (!super.remove(element)) {
                return false;
            }
            notification = ListNotification.newListRemoveNotification(
                    Collections.singletonList( (T) element), new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            List<T> elementsToRemove =
                    collection.stream()
                            .filter(this::contains)
                            .map(element -> (T) element)
                            .collect(Collectors.toCollection(ArrayList::new));
            if (!super.removeAll(collection)) {
                return false;
            }
            notification = ListNotification.newListRemoveNotification(elementsToRemove, new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeIf(Predicate<? super T> filter) {

        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            List<T> filtered = this.stream()
                    .filter(filter)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (filtered.isEmpty()) {
                return false;
            }
            if (!super.removeIf(filter)) {
                return false;
            }
            notification = ListNotification.newListRemoveNotification(filtered, new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            List<T> elementsToRemove = new ArrayList<>(this.subList(fromIndex, toIndex));
            super.removeRange(fromIndex, toIndex);
            notification = ListNotification.newListRemoveNotification(elementsToRemove, new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        ListNotification<T> notification;
        this.writeLock.lock();
        try {
            List<T> elementsToRemove =
                    this.stream()
                            .filter(element -> !collection.contains(element))
                            .map(element -> element)
                            .collect(Collectors.toCollection(ArrayList::new));
            if (!super.retainAll(collection)) {
                return false;
            }
            notification = ListNotification.newListRemoveNotification(elementsToRemove, new ObservableList<>(this));
        } finally {
            this.writeLock.unlock();
        }
        notifyAllListeners(notification);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        this.readLock.lock();
        try {
            return super.clone();
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        this.readLock.lock();
        try {
            return super.contains(o);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ensureCapacity(int minCapacity) {
        this.writeLock.lock();
        try {
            super.ensureCapacity(minCapacity);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(int index) {
        this.readLock.lock();
        try {
            return super.get(index);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        this.readLock.lock();
        try {
            return super.indexOf(o);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        this.readLock.lock();
        try {
            return super.lastIndexOf(o);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        this.writeLock.lock();
        try {
            super.replaceAll(operator);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T set(int index, T element) {
        this.writeLock.lock();
        try {
            return super.set(index, element);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        this.readLock.lock();
        try {
            return super.size();
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        this.readLock.lock();
        try {
            return super.isEmpty();
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        this.readLock.lock();
        try {
            return super.subList(fromIndex, toIndex);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        this.readLock.lock();
        try {
            return super.toArray();
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T1> T1[] toArray(T1[] a) {
        this.readLock.lock();
        try {
            return super.toArray(a);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trimToSize() {
        this.writeLock.lock();
        try {
            super.trimToSize();
        } finally {
            this.writeLock.unlock();
        }
    }

    // Observable implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(ListListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(ListListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyAllListeners(ListNotification<T> notification) {
        listeners.forEach(listener -> listener.onListChange(notification));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.name + " - " + super.toString();
    }
}
