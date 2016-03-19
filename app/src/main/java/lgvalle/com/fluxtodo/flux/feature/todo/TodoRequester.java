package lgvalle.com.fluxtodo.flux.feature.todo;

import com.github.bluzwong.myflux.lib.FluxFragmentRequester;
import lgvalle.com.fluxtodo.flux.action.TodoAction;

import static lgvalle.com.fluxtodo.flux.feature.todo.TodoRequester.Type.*;

/**
 * Created by Bruce-Home on 2016/3/19.
 */
public class TodoRequester extends FluxFragmentRequester {
    public interface Type {
        String TODO_CREATE = "TODO_CREATE";
        String TODO_DESTROY = "TODO_DESTROY";
        String TODO_UNDO_DESTROY = "TODO_UNDO_DESTROY";
        String TODO_COMPLETE = "TODO_COMPLETE";
        String TODO_UNDO_COMPLETE = "TODO_UNDO_COMPLETE";
        String TODO_DESTROY_COMPLETED = "TODO_DESTROY_COMPLETED";
        String TODO_TOGGLE_COMPLETE_ALL = "TODO_TOGGLE_COMPLETE_ALL";
        String SET_PB = "SET_PB";
    }

    public String create(final String text) {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.addTodo(text);
                newFluxResponse(TODO_CREATE, uuid)
                        .putOnly(TodoAction.getTodoList())
                        .post();
            }
        });
    }

    public String destroy(final long id) {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.destroyTodo(id);
                newFluxResponse(TODO_DESTROY, uuid).putOnly(TodoAction.getTodoList()).post();
            }
        });
    }

    public String undoDestroy() {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.undoDestroy();
                newFluxResponse(TODO_UNDO_DESTROY, uuid)
                        .putOnly(TodoAction.getTodoList()).post();
            }
        });
    }

    public String complete(final long id) {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.setComplete(id, true);
                newFluxResponse(TODO_COMPLETE, uuid)
                        .putOnly(TodoAction.getTodoList()).post();
            }
        });
    }

    public String undoComplete(final long id) {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.setComplete(id, false);
                newFluxResponse(TODO_UNDO_COMPLETE, uuid)
                        .putOnly(TodoAction.getTodoList()).post();
            }
        });
    }

    public String destroyCompleted() {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.destroyCompleted();
                newFluxResponse(TODO_DESTROY_COMPLETED, uuid)
                        .putOnly(TodoAction.getTodoList()).post();
            }
        });
    }

    public String updateCompleteAll() {
        return doRequestIO(new RequestAction() {
            @Override
            public void request(String uuid) {
                TodoAction.updateCompleteAll();
                newFluxResponse(TODO_TOGGLE_COMPLETE_ALL, uuid)
                        .putOnly(TodoAction.getTodoList()).post();
            }
        });
    }
    public void setProgressBar(final boolean visible) {
        doRequestIO(new RequestAction() {
            @Override
            public void request(String s) {

                newFluxResponse(SET_PB, s)
                        .putOnly(visible).post();
            }
        });
    }
}
