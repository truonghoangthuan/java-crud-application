package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextField textFieldAuthor;
    @FXML
    private TextField textFieldYear;
    @FXML
    private TextField textFieldPages;

    @FXML
    private TableView<Books> tableBook;
    @FXML
    private TableColumn<Books, Integer> colID;
    @FXML
    private TableColumn<Books, String> colTitle;
    @FXML
    private TableColumn<Books, String> colAuthor;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private TableColumn<Books, Integer> colPages;

    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private void buttonInsert(ActionEvent event) {
        if (event.getSource() == btnInsert) {
            System.out.println("Button insert");
            insertBook();
        }
    }

    @FXML
    private void buttonUpdate(ActionEvent event) {
        updateBook();
    }

    @FXML
    private void buttonDelete(ActionEvent event) {
        deleteBook();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java-crud-application", "root", "0974667060Th");
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ObservableList<Books> getBooksList() {
        ObservableList<Books> booksList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM books";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            Books books;
            while (resultSet.next()) {
                books = new Books(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("year"),
                        resultSet.getInt("pages"));
                booksList.add(books);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return booksList;
    }

    public void showBooks() {
        ObservableList<Books> booksList = getBooksList();

        colID.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));

        tableBook.setItems(booksList);
    }

    private void insertBook() {
        String query = "INSERT INTO books VALUES ("
                + textFieldID.getText() + ",'"
                + textFieldTitle.getText() + "','"
                + textFieldAuthor.getText() + "',"
                + textFieldYear.getText() + ","
                + textFieldPages.getText() + ")";
        executeQuery(query);
        showBooks();
    }

    private void updateBook() {
        String query = "UPDATE books SET " +
                "title = '" + textFieldTitle.getText() + "'," +
                "author = '" + textFieldAuthor.getText() + "'," +
                "year = " + textFieldYear.getText() + "," +
                "pages = " + textFieldPages.getText() +
                " WHERE id = " + textFieldID.getText() + "";
        System.out.println(query);
        executeQuery(query);
        showBooks();
    }

    private void deleteBook() {
        String query = "DELETE FROM books WHERE id = " + textFieldID.getText();
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
        }
    }
}
