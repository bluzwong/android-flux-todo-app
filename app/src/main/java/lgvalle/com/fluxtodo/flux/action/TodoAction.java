package lgvalle.com.fluxtodo.flux.action;

import android.os.SystemClock;
import lgvalle.com.fluxtodo.flux.api.SimpleApi;
import lgvalle.com.fluxtodo.model.Todo;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Bruce-Home on 2016/3/19.
 */
public class TodoAction {

    public static void addTodo(String text) {
        Todo todo = new Todo(System.currentTimeMillis(), text);
        SimpleApi.instance.addTodo(todo);
    }

    public static List<Todo> getTodoList() {
        return SimpleApi.instance.getTodos();
    }

    public static void destroyTodo(long id) {
        List<Todo> todos = SimpleApi.instance.getTodos();
        Iterator<Todo> iter = todos.iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.getId() == id) {
                SimpleApi.instance.setLastDeleted(todo.clone());
                iter.remove();
                break;
            }
        }
    }

    public static void undoDestroy() {
        SimpleApi.instance.addTodo(SimpleApi.instance.getLastDeleted().clone());
    }

    public static Todo getById(long id) {
        Iterator<Todo> iter = getTodoList().iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    public static void setComplete(long id, boolean complete) {
        Todo todo = getById(id);
        if (todo != null) {
            todo.setComplete(complete);
        }
    }

    public static void destroyCompleted() {
        Iterator<Todo> iter = getTodoList().iterator();
        while (iter.hasNext()) {
            Todo todo = iter.next();
            if (todo.isComplete()) {
                iter.remove();
            }
        }
    }


    private static boolean areAllComplete() {
        for (Todo todo : getTodoList()) {
            if (!todo.isComplete()) {
                return false;
            }
        }
        return true;
    }

    private static void updateAllComplete(boolean complete) {
        for (Todo todo : getTodoList()) {
            todo.setComplete(complete);
        }
    }

    public static void updateCompleteAll() {
        if (areAllComplete()) {
            updateAllComplete(false);
        } else {
            updateAllComplete(true);
        }
    }
}
