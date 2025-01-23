import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CalendarApp extends Application {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private LocalDate currentDate = LocalDate.now();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calendar App");

        // Главный контейнер
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(10));
        mainContainer.setPrefSize(WIDTH, HEIGHT);

        // Панель управления (месяц + кнопки)
        HBox controlPanel = new HBox(10);
        controlPanel.setAlignment(Pos.CENTER);

        Button prevButton = new Button("◀");
        Button nextButton = new Button("▶");
        Label monthLabel = new Label();

        // Сетка календаря
        GridPane calendarGrid = new GridPane();
        calendarGrid.setAlignment(Pos.TOP_CENTER);

        // Настройка растягивания
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100/7.0); // 7 колонок
        calendarGrid.getColumnConstraints().addAll(column, column, column, column, column, column, column);

        // Настройка размеров кнопок дней
        for (int i = 0; i < 6; i++) { // Максимум 6 строк
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/6.0);
            calendarGrid.getRowConstraints().add(row);
        }

        // Привязка размеров
        calendarGrid.prefWidthProperty().bind(mainContainer.widthProperty());
        calendarGrid.prefHeightProperty().bind(mainContainer.heightProperty().multiply(0.8));

        // Добавление элементов
        controlPanel.getChildren().addAll(prevButton, monthLabel, nextButton);
        mainContainer.getChildren().addAll(controlPanel, calendarGrid);

        // Обработчики событий
        prevButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar(monthLabel, calendarGrid);
        });

        nextButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar(monthLabel, calendarGrid);
        });

        // Первоначальное обновление
        updateCalendar(monthLabel, calendarGrid);

        // Настройка сцены
        Scene scene = new Scene(mainContainer, WIDTH, HEIGHT);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Запрет изменения размера
        primaryStage.show();
    }

    private void updateCalendar(Label monthLabel, GridPane grid) {
        grid.getChildren().clear();
        monthLabel.setText(currentDate.getMonth() + " " + currentDate.getYear());

        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7; // 0 = Sunday
        int daysInMonth = currentDate.lengthOfMonth();

        // Заполнение дней
        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = new Button(String.valueOf(day));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayButton.setStyle("-fx-font-size: 14;");

            // Подсветка текущей даты
            if (currentDate.withDayOfMonth(day).equals(LocalDate.now())) {
                dayButton.setStyle("-fx-color: #90EE90; -fx-font-weight: bold;");
            }

            int row = (startDay + day - 1) / 7;
            int column = (startDay + day - 1) % 7;

            grid.add(dayButton, column, row);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}