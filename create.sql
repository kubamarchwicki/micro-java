create table todos (
  todo_id BIGINT NOT NULL AUTO_INCREMENT,
  todo_title VARCHAR(100) NOT NULL,
  todo_order BIGINT NOT NULL,
  todo_completed BIT(1) default false,
  PRIMARY KEY(todo_id)
);