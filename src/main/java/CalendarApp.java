import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.time.LocalDate;

public class CalendarApp extends Application {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private LocalDate currentDate = LocalDate.now();

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            // Корневой контейнер с фоновым размытием
            StackPane root = new StackPane();

            // Фон с размытием
            Rectangle background = new Rectangle(WIDTH, HEIGHT);
            background.setArcWidth(25);
            background.setArcHeight(25);
            background.setFill(Color.WHITE);
            background.setEffect(new GaussianBlur(15));

            // Основной контент
            VBox mainContainer = createMainContent();

            root.getChildren().addAll(background, mainContainer);

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scene.setFill(null); // Прозрачная сцена
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createMainContent() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(15));
        mainContainer.setPrefSize(WIDTH, HEIGHT);
        mainContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: rgba(0,0,0,0.1);"
        );

        HBox controlPanel = new HBox(10);
        controlPanel.setAlignment(Pos.CENTER);

        Button prevButton = new Button("◀");
        Button nextButton = new Button("▶");
        Label monthLabel = new Label();
        monthLabel.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

        GridPane calendarGrid = new GridPane();
        calendarGrid.setAlignment(Pos.TOP_CENTER);

        // Настройка столбцов и строк
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 6);
            calendarGrid.getRowConstraints().add(row);
        }

        // Обработчики событий
        prevButton.setOnAction(e -> updateMonth(-1, monthLabel, calendarGrid));
        nextButton.setOnAction(e -> updateMonth(1, monthLabel, calendarGrid));

        controlPanel.getChildren().addAll(prevButton, monthLabel, nextButton);
        mainContainer.getChildren().addAll(controlPanel, calendarGrid);
        updateCalendar(monthLabel, calendarGrid);

        return mainContainer;
    }

    private void updateMonth(int monthsToAdd, Label monthLabel, GridPane grid) {
        currentDate = currentDate.plusMonths(monthsToAdd);
        updateCalendar(monthLabel, grid);
    }

    private void updateCalendar(Label monthLabel, GridPane grid) {
        grid.getChildren().clear();
        monthLabel.setText(currentDate.getMonth() + " " + currentDate.getYear());

        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentDate.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = new Button(String.valueOf(day));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayButton.setStyle(
                    "-fx-background-color: rgba(245,245,245,0.9);" +
                            "-fx-text-fill: #333;" +
                            "-fx-background-radius: 10;"
            );

            // Стиль для текущего дня
            if (currentDate.withDayOfMonth(day).equals(LocalDate.now())) {
                dayButton.setStyle(
                        "-fx-background-color: #90EE90; -fx-font-weight: bold; -fx-background-radius: 10;"
                );
            }

            int position = startDay + day - 1;
            int row = position / 7;
            int column = position % 7;

            grid.add(dayButton, column, row);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}