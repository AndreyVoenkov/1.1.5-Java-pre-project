package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.ResultSet;


public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("CREATE TABLE if not exists usersTable " +
                        "(id INT NOT NULL AUTO_INCREMENT, " +
                        "name VARCHAR(255), " +
                        "lastName VARCHAR(255), " +
                        "age INT UNSIGNED, " +
                        "PRIMARY KEY (id))")
        ) {
            statement.executeUpdate();
//                     System.out.println("Таблица создана");
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("DROP TABLE if exists usersTable")) {
            statement.executeUpdate();
            // System.out.println("Таблица удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("INSERT INTO usersTable (name, lastName, age) VALUES (?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблицы для добавления User не существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("DELETE FROM usersTable WHERE id = ?")) {
            statement.setLong(1, id);
            if (statement.executeUpdate() > 0) {
                System.out.println("User " + id + " удален");
            } else {
                System.out.println("User " + id + " не существует");
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблица для удаления User не существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("select * from usersTable");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getByte("age"));
                list.add(user);
                System.out.println(user);
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблица для вывода всех User'ов не существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("delete from usersTable")) {
            statement.executeUpdate();
            // System.out.println("Таблица очищена");
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблица для очистки не существует");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
