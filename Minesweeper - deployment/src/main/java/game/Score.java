package game;

class Score{
    String name;
    double time;
    String mode;

    public Score(String name, String time, String mode)
    {
        this.mode = mode;
        this.name = name;
        this.name.replaceAll("\\s", "_");
        this.time = Double.parseDouble(time);
    }
}