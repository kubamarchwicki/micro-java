package pl.marchwicki.microjava.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "todos")
@NamedQueries({
        @NamedQuery(name = Todo.GET_ALL, query = "from Todo"),
        @NamedQuery(name = Todo.GET_BY_ID, query = "from Todo where id = :id")
})
public class Todo {

    public final static String GET_ALL = "Todo.getAll()";
    public final static String GET_BY_ID = "Todo.getById()";

    @Id
    @Column(name = "todo_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "todo_title")
    private String title;

    @Column(name = "todo_order")
    private long order;

    @Column(name = "todo_completed")
    private boolean completed;

    public Todo(){
    }

    private Todo(long id, String title, long order, boolean completed) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getOrder() {
        return order;
    }

    public boolean isCompleted() {
        return completed;
    }

    public static class TodoBuilder {
        private long id;
        private String title;
        private long order;
        private boolean completed;

        private TodoBuilder() {

        }

        public static TodoBuilder aTodo() {
            return new TodoBuilder();
        }

        public TodoBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public TodoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TodoBuilder withOrder(long order) {
            this.order = order;
            return this;
        }

        public TodoBuilder isCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Todo build() {
            return new Todo(id, title, order, completed);
        }
    }
}

