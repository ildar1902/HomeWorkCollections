package collections.myArrayList;

import java.util.Iterator;

public class MySimpleArrayList<E> implements MySimpleList<E> {
    private E[] values;

    public MySimpleArrayList() {
        values = (E[]) new Object[0];
    }

    /***
     * метод добавления элемента в коллекцию
     * @param e в качестве параметра принимается элемент, который хотим добавить
     * @return при успешном добавлении возвращает true
     */

    @Override
    public boolean add(E e) {
        try {
            E[] temp = values;
            values = (E[]) new Object[temp.length + 1];
            System.arraycopy(temp, 0, values, 0, temp.length);
            values[values.length - 1] = e;
            return true;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * метод удаления элемента по индексу
     * @param index - в качестве параметра принимается индекс ячейки,
     *             элемент которой хотим удалить
     */
    @Override
    public void delete(int index) {
        try {
            E[] temp = values;
            values = (E[]) new Object[temp.length - 1];
            System.arraycopy(temp, 0, values, 0, index);
            System.arraycopy(temp, index + 1, values, index, temp.length - index - 1);

        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * метод получения элемента по индексу
     * @param index - в качестве параметра принимается индекс ячейки,
     *      *             элемент которой хотим получить
     * @return возвращает элемент, находяшийся в ячейке,
     * индекс которой указывается в параметре
     */
    @Override
    public E get(int index) {
        return values[index];
    }

    /**
     * метод, указывающий размер коллекции
     * @return возвращает размер коллекции, в виде числа типа int
     */
    @Override
    public int size() {
        return values.length;
    }

    /**
     * метод изменения элементов в коллекции по индексу
     * @param index индекс ячейки, в которой хотим изменить элемент
     * @param e новый элемент, который заменит тот, что был в ячейке
     */
    @Override
    public void update(int index, E e) {
        values[index] = e;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyArrayIterator<>(values);
    }
}
