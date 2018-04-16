package osama.ned.society.Others;

public class Event {

    public String imageURI;
    public String name;
    public String description;
    public String date;
    public String time;
    public String venue;
    public String tag1;
    public String tag2;

    public Event(){

    }

    public Event(String imageURI, String name, String description, String date, String venue, String time, String tag1, String tag2) {
        this.imageURI = imageURI;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.tag1 = tag1;
        this.tag2 = tag2;
    }

    public String getImageResource() {
        return imageURI;
    }

    public String getName() {
        return name;
    }

    public String getDescription() { return description; }

    public String getDate() { return date; }

    public String getEventTime() { return time; }

    public String getVenue() {
        return venue;
    }

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }
}
