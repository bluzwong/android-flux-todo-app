package lgvalle.com.fluxtodo.flux.api;

import android.os.SystemClock;
import lgvalle.com.fluxtodo.model.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce-Home on 2016/3/19.
 */
public enum SimpleApi {
    instance;

    private Todo lastDeleted;

    private final List<Todo> todos = new ArrayList<>();

    public void addTodo(Todo todo) {
        SystemClock.sleep(1000);
        todos.add(todo);
    }

    public List<Todo> getTodos() {
        SystemClock.sleep(1000);
        return todos;
    }


    public Todo getLastDeleted() {
        return lastDeleted;
    }

    public void setLastDeleted(Todo lastDeleted) {
        this.lastDeleted = lastDeleted;
    }
}
