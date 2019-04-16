package kubsu.timetable.Model;

public class ItemNew {
    private String tittle;
    private String date;
    private String imgURL;
    private String newsURL;

    public ItemNew(String tittle, String date, String imgURL, String newsURL) {
        this.tittle = tittle;
        this.date = date;
        this.imgURL = imgURL;
        this.newsURL = newsURL;
    }

    public String getTittle() {

        return tittle;
    }

    public String getDate() {
        return date;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getNewsURL() {
        return newsURL;
    }
}
