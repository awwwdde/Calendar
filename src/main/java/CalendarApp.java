import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarApp extends Application {

    private LocalDate currentDate;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        currentDate = LocalDate.now();

        VBox root = new VBox();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(createMonthView(currentDate));

        Button prevButton = new Button("< Previous");
        Button nextButton = new Button("Next >");

        prevButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            scrollPane.setContent(createMonthView(currentDate));
        });

        nextButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            scrollPane.setContent(createMonthView(currentDate));
        });

        HBox buttonBox = new HBox(10, prevButton, nextButton);
        root.getChildren().addAll(buttonBox, scrollPane);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Календарь");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createMonthView(LocalDate date) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Заголовок месяца
        Label monthLabel = new Label(date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()) + " " + date.getYear());
        monthLabel.setFont(new Font(20));
        grid.add(monthLabel, 0, 0, 7, 1);

        // Дни недели
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i].getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()));
            grid.add(dayLabel, i, 1);
        }

        // Заполнение дней месяца
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, ..., 7 = Sunday
        int dayCounter = 1;

        for (int i = 0; i < 6; i++) { // 6 строк для дней
            for (int j = 0; j < 7; j++) { // 7 дней в неделе
                if (i == 0 && j < dayOfWeekValue - 1) {
                    // Пустые ячейки до первого дня месяца
                    grid.add(new Label(""), j, i + 2);
                } else if (dayCounter <= firstOfMonth.lengthOfMonth()) {
                    Label dayLabel = new Label(String.valueOf(dayCounter));
                    if (dayCounter == currentDate.getDayOfMonth() && date.getMonth() == currentDate.getMonth()) {
                        dayLabel.setTextFill(Color.BLUE); // Change text color to blue for the current day
                    }
                    int finalDayCounter = dayCounter;
                    dayLabel.setOnMouseClicked(event -> openTimeTable(finalDayCounter, date));
                    grid.add(dayLabel, j, i + 2);
                    dayCounter++;
                }
            }
        }


        Line currentTimeLine = new Line();
        currentTimeLine.setStroke(Color.RED);
        currentTimeLine.setStrokeWidth(2);
        grid.add(currentTimeLine, LocalDate.now().getDayOfMonth() - 1, 2); // Adjust position as needed

        // Update current time line every minute
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.minutes(1), e -> updateCurrentTimeLine(currentTimeLine)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        return grid;
    }


    private void openTimeTable(int day, LocalDate date) {
        Stage timeTableStage = new Stage();
        VBox timeTableLayout = new VBox();
        Label titleLabel = new Label("Выберите время для " + day + " " + date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()));
        timeTableLayout.getChildren().add(titleLabel);

        // Create time slots
        for (int hour = 0; hour < 24; hour++) {
            Label timeSlot = new Label(String.format("%02d:00 - %02d:00", hour, hour + 1));
            timeTableLayout.getChildren().add(timeSlot);
        }

        Scene timeTableScene = new Scene(timeTableLayout, 200, 300);
        timeTableStage.setScene(timeTableScene);
        timeTableStage.setTitle("Таблица времени");
        timeTableStage.show();

        // Close on ESC or arrow key
        timeTableScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE:
                case LEFT:
                case RIGHT:
                    timeTableStage.close();
                    break;
                default:
                    break;
            }
        });
    }

    private void updateCurrentTimeLine(Line line) {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        double position = (minute / 60.0) * 100; // Example calculation for position
        line.setStartX(position);
        line.setEndX(position);
    }
}