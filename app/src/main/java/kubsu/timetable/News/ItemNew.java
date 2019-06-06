package kubsu.timetable.News;

public class ItemNew {
    private String tittle;
    private String date;
    private String imgURL;
    private String newsURL;

    ItemNew(String tittle, String date, String imgURL, String newsURL) {
        this.tittle = tittle;
        this.date = date;
        this.imgURL = imgURL;
        this.newsURL = newsURL;
    }

    String getTittle() {

        return tittle;
    }

    public String getDate() {
        return date;
    }

    String getImgURL() {
        return imgURL;
    }

    String getNewsURL() {
        return newsURL;
    }
}
