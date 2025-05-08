package Entity;

public class Commentaire {
    private int id;
    private String content;
    private String author;
    private String date;
    private int articleId;

    public Commentaire(int id, String content, String author, String date, int articleId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.date = date;
        this.articleId = articleId;
    }

    public Commentaire() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    // toString method for easy representation
    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +  // Adjusted to match String date
                ", articleId=" + articleId +
                '}';
    }
}
