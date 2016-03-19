package lgvalle.com.fluxtodo.flux.feature.todo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.github.bluzwong.myflux.lib.FluxResponse;
import com.github.bluzwong.myflux.lib.switchtype.ReceiveType;
import flux.Flux;
import lgvalle.com.fluxtodo.R;
import lgvalle.com.fluxtodo.model.Todo;

import java.util.ArrayList;
import java.util.List;

import static lgvalle.com.fluxtodo.flux.feature.todo.TodoRequester.Type.*;

public class TodoActivity extends AppCompatActivity {

    private EditText mainInput;
    private ViewGroup mainLayout;
    private TodoRecyclerAdapter listAdapter;
    private CheckBox mainCheck;
    private Toolbar toolbar;
    private ProgressBar progressBar;


    private TodoRequester requester;

    private List<Todo> todoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flux);
        initDependencies();
        setupView();
    }

    private void initDependencies() {
        requester = Flux.getRequester(this, TodoRequester.class);
    }

    private void setupView() {
        mainLayout = ((ViewGroup) findViewById(R.id.main_layout));
        mainInput = (EditText) findViewById(R.id.main_input);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        toolbar.setTitle("Flux todo");
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        Button mainAdd = (Button) findViewById(R.id.main_add);
        mainAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodo();
                resetMainInput();
            }
        });
        mainCheck = (CheckBox) findViewById(R.id.main_checkbox);
        mainCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAll();
            }
        });
        Button mainClearCompleted = (Button) findViewById(R.id.main_clear_completed);
        mainClearCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCompleted();
                resetMainCheck();
            }
        });


        RecyclerView mainList = (RecyclerView) findViewById(R.id.main_list);
        mainList.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new TodoRecyclerAdapter(requester);
        mainList.setAdapter(listAdapter);
    }



    private void addTodo() {
        if (validateInput()) {
            progressBar.setVisibility(View.VISIBLE);
            requester.create(getInputText());
        }
    }

    private void checkAll() {
        progressBar.setVisibility(View.VISIBLE);
        requester.updateCompleteAll();
    }

    private void clearCompleted() {
        progressBar.setVisibility(View.VISIBLE);
        requester.destroyCompleted();
    }


    @ReceiveType(type = {
            TODO_CREATE,
            TODO_UNDO_DESTROY,
            TODO_COMPLETE,
            TODO_UNDO_COMPLETE,
            TODO_DESTROY_COMPLETED,
            TODO_TOGGLE_COMPLETE_ALL,
    })
    public void onUpdate(FluxResponse response) {
        todoList = response.getOnly();
        listAdapter.setItems(todoList);
        progressBar.setVisibility(View.GONE);
    }

    @ReceiveType(type = TODO_DESTROY)
    public void onDeleteTodo(FluxResponse response) {
        onUpdate(response);
        Snackbar snackbar = Snackbar.make(mainLayout, "Element deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                requester.undoDestroy();
            }
        });
        snackbar.show();
    }

    @ReceiveType(type = SET_PB)
    public void onSetPb(FluxResponse response) {
        boolean visible = response.getOnly();
       progressBar.setVisibility(visible? View.VISIBLE:View.GONE);
    }

    private void resetMainInput() {
        mainInput.setText("");
    }

    private void resetMainCheck() {
        if (mainCheck.isChecked()) {
            mainCheck.setChecked(false);
        }
    }

    private boolean validateInput() {
        return !TextUtils.isEmpty(getInputText());
    }

    private String getInputText() {
        return mainInput.getText().toString();
    }

}
