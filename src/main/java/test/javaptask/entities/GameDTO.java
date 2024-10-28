package test.javaptask.entities;

public class GameDTO {
    private int points;
    private Colour chosenColour;

    public GameDTO(Colour chosenColour) {
        this.points = 0;
        this.chosenColour = chosenColour;
    }

    public void incrementPoints() {
        points++;
    }

    public int getPoints() {
        return points;
    }

    public Colour getColour() {
        return chosenColour;
    }
}
